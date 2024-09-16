package com.os.console;

import java.io.BufferedReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.WebClient;

import com.os.client.model.Loan;
import com.os.console.api.ConsoleConfig;
import com.os.console.api.tasks.ApproveLoanTask;
import com.os.console.api.tasks.CancelLoanTask;
import com.os.console.api.tasks.DeclineLoanTask;
import com.os.console.api.tasks.SearchLoanHistoryTask;
import com.os.console.api.tasks.SearchLoanRateHistoryTask;
import com.os.console.api.tasks.SearchLoanTask;
import com.os.console.api.tasks.UpdateLoanVenueKeyTask;
import com.os.console.api.tasks.UpdateLoanSettlementStatusTask;
import com.os.console.util.ConsoleOutputUtil;
import com.os.console.util.PayloadUtil;

public class LoanConsole extends AbstractConsole {

	private static final Logger logger = LoggerFactory.getLogger(LoanConsole.class);

	Loan loan;

	public LoanConsole(Loan loan) {
		this.loan = loan;
	}

	protected boolean prompt() {
		
		if (loan == null) {
			System.out.println("Loan not available");
			return false;
		}
		
		System.out.print(ConsoleConfig.ACTING_PARTY.getPartyId() + " /loans/" + loan.getLoanId() + " > ");
		
		return true;
	}

	public void handleArgs(String args[], BufferedReader consoleIn, WebClient webClient) {

		if (args[0].equals("J")) {
			ConsoleOutputUtil.printObject(loan);
		} else if (args[0].equals("F")) {
			refreshLoan(webClient);
		} else if (args[0].equals("H")) {
			System.out.print("Listing full history...");
			SearchLoanHistoryTask searchLoanHistoryTask = new SearchLoanHistoryTask(webClient, loan);
			Thread taskT = new Thread(searchLoanHistoryTask);
			taskT.run();
			try {
				taskT.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} else if (args[0].equals("Y")) {
			System.out.print("Listing rate change history...");
			SearchLoanRateHistoryTask searchLoanRateHistoryTask = new SearchLoanRateHistoryTask(webClient, loan);
			Thread taskT = new Thread(searchLoanRateHistoryTask);
			taskT.run();
			try {
				taskT.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} else if (args[0].equals("A")) {
		
			System.out.print("Approving loan...");
			ApproveLoanTask approveLoanTask = new ApproveLoanTask(webClient, loan,
					PayloadUtil.createLoanProposalApproval());
			Thread taskT = new Thread(approveLoanTask);
			taskT.run();
			try {
				taskT.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			refreshLoan(webClient);
		} else if (args[0].equals("C")) {
			System.out.print("Canceling loan...");
			CancelLoanTask cancelLoanTask = new CancelLoanTask(webClient, loan.getLoanId());
			Thread taskT = new Thread(cancelLoanTask);
			taskT.run();
			try {
				taskT.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			refreshLoan(webClient);
		} else if (args[0].equals("D")) {
			System.out.print("Declining loan...");
			DeclineLoanTask declineLoanTask = new DeclineLoanTask(webClient, loan.getLoanId());
			Thread taskT = new Thread(declineLoanTask);
			taskT.run();
			try {
				taskT.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			refreshLoan(webClient);
		} else if (args[0].equals("U")) {
			System.out.print("Updating settlement status to SETTLED...");
			UpdateLoanSettlementStatusTask updateSettlementStatusTask = new UpdateLoanSettlementStatusTask(
					webClient, loan, ConsoleConfig.ACTING_PARTY);
			Thread taskT = new Thread(updateSettlementStatusTask);
			taskT.run();
			try {
				taskT.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			refreshLoan(webClient);
		} else if (args[0].equals("V")) {
			if (args.length != 2 || args[1].length() == 0 || args[1].length() > 50) {
				System.out.println("Invalid reference key");
			} else {
				String venueRefKey = args[1];
				try {
					System.out.print("Assigning venue reference " + venueRefKey + "...");
					UpdateLoanVenueKeyTask updateLoanVenueKeyTask = new UpdateLoanVenueKeyTask(webClient,
							loan.getLoanId(), venueRefKey);
					Thread taskT = new Thread(updateLoanVenueKeyTask);
					taskT.run();
					try {
						taskT.join();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					refreshLoan(webClient);
				} catch (Exception u) {
					System.out.println("Invalid reference key");
				}
			}
		} else if (args[0].equals("R")) {
			LoanReturnsConsole loanReturnsConsole = new LoanReturnsConsole(loan);
			loanReturnsConsole.execute(consoleIn, webClient);
		} else if (args[0].equals("E")) {
			LoanRecallsConsole loanRecallsConsole = new LoanRecallsConsole(loan);
			loanRecallsConsole.execute(consoleIn, webClient);
		} else if (args[0].equals("T")) {
			LoanReratesConsole loanReratesConsole = new LoanReratesConsole(loan);
			loanReratesConsole.execute(consoleIn, webClient);
		} else {
			System.out.println("Unknown command");
		}
	}

	private void refreshLoan(WebClient webClient) {

		System.out.print("Refreshing loan " + loan.getLoanId() + "...");
		SearchLoanTask searchLoanTask = new SearchLoanTask(webClient, loan.getLoanId());
		Thread taskT = new Thread(searchLoanTask);
		taskT.run();
		try {
			taskT.join();
		} catch (InterruptedException e) {
			logger.error(e.getMessage(), e);
		}
		loan = searchLoanTask.getLoan();
	}

	protected void printMenu() {
		System.out.println("Loan Menu");
		System.out.println("-----------------------");
		System.out.println("J                   - Print JSON");
		System.out.println("F                   - Refresh");
		System.out.println("H                   - Full history");
		System.out.println("Y                   - Rate change history");
		System.out.println();
		System.out.println("A                   - Approve");
		System.out.println("C                   - Cancel");
		System.out.println("D                   - Decline");
		System.out.println("U                   - Update settlement status to SETTLED");
		System.out.println("V <Venue Ref>       - Add a venue reference key");
		System.out.println();
		System.out.println("R                   - Manage returns");
		System.out.println("E                   - Manage recalls");
		System.out.println("T                   - Manage rerates");
		System.out.println();
		System.out.println("X                   - Go back");
	}

}
