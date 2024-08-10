package com.os.console;

import java.io.BufferedReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.WebClient;

import com.os.client.model.Contract;
import com.os.client.model.Recall;
import com.os.console.api.tasks.CancelRecallTask;
import com.os.console.api.tasks.SearchContractRecallTask;
import com.os.console.api.tasks.SearchContractTask;
import com.os.console.util.ConsoleOutputUtil;

public class RecallConsole extends AbstractConsole {

	private static final Logger logger = LoggerFactory.getLogger(RecallConsole.class);

	Recall recall;

	public RecallConsole(Recall recall) {
		this.recall = recall;
	}

	protected boolean prompt() {
		
		if (recall == null) {
			System.out.println("Recall not available");
			return false;
		}
		
		System.out.print("/recalls/" + recall.getRecallId() + " > ");
		
		return true;
	}

	public void handleArgs(String args[], BufferedReader consoleIn, WebClient webClient) {

		if (args[0].equals("J")) {

			ConsoleOutputUtil.printObject(recall);

		} else if (args[0].equals("C")) {
			System.out.print("Searching for contract " + recall.getContractId() + "...");

			SearchContractTask searchContractTask = new SearchContractTask(webClient, recall.getContractId());
			Thread taskT = new Thread(searchContractTask);
			taskT.run();
			try {
				taskT.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			if (searchContractTask.getContract() != null) {
				System.out.print("Canceling recall...");
				CancelRecallTask cancelRecallTask = new CancelRecallTask(webClient, recall.getContractId(), recall.getRecallId());
				Thread taskS = new Thread(cancelRecallTask);
				taskS.run();
				try {
					taskS.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				refreshRecall(webClient, searchContractTask.getContract());
			}
		} else {
			System.out.println("Unknown command");
		}
	}

	private void refreshRecall(WebClient webClient, Contract contract) {

		System.out.print("Refreshing recall " + recall.getRecallId() + "...");
		SearchContractRecallTask searchContractRecallTask = new SearchContractRecallTask(webClient, contract.getContractId(),
				recall.getRecallId());
		Thread taskT = new Thread(searchContractRecallTask);
		taskT.run();
		try {
			taskT.join();
		} catch (InterruptedException e) {
			logger.error(e.getMessage(), e);
		}
		recall = searchContractRecallTask.getRecall();
	}

	protected void printMenu() {
		System.out.println("Recall Menu");
		System.out.println("-----------------------");
		System.out.println("J             - Print JSON");
		System.out.println();
		System.out.println("C             - Cancel");
		System.out.println();
		System.out.println("X             - Go back");
	}

}
