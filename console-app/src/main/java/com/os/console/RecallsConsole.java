package com.os.console;

import java.io.BufferedReader;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.WebClient;

import com.os.console.api.tasks.SearchRecallTask;
import com.os.console.api.tasks.SearchRecallsTask;

public class RecallsConsole extends AbstractConsole {

	private static final Logger logger = LoggerFactory.getLogger(RecallsConsole.class);

	protected void prompt() {
		System.out.print("/recalls > ");
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
						System.out.print("Retrieving all recalls...");
						SearchRecallsTask searchRecallsTask = new SearchRecallsTask(webClient);
						Thread taskT = new Thread(searchRecallsTask);
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
							String recallId = command.substring(2);
							try {
								if (UUID.fromString(recallId).toString().equals(recallId)) {
									System.out.print("Retrieving recall " + recallId + "...");
									SearchRecallTask searchRecallTask = new SearchRecallTask(webClient, recallId);
									Thread taskT = new Thread(searchRecallTask);
									taskT.run();
									try {
										taskT.join();
									} catch (InterruptedException e) {
										e.printStackTrace();
									}
									if (searchRecallTask.getRecall() != null) {
										RecallConsole recallConsole = new RecallConsole();
										recallConsole.execute(consoleIn, webClient, searchRecallTask.getRecall());
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
			logger.error("Exception with recalls command: " + command);
			e.printStackTrace();
		}

	}

	protected void printMenu() {
		System.out.println("Recalls Menu");
		System.out.println("-----------------------");
		System.out.println("A             - List all recalls");
		System.out.println("S <Recall Id> - Load a recall by Id");
		System.out.println("X             - Go back");
	}

}
