package com.os.console.api.tasks;

import org.springframework.web.reactive.function.client.WebClient;

import com.os.console.util.RESTUtil;

public class DeclineContractTask implements Runnable {

	private WebClient webClient;
	private String contractId;

	public DeclineContractTask(WebClient webClient, String contractId) {
		this.webClient = webClient;
		this.contractId = contractId;
	}

	@Override
	public void run() {
		RESTUtil.postRequest(webClient, "/contracts/" + contractId + "/decline");
	}
}
