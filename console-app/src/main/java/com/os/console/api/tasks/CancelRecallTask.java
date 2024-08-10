package com.os.console.api.tasks;

import org.springframework.web.reactive.function.client.WebClient;

import com.os.console.util.RESTUtil;

public class CancelRecallTask implements Runnable {

	private WebClient webClient;
	private String contractId;
	private String recallId;

	public CancelRecallTask(WebClient webClient, String contractId, String recallId) {
		this.webClient = webClient;
		this.contractId = contractId;
		this.recallId = recallId;
	}

	@Override
	public void run() {
		RESTUtil.postRequest(webClient, "/contracts/" + contractId + "/recalls/" + recallId + "/cancel");
	}
}
