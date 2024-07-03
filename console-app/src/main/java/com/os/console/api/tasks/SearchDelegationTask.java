package com.os.console.api.tasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.WebClient;

import com.os.client.model.Delegation;
import com.os.console.api.AuthConfig;

import reactor.core.publisher.Mono;

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

		delegation = webClient.get().uri("/delegations/" + delegationId)
				.headers(h -> h.setBearerAuth(AuthConfig.TOKEN.getAccess_token())).retrieve()
				.onStatus(HttpStatusCode.valueOf(404)::equals, response -> {
					logger.error(HttpStatus.NOT_FOUND.toString());
					return Mono.empty();
				}).bodyToMono(Delegation.class).block();

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
