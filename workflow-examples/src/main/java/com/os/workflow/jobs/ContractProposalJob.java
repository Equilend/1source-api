package com.os.workflow.jobs;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.os.workflow.tasks.AuthTask;

@Configuration
public class ContractProposalJob {

	@Bean
	public Job contractProposal(JobRepository jobRepository, DataSourceTransactionManager transactionManager) {
		return new JobBuilder("contractProposal", jobRepository).start(ledgerAuth(jobRepository, transactionManager))
				.build();
	}

	@Bean
	public AuthTask ledgerAuthTask() {
		return new AuthTask();
	}

	@Bean
	public Step ledgerAuth(JobRepository jobRepository, DataSourceTransactionManager transactionManager) {
		return new StepBuilder("ledgerAuth", jobRepository).allowStartIfComplete(true)
				.tasklet(ledgerAuthTask(), transactionManager).build();
	}

}
