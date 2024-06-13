package com.os.workflow.tasks;

import java.util.Date;

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

import com.os.workflow.AuthToken;
import com.os.workflow.DateGsonTypeAdapter;
import com.os.workflow.WorkflowConfig;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.os.client.model.AcknowledgementType;
import com.os.client.model.LedgerResponse;
import com.os.client.model.ReturnAcknowledgement;
import reactor.core.publisher.Mono;

public class ReturnAcknowledgeNotificationTask implements Tasklet, StepExecutionListener {

	private static final Logger logger = LoggerFactory.getLogger(ReturnAcknowledgeNotificationTask.class);

	private AuthToken ledgerToken;
	//private Return returnObj;

	@Autowired
	WebClient restWebClient;

	@Autowired
	WorkflowConfig workflowConfig;

	@Override
	public void beforeStep(StepExecution stepExecution) {
		ledgerToken = (AuthToken) stepExecution.getJobExecution().getExecutionContext().get("ledgerToken");
		//returnObj = (Return) stepExecution.getJobExecution().getExecutionContext().get("returnObj");
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

		ReturnAcknowledgement acknowledgement = new ReturnAcknowledgement();
		
		acknowledgement.setAcknowledgementType(AcknowledgementType.POSITIVE);
		
//		PartySettlementInstruction partySettlementInstruction = new PartySettlementInstruction();
//		partySettlementInstruction.setPartyRole(PartyRole.fromValue(workflowConfig.getActing_as()));
//		partySettlementInstruction.setInternalAcctCd("stl_b_12345");
//
//		SettlementInstruction instruction = new SettlementInstruction();
//		partySettlementInstruction.setInstruction(instruction);
//		instruction.setSettlementBic("DTCYUS33");
//		instruction.setLocalAgentBic("IRVTBEBBXXX");
//		instruction.setLocalAgentName("THE BANK OF NEW YORK MELLON SA/NV");
//		instruction.setLocalAgentAcct("A12345");
//		instruction.setDtcParticipantNumber("0901");
//
//		acknowledgement.setSettlement(partySettlementInstruction);

		Gson gson = new GsonBuilder()
			    .registerTypeAdapter(Date.class, new DateGsonTypeAdapter())
			    .create();

		String json = gson.toJson(acknowledgement);
		
		logger.debug(json);

		LedgerResponse ledgerResponse = restWebClient.post().uri("/contracts/" + workflowConfig.getContract_id() + "/returns/" + workflowConfig.getReturn_id() + "/acknowledge").contentType(MediaType.APPLICATION_JSON)
				.bodyValue(json)
				.headers(h -> h.setBearerAuth(ledgerToken.getAccess_token())).retrieve()
				.onStatus(HttpStatusCode::is4xxClientError, response -> {
					return Mono.empty();
				}).bodyToMono(LedgerResponse.class).block();

		logger.debug("Ledger Response: " + ledgerResponse);

		return RepeatStatus.FINISHED;
	}

}
