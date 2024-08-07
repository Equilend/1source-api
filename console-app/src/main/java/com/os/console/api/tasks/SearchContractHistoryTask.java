package com.os.console.api.tasks;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.WebClient;

import com.os.client.model.Contract;
import com.os.client.model.Contracts;
import com.os.client.model.FeeRate;
import com.os.client.model.FixedRate;
import com.os.client.model.FloatingRate;
import com.os.client.model.OneOfRebateRateRebate;
import com.os.client.model.Rate;
import com.os.client.model.RebateRate;
import com.os.console.util.ConsoleOutputUtil;
import com.os.console.util.RESTUtil;

public class SearchContractHistoryTask implements Runnable {

	private static final Logger logger = LoggerFactory.getLogger(SearchContractHistoryTask.class);

	private WebClient webClient;
	private Contract contract;

	public SearchContractHistoryTask(WebClient webClient, Contract contract) {
		this.webClient = webClient;
		this.contract = contract;
	}

	@Override
	public void run() {

		Contracts contracts = (Contracts) RESTUtil.getRequest(webClient,
				"/contracts/" + contract.getContractId() + "/history", Contracts.class);

		if (contracts == null || contracts.size() == 0) {
			logger.warn("Invalid contract history object or no contract history");
			System.out.println("no contract history found");
			printHeader();
		} else {
			System.out.println("complete");
			System.out.print("sorting...");
			ArrayList<ContractHistory> history = new ArrayList<>();
			for (Contract contract : contracts) {
				Double rate = null;
				Double effectiveRate = null;
				Rate contractRate = contract.getTrade().getRate();
				if (contractRate instanceof RebateRate) {
					OneOfRebateRateRebate oneOfRebateRateRebate = ((RebateRate) contractRate).getRebate();
					if (oneOfRebateRateRebate instanceof FloatingRate) {
						rate = ((FloatingRate) oneOfRebateRateRebate).getFloating().getSpread();
						effectiveRate = ((FloatingRate) oneOfRebateRateRebate).getFloating().getEffectiveRate();
					} else if (oneOfRebateRateRebate instanceof FixedRate) {
						rate = ((FixedRate) oneOfRebateRateRebate).getFixed().getBaseRate();
						effectiveRate = ((FloatingRate) oneOfRebateRateRebate).getFloating().getEffectiveRate();
					}
				} else if (contractRate instanceof FeeRate) {
					rate = ((FeeRate) contractRate).getFee().getBaseRate();
					effectiveRate = ((FeeRate) contractRate).getFee().getEffectiveRate();
				}

				history.add(new ContractHistory(contract.getLastUpdateDateTime(),
						contract.getLastEvent().getEventType().toString(), contract.getContractStatus().toString(),
						contract.getTrade().getQuantity(), contract.getTrade().getOpenQuantity(), rate, effectiveRate,
						contract.getTrade().getCollateral().getContractPrice()));
			}

			Collections.sort(history);
			
			System.out.println("complete");

			printHeader();
			int rows = 1;
			for (ContractHistory contractHistory : history) {

				if (rows % 15 == 0) {
					printHeader();
				}

				System.out.print(ConsoleOutputUtil.padSpaces(contractHistory.lastUpdateDateTime, 30));
				System.out.print(ConsoleOutputUtil.padSpaces(contractHistory.eventType, 30));
				System.out.print(ConsoleOutputUtil.padSpaces(contractHistory.contractStatus, 12));
				System.out.print(ConsoleOutputUtil.padSpaces(contractHistory.origQuantity, 15));
				System.out.print(ConsoleOutputUtil.padSpaces(contractHistory.openQuantity, 15));
				System.out.print(ConsoleOutputUtil.padSpaces(contractHistory.rate, 10));
				System.out.print(ConsoleOutputUtil.padSpaces(contractHistory.effectiveRate, 15));
				System.out.print(ConsoleOutputUtil.padSpaces(contractHistory.price, 15));
				System.out.println();

				rows++;

			}
		}
	}

	class ContractHistory implements Comparable<ContractHistory> {
		OffsetDateTime lastUpdateDateTime;
		String eventType;
		String contractStatus;
		Integer origQuantity;
		Integer openQuantity;
		Double rate;
		Double effectiveRate;
		Double price;

		public ContractHistory(OffsetDateTime lastUpdateDateTime, String eventType, String contractStatus,
				Integer origQuantity, Integer openQuantity, Double rate, Double effectiveRate, Double price) {
			super();
			this.lastUpdateDateTime = lastUpdateDateTime;
			this.eventType = eventType;
			this.contractStatus = contractStatus;
			this.origQuantity = origQuantity;
			this.openQuantity = openQuantity;
			this.rate = rate;
			this.effectiveRate = effectiveRate;
			this.price = price;
		}

		@Override
		public int compareTo(ContractHistory o) {
			if (o.lastUpdateDateTime == null) {
				return 1;
			} else if (this.lastUpdateDateTime == null) {
				return -1;
			}
			return o.lastUpdateDateTime.compareTo(this.lastUpdateDateTime);
		}
	}

	public void printHeader() {
		System.out.println();
		System.out.print(ConsoleOutputUtil.padSpaces("Last Update", 30));
		System.out.print(ConsoleOutputUtil.padSpaces("Event", 30));
		System.out.print(ConsoleOutputUtil.padSpaces("Status", 12));
		System.out.print(ConsoleOutputUtil.padSpaces("Orig Quantity", 15));
		System.out.print(ConsoleOutputUtil.padSpaces("Open Quantity", 15));
		System.out.print(ConsoleOutputUtil.padSpaces("Rate", 10));
		System.out.print(ConsoleOutputUtil.padSpaces("Effective Rate", 15));
		System.out.print(ConsoleOutputUtil.padSpaces("Price", 15));
		System.out.println();
		System.out.print(ConsoleOutputUtil.padSpaces("-----------", 30));
		System.out.print(ConsoleOutputUtil.padSpaces("-----", 30));
		System.out.print(ConsoleOutputUtil.padSpaces("------", 12));
		System.out.print(ConsoleOutputUtil.padSpaces("-------------", 15));
		System.out.print(ConsoleOutputUtil.padSpaces("-------------", 15));
		System.out.print(ConsoleOutputUtil.padSpaces("----", 10));
		System.out.print(ConsoleOutputUtil.padSpaces("--------------", 15));
		System.out.print(ConsoleOutputUtil.padSpaces("-----", 15));
		System.out.println();
	}
}
