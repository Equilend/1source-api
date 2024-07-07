package com.os.console.api.tasks;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.WebClient;

import com.os.client.model.Delegation;
import com.os.client.model.DelegationAuthorizationType;
import com.os.client.model.Delegations;
import com.os.client.model.Party;
import com.os.console.api.ConsoleConfig;
import com.os.console.util.ConsoleOutputUtil;

import reactor.core.publisher.Mono;

public class SearchDelegationsTask implements Runnable {

	private static final Logger logger = LoggerFactory.getLogger(SearchDelegationsTask.class);

	private WebClient webClient;
	
	public SearchDelegationsTask(WebClient webClient) {
		this.webClient = webClient;
	}

	@Override
	public void run() {
		
		Delegations delegations = webClient.get().uri("/delegations")
				.headers(h -> h.setBearerAuth(ConsoleConfig.TOKEN.getAccess_token())).retrieve()
				.onStatus(HttpStatusCode.valueOf(404)::equals, response -> {
					logger.error(HttpStatus.NOT_FOUND.toString());
					return Mono.empty();
				}).bodyToMono(Delegations.class).block();

		if (delegations == null || delegations.size() == 0) {
			logger.warn("Invalid delegations object or no delegations");			
			System.out.println("no delegations found");
			printHeader();
		} else {
			System.out.println("complete");
			printHeader();
			int rows = 1;
			for (Delegation delegation : delegations) {
				if (rows % 15 == 0) {
					printHeader();
				}
				System.out.print(ConsoleOutputUtil.padSpaces(delegation.getDelegationId(), 40));
				System.out.print(ConsoleOutputUtil.padSpaces(delegation.getDelegationStatus().toString(), 12));
				
				String authorization = null;
				List<DelegationAuthorizationType> authorizations = delegation.getAuthorizations(); //there should only be 1
				if (authorizations != null && authorizations.size() > 0) {
					authorization = authorizations.get(0).toString();
				}
				System.out.print(ConsoleOutputUtil.padSpaces(authorization, 15));
				
				String party1 = null;
				String party2 = null;
				List<Party> parties = delegation.getParties(); //there should be 2
				if (parties != null) {
					if (parties.size() > 0) {
						party1 = parties.get(0).getPartyId();
					}
					
					if (parties.size() > 1) {
						party2 = parties.get(1).getPartyId();
					}
				}
				System.out.print(ConsoleOutputUtil.padSpaces(party1, 15));
				System.out.print(ConsoleOutputUtil.padSpaces(party2, 15));

				System.out.println();
				
				rows++;
			}
		}
	}

	public void printHeader() {
		System.out.println();
		System.out.print(ConsoleOutputUtil.padSpaces("Delegation Id", 40));
		System.out.print(ConsoleOutputUtil.padSpaces("Status", 12));
		System.out.print(ConsoleOutputUtil.padSpaces("Authorization", 15));
		System.out.print(ConsoleOutputUtil.padSpaces("Party 1", 15));
		System.out.print(ConsoleOutputUtil.padSpaces("Party 2", 15));
		System.out.println();
		System.out.print(ConsoleOutputUtil.padSpaces("-------------", 40));
		System.out.print(ConsoleOutputUtil.padSpaces("------", 12));
		System.out.print(ConsoleOutputUtil.padSpaces("-------------", 15));
		System.out.print(ConsoleOutputUtil.padSpaces("-------", 15));
		System.out.print(ConsoleOutputUtil.padSpaces("-------", 15));
		System.out.println();
	}
}
