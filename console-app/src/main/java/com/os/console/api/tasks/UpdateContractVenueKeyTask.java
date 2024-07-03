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
import com.os.client.model.VenueReferenceKeyUpdate;
import com.os.console.api.AuthConfig;
import com.os.console.api.LocalDateTypeGsonAdapter;
import com.os.console.api.OffsetDateTimeTypeGsonAdapter;

import reactor.core.publisher.Mono;

public class UpdateContractVenueKeyTask implements Runnable {

	private static final Logger logger = LoggerFactory.getLogger(UpdateContractVenueKeyTask.class);

	private WebClient webClient;
	private String contractId;
	private String venueRefKey;

	public UpdateContractVenueKeyTask(WebClient webClient, String contractId, String venueRefKey) {
		this.webClient = webClient;
		this.contractId = contractId;
		this.venueRefKey = venueRefKey;
	}

	@Override
	public void run() {

		VenueReferenceKeyUpdate update = new VenueReferenceKeyUpdate();

		update.setVenueRefKey(venueRefKey);

		Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateTypeGsonAdapter())
				.registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeTypeGsonAdapter()).create();

		String json = gson.toJson(update);
		logger.debug(json);

		LedgerResponse ledgerResponse = webClient.patch().uri("/contracts/" + contractId)
				.contentType(MediaType.APPLICATION_JSON).bodyValue(json)
				.headers(h -> h.setBearerAuth(AuthConfig.TOKEN.getAccess_token())).retrieve()
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
