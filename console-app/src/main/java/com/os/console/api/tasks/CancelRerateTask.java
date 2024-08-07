package com.os.console.api.tasks;

import org.springframework.web.reactive.function.client.WebClient;

import com.os.console.util.RESTUtil;

public class CancelRerateTask implements Runnable {

	private WebClient webClient;
	private String contractId;
	private String rerateId;

	public CancelRerateTask(WebClient webClient, String contractId, String rerateId) {
		this.webClient = webClient;
		this.contractId = contractId;
		this.rerateId = rerateId;
	}

	@Override
	public void run() {
		RESTUtil.postRequest(webClient, "/contracts/" + contractId + "/rerates/" + rerateId + "/cancel");
	}
}
