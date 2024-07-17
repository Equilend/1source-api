package com.os.console;

import java.io.BufferedReader;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.WebClient;

import com.os.client.model.Contract;
import com.os.console.api.ConsoleConfig;
import com.os.console.api.tasks.ProposeReturnTask;
import com.os.console.api.tasks.SearchContractReturnTask;
import com.os.console.api.tasks.SearchContractReturnsTask;
import com.os.console.util.PayloadUtil;

public class ContractReturnsConsole extends AbstractConsole {

	private static final Logger logger = LoggerFactory.getLogger(ContractReturnsConsole.class);

	Contract contract;

	protected void prompt() {
		System.out.print("/contracts/" + contract.getContractId() + "/returns > ");
	}

	public void execute(BufferedReader consoleIn, ConsoleConfig consoleConfig, WebClient webClient,
			Contract origContract) {

		contract = origContract;

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
					if (command.equals("L")) {
						System.out.print("Listing all returns...");
						SearchContractReturnsTask searchContractReturnsTask = new SearchContractReturnsTask(webClient,
								contract);
						Thread taskT = new Thread(searchContractReturnsTask);
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
							String returnId = command.substring(2).toLowerCase();
							try {
								if (UUID.fromString(returnId).toString().equals(returnId)) {
									System.out.print("Retrieving return " + returnId + "...");
									SearchContractReturnTask searchContractReturnTask = new SearchContractReturnTask(
											webClient, contract, returnId);
									Thread taskT = new Thread(searchContractReturnTask);
									taskT.run();
									try {
										taskT.join();
									} catch (InterruptedException e) {
										e.printStackTrace();
									}
									if (searchContractReturnTask.getReturn() != null) {
										ContractReturnConsole contractReturnConsole = new ContractReturnConsole();
										contractReturnConsole.execute(consoleIn, webClient, contract,
												searchContractReturnTask.getReturn());
									}
								} else {
									System.out.println("Invalid UUID");
								}
							} catch (Exception u) {
								System.out.println("Invalid UUID");
							}
						}
					} else if (command.startsWith("R ")) {
						if (command.length() > 15) {
							System.out.println("Invalid quantity");
						} else {
							Integer quantity = Integer.valueOf(command.substring(2));
							try {
								System.out.print("Notifying return...");
								ProposeReturnTask proposeReturnTask = new ProposeReturnTask(webClient,
										contract, PayloadUtil.createReturnProposal(consoleConfig, contract, quantity), ConsoleConfig.ACTING_PARTY);
								Thread taskT = new Thread(proposeReturnTask);
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
					} else {
						System.out.println("Unknown command");
					}

				}

				prompt();
			}
		} catch (Exception e) {
			logger.error("Exception with contract returns command: " + command);
			e.printStackTrace();
		}

	}

	protected void printMenu() {
		System.out.println("Contract Returns Menu");
		System.out.println("-----------------------");
		System.out.println("L                   - List all returns");
		System.out.println("S <Return ID>       - Load return by Id");
		System.out.println("R <Quantity>        - Notify return");
		System.out.println("X                   - Go back");
	}

}
