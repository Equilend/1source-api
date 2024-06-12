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
import com.os.workflow.tasks.ContractApprovalTask;

@Configuration
public class ContractApprovalJob {

	@Bean
	public Job contractApproval(JobRepository jobRepository, ResourcelessTransactionManager transactionManager) {
		return new JobBuilder("contractApproval", jobRepository)
				.start(contractApprovalAuthStep(jobRepository, transactionManager))
				.next(contractApprovalStep(jobRepository, transactionManager))
				.build();
	}

	@Bean
	public AuthTask contractApprovalAuthTask() {
		return new AuthTask();
	}

	@Bean
	public Step contractApprovalAuthStep(JobRepository jobRepository, ResourcelessTransactionManager transactionManager) {
		return new StepBuilder("contractApprovalAuthStep", jobRepository).allowStartIfComplete(true)
				.tasklet(contractApprovalAuthTask(), transactionManager).build();
	}

	@Bean
	public ContractApprovalTask contractApprovalTask() {
		return new ContractApprovalTask();
	}

	@Bean
	public Step contractApprovalStep(JobRepository jobRepository, ResourcelessTransactionManager transactionManager) {
		return new StepBuilder("contractApprovalStep", jobRepository).allowStartIfComplete(true)
				.tasklet(contractApprovalTask(), transactionManager).build();
	}

}
