package com.os.workflow.tasks;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.Random;

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
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.os.workflow.AuthToken;
import com.os.workflow.DateGsonTypeAdapter;
import com.os.workflow.WorkflowConfig;

import io.swagger.v1_0_5_20240611.client.model.Contract;
import io.swagger.v1_0_5_20240611.client.model.FeeRate;
import io.swagger.v1_0_5_20240611.client.model.FixedRate;
import io.swagger.v1_0_5_20240611.client.model.FloatingRate;
import io.swagger.v1_0_5_20240611.client.model.LedgerResponse;
import io.swagger.v1_0_5_20240611.client.model.RebateRate;
import io.swagger.v1_0_5_20240611.client.model.RerateProposal;
import reactor.core.publisher.Mono;

public class RerateProposalTask implements Tasklet, StepExecutionListener {

	private static final Logger logger = LoggerFactory.getLogger(RerateProposalTask.class);

	private AuthToken ledgerToken;
	private Contract contract;

	@Autowired
	WebClient restWebClient;

	@Autowired
	WorkflowConfig workflowConfig;

	@Override
	public void beforeStep(StepExecution stepExecution) {
		ledgerToken = (AuthToken) stepExecution.getJobExecution().getExecutionContext().get("ledgerToken");
		contract = (Contract) stepExecution.getJobExecution().getExecutionContext().get("contract");
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

		RerateProposal rerateProposal = new RerateProposal();

		LocalDate rerateDate = LocalDate.now();
		Random random = new Random();
		
		if (contract.getTrade().getRate() instanceof FeeRate) {
			
			BigDecimal rate = BigDecimal.valueOf(((FeeRate) contract.getTrade().getRate()).getFee().getBaseRate());
			
			BigDecimal rerate = rate.add(new BigDecimal(random.nextInt(8) + 1));
			
			((FeeRate) contract.getTrade().getRate()).getFee().setBaseRate(rerate.doubleValue());

			rerateProposal.setRate(((FeeRate) contract.getTrade().getRate()));

			logger.debug("Original Fee Rate: " + rate.toPlainString() + " Rerate: " + rerate.toPlainString());
			
		} else if (contract.getTrade().getRate() instanceof RebateRate) {
			
			if (((RebateRate) contract.getTrade().getRate()).getRebate() instanceof FixedRate) {
			
				BigDecimal rate = BigDecimal
						.valueOf(((FixedRate) (((RebateRate) contract.getTrade().getRate()).getRebate()))
								.getFixed().getBaseRate());
				
				BigDecimal rerate = rate.add(new BigDecimal(random.nextInt(8) + 1));
				((FixedRate)((RebateRate) contract.getTrade().getRate()).getRebate()).getFixed().setBaseRate(rerate.doubleValue());

				rerateProposal.setRate(((RebateRate) contract.getTrade().getRate()));

				logger.debug("Original Rebate Fixed Rate: " + rate.toPlainString() + " Rerate: " + rerate.toPlainString());

			} else if (((RebateRate) contract.getTrade().getRate()).getRebate() instanceof FloatingRate) {
				
				BigDecimal rate = BigDecimal
						.valueOf(((FloatingRate) (((RebateRate) contract.getTrade().getRate()).getRebate()))
								.getFloating().getSpread());

				BigDecimal rerate = rate.add(new BigDecimal(random.nextInt(8) + 1));

				((FloatingRate)((RebateRate) contract.getTrade().getRate()).getRebate()).getFloating().setSpread(rerate.doubleValue());

				rerateProposal.setRate(((RebateRate) contract.getTrade().getRate()));

				logger.debug("Original Rebate Floating Rate: " + rate.toPlainString() + " Rerate: " + rerate.toPlainString());
			}
		}

		Gson gson = new GsonBuilder()
			    .registerTypeAdapter(Date.class, new DateGsonTypeAdapter())
			    .create();

		String json = gson.toJson(rerateProposal);
		logger.debug(json);

		LedgerResponse ledgerResponse = restWebClient.post().uri("/contracts/" + workflowConfig.getContract_id() + "/rerates").contentType(MediaType.APPLICATION_JSON)
				.bodyValue(json).headers(h -> h.setBearerAuth(ledgerToken.getAccess_token())).retrieve()
				.onStatus(HttpStatusCode::is4xxClientError, response -> {
					return Mono.empty();
				}).bodyToMono(LedgerResponse.class).block();

		logger.debug("Ledger Response: " + ledgerResponse);

		return RepeatStatus.FINISHED;
	}

}
