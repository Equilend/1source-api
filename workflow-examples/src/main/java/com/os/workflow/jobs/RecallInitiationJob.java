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
import com.os.workflow.tasks.RecallInitiationTask;

@Configuration
public class RecallInitiationJob {

	@Bean
	public Job recallInitiation(JobRepository jobRepository, ResourcelessTransactionManager transactionManager) {
		return new JobBuilder("recallInitiation", jobRepository)
				.start(recallInitiationLedgerAuthStep(jobRepository, transactionManager))
				.next(recallInitiationStep(jobRepository, transactionManager))
				.build();
	}

	@Bean
	public AuthTask recallInitiationLedgerAuthTask() {
		return new AuthTask();
	}

	@Bean
	public Step recallInitiationLedgerAuthStep(JobRepository jobRepository, ResourcelessTransactionManager transactionManager) {
		return new StepBuilder("ledgerAuthStep", jobRepository).allowStartIfComplete(true)
				.tasklet(recallInitiationLedgerAuthTask(), transactionManager).build();
	}

	@Bean
	public RecallInitiationTask recallInitiationTask() {
		return new RecallInitiationTask();
	}

	@Bean
	public Step recallInitiationStep(JobRepository jobRepository, ResourcelessTransactionManager transactionManager) {
		return new StepBuilder("recallInitiationStep", jobRepository).allowStartIfComplete(true)
				.tasklet(recallInitiationTask(), transactionManager).build();
	}

}
