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
import com.os.workflow.tasks.RerateRetrievalTask;

@Configuration
public class RerateRetrievalJob {

	@Bean
	public Job rerateRetrieval(JobRepository jobRepository, ResourcelessTransactionManager transactionManager) {
		return new JobBuilder("rerateRetrieval", jobRepository)
				.start(rerateRetrievalAuthStep(jobRepository, transactionManager))
				.next(rerateRetrievalStep(jobRepository, transactionManager))
				.build();
	}

	@Bean
	public AuthTask rerateRetrievalAuthTask() {
		return new AuthTask();
	}

	@Bean
	public Step rerateRetrievalAuthStep(JobRepository jobRepository, ResourcelessTransactionManager transactionManager) {
		return new StepBuilder("rerateRetrievalAuthStep", jobRepository).allowStartIfComplete(true)
				.tasklet(rerateRetrievalAuthTask(), transactionManager).build();
	}

	@Bean
	public RerateRetrievalTask rerateRetrievalTask() {
		return new RerateRetrievalTask();
	}

	@Bean
	public Step rerateRetrievalStep(JobRepository jobRepository, ResourcelessTransactionManager transactionManager) {
		return new StepBuilder("rerateRetrievalStep", jobRepository).allowStartIfComplete(true)
				.tasklet(rerateRetrievalTask(), transactionManager).build();
	}

}
