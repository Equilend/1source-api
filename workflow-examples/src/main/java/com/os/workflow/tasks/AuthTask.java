package com.os.workflow.tasks;

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
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.os.workflow.AuthConfig;
import com.os.workflow.AuthToken;

public class AuthTask implements Tasklet, StepExecutionListener {

	private static final Logger logger = LoggerFactory.getLogger(AuthTask.class);

	private AuthToken ledgerToken;
	
	@Autowired
	AuthConfig authConfig;
	
	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		
		MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
		formData.add("grant_type", authConfig.getGrant_type());
		formData.add("client_id", authConfig.getClient_id());
		formData.add("username", authConfig.getUsername());
		formData.add("password", authConfig.getPassword());
		formData.add("client_secret", authConfig.getClient_secret());

		WebClient authClient = WebClient.create("https://stageauth.equilend.com");
		
		ledgerToken = authClient.post()
	      .uri("/auth/realms/1Source/protocol/openid-connect/token")
	      .contentType(MediaType.APPLICATION_FORM_URLENCODED)
	      .body(BodyInserters.fromFormData(formData))
	      .retrieve()
	      .bodyToMono(AuthToken.class)
	      .block();
		
		logger.debug("Ledger access token: " + (ledgerToken != null ? ledgerToken.getAccess_token() : "EMPTY"));
		
		return RepeatStatus.FINISHED;
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		stepExecution.getJobExecution().getExecutionContext().put("ledgerToken", this.ledgerToken);
		return ExitStatus.COMPLETED;
	}

}
