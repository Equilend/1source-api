package com.os.replay.tasks;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.WebClient;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.os.client.model.Event;
import com.os.client.model.Events;
import com.os.client.model.FeeRate;
import com.os.client.model.FixedRate;
import com.os.client.model.FloatingRate;
import com.os.client.model.Loan;
import com.os.client.model.PartyRole;
import com.os.client.model.RebateRate;
import com.os.client.model.TransactingParties;
import com.os.client.model.TransactingParty;
import com.os.replay.ReplayDBDao;
import com.os.replay.model.AuthToken;
import com.os.replay.model.LedgerException;
import com.os.replay.model.LedgerRecord;
import com.os.replay.util.LoanProposalUtil;
import com.os.replay.util.LocalDateTypeAdapter;
import com.os.replay.util.OffsetDateTimeTypeAdapter;

import reactor.core.publisher.Mono;

public class Day1LedgerBTask extends RecordReader implements Tasklet, StepExecutionListener {

	private static final Logger logger = LoggerFactory.getLogger(Day1LedgerBTask.class);

	private AuthToken ledgerBToken;
	private String ledgerBParty;
	private String ledgerBName;
	//private LocalDate ledgerBStartDate;

	@Autowired
	ReplayDBDao dao;

	@Autowired
	WebClient restWebClient;

	@Override
	public void beforeStep(StepExecution stepExecution) {
		ledgerBToken = (AuthToken) stepExecution.getJobExecution().getExecutionContext().get("ledgerBToken");
		ledgerBParty = (String) stepExecution.getJobExecution().getExecutionContext().get("ledgerBParty");
		ledgerBName = (String) stepExecution.getJobExecution().getExecutionContext().get("ledgerBName");
		//ledgerBStartDate = (LocalDate) stepExecution.getJobExecution().getExecutionContext().get("ledgerBStartDate");
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

		try {
			// 1. Query for new loan proposal events since startDate
			Events loanProposalEvents = restWebClient.get()
					.uri("/events?since=" + LocalDate.now().atStartOfDay().format(DateTimeFormatter.ISO_DATE)
							+ "T00:00:00.000000Z&eventType=LOAN_PROPOSED")
					.headers(h -> h.setBearerAuth(ledgerBToken.getAccess_token())).retrieve()
					.onStatus(HttpStatusCode::is4xxClientError, error -> {
						return Mono.error(new LedgerException(error.statusCode().toString()));
					}).bodyToMono(Events.class).block();

			LoanProposalUtil loanUtil = new LoanProposalUtil();

			if (loanProposalEvents != null && loanProposalEvents.size() > 0) {
				Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())
						.registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeTypeAdapter()).create();

				for (Event event : loanProposalEvents) {
					String osLoanId = loanUtil.parseResourceUri(event.getResourceUri());

					Loan loan = restWebClient.get().uri("/loans/" + osLoanId)
							.headers(h -> h.setBearerAuth(ledgerBToken.getAccess_token())).retrieve()
							.onStatus(HttpStatusCode::is4xxClientError, response -> {
								return Mono.empty();
							}).bodyToMono(Loan.class).block();

					logger.debug(gson.toJson(loan));

					if (loan == null || loan.getTrade() == null) {
						logger.warn("Invalid loan object: " + loan);
					} else if (loan.isIsInitiator()) {
						// My party is the initiator. Nothing to do
						logger.warn("Ignoring loan proposal as initiator: " + loan.getLoanId());
					} else if (loan.getTrade().getTransactingParties() == null
							|| loan.getTrade().getTransactingParties().size() == 0) {
						logger.warn("Transacting party information missing: " + loan);
					} else if (loan.getTrade().getQuantity() == null) {
						logger.warn("Quantity information missing: " + loan);
					} else if (loan.getTrade().getInstrument() == null
							|| loan.getTrade().getInstrument().getFigi() == null) {
						logger.warn("Instrument FIGI missing: " + loan);
					} else if (loan.getTrade().getRate() == null) {
						logger.warn("Rate information missing: " + loan);
					} else if (loan.getTrade().getTradeDate() == null) {
						logger.warn("Trade date information missing: " + loan);
					} else {
						String myBorrowLoan = null;
						String counterparty = null;
						TransactingParties transactingParties = loan.getTrade().getTransactingParties();
						for (TransactingParty party : transactingParties) {
							if (PartyRole.BORROWER.equals(party.getPartyRole())) {
								if (party.getParty().getPartyId().equals(ledgerBParty)) {
									myBorrowLoan = PartyRole.BORROWER.getValue();
								} else {
									counterparty = party.getParty().getPartyId();
								}
							} else if (PartyRole.LENDER.equals(party.getPartyRole())) {
								if (party.getParty().getPartyId().equals(ledgerBParty)) {
									myBorrowLoan = PartyRole.LENDER.getValue();
								} else {
									counterparty = party.getParty().getPartyId();
								}
							}

						}
						if (myBorrowLoan == null) {
							logger.warn("Not a transacting party. Skipping: " + loan);
						} else {
							String figi = loan.getTrade().getInstrument().getFigi();
							Long quantity = loan.getTrade().getQuantity().longValue();
							BigDecimal rate = null;
							if (loan.getTrade().getRate() instanceof FeeRate) {
								rate = BigDecimal
										.valueOf(((FeeRate) loan.getTrade().getRate()).getFee().getBaseRate());
							} else if (loan.getTrade().getRate() instanceof RebateRate) {
								if (((RebateRate) loan.getTrade().getRate()).getRebate() instanceof FixedRate) {
									rate = BigDecimal.valueOf(
											((FixedRate) (((RebateRate) loan.getTrade().getRate()).getRebate()))
													.getFixed().getBaseRate());
								} else if (((RebateRate) loan.getTrade().getRate())
										.getRebate() instanceof FloatingRate) {
									rate = BigDecimal.valueOf(
											((FloatingRate) (((RebateRate) loan.getTrade().getRate()).getRebate()))
													.getFloating().getEffectiveRate());
								}
							}

							if (rate == null) {
								logger.warn("Could not find base rate. Skipping: " + loan);
							} else {
								// Finally time to look for this loan
								List<LedgerRecord> ledgerRecords = dao.getLedgerRecords(loan.getTrade().getTradeDate(), ledgerBName, myBorrowLoan, ledgerBParty, counterparty, figi, quantity, rate.multiply(new BigDecimal(100)));
								//If there happen to be more than 1 that's fine. Maybe there were multiple identical loans. Pick the first one that doesn't have a 1Source loan id
								if (ledgerRecords == null || ledgerRecords.size() == 0) {
									logger.warn("Could not find a matching loan: " + loan);
								} else {
									for (LedgerRecord ledgerRecord : ledgerRecords) {
										if (ledgerRecord.getOneSourceLoanId() == null) {
											dao.updateOSLoanId(ledgerBName, ledgerRecord.getInternalRefId(), loan.getLoanId());
											logger.info("Found a match. Updating " + ledgerRecord.getInternalRefId() + ": " + loan.getLoanId());
											break;
										}
									}
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			if (e.getCause() instanceof com.os.replay.model.LedgerException) {
				logger.error("Error processing day 1 events: " + e.getMessage());
			} else {
				throw e;
			}
		}

		return RepeatStatus.FINISHED;
	}

}
