package com.os.console;

import java.io.BufferedReader;
import java.util.UUID;

import org.springframework.web.reactive.function.client.WebClient;

import com.os.client.model.Loan;
import com.os.client.model.ReturnProposal;
import com.os.console.api.ConsoleConfig;
import com.os.console.api.tasks.CancelReturnTask;
import com.os.console.api.tasks.ProposeReturnTask;
import com.os.console.api.tasks.SearchLoanReturnTask;
import com.os.console.api.tasks.SearchLoanReturnsTask;
import com.os.console.util.ConsoleOutputUtil;
import com.os.console.util.PayloadUtil;

public class LoanReturnsConsole extends AbstractConsole {

	Loan loan;

	public LoanReturnsConsole(Loan loan) {
		this.loan = loan;
	}

	protected boolean prompt() {
		
		if (loan == null) {
			System.out.println("Loan not available");
			return false;
		}
		
		System.out.print("/loans/" + loan.getLoanId() + "/returns > ");
		
		return true;
	}

	public void handleArgs(String args[], BufferedReader consoleIn, WebClient webClient) {

		if (args[0].equals("I")) {
			System.out.print("Listing all returns...");
			SearchLoanReturnsTask searchLoanReturnsTask = new SearchLoanReturnsTask(webClient, loan);
			Thread taskT = new Thread(searchLoanReturnsTask);
			taskT.run();
			try {
				taskT.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} else if (args[0].equals("S")) {
			if (args.length != 2 || args[1].length() == 0 || args[1].length() != 36) {
				System.out.println("Invalid UUID");
			} else {
				String returnId = args[1];
				try {
					if (UUID.fromString(returnId).toString().equals(returnId)) {
						System.out.print("Retrieving return " + returnId + "...");
						SearchLoanReturnTask searchLoanReturnTask = new SearchLoanReturnTask(webClient,
								loan, returnId);
						Thread taskT = new Thread(searchLoanReturnTask);
						taskT.run();
						try {
							taskT.join();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						if (searchLoanReturnTask.getReturn() != null) {
							LoanReturnConsole loanReturnConsole = new LoanReturnConsole(loan,
									searchLoanReturnTask.getReturn());
							loanReturnConsole.execute(consoleIn, webClient);
						}
					} else {
						System.out.println("Invalid UUID");
					}
				} catch (Exception u) {
					System.out.println("Invalid UUID");
				}
			}
		} else if (args[0].equals("R")) {
			if (args.length != 2 || args[1].length() == 0 || args[1].length() > 15) {
				System.out.println("Invalid quantity");
			} else {
				Integer quantity = Integer.valueOf(args[1]);
				try {
					System.out.print("Notifying return...");

					ReturnProposal returnProposal = PayloadUtil.createReturnProposal(loan, quantity);

					ConsoleOutputUtil.printObject(returnProposal);

					ProposeReturnTask proposeReturnTask = new ProposeReturnTask(webClient, loan, returnProposal,
							ConsoleConfig.ACTING_PARTY);
					Thread taskT = new Thread(proposeReturnTask);
					taskT.run();
					try {
						taskT.join();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} catch (Exception u) {
					System.out.println("Invalid quantity");
				}
			}
		} else if (args[0].equals("C")) {
			if (args.length != 2 || args[1].length() == 0 || args[1].length() != 36) {
				System.out.println("Invalid UUID");
			} else {
				String returnId = args[1];
				try {
					if (UUID.fromString(returnId).toString().equals(returnId)) {
						System.out.print("Cancelling return " + returnId + "...");
						CancelReturnTask cancelReturnTask = new CancelReturnTask(webClient, loan.getLoanId(),
								returnId);
						Thread taskT = new Thread(cancelReturnTask);
						taskT.run();
						try {
							taskT.join();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					} else {
						System.out.println("Invalid UUID");
					}
				} catch (Exception u) {
					System.out.println("Invalid UUID");
				}
			}
		} else {
			System.out.println("Unknown command");
		}
	}

	protected void printMenu() {
		System.out.println("Loan Returns Menu");
		System.out.println("-----------------------");
		System.out.println("I                   - List all returns");
		System.out.println("S <Return ID>       - Load return by Id");
		System.out.println();
		System.out.println("R <Quantity>        - Notify return");
		System.out.println("C <Return ID>       - Cancel return by Id");
		System.out.println();
		System.out.println("X                   - Go back");
	}

}
