package com.os.console.api.tasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.WebClient;

import com.os.client.model.Rerate;
import com.os.console.util.RESTUtil;

public class SearchLoanRerateTask implements Runnable {

	private static final Logger logger = LoggerFactory.getLogger(SearchLoanRerateTask.class);

	private WebClient webClient;
	private String loanId;
	private String rerateId;

	private Rerate rerate;
	
	public SearchLoanRerateTask(WebClient webClient, String loanId, String rerateId) {
		this.webClient = webClient;
		this.loanId = loanId;
		this.rerateId = rerateId;
	}

	@Override
	public void run() {

		rerate = (Rerate) RESTUtil.getRequest(webClient, "/loans/" + loanId + "/rerates/" + rerateId, Rerate.class);

		if (rerate == null) {
			logger.warn("Invalid rerate object or rerate not found");
			System.out.println("rerate not found");
		} else {
			System.out.println("complete");
			System.out.println();			
		}
	}

	public Rerate getRerate() {
		return rerate;
	}
	
	
}
