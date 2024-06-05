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
import com.os.workflow.WorkflowConfig;
import com.os.workflow.AuthToken;

import io.swagger.v1_0_5_20240428.client.model.ContractProposalApproval;
import io.swagger.v1_0_5_20240428.client.model.LedgerResponse;
import io.swagger.v1_0_5_20240428.client.model.LocalDateTypeAdapter;
import io.swagger.v1_0_5_20240428.client.model.OffsetDateTimeTypeAdapter;
import io.swagger.v1_0_5_20240428.client.model.PartyRole;
import io.swagger.v1_0_5_20240428.client.model.PartySettlementInstruction;
import io.swagger.v1_0_5_20240428.client.model.SettlementInstruction;
import reactor.core.publisher.Mono;

public class ContractApprovalTask implements Tasklet, StepExecutionListener {

	private static final Logger logger = LoggerFactory.getLogger(ContractApprovalTask.class);

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

		ContractProposalApproval proposal = new ContractProposalApproval();

		proposal.setInternalRefId("b_int_ref_z0001");
		
		PartySettlementInstruction partySettlementInstruction = new PartySettlementInstruction();
		partySettlementInstruction.setPartyRole(PartyRole.fromValue(workflowConfig.getActing_as()));
		partySettlementInstruction.setInternalAcctCd("stl_b_12345");

		SettlementInstruction instruction = new SettlementInstruction();
		partySettlementInstruction.setInstruction(instruction);
		instruction.setSettlementBic("DTCYUS33");
		instruction.setLocalAgentBic("IRVTBEBBXXX");
		instruction.setLocalAgentName("THE BANK OF NEW YORK MELLON SA/NV");
		instruction.setLocalAgentAcct("A12345");
		instruction.setDtcParticipantNumber("0901");

		proposal.setSettlement(partySettlementInstruction);

		Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())
				.registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeTypeAdapter()).create();

		logger.debug(gson.toJson(proposal));

		LedgerResponse ledgerResponse = restWebClient.post().uri("/contracts/" + workflowConfig.getContract_id() + "/approve").contentType(MediaType.APPLICATION_JSON)
				.bodyValue(proposal).headers(h -> h.setBearerAuth(ledgerToken.getAccess_token())).retrieve()
				.onStatus(HttpStatusCode::is4xxClientError, response -> {
					return Mono.empty();
				}).bodyToMono(LedgerResponse.class).block();

		logger.debug("Ledger Response: " + ledgerResponse);

		return RepeatStatus.FINISHED;
	}

}
