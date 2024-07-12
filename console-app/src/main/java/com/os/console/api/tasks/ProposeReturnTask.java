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
import com.os.client.model.LedgerResponse;
import com.os.client.model.ReturnProposal;
import com.os.console.api.ConsoleConfig;
import com.os.console.api.LocalDateTypeGsonAdapter;
import com.os.console.api.OffsetDateTimeTypeGsonAdapter;

import reactor.core.publisher.Mono;

public class ProposeReturnTask implements Runnable {

	private static final Logger logger = LoggerFactory.getLogger(ProposeReturnTask.class);

	private WebClient webClient;
	private String contractId;
	private ReturnProposal returnProposal;

	public ProposeReturnTask(WebClient webClient, String contractId, ReturnProposal returnProposal) {
		this.webClient = webClient;
		this.contractId = contractId;
		this.returnProposal = returnProposal;
	}

	@Override
	public void run() {

		Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateTypeGsonAdapter())
				.registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeTypeGsonAdapter()).create();

		String json = gson.toJson(returnProposal);
		logger.debug(json);

		LedgerResponse ledgerResponse = webClient.post().uri("/contracts/" + contractId + "/returns")
				.contentType(MediaType.APPLICATION_JSON).bodyValue(json)
				.headers(h -> h.setBearerAuth(ConsoleConfig.TOKEN.getAccess_token())).retrieve()
				.onStatus(HttpStatusCode::is4xxClientError, response -> {
					System.out.println(response.statusCode().toString());
					return Mono.empty();
				}).bodyToMono(LedgerResponse.class).block();

		if (ledgerResponse != null && ledgerResponse.getCode().equals(String.valueOf(HttpStatus.CREATED.value()))) {
			System.out.println("complete");
			System.out.println();
			System.out.println(ledgerResponse);
		} else {
			System.out.println("failed");			
		}
	}
}
