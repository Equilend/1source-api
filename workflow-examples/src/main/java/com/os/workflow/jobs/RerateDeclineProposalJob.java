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
import com.os.workflow.tasks.RerateDeclineProposalTask;

@Configuration
public class RerateDeclineProposalJob {

	@Bean
	public Job rerateDeclineProposal(JobRepository jobRepository, ResourcelessTransactionManager transactionManager) {
		return new JobBuilder("rerateDeclineProposal", jobRepository)
				.start(rerateDeclineProposalAuthStep(jobRepository, transactionManager))
				.next(rerateDeclineProposalStep(jobRepository, transactionManager))
				.build();
	}

	@Bean
	public AuthTask rerateDeclineProposalAuthTask() {
		return new AuthTask();
	}

	@Bean
	public Step rerateDeclineProposalAuthStep(JobRepository jobRepository, ResourcelessTransactionManager transactionManager) {
		return new StepBuilder("rerateDeclineProposalAuthStep", jobRepository).allowStartIfComplete(true)
				.tasklet(rerateDeclineProposalAuthTask(), transactionManager).build();
	}

	@Bean
	public RerateDeclineProposalTask rerateDeclineProposalTask() {
		return new RerateDeclineProposalTask();
	}

	@Bean
	public Step rerateDeclineProposalStep(JobRepository jobRepository, ResourcelessTransactionManager transactionManager) {
		return new StepBuilder("rerateDeclineProposalStep", jobRepository).allowStartIfComplete(true)
				.tasklet(rerateDeclineProposalTask(), transactionManager).build();
	}

}
