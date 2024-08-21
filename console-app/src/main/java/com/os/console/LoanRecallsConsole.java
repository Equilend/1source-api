package com.os.console;

import java.io.BufferedReader;
import java.util.UUID;

import org.springframework.web.reactive.function.client.WebClient;

import com.os.client.model.Loan;
import com.os.client.model.RecallProposal;
import com.os.console.api.ConsoleConfig;
import com.os.console.api.tasks.CancelRecallTask;
import com.os.console.api.tasks.ProposeRecallTask;
import com.os.console.api.tasks.SearchLoanRecallTask;
import com.os.console.api.tasks.SearchLoanRecallsTask;
import com.os.console.util.ConsoleOutputUtil;
import com.os.console.util.PayloadUtil;

public class LoanRecallsConsole extends AbstractConsole {

	Loan loan;

	public LoanRecallsConsole(Loan loan) {
		this.loan = loan;
	}

	protected boolean prompt() {
		
		if (loan == null) {
			System.out.println("Loan not available");
			return false;
		}
		
		System.out.print("/loans/" + loan.getLoanId() + "/recalls > ");
		return true;
	}

	public void handleArgs(String args[], BufferedReader consoleIn, WebClient webClient) {
		if (args[0].equals("I")) {
			System.out.print("Listing all recalls...");
			SearchLoanRecallsTask searchLoanRecallsTask = new SearchLoanRecallsTask(webClient, loan);
			Thread taskT = new Thread(searchLoanRecallsTask);
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
				String recallId = args[1];
				try {
					if (UUID.fromString(recallId).toString().equals(recallId)) {
						System.out.print("Retrieving recall " + recallId + "...");
						SearchLoanRecallTask searchLoanRecallTask = new SearchLoanRecallTask(webClient,
								loan.getLoanId(), recallId);
						Thread taskT = new Thread(searchLoanRecallTask);
						taskT.run();
						try {
							taskT.join();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						if (searchLoanRecallTask.getRecall() != null) {
							LoanRecallConsole loanRecallConsole = new LoanRecallConsole(loan,
									searchLoanRecallTask.getRecall());
							loanRecallConsole.execute(consoleIn, webClient);
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
					System.out.print("Notifying recall...");

					RecallProposal recallProposal = PayloadUtil.createRecallProposal(quantity);

					ConsoleOutputUtil.printObject(recallProposal);

					ProposeRecallTask proposeRecallTask = new ProposeRecallTask(webClient, loan, recallProposal,
							ConsoleConfig.ACTING_PARTY);
					Thread taskT = new Thread(proposeRecallTask);
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
				String recallId = args[1];
				try {
					if (UUID.fromString(recallId).toString().equals(recallId)) {
						System.out.print("Cancelling recall " + recallId + "...");
						CancelRecallTask cancelRecallTask = new CancelRecallTask(webClient, loan.getLoanId(),
								recallId);
						Thread taskT = new Thread(cancelRecallTask);
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
		System.out.println("Loan Recalls Menu");
		System.out.println("-----------------------");
		System.out.println("I                   - List all recalls");
		System.out.println("S <Recall ID>       - Load recall by Id");
		System.out.println();
		System.out.println("R <Quantity>        - Notify recall");
		System.out.println("C <Recall ID>       - Cancel recall by Id");
		System.out.println();
		System.out.println("X                   - Go back");
	}

}
