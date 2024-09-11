package com.os.console.api.tasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.WebClient;

import com.os.client.model.Loan;
import com.os.client.model.ModelReturn;
import com.os.console.util.RESTUtil;

public class SearchLoanReturnTask implements Runnable {

	private static final Logger logger = LoggerFactory.getLogger(SearchLoanReturnTask.class);

	private WebClient webClient;
	private Loan loan;
	private String returnId;

	private ModelReturn modelReturn;
	
	public SearchLoanReturnTask(WebClient webClient, Loan loan, String returnId) {
		this.webClient = webClient;
		this.loan = loan;
		this.returnId = returnId;
	}

	@Override
	public void run() {

		modelReturn = (ModelReturn) RESTUtil.getRequest(webClient, "/loans/" + loan.getLoanId() + "/returns/" + returnId, ModelReturn.class);

		if (modelReturn == null) {
			logger.warn("Invalid return object or return not found");
			System.out.println("return not found");
		} else {
			System.out.println("complete");
			System.out.println();			
		}
	}

	public ModelReturn getReturn() {
		return modelReturn;
	}
	
	
}
