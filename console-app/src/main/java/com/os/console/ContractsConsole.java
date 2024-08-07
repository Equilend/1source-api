package com.os.console;

import java.io.BufferedReader;
import java.util.UUID;

import org.springframework.web.reactive.function.client.WebClient;

import com.os.client.model.PartyRole;
import com.os.console.api.ConsoleConfig;
import com.os.console.api.tasks.ApproveContractTask;
import com.os.console.api.tasks.CancelContractTask;
import com.os.console.api.tasks.DeclineContractTask;
import com.os.console.api.tasks.SearchContractHistoryTask;
import com.os.console.api.tasks.SearchContractRateHistoryTask;
import com.os.console.api.tasks.SearchContractTask;
import com.os.console.api.tasks.SearchContractsTask;
import com.os.console.api.tasks.SearchPartyTask;
import com.os.console.api.tasks.UpdateContractSettlementStatusTask;
import com.os.console.util.PayloadUtil;

public class ContractsConsole extends AbstractConsole {

	public ContractsConsole() {

	}

	protected boolean prompt() {
		System.out.print("/contracts > ");
		return true;
	}

	public void handleArgs(String args[], BufferedReader consoleIn, WebClient webClient) {

		if (args[0].equals("L")) {
			System.out.print("Listing all contracts...");
			SearchContractsTask searchContractsTask = new SearchContractsTask(webClient);
			Thread taskT = new Thread(searchContractsTask);
			taskT.run();
			try {
				taskT.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} else if (args[0].equals("S")) {
			if (args.length != 2 || args[1].length() != 36) {
				System.out.println("Invalid UUID");
			} else {
				String contractId = args[1].toLowerCase();
				try {
					if (UUID.fromString(contractId).toString().equalsIgnoreCase(contractId)) {
						System.out.print("Searching for contract " + contractId + "...");
						SearchContractTask searchContractTask = new SearchContractTask(webClient, contractId);
						Thread taskT = new Thread(searchContractTask);
						taskT.run();
						try {
							taskT.join();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						if (searchContractTask.getContract() != null) {
							ContractConsole contractConsole = new ContractConsole(searchContractTask.getContract());
							contractConsole.execute(consoleIn, webClient);
						}
					} else {
						System.out.println("Invalid UUID");
					}
				} catch (Exception u) {
					System.out.println("Invalid UUID");
				}
			}
		} else if (args[0].equals("H")) {
			if (args.length != 2 || args[1].length() != 36) {
				System.out.println("Invalid UUID");
			} else {
				String contractId = args[1].toLowerCase();
				try {
					if (UUID.fromString(contractId).toString().equalsIgnoreCase(contractId)) {
						System.out.print("Searching for contract " + contractId + "...");
						SearchContractTask searchContractTask = new SearchContractTask(webClient, contractId);
						Thread taskT = new Thread(searchContractTask);
						taskT.run();
						try {
							taskT.join();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						if (searchContractTask.getContract() != null) {
							System.out.print("Listing contract full history " + contractId + "...");
							SearchContractHistoryTask searchContractHistoryTask = new SearchContractHistoryTask(webClient, searchContractTask.getContract());
							Thread taskS = new Thread(searchContractHistoryTask);
							taskS.run();
							try {
								taskS.join();
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					} else {
						System.out.println("Invalid UUID");
					}
				} catch (Exception u) {
					System.out.println("Invalid UUID");
				}
			}
		} else if (args[0].equals("Y")) {
			if (args.length != 2 || args[1].length() != 36) {
				System.out.println("Invalid UUID");
			} else {
				String contractId = args[1].toLowerCase();
				try {
					if (UUID.fromString(contractId).toString().equalsIgnoreCase(contractId)) {
						System.out.print("Searching for contract " + contractId + "...");
						SearchContractTask searchContractTask = new SearchContractTask(webClient, contractId);
						Thread taskT = new Thread(searchContractTask);
						taskT.run();
						try {
							taskT.join();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						if (searchContractTask.getContract() != null) {
							System.out.print("Listing contract rate change history " + contractId + "...");
							SearchContractRateHistoryTask searchContractRateHistoryTask = new SearchContractRateHistoryTask(webClient, searchContractTask.getContract());
							Thread taskS = new Thread(searchContractRateHistoryTask);
							taskS.run();
							try {
								taskS.join();
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					} else {
						System.out.println("Invalid UUID");
					}
				} catch (Exception u) {
					System.out.println("Invalid UUID");
				}
			}
		} else if (args[0].equals("A")) {
			if (args.length != 2 || args[1].length() != 36) {
				System.out.println("Invalid UUID");
			} else {
				String contractId = args[1].toLowerCase();
				try {
					if (UUID.fromString(contractId).toString().equalsIgnoreCase(contractId)) {
						System.out.print("Searching for contract " + contractId + "...");
						SearchContractTask searchContractTask = new SearchContractTask(webClient, contractId);
						Thread taskT = new Thread(searchContractTask);
						taskT.run();
						try {
							taskT.join();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						if (searchContractTask.getContract() != null) {
							System.out.print("Approving contract " + contractId + "...");
							ApproveContractTask approveContractTask = new ApproveContractTask(webClient,
									searchContractTask.getContract(), PayloadUtil.createContractProposalApproval());
							Thread taskS = new Thread(approveContractTask);
							taskS.run();
							try {
								taskS.join();
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					} else {
						System.out.println("Invalid UUID");
					}
				} catch (Exception u) {
					System.out.println("Invalid UUID");
				}
			}
		} else if (args[0].equals("C")) {
			if (args.length != 2 || args[1].length() != 36) {
				System.out.println("Invalid UUID");
			} else {
				String contractId = args[1].toLowerCase();
				try {
					if (UUID.fromString(contractId).toString().equalsIgnoreCase(contractId)) {
						System.out.print("Canceling contract " + contractId + "...");
						CancelContractTask cancelContractTask = new CancelContractTask(webClient, contractId);
						Thread taskT = new Thread(cancelContractTask);
						taskT.run();
						try {
							taskT.join();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					} else {
						System.out.println("Invalid UUID");
					}
				} catch (Exception u) {
					System.out.println("Invalid UUID");
				}
			}
		} else if (args[0].equals("D")) {
			if (args.length != 2 || args[1].length() != 36) {
				System.out.println("Invalid UUID");
			} else {
				String contractId = args[1].toLowerCase();
				try {
					if (UUID.fromString(contractId).toString().equalsIgnoreCase(contractId)) {
						System.out.print("Declining contract " + contractId + "...");
						DeclineContractTask declineContractTask = new DeclineContractTask(webClient, contractId);
						Thread taskT = new Thread(declineContractTask);
						taskT.run();
						try {
							taskT.join();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					} else {
						System.out.println("Invalid UUID");
					}
				} catch (Exception u) {
					System.out.println("Invalid UUID");
				}
			}
		} else if (args[0].equals("U")) {
			if (args.length != 2 || args[1].length() != 36) {
				System.out.println("Invalid UUID");
			} else {
				String contractId = args[1].toLowerCase();
				try {
					if (UUID.fromString(contractId).toString().equalsIgnoreCase(contractId)) {

						System.out.print("Retrieving contract " + contractId + "...");

						SearchContractTask searchContractTask = new SearchContractTask(webClient, contractId);
						Thread taskT = new Thread(searchContractTask);
						taskT.run();
						try {
							taskT.join();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}

						if (searchContractTask.getContract() != null) {
							System.out.print("Updating contract " + contractId + " settlement status to SETTLED...");
							UpdateContractSettlementStatusTask updateSettlementStatusTask = new UpdateContractSettlementStatusTask(
									webClient, searchContractTask.getContract(), ConsoleConfig.ACTING_PARTY);
							Thread taskU = new Thread(updateSettlementStatusTask);
							taskU.run();
							try {
								taskU.join();
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					} else {
						System.out.println("Invalid UUID");
					}
				} catch (Exception u) {
					System.out.println("Invalid UUID");
				}
			}
		} else if (args[0].equals("P")) {
			if (args.length != 2 || args[1].length() > 30) {
				System.out.println("Invalid Party Id");
			} else if (ConsoleConfig.ACTING_PARTY.getPartyId().equals(args[1])) {
				System.out.println("You cannot propose a contract to yourself");
			} else {

				String partyId = args[1];
				try {
					System.out.print("Verifying party " + partyId + "...");
					SearchPartyTask searchPartyTask = new SearchPartyTask(webClient, partyId);
					Thread taskT = new Thread(searchPartyTask);
					taskT.run();
					try {
						taskT.join();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if (searchPartyTask.getParty() != null) {
						ContractProposalConsole contractProposalConsole = new ContractProposalConsole(
								(PartyRole.BORROWER.equals(ConsoleConfig.ACTING_AS) ? ConsoleConfig.ACTING_PARTY
										: searchPartyTask.getParty()),
								(PartyRole.LENDER.equals(ConsoleConfig.ACTING_AS) ? ConsoleConfig.ACTING_PARTY
										: searchPartyTask.getParty()),
								ConsoleConfig.ACTING_AS);
						contractProposalConsole.execute(consoleIn, webClient);
					}
				} catch (Exception u) {
					System.out.println("Invalid party id");
				}
			}
		} else {
			System.out.println("Unknown command");
		}

	}

	protected void printMenu() {
		System.out.println("Contracts Menu");
		System.out.println("-----------------------");
		System.out.println("L               - List all contracts");
		System.out.println("S <Contract Id> - Search a contract by Id");
		System.out.println("H <Contract Id> - Show full history for contract Id");
		System.out.println("Y <Contract Id> - Show rate change history for contract Id");
		System.out.println();
		System.out.println("A <Contract Id> - Approve a contract by Id");
		System.out.println("C <Contract Id> - Cancel a contract by Id");
		System.out.println("D <Contract Id> - Decline a contract by Id");
		System.out.println();
		System.out.println("U <Contract Id> - Update settlement status to SETTLED");
		System.out.println();
		System.out.println("P <Party ID>    - Propose a contract to Party Id");
		System.out.println();
		System.out.println("X               - Go back");
	}

}
