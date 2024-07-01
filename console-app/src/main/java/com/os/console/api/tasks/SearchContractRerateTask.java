package com.os.console.api.tasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.WebClient;

import com.os.client.model.Rerate;
import com.os.console.api.AuthConfig;

import reactor.core.publisher.Mono;

public class SearchContractRerateTask implements Runnable {

	private static final Logger logger = LoggerFactory.getLogger(SearchContractRerateTask.class);

	private WebClient webClient;
	private String contractId;
	private String rerateId;

	private Rerate rerate;
	
	public SearchContractRerateTask(WebClient webClient, String contractId, String rerateId) {
		this.webClient = webClient;
		this.contractId = contractId;
		this.rerateId = rerateId;
	}

	@Override
	public void run() {

		rerate = webClient.get().uri("/contracts/" + contractId + "/rerates/" + rerateId)
				.headers(h -> h.setBearerAuth(AuthConfig.TOKEN.getAccess_token())).retrieve()
				.onStatus(HttpStatusCode.valueOf(404)::equals, response -> {
					logger.error(HttpStatus.NOT_FOUND.toString());
					return Mono.empty();
				}).bodyToMono(Rerate.class).block();

		if (rerate == null) {
			logger.warn("Invalid rerate object or rerate not found");
			System.out.println("rerate not found");
		} else {
			System.out.println("complete");
			System.out.println();			
		}
	}

	public Rerate getRerate() {
		return rerate;
	}
	
	
}
