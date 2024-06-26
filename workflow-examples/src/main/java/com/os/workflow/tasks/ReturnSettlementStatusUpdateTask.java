package com.os.workflow.tasks;

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
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.os.client.model.LedgerResponse;
import com.os.client.model.SettlementStatus;
import com.os.client.model.SettlementStatusUpdate;
import com.os.workflow.AuthToken;
import com.os.workflow.LocalDateTypeGsonAdapter;
import com.os.workflow.OffsetDateTimeTypeGsonAdapter;
import com.os.workflow.WorkflowConfig;

import reactor.core.publisher.Mono;

public class ReturnSettlementStatusUpdateTask implements Tasklet, StepExecutionListener {

	private static final Logger logger = LoggerFactory.getLogger(ReturnSettlementStatusUpdateTask.class);

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

		SettlementStatusUpdate update = new SettlementStatusUpdate();
		
		update.setSettlementStatus(SettlementStatus.SETTLED);

		Gson gson = new GsonBuilder()
			    .registerTypeAdapter(LocalDate.class, new LocalDateTypeGsonAdapter())
			    .registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeTypeGsonAdapter())
			    .create();

		String json = gson.toJson(update);
		logger.debug(json);

		LedgerResponse ledgerResponse = restWebClient.patch().uri("/contracts/" + workflowConfig.getContract_id() + "/returns/" + workflowConfig.getReturn_id()).contentType(MediaType.APPLICATION_JSON)
				.bodyValue(json).headers(h -> h.setBearerAuth(ledgerToken.getAccess_token())).retrieve()
				.onStatus(HttpStatusCode::is4xxClientError, response -> {
					return Mono.empty();
				}).bodyToMono(LedgerResponse.class).block();

		logger.debug("Ledger Response: " + ledgerResponse);

		return RepeatStatus.FINISHED;
	}

}
