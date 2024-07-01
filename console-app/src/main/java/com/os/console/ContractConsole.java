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
import com.os.console.api.LocalDateTypeGsonAdapter;
import com.os.console.api.OffsetDateTimeTypeGsonAdapter;
import com.os.console.api.tasks.SearchContractRecallsTask;
import com.os.console.api.tasks.SearchContractReratesTask;
import com.os.console.api.tasks.SearchContractReturnsTask;

public class ContractConsole {

	private static final Logger logger = LoggerFactory.getLogger(ContractConsole.class);

	public void execute(BufferedReader consoleIn, WebClient webClient, Contract contract) {

		String command = null;
		System.out.print("/contracts/" + contract.getContractId() + " > ");

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
					
					System.out.println(gson.toJson(contract));
					System.out.println();

				} else if (command.equalsIgnoreCase("u")) {
					System.out.print("Retrieving all returns...");
					SearchContractReturnsTask searchContractReturnsTask = new SearchContractReturnsTask(webClient, contract);
					Thread taskT = new Thread(searchContractReturnsTask);
					taskT.run();
					try {
						taskT.join();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} else if (command.equalsIgnoreCase("c")) {
					System.out.print("Retrieving all recalls...");
					SearchContractRecallsTask searchContractRecallsTask = new SearchContractRecallsTask(webClient, contract);
					Thread taskT = new Thread(searchContractRecallsTask);
					taskT.run();
					try {
						taskT.join();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} else if (command.equalsIgnoreCase("a")) {
					System.out.print("Retrieving all rerates...");
					SearchContractReratesTask searchContractReratesTask = new SearchContractReratesTask(webClient, contract);
					Thread taskT = new Thread(searchContractReratesTask);
					taskT.run();
					try {
						taskT.join();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} else {
					System.out.println("Unknown command");
				}

				System.out.print("/contracts/" + contract.getContractId() + " > ");
			}
		} catch (Exception e) {
			logger.error("Exception with contracts command: " + command);
			e.printStackTrace();
		}

	}

	private void printMainContractHelp() {
		System.out.println();
		System.out.println("Contract Menu");
		System.out.println("-----------------------");
		System.out.println("J - Print JSON");
		System.out.println("U - List Returns");
		System.out.println("C - List Recalls");
		System.out.println("A - List Rerates");
		System.out.println("X - Go back");
		System.out.println();
	}

}
