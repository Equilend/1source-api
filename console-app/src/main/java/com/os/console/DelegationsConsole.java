package com.os.console;

import java.io.BufferedReader;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.WebClient;

import com.os.console.api.tasks.SearchDelegationTask;
import com.os.console.api.tasks.SearchDelegationsTask;

public class DelegationsConsole {

	private static final Logger logger = LoggerFactory.getLogger(DelegationsConsole.class);

	public void execute(BufferedReader consoleIn, WebClient webClient) {

		String command = null;
		System.out.print("/delegations > ");

		try {
			while ((command = consoleIn.readLine()) != null) {
				command = command.trim();
				if (command.equals("?") || command.equalsIgnoreCase("help")) {
					printMainContractsHelp();
				} else if (command.equalsIgnoreCase("quit") || command.equalsIgnoreCase("exit") || command.equalsIgnoreCase("q")) {
					System.exit(0);
				} else if (command.equalsIgnoreCase("x")) {
					break;
				} else if (command.equalsIgnoreCase("a")) {
					System.out.print("Retrieving all delegations...");
					SearchDelegationsTask searchDelegationsTask = new SearchDelegationsTask(webClient);
					Thread taskT = new Thread(searchDelegationsTask);
					taskT.run();
					try {
						taskT.join();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} else if (command.startsWith("s ") || command.startsWith("S ")) {
					if (command.length() != 38) {
						System.out.println("Invalid UUID");
					} else {
						String delegationId = command.substring(2);
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
									DelegationConsole delegationConsole = new DelegationConsole();
									delegationConsole.execute(consoleIn, webClient, searchDelegationTask.getDelegation());
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

				System.out.print("/delegations > ");
			}
		} catch (Exception e) {
			logger.error("Exception with delegations command: " + command);
			e.printStackTrace();
		}

	}

	private void printMainContractsHelp() {
		System.out.println();
		System.out.println("Delegations Menu");
		System.out.println("-----------------------");
		System.out.println("A             - List all delegations");
		System.out.println("S <Delegation Id> - Load a delegation by Id");
		System.out.println("X             - Go back");
		System.out.println();
	}

}
