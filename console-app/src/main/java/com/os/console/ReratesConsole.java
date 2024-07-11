package com.os.console;

import java.io.BufferedReader;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.WebClient;

import com.os.console.api.tasks.SearchRerateTask;
import com.os.console.api.tasks.SearchReratesTask;

public class ReratesConsole extends AbstractConsole {

	private static final Logger logger = LoggerFactory.getLogger(ReratesConsole.class);

	protected void prompt() {
		System.out.print("/rerates > ");
	}

	public void execute(BufferedReader consoleIn, WebClient webClient) {

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
						System.out.print("Retrieving all rerates...");
						SearchReratesTask searchReratesTask = new SearchReratesTask(webClient);
						Thread taskT = new Thread(searchReratesTask);
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
							String rerateId = command.substring(2);
							try {
								if (UUID.fromString(rerateId).toString().equals(rerateId)) {
									System.out.print("Retrieving rerate " + rerateId + "...");
									SearchRerateTask searchRerateTask = new SearchRerateTask(webClient, rerateId);
									Thread taskT = new Thread(searchRerateTask);
									taskT.run();
									try {
										taskT.join();
									} catch (InterruptedException e) {
										e.printStackTrace();
									}
									if (searchRerateTask.getRerate() != null) {
										RerateConsole rerateConsole = new RerateConsole();
										rerateConsole.execute(consoleIn, webClient, searchRerateTask.getRerate());
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

				prompt();
			}
		} catch (Exception e) {
			logger.error("Exception with rerates command: " + command);
			e.printStackTrace();
		}

	}

	protected void printMenu() {
		System.out.println("Rerates Menu");
		System.out.println("-----------------------");
		System.out.println("A             - List all rerates");
		System.out.println("S <Rerate Id> - Load a rerate by Id");
		System.out.println("X             - Go back");
	}

}
