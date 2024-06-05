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
import com.os.workflow.tasks.SettlementStatusUpdateTask;

@Configuration
public class SettlementStatusUpdateJob {

	@Bean
	public Job settlmentStatusUpdate(JobRepository jobRepository, ResourcelessTransactionManager transactionManager) {
		return new JobBuilder("settlementStatusUpdate", jobRepository)
				.start(settlementStatusUpdateAuthStep(jobRepository, transactionManager))
				.next(settlementStatusUpdateStep(jobRepository, transactionManager))
				.build();
	}

	@Bean
	public AuthTask settlementStatusUpdateAuthTask() {
		return new AuthTask();
	}

	@Bean
	public Step settlementStatusUpdateAuthStep(JobRepository jobRepository, ResourcelessTransactionManager transactionManager) {
		return new StepBuilder("settlementStatusUpdateAuthStep", jobRepository).allowStartIfComplete(true)
				.tasklet(settlementStatusUpdateAuthTask(), transactionManager).build();
	}

	@Bean
	public SettlementStatusUpdateTask settlementStatusUpdateTask() {
		return new SettlementStatusUpdateTask();
	}

	@Bean
	public Step settlementStatusUpdateStep(JobRepository jobRepository, ResourcelessTransactionManager transactionManager) {
		return new StepBuilder("settlmentStatusUpdateStep", jobRepository).allowStartIfComplete(true)
				.tasklet(settlementStatusUpdateTask(), transactionManager).build();
	}

}
