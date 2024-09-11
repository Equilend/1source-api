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
import com.os.workflow.tasks.LoanProposalTask;

@Configuration
public class LoanProposalJob {

	@Bean
	public Job loanProposal(JobRepository jobRepository, ResourcelessTransactionManager transactionManager) {
		return new JobBuilder("loanProposal", jobRepository)
				.start(loanProposalAuthStep(jobRepository, transactionManager))
				.next(loanProposalStep(jobRepository, transactionManager))
				.build();
	}

	@Bean
	public AuthTask loanProposalAuthTask() {
		return new AuthTask();
	}

	@Bean
	public Step loanProposalAuthStep(JobRepository jobRepository, ResourcelessTransactionManager transactionManager) {
		return new StepBuilder("loanProposalAuthStep", jobRepository).allowStartIfComplete(true)
				.tasklet(loanProposalAuthTask(), transactionManager).build();
	}

	@Bean
	public LoanProposalTask loanProposalTask() {
		return new LoanProposalTask();
	}

	@Bean
	public Step loanProposalStep(JobRepository jobRepository, ResourcelessTransactionManager transactionManager) {
		return new StepBuilder("loanProposalStep", jobRepository).allowStartIfComplete(true)
				.tasklet(loanProposalTask(), transactionManager).build();
	}

}
