package com.os.marktomarket.jobs;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.os.marktomarket.JobCompletionNotificationListener;
import com.os.marktomarket.tasks.AuthTask;
import com.os.marktomarket.tasks.CalculateMarkTask;
import com.os.marktomarket.tasks.CurrentMarkTask;
import com.os.marktomarket.tasks.EventsTask;
import com.os.marktomarket.tasks.LastMarkEventTask;
import com.os.marktomarket.tasks.LastMarkTask;

@Configuration
public class MarkToMarketJob {

	@Bean
	public Job markToMarket(JobRepository jobRepository, ResourcelessTransactionManager transactionManager, Flow markToMarketFlow, JobCompletionNotificationListener listener) {
		return new JobBuilder("markToMarket", jobRepository)
				.listener(listener)
				.start(markToMarketFlow)
				.end()
				.build();
	}

	@Bean
	public AuthTask ledgerAuthTask() {
		return new AuthTask();
	}

	@Bean
	public Step authStep(JobRepository jobRepository, ResourcelessTransactionManager transactionManager) {
		return new StepBuilder("auth", jobRepository).allowStartIfComplete(true)
				.tasklet(ledgerAuthTask(), transactionManager).build();
	}

	@Bean
	public EventsTask eventsTask() {
		return new EventsTask();
	}

	@Bean
	public Step eventsStep(JobRepository jobRepository, ResourcelessTransactionManager transactionManager) {
		return new StepBuilder("events", jobRepository).allowStartIfComplete(true)
				.tasklet(eventsTask(), transactionManager)
				.build();
	}

	@Bean
	public CurrentMarkTask currentMarkTask() {
		return new CurrentMarkTask();
	}

	@Bean
	public Step currentMarkStep(JobRepository jobRepository, ResourcelessTransactionManager transactionManager) {
		return new StepBuilder("currentMark", jobRepository).allowStartIfComplete(true)
				.tasklet(currentMarkTask(), transactionManager)
				.build();
	}

	@Bean
	public LastMarkEventTask lastMarkEventTask() {
		return new LastMarkEventTask();
	}

	@Bean
	public Step lastMarkEventStep(JobRepository jobRepository, ResourcelessTransactionManager transactionManager) {
		return new StepBuilder("lastMarkEvent", jobRepository).allowStartIfComplete(true)
				.tasklet(lastMarkEventTask(), transactionManager)
				.build();
	}

	@Bean
	public LastMarkTask lastMarkTask() {
		return new LastMarkTask();
	}

	@Bean
	public Step lastMarkStep(JobRepository jobRepository, ResourcelessTransactionManager transactionManager) {
		return new StepBuilder("lastMark", jobRepository).allowStartIfComplete(true)
				.tasklet(lastMarkTask(), transactionManager)
				.build();
	}

	@Bean
	public CalculateMarkTask calculateMarkTask() {
		return new CalculateMarkTask();
	}

	@Bean
	public Step calculateMarkStep(JobRepository jobRepository, ResourcelessTransactionManager transactionManager) {
		return new StepBuilder("calculateMark", jobRepository).allowStartIfComplete(true)
				.tasklet(calculateMarkTask(), transactionManager)
				.build();
	}

	@Bean
	public Flow markToMarketFlow(Step authStep, Step eventsStep, Step currentMarkStep, Step lastMarkEventStep, Step lastMarkStep, Step calculateMarkStep) {
		
		return new FlowBuilder<Flow>("markToMarketFlow")
				.start(authStep)
				.next(eventsStep)
				.next(currentMarkStep)
				.next(lastMarkEventStep)
				.next(lastMarkStep)
				.next(calculateMarkStep)
				.build();
	}
}
