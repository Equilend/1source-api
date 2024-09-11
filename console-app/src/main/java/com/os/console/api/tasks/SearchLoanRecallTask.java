package com.os.console.api.tasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.WebClient;

import com.os.client.model.Recall;
import com.os.console.util.RESTUtil;

public class SearchLoanRecallTask implements Runnable {

	private static final Logger logger = LoggerFactory.getLogger(SearchLoanRecallTask.class);

	private WebClient webClient;
	private String loanId;
	private String recallId;

	private Recall recall;
	
	public SearchLoanRecallTask(WebClient webClient, String loanId, String recallId) {
		this.webClient = webClient;
		this.loanId = loanId;
		this.recallId = recallId;
	}

	@Override
	public void run() {

		recall = (Recall) RESTUtil.getRequest(webClient, "/loans/" + loanId + "/recalls/" + recallId, Recall.class);
		
		if (recall == null) {
			logger.warn("Invalid recall object or recall not found");
			System.out.println("recall not found");
		} else {
			System.out.println("complete");
			System.out.println();			
		}
	}

	public Recall getRecall() {
		return recall;
	}
	
	
}
