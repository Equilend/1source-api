package com.os.marktomarket.tasks;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

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
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.WebClient;

import com.os.client.model.EventType;
import com.os.client.model.Loan;
import com.os.client.model.Loans;
import com.os.marktomarket.AuthToken;
import com.os.marktomarket.LoanMark;

import reactor.core.publisher.Mono;

public class LastMarkTask implements Tasklet, StepExecutionListener {

	private static final Logger logger = LoggerFactory.getLogger(LastMarkTask.class);

	private AuthToken token;
	private List<LoanMark> loanMarks;

	@Autowired
	WebClient restWebClient;

	@SuppressWarnings("unchecked")
	@Override
	public void beforeStep(StepExecution stepExecution) {
		token = (AuthToken) stepExecution.getJobExecution().getExecutionContext().get("token");
		loanMarks = (List<LoanMark>) stepExecution.getJobExecution().getExecutionContext().get("marks");
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

		logger.info("Gathering last mark for loans: " + token.getAccess_token());

		for (LoanMark loanMark : loanMarks) {
			
			//Grab the record from loan history with matching event and timestamp.
			Loans loanHistory = restWebClient.get().uri("/loans/" + loanMark.getLoanId() + "/history")
					.headers(h -> h.setBearerAuth(token.getAccess_token())).retrieve()
					.onStatus(HttpStatusCode::is4xxClientError, response -> {
						return Mono.empty();
					}).bodyToMono(Loans.class).block();
			
			if (loanHistory != null) {
				for (Loan loan : loanHistory) {
					if (EventType.LOAN_MARKTOMARKET.equals(loan.getLastEvent().getEventType())
							&& loan.getLastEvent().getEventId().equals(loanMark.getLastMarkEventId())) {
						
						BigDecimal price = BigDecimal.valueOf(loan.getTrade().getInstrument().getPrice().getValue());
						BigDecimal quantity = BigDecimal.valueOf(loan.getTrade().getOpenQuantity());
						BigDecimal margin = BigDecimal.valueOf(loan.getTrade().getCollateral().getMargin()).divide(new BigDecimal(100));
						BigDecimal mark = price.multiply(quantity).multiply(margin).setScale(2, RoundingMode.HALF_UP);
						loanMark.setLastMark(mark.doubleValue());
						logger.debug(loanMark.getLoanId() + ": " + price.toPlainString() + " x " + quantity.toPlainString() + " x " + margin.toPlainString() + " = " + mark.toPlainString());
					}
				}
			}

		}

		return RepeatStatus.FINISHED;
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		stepExecution.getJobExecution().getExecutionContext().put("marks", this.loanMarks);
		return ExitStatus.COMPLETED;
	}
}
