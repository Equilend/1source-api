package com.os.replay.tasks;

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;

import com.os.replay.ReplayDBDao;
import com.os.replay.model.HarvestedRecord;
import com.os.replay.model.HarvestedRecordMapper;
import com.os.replay.util.FigiCache;

public class Day0LedgerBInitTask extends RecordReader implements Tasklet, StepExecutionListener {

	private static final Logger logger = LoggerFactory.getLogger(Day0LedgerBInitTask.class);

	private String ledgerBName = "b";
	private LocalDate ledgerBStartDate = LocalDate.parse("2023-12-01");
	private String ledgerBFileName = "src/main/resources/harvest_GS_BNYM.csv";
	
	@Autowired
	ReplayDBDao dao;
	
	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		
		BufferedReader reader = new BufferedReader(new FileReader(ledgerBFileName));
		
		HarvestedRecordMapper mapper = new HarvestedRecordMapper();

		dao.truncate(ledgerBName);

		FigiCache figiCache = FigiCache.getInstance();

		String nextLine;
		while ((nextLine = reader.readLine()) != null) {

			String[] lineParts = nextLine.split(",");
			HarvestedRecord record = mapper.mapRow(lineParts, 0);
			
			if (record.getFileDate().equals(ledgerBStartDate) && "NEW".equals(record.getRowtype())) {
				dao.insert(record, figiCache.getFigi(record.getSedol()), ledgerBName, "BORROWER", "GSCO US", "BNYMELLON");
				
				logger.debug(record.toString());
			}
		}
		
		reader.close();
		
		return RepeatStatus.FINISHED;
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		stepExecution.getJobExecution().getExecutionContext().put("ledgerBName", this.ledgerBName);
		stepExecution.getJobExecution().getExecutionContext().put("ledgerBStartDate", this.ledgerBStartDate);
		return ExitStatus.COMPLETED;
	}

}
