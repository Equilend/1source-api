package com.os.console.api.tasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.WebClient;

import com.os.client.model.Recall;
import com.os.console.api.ConsoleConfig;

import reactor.core.publisher.Mono;

public class SearchRecallTask implements Runnable {

	private static final Logger logger = LoggerFactory.getLogger(SearchRecallTask.class);

	private WebClient webClient;
	private String recallId;

	private Recall recall;
	
	public SearchRecallTask(WebClient webClient, String recallId) {
		this.webClient = webClient;
		this.recallId = recallId;
	}

	@Override
	public void run() {

		recall = webClient.get().uri("/recalls/" + recallId)
				.headers(h -> h.setBearerAuth(ConsoleConfig.TOKEN.getAccess_token())).retrieve()
				.onStatus(HttpStatusCode.valueOf(404)::equals, response -> {
					logger.error(HttpStatus.NOT_FOUND.toString());
					return Mono.empty();
				}).bodyToMono(Recall.class).block();

		if (recall == null) {
			logger.warn("Invalid recall object or recall not found");
			System.out.println("recall not found");
		} else {
			System.out.println("complete");
			System.out.println();			
		}
	}

	public Recall getRecall() {
		return recall;
	}
	
	
}
