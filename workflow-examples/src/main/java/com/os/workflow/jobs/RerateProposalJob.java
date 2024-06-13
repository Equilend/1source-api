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
import com.os.workflow.tasks.ContractRetrievalTask;
import com.os.workflow.tasks.RerateProposalTask;

@Configuration
public class RerateProposalJob {

	@Bean
	public Job rerateProposal(JobRepository jobRepository, ResourcelessTransactionManager transactionManager) {
		return new JobBuilder("rerateProposal", jobRepository)
				.start(rerateProposalAuthStep(jobRepository, transactionManager))
				.next(rerateContractRetrievalStep(jobRepository, transactionManager))
				.next(rerateProposalStep(jobRepository, transactionManager))
				.build();
	}

	@Bean
	public AuthTask rerateProposalAuthTask() {
		return new AuthTask();
	}

	@Bean
	public Step rerateProposalAuthStep(JobRepository jobRepository, ResourcelessTransactionManager transactionManager) {
		return new StepBuilder("rerateProposalAuthStep", jobRepository).allowStartIfComplete(true)
				.tasklet(rerateProposalAuthTask(), transactionManager).build();
	}

	@Bean
	public ContractRetrievalTask rerateContractRetrievalTask() {
		return new ContractRetrievalTask();
	}

	@Bean
	public Step rerateContractRetrievalStep(JobRepository jobRepository, ResourcelessTransactionManager transactionManager) {
		return new StepBuilder("rerateContractRetrievalStep", jobRepository).allowStartIfComplete(true)
				.tasklet(rerateContractRetrievalTask(), transactionManager).build();
	}

	@Bean
	public RerateProposalTask rerateProposalTask() {
		return new RerateProposalTask();
	}

	@Bean
	public Step rerateProposalStep(JobRepository jobRepository, ResourcelessTransactionManager transactionManager) {
		return new StepBuilder("rerateProposalStep", jobRepository).allowStartIfComplete(true)
				.tasklet(rerateProposalTask(), transactionManager).build();
	}

}
