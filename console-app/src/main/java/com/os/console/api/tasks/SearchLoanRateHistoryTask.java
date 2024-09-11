package com.os.console.api.tasks;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.WebClient;

import com.os.client.model.Loan;
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

public class SearchLoanRateHistoryTask implements Runnable {

	private static final Logger logger = LoggerFactory.getLogger(SearchLoanRateHistoryTask.class);

	private WebClient webClient;
	private Loan loan;

	public SearchLoanRateHistoryTask(WebClient webClient, Loan loan) {
		this.webClient = webClient;
		this.loan = loan;
	}

	@Override
	public void run() {

		Rates rates = (Rates) RESTUtil.getRequest(webClient,
				"/loans/" + loan.getLoanId() + "/ratehistory", Rates.class);

		if (rates == null || rates.getRates() == null || rates.getRates().size() == 0) {
			logger.warn("Invalid loan rate history object or no loan rate history");
			System.out.println("no loan rate history found");
			printHeader();
		} else {
			System.out.println("complete");
			System.out.print("sorting...");
			ArrayList<LoanRateHistory> history = new ArrayList<>();
			for (Rate loanRate : rates.getRates()) {
				
				if (loanRate instanceof RebateRate) {
					OneOfRebateRateRebate oneOfRebateRateRebate = ((RebateRate) loanRate).getRebate();
					if (oneOfRebateRateRebate instanceof FloatingRate) {
						FloatingRateDef def = ((FloatingRate) oneOfRebateRateRebate).getFloating();
						history.add(new LoanRateHistory(def.getEffectiveDate(), "REBATE FLOATING", def.getSpread(), def.getEffectiveRate(), def.getBenchmark().toString(), def.getBaseRate()));
					} else if (oneOfRebateRateRebate instanceof FixedRate) {
						FixedRateDef def = ((FixedRate) oneOfRebateRateRebate).getFixed();
						history.add(new LoanRateHistory(def.getEffectiveDate(), "REBATE FIXED", null, def.getEffectiveRate(), null, def.getBaseRate()));
					}
				} else if (loanRate instanceof FeeRate) {
					FixedRateDef def = ((FeeRate) loanRate).getFee();
					history.add(new LoanRateHistory(def.getEffectiveDate(), "FEE FIXED", null, def.getEffectiveRate(), null, def.getBaseRate()));
				}
			}

			Collections.sort(history);
			
			System.out.println("complete");

			printHeader();
			int rows = 1;
			for (LoanRateHistory loanRateHistory : history) {

				if (rows % 15 == 0) {
					printHeader();
				}

				System.out.print(ConsoleOutputUtil.padSpaces(loanRateHistory.effectiveDate, 30));
				System.out.print(ConsoleOutputUtil.padSpaces(loanRateHistory.rateType, 30));
				System.out.print(ConsoleOutputUtil.padSpaces(loanRateHistory.spread, 12));
				System.out.print(ConsoleOutputUtil.padSpaces(loanRateHistory.effectiveRate, 15));
				System.out.print(ConsoleOutputUtil.padSpaces(loanRateHistory.benchmark, 10));
				System.out.print(ConsoleOutputUtil.padSpaces(loanRateHistory.baseRate, 10));
				System.out.println();

				rows++;

			}
		}
	}

	class LoanRateHistory implements Comparable<LoanRateHistory> {
		
		LocalDate effectiveDate;
		String rateType;
		Double spread;
		Double effectiveRate;
		String benchmark;
		Double baseRate;

		public LoanRateHistory(LocalDate effectiveDate, String rateType, Double spread, Double effectiveRate,
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
		public int compareTo(LoanRateHistory o) {
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
