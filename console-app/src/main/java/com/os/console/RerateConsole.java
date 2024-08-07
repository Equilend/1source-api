package com.os.console;

import java.io.BufferedReader;
import org.springframework.web.reactive.function.client.WebClient;

import com.os.client.model.Rerate;
import com.os.console.util.ConsoleOutputUtil;

public class RerateConsole extends AbstractConsole {

	Rerate rerate;

	public RerateConsole(Rerate rerate) {
		this.rerate = rerate;
	}

	protected void prompt() {
		System.out.print("/rerates/" + rerate.getRerateId() + " > ");
	}

	public void handleArgs(String args[], BufferedReader consoleIn, WebClient webClient) {

		if (args[0].equals("J")) {
			ConsoleOutputUtil.printObject(rerate);
		} else {
			System.out.println("Unknown command");
		}
	}

	protected void printMenu() {
		System.out.println("Rerate Menu");
		System.out.println("-----------------------");
		System.out.println("J             - Print JSON");
		System.out.println("X             - Go back");
	}

}
