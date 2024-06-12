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
import com.os.workflow.tasks.RerateApproveProposalTask;

@Configuration
public class RerateApproveProposalJob {

	@Bean
	public Job rerateApproveProposal(JobRepository jobRepository, ResourcelessTransactionManager transactionManager) {
		return new JobBuilder("rerateApproveProposal", jobRepository)
				.start(rerateApproveProposalAuthStep(jobRepository, transactionManager))
				.next(rerateApproveProposalStep(jobRepository, transactionManager))
				.build();
	}

	@Bean
	public AuthTask rerateApproveProposalAuthTask() {
		return new AuthTask();
	}

	@Bean
	public Step rerateApproveProposalAuthStep(JobRepository jobRepository, ResourcelessTransactionManager transactionManager) {
		return new StepBuilder("rerateApproveProposalAuthStep", jobRepository).allowStartIfComplete(true)
				.tasklet(rerateApproveProposalAuthTask(), transactionManager).build();
	}

	@Bean
	public RerateApproveProposalTask rerateApproveProposalTask() {
		return new RerateApproveProposalTask();
	}

	@Bean
	public Step rerateApproveProposalStep(JobRepository jobRepository, ResourcelessTransactionManager transactionManager) {
		return new StepBuilder("rerateApproveProposalStep", jobRepository).allowStartIfComplete(true)
				.tasklet(rerateApproveProposalTask(), transactionManager).build();
	}

}
