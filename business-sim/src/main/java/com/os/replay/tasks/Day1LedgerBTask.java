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
import com.os.replay.ReplayDBDao;
import com.os.replay.model.AuthToken;
import com.os.replay.model.LedgerException;
import com.os.replay.model.LedgerRecord;
import com.os.replay.util.ContractProposalUtil;
import com.os.replay.util.LocalDateTypeAdapter;
import com.os.replay.util.OffsetDateTimeTypeAdapter;

import io.swagger.client.model.Contract;
import io.swagger.client.model.Event;
import io.swagger.client.model.Events;
import io.swagger.client.model.FeeRate;
import io.swagger.client.model.FixedRate;
import io.swagger.client.model.FloatingRate;
import io.swagger.client.model.PartyRole;
import io.swagger.client.model.RebateRate;
import io.swagger.client.model.TransactingParties;
import io.swagger.client.model.TransactingParty;
import reactor.core.publisher.Mono;

public class Day1LedgerBTask extends RecordReader implements Tasklet, StepExecutionListener {

	private static final Logger logger = LoggerFactory.getLogger(Day1LedgerBTask.class);

	private AuthToken ledgerBToken;
	private String ledgerBParty;
	private String ledgerBName;
	private LocalDate ledgerBStartDate;

	@Autowired
	ReplayDBDao dao;

	@Autowired
	WebClient restWebClient;

	@Override
	public void beforeStep(StepExecution stepExecution) {
		ledgerBToken = (AuthToken) stepExecution.getJobExecution().getExecutionContext().get("ledgerBToken");
		ledgerBParty = (String) stepExecution.getJobExecution().getExecutionContext().get("ledgerBParty");
		ledgerBName = (String) stepExecution.getJobExecution().getExecutionContext().get("ledgerBName");
		ledgerBStartDate = (LocalDate) stepExecution.getJobExecution().getExecutionContext().get("ledgerBStartDate");
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

		try {
			// 1. Query for new contract proposal events since startDate
			Events contractProposalEvents = restWebClient.get()
					.uri("/events?since=" + LocalDate.now().atStartOfDay().format(DateTimeFormatter.ISO_DATE)
							+ "T00:00:00.000000Z&eventType=CONTRACT_PROPOSED")
					.headers(h -> h.setBearerAuth(ledgerBToken.getAccess_token())).retrieve()
					.onStatus(HttpStatusCode::is4xxClientError, error -> {
						return Mono.error(new LedgerException(error.statusCode().toString()));
					}).bodyToMono(Events.class).block();

			ContractProposalUtil contractUtil = new ContractProposalUtil();

			if (contractProposalEvents != null && contractProposalEvents.size() > 0) {
				Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())
						.registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeTypeAdapter()).create();

				for (Event event : contractProposalEvents) {
					String osContractId = contractUtil.parseResourceUri(event.getResourceUri());

					Contract contract = restWebClient.get().uri("/contracts/" + osContractId)
							.headers(h -> h.setBearerAuth(ledgerBToken.getAccess_token())).retrieve()
							.onStatus(HttpStatusCode::is4xxClientError, response -> {
								return Mono.empty();
							}).bodyToMono(Contract.class).block();

					logger.debug(gson.toJson(contract));

					if (contract == null || contract.getTrade() == null) {
						logger.warn("Invalid contract object: " + contract);
					} else if (contract.isIsInitiator()) {
						// My party is the initiator. Nothing to do
						logger.warn("Ignoring contract proposal as initiator: " + contract.getContractId());
					} else if (contract.getTrade().getTransactingParties() == null
							|| contract.getTrade().getTransactingParties().size() == 0) {
						logger.warn("Transacting party information missing: " + contract);
					} else if (contract.getTrade().getQuantity() == null) {
						logger.warn("Quantity information missing: " + contract);
					} else if (contract.getTrade().getInstrument() == null
							|| contract.getTrade().getInstrument().getFigi() == null) {
						logger.warn("Instrument FIGI missing: " + contract);
					} else if (contract.getTrade().getRate() == null) {
						logger.warn("Rate information missing: " + contract);
					} else if (contract.getTrade().getTradeDate() == null) {
						logger.warn("Trade date information missing: " + contract);
					} else {
						String myBorrowLoan = null;
						String counterparty = null;
						TransactingParties transactingParties = contract.getTrade().getTransactingParties();
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
							logger.warn("Not a transacting party. Skipping: " + contract);
						} else {
							String figi = contract.getTrade().getInstrument().getFigi();
							Long quantity = contract.getTrade().getQuantity().longValue();
							BigDecimal rate = null;
							if (contract.getTrade().getRate() instanceof FeeRate) {
								rate = BigDecimal
										.valueOf(((FeeRate) contract.getTrade().getRate()).getFee().getBaseRate());
							} else if (contract.getTrade().getRate() instanceof RebateRate) {
								if (((RebateRate) contract.getTrade().getRate()).getRebate() instanceof FixedRate) {
									rate = BigDecimal.valueOf(
											((FixedRate) (((RebateRate) contract.getTrade().getRate()).getRebate()))
													.getFixed().getBaseRate());
								} else if (((RebateRate) contract.getTrade().getRate())
										.getRebate() instanceof FloatingRate) {
									rate = BigDecimal.valueOf(
											((FloatingRate) (((RebateRate) contract.getTrade().getRate()).getRebate()))
													.getFloating().getEffectiveRate());
								}
							}

							if (rate == null) {
								logger.warn("Could not find base rate. Skipping: " + contract);
							} else {
								// Finally time to look for this contract
								List<LedgerRecord> ledgerRecords = dao.getLedgerRecords(contract.getTrade().getTradeDate(), ledgerBName, myBorrowLoan, ledgerBParty, counterparty, figi, quantity, rate.multiply(new BigDecimal(100)));
								//If there happen to be more than 1 that's fine. Maybe there were multiple identical loans. Pick the first one that doesn't have a 1Source contract id
								if (ledgerRecords == null || ledgerRecords.size() == 0) {
									logger.warn("Could not find a matching contract: " + contract);
								} else {
									for (LedgerRecord ledgerRecord : ledgerRecords) {
										if (ledgerRecord.getOneSourceContractId() == null) {
											dao.updateOSContractId(ledgerBName, ledgerRecord.getInternalRefId(), contract.getContractId());
											logger.info("Found a match. Updating " + ledgerRecord.getInternalRefId() + ": " + contract.getContractId());
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
