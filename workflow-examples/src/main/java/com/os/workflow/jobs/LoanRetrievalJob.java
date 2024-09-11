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
import com.os.workflow.tasks.LoanRetrievalTask;

@Configuration
public class LoanRetrievalJob {

	@Bean
	public Job loanRetrieval(JobRepository jobRepository, ResourcelessTransactionManager transactionManager) {
		return new JobBuilder("loanRetrieval", jobRepository)
				.start(loanRetrievalAuthStep(jobRepository, transactionManager))
				.next(loanRetrievalStep(jobRepository, transactionManager))
				.build();
	}

	@Bean
	public AuthTask loanRetrievalAuthTask() {
		return new AuthTask();
	}

	@Bean
	public Step loanRetrievalAuthStep(JobRepository jobRepository, ResourcelessTransactionManager transactionManager) {
		return new StepBuilder("loanRetrievalAuthStep", jobRepository).allowStartIfComplete(true)
				.tasklet(loanRetrievalAuthTask(), transactionManager).build();
	}

	@Bean
	public LoanRetrievalTask loanRetrievalTask() {
		return new LoanRetrievalTask();
	}

	@Bean
	public Step loanRetrievalStep(JobRepository jobRepository, ResourcelessTransactionManager transactionManager) {
		return new StepBuilder("loanRetrievalStep", jobRepository).allowStartIfComplete(true)
				.tasklet(loanRetrievalTask(), transactionManager).build();
	}

}
