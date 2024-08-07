package com.os.console;

import java.io.BufferedReader;
import org.springframework.web.reactive.function.client.WebClient;

import com.os.client.model.Contract;
import com.os.client.model.Rerate;
import com.os.console.util.ConsoleOutputUtil;

public class ContractRerateConsole extends AbstractConsole {

	Contract contract;
	Rerate rerate;

	public ContractRerateConsole(Contract contract, Rerate rerate) {
		this.contract = contract;
		this.rerate = rerate;
	}

	protected void prompt() {
		System.out.print("/contracts/" + contract.getContractId() + "/rerates/" + rerate.getRerateId() + " > ");
	}

	public void handleArgs(String args[], BufferedReader consoleIn, WebClient webClient) {

		if (args[0].equals("J")) {

			ConsoleOutputUtil.printObject(rerate);

		} else {
			System.out.println("Unknown command");
		}
	}

	protected void printMenu() {
		System.out.println("Contract Rerate Menu");
		System.out.println("-----------------------");
		System.out.println("J             - Print JSON");
		System.out.println();
		System.out.println("X             - Go back");
	}

}
