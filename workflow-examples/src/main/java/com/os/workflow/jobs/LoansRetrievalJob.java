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
import com.os.workflow.tasks.LoansRetrievalTask;

@Configuration
public class LoansRetrievalJob {

	@Bean
	public Job loansRetrieval(JobRepository jobRepository, ResourcelessTransactionManager transactionManager) {
		return new JobBuilder("loansRetrieval", jobRepository)
				.start(loansRetrievalAuthStep(jobRepository, transactionManager))
				.next(loansRetrievalStep(jobRepository, transactionManager))
				.build();
	}

	@Bean
	public AuthTask loansRetrievalAuthTask() {
		return new AuthTask();
	}

	@Bean
	public Step loansRetrievalAuthStep(JobRepository jobRepository, ResourcelessTransactionManager transactionManager) {
		return new StepBuilder("loansRetrievalAuthStep", jobRepository).allowStartIfComplete(true)
				.tasklet(loansRetrievalAuthTask(), transactionManager).build();
	}

	@Bean
	public LoansRetrievalTask loansRetrievalTask() {
		return new LoansRetrievalTask();
	}

	@Bean
	public Step loansRetrievalStep(JobRepository jobRepository, ResourcelessTransactionManager transactionManager) {
		return new StepBuilder("loansRetrievalStep", jobRepository).allowStartIfComplete(true)
				.tasklet(loansRetrievalTask(), transactionManager).build();
	}

}
