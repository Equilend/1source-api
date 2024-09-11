package com.os.replay.tasks;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.os.client.model.LedgerResponse;
import com.os.client.model.LoanProposal;
import com.os.replay.ReplayDBDao;
import com.os.replay.model.AuthToken;
import com.os.replay.model.LedgerRecord;
import com.os.replay.util.LoanProposalUtil;
import com.os.replay.util.LocalDateTypeAdapter;
import com.os.replay.util.OffsetDateTimeTypeAdapter;

import reactor.core.publisher.Mono;

public class Day1LedgerATask extends RecordReader implements Tasklet, StepExecutionListener {

	private static final Logger logger = LoggerFactory.getLogger(Day1LedgerATask.class);

	private AuthToken ledgerAToken;
	//private String ledgerAParty;
	private String ledgerAName;
	private LocalDate ledgerAStartDate;
	
	@Autowired
	ReplayDBDao dao;
	
	@Autowired
	WebClient restWebClient;
	
	@Override
	public void beforeStep(StepExecution stepExecution) {
		ledgerAToken = (AuthToken) stepExecution.getJobExecution().getExecutionContext().get("ledgerAToken");
		//ledgerAParty = (String) stepExecution.getJobExecution().getExecutionContext().get("ledgerAParty");
		ledgerAName = (String) stepExecution.getJobExecution().getExecutionContext().get("ledgerAName");
		ledgerAStartDate = (LocalDate) stepExecution.getJobExecution().getExecutionContext().get("ledgerAStartDate");
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		
		//1. Query for new loans on startDate
		List<LedgerRecord> ledgerRecords = dao.getLedgerRecords(ledgerAStartDate, ledgerAName);
		
		//2. Submit each as loan proposals to the API
		if (ledgerRecords != null && ledgerRecords.size() > 0) {
			
			int submittedLoans = 0;
			
			Gson gson = new GsonBuilder()
				    .registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())
				    .registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeTypeAdapter())
				    .create();
			
			LoanProposalUtil loanUtil = new LoanProposalUtil();
			
			for (LedgerRecord record : ledgerRecords) {
				LoanProposal proposal = loanUtil.createLoanProposal(record);
				
				logger.debug(gson.toJson(proposal));
				
				LedgerResponse ledgerResponse = restWebClient.post()
			      .uri("/loans")
			      .contentType(MediaType.APPLICATION_JSON)
			      .bodyValue(proposal)
			      .headers(h -> h.setBearerAuth(ledgerAToken.getAccess_token()))
			      .retrieve()
			      .onStatus(HttpStatusCode::is4xxClientError,
		                    response -> { return Mono.empty(); })
			      .bodyToMono(LedgerResponse.class)
			      .block();
				
				logger.debug("Ledger Response: " + ledgerResponse);
				
				if (HttpStatus.CREATED.value() == Integer.parseInt(ledgerResponse.getCode())) {
					dao.updateOSLoanId(ledgerAName, record.getInternalRefId(), loanUtil.parseResourceUri(ledgerResponse.getResourceUri()));
					submittedLoans++;
				}
				
				//TODO - remove this limiter
				if (submittedLoans >= 5) {
					break;
				}
			}
		}
		
		return RepeatStatus.FINISHED;
	}

}
