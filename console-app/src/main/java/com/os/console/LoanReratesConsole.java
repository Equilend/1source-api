package com.os.console;

import java.io.BufferedReader;
import java.util.UUID;

import org.springframework.web.reactive.function.client.WebClient;

import com.os.client.model.Loan;
import com.os.client.model.RerateProposal;
import com.os.console.api.ConsoleConfig;
import com.os.console.api.tasks.ApproveRerateTask;
import com.os.console.api.tasks.CancelRerateTask;
import com.os.console.api.tasks.DeclineRerateTask;
import com.os.console.api.tasks.ProposeRerateTask;
import com.os.console.api.tasks.SearchLoanRerateTask;
import com.os.console.api.tasks.SearchLoanReratesTask;
import com.os.console.util.ConsoleOutputUtil;
import com.os.console.util.PayloadUtil;

public class LoanReratesConsole extends AbstractConsole {

	Loan loan;

	public LoanReratesConsole(Loan loan) {
		this.loan = loan;
	}

	protected boolean prompt() {

		if (loan == null) {
			System.out.println("Loan not available");
			return false;
		}

		System.out.print("/loans/" + loan.getLoanId() + "/rerates > ");
		return true;
	}

	public void handleArgs(String args[], BufferedReader consoleIn, WebClient webClient) {

		if (args[0].equals("I")) {
			System.out.print("Listing all rerates...");
			SearchLoanReratesTask searchLoanReratesTask = new SearchLoanReratesTask(webClient, loan);
			Thread taskT = new Thread(searchLoanReratesTask);
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
				String rerateId = args[1];
				try {
					if (UUID.fromString(rerateId).toString().equals(rerateId)) {
						System.out.print("Retrieving rerate " + rerateId + "...");
						SearchLoanRerateTask searchLoanRerateTask = new SearchLoanRerateTask(webClient,
								loan.getLoanId(), rerateId);
						Thread taskT = new Thread(searchLoanRerateTask);
						taskT.run();
						try {
							taskT.join();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						if (searchLoanRerateTask.getRerate() != null) {
							LoanRerateConsole loanRerateConsole = new LoanRerateConsole(loan,
									searchLoanRerateTask.getRerate());
							loanRerateConsole.execute(consoleIn, webClient);
						}
					} else {
						System.out.println("Invalid UUID");
					}
				} catch (Exception u) {
					System.out.println("Invalid UUID");
				}
			}
		} else if (args[0].equals("J")) {
			ConsoleOutputUtil.printObject(loan.getTrade().getRate());
		} else if (args[0].equals("P")) {
			if (args.length != 2 || args[1].length() == 0 || args[1].length() > 15) {
				System.out.println("Invalid spread/fee");
			} else {
				Double rerate = Double.valueOf(args[1]);
				try {
					System.out.print("Proposing rerate...");

					RerateProposal rerateProposal = PayloadUtil.createRerateProposal(loan, rerate);

					ConsoleOutputUtil.printObject(rerateProposal);

					ProposeRerateTask proposeRerateTask = new ProposeRerateTask(webClient, loan, rerateProposal,
							ConsoleConfig.ACTING_PARTY);
					Thread taskT = new Thread(proposeRerateTask);
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
		} else if (args[0].equals("A")) {
			if (args.length != 2 || args[1].length() != 36) {
				System.out.println("Invalid UUID");
			} else {
				String rerateId = args[1];
				try {
					if (UUID.fromString(rerateId).toString().equals(rerateId)) {
						System.out.print("Approving rerate...");
						ApproveRerateTask approveRerateTask = new ApproveRerateTask(webClient, loan.getLoanId(),
								rerateId);
						Thread taskS = new Thread(approveRerateTask);
						taskS.run();
						try {
							taskS.join();
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
		} else if (args[0].equals("C")) {
			if (args.length != 2 || args[1].length() == 0 || args[1].length() != 36) {
				System.out.println("Invalid UUID");
			} else {
				String rerateId = args[1];
				try {
					if (UUID.fromString(rerateId).toString().equals(rerateId)) {
						System.out.print("Cancelling rerate " + rerateId + "...");
						CancelRerateTask cancelRerateTask = new CancelRerateTask(webClient, loan.getLoanId(),
								rerateId);
						Thread taskT = new Thread(cancelRerateTask);
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
		} else if (args[0].equals("D")) {
			if (args.length != 2 || args[1].length() != 36) {
				System.out.println("Invalid UUID");
			} else {
				String rerateId = args[1];
				try {
					if (UUID.fromString(rerateId).toString().equals(rerateId)) {
						System.out.print("Declining rerate...");
						DeclineRerateTask declineRerateTask = new DeclineRerateTask(webClient, loan.getLoanId(),
								rerateId);
						Thread taskS = new Thread(declineRerateTask);
						taskS.run();
						try {
							taskS.join();
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
		System.out.println("Loan Rerates Menu");
		System.out.println("-----------------------");
		System.out.println("I                   - List all rerates");
		System.out.println("S <Rerate ID>       - Search rerate by Id");
		System.out.println("J                   - Show current rate");
		System.out.println();
		System.out.println("P <Spread/Fee>      - Propose rerate");
		System.out.println();
		System.out.println("A <Rerate ID>       - Approve rerate by Id");
		System.out.println("C <Rerate ID>       - Cancel rerate by Id");
		System.out.println("D <Rerate ID>       - Decline rerate by Id");
		System.out.println();
		System.out.println("X                   - Go back");
	}

}
