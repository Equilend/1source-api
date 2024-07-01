package com.os.console.api.tasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.WebClient;

import com.os.client.model.ModelReturn;
import com.os.console.api.AuthConfig;

import reactor.core.publisher.Mono;

public class SearchContractReturnTask implements Runnable {

	private static final Logger logger = LoggerFactory.getLogger(SearchContractReturnTask.class);

	private WebClient webClient;
	private String contractId;
	private String returnId;

	private ModelReturn returnObj;
	
	public SearchContractReturnTask(WebClient webClient, String contractId, String returnId) {
		this.webClient = webClient;
		this.contractId = contractId;
		this.returnId = returnId;
	}

	@Override
	public void run() {

		returnObj = webClient.get().uri("/contracts/" + contractId + "/returns/" + returnId)
				.headers(h -> h.setBearerAuth(AuthConfig.TOKEN.getAccess_token())).retrieve()
				.onStatus(HttpStatusCode.valueOf(404)::equals, response -> {
					logger.error(HttpStatus.NOT_FOUND.toString());
					return Mono.empty();
				}).bodyToMono(ModelReturn.class).block();

		if (returnObj == null) {
			logger.warn("Invalid return object or return not found");
			System.out.println("return not found");
		} else {
			System.out.println("complete");
			System.out.println();			
		}
	}

	public ModelReturn getReturn() {
		return returnObj;
	}
	
	
}
