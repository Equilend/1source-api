package com.os.console.api.tasks;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.WebClient;

import com.os.client.model.Contract;
import com.os.client.model.FeeRate;
import com.os.client.model.FixedRate;
import com.os.client.model.FixedRateDef;
import com.os.client.model.FloatingRate;
import com.os.client.model.FloatingRateDef;
import com.os.client.model.OneOfRebateRateRebate;
import com.os.client.model.Rate;
import com.os.client.model.Rates;
import com.os.client.model.RebateRate;
import com.os.console.util.ConsoleOutputUtil;
import com.os.console.util.RESTUtil;

public class SearchContractRateHistoryTask implements Runnable {

	private static final Logger logger = LoggerFactory.getLogger(SearchContractRateHistoryTask.class);

	private WebClient webClient;
	private Contract contract;

	public SearchContractRateHistoryTask(WebClient webClient, Contract contract) {
		this.webClient = webClient;
		this.contract = contract;
	}

	@Override
	public void run() {

		Rates rates = (Rates) RESTUtil.getRequest(webClient,
				"/contracts/" + contract.getContractId() + "/ratehistory", Rates.class);

		if (rates == null || rates.getRates() == null || rates.getRates().size() == 0) {
			logger.warn("Invalid contract rate history object or no contract rate history");
			System.out.println("no contract rate history found");
			printHeader();
		} else {
			System.out.println("complete");
			System.out.print("sorting...");
			ArrayList<ContractRateHistory> history = new ArrayList<>();
			for (Rate contractRate : rates.getRates()) {
				
				if (contractRate instanceof RebateRate) {
					OneOfRebateRateRebate oneOfRebateRateRebate = ((RebateRate) contractRate).getRebate();
					if (oneOfRebateRateRebate instanceof FloatingRate) {
						FloatingRateDef def = ((FloatingRate) oneOfRebateRateRebate).getFloating();
						history.add(new ContractRateHistory(def.getEffectiveDate(), "REBATE FLOATING", def.getSpread(), def.getEffectiveRate(), def.getBenchmark().toString(), def.getBaseRate()));
					} else if (oneOfRebateRateRebate instanceof FixedRate) {
						FixedRateDef def = ((FixedRate) oneOfRebateRateRebate).getFixed();
						history.add(new ContractRateHistory(def.getEffectiveDate(), "REBATE FIXED", null, def.getEffectiveRate(), null, def.getBaseRate()));
					}
				} else if (contractRate instanceof FeeRate) {
					FixedRateDef def = ((FeeRate) contractRate).getFee();
					history.add(new ContractRateHistory(def.getEffectiveDate(), "FEE FIXED", null, def.getEffectiveRate(), null, def.getBaseRate()));
				}
			}

			Collections.sort(history);
			
			System.out.println("complete");

			printHeader();
			int rows = 1;
			for (ContractRateHistory contractRateHistory : history) {

				if (rows % 15 == 0) {
					printHeader();
				}

				System.out.print(ConsoleOutputUtil.padSpaces(contractRateHistory.effectiveDate, 30));
				System.out.print(ConsoleOutputUtil.padSpaces(contractRateHistory.rateType, 30));
				System.out.print(ConsoleOutputUtil.padSpaces(contractRateHistory.spread, 12));
				System.out.print(ConsoleOutputUtil.padSpaces(contractRateHistory.effectiveRate, 15));
				System.out.print(ConsoleOutputUtil.padSpaces(contractRateHistory.benchmark, 10));
				System.out.print(ConsoleOutputUtil.padSpaces(contractRateHistory.baseRate, 10));
				System.out.println();

				rows++;

			}
		}
	}

	class ContractRateHistory implements Comparable<ContractRateHistory> {
		
		LocalDate effectiveDate;
		String rateType;
		Double spread;
		Double effectiveRate;
		String benchmark;
		Double baseRate;

		public ContractRateHistory(LocalDate effectiveDate, String rateType, Double spread, Double effectiveRate,
				String benchmark, Double baseRate) {
			super();
			this.effectiveDate = effectiveDate;
			this.rateType = rateType;
			this.spread = spread;
			this.effectiveRate = effectiveRate;
			this.benchmark = benchmark;
			this.baseRate = baseRate;
		}


		@Override
		public int compareTo(ContractRateHistory o) {
			if (o.effectiveDate == null) {
				return 1;
			} else if (this.effectiveDate == null) {
				return -1;
			}
			return o.effectiveDate.compareTo(this.effectiveDate);
		}
	}

	public void printHeader() {
		System.out.println();
		System.out.print(ConsoleOutputUtil.padSpaces("Effective Date", 30));
		System.out.print(ConsoleOutputUtil.padSpaces("Rate Type", 30));
		System.out.print(ConsoleOutputUtil.padSpaces("Spread", 12));
		System.out.print(ConsoleOutputUtil.padSpaces("Effective Rate", 15));
		System.out.print(ConsoleOutputUtil.padSpaces("Benchmark", 10));
		System.out.print(ConsoleOutputUtil.padSpaces("Base Rate", 10));
		System.out.println();
		System.out.print(ConsoleOutputUtil.padSpaces("--------------", 30));
		System.out.print(ConsoleOutputUtil.padSpaces("---------", 30));
		System.out.print(ConsoleOutputUtil.padSpaces("------", 12));
		System.out.print(ConsoleOutputUtil.padSpaces("--------------", 15));
		System.out.print(ConsoleOutputUtil.padSpaces("---------", 10));
		System.out.print(ConsoleOutputUtil.padSpaces("---------", 10));
		System.out.println();
	}
}
