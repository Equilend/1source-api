package com.os.console;

import java.io.BufferedReader;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.WebClient;

import com.os.console.api.tasks.SearchReturnTask;
import com.os.console.api.tasks.SearchReturnsTask;

public class ReturnsConsole extends AbstractConsole {

	private static final Logger logger = LoggerFactory.getLogger(ReturnsConsole.class);

	protected void prompt() {
		System.out.print("/returns > ");
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
						System.out.print("Retrieving all returns...");
						SearchReturnsTask searchReturnsTask = new SearchReturnsTask(webClient);
						Thread taskT = new Thread(searchReturnsTask);
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
							String returnId = command.substring(2);
							try {
								if (UUID.fromString(returnId).toString().equals(returnId)) {
									System.out.print("Retrieving return " + returnId + "...");
									SearchReturnTask searchReturnTask = new SearchReturnTask(webClient, returnId);
									Thread taskT = new Thread(searchReturnTask);
									taskT.run();
									try {
										taskT.join();
									} catch (InterruptedException e) {
										e.printStackTrace();
									}
									if (searchReturnTask.getReturn() != null) {
										ReturnConsole returnConsole = new ReturnConsole();
										returnConsole.execute(consoleIn, webClient, searchReturnTask.getReturn());
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
			logger.error("Exception with returns command: " + command);
			e.printStackTrace();
		}

	}

	protected void printMenu() {
		System.out.println("Returns Menu");
		System.out.println("-----------------------");
		System.out.println("A             - List all returns");
		System.out.println("S <Return Id> - Load a return by Id");
		System.out.println("X             - Go back");
	}

}
