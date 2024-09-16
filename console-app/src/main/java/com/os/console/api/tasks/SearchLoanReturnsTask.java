package com.os.console.api.tasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.WebClient;

import com.os.client.model.Loan;
import com.os.client.model.ModelReturn;
import com.os.client.model.Returns;
import com.os.console.util.ConsoleOutputUtil;
import com.os.console.util.RESTUtil;

public class SearchLoanReturnsTask implements Runnable {

	private static final Logger logger = LoggerFactory.getLogger(SearchLoanReturnsTask.class);

	private WebClient webClient;
	private Loan loan;
	
	public SearchLoanReturnsTask(WebClient webClient, Loan loan) {
		this.webClient = webClient;
		this.loan = loan;
	}

	@Override
	public void run() {

		Returns returns = (Returns) RESTUtil.getRequest(webClient, "/loans/" + loan.getLoanId() + "/returns", Returns.class);

		if (returns == null || returns.size() == 0) {
			logger.warn("Invalid returns object or no returns");			
			System.out.println("no returns found");
			printHeader();
		} else {
			System.out.println("complete");
			printHeader();
			int rows = 1;
			for (ModelReturn modelReturn : returns) {
				if (rows % 15 == 0) {
					printHeader();
				}
				System.out.print(ConsoleOutputUtil.padSpaces(modelReturn.getReturnId(), 40));
				System.out.print(ConsoleOutputUtil.padSpaces(modelReturn.getStatus().toString(), 12));
				System.out.print(ConsoleOutputUtil.padSpaces(modelReturn.getLastUpdateDatetime(), 30));
				System.out.print(ConsoleOutputUtil.padSpaces(modelReturn.getReturnDate(), 15));
				System.out.print(ConsoleOutputUtil.padSpaces(modelReturn.getQuantity(), 15));
				System.out.println();
				
				rows++;
			}
		}
		System.out.println();
	}

	public void printHeader() {
		System.out.println();
		System.out.print(ConsoleOutputUtil.padSpaces("Return Id", 40));
		System.out.print(ConsoleOutputUtil.padSpaces("Status", 12));
		System.out.print(ConsoleOutputUtil.padSpaces("Last Update DateTime", 30));
		System.out.print(ConsoleOutputUtil.padSpaces("Return Date", 15));
		System.out.print(ConsoleOutputUtil.padSpaces("Quantity", 15));
		System.out.println();
		System.out.print(ConsoleOutputUtil.padSpaces("---------", 40));
		System.out.print(ConsoleOutputUtil.padSpaces("------", 12));
		System.out.print(ConsoleOutputUtil.padSpaces("--------------------", 30));
		System.out.print(ConsoleOutputUtil.padSpaces("-----------", 15));
		System.out.print(ConsoleOutputUtil.padSpaces("--------", 15));
		System.out.println();
	}
}
