package com.os.console;

import java.io.BufferedReader;
import org.springframework.web.reactive.function.client.WebClient;

import com.os.client.model.Contract;
import com.os.client.model.Recall;
import com.os.console.util.ConsoleOutputUtil;

public class ContractRecallConsole extends AbstractConsole {

	private Contract contract;
	private Recall recall;

	public ContractRecallConsole(Contract contract, Recall recall) {
		this.contract = contract;
		this.recall = recall;
	}

	protected boolean prompt() {

		if (contract == null) {
			System.out.println("Contract not available");
			return false;
		} else if (recall == null) {
			System.out.println("Recall not available");
			return false;
		}

		System.out.print("/contracts/" + contract.getContractId() + "/recalls/" + recall.getRecallId() + " > ");
		return true;
	}

	public void handleArgs(String args[], BufferedReader consoleIn, WebClient webClient) {

		if (args[0].equals("J")) {
			ConsoleOutputUtil.printObject(recall);
		} else {
			System.out.println("Unknown command");
		}
	}

	protected void printMenu() {
		System.out.println("Contract Recall Menu");
		System.out.println("-----------------------");
		System.out.println("J             - Print JSON");
		System.out.println();
		System.out.println("X             - Go back");
	}

}
