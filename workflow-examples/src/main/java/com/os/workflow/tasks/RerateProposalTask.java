package com.os.workflow.tasks;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
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
import com.os.client.model.Loan;
import com.os.client.model.FeeRate;
import com.os.client.model.FixedRate;
import com.os.client.model.FloatingRate;
import com.os.client.model.LedgerResponse;
import com.os.client.model.RebateRate;
import com.os.client.model.RerateProposal;
import com.os.workflow.AuthToken;
import com.os.workflow.LocalDateTypeGsonAdapter;
import com.os.workflow.OffsetDateTimeTypeGsonAdapter;
import com.os.workflow.WorkflowConfig;

import reactor.core.publisher.Mono;

public class RerateProposalTask implements Tasklet, StepExecutionListener {

	private static final Logger logger = LoggerFactory.getLogger(RerateProposalTask.class);

	private AuthToken ledgerToken;
	private Loan loan;

	@Autowired
	WebClient restWebClient;

	@Autowired
	WorkflowConfig workflowConfig;

	@Override
	public void beforeStep(StepExecution stepExecution) {
		ledgerToken = (AuthToken) stepExecution.getJobExecution().getExecutionContext().get("ledgerToken");
		loan = (Loan) stepExecution.getJobExecution().getExecutionContext().get("loan");
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

		RerateProposal rerateProposal = new RerateProposal();

		LocalDate rerateDate = LocalDate.now(ZoneId.of("UTC"));
		
		Random random = new Random();
		
		if (loan.getTrade().getRate() instanceof FeeRate) {
			
			BigDecimal rate = BigDecimal.valueOf(((FeeRate) loan.getTrade().getRate()).getFee().getBaseRate());
			
			BigDecimal rerate = rate.add(new BigDecimal(random.nextInt(8) + 1));
			
			((FeeRate) loan.getTrade().getRate()).getFee().setBaseRate(rerate.doubleValue());
			((FeeRate) loan.getTrade().getRate()).getFee().setEffectiveDate(rerateDate);

			rerateProposal.setRate(((FeeRate) loan.getTrade().getRate()));

			logger.debug("Original Fee Rate: " + rate.toPlainString() + " Rerate: " + rerate.toPlainString());
			
		} else if (loan.getTrade().getRate() instanceof RebateRate) {
			
			if (((RebateRate) loan.getTrade().getRate()).getRebate() instanceof FixedRate) {
			
				BigDecimal rate = BigDecimal
						.valueOf(((FixedRate) (((RebateRate) loan.getTrade().getRate()).getRebate()))
								.getFixed().getBaseRate());
				
				BigDecimal rerate = rate.add(new BigDecimal(random.nextInt(8) + 1));
				((FixedRate)((RebateRate) loan.getTrade().getRate()).getRebate()).getFixed().setBaseRate(rerate.doubleValue());
				((FixedRate)((RebateRate) loan.getTrade().getRate()).getRebate()).getFixed().setEffectiveDate(rerateDate);
				
				rerateProposal.setRate(((RebateRate) loan.getTrade().getRate()));

				logger.debug("Original Rebate Fixed Rate: " + rate.toPlainString() + " Rerate: " + rerate.toPlainString());

			} else if (((RebateRate) loan.getTrade().getRate()).getRebate() instanceof FloatingRate) {
				
				BigDecimal rate = BigDecimal
						.valueOf(((FloatingRate) (((RebateRate) loan.getTrade().getRate()).getRebate()))
								.getFloating().getSpread());

				BigDecimal rerate = rate.add(new BigDecimal(random.nextInt(8) + 1));

				((FloatingRate)((RebateRate) loan.getTrade().getRate()).getRebate()).getFloating().setSpread(rerate.doubleValue());
				((FloatingRate)((RebateRate) loan.getTrade().getRate()).getRebate()).getFloating().setEffectiveDate(rerateDate);

				rerateProposal.setRate(((RebateRate) loan.getTrade().getRate()));

				logger.debug("Original Rebate Floating Rate: " + rate.toPlainString() + " Rerate: " + rerate.toPlainString());
			}
		}

		Gson gson = new GsonBuilder()
			    .registerTypeAdapter(LocalDate.class, new LocalDateTypeGsonAdapter())
			    .registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeTypeGsonAdapter())
			    .create();

		String json = gson.toJson(rerateProposal);
		logger.debug(json);

		LedgerResponse ledgerResponse = restWebClient.post().uri("/loans/" + workflowConfig.getLoan_id() + "/rerates").contentType(MediaType.APPLICATION_JSON)
				.bodyValue(json).headers(h -> h.setBearerAuth(ledgerToken.getAccess_token())).retrieve()
				.onStatus(HttpStatusCode::is4xxClientError, response -> {
					return Mono.empty();
				}).bodyToMono(LedgerResponse.class).block();

		logger.debug("Ledger Response: " + ledgerResponse);

		return RepeatStatus.FINISHED;
	}

}
