package com.os.console;

import java.io.BufferedReader;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.WebClient;

import com.os.console.api.ConsoleConfig;
import com.os.console.api.tasks.ApproveDelegationTask;
import com.os.console.api.tasks.CancelDelegationTask;
import com.os.console.api.tasks.DeclineDelegationTask;
import com.os.console.api.tasks.SearchDelegationTask;
import com.os.console.api.tasks.SearchDelegationsTask;
import com.os.console.api.tasks.SearchPartyTask;

public class DelegationsConsole extends AbstractConsole {

	private static final Logger logger = LoggerFactory.getLogger(DelegationsConsole.class);

	protected boolean prompt() {
		System.out.print("/delegations > ");
		return true;
	}

	public void handleArgs(String args[], BufferedReader consoleIn, WebClient webClient) {

		if (args[0].equals("L")) {
			System.out.print("Listing all delegations...");
			SearchDelegationsTask searchDelegationsTask = new SearchDelegationsTask(webClient);
			Thread taskT = new Thread(searchDelegationsTask);
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
				String delegationId = args[1];
				try {
					if (UUID.fromString(delegationId).toString().equals(delegationId)) {
						System.out.print("Retrieving delegation " + delegationId + "...");
						SearchDelegationTask searchDelegationTask = new SearchDelegationTask(webClient, delegationId);
						Thread taskT = new Thread(searchDelegationTask);
						taskT.run();
						try {
							taskT.join();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						if (searchDelegationTask.getDelegation() != null) {
							DelegationConsole delegationConsole = new DelegationConsole(searchDelegationTask.getDelegation());
							delegationConsole.execute(consoleIn, webClient);
						}
					} else {
						System.out.println("Invalid UUID");
					}
				} catch (Exception u) {
					System.out.println(u.getMessage());
					logger.error(u.getMessage(), u);
				}
			}
		} else if (args[0].equals("A")) {
			if (args.length != 2 || args[1].length() != 36) {
				System.out.println("Invalid UUID");
			} else {
				String delegationId = args[1];
				try {
					if (UUID.fromString(delegationId).toString().equalsIgnoreCase(delegationId)) {
						System.out.print("Approving delegation " + delegationId + "...");
						ApproveDelegationTask approveDelegationTask = new ApproveDelegationTask(webClient,
								delegationId);
						Thread taskT = new Thread(approveDelegationTask);
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
		} else if (args[0].equals("C")) {
			if (args.length != 2 || args[1].length() != 36) {
				System.out.println("Invalid UUID");
			} else {
				String delegationId = args[1];
				try {
					if (UUID.fromString(delegationId).toString().equalsIgnoreCase(delegationId)) {
						System.out.print("Canceling delegation " + delegationId + "...");
						CancelDelegationTask cancelDelegationTask = new CancelDelegationTask(webClient, delegationId);
						Thread taskT = new Thread(cancelDelegationTask);
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
				String delegationId = args[1];
				try {
					if (UUID.fromString(delegationId).toString().equalsIgnoreCase(delegationId)) {
						System.out.print("Declining delegation " + delegationId + "...");
						DeclineDelegationTask declineDelegationTask = new DeclineDelegationTask(webClient,
								delegationId);
						Thread taskT = new Thread(declineDelegationTask);
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
		} else if (args[0].equals("P")) {
			if (args.length != 2 || args[1].length() > 100) {
				System.out.println("Invalid Party Id");
			} else if (ConsoleConfig.ACTING_PARTY.getPartyId().equalsIgnoreCase(args[1])) {
				System.out.println("You cannot propose a delegation to yourself");
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
						DelegationProposalConsole delegationProposalConsole = new DelegationProposalConsole(searchPartyTask.getParty());
						delegationProposalConsole.execute(consoleIn, webClient);
					}
				} catch (Exception u) {
					logger.error("Problem verifying party", u);
					System.out.println("Invalid party id");
				}
			}
		} else {
			System.out.println("Unknown command");
		}
	}

	protected void printMenu() {
		System.out.println("Delegations Menu");
		System.out.println("-----------------------");
		System.out.println("L                 - List all delegations");
		System.out.println("S <Delegation Id> - Load a delegation by Id");
		System.out.println();
		System.out.println("A <Delegation Id> - Approve delegation by Id");
		System.out.println("C <Delegation Id> - Cancel delegation by Id");
		System.out.println("D <Delegation Id> - Decline delegation by Id");
		System.out.println();
		System.out.println("P <Party Id>      - Propose a delegation to Party Id");
		System.out.println();
		System.out.println("X                 - Go back");
	}

}
