package com.os.contractexport;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.os.contractexport.tasks.ContractCSVExportTask;
import com.os.contractexport.tasks.LedgerAuthTask;
import com.os.contractexport.tasks.PartyAccessDecider;

@Configuration
public class ContractCSVExportJob {

	@Bean
	public Job contractCSVExport(JobRepository jobRepository, ResourcelessTransactionManager transactionManager, Flow contractCSVExportFlow, JobCompletionNotificationListener listener) {
		return new JobBuilder("contractCSVExport", jobRepository)
				.listener(listener)
				.start(contractCSVExportFlow)
				.end()
				.build();
	}

	@Bean
	public PartyAccessDecider partyAccessListener() {
		return new PartyAccessDecider();
	}
	
	@Bean
	public LedgerAuthTask ledgerAuthTask() {
		return new LedgerAuthTask();
	}

	@Bean
	public Step authStep(JobRepository jobRepository, ResourcelessTransactionManager transactionManager) {
		return new StepBuilder("auth", jobRepository).allowStartIfComplete(true)
				.tasklet(ledgerAuthTask(), transactionManager).build();
	}

	@Bean
	public ContractCSVExportTask contractCSVExportTask() {
		return new ContractCSVExportTask();
	}

	@Bean
	public Step extractStep(JobRepository jobRepository, ResourcelessTransactionManager transactionManager) {
		return new StepBuilder("extract", jobRepository).allowStartIfComplete(true)
				.tasklet(contractCSVExportTask(), transactionManager)
				.build();
	}

	@Bean
	public Flow contractCSVExportFlow(Step authStep, Step extractStep, PartyAccessDecider decider) {
		
		return new FlowBuilder<Flow>("contractCSVExportFlow")
				.start(authStep)
				.next(extractStep)
				.on("*")
				.to(decider)
				.from(decider)
				.on("CONTINUE")
				.to(authStep)
				.from(decider)
				.on("FINISHED")
				.end("COMPLETED")
				.build();
	}
}
