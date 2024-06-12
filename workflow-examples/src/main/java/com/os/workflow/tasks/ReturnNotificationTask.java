package com.os.workflow.tasks;

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

import com.os.client.model.LedgerResponse;
import com.os.client.model.ReturnProposal;
import com.os.client.model.SettlementType;
import reactor.core.publisher.Mono;

public class ReturnNotificationTask implements Tasklet, StepExecutionListener {

	private static final Logger logger = LoggerFactory.getLogger(ReturnNotificationTask.class);

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

		Random random = new Random();
		
		ReturnProposal proposal = new ReturnProposal();
		
		Date currentDate = new Date();
		
		proposal.setQuantity(((((random.nextInt(1000 - 100) + 1000))+99)/100)*100);
		proposal.setReturnDate(currentDate);
		proposal.setReturnSettlementDate(currentDate);
		proposal.setSettlementType(SettlementType.DVP);
		proposal.setCollateralValue(100000.00);

		Gson gson = new GsonBuilder()
			    .registerTypeAdapter(Date.class, new DateGsonTypeAdapter())
			    .create();

		String json = gson.toJson(proposal);
		logger.debug(json);

		LedgerResponse ledgerResponse = restWebClient.post().uri("/contracts/" + workflowConfig.getContract_id() + "/returns").contentType(MediaType.APPLICATION_JSON)
				.bodyValue(json).headers(h -> h.setBearerAuth(ledgerToken.getAccess_token())).retrieve()
				.onStatus(HttpStatusCode::is4xxClientError, response -> {
					return Mono.empty();
				}).bodyToMono(LedgerResponse.class).block();

		logger.debug("Ledger Response: " + ledgerResponse);

		return RepeatStatus.FINISHED;
	}

}
