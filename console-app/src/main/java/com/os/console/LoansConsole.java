package com.os.console;

import java.io.BufferedReader;
import java.util.UUID;

import org.springframework.web.reactive.function.client.WebClient;

import com.os.client.model.PartyRole;
import com.os.console.api.ConsoleConfig;
import com.os.console.api.tasks.ApproveLoanTask;
import com.os.console.api.tasks.CancelLoanTask;
import com.os.console.api.tasks.DeclineLoanTask;
import com.os.console.api.tasks.SearchLoanHistoryTask;
import com.os.console.api.tasks.SearchLoanRateHistoryTask;
import com.os.console.api.tasks.SearchLoanTask;
import com.os.console.api.tasks.SearchLoansTask;
import com.os.console.api.tasks.SearchPartyTask;
import com.os.console.api.tasks.UpdateLoanSettlementStatusTask;
import com.os.console.util.PayloadUtil;

public class LoansConsole extends AbstractConsole {

	public LoansConsole() {

	}

	protected boolean prompt() {
		System.out.print("/loans > ");
		return true;
	}

	public void handleArgs(String args[], BufferedReader consoleIn, WebClient webClient) {

		if (args[0].equals("I")) {
			System.out.print("Listing all loans...");
			SearchLoansTask searchLoansTask = new SearchLoansTask(webClient);
			Thread taskT = new Thread(searchLoansTask);
			taskT.run();
			try {
				taskT.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} else if (args[0].equals("S")) {
			if (args.length != 2 || args[1].length() != 36) {
				System.out.println("Invalid UUID");
			} else {
				String loanId = args[1].toLowerCase();
				try {
					if (UUID.fromString(loanId).toString().equalsIgnoreCase(loanId)) {
						System.out.print("Searching for loan " + loanId + "...");
						SearchLoanTask searchLoanTask = new SearchLoanTask(webClient, loanId);
						Thread taskT = new Thread(searchLoanTask);
						taskT.run();
						try {
							taskT.join();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						if (searchLoanTask.getLoan() != null) {
							LoanConsole loanConsole = new LoanConsole(searchLoanTask.getLoan());
							loanConsole.execute(consoleIn, webClient);
						}
					} else {
						System.out.println("Invalid UUID");
					}
				} catch (Exception u) {
					System.out.println("Invalid UUID");
				}
			}
		} else if (args[0].equals("H")) {
			if (args.length != 2 || args[1].length() != 36) {
				System.out.println("Invalid UUID");
			} else {
				String loanId = args[1].toLowerCase();
				try {
					if (UUID.fromString(loanId).toString().equalsIgnoreCase(loanId)) {
						System.out.print("Searching for loan " + loanId + "...");
						SearchLoanTask searchLoanTask = new SearchLoanTask(webClient, loanId);
						Thread taskT = new Thread(searchLoanTask);
						taskT.run();
						try {
							taskT.join();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						if (searchLoanTask.getLoan() != null) {
							System.out.print("Listing loan full history " + loanId + "...");
							SearchLoanHistoryTask searchLoanHistoryTask = new SearchLoanHistoryTask(webClient, searchLoanTask.getLoan());
							Thread taskS = new Thread(searchLoanHistoryTask);
							taskS.run();
							try {
								taskS.join();
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					} else {
						System.out.println("Invalid UUID");
					}
				} catch (Exception u) {
					System.out.println("Invalid UUID");
				}
			}
		} else if (args[0].equals("Y")) {
			if (args.length != 2 || args[1].length() != 36) {
				System.out.println("Invalid UUID");
			} else {
				String loanId = args[1].toLowerCase();
				try {
					if (UUID.fromString(loanId).toString().equalsIgnoreCase(loanId)) {
						System.out.print("Searching for loan " + loanId + "...");
						SearchLoanTask searchLoanTask = new SearchLoanTask(webClient, loanId);
						Thread taskT = new Thread(searchLoanTask);
						taskT.run();
						try {
							taskT.join();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						if (searchLoanTask.getLoan() != null) {
							System.out.print("Listing loan rate change history " + loanId + "...");
							SearchLoanRateHistoryTask searchLoanRateHistoryTask = new SearchLoanRateHistoryTask(webClient, searchLoanTask.getLoan());
							Thread taskS = new Thread(searchLoanRateHistoryTask);
							taskS.run();
							try {
								taskS.join();
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					} else {
						System.out.println("Invalid UUID");
					}
				} catch (Exception u) {
					System.out.println("Invalid UUID");
				}
			}
		} else if (args[0].equals("A")) {
			if (args.length != 2 || args[1].length() != 36) {
				System.out.println("Invalid UUID");
			} else {
				String loanId = args[1].toLowerCase();
				try {
					if (UUID.fromString(loanId).toString().equalsIgnoreCase(loanId)) {
						System.out.print("Searching for loan " + loanId + "...");
						SearchLoanTask searchLoanTask = new SearchLoanTask(webClient, loanId);
						Thread taskT = new Thread(searchLoanTask);
						taskT.run();
						try {
							taskT.join();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						if (searchLoanTask.getLoan() != null) {
							System.out.print("Approving loan " + loanId + "...");
							ApproveLoanTask approveLoanTask = new ApproveLoanTask(webClient,
									searchLoanTask.getLoan(), PayloadUtil.createLoanProposalApproval());
							Thread taskS = new Thread(approveLoanTask);
							taskS.run();
							try {
								taskS.join();
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					} else {
						System.out.println("Invalid UUID");
					}
				} catch (Exception u) {
					System.out.println("Invalid UUID");
				}
			}
		} else if (args[0].equals("C")) {
			if (args.length != 2 || args[1].length() != 36) {
				System.out.println("Invalid UUID");
			} else {
				String loanId = args[1].toLowerCase();
				try {
					if (UUID.fromString(loanId).toString().equalsIgnoreCase(loanId)) {
						System.out.print("Canceling loan " + loanId + "...");
						CancelLoanTask cancelLoanTask = new CancelLoanTask(webClient, loanId);
						Thread taskT = new Thread(cancelLoanTask);
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
				String loanId = args[1].toLowerCase();
				try {
					if (UUID.fromString(loanId).toString().equalsIgnoreCase(loanId)) {
						System.out.print("Declining loan " + loanId + "...");
						DeclineLoanTask declineLoanTask = new DeclineLoanTask(webClient, loanId);
						Thread taskT = new Thread(declineLoanTask);
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
		} else if (args[0].equals("U")) {
			if (args.length != 2 || args[1].length() != 36) {
				System.out.println("Invalid UUID");
			} else {
				String loanId = args[1].toLowerCase();
				try {
					if (UUID.fromString(loanId).toString().equalsIgnoreCase(loanId)) {

						System.out.print("Retrieving loan " + loanId + "...");

						SearchLoanTask searchLoanTask = new SearchLoanTask(webClient, loanId);
						Thread taskT = new Thread(searchLoanTask);
						taskT.run();
						try {
							taskT.join();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}

						if (searchLoanTask.getLoan() != null) {
							System.out.print("Updating loan " + loanId + " settlement status to SETTLED...");
							UpdateLoanSettlementStatusTask updateSettlementStatusTask = new UpdateLoanSettlementStatusTask(
									webClient, searchLoanTask.getLoan(), ConsoleConfig.ACTING_PARTY);
							Thread taskU = new Thread(updateSettlementStatusTask);
							taskU.run();
							try {
								taskU.join();
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					} else {
						System.out.println("Invalid UUID");
					}
				} catch (Exception u) {
					System.out.println("Invalid UUID");
				}
			}
		} else if (args[0].equals("P")) {
			if (args.length != 2 || args[1].length() > 30) {
				System.out.println("Invalid Party Id");
			} else if (ConsoleConfig.ACTING_PARTY.getPartyId().equals(args[1])) {
				System.out.println("You cannot propose a loan to yourself");
			} else {

				String partyId = args[1];
				try {
					System.out.print("Verifying party " + partyId + "...");
					SearchPartyTask searchPartyTask = new SearchPartyTask(webClient, partyId);
					Thread taskT = new Thread(searchPartyTask);
					taskT.run();
					try {
						taskT.join();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if (searchPartyTask.getParty() != null) {
						LoanProposalConsole loanProposalConsole = new LoanProposalConsole(
								(PartyRole.BORROWER.equals(ConsoleConfig.ACTING_AS) ? ConsoleConfig.ACTING_PARTY
										: searchPartyTask.getParty()),
								(PartyRole.LENDER.equals(ConsoleConfig.ACTING_AS) ? ConsoleConfig.ACTING_PARTY
										: searchPartyTask.getParty()),
								ConsoleConfig.ACTING_AS);
						loanProposalConsole.execute(consoleIn, webClient);
					}
				} catch (Exception u) {
					System.out.println("Invalid party id");
				}
			}
		} else {
			System.out.println("Unknown command");
		}

	}

	protected void printMenu() {
		System.out.println("Loans Menu");
		System.out.println("-----------------------");
		System.out.println("I            - List all loans");
		System.out.println("S <Loan Id>  - Search a loan by Id");
		System.out.println("H <Loan Id>  - Show full history for loan Id");
		System.out.println("Y <Loan Id>  - Show rate change history for loan Id");
		System.out.println();
		System.out.println("A <Loan Id>  - Approve a loan by Id");
		System.out.println("C <Loan Id>  - Cancel a loan by Id");
		System.out.println("D <Loan Id>  - Decline a loan by Id");
		System.out.println();
		System.out.println("U <Loan Id>  - Update settlement status to SETTLED");
		System.out.println();
		System.out.println("P <Party ID> - Propose a loan to Party Id");
		System.out.println();
		System.out.println("X            - Go back");
	}

}
