package com.os.workflow.tasks;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.os.workflow.WorkflowConfig;
import com.os.workflow.AuthToken;
import io.swagger.v1_0_5_20240428.client.model.Contract;
import io.swagger.v1_0_5_20240428.client.model.FeeRate;
import io.swagger.v1_0_5_20240428.client.model.FixedRate;
import io.swagger.v1_0_5_20240428.client.model.FloatingRate;
import io.swagger.v1_0_5_20240428.client.model.LocalDateTypeAdapter;
import io.swagger.v1_0_5_20240428.client.model.OffsetDateTimeTypeAdapter;
import io.swagger.v1_0_5_20240428.client.model.PartyRole;
import io.swagger.v1_0_5_20240428.client.model.RebateRate;
import io.swagger.v1_0_5_20240428.client.model.TransactingParties;
import io.swagger.v1_0_5_20240428.client.model.TransactingParty;
import reactor.core.publisher.Mono;

public class ContractRetrievalTask implements Tasklet, StepExecutionListener {

	private static final Logger logger = LoggerFactory.getLogger(ContractRetrievalTask.class);

	private AuthToken ledgerToken;

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

		Contract contract = restWebClient.get().uri("/contracts/" + workflowConfig.getContract_id())
				.headers(h -> h.setBearerAuth(ledgerToken.getAccess_token())).retrieve()
				.onStatus(HttpStatusCode.valueOf(404)::equals, response -> {
					logger.error(HttpStatus.NOT_FOUND.toString());
					return Mono.empty();
				}).bodyToMono(Contract.class).block();

		Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())
				.registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeTypeAdapter()).create();

		logger.debug(gson.toJson(contract));

		logger.debug(contract.toString());

		if (contract == null || contract.getTrade() == null) {
			logger.warn("Invalid contract object or contract not found");
		} else {
			if (contract.isIsInitiator() == null) {
				logger.warn("Contract initiator unknown");
			} else if (contract.isIsInitiator()) {
				logger.debug("My party is initiator: " + contract.getContractId());
			}

			String myBorrowLoan = null;
			String counterparty = null;

			if (contract.getTrade().getTransactingParties() == null
					|| contract.getTrade().getTransactingParties().size() == 0) {
				logger.warn("Transacting party information missing: " + contract);
			} else {
				TransactingParties transactingParties = contract.getTrade().getTransactingParties();
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
			if (contract.getTrade().getQuantity() == null) {
				logger.warn("Quantity information missing");
			}

			if (contract.getTrade().getInstrument() == null || contract.getTrade().getInstrument().getFigi() == null) {
				logger.warn("Instrument FIGI missing");
			}

			if (contract.getTrade().getRate() == null) {
				logger.warn("Rate information missing");
			}

			if (contract.getTrade().getTradeDate() == null) {
				logger.warn("Trade date information missing");
			}

			if (myBorrowLoan == null) {
				logger.warn("Not a transacting party");
			} else {
				String figi = contract.getTrade().getInstrument().getFigi();
				Long quantity = contract.getTrade().getQuantity().longValue();
				BigDecimal rate = null;
				if (contract.getTrade().getRate() instanceof FeeRate) {
					rate = BigDecimal.valueOf(((FeeRate) contract.getTrade().getRate()).getFee().getBaseRate());
				} else if (contract.getTrade().getRate() instanceof RebateRate) {
					if (((RebateRate) contract.getTrade().getRate()).getRebate() instanceof FixedRate) {
						rate = BigDecimal
								.valueOf(((FixedRate) (((RebateRate) contract.getTrade().getRate()).getRebate()))
										.getFixed().getBaseRate());
					} else if (((RebateRate) contract.getTrade().getRate()).getRebate() instanceof FloatingRate) {
						rate = BigDecimal
								.valueOf(((FloatingRate) (((RebateRate) contract.getTrade().getRate()).getRebate()))
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

}