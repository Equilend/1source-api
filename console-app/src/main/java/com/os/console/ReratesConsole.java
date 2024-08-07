package com.os.console;

import java.io.BufferedReader;
import java.util.UUID;

import org.springframework.web.reactive.function.client.WebClient;

import com.os.console.api.tasks.SearchRerateTask;
import com.os.console.api.tasks.SearchReratesTask;

public class ReratesConsole extends AbstractConsole {

	protected void prompt() {
		System.out.print("/rerates > ");
	}

	public void handleArgs(String args[], BufferedReader consoleIn, WebClient webClient) {

		if (args[0].equals("L")) {
			System.out.print("Listing all rerates...");
			SearchReratesTask searchReratesTask = new SearchReratesTask(webClient);
			Thread taskT = new Thread(searchReratesTask);
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
				String rerateId = args[1];
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
							RerateConsole rerateConsole = new RerateConsole(searchRerateTask.getRerate());
							rerateConsole.execute(consoleIn, webClient);
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

	protected void printMenu() {
		System.out.println("Rerates Menu");
		System.out.println("-----------------------");
		System.out.println("L             - List all rerates");
		System.out.println("S <Rerate Id> - Load a rerate by Id");
		System.out.println("X             - Go back");
	}

}
