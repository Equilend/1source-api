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

public class ContractRecallConsole extends AbstractConsole {

	private static final Logger logger = LoggerFactory.getLogger(ContractRecallConsole.class);

	private Contract contract;
	private Recall recall;

	protected void prompt() {
		System.out.print("/contracts/" + contract.getContractId() + "/recalls/" + recall.getRecallId() + " > ");
	}

	public void execute(BufferedReader consoleIn, WebClient webClient, Contract origContract, Recall origRecall) {

		this.contract = origContract;
		this.recall = origRecall;

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

						System.out.println(gson.toJson(recall));
						System.out.println();

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
		System.out.println("Contract Recall Menu");
		System.out.println("-----------------------");
		System.out.println("J             - Print JSON");
		System.out.println("X             - Go back");
	}

}
