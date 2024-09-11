package com.os.console.api.tasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.WebClient;

import com.os.client.model.Loan;
import com.os.console.util.RESTUtil;

public class SearchLoanTask implements Runnable {

	private static final Logger logger = LoggerFactory.getLogger(SearchLoanTask.class);

	private WebClient webClient;
	private String loanId;

	private Loan loan;
	
	public SearchLoanTask(WebClient webClient, String loanId) {
		this.webClient = webClient;
		this.loanId = loanId;
	}

	@Override
	public void run() {

		loan = (Loan) RESTUtil.getRequest(webClient, "/loans/" + loanId, Loan.class);

		if (loan == null || loan.getTrade() == null) {
			logger.warn("Invalid loan object or loan not found");
			System.out.println("loan not found");
		} else {
			System.out.println("complete");
			System.out.println();			
		}
	}

	public Loan getLoan() {
		return loan;
	}
	
	
}
