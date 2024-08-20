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
import com.os.console.api.tasks.SearchLoanRerateTask;
import com.os.console.api.tasks.SearchLoanTask;
import com.os.console.util.ConsoleOutputUtil;

public class RerateConsole extends AbstractConsole {

	private static final Logger logger = LoggerFactory.getLogger(RerateConsole.class);

	Rerate rerate;

	public RerateConsole(Rerate rerate) {
		this.rerate = rerate;
	}

	protected boolean prompt() {
		
		if (rerate == null) {
			System.out.println("Rerate not available");
			return false;
		}

		System.out.print("/rerates/" + rerate.getRerateId() + " > ");
		
		return true;
	}

	public void handleArgs(String args[], BufferedReader consoleIn, WebClient webClient) {

		if (args[0].equals("J")) {
			ConsoleOutputUtil.printObject(rerate);
		} else if (args[0].equals("A")) {
			System.out.print("Searching for contract " + rerate.getContractId() + "...");

			SearchLoanTask searchContractTask = new SearchLoanTask(webClient, rerate.getContractId());
			Thread taskT = new Thread(searchContractTask);
			taskT.run();
			try {
				taskT.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			if (searchContractTask.getContract() != null) {
				Contract contract = searchContractTask.getContract();
				System.out.print("Approving rerate...");
				ApproveRerateTask approveRerateTask = new ApproveRerateTask(webClient, contract.getContractId(), rerate.getRerateId());
				Thread taskS = new Thread(approveRerateTask);
				taskS.run();
				try {
					taskS.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				refreshRerate(webClient, searchContractTask.getContract());
			}
		} else if (args[0].equals("C")) {
			System.out.print("Searching for contract " + rerate.getContractId() + "...");

			SearchLoanTask searchContractTask = new SearchLoanTask(webClient, rerate.getContractId());
			Thread taskT = new Thread(searchContractTask);
			taskT.run();
			try {
				taskT.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			if (searchContractTask.getContract() != null) {
				System.out.print("Canceling rerate...");
				CancelRerateTask cancelRerateTask = new CancelRerateTask(webClient, rerate.getContractId(), rerate.getRerateId());
				Thread taskS = new Thread(cancelRerateTask);
				taskS.run();
				try {
					taskS.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				refreshRerate(webClient, searchContractTask.getContract());
			}
		} else if (args[0].equals("D")) {
			System.out.print("Searching for contract " + rerate.getContractId() + "...");

			SearchLoanTask searchContractTask = new SearchLoanTask(webClient, rerate.getContractId());
			Thread taskT = new Thread(searchContractTask);
			taskT.run();
			try {
				taskT.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			if (searchContractTask.getContract() != null) {
				System.out.print("Declining rerate...");
				DeclineRerateTask declineRerateTask = new DeclineRerateTask(webClient, rerate.getContractId(), rerate.getRerateId());
				Thread taskS = new Thread(declineRerateTask);
				taskS.run();
				try {
					taskS.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				refreshRerate(webClient, searchContractTask.getContract());
			}
		} else {
			System.out.println("Unknown command");
		}
	}

	private void refreshRerate(WebClient webClient, Contract contract) {

		System.out.print("Refreshing rerate " + rerate.getRerateId() + "...");
		SearchLoanRerateTask searchContractRerateTask = new SearchLoanRerateTask(webClient, contract.getContractId(),
				rerate.getRerateId());
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
		System.out.println("Rerate Menu");
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
