package com.os.console.api.tasks;

import org.springframework.web.reactive.function.client.WebClient;

import com.os.client.model.LoanProposal;
import com.os.console.util.RESTUtil;

public class ProposeLoanTask implements Runnable {

	private WebClient webClient;
	private LoanProposal loanProposal;

	public ProposeLoanTask(WebClient webClient, LoanProposal loanProposal) {
		this.webClient = webClient;
		this.loanProposal = loanProposal;
	}

	@Override
	public void run() {
		RESTUtil.postRequest(webClient, "/loans", loanProposal);
	}
}
