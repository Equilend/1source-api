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
import com.os.workflow.tasks.ContractSettlementStatusUpdateTask;

@Configuration
public class ContractSettlementStatusUpdateJob {

	@Bean
	public Job contractSettlementStatusUpdate(JobRepository jobRepository, ResourcelessTransactionManager transactionManager) {
		return new JobBuilder("contractSettlementStatusUpdate", jobRepository)
				.start(contractSettlementStatusUpdateAuthStep(jobRepository, transactionManager))
				.next(contractSettlementStatusUpdateStep(jobRepository, transactionManager))
				.build();
	}

	@Bean
	public AuthTask contractSettlementStatusUpdateAuthTask() {
		return new AuthTask();
	}

	@Bean
	public Step contractSettlementStatusUpdateAuthStep(JobRepository jobRepository, ResourcelessTransactionManager transactionManager) {
		return new StepBuilder("contractSettlementStatusUpdateAuthStep", jobRepository).allowStartIfComplete(true)
				.tasklet(contractSettlementStatusUpdateAuthTask(), transactionManager).build();
	}

	@Bean
	public ContractSettlementStatusUpdateTask contractSettlementStatusUpdateTask() {
		return new ContractSettlementStatusUpdateTask();
	}

	@Bean
	public Step contractSettlementStatusUpdateStep(JobRepository jobRepository, ResourcelessTransactionManager transactionManager) {
		return new StepBuilder("contractSettlementStatusUpdateStep", jobRepository).allowStartIfComplete(true)
				.tasklet(contractSettlementStatusUpdateTask(), transactionManager).build();
	}

}
