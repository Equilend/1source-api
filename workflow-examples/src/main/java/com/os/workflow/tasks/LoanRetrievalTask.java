package com.os.workflow.tasks;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.WebClient;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.os.client.model.Loan;
import com.os.client.model.FeeRate;
import com.os.client.model.FixedRate;
import com.os.client.model.FloatingRate;
import com.os.client.model.PartyRole;
import com.os.client.model.RebateRate;
import com.os.client.model.TransactingParties;
import com.os.client.model.TransactingParty;
import com.os.workflow.AuthToken;
import com.os.workflow.LocalDateTypeGsonAdapter;
import com.os.workflow.OffsetDateTimeTypeGsonAdapter;
import com.os.workflow.WorkflowConfig;

import reactor.core.publisher.Mono;

public class LoanRetrievalTask implements Tasklet, StepExecutionListener {

	private static final Logger logger = LoggerFactory.getLogger(LoanRetrievalTask.class);

	private AuthToken ledgerToken;
	private Loan loan;
	
	@Autowired
	WebClient restWebClient;

	@Autowired
	WorkflowConfig workflowConfig;

	@Override
	public void beforeStep(StepExecution stepExecution) {
		ledgerToken = (AuthToken) stepExecution.getJobExecution().getExecutionContext().get("ledgerToken");
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		
		loan = restWebClient.get().uri("/loans/" + workflowConfig.getLoan_id())
				.headers(h -> h.setBearerAuth(ledgerToken.getAccess_token())).retrieve()
				.onStatus(HttpStatusCode.valueOf(404)::equals, response -> {
					logger.error(HttpStatus.NOT_FOUND.toString());
					return Mono.empty();
				}).bodyToMono(Loan.class).block();

		Gson gson = new GsonBuilder()
			    .registerTypeAdapter(LocalDate.class, new LocalDateTypeGsonAdapter())
			    .registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeTypeGsonAdapter())
			    .create();

		logger.debug(gson.toJson(loan));

		logger.debug(loan.toString());

		if (loan == null || loan.getTrade() == null) {
			logger.warn("Invalid loan object or loan not found");
		} else {
			if (loan.isIsInitiator() == null) {
				logger.warn("Loan initiator unknown");
			} else if (loan.isIsInitiator()) {
				logger.debug("My party is initiator: " + loan.getLoanId());
			}

			String myBorrowLoan = null;
			String counterparty = null;

			if (loan.getTrade().getTransactingParties() == null
					|| loan.getTrade().getTransactingParties().size() == 0) {
				logger.warn("Transacting party information missing: " + loan);
			} else {
				TransactingParties transactingParties = loan.getTrade().getTransactingParties();
				for (TransactingParty party : transactingParties) {
					if (PartyRole.BORROWER.equals(party.getPartyRole())) {
						if (party.getParty().getPartyId().equals(workflowConfig.getParty_id())) {
							myBorrowLoan = PartyRole.BORROWER.getValue();
						} else {
							counterparty = party.getParty().getPartyId();
						}
					} else if (PartyRole.LENDER.equals(party.getPartyRole())) {
						if (party.getParty().getPartyId().equals(workflowConfig.getParty_id())) {
							myBorrowLoan = PartyRole.LENDER.getValue();
						} else {
							counterparty = party.getParty().getPartyId();
						}
					}
				}
			}
			logger.debug(myBorrowLoan + ":" + counterparty);
			
			
			if (loan.getTrade().getQuantity() == null) {
				logger.warn("Quantity information missing");
			}

			if (loan.getTrade().getInstrument() == null || loan.getTrade().getInstrument().getFigi() == null) {
				logger.warn("Instrument FIGI missing");
			}

			if (loan.getTrade().getRate() == null) {
				logger.warn("Rate information missing");
			}

			if (loan.getTrade().getTradeDate() == null) {
				logger.warn("Trade date information missing");
			}

			if (myBorrowLoan == null) {
				logger.warn("Not a transacting party");
			} else {
//				String figi = loan.getTrade().getInstrument().getFigi();
//				Long quantity = loan.getTrade().getQuantity().longValue();
				BigDecimal rate = null;
				if (loan.getTrade().getRate() instanceof FeeRate) {
					rate = BigDecimal.valueOf(((FeeRate) loan.getTrade().getRate()).getFee().getBaseRate());
				} else if (loan.getTrade().getRate() instanceof RebateRate) {
					if (((RebateRate) loan.getTrade().getRate()).getRebate() instanceof FixedRate) {
						rate = BigDecimal
								.valueOf(((FixedRate) (((RebateRate) loan.getTrade().getRate()).getRebate()))
										.getFixed().getBaseRate());
					} else if (((RebateRate) loan.getTrade().getRate()).getRebate() instanceof FloatingRate) {
						rate = BigDecimal
								.valueOf(((FloatingRate) (((RebateRate) loan.getTrade().getRate()).getRebate()))
										.getFloating().getEffectiveRate());
					}
				}

				if (rate == null) {
					logger.warn("Could not find base rate.");
				}
			}
		}

		return RepeatStatus.FINISHED;
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		stepExecution.getJobExecution().getExecutionContext().put("loan", this.loan);
		return ExitStatus.COMPLETED;
	}

}
