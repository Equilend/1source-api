package com.os.replay.tasks;

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

import com.os.replay.ReplayDBDao;
import com.os.replay.model.AuthToken;

public class Day1LedgerAAuthTask extends RecordReader implements Tasklet, StepExecutionListener {

	private static final Logger logger = LoggerFactory.getLogger(Day1LedgerAAuthTask.class);

	private AuthToken ledgerAToken;
	
	@Autowired
	ReplayDBDao dao;
	
	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		
		MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
		formData.add("grant_type", "password");
		formData.add("client_id", "canton-participant1-client");
		formData.add("username", "TestLender1User");
		formData.add("password", "fjmxVeKzpzUDg3YJ");
		formData.add("client_secret", "c0a05c2d-ac70-472a-ac4f-38b80dba28d8");

		WebClient authClient = WebClient.create("https://stageauth.equilend.com");
		
		ledgerAToken = authClient.post()
	      .uri("/auth/realms/1Source/protocol/openid-connect/token")
	      .contentType(MediaType.APPLICATION_FORM_URLENCODED)
	      .body(BodyInserters.fromFormData(formData))
	      .retrieve()
	      .bodyToMono(AuthToken.class)
	      .block();
		
		logger.debug("Ledger A access token: " + (ledgerAToken != null ? ledgerAToken.getAccess_token() : "EMPTY"));
		
		return RepeatStatus.FINISHED;
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		stepExecution.getJobExecution().getExecutionContext().put("ledgerAToken", this.ledgerAToken);
		stepExecution.getJobExecution().getExecutionContext().put("ledgerAParty", "TLEND-US");
		return ExitStatus.COMPLETED;
	}

}
