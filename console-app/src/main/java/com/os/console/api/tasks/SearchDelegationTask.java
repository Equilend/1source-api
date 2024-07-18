package com.os.console.api.tasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.WebClient;

import com.os.client.model.Delegation;
import com.os.console.util.RESTUtil;

public class SearchDelegationTask implements Runnable {

	private static final Logger logger = LoggerFactory.getLogger(SearchDelegationTask.class);

	private WebClient webClient;
	private String delegationId;

	private Delegation delegation;
	
	public SearchDelegationTask(WebClient webClient, String delegationId) {
		this.webClient = webClient;
		this.delegationId = delegationId;
	}

	@Override
	public void run() {

		delegation = (Delegation) RESTUtil.getRequest(webClient, "/delegations/" + delegationId, Delegation.class);

		if (delegation == null) {
			logger.warn("Invalid delegation object or delegation not found");
			System.out.println("delegation not found");
		} else {
			System.out.println("complete");
			System.out.println();			
		}
	}

	public Delegation getDelegation() {
		return delegation;
	}
	
	
}
