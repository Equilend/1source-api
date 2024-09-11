package com.os.console.api.tasks;

import org.springframework.web.reactive.function.client.WebClient;

import com.os.client.model.VenueReferenceKeyUpdate;
import com.os.console.util.RESTUtil;

public class UpdateLoanVenueKeyTask implements Runnable {

	private WebClient webClient;
	private String loanId;
	private String venueRefKey;

	public UpdateLoanVenueKeyTask(WebClient webClient, String loanId, String venueRefKey) {
		this.webClient = webClient;
		this.loanId = loanId;
		this.venueRefKey = venueRefKey;
	}

	@Override
	public void run() {

		VenueReferenceKeyUpdate venueReferenceKeyUpdate = new VenueReferenceKeyUpdate();

		venueReferenceKeyUpdate.setVenueRefKey(venueRefKey);

		RESTUtil.patchRequest(webClient, "/loans/" + loanId, venueReferenceKeyUpdate);

	}
}
