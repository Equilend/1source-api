package com.os.marktomarket.tasks;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.WebClient;

import com.os.client.model.Event;
import com.os.client.model.Events;
import com.os.client.model.Loan;
import com.os.marktomarket.AuthToken;
import com.os.marktomarket.LoanMark;

import reactor.core.publisher.Mono;

public class CurrentMarkTask implements Tasklet, StepExecutionListener {

	private static final Logger logger = LoggerFactory.getLogger(CurrentMarkTask.class);

	private AuthToken token;
	private Events events;
	private ArrayList<LoanMark> loanMarks;

	@Autowired
	WebClient restWebClient;

	@Override
	public void beforeStep(StepExecution stepExecution) {
		token = (AuthToken) stepExecution.getJobExecution().getExecutionContext().get("token");
		events = (Events) stepExecution.getJobExecution().getExecutionContext().get("events");
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

		logger.info("Gathering marked loans: " + token.getAccess_token());

		loanMarks = new ArrayList<>();
		
		for (Event event : events) {

			String loanId = event.getResourceUri().substring(event.getResourceUri().lastIndexOf("/") + 1);
			Loan loan = restWebClient.get().uri("/loans/" + loanId)
					.headers(h -> h.setBearerAuth(token.getAccess_token())).retrieve()
					.onStatus(HttpStatusCode::is4xxClientError, response -> {
						return Mono.empty();
					}).bodyToMono(Loan.class).block();
			
			if (loan != null) {

				LoanMark loanMark = new LoanMark(loanId);
				loanMark.setCurrentMarkEventId(event.getEventId());

				BigDecimal price = BigDecimal.valueOf(loan.getTrade().getInstrument().getPrice().getValue());
				BigDecimal quantity = BigDecimal.valueOf(loan.getTrade().getOpenQuantity());
				BigDecimal margin = BigDecimal.valueOf(loan.getTrade().getCollateral().getMargin()).divide(new BigDecimal(100));
				BigDecimal mark = price.multiply(quantity).multiply(margin).setScale(2, RoundingMode.HALF_UP);
				loanMark.setCurrentMark(mark.doubleValue());
				logger.debug(loanId + ": " + price.toPlainString() + " x " + quantity.toPlainString() + " x " + margin.toPlainString() + " = " + mark.toPlainString());
				loanMarks.add(loanMark);
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
