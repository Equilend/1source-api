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
import com.os.workflow.tasks.ReturnNotificationTask;

@Configuration
public class ReturnNotificationJob {

	@Bean
	public Job returnNotification(JobRepository jobRepository, ResourcelessTransactionManager transactionManager) {
		return new JobBuilder("returnNotification", jobRepository)
				.start(returnNotificationLedgerAuthStep(jobRepository, transactionManager))
				.next(returnNotificationStep(jobRepository, transactionManager))
				.build();
	}

	@Bean
	public AuthTask returnNotificationLedgerAuthTask() {
		return new AuthTask();
	}

	@Bean
	public Step returnNotificationLedgerAuthStep(JobRepository jobRepository, ResourcelessTransactionManager transactionManager) {
		return new StepBuilder("ledgerAuthStep", jobRepository).allowStartIfComplete(true)
				.tasklet(returnNotificationLedgerAuthTask(), transactionManager).build();
	}

	@Bean
	public ReturnNotificationTask returnNotificationTask() {
		return new ReturnNotificationTask();
	}

	@Bean
	public Step returnNotificationStep(JobRepository jobRepository, ResourcelessTransactionManager transactionManager) {
		return new StepBuilder("returnNotificationStep", jobRepository).allowStartIfComplete(true)
				.tasklet(returnNotificationTask(), transactionManager).build();
	}

}
