package com.os.console;

import java.io.BufferedReader;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.WebClient;

import com.os.console.api.ConsoleConfig;
import com.os.console.api.tasks.SearchDelegationTask;
import com.os.console.api.tasks.SearchDelegationsTask;
import com.os.console.api.tasks.SearchPartyTask;

public class DelegationsConsole extends AbstractConsole {

	private static final Logger logger = LoggerFactory.getLogger(DelegationsConsole.class);

	protected void prompt() {
		System.out.print("/delegations > ");
	}

	public void execute(BufferedReader consoleIn, ConsoleConfig consoleConfig, WebClient webClient) {

		String command = null;
		prompt();

		try {
			while ((command = consoleIn.readLine()) != null) {

				command = command.trim().toUpperCase();

				if (checkSystemCommand(command)) {
					continue;
				} else if (goBackMenu(command)) {
					break;
				} else {
					if (command.equals("A")) {
						System.out.print("Retrieving all delegations...");
						SearchDelegationsTask searchDelegationsTask = new SearchDelegationsTask(webClient);
						Thread taskT = new Thread(searchDelegationsTask);
						taskT.run();
						try {
							taskT.join();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					} else if (command.startsWith("S ")) {
						if (command.length() != 38) {
							System.out.println("Invalid UUID");
						} else {
							String delegationId = command.substring(2);
							try {
								if (UUID.fromString(delegationId).toString().equals(delegationId)) {
									System.out.print("Retrieving delegation " + delegationId + "...");
									SearchDelegationTask searchDelegationTask = new SearchDelegationTask(webClient,
											delegationId);
									Thread taskT = new Thread(searchDelegationTask);
									taskT.run();
									try {
										taskT.join();
									} catch (InterruptedException e) {
										e.printStackTrace();
									}
									if (searchDelegationTask.getDelegation() != null) {
										DelegationConsole delegationConsole = new DelegationConsole();
										delegationConsole.execute(consoleIn, webClient,
												searchDelegationTask.getDelegation());
									}
								} else {
									System.out.println("Invalid UUID");
								}
							} catch (Exception u) {
								System.out.println(u.getMessage());
								logger.error(u.getMessage(), u);
							}
						}
					} else if (command.startsWith("P ")) {
						if (command.length() > 100) {
							System.out.println("Invalid Party Id");
						} else if (consoleConfig.getAuth_party().equalsIgnoreCase(command)) {
							System.out.println("You cannot propose a delegation to yourself");
						} else {

							String partyId = command.substring(2).toUpperCase();
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
									DelegationProposalConsole delegationProposalConsole = new DelegationProposalConsole();
									delegationProposalConsole.execute(consoleIn, consoleConfig, webClient,
											searchPartyTask.getParty());
								}
							} catch (Exception u) {
								System.out.println("Invalid party id");
							}
						}
					} else {
						System.out.println("Unknown command");
					}
				}
				
				prompt();
			}
		} catch (Exception e) {
			logger.error("Exception with delegations command: " + command);
			e.printStackTrace();
		}

	}

	protected void printMenu() {
		System.out.println("Delegations Menu");
		System.out.println("-----------------------");
		System.out.println("A                 - List all delegations");
		System.out.println("S <Delegation Id> - Load a delegation by Id");
		System.out.println("P <Party Id>      - Propose a delegation to Party Id");
		System.out.println("X                 - Go back");
	}

}
