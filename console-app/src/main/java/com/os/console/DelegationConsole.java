package com.os.console;

import java.io.BufferedReader;
import java.time.LocalDate;
import java.time.OffsetDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.WebClient;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.os.client.model.Delegation;
import com.os.console.api.LocalDateTypeGsonAdapter;
import com.os.console.api.OffsetDateTimeTypeGsonAdapter;
import com.os.console.api.tasks.ApproveDelegationTask;
import com.os.console.api.tasks.CancelDelegationTask;
import com.os.console.api.tasks.DeclineDelegationTask;
import com.os.console.api.tasks.SearchDelegationTask;

public class DelegationConsole {

	private static final Logger logger = LoggerFactory.getLogger(DelegationConsole.class);

	Delegation delegation;
	
	public void execute(BufferedReader consoleIn, WebClient webClient, Delegation origDelegation) {

		this.delegation = origDelegation;
		
		String command = null;
		System.out.print("/delegations/" + delegation.getDelegationId() + " > ");

		try {
			while ((command = consoleIn.readLine()) != null) {
				command = command.trim();
				if (command.equals("?") || command.equalsIgnoreCase("HELP")) {
					printMainContractHelp();
				} else if (command.equalsIgnoreCase("QUIT") || command.equalsIgnoreCase("EXIT") || command.equalsIgnoreCase("Q")) {
					System.exit(0);
				} else if (command.equalsIgnoreCase("X")) {
					break;
				} else if (command.equalsIgnoreCase("J")) {

					Gson gson = new GsonBuilder()
							.setPrettyPrinting()
						    .registerTypeAdapter(LocalDate.class, new LocalDateTypeGsonAdapter())
						    .registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeTypeGsonAdapter())
						    .create();
					
					System.out.println(gson.toJson(delegation));
					System.out.println();

				} else if (command.equalsIgnoreCase("A")) {
					System.out.print("Approving delegation...");
					ApproveDelegationTask approveDelegationTask = new ApproveDelegationTask(webClient, delegation);
					Thread taskT = new Thread(approveDelegationTask);
					taskT.run();
					try {
						taskT.join();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					refreshDelegation(webClient);
				} else if (command.equalsIgnoreCase("C")) {
					System.out.print("Canceling delegation...");
					CancelDelegationTask cancelDelegationTask = new CancelDelegationTask(webClient, delegation);
					Thread taskT = new Thread(cancelDelegationTask);
					taskT.run();
					try {
						taskT.join();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					refreshDelegation(webClient);
				} else if (command.equalsIgnoreCase("D")) {
					System.out.print("Declining delegation...");
					DeclineDelegationTask declineDelegationTask = new DeclineDelegationTask(webClient, delegation);
					Thread taskT = new Thread(declineDelegationTask);
					taskT.run();
					try {
						taskT.join();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					refreshDelegation(webClient);
				} else {
					System.out.println("Unknown command");
				}

				System.out.print("/delgations/" + delegation.getDelegationId() + " > ");
			}
		} catch (Exception e) {
			logger.error("Exception with delegations command: " + command);
			e.printStackTrace();
		}

	}

	private void refreshDelegation(WebClient webClient) {
		
		System.out.print("Refreshing delegation " + delegation.getDelegationId() + "...");
		SearchDelegationTask searchDelegationTask = new SearchDelegationTask(webClient, delegation.getDelegationId());
		Thread taskT = new Thread(searchDelegationTask);
		taskT.run();
		try {
			taskT.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		delegation = searchDelegationTask.getDelegation();
	}
	
	private void printMainContractHelp() {
		System.out.println();
		System.out.println("Delgation Menu");
		System.out.println("-----------------------");
		System.out.println("J             - Print JSON");
		System.out.println("A             - Approve");
		System.out.println("C             - Cancel");
		System.out.println("D             - Decline");
		System.out.println("X             - Go back");
		System.out.println();
	}

}
