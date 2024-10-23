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
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.WebClient;

import com.os.client.model.Event;
import com.os.client.model.Events;
import com.os.marktomarket.AuthToken;

import reactor.core.publisher.Mono;

public class EventsTask implements Tasklet, StepExecutionListener {

	private static final Logger logger = LoggerFactory.getLogger(EventsTask.class);

	private AuthToken token;
	private Events events;
	
	@Autowired
	WebClient restWebClient;

	@Override
	public void beforeStep(StepExecution stepExecution) {
		token = (AuthToken) stepExecution.getJobExecution().getExecutionContext().get("token");
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

		logger.info("Gathering LOAN_MARKTOMARKET events: " + token.getAccess_token());

		events = restWebClient.get().uri("/events?eventType=LOAN_MARKTOMARKET")
				.headers(h -> h.setBearerAuth(token.getAccess_token())).retrieve()
				.onStatus(HttpStatusCode::is4xxClientError, response -> {
					return Mono.empty();
				}).bodyToMono(Events.class).block();

		for (Event event : events) {
			logger.debug(event.toString());
		}

		return RepeatStatus.FINISHED;
	}
	
	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		stepExecution.getJobExecution().getExecutionContext().put("events", this.events);
		return ExitStatus.COMPLETED;
	}
	
}
