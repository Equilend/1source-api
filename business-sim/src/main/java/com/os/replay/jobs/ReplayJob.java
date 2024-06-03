package com.os.replay.jobs;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.os.replay.tasks.Day1LedgerAAuthTask;
import com.os.replay.tasks.Day1LedgerATask;
import com.os.replay.tasks.Day1LedgerBAuthTask;
import com.os.replay.tasks.Day1LedgerBTask;
import com.os.replay.tasks.Day0LedgerAInitTask;
import com.os.replay.tasks.Day0LedgerBInitTask;

@Configuration
public class ReplayJob {
	
	@Bean
	public Job replay(JobRepository jobRepository, DataSourceTransactionManager transactionManager) {
		return new JobBuilder("replay", jobRepository)
//				.start(day0LedgerAInit(jobRepository, transactionManager))
//				.next(day0LedgerBInit(jobRepository, transactionManager))
				.start(day1LedgerAAuth(jobRepository, transactionManager))
				.next(day1LedgerA(jobRepository, transactionManager))
//				.next(day1LedgerBAuth(jobRepository, transactionManager))
//				.next(day1LedgerB(jobRepository, transactionManager))
				.build();
	}

	@Bean
	public Day0LedgerAInitTask day0LedgerAInitTask() {
		return new Day0LedgerAInitTask();
	}

	@Bean
	public Step day0LedgerAInit(JobRepository jobRepository, DataSourceTransactionManager transactionManager) {
		return new StepBuilder("day0LedgerAInit", jobRepository).allowStartIfComplete(true)
				.tasklet(day0LedgerAInitTask(), transactionManager).build();
	}

	@Bean
	public Day0LedgerBInitTask day0LedgerBInitTask() {
		return new Day0LedgerBInitTask();
	}

	@Bean
	public Step day0LedgerBInit(JobRepository jobRepository, DataSourceTransactionManager transactionManager) {
		return new StepBuilder("day0LedgerBInit", jobRepository).allowStartIfComplete(true)
				.tasklet(day0LedgerBInitTask(), transactionManager).build();
	}

	@Bean
	public Day1LedgerATask day1LedgerATask() {
		return new Day1LedgerATask();
	}

	@Bean
	public Step day1LedgerA(JobRepository jobRepository, DataSourceTransactionManager transactionManager) {
		return new StepBuilder("day1LedgerA", jobRepository).allowStartIfComplete(true)
				.tasklet(day1LedgerATask(), transactionManager).build();
	}

	@Bean
	public Day1LedgerAAuthTask day1LedgerAAuthTask() {
		return new Day1LedgerAAuthTask();
	}

	@Bean
	public Step day1LedgerAAuth(JobRepository jobRepository, DataSourceTransactionManager transactionManager) {
		return new StepBuilder("day1LedgerAAuth", jobRepository).allowStartIfComplete(true)
				.tasklet(day1LedgerAAuthTask(), transactionManager).build();
	}

	@Bean
	public Day1LedgerBTask day1LedgerBTask() {
		return new Day1LedgerBTask();
	}

	@Bean
	public Step day1LedgerB(JobRepository jobRepository, DataSourceTransactionManager transactionManager) {
		return new StepBuilder("day1LedgerB", jobRepository).allowStartIfComplete(true)
				.tasklet(day1LedgerBTask(), transactionManager).build();
	}

	@Bean
	public Day1LedgerBAuthTask day1LedgerBAuthTask() {
		return new Day1LedgerBAuthTask();
	}

	@Bean
	public Step day1LedgerBAuth(JobRepository jobRepository, DataSourceTransactionManager transactionManager) {
		return new StepBuilder("day1LedgerBAuth", jobRepository).allowStartIfComplete(true)
				.tasklet(day1LedgerBAuthTask(), transactionManager).build();
	}

}
