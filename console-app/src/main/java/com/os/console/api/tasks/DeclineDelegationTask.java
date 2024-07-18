package com.os.console.api.tasks;

import org.springframework.web.reactive.function.client.WebClient;

import com.os.console.util.RESTUtil;

public class DeclineDelegationTask implements Runnable {

	private WebClient webClient;
	private String delegationId;

	public DeclineDelegationTask(WebClient webClient, String delegationId) {
		this.webClient = webClient;
		this.delegationId = delegationId;
	}

	@Override
	public void run() {
		RESTUtil.postRequest(webClient, "/delegations/" + delegationId + "/decline");
	}
}
