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
import com.os.workflow.tasks.ReturnSettlementStatusUpdateTask;

@Configuration
public class ReturnSettlementStatusUpdateJob {

	@Bean
	public Job returnSettlementStatusUpdate(JobRepository jobRepository, ResourcelessTransactionManager transactionManager) {
		return new JobBuilder("returnSettlementStatusUpdate", jobRepository)
				.start(returnSettlementStatusUpdateAuthStep(jobRepository, transactionManager))
				.next(returnSettlementStatusUpdateStep(jobRepository, transactionManager))
				.build();
	}

	@Bean
	public AuthTask returnSettlementStatusUpdateAuthTask() {
		return new AuthTask();
	}

	@Bean
	public Step returnSettlementStatusUpdateAuthStep(JobRepository jobRepository, ResourcelessTransactionManager transactionManager) {
		return new StepBuilder("returnSettlementStatusUpdateAuthStep", jobRepository).allowStartIfComplete(true)
				.tasklet(returnSettlementStatusUpdateAuthTask(), transactionManager).build();
	}

	@Bean
	public ReturnSettlementStatusUpdateTask returnSettlementStatusUpdateTask() {
		return new ReturnSettlementStatusUpdateTask();
	}

	@Bean
	public Step returnSettlementStatusUpdateStep(JobRepository jobRepository, ResourcelessTransactionManager transactionManager) {
		return new StepBuilder("returnSettlementStatusUpdateStep", jobRepository).allowStartIfComplete(true)
				.tasklet(returnSettlementStatusUpdateTask(), transactionManager).build();
	}

}
