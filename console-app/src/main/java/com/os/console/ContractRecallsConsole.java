package com.os.console;

import java.io.BufferedReader;
import java.util.UUID;

import org.springframework.web.reactive.function.client.WebClient;

import com.os.client.model.Contract;
import com.os.client.model.RecallProposal;
import com.os.console.api.ConsoleConfig;
import com.os.console.api.tasks.ProposeRecallTask;
import com.os.console.api.tasks.SearchContractRecallTask;
import com.os.console.api.tasks.SearchContractRecallsTask;
import com.os.console.util.ConsoleOutputUtil;
import com.os.console.util.PayloadUtil;

public class ContractRecallsConsole extends AbstractConsole {

	Contract contract;

	public ContractRecallsConsole(Contract contract) {
		this.contract = contract;
	}

	protected boolean prompt() {
		
		if (contract == null) {
			System.out.println("Contract not available");
			return false;
		}
		
		System.out.print("/contracts/" + contract.getContractId() + "/recalls > ");
		return true;
	}

	public void handleArgs(String args[], BufferedReader consoleIn, WebClient webClient) {
		if (args[0].equals("L")) {
			System.out.print("Listing all recalls...");
			SearchContractRecallsTask searchContractRecallsTask = new SearchContractRecallsTask(webClient, contract);
			Thread taskT = new Thread(searchContractRecallsTask);
			taskT.run();
			try {
				taskT.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} else if (args[0].equals("S")) {
			if (args.length != 2 || args[1].length() == 0 || args[1].length() != 36) {
				System.out.println("Invalid UUID");
			} else {
				String recallId = args[1];
				try {
					if (UUID.fromString(recallId).toString().equals(recallId)) {
						System.out.print("Retrieving recall " + recallId + "...");
						SearchContractRecallTask searchContractRecallTask = new SearchContractRecallTask(webClient,
								contract.getContractId(), recallId);
						Thread taskT = new Thread(searchContractRecallTask);
						taskT.run();
						try {
							taskT.join();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						if (searchContractRecallTask.getRecall() != null) {
							ContractRecallConsole contractRecallConsole = new ContractRecallConsole(contract,
									searchContractRecallTask.getRecall());
							contractRecallConsole.execute(consoleIn, webClient);
						}
					} else {
						System.out.println("Invalid UUID");
					}
				} catch (Exception u) {
					System.out.println("Invalid UUID");
				}
			}
		} else if (args[0].equals("R")) {
			if (args.length != 2 || args[1].length() == 0 || args[1].length() > 15) {
				System.out.println("Invalid quantity");
			} else {
				Integer quantity = Integer.valueOf(args[1]);
				try {
					System.out.print("Notifying recall...");

					RecallProposal recallProposal = PayloadUtil.createRecallProposal(quantity);

					ConsoleOutputUtil.printObject(recallProposal);

					ProposeRecallTask proposeRecallTask = new ProposeRecallTask(webClient, contract, recallProposal,
							ConsoleConfig.ACTING_PARTY);
					Thread taskT = new Thread(proposeRecallTask);
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
		System.out.println("Contract Recalls Menu");
		System.out.println("-----------------------");
		System.out.println("L                   - List all recalls");
		System.out.println("S <Recall ID>       - Load recall by Id");
		System.out.println();
		System.out.println("R <Quantity>        - Notify recall");
		System.out.println();
		System.out.println("X                   - Go back");
	}

}
