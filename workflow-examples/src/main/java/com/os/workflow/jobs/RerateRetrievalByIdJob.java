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
import com.os.workflow.tasks.RerateRetrievalByIdTask;

@Configuration
public class RerateRetrievalByIdJob {

	@Bean
	public Job rerateRetrievalById(JobRepository jobRepository, ResourcelessTransactionManager transactionManager) {
		return new JobBuilder("rerateRetrievalById", jobRepository)
				.start(rerateRetrievalByIdAuthStep(jobRepository, transactionManager))
				.next(rerateRetrievalByIdStep(jobRepository, transactionManager))
				.build();
	}

	@Bean
	public AuthTask rerateRetrievalByIdAuthTask() {
		return new AuthTask();
	}

	@Bean
	public Step rerateRetrievalByIdAuthStep(JobRepository jobRepository, ResourcelessTransactionManager transactionManager) {
		return new StepBuilder("rerateRetrievalByIdAuthStep", jobRepository).allowStartIfComplete(true)
				.tasklet(rerateRetrievalByIdAuthTask(), transactionManager).build();
	}

	@Bean
	public RerateRetrievalByIdTask rerateRetrievalByIdTask() {
		return new RerateRetrievalByIdTask();
	}

	@Bean
	public Step rerateRetrievalByIdStep(JobRepository jobRepository, ResourcelessTransactionManager transactionManager) {
		return new StepBuilder("rerateRetrievalByIdStep", jobRepository).allowStartIfComplete(true)
				.tasklet(rerateRetrievalByIdTask(), transactionManager).build();
	}

}
