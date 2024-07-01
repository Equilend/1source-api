package com.os.console;

import java.io.BufferedReader;
import java.time.LocalDate;
import java.time.OffsetDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.WebClient;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.os.client.model.Rerate;
import com.os.console.api.LocalDateTypeGsonAdapter;
import com.os.console.api.OffsetDateTimeTypeGsonAdapter;

public class RerateConsole {

	private static final Logger logger = LoggerFactory.getLogger(RerateConsole.class);

	public void execute(BufferedReader consoleIn, WebClient webClient, Rerate rerate) {

		String command = null;
		System.out.print("/rerates/" + rerate.getRerateId() + " > ");

		try {
			while ((command = consoleIn.readLine()) != null) {
				command = command.trim();
				if (command.equals("?") || command.equalsIgnoreCase("help")) {
					printMainContractHelp();
				} else if (command.equalsIgnoreCase("quit") || command.equalsIgnoreCase("exit") || command.equalsIgnoreCase("q")) {
					System.exit(0);
				} else if (command.equalsIgnoreCase("x")) {
					break;
				} else if (command.equalsIgnoreCase("j")) {

					Gson gson = new GsonBuilder()
							.setPrettyPrinting()
						    .registerTypeAdapter(LocalDate.class, new LocalDateTypeGsonAdapter())
						    .registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeTypeGsonAdapter())
						    .create();
					
					System.out.println(gson.toJson(rerate));
					System.out.println();

				} else {
					System.out.println("Unknown command");
				}

				System.out.print("/rerates/" + rerate.getRerateId() + " > ");
			}
		} catch (Exception e) {
			logger.error("Exception with rerates command: " + command);
			e.printStackTrace();
		}

	}

	private void printMainContractHelp() {
		System.out.println();
		System.out.println("Rerate Menu");
		System.out.println("-----------------------");
		System.out.println("J             - Print JSON");
		System.out.println("X             - Go back");
		System.out.println();
	}

}
