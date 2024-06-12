package com.os.workflow.jobs;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.os.workflow.tasks.AuthTask;
import com.os.workflow.tasks.RerateQueryTask;

@Configuration
public class RerateQueryJob {

	@Bean
	public Job rerateQuery(JobRepository jobRepository, ResourcelessTransactionManager transactionManager) {
		return new JobBuilder("rerateQuery", jobRepository)
				.start(rerateQueryAuthStep(jobRepository, transactionManager))
				.next(rerateQueryStep(jobRepository, transactionManager))
				.build();
	}

	@Bean
	public AuthTask rerateQueryAuthTask() {
		return new AuthTask();
	}

	@Bean
	public Step rerateQueryAuthStep(JobRepository jobRepository, ResourcelessTransactionManager transactionManager) {
		return new StepBuilder("rerateQueryAuthStep", jobRepository).allowStartIfComplete(true)
				.tasklet(rerateQueryAuthTask(), transactionManager).build();
	}

	@Bean
	public RerateQueryTask rerateQueryTask() {
		return new RerateQueryTask();
	}

	@Bean
	public Step rerateQueryStep(JobRepository jobRepository, ResourcelessTransactionManager transactionManager) {
		return new StepBuilder("rerateQueryStep", jobRepository).allowStartIfComplete(true)
				.tasklet(rerateQueryTask(), transactionManager).build();
	}

}
