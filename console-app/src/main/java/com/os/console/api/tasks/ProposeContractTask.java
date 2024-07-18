package com.os.console.api.tasks;

import org.springframework.web.reactive.function.client.WebClient;

import com.os.client.model.ContractProposal;
import com.os.console.util.RESTUtil;

public class ProposeContractTask implements Runnable {

	private WebClient webClient;
	private ContractProposal contractProposal;

	public ProposeContractTask(WebClient webClient, ContractProposal contractProposal) {
		this.webClient = webClient;
		this.contractProposal = contractProposal;
	}

	@Override
	public void run() {
		RESTUtil.postRequest(webClient, "/contracts", contractProposal);
	}
}
