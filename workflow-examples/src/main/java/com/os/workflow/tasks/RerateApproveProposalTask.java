package com.os.workflow.tasks;

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

import com.os.client.model.LedgerResponse;
import com.os.workflow.AuthToken;
import com.os.workflow.WorkflowConfig;

import reactor.core.publisher.Mono;

public class RerateApproveProposalTask implements Tasklet, StepExecutionListener {

	private static final Logger logger = LoggerFactory.getLogger(RerateApproveProposalTask.class);

	private AuthToken ledgerToken;
	//private Rerate rerate;

	@Autowired
	WebClient restWebClient;

	@Autowired
	WorkflowConfig workflowConfig;

	@Override
	public void beforeStep(StepExecution stepExecution) {
		ledgerToken = (AuthToken) stepExecution.getJobExecution().getExecutionContext().get("ledgerToken");
		//rerate = (Rerate) stepExecution.getJobExecution().getExecutionContext().get("rerate");
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

		LedgerResponse ledgerResponse = restWebClient.post().uri("/contracts/" + workflowConfig.getContract_id() + "/rerates/" + workflowConfig.getRerate_id() + "/approve").contentType(MediaType.APPLICATION_JSON)
				//.bodyValue(json)
				.headers(h -> h.setBearerAuth(ledgerToken.getAccess_token())).retrieve()
				.onStatus(HttpStatusCode::is4xxClientError, response -> {
					return Mono.empty();
				}).bodyToMono(LedgerResponse.class).block();

		logger.debug("Ledger Response: " + ledgerResponse);

		return RepeatStatus.FINISHED;
	}

}
