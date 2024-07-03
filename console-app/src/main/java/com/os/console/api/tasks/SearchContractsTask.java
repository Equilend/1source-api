package com.os.console.api.tasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.WebClient;

import com.os.client.model.Contract;
import com.os.client.model.Contracts;
import com.os.client.model.PartyRole;
import com.os.client.model.TransactingParties;
import com.os.client.model.TransactingParty;
import com.os.console.api.AuthConfig;
import com.os.console.util.ConsoleOutputUtil;

import reactor.core.publisher.Mono;

public class SearchContractsTask implements Runnable {

	private static final Logger logger = LoggerFactory.getLogger(SearchContractsTask.class);

	private WebClient webClient;

	public SearchContractsTask(WebClient webClient) {
		this.webClient = webClient;
	}

	@Override
	public void run() {

		Contracts contracts = webClient.get().uri("/contracts")
				.headers(h -> h.setBearerAuth(AuthConfig.TOKEN.getAccess_token())).retrieve()
				.onStatus(HttpStatusCode.valueOf(404)::equals, response -> {
					logger.error(HttpStatus.NOT_FOUND.toString());
					return Mono.empty();
				}).bodyToMono(Contracts.class).block();

		if (contracts == null || contracts.size() == 0) {
			logger.warn("Invalid contracts object or no contracts");
			System.out.println("no contracts found");
			printHeader();
		} else {
			System.out.println("complete");
			printHeader();
			int rows = 1;
			for (Contract contract : contracts) {
				if (rows % 15 == 0) {
					printHeader();
				}
				System.out.print(ConsoleOutputUtil.padSpaces(contract.getContractId(), 40));

				String borrower = null;
				String lender = null;
				TransactingParties parties = contract.getTrade().getTransactingParties(); // there should be 2
				if (parties != null) {
					for (TransactingParty party : parties) {
						if (borrower == null && PartyRole.BORROWER.equals(party.getPartyRole())) {
							borrower = party.getParty().getPartyId();
						} else if (lender == null && PartyRole.LENDER.equals(party.getPartyRole())) {
							lender = party.getParty().getPartyId();
						}

					}
				}
				System.out.print(ConsoleOutputUtil.padSpaces(borrower, 15));
				System.out.print(ConsoleOutputUtil.padSpaces(lender, 15));

				System.out.print(ConsoleOutputUtil.padSpaces(contract.getTrade().getTradeDate(), 15));
				System.out.print(ConsoleOutputUtil.padSpaces(contract.getContractStatus().toString(), 12));
				System.out.print(ConsoleOutputUtil.padSpaces(contract.getTrade().getInstrument().getFigi(), 15));
				System.out.print(ConsoleOutputUtil.padSpaces(contract.getTrade().getInstrument().getTicker(), 15));
				System.out.print(ConsoleOutputUtil.padSpaces(contract.getTrade().getQuantity(), 15));
				System.out.println();

				rows++;
			}
		}
	}

	public void printHeader() {
		System.out.println();
		System.out.print(ConsoleOutputUtil.padSpaces("Contract Id", 40));
		System.out.print(ConsoleOutputUtil.padSpaces("Borrower", 15));
		System.out.print(ConsoleOutputUtil.padSpaces("Lender", 15));
		System.out.print(ConsoleOutputUtil.padSpaces("Trade Date", 15));
		System.out.print(ConsoleOutputUtil.padSpaces("Status", 12));
		System.out.print(ConsoleOutputUtil.padSpaces("Figi", 15));
		System.out.print(ConsoleOutputUtil.padSpaces("Ticker", 15));
		System.out.print(ConsoleOutputUtil.padSpaces("Quantity", 15));
		System.out.println();
		System.out.print(ConsoleOutputUtil.padSpaces("-----------", 40));
		System.out.print(ConsoleOutputUtil.padSpaces("--------", 15));
		System.out.print(ConsoleOutputUtil.padSpaces("------", 15));
		System.out.print(ConsoleOutputUtil.padSpaces("----------", 15));
		System.out.print(ConsoleOutputUtil.padSpaces("------", 12));
		System.out.print(ConsoleOutputUtil.padSpaces("----", 15));
		System.out.print(ConsoleOutputUtil.padSpaces("------", 15));
		System.out.print(ConsoleOutputUtil.padSpaces("--------", 15));
		System.out.println();
	}
}
