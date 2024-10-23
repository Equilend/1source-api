package com.os.marktomarket.tasks;

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

import com.os.marktomarket.AppConfig;
import com.os.marktomarket.AuthToken;

public class AuthTask implements Tasklet, StepExecutionListener {

	private static final Logger logger = LoggerFactory.getLogger(AuthTask.class);

	@Autowired
	private AppConfig appConfig;
	
	private AuthToken token;
	
	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

		MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
		formData.add("grant_type", "password");
		formData.add("client_id", appConfig.getAuth_client_id());
		formData.add("username", appConfig.getAuth_username());
		formData.add("password", appConfig.getAuth_password());
		formData.add("client_secret", appConfig.getAuth_client_secret());

		WebClient authClient = WebClient.create(appConfig.getAuth_uri());
		
		token = authClient.post()
	      .uri("/auth/realms/1Source/protocol/openid-connect/token")
	      .contentType(MediaType.APPLICATION_FORM_URLENCODED)
	      .body(BodyInserters.fromFormData(formData))
	      .retrieve()
	      .bodyToMono(AuthToken.class)
	      .block();
		
		logger.debug("Ledger access token for " + appConfig.getAuth_username() + ": " + (token != null ? token.getAccess_token() : "EMPTY"));
		
		return RepeatStatus.FINISHED;
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		stepExecution.getJobExecution().getExecutionContext().put("token", this.token);
		return ExitStatus.COMPLETED;
	}

}
