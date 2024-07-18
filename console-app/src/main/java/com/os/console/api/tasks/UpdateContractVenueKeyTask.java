package com.os.console.api.tasks;

import org.springframework.web.reactive.function.client.WebClient;

import com.os.client.model.VenueReferenceKeyUpdate;
import com.os.console.util.RESTUtil;

public class UpdateContractVenueKeyTask implements Runnable {

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

		VenueReferenceKeyUpdate venueReferenceKeyUpdate = new VenueReferenceKeyUpdate();

		venueReferenceKeyUpdate.setVenueRefKey(venueRefKey);

		RESTUtil.patchRequest(webClient, "/contracts/" + contractId, venueReferenceKeyUpdate);

	}
}
