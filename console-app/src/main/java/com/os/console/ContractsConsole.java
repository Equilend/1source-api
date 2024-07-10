package com.os.console;

import java.io.BufferedReader;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.WebClient;

import com.os.client.model.PartyRole;
import com.os.console.api.ConsoleConfig;
import com.os.console.api.tasks.SearchContractTask;
import com.os.console.api.tasks.SearchContractsTask;
import com.os.console.api.tasks.SearchPartyTask;

public class ContractsConsole extends AbstractConsole {

	private static final Logger logger = LoggerFactory.getLogger(ContractsConsole.class);

	protected void prompt() {
		System.out.print("/contracts > ");
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
						System.out.print("Retrieving all contracts...");
						SearchContractsTask searchContractsTask = new SearchContractsTask(webClient);
						Thread taskT = new Thread(searchContractsTask);
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
							String contractId = command.substring(2);
							try {
								if (UUID.fromString(contractId).toString().equalsIgnoreCase(contractId)) {
									System.out.print("Retrieving contract " + contractId + "...");
									SearchContractTask searchContractTask = new SearchContractTask(webClient,
											contractId);
									Thread taskT = new Thread(searchContractTask);
									taskT.run();
									try {
										taskT.join();
									} catch (InterruptedException e) {
										e.printStackTrace();
									}
									if (searchContractTask.getContract() != null) {
										ContractConsole contractConsole = new ContractConsole();
										contractConsole.execute(consoleIn, consoleConfig, webClient,
												searchContractTask.getContract());
									}
								} else {
									System.out.println("Invalid UUID");
								}
							} catch (Exception u) {
								System.out.println("Invalid UUID");
							}
						}
					} else if (command.startsWith("P ")) {
						if (command.length() > 100) {
							System.out.println("Invalid Party Id");
						} else if (consoleConfig.getAuth_party().equals(command)) {
							System.out.println("You cannot propose a contract to yourself");
						} else {

							String partyId = command.substring(2);
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
									ContractProposalConsole contractProposalConsole = new ContractProposalConsole();
									contractProposalConsole.execute(consoleIn, consoleConfig, webClient,
											(PartyRole.BORROWER.equals(ConsoleConfig.ACTING_AS)
													? ConsoleConfig.ACTING_PARTY
													: searchPartyTask.getParty()),
											(PartyRole.LENDER.equals(ConsoleConfig.ACTING_AS)
													? ConsoleConfig.ACTING_PARTY
													: searchPartyTask.getParty()),
											ConsoleConfig.ACTING_AS);
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
			logger.error("Exception with contracts command: " + command);
			e.printStackTrace();
		}

	}

	protected void printMenu() {
		System.out.println("Contracts Menu");
		System.out.println("-----------------------");
		System.out.println("A               - List all contracts");
		System.out.println("S <Contract Id> - Load a contract by Id");
		System.out.println("P <Party ID>    - Propose a contract to Party Id");
		System.out.println("X               - Go back");
	}

}
