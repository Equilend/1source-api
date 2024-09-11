package com.os.console;

import java.io.BufferedReader;
import org.springframework.web.reactive.function.client.WebClient;

import com.os.client.model.Delegation;
import com.os.console.api.tasks.ApproveDelegationTask;
import com.os.console.api.tasks.CancelDelegationTask;
import com.os.console.api.tasks.DeclineDelegationTask;
import com.os.console.api.tasks.SearchDelegationTask;
import com.os.console.util.ConsoleOutputUtil;

public class DelegationConsole extends AbstractConsole {

	Delegation delegation;

	public DelegationConsole(Delegation delegation) {
		this.delegation = delegation;
	}

	protected boolean prompt() {
		
		if (delegation == null) {
			System.out.println("Delegation not available");
			return false;
		}
		
		System.out.print("/delegations/" + delegation.getDelegationId() + " > ");
		return true;
	}

	public void handleArgs(String args[], BufferedReader consoleIn, WebClient webClient) {

		if (args[0].equals("J")) {

			ConsoleOutputUtil.printObject(delegation);

		} else if (args[0].equals("A")) {
			System.out.print("Approving delegation...");
			ApproveDelegationTask approveDelegationTask = new ApproveDelegationTask(webClient,
					delegation.getDelegationId());
			Thread taskT = new Thread(approveDelegationTask);
			taskT.run();
			try {
				taskT.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			refreshDelegation(webClient);
		} else if (args[0].equals("C")) {
			System.out.print("Canceling delegation...");
			CancelDelegationTask cancelDelegationTask = new CancelDelegationTask(webClient,
					delegation.getDelegationId());
			Thread taskT = new Thread(cancelDelegationTask);
			taskT.run();
			try {
				taskT.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			refreshDelegation(webClient);
		} else if (args[0].equals("D")) {
			System.out.print("Declining delegation...");
			DeclineDelegationTask declineDelegationTask = new DeclineDelegationTask(webClient,
					delegation.getDelegationId());
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

	protected void printMenu() {
		System.out.println("Delgation Menu");
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
