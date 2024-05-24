package com.os.contractexport.tasks;

import java.util.ArrayList;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.beans.factory.annotation.Autowired;

import com.os.contractexport.model.PartyAccess;

public class PartyAccessDecider implements JobExecutionDecider {

	@Autowired
	private ArrayList<PartyAccess> partyAccessList;

	@Override
    public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {

		if (partyAccessList.size() > 0) {
			return new FlowExecutionStatus("CONTINUE");
		} else {
			return new FlowExecutionStatus("FINISHED");
		}
    }

}
