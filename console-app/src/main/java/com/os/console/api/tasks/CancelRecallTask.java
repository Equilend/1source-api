package com.os.console.api.tasks;

import org.springframework.web.reactive.function.client.WebClient;

import com.os.console.util.RESTUtil;

public class CancelRecallTask implements Runnable {

	private WebClient webClient;
	private String loanId;
	private String recallId;

	public CancelRecallTask(WebClient webClient, String loanId, String recallId) {
		this.webClient = webClient;
		this.loanId = loanId;
		this.recallId = recallId;
	}

	@Override
	public void run() {
		RESTUtil.postRequest(webClient, "/loans/" + loanId + "/recalls/" + recallId + "/cancel");
	}
}
