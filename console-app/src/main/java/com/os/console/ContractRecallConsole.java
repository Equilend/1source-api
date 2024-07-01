package com.os.console;

import java.io.BufferedReader;
import java.time.LocalDate;
import java.time.OffsetDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.WebClient;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.os.client.model.Contract;
import com.os.client.model.Recall;
import com.os.console.api.LocalDateTypeGsonAdapter;
import com.os.console.api.OffsetDateTimeTypeGsonAdapter;

public class ContractRecallConsole {

	private static final Logger logger = LoggerFactory.getLogger(ContractRecallConsole.class);

	public void execute(BufferedReader consoleIn, WebClient webClient, Contract contract, Recall recall) {

		String command = null;
		System.out.print("/contracts/" + contract.getContractId() + "/recalls/" + recall.getRecallId() + " > ");

		try {
			while ((command = consoleIn.readLine()) != null) {
				command = command.trim();
				if (command.equals("?") || command.equalsIgnoreCase("help")) {
					printMainContractHelp();
				} else if (command.equalsIgnoreCase("x")) {
					break;
				} else if (command.equalsIgnoreCase("j")) {

					Gson gson = new GsonBuilder()
							.setPrettyPrinting()
						    .registerTypeAdapter(LocalDate.class, new LocalDateTypeGsonAdapter())
						    .registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeTypeGsonAdapter())
						    .create();
					
					System.out.println(gson.toJson(recall));
					System.out.println();

				} else {
					System.out.println("Unknown command");
				}

				System.out.print("/contracts/" + contract.getContractId() + "/recalls/" + recall.getRecallId() + " > ");
			}
		} catch (Exception e) {
			logger.error("Exception with recalls command: " + command);
			e.printStackTrace();
		}

	}

	private void printMainContractHelp() {
		System.out.println();
		System.out.println("Recall Menu");
		System.out.println("-----------------------");
		System.out.println("J             - Print JSON");
		System.out.println("X             - Go back");
		System.out.println();
	}

}
