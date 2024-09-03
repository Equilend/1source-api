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
import com.os.workflow.tasks.LoanSettlementStatusUpdateTask;

@Configuration
public class LoanSettlementStatusUpdateJob {

	@Bean
	public Job loanSettlementStatusUpdate(JobRepository jobRepository, ResourcelessTransactionManager transactionManager) {
		return new JobBuilder("loanSettlementStatusUpdate", jobRepository)
				.start(loanSettlementStatusUpdateAuthStep(jobRepository, transactionManager))
				.next(loanSettlementStatusUpdateStep(jobRepository, transactionManager))
				.build();
	}

	@Bean
	public AuthTask loanSettlementStatusUpdateAuthTask() {
		return new AuthTask();
	}

	@Bean
	public Step loanSettlementStatusUpdateAuthStep(JobRepository jobRepository, ResourcelessTransactionManager transactionManager) {
		return new StepBuilder("loanSettlementStatusUpdateAuthStep", jobRepository).allowStartIfComplete(true)
				.tasklet(loanSettlementStatusUpdateAuthTask(), transactionManager).build();
	}

	@Bean
	public LoanSettlementStatusUpdateTask loanSettlementStatusUpdateTask() {
		return new LoanSettlementStatusUpdateTask();
	}

	@Bean
	public Step loanSettlementStatusUpdateStep(JobRepository jobRepository, ResourcelessTransactionManager transactionManager) {
		return new StepBuilder("loanSettlementStatusUpdateStep", jobRepository).allowStartIfComplete(true)
				.tasklet(loanSettlementStatusUpdateTask(), transactionManager).build();
	}

}
