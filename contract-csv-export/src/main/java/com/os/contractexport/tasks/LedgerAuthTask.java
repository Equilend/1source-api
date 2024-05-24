package com.os.contractexport.tasks;

import java.util.ArrayList;

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

import com.os.contractexport.model.AuthToken;
import com.os.contractexport.model.PartyAccess;

public class LedgerAuthTask implements Tasklet, StepExecutionListener {

	private static final Logger logger = LoggerFactory.getLogger(LedgerAuthTask.class);

	@Autowired
	private ArrayList<PartyAccess> partyAccessList;

	private AuthToken token;
	
	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

		PartyAccess partyAccess = partyAccessList.remove(0);
		
		MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
		formData.add("grant_type", "password");
		formData.add("client_id", "canton-participant1-client");
		formData.add("username", partyAccess.getUserName());
		formData.add("password", partyAccess.getPassword());
		formData.add("client_secret", "c0a05c2d-ac70-472a-ac4f-38b80dba28d8");

		WebClient authClient = WebClient.create("https://stageauth.equilend.com");
		
		token = authClient.post()
	      .uri("/auth/realms/1Source/protocol/openid-connect/token")
	      .contentType(MediaType.APPLICATION_FORM_URLENCODED)
	      .body(BodyInserters.fromFormData(formData))
	      .retrieve()
	      .bodyToMono(AuthToken.class)
	      .block();
		
		logger.debug("Ledger access token for " + partyAccess.getUserName() + ": " + (token != null ? token.getAccess_token() : "EMPTY"));
		
		return RepeatStatus.FINISHED;
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		stepExecution.getJobExecution().getExecutionContext().put("token", this.token);
		return ExitStatus.COMPLETED;
	}

}
