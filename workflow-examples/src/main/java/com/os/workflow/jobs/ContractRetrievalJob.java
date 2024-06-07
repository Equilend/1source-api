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
import com.os.workflow.tasks.ContractRetrievalTask;

@Configuration
public class ContractRetrievalJob {

	@Bean
	public Job contractRetrieval(JobRepository jobRepository, ResourcelessTransactionManager transactionManager) {
		return new JobBuilder("contractRetrieval", jobRepository)
				.start(contractRetrievalAuthStep(jobRepository, transactionManager))
				.next(contractRetrievalStep(jobRepository, transactionManager))
				.build();
	}

	@Bean
	public AuthTask contractRetrievalAuthTask() {
		return new AuthTask();
	}

	@Bean
	public Step contractRetrievalAuthStep(JobRepository jobRepository, ResourcelessTransactionManager transactionManager) {
		return new StepBuilder("contractRetrievalAuthStep", jobRepository).allowStartIfComplete(true)
				.tasklet(contractRetrievalAuthTask(), transactionManager).build();
	}

	@Bean
	public ContractRetrievalTask contractRetrievalTask() {
		return new ContractRetrievalTask();
	}

	@Bean
	public Step contractRetrievalStep(JobRepository jobRepository, ResourcelessTransactionManager transactionManager) {
		return new StepBuilder("contractRetrievalStep", jobRepository).allowStartIfComplete(true)
				.tasklet(contractRetrievalTask(), transactionManager).build();
	}

}
