package com.os.console.api.tasks;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.WebClient;

import com.os.client.model.Loan;
import com.os.client.model.Loans;
import com.os.client.model.FeeRate;
import com.os.client.model.FixedRate;
import com.os.client.model.FloatingRate;
import com.os.client.model.OneOfRebateRateRebate;
import com.os.client.model.Rate;
import com.os.client.model.RebateRate;
import com.os.console.util.ConsoleOutputUtil;
import com.os.console.util.RESTUtil;

public class SearchLoanHistoryTask implements Runnable {

	private static final Logger logger = LoggerFactory.getLogger(SearchLoanHistoryTask.class);

	private WebClient webClient;
	private Loan loan;

	public SearchLoanHistoryTask(WebClient webClient, Loan loan) {
		this.webClient = webClient;
		this.loan = loan;
	}

	@Override
	public void run() {

		Loans loans = (Loans) RESTUtil.getRequest(webClient,
				"/loans/" + loan.getLoanId() + "/history", Loans.class);

		if (loans == null || loans.size() == 0) {
			logger.warn("Invalid loan history object or no loan history");
			System.out.println("no loan history found");
			printHeader();
		} else {
			System.out.println("complete");
			System.out.print("sorting...");
			ArrayList<LoanHistory> history = new ArrayList<>();
			for (Loan loan : loans) {
				Double rate = null;
				Double effectiveRate = null;
				Rate loanRate = loan.getTrade().getRate();
				if (loanRate instanceof RebateRate) {
					OneOfRebateRateRebate oneOfRebateRateRebate = ((RebateRate) loanRate).getRebate();
					if (oneOfRebateRateRebate instanceof FloatingRate) {
						rate = ((FloatingRate) oneOfRebateRateRebate).getFloating().getSpread();
						effectiveRate = ((FloatingRate) oneOfRebateRateRebate).getFloating().getEffectiveRate();
					} else if (oneOfRebateRateRebate instanceof FixedRate) {
						rate = ((FixedRate) oneOfRebateRateRebate).getFixed().getBaseRate();
						effectiveRate = ((FloatingRate) oneOfRebateRateRebate).getFloating().getEffectiveRate();
					}
				} else if (loanRate instanceof FeeRate) {
					rate = ((FeeRate) loanRate).getFee().getBaseRate();
					effectiveRate = ((FeeRate) loanRate).getFee().getEffectiveRate();
				}

				history.add(new LoanHistory(loan.getLastUpdateDateTime(),
						loan.getLastEvent().getEventType().toString(), loan.getLoanStatus().toString(),
						loan.getTrade().getQuantity(), loan.getTrade().getOpenQuantity(), rate, effectiveRate,
						loan.getTrade().getCollateral().getContractPrice()));
			}

			Collections.sort(history);
			
			System.out.println("complete");

			printHeader();
			int rows = 1;
			for (LoanHistory loanHistory : history) {

				if (rows % 15 == 0) {
					printHeader();
				}

				System.out.print(ConsoleOutputUtil.padSpaces(loanHistory.lastUpdateDateTime, 30));
				System.out.print(ConsoleOutputUtil.padSpaces(loanHistory.eventType, 30));
				System.out.print(ConsoleOutputUtil.padSpaces(loanHistory.loanStatus, 12));
				System.out.print(ConsoleOutputUtil.padSpaces(loanHistory.origQuantity, 15));
				System.out.print(ConsoleOutputUtil.padSpaces(loanHistory.openQuantity, 15));
				System.out.print(ConsoleOutputUtil.padSpaces(loanHistory.rate, 10));
				System.out.print(ConsoleOutputUtil.padSpaces(loanHistory.effectiveRate, 15));
				System.out.print(ConsoleOutputUtil.padSpaces(loanHistory.price, 15));
				System.out.println();

				rows++;

			}
		}
	}

	class LoanHistory implements Comparable<LoanHistory> {
		OffsetDateTime lastUpdateDateTime;
		String eventType;
		String loanStatus;
		Integer origQuantity;
		Integer openQuantity;
		Double rate;
		Double effectiveRate;
		Double price;

		public LoanHistory(OffsetDateTime lastUpdateDateTime, String eventType, String loanStatus,
				Integer origQuantity, Integer openQuantity, Double rate, Double effectiveRate, Double price) {
			super();
			this.lastUpdateDateTime = lastUpdateDateTime;
			this.eventType = eventType;
			this.loanStatus = loanStatus;
			this.origQuantity = origQuantity;
			this.openQuantity = openQuantity;
			this.rate = rate;
			this.effectiveRate = effectiveRate;
			this.price = price;
		}

		@Override
		public int compareTo(LoanHistory o) {
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
