package com.os.console;

import java.io.BufferedReader;
import java.util.UUID;

import org.springframework.web.reactive.function.client.WebClient;

import com.os.console.api.tasks.SearchRecallTask;
import com.os.console.api.tasks.SearchRecallsTask;

public class RecallsConsole extends AbstractConsole {

	protected void prompt() {
		System.out.print("/recalls > ");
	}

	public void handleArgs(String args[], BufferedReader consoleIn, WebClient webClient) {

		if (args[0].equals("L")) {
			System.out.print("Listing all recalls...");
			SearchRecallsTask searchRecallsTask = new SearchRecallsTask(webClient);
			Thread taskT = new Thread(searchRecallsTask);
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
				String recallId = args[1];
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
							RecallConsole recallConsole = new RecallConsole(searchRecallTask.getRecall());
							recallConsole.execute(consoleIn, webClient);
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
		System.out.println("Recalls Menu");
		System.out.println("-----------------------");
		System.out.println("L             - List all recalls");
		System.out.println("S <Recall Id> - Load a recall by Id");
		System.out.println("X             - Go back");
	}

}
