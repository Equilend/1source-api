package com.os.console;

import java.io.BufferedReader;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.WebClient;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.os.client.model.Contract;
import com.os.console.api.LocalDateTypeGsonAdapter;
import com.os.console.api.OffsetDateTimeTypeGsonAdapter;
import com.os.console.api.tasks.SearchContractRecallTask;
import com.os.console.api.tasks.SearchContractRecallsTask;
import com.os.console.api.tasks.SearchContractRerateTask;
import com.os.console.api.tasks.SearchContractReratesTask;
import com.os.console.api.tasks.SearchContractReturnTask;
import com.os.console.api.tasks.SearchContractReturnsTask;
import com.os.console.api.tasks.UpdateContractVenueKeyTask;

public class ContractConsole {

	private static final Logger logger = LoggerFactory.getLogger(ContractConsole.class);

	public void execute(BufferedReader consoleIn, WebClient webClient, Contract contract) {

		String command = null;
		System.out.print("/contracts/" + contract.getContractId() + " > ");

		try {
			while ((command = consoleIn.readLine()) != null) {
				command = command.trim();
				if (command.equals("?") || command.equalsIgnoreCase("help")) {
					printMainContractHelp();
				} else if (command.equalsIgnoreCase("quit") || command.equalsIgnoreCase("exit")
						|| command.equalsIgnoreCase("q")) {
					System.exit(0);
				} else if (command.equalsIgnoreCase("x")) {
					break;
				} else if (command.equalsIgnoreCase("j")) {

					Gson gson = new GsonBuilder().setPrettyPrinting()
							.registerTypeAdapter(LocalDate.class, new LocalDateTypeGsonAdapter())
							.registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeTypeGsonAdapter()).create();

					System.out.println(gson.toJson(contract));
					System.out.println();

				} else if (command.equalsIgnoreCase("u")) {
					System.out.print("Retrieving all returns...");
					SearchContractReturnsTask searchContractReturnsTask = new SearchContractReturnsTask(webClient,
							contract);
					Thread taskT = new Thread(searchContractReturnsTask);
					taskT.run();
					try {
						taskT.join();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} else if (command.startsWith("u ") || command.startsWith("U ")) {
					if (command.length() != 38) {
						System.out.println("Invalid UUID");
					} else {
						String returnId = command.substring(2);
						try {
							if (UUID.fromString(returnId).toString().equals(returnId)) {
								System.out.print("Retrieving return " + returnId + "...");
								SearchContractReturnTask searchContractReturnTask = new SearchContractReturnTask(
										webClient, contract.getContractId(), returnId);
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
				} else if (command.equalsIgnoreCase("e")) {
					System.out.print("Retrieving all recalls...");
					SearchContractRecallsTask searchContractRecallsTask = new SearchContractRecallsTask(webClient,
							contract);
					Thread taskT = new Thread(searchContractRecallsTask);
					taskT.run();
					try {
						taskT.join();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} else if (command.startsWith("e ") || command.startsWith("E ")) {
					if (command.length() != 38) {
						System.out.println("Invalid UUID");
					} else {
						String recallId = command.substring(2);
						try {
							if (UUID.fromString(recallId).toString().equals(recallId)) {
								System.out.print("Retrieving recall " + recallId + "...");
								SearchContractRecallTask searchContractRecallTask = new SearchContractRecallTask(
										webClient, contract.getContractId(), recallId);
								Thread taskT = new Thread(searchContractRecallTask);
								taskT.run();
								try {
									taskT.join();
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
								if (searchContractRecallTask.getRecall() != null) {
									ContractRecallConsole contractRecallConsole = new ContractRecallConsole();
									contractRecallConsole.execute(consoleIn, webClient, contract,
											searchContractRecallTask.getRecall());
								}
							} else {
								System.out.println("Invalid UUID");
							}
						} catch (Exception u) {
							System.out.println("Invalid UUID");
						}
					}
				} else if (command.equalsIgnoreCase("a")) {
					System.out.print("Retrieving all rerates...");
					SearchContractReratesTask searchContractReratesTask = new SearchContractReratesTask(webClient,
							contract);
					Thread taskT = new Thread(searchContractReratesTask);
					taskT.run();
					try {
						taskT.join();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} else if (command.startsWith("a ") || command.startsWith("A ")) {
					if (command.length() != 38) {
						System.out.println("Invalid UUID");
					} else {
						String rerateId = command.substring(2);
						try {
							if (UUID.fromString(rerateId).toString().equals(rerateId)) {
								System.out.print("Retrieving rerate " + rerateId + "...");
								SearchContractRerateTask searchContractRerateTask = new SearchContractRerateTask(
										webClient, contract.getContractId(), rerateId);
								Thread taskT = new Thread(searchContractRerateTask);
								taskT.run();
								try {
									taskT.join();
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
								if (searchContractRerateTask.getRerate() != null) {
									ContractRerateConsole contractRerateConsole = new ContractRerateConsole();
									contractRerateConsole.execute(consoleIn, webClient, contract,
											searchContractRerateTask.getRerate());
								}
							} else {
								System.out.println("Invalid UUID");
							}
						} catch (Exception u) {
							System.out.println("Invalid UUID");
						}
					}
				} else if (command.startsWith("v ") || command.startsWith("V ")) {
					if (command.length() > 100) {
						System.out.println("Invalid reference key");
					} else {
						String venueRefKey = command.substring(2);
						try {
							System.out.print("Assigning venue reference " + venueRefKey + "...");
							UpdateContractVenueKeyTask updateContractVenueKeyTask = new UpdateContractVenueKeyTask(webClient,
									contract.getContractId(), venueRefKey);
							Thread taskT = new Thread(updateContractVenueKeyTask);
							taskT.run();
							try {
								taskT.join();
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						} catch (Exception u) {
							System.out.println("Invalid reference key");
						}
					}
				} else {
					System.out.println("Unknown command");
				}

				System.out.print("/contracts/" + contract.getContractId() + " > ");
			}
		} catch (Exception e) {
			logger.error("Exception with contracts command: " + command);
			e.printStackTrace();
		}

	}

	private void printMainContractHelp() {
		System.out.println();
		System.out.println("Contract Menu");
		System.out.println("-----------------------");
		System.out.println("J             - Print JSON");
		System.out.println("U             - List Returns");
		System.out.println("U <Return ID> - Load return by Id");
		System.out.println("E             - List Recalls");
		System.out.println("E <Recall ID> - Load recall by Id");
		System.out.println("A             - List Rerates");
		System.out.println("A <Recall ID> - Load rerate by Id");
		System.out.println("V <Venue Ref> - Add a venue reference key");
		System.out.println("X             - Go back");
		System.out.println();
	}

}
