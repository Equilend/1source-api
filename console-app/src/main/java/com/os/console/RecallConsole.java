package com.os.console;

import java.io.BufferedReader;
import org.springframework.web.reactive.function.client.WebClient;

import com.os.client.model.Recall;
import com.os.console.util.ConsoleOutputUtil;

public class RecallConsole extends AbstractConsole {

	Recall recall;

	public RecallConsole(Recall recall) {
		this.recall = recall;
	}

	protected void prompt() {
		System.out.print("/recalls/" + recall.getRecallId() + " > ");
	}

	public void handleArgs(String args[], BufferedReader consoleIn, WebClient webClient) {

		if (args[0].equals("J")) {

			ConsoleOutputUtil.printObject(recall);

		} else {
			System.out.println("Unknown command");
		}
	}

	protected void printMenu() {
		System.out.println("Recall Menu");
		System.out.println("-----------------------");
		System.out.println("J             - Print JSON");
		System.out.println("X             - Go back");
	}

}
