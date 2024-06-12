package com.os.workflow.tasks;

import java.util.Date;

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
import com.os.workflow.AuthToken;
import com.os.workflow.DateGsonTypeAdapter;
import com.os.workflow.WorkflowConfig;

import io.swagger.v1_0_5_20240611.client.model.Rerates;
import reactor.core.publisher.Mono;

public class RerateQueryTask implements Tasklet, StepExecutionListener {

	private static final Logger logger = LoggerFactory.getLogger(RerateQueryTask.class);

	private AuthToken ledgerToken;
	private Rerates rerates;
	
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

		rerates = restWebClient.get().uri("/contracts/" + workflowConfig.getContract_id() + "/rerates")
				.headers(h -> h.setBearerAuth(ledgerToken.getAccess_token())).retrieve()
				.onStatus(HttpStatusCode.valueOf(404)::equals, response -> {
					logger.error(HttpStatus.NOT_FOUND.toString());
					return Mono.empty();
				}).bodyToMono(Rerates.class).block();

		Gson gson = new GsonBuilder()
			    .registerTypeAdapter(Date.class, new DateGsonTypeAdapter())
			    .create();

		logger.debug(gson.toJson(rerates));

		logger.debug(rerates.toString());

		if (rerates == null || rerates.size() == 0) {
			logger.warn("Invalid rerates object or rerates not found");
		}
		
		return RepeatStatus.FINISHED;
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		if (this.rerates.size() > 0) {
			stepExecution.getJobExecution().getExecutionContext().put("rerate", this.rerates.get(0));
		}
		return ExitStatus.COMPLETED;
	}

}
