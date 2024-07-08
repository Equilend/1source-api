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
import com.os.client.model.ContractProposal;
import com.os.client.model.LedgerResponse;
import com.os.console.api.ConsoleConfig;
import com.os.console.api.LocalDateTypeGsonAdapter;
import com.os.console.api.OffsetDateTimeTypeGsonAdapter;

import reactor.core.publisher.Mono;

public class ProposeContractTask implements Runnable {

	private static final Logger logger = LoggerFactory.getLogger(ProposeContractTask.class);

	private WebClient webClient;
	private ContractProposal contractProposal;

	public ProposeContractTask(WebClient webClient, ContractProposal contractProposal) {
		this.webClient = webClient;
		this.contractProposal = contractProposal;
	}

	@Override
	public void run() {

		Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateTypeGsonAdapter())
				.registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeTypeGsonAdapter()).create();

		String json = gson.toJson(contractProposal);
		logger.debug(json);

		LedgerResponse ledgerResponse = webClient.post().uri("/contracts")
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
