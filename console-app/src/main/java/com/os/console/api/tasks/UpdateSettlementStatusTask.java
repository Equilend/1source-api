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
import com.os.client.model.Contract;
import com.os.client.model.LedgerResponse;
import com.os.client.model.SettlementStatus;
import com.os.client.model.SettlementStatusUpdate;
import com.os.client.model.TransactingParties;
import com.os.client.model.TransactingParty;
import com.os.console.api.ConsoleConfig;
import com.os.console.api.LocalDateTypeGsonAdapter;
import com.os.console.api.OffsetDateTimeTypeGsonAdapter;

import reactor.core.publisher.Mono;

public class UpdateSettlementStatusTask implements Runnable {

	private static final Logger logger = LoggerFactory.getLogger(UpdateSettlementStatusTask.class);

	private WebClient webClient;
	private Contract contract;
	private String actingParty;

	public UpdateSettlementStatusTask(WebClient webClient, Contract contract, String actingParty) {
		this.webClient = webClient;
		this.contract = contract;
		this.actingParty = actingParty;
	}

	@Override
	public void run() {

		TransactingParties parties = contract.getTrade().getTransactingParties();
		boolean canAct = false;
		for (TransactingParty transactingParty : parties) {
			if (actingParty.equals(transactingParty.getParty().getPartyId())) {
				canAct = true;
				break;
			}
		}
		
		if (!canAct) {
			System.out.println("Not a transacting party");			
			return;
		}
		
		SettlementStatusUpdate update = new SettlementStatusUpdate();

		update.setSettlementStatus(SettlementStatus.SETTLED);

		Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateTypeGsonAdapter())
				.registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeTypeGsonAdapter()).create();

		String json = gson.toJson(update);
		logger.debug(json);

		LedgerResponse ledgerResponse = webClient.patch().uri("/contracts/" + contract.getContractId())
				.contentType(MediaType.APPLICATION_JSON).bodyValue(json)
				.headers(h -> h.setBearerAuth(ConsoleConfig.TOKEN.getAccess_token())).retrieve()
				.onStatus(HttpStatusCode::is4xxClientError, response -> {
					System.out.println(response.statusCode().toString());
					return Mono.empty();
				}).bodyToMono(LedgerResponse.class).block();

		if (ledgerResponse != null && ledgerResponse.getCode().equals(String.valueOf(HttpStatus.OK.value()))) {
			System.out.println("complete");
			System.out.println();
			System.out.println(ledgerResponse);
		} else {
			System.out.println("failed");			
		}
	}
}