package com.os.console;

import java.io.BufferedReader;
import org.springframework.web.reactive.function.client.WebClient;

import com.os.console.api.ConsoleConfig;
import com.os.console.api.tasks.SearchEventsTask;

public class EventsConsole extends AbstractConsole {

	public EventsConsole() {

	}

	protected boolean prompt() {
		System.out.print(ConsoleConfig.ACTING_PARTY.getPartyId() + " /events > ");
		return true;
	}

	public void handleArgs(String args[], BufferedReader consoleIn, WebClient webClient) {

		if (args[0].equals("I")) {
			System.out.print("Listing latest events...");
			SearchEventsTask searchEventsTask = new SearchEventsTask(webClient);
			Thread taskT = new Thread(searchEventsTask);
			taskT.run();
			try {
				taskT.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Unknown command");
		}

	}

	protected void printMenu() {
		System.out.println("Events Menu");
		System.out.println("-----------------------");
		System.out.println("I            - List latest events");
		System.out.println();
		System.out.println("X            - Go back");
	}

}
