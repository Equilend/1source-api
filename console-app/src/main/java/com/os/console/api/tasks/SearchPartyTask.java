package com.os.console.api.tasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.WebClient;

import com.os.client.model.Parties;
import com.os.client.model.Party;
import com.os.console.util.RESTUtil;

public class SearchPartyTask implements Runnable {

	private static final Logger logger = LoggerFactory.getLogger(SearchPartyTask.class);

	private WebClient webClient;
	private String partyId;

	private Party party;
	
	public SearchPartyTask(WebClient webClient, String partyId) {
		this.webClient = webClient;
		this.partyId = partyId;
	}

	@Override
	public void run() {

		Parties parties = (Parties) RESTUtil.getRequest(webClient, "/parties?partyId=" + partyId, Parties.class);

		if (parties == null || parties.size() == 0) {
			logger.warn("Invalid party object or party not found");
			System.out.println("party not found");
		} else {
			party = parties.get(0);
			System.out.println("complete");
		}
	}

	public Party getParty() {
		return party;
	}
	
}
