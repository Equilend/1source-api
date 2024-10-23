package com.os.marktomarket.tasks;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.WebClient;

import com.os.marktomarket.LoanMark;

public class CalculateMarkTask implements Tasklet, StepExecutionListener {

	private static final Logger logger = LoggerFactory.getLogger(CalculateMarkTask.class);

	private List<LoanMark> loanMarks;

	@Autowired
	WebClient restWebClient;

	@SuppressWarnings("unchecked")
	@Override
	public void beforeStep(StepExecution stepExecution) {
		loanMarks = (List<LoanMark>) stepExecution.getJobExecution().getExecutionContext().get("marks");
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

		BigDecimal totalMark = BigDecimal.ZERO;
		
		for (LoanMark loanMark : loanMarks) {
			totalMark = totalMark.add(new BigDecimal(loanMark.getCurrentMark() - loanMark.getLastMark()));
		}

		totalMark = totalMark.setScale(2, RoundingMode.HALF_UP);

		logger.info("Total mark: " + totalMark.toPlainString());
		
		return RepeatStatus.FINISHED;
	}

}
