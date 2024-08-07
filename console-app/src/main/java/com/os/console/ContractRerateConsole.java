package com.os.console;

import java.io.BufferedReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.WebClient;

import com.os.client.model.Contract;
import com.os.client.model.Rerate;
import com.os.console.api.tasks.ApproveRerateTask;
import com.os.console.api.tasks.CancelRerateTask;
import com.os.console.api.tasks.DeclineRerateTask;
import com.os.console.api.tasks.SearchContractRerateTask;
import com.os.console.util.ConsoleOutputUtil;

public class ContractRerateConsole extends AbstractConsole {

	private static final Logger logger = LoggerFactory.getLogger(ContractRerateConsole.class);

	Contract contract;
	Rerate rerate;

	public ContractRerateConsole(Contract contract, Rerate rerate) {
		this.contract = contract;
		this.rerate = rerate;
	}

	protected boolean prompt() {
		if (contract == null) {
			System.out.println("Contract not available");
			return false;
		} else if (rerate == null) {
			System.out.println("Rerate not available");
			return false;
		}

		System.out.print("/contracts/" + contract.getContractId() + "/rerates/" + rerate.getRerateId() + " > ");

		return true;
	}

	public void handleArgs(String args[], BufferedReader consoleIn, WebClient webClient) {

		if (args[0].equals("J")) {

			ConsoleOutputUtil.printObject(rerate);

		} else if (args[0].equals("A")) {
			System.out.print("Approving rerate...");
			ApproveRerateTask approveRerateTask = new ApproveRerateTask(webClient, contract.getContractId(),
					rerate.getRerateId());
			Thread taskS = new Thread(approveRerateTask);
			taskS.run();
			try {
				taskS.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			refreshRerate(webClient, contract);
		} else if (args[0].equals("C")) {
			System.out.print("Canceling rerate...");
			CancelRerateTask cancelRerateTask = new CancelRerateTask(webClient, contract.getContractId(),
					rerate.getRerateId());
			Thread taskS = new Thread(cancelRerateTask);
			taskS.run();
			try {
				taskS.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			refreshRerate(webClient, contract);
		} else if (args[0].equals("D")) {
			System.out.print("Declining rerate...");
			DeclineRerateTask declineRerateTask = new DeclineRerateTask(webClient, contract.getContractId(),
					rerate.getRerateId());
			Thread taskS = new Thread(declineRerateTask);
			taskS.run();
			try {
				taskS.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			refreshRerate(webClient, contract);
		} else {
			System.out.println("Unknown command");
		}
	}

	private void refreshRerate(WebClient webClient, Contract contract) {

		System.out.print("Refreshing rerate " + rerate.getRerateId() + "...");
		SearchContractRerateTask searchContractRerateTask = new SearchContractRerateTask(webClient,
				contract.getContractId(), rerate.getRerateId());
		Thread taskT = new Thread(searchContractRerateTask);
		taskT.run();
		try {
			taskT.join();
		} catch (InterruptedException e) {
			logger.error(e.getMessage(), e);
		}
		rerate = searchContractRerateTask.getRerate();
	}

	protected void printMenu() {
		System.out.println("Contract Rerate Menu");
		System.out.println("-----------------------");
		System.out.println("J             - Print JSON");
		System.out.println();
		System.out.println("A             - Approve");
		System.out.println("C             - Cancel");
		System.out.println("D             - Decline");
		System.out.println();
		System.out.println("X             - Go back");
	}

}
