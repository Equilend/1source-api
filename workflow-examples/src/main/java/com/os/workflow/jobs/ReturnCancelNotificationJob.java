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
import com.os.workflow.tasks.ReturnCancelNotificationTask;

@Configuration
public class ReturnCancelNotificationJob {

	@Bean
	public Job returnCancelNotification(JobRepository jobRepository, ResourcelessTransactionManager transactionManager) {
		return new JobBuilder("returnCancelNotification", jobRepository)
				.start(returnCancelNotificationAuthStep(jobRepository, transactionManager))
				.next(returnCancelNotificationStep(jobRepository, transactionManager))
				.build();
	}

	@Bean
	public AuthTask returnCancelNotificationAuthTask() {
		return new AuthTask();
	}

	@Bean
	public Step returnCancelNotificationAuthStep(JobRepository jobRepository, ResourcelessTransactionManager transactionManager) {
		return new StepBuilder("returnCancelNotificationAuthStep", jobRepository).allowStartIfComplete(true)
				.tasklet(returnCancelNotificationAuthTask(), transactionManager).build();
	}

	@Bean
	public ReturnCancelNotificationTask returnCancelNotificationTask() {
		return new ReturnCancelNotificationTask();
	}

	@Bean
	public Step returnCancelNotificationStep(JobRepository jobRepository, ResourcelessTransactionManager transactionManager) {
		return new StepBuilder("returnCancelNotificationStep", jobRepository).allowStartIfComplete(true)
				.tasklet(returnCancelNotificationTask(), transactionManager).build();
	}

}
