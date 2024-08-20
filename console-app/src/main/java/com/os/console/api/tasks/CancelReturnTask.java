package com.os.console.api.tasks;

import org.springframework.web.reactive.function.client.WebClient;

import com.os.console.util.RESTUtil;

public class CancelReturnTask implements Runnable {

	private WebClient webClient;
	private String loanId;
	private String returnId;

	public CancelReturnTask(WebClient webClient, String loanId, String returnId) {
		this.webClient = webClient;
		this.loanId = loanId;
		this.returnId = returnId;
	}

	@Override
	public void run() {
		RESTUtil.postRequest(webClient, "/loans/" + loanId + "/returns/" + returnId + "/cancel");
	}
}
