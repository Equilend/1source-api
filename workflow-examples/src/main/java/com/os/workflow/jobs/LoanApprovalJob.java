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
import com.os.workflow.tasks.LoanApprovalTask;

@Configuration
public class LoanApprovalJob {

	@Bean
	public Job loanApproval(JobRepository jobRepository, ResourcelessTransactionManager transactionManager) {
		return new JobBuilder("loanApproval", jobRepository)
				.start(loanApprovalAuthStep(jobRepository, transactionManager))
				.next(loanApprovalStep(jobRepository, transactionManager))
				.build();
	}

	@Bean
	public AuthTask loanApprovalAuthTask() {
		return new AuthTask();
	}

	@Bean
	public Step loanApprovalAuthStep(JobRepository jobRepository, ResourcelessTransactionManager transactionManager) {
		return new StepBuilder("loanApprovalAuthStep", jobRepository).allowStartIfComplete(true)
				.tasklet(loanApprovalAuthTask(), transactionManager).build();
	}

	@Bean
	public LoanApprovalTask loanApprovalTask() {
		return new LoanApprovalTask();
	}

	@Bean
	public Step loanApprovalStep(JobRepository jobRepository, ResourcelessTransactionManager transactionManager) {
		return new StepBuilder("loanApprovalStep", jobRepository).allowStartIfComplete(true)
				.tasklet(loanApprovalTask(), transactionManager).build();
	}

}
