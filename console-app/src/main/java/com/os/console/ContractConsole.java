package com.os.console;

import java.io.BufferedReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.WebClient;

import com.os.client.model.Contract;
import com.os.console.api.ConsoleConfig;
import com.os.console.api.tasks.ApproveContractTask;
import com.os.console.api.tasks.CancelContractTask;
import com.os.console.api.tasks.DeclineContractTask;
import com.os.console.api.tasks.SearchContractHistoryTask;
import com.os.console.api.tasks.SearchContractRateHistoryTask;
import com.os.console.api.tasks.SearchContractTask;
import com.os.console.api.tasks.UpdateContractVenueKeyTask;
import com.os.console.api.tasks.UpdateContractSettlementStatusTask;
import com.os.console.util.ConsoleOutputUtil;
import com.os.console.util.PayloadUtil;

public class ContractConsole extends AbstractConsole {

	private static final Logger logger = LoggerFactory.getLogger(ContractConsole.class);

	Contract contract;

	public ContractConsole(Contract contract) {
		this.contract = contract;
	}

	protected boolean prompt() {
		
		if (contract == null) {
			System.out.println("Contract not available");
			return false;
		}
		
		System.out.print("/contracts/" + contract.getContractId() + " > ");
		
		return true;
	}

	public void handleArgs(String args[], BufferedReader consoleIn, WebClient webClient) {

		if (args[0].equals("J")) {
			ConsoleOutputUtil.printObject(contract);
		} else if (args[0].equals("F")) {
			refreshContract(webClient);
		} else if (args[0].equals("H")) {
			System.out.print("Listing full history...");
			SearchContractHistoryTask searchContractHistoryTask = new SearchContractHistoryTask(webClient, contract);
			Thread taskT = new Thread(searchContractHistoryTask);
			taskT.run();
			try {
				taskT.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} else if (args[0].equals("Y")) {
			System.out.print("Listing rate change history...");
			SearchContractRateHistoryTask searchContractRateHistoryTask = new SearchContractRateHistoryTask(webClient, contract);
			Thread taskT = new Thread(searchContractRateHistoryTask);
			taskT.run();
			try {
				taskT.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} else if (args[0].equals("A")) {
		
			System.out.print("Approving contract...");
			ApproveContractTask approveContractTask = new ApproveContractTask(webClient, contract,
					PayloadUtil.createContractProposalApproval());
			Thread taskT = new Thread(approveContractTask);
			taskT.run();
			try {
				taskT.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			refreshContract(webClient);
		} else if (args[0].equals("C")) {
			System.out.print("Canceling contract...");
			CancelContractTask cancelContractTask = new CancelContractTask(webClient, contract.getContractId());
			Thread taskT = new Thread(cancelContractTask);
			taskT.run();
			try {
				taskT.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			refreshContract(webClient);
		} else if (args[0].equals("D")) {
			System.out.print("Declining contract...");
			DeclineContractTask declineContractTask = new DeclineContractTask(webClient, contract.getContractId());
			Thread taskT = new Thread(declineContractTask);
			taskT.run();
			try {
				taskT.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			refreshContract(webClient);
		} else if (args[0].equals("U")) {
			System.out.print("Updating settlement status to SETTLED...");
			UpdateContractSettlementStatusTask updateSettlementStatusTask = new UpdateContractSettlementStatusTask(
					webClient, contract, ConsoleConfig.ACTING_PARTY);
			Thread taskT = new Thread(updateSettlementStatusTask);
			taskT.run();
			try {
				taskT.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			refreshContract(webClient);
		} else if (args[0].equals("V")) {
			if (args.length != 2 || args[1].length() == 0 || args[1].length() > 50) {
				System.out.println("Invalid reference key");
			} else {
				String venueRefKey = args[1];
				try {
					System.out.print("Assigning venue reference " + venueRefKey + "...");
					UpdateContractVenueKeyTask updateContractVenueKeyTask = new UpdateContractVenueKeyTask(webClient,
							contract.getContractId(), venueRefKey);
					Thread taskT = new Thread(updateContractVenueKeyTask);
					taskT.run();
					try {
						taskT.join();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					refreshContract(webClient);
				} catch (Exception u) {
					System.out.println("Invalid reference key");
				}
			}
		} else if (args[0].equals("R")) {
			ContractReturnsConsole contractReturnsConsole = new ContractReturnsConsole(contract);
			contractReturnsConsole.execute(consoleIn, webClient);
		} else if (args[0].equals("E")) {
			ContractRecallsConsole contractRecallsConsole = new ContractRecallsConsole(contract);
			contractRecallsConsole.execute(consoleIn, webClient);
		} else if (args[0].equals("T")) {
			ContractReratesConsole contractReratesConsole = new ContractReratesConsole(contract);
			contractReratesConsole.execute(consoleIn, webClient);
		} else {
			System.out.println("Unknown command");
		}
	}

	private void refreshContract(WebClient webClient) {

		System.out.print("Refreshing contract " + contract.getContractId() + "...");
		SearchContractTask searchContractTask = new SearchContractTask(webClient, contract.getContractId());
		Thread taskT = new Thread(searchContractTask);
		taskT.run();
		try {
			taskT.join();
		} catch (InterruptedException e) {
			logger.error(e.getMessage(), e);
		}
		contract = searchContractTask.getContract();
	}

	protected void printMenu() {
		System.out.println("Contract Menu");
		System.out.println("-----------------------");
		System.out.println("J                   - Print JSON");
		System.out.println("F                   - Refresh");
		System.out.println("H                   - Full history");
		System.out.println("Y                   - Rate change history");
		System.out.println();
		System.out.println("A                   - Approve");
		System.out.println("C                   - Cancel");
		System.out.println("D                   - Decline");
		System.out.println("U                   - Update settlement status to SETTLED");
		System.out.println("V <Venue Ref>       - Add a venue reference key");
		System.out.println();
		System.out.println("R                   - Manage returns");
		System.out.println("E                   - Manage recalls");
		System.out.println("T                   - Manage rerates");
		System.out.println();
		System.out.println("X                   - Go back");
	}

}
