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
import com.os.workflow.tasks.RerateCancelProposalTask;

@Configuration
public class RerateCancelProposalJob {

	@Bean
	public Job rerateCancelProposal(JobRepository jobRepository, ResourcelessTransactionManager transactionManager) {
		return new JobBuilder("rerateCancelProposal", jobRepository)
				.start(rerateCancelProposalAuthStep(jobRepository, transactionManager))
				.next(rerateCancelProposalStep(jobRepository, transactionManager))
				.build();
	}

	@Bean
	public AuthTask rerateCancelProposalAuthTask() {
		return new AuthTask();
	}

	@Bean
	public Step rerateCancelProposalAuthStep(JobRepository jobRepository, ResourcelessTransactionManager transactionManager) {
		return new StepBuilder("rerateCancelProposalAuthStep", jobRepository).allowStartIfComplete(true)
				.tasklet(rerateCancelProposalAuthTask(), transactionManager).build();
	}

	@Bean
	public RerateCancelProposalTask rerateCancelProposalTask() {
		return new RerateCancelProposalTask();
	}

	@Bean
	public Step rerateCancelProposalStep(JobRepository jobRepository, ResourcelessTransactionManager transactionManager) {
		return new StepBuilder("rerateCancelProposalStep", jobRepository).allowStartIfComplete(true)
				.tasklet(rerateCancelProposalTask(), transactionManager).build();
	}

}
