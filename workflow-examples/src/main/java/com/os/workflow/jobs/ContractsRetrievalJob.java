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
import com.os.workflow.tasks.ContractsRetrievalTask;

@Configuration
public class ContractsRetrievalJob {

	@Bean
	public Job contractsRetrieval(JobRepository jobRepository, ResourcelessTransactionManager transactionManager) {
		return new JobBuilder("contractsRetrieval", jobRepository)
				.start(contractsRetrievalAuthStep(jobRepository, transactionManager))
				.next(contractsRetrievalStep(jobRepository, transactionManager))
				.build();
	}

	@Bean
	public AuthTask contractsRetrievalAuthTask() {
		return new AuthTask();
	}

	@Bean
	public Step contractsRetrievalAuthStep(JobRepository jobRepository, ResourcelessTransactionManager transactionManager) {
		return new StepBuilder("contractsRetrievalAuthStep", jobRepository).allowStartIfComplete(true)
				.tasklet(contractsRetrievalAuthTask(), transactionManager).build();
	}

	@Bean
	public ContractsRetrievalTask contractsRetrievalTask() {
		return new ContractsRetrievalTask();
	}

	@Bean
	public Step contractsRetrievalStep(JobRepository jobRepository, ResourcelessTransactionManager transactionManager) {
		return new StepBuilder("contractsRetrievalStep", jobRepository).allowStartIfComplete(true)
				.tasklet(contractsRetrievalTask(), transactionManager).build();
	}

}
