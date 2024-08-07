package com.os.console;

import java.io.BufferedReader;
import java.util.UUID;

import org.springframework.web.reactive.function.client.WebClient;

import com.os.client.model.Contract;
import com.os.client.model.RerateProposal;
import com.os.console.api.ConsoleConfig;
import com.os.console.api.tasks.ProposeRerateTask;
import com.os.console.api.tasks.SearchContractRerateTask;
import com.os.console.api.tasks.SearchContractReratesTask;
import com.os.console.util.ConsoleOutputUtil;
import com.os.console.util.PayloadUtil;

public class ContractReratesConsole extends AbstractConsole {

	Contract contract;

	public ContractReratesConsole(Contract contract) {
		this.contract = contract;
	}

	protected void prompt() {
		System.out.print("/contracts/" + contract.getContractId() + "/rerates > ");
	}

	public void handleArgs(String args[], BufferedReader consoleIn, WebClient webClient) {

		if (args[0].equals("L")) {
			System.out.print("Listing all rerates...");
			SearchContractReratesTask searchContractReratesTask = new SearchContractReratesTask(webClient, contract);
			Thread taskT = new Thread(searchContractReratesTask);
			taskT.run();
			try {
				taskT.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} else if (args[0].equals("S ")) {
			if (args.length != 2 || args[1].length() == 0 || args[1].length() != 36) {
				System.out.println("Invalid UUID");
			} else {
				String rerateId = args[1];
				try {
					if (UUID.fromString(rerateId).toString().equals(rerateId)) {
						System.out.print("Retrieving rerate " + rerateId + "...");
						SearchContractRerateTask searchContractRerateTask = new SearchContractRerateTask(webClient,
								contract.getContractId(), rerateId);
						Thread taskT = new Thread(searchContractRerateTask);
						taskT.run();
						try {
							taskT.join();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						if (searchContractRerateTask.getRerate() != null) {
							ContractRerateConsole contractRerateConsole = new ContractRerateConsole(contract,
									searchContractRerateTask.getRerate());
							contractRerateConsole.execute(consoleIn, webClient);
						}
					} else {
						System.out.println("Invalid UUID");
					}
				} catch (Exception u) {
					System.out.println("Invalid UUID");
				}
			}
		} else if (args[0].equals("P")) {
			if (args.length != 2 || args[1].length() == 0 || args[1].length() > 15) {
				System.out.println("Invalid spread/fee");
			} else {
				Double rerate = Double.valueOf(args[1]);
				try {
					System.out.print("Proposing rerate...");

					RerateProposal rerateProposal = PayloadUtil.createRerateProposal(contract, rerate);

					ConsoleOutputUtil.printObject(rerateProposal);

					ProposeRerateTask proposeRerateTask = new ProposeRerateTask(webClient, contract, rerateProposal,
							ConsoleConfig.ACTING_PARTY);
					Thread taskT = new Thread(proposeRerateTask);
					taskT.run();
					try {
						taskT.join();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} catch (Exception u) {
					System.out.println("Invalid quantity");
				}
			}
		} else {
			System.out.println("Unknown command");
		}
	}

	protected void printMenu() {
		System.out.println("Contract Rerates Menu");
		System.out.println("-----------------------");
		System.out.println("L                   - List all rerates");
		System.out.println("S <Recall ID>       - Load rerate by Id");
		System.out.println("P <Spread/Fee>      - Propose rerate");
		System.out.println("X                   - Go back");
	}

}
