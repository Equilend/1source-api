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
import com.os.workflow.tasks.ContractProposalTask;

@Configuration
public class ContractProposalJob {

	@Bean
	public Job contractProposal(JobRepository jobRepository, ResourcelessTransactionManager transactionManager) {
		return new JobBuilder("contractProposal", jobRepository)
				.start(contractProposalAuthStep(jobRepository, transactionManager))
				.next(contractProposalStep(jobRepository, transactionManager))
				.build();
	}

	@Bean
	public AuthTask contractProposalAuthTask() {
		return new AuthTask();
	}

	@Bean
	public Step contractProposalAuthStep(JobRepository jobRepository, ResourcelessTransactionManager transactionManager) {
		return new StepBuilder("contractProposalAuthStep", jobRepository).allowStartIfComplete(true)
				.tasklet(contractProposalAuthTask(), transactionManager).build();
	}

	@Bean
	public ContractProposalTask contractProposalTask() {
		return new ContractProposalTask();
	}

	@Bean
	public Step contractProposalStep(JobRepository jobRepository, ResourcelessTransactionManager transactionManager) {
		return new StepBuilder("contractProposalStep", jobRepository).allowStartIfComplete(true)
				.tasklet(contractProposalTask(), transactionManager).build();
	}

}
