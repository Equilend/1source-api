package com.os.workflow.tasks;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Random;
import java.util.UUID;

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
import com.os.workflow.ContractProposalUtil;
import com.os.workflow.LedgerRecord;
import com.os.workflow.WorkflowConfig;

import io.swagger.v1_0_5_20240428.client.model.ContractProposal;
import io.swagger.v1_0_5_20240428.client.model.CurrencyCd;
import io.swagger.v1_0_5_20240428.client.model.LedgerResponse;
import io.swagger.v1_0_5_20240428.client.model.LocalDateTypeAdapter;
import io.swagger.v1_0_5_20240428.client.model.OffsetDateTimeTypeAdapter;
import reactor.core.publisher.Mono;

public class ContractProposalTask implements Tasklet, StepExecutionListener {

	private static final Logger logger = LoggerFactory.getLogger(ContractProposalTask.class);

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

		ContractProposalUtil contractUtil = new ContractProposalUtil();

		LedgerRecord ledgerRecord = buildLedgerRecord();

		ContractProposal proposal = contractUtil.createContractProposal(ledgerRecord);

		Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())
				.registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeTypeAdapter()).create();

		logger.debug(gson.toJson(proposal));

		LedgerResponse ledgerResponse = restWebClient.post().uri("/contracts").contentType(MediaType.APPLICATION_JSON)
				.bodyValue(proposal).headers(h -> h.setBearerAuth(ledgerToken.getAccess_token())).retrieve()
				.onStatus(HttpStatusCode::is4xxClientError, response -> {
					return Mono.empty();
				}).bodyToMono(LedgerResponse.class).block();

		logger.debug("Ledger Response: " + ledgerResponse);

		return RepeatStatus.FINISHED;
	}

	public LedgerRecord buildLedgerRecord() {
		
		Random random = new Random();
	    
		LedgerRecord ledgerRecord = new LedgerRecord();
		ledgerRecord.setInternalRefId(UUID.randomUUID().toString());
		ledgerRecord.setTradeDate(LocalDate.now());
		
		ledgerRecord.setBorrowLoan(workflowConfig.getActing_as());
		ledgerRecord.setFigi("BBG000B9XRY4");
		ledgerRecord.setQuantity(Long.valueOf((((random.nextInt(100000 - 10000) + 10000))+99)/100)*100);
		ledgerRecord.setCurrencyCd(CurrencyCd.USD.getValue());
		ledgerRecord.setDividendRate(new BigDecimal(85));
		ledgerRecord.setContractPrice(new BigDecimal(random.nextInt((500-100) + 500)));
		ledgerRecord.setCollateralMargin(new BigDecimal(102));
		ledgerRecord.setSpreadRate(new BigDecimal(random.nextInt((100-1) + 100)));

		ledgerRecord.setOneSourcePartyId("TLEN-US");
		ledgerRecord.setOneSourcePartyName("Test Lender 1");
		ledgerRecord.setOneSourcePartyGleifLei("KTB500SKZSDI75VSFU40");

		ledgerRecord.setOneSourceCounterpartyId("TBORR-US");
		ledgerRecord.setOneSourceCounterpartyName("Test Borrower 1");
		ledgerRecord.setOneSourceCounterpartyGleifLei("KTB500SKZSDI75VSFU40");
		
		ledgerRecord.setSsiInternalAcctCd("stl_1234");
		ledgerRecord.setSsiSettlementBic("DTCYUS33");
		ledgerRecord.setSsiLocalAgentBic("IRVTBEBBXXX");
		ledgerRecord.setSsiLocalAgentName("THE BANK OF NEW YORK MELLON SA/NV");
		ledgerRecord.setSsiLocalAgentAcct("A12345");
		ledgerRecord.setSsiCustodianBic("IRVTBEBBXXX");
		ledgerRecord.setSsiCustodianName("THE BANK OF NEW YORK MELLON SA/NV");
		ledgerRecord.setSsiCustodianAcct("C12345");
		ledgerRecord.setDtcParticipantNum("0901");
		ledgerRecord.setCdsParticipantNum(null);

		return ledgerRecord;
	}
}
