package com.os.console;

import java.io.BufferedReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.WebClient;

import com.os.client.model.Loan;
import com.os.client.model.Rerate;
import com.os.console.api.tasks.ApproveRerateTask;
import com.os.console.api.tasks.CancelRerateTask;
import com.os.console.api.tasks.DeclineRerateTask;
import com.os.console.api.tasks.SearchLoanRerateTask;
import com.os.console.util.ConsoleOutputUtil;

public class LoanRerateConsole extends AbstractConsole {

	private static final Logger logger = LoggerFactory.getLogger(LoanRerateConsole.class);

	Loan loan;
	Rerate rerate;

	public LoanRerateConsole(Loan loan, Rerate rerate) {
		this.loan = loan;
		this.rerate = rerate;
	}

	protected boolean prompt() {
		if (loan == null) {
			System.out.println("Loan not available");
			return false;
		} else if (rerate == null) {
			System.out.println("Rerate not available");
			return false;
		}

		System.out.print("/loans/" + loan.getLoanId() + "/rerates/" + rerate.getRerateId() + " > ");

		return true;
	}

	public void handleArgs(String args[], BufferedReader consoleIn, WebClient webClient) {

		if (args[0].equals("J")) {

			ConsoleOutputUtil.printObject(rerate);

		} else if (args[0].equals("A")) {
			System.out.print("Approving rerate...");
			ApproveRerateTask approveRerateTask = new ApproveRerateTask(webClient, loan.getLoanId(),
					rerate.getRerateId());
			Thread taskS = new Thread(approveRerateTask);
			taskS.run();
			try {
				taskS.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			refreshRerate(webClient, loan);
		} else if (args[0].equals("C")) {
			System.out.print("Canceling rerate...");
			CancelRerateTask cancelRerateTask = new CancelRerateTask(webClient, loan.getLoanId(),
					rerate.getRerateId());
			Thread taskS = new Thread(cancelRerateTask);
			taskS.run();
			try {
				taskS.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			refreshRerate(webClient, loan);
		} else if (args[0].equals("D")) {
			System.out.print("Declining rerate...");
			DeclineRerateTask declineRerateTask = new DeclineRerateTask(webClient, loan.getLoanId(),
					rerate.getRerateId());
			Thread taskS = new Thread(declineRerateTask);
			taskS.run();
			try {
				taskS.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			refreshRerate(webClient, loan);
		} else {
			System.out.println("Unknown command");
		}
	}

	private void refreshRerate(WebClient webClient, Loan loan) {

		System.out.print("Refreshing rerate " + rerate.getRerateId() + "...");
		SearchLoanRerateTask searchLoanRerateTask = new SearchLoanRerateTask(webClient,
				loan.getLoanId(), rerate.getRerateId());
		Thread taskT = new Thread(searchLoanRerateTask);
		taskT.run();
		try {
			taskT.join();
		} catch (InterruptedException e) {
			logger.error(e.getMessage(), e);
		}
		rerate = searchLoanRerateTask.getRerate();
	}

	protected void printMenu() {
		System.out.println("Loan Rerate Menu");
		System.out.println("-----------------------");
		System.out.println("J             - Print JSON");
		System.out.println();
		System.out.println("A             - Approve");
		System.out.println("C             - Cancel");
		System.out.println("D             - Decline");
		System.out.println();
		System.out.println("X             - Go back");
	}

}
