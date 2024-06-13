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
import com.os.workflow.tasks.ReturnQueryTask;

@Configuration
public class ReturnQueryJob {

	@Bean
	public Job returnQuery(JobRepository jobRepository, ResourcelessTransactionManager transactionManager) {
		return new JobBuilder("returnQuery", jobRepository)
				.start(returnQueryAuthStep(jobRepository, transactionManager))
				.next(returnQueryStep(jobRepository, transactionManager))
				.build();
	}

	@Bean
	public AuthTask returnQueryAuthTask() {
		return new AuthTask();
	}

	@Bean
	public Step returnQueryAuthStep(JobRepository jobRepository, ResourcelessTransactionManager transactionManager) {
		return new StepBuilder("returnQueryAuthStep", jobRepository).allowStartIfComplete(true)
				.tasklet(returnQueryAuthTask(), transactionManager).build();
	}

	@Bean
	public ReturnQueryTask returnQueryTask() {
		return new ReturnQueryTask();
	}

	@Bean
	public Step returnQueryStep(JobRepository jobRepository, ResourcelessTransactionManager transactionManager) {
		return new StepBuilder("returnQueryStep", jobRepository).allowStartIfComplete(true)
				.tasklet(returnQueryTask(), transactionManager).build();
	}

}
