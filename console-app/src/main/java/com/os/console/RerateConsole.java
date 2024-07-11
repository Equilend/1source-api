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

public class RerateConsole extends AbstractConsole {

	private static final Logger logger = LoggerFactory.getLogger(RerateConsole.class);

	Rerate rerate;
	
	protected void prompt() {
		System.out.print("/rerates/" + rerate.getRerateId() + " > ");
	}

	public void execute(BufferedReader consoleIn, WebClient webClient, Rerate origRerate) {

		this.rerate = origRerate;
		
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
					if (command.equals("J")) {

						Gson gson = new GsonBuilder().setPrettyPrinting()
								.registerTypeAdapter(LocalDate.class, new LocalDateTypeGsonAdapter())
								.registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeTypeGsonAdapter())
								.create();

						System.out.println(gson.toJson(rerate));
						System.out.println();

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
		System.out.println("Rerate Menu");
		System.out.println("-----------------------");
		System.out.println("J             - Print JSON");
		System.out.println("X             - Go back");
	}

}
