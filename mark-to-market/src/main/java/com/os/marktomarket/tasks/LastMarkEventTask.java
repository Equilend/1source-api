package com.os.marktomarket.tasks;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
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

import com.os.client.model.Event;
import com.os.client.model.Events;
import com.os.marktomarket.AuthToken;
import com.os.marktomarket.LoanMark;

import reactor.core.publisher.Mono;

public class LastMarkEventTask implements Tasklet, StepExecutionListener, Comparator<Event> {

	private static final Logger logger = LoggerFactory.getLogger(LastMarkEventTask.class);

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

		logger.info("Gathering last mark events for loans: " + token.getAccess_token());

		LocalDate yesterday = LocalDate.now().minusDays(1);
		
		for (LoanMark loanMark : loanMarks) {

			//Check that loan was marked yesterday
			Events events = restWebClient.get().uri("/loans/" + loanMark.getLoanId() + "/events?beforeEventId=" + loanMark.getCurrentMarkEventId() + "&since=" + yesterday.format(DateTimeFormatter.ISO_LOCAL_DATE) + "T00:00:00.000Z&eventType=LOAN_MARKTOMARKET")
					.headers(h -> h.setBearerAuth(token.getAccess_token())).retrieve()
					.onStatus(HttpStatusCode::is4xxClientError, response -> {
						return Mono.empty();
					}).bodyToMono(Events.class).block();

			//Should be a single event. If there are 0 then mark wasn't applied.
			if (events == null || events.size() == 0) {
				logger.warn("No prior mark for: " + loanMark.getLoanId());
			} else if (events.size() == 1) {
				loanMark.setLastMarkEventId(events.get(0).getEventId());
			} else {
				logger.warn("Multiple mark events for: " + loanMark.getLoanId());
				events.sort(this);
				loanMark.setLastMarkEventId(events.get(0).getEventId());
			}

			logger.debug("Prior mark event " + loanMark.getLoanId() + ": " + loanMark.getLastMarkEventId());
			
		}

		return RepeatStatus.FINISHED;
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		stepExecution.getJobExecution().getExecutionContext().put("marks", this.loanMarks);
		return ExitStatus.COMPLETED;
	}

	@Override
	public int compare(Event o1, Event o2) {
		return o1.getEventDateTime().compareTo(o2.getEventDateTime());
	}

}
