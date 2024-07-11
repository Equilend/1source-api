package com.os.console.api.tasks;

import java.time.LocalDate;
import java.time.OffsetDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.os.client.model.Delegation;
import com.os.client.model.LedgerResponse;
import com.os.console.api.ConsoleConfig;
import com.os.console.api.LedgerException;
import com.os.console.api.LocalDateTypeGsonAdapter;
import com.os.console.api.OffsetDateTimeTypeGsonAdapter;

public class ApproveDelegationTask implements Runnable {

	private static final Logger logger = LoggerFactory.getLogger(ApproveDelegationTask.class);

	private WebClient webClient;
	private Delegation delegation;

	public ApproveDelegationTask(WebClient webClient, Delegation delegation) {
		this.webClient = webClient;
		this.delegation = delegation;
	}

	@Override
	public void run() {

		Gson gson = new GsonBuilder().setPrettyPrinting()
				.registerTypeAdapter(LocalDate.class, new LocalDateTypeGsonAdapter())
				.registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeTypeGsonAdapter()).create();

//		String json = gson.toJson(delegation);
//		logger.debug(json);

		try {
			LedgerResponse ledgerResponse = webClient.post().uri("/delegations/" + delegation.getDelegationId() + "/approve").contentType(MediaType.APPLICATION_JSON)
//					.bodyValue(json)
					.headers(h -> h.setBearerAuth(ConsoleConfig.TOKEN.getAccess_token())).retrieve()
					.onStatus(HttpStatusCode::is4xxClientError, response -> {
						System.out.println(response.statusCode().toString());
						return response.bodyToMono(LedgerException.class);
					}).bodyToMono(LedgerResponse.class).block();

			if (ledgerResponse != null) {
				System.out.println(ledgerResponse);
				System.out.println();
				if (ledgerResponse.getCode().equals(String.valueOf(HttpStatus.CREATED.value()))) {
					System.out.println("complete");
				}
			} else {
				System.out.println("failed");
			}

		} catch (Exception e) {
			if (e.getCause() instanceof LedgerException) {
				System.out.println(gson.toJson(((LedgerException) e.getCause()).getLedgerResponse()));
				System.out.println();
			} else {
				System.out.println("failed");
			}
		}

	}
}
