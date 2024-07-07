package com.os.console.api.tasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.WebClient;

import com.os.client.model.Contract;
import com.os.console.api.ConsoleConfig;

import reactor.core.publisher.Mono;

public class SearchContractTask implements Runnable {

	private static final Logger logger = LoggerFactory.getLogger(SearchContractTask.class);

	private WebClient webClient;
	private String contractId;

	private Contract contract;
	
	public SearchContractTask(WebClient webClient, String contractId) {
		this.webClient = webClient;
		this.contractId = contractId;
	}

	@Override
	public void run() {

		contract = webClient.get().uri("/contracts/" + contractId)
				.headers(h -> h.setBearerAuth(ConsoleConfig.TOKEN.getAccess_token())).retrieve()
				.onStatus(HttpStatusCode.valueOf(404)::equals, response -> {
					logger.error(HttpStatus.NOT_FOUND.toString());
					return Mono.empty();
				}).bodyToMono(Contract.class).block();

		if (contract == null || contract.getTrade() == null) {
			logger.warn("Invalid contract object or contract not found");
			System.out.println("contract not found");
		} else {
			System.out.println("complete");
			System.out.println();			
		}
	}

	public Contract getContract() {
		return contract;
	}
	
	
}
