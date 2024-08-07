package com.os.console.api.tasks;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.WebClient;

import com.os.client.model.Contract;
import com.os.client.model.Contracts;
import com.os.client.model.PartyRole;
import com.os.client.model.PartySettlementInstruction;
import com.os.client.model.TransactingParties;
import com.os.client.model.TransactingParty;
import com.os.console.util.ConsoleOutputUtil;
import com.os.console.util.RESTUtil;

public class SearchContractsTask implements Runnable {

	private static final Logger logger = LoggerFactory.getLogger(SearchContractsTask.class);

	private WebClient webClient;

	public SearchContractsTask(WebClient webClient) {
		this.webClient = webClient;
	}

	@Override
	public void run() {

		Contracts contracts = (Contracts) RESTUtil.getRequest(webClient, "/contracts", Contracts.class);

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

				String borrowerSettlement = "NONE";
				String lenderSettlement = "NONE";
				List<PartySettlementInstruction> settlementInstructions = contract.getSettlement();
				if (settlementInstructions != null) {
					for (PartySettlementInstruction settlementInstruction : settlementInstructions) {
						if (PartyRole.BORROWER.equals(settlementInstruction.getPartyRole())) {
							borrowerSettlement = settlementInstruction.getSettlementStatus().toString();
						} else if (PartyRole.LENDER.equals(settlementInstruction.getPartyRole())) {
							lenderSettlement = settlementInstruction.getSettlementStatus().toString();
						}

					}
				}

				System.out.print(ConsoleOutputUtil.padSpaces(contract.getContractId(), 40));
				System.out.print(ConsoleOutputUtil.padSpaces(contract.getContractStatus().toString(), 12));

				System.out.print(ConsoleOutputUtil.padSpaces(borrower, 15));
				System.out.print(ConsoleOutputUtil.padSpaces(borrowerSettlement, 15));
				System.out.print(ConsoleOutputUtil.padSpaces(lender, 15));
				System.out.print(ConsoleOutputUtil.padSpaces(lenderSettlement, 15));

				System.out.print(ConsoleOutputUtil.padSpaces(contract.getTrade().getTradeDate(), 15));
				System.out.print(ConsoleOutputUtil.padSpaces(contract.getTrade().getInstrument().getTicker(), 15));
				System.out.print(ConsoleOutputUtil.padSpaces(contract.getTrade().getQuantity(), 15));
				System.out.print(ConsoleOutputUtil.padSpaces(contract.getTrade().getOpenQuantity(), 15));
				System.out.println();

				rows++;
			}
		}
	}

	public void printHeader() {
		System.out.println();
		System.out.print(ConsoleOutputUtil.padSpaces("Contract Id", 40));
		System.out.print(ConsoleOutputUtil.padSpaces("Status", 12));
		System.out.print(ConsoleOutputUtil.padSpaces("Borrower", 15));
		System.out.print(ConsoleOutputUtil.padSpaces("Settlement", 15));
		System.out.print(ConsoleOutputUtil.padSpaces("Lender", 15));
		System.out.print(ConsoleOutputUtil.padSpaces("Settlement", 15));
		System.out.print(ConsoleOutputUtil.padSpaces("Trade Date", 15));
		System.out.print(ConsoleOutputUtil.padSpaces("Ticker", 15));
		System.out.print(ConsoleOutputUtil.padSpaces("Orig Quantity", 15));
		System.out.print(ConsoleOutputUtil.padSpaces("Open Quantity", 15));
		System.out.println();
		System.out.print(ConsoleOutputUtil.padSpaces("-----------", 40));
		System.out.print(ConsoleOutputUtil.padSpaces("------", 12));
		System.out.print(ConsoleOutputUtil.padSpaces("--------", 15));
		System.out.print(ConsoleOutputUtil.padSpaces("----------", 15));
		System.out.print(ConsoleOutputUtil.padSpaces("------", 15));
		System.out.print(ConsoleOutputUtil.padSpaces("----------", 15));
		System.out.print(ConsoleOutputUtil.padSpaces("----------", 15));
		System.out.print(ConsoleOutputUtil.padSpaces("------", 15));
		System.out.print(ConsoleOutputUtil.padSpaces("-------------", 15));
		System.out.print(ConsoleOutputUtil.padSpaces("-------------", 15));
		System.out.println();
	}
}
