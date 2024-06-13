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
import com.os.workflow.tasks.ReturnAcknowledgeNotificationTask;

@Configuration
public class ReturnAcknowledgeNotificationJob {

	@Bean
	public Job returnAcknowledgeNotification(JobRepository jobRepository, ResourcelessTransactionManager transactionManager) {
		return new JobBuilder("returnAcknowledgeNotification", jobRepository)
				.start(returnAcknowledgeNotificationAuthStep(jobRepository, transactionManager))
				.next(returnAcknowledgeNotificationStep(jobRepository, transactionManager))
				.build();
	}

	@Bean
	public AuthTask returnAcknowledgeNotificationAuthTask() {
		return new AuthTask();
	}

	@Bean
	public Step returnAcknowledgeNotificationAuthStep(JobRepository jobRepository, ResourcelessTransactionManager transactionManager) {
		return new StepBuilder("returnAcknowledgeNotificationAuthStep", jobRepository).allowStartIfComplete(true)
				.tasklet(returnAcknowledgeNotificationAuthTask(), transactionManager).build();
	}

	@Bean
	public ReturnAcknowledgeNotificationTask returnAcknowledgeNotificationTask() {
		return new ReturnAcknowledgeNotificationTask();
	}

	@Bean
	public Step returnAcknowledgeNotificationStep(JobRepository jobRepository, ResourcelessTransactionManager transactionManager) {
		return new StepBuilder("returnAcknowledgeNotificationStep", jobRepository).allowStartIfComplete(true)
				.tasklet(returnAcknowledgeNotificationTask(), transactionManager).build();
	}

}
