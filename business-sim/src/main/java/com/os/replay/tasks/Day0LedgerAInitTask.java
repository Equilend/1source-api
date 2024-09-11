package com.os.replay.tasks;

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDate;
import java.util.ArrayList;

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
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import com.os.replay.ReplayDBDao;
import com.os.replay.model.HarvestedRecord;
import com.os.replay.model.HarvestedRecordMapper;
import com.os.replay.model.OpenFigiV3Request;
import com.os.replay.model.OpenFigiV3RequestWithExchangeCode;
import com.os.replay.model.OpenFigiV3Response;
import com.os.replay.model.OpenFigiV3ResponseData;
import com.os.replay.model.OpenFigiV3ResponseDataItem;
import com.os.replay.util.FigiCache;

public class Day0LedgerAInitTask extends RecordReader implements Tasklet, StepExecutionListener {

	private static final Logger logger = LoggerFactory.getLogger(Day0LedgerAInitTask.class);

	private String ledgerAName = "a";
	private LocalDate ledgerAStartDate = LocalDate.parse("2023-12-01");
	private String ledgerAFileName = "src/main/resources/harvest_lender_borrower.csv";
	
	@Autowired
	ReplayDBDao dao;
	
	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		
		BufferedReader reader = new BufferedReader(new FileReader(ledgerAFileName));
		
		HarvestedRecordMapper mapper = new HarvestedRecordMapper();

		dao.truncate(ledgerAName);

		WebClient figiClient = WebClient.create("https://api.openfigi.com");
		
		FigiCache figiCache = FigiCache.getInstance();
		
		int cnt = 1;
		String nextLine;
		while ((nextLine = reader.readLine()) != null) {

			String[] lineParts = nextLine.split(",");
			HarvestedRecord record = mapper.mapRow(lineParts, 0);

			if (record.getFileDate().equals(ledgerAStartDate) && "NEW".equals(record.getRowtype())) {

				OpenFigiV3ResponseDataItem figi = figiCache.getFigi(record.getSedol());

				if (figi == null) {
					
					ArrayList<OpenFigiV3Request> figiRequests = new ArrayList<OpenFigiV3Request>();
					
					OpenFigiV3RequestWithExchangeCode figiRequestWithExchangeCode = new OpenFigiV3RequestWithExchangeCode();
					figiRequestWithExchangeCode.setIdType("ID_SEDOL");
					figiRequestWithExchangeCode.setIdValue(record.getSedol());
					figiRequestWithExchangeCode.setExchCode(record.getCountryCd());
					figiRequests.add(figiRequestWithExchangeCode);

					OpenFigiV3Request figiRequest = new OpenFigiV3Request();
					figiRequest.setIdType("ID_SEDOL");
					figiRequest.setIdValue(record.getSedol());
					figiRequests.add(figiRequest);
					
					OpenFigiV3Response figiResponse = figiClient.post().uri("/v3/mapping")
							.contentType(MediaType.APPLICATION_JSON).bodyValue(figiRequests).retrieve()
							.bodyToMono(OpenFigiV3Response.class).block();

					if (figiResponse != null) {
						for (OpenFigiV3ResponseData responseData : figiResponse) {
							if (responseData.getData() == null) {
								continue;
							}
							figi = responseData.getData().get(0);
							figiCache.addFigi(record.getSedol(), figi);
							break;
						}
					}
					
					Thread.sleep(5000); //need to avoid open figi rate limits
				}
				
				dao.insert(record, figiCache.getFigi(record.getSedol()), ledgerAName, "LENDER", "USPRIMELEND", "BORROWER-X");
				
			}

			logger.debug(cnt + ": " + record.toString());

			cnt++;
		}
		
		reader.close();
		
		return RepeatStatus.FINISHED;
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		stepExecution.getJobExecution().getExecutionContext().put("ledgerAName", this.ledgerAName);
		stepExecution.getJobExecution().getExecutionContext().put("ledgerAStartDate", this.ledgerAStartDate);
		return ExitStatus.COMPLETED;
	}

}
