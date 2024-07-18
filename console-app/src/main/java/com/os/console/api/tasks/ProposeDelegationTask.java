package com.os.console.api.tasks;

import org.springframework.web.reactive.function.client.WebClient;

import com.os.client.model.DelegationProposal;
import com.os.console.util.RESTUtil;

public class ProposeDelegationTask implements Runnable {

	private WebClient webClient;
	private DelegationProposal delegationProposal;

	public ProposeDelegationTask(WebClient webClient, DelegationProposal delegationProposal) {
		this.webClient = webClient;
		this.delegationProposal = delegationProposal;
	}

	@Override
	public void run() {
		RESTUtil.postRequest(webClient, "/delegations", delegationProposal);
	}
}
