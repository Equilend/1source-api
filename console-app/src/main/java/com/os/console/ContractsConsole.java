package com.os.console;

import java.io.BufferedReader;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.WebClient;

import com.os.console.api.tasks.SearchContractTask;
import com.os.console.api.tasks.SearchContractsTask;

public class ContractsConsole {

	private static final Logger logger = LoggerFactory.getLogger(ContractsConsole.class);

	public void execute(BufferedReader consoleIn, WebClient webClient) {

		String command = null;
		System.out.print("/contracts > ");

		try {
			while ((command = consoleIn.readLine()) != null) {
				command = command.trim();
				if (command.equals("?") || command.equalsIgnoreCase("help")) {
					printMainContractsHelp();
				} else if (command.equalsIgnoreCase("x")) {
					break;
				} else if (command.equalsIgnoreCase("a")) {
					System.out.print("Retrieving all contracts...");
					SearchContractsTask searchContractsTask = new SearchContractsTask(webClient);
					Thread taskT = new Thread(searchContractsTask);
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
						String contractId = command.substring(2);
						try {
							if (UUID.fromString(contractId).toString().equals(contractId)) {
								System.out.print("Retrieving contract " + contractId + "...");
								SearchContractTask searchContractTask = new SearchContractTask(webClient, contractId);
								Thread taskT = new Thread(searchContractTask);
								taskT.run();
								try {
									taskT.join();
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
								if (searchContractTask.getContract() != null) {
									ContractConsole contractConsole = new ContractConsole();
									contractConsole.execute(consoleIn, webClient, searchContractTask.getContract());
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

				System.out.print("/contracts > ");
			}
		} catch (Exception e) {
			logger.error("Exception with contracts command: " + command);
			e.printStackTrace();
		}

	}

	private void printMainContractsHelp() {
		System.out.println();
		System.out.println("Contracts Menu");
		System.out.println("-----------------------");
		System.out.println("A               - List all contracts");
		System.out.println("S <Contract Id> - Load a contract by Id");
		System.out.println("X               - Go back");
		System.out.println();
	}

}
