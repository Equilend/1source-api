package com.os.console;

import java.io.BufferedReader;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.WebClient;

import com.os.client.model.PartyRole;
import com.os.console.api.ConsoleConfig;
import com.os.console.api.tasks.ApproveContractTask;
import com.os.console.api.tasks.CancelContractTask;
import com.os.console.api.tasks.DeclineContractTask;
import com.os.console.api.tasks.SearchContractTask;
import com.os.console.api.tasks.SearchContractsTask;
import com.os.console.api.tasks.SearchPartyTask;
import com.os.console.api.tasks.UpdateContractSettlementStatusTask;
import com.os.console.util.PayloadUtil;

public class ContractsConsole extends AbstractConsole {

	private static final Logger logger = LoggerFactory.getLogger(ContractsConsole.class);

	protected void prompt() {
		System.out.print("/contracts > ");
	}

	public void execute(BufferedReader consoleIn, ConsoleConfig consoleConfig, WebClient webClient) {

		String input = null;

		prompt();

		try {
			while ((input = consoleIn.readLine()) != null) {

				String[] args = parseArgs(input);
				if (args.length == 0) {
					prompt();
					continue;
				}

				if (checkSystemCommand(args[0])) {
					continue;
				} else if (goBackMenu(args[0])) {
					break;
				} else {
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
									SearchContractTask searchContractTask = new SearchContractTask(webClient,
											contractId);
									Thread taskT = new Thread(searchContractTask);
									taskT.run();
									try {
										taskT.join();
									} catch (InterruptedException e) {
										e.printStackTrace();
									}
									if (searchContractTask.getContract() != null) {
										ContractConsole contractConsole = new ContractConsole();
										contractConsole.execute(consoleIn, consoleConfig, webClient,
												searchContractTask.getContract());
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
									SearchContractTask searchContractTask = new SearchContractTask(webClient,
											contractId);
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
												searchContractTask.getContract(),
												PayloadUtil.createContractProposalApproval(consoleConfig));
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
									CancelContractTask cancelContractTask = new CancelContractTask(webClient,
											contractId);
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
									DeclineContractTask declineContractTask = new DeclineContractTask(webClient,
											contractId);
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

									SearchContractTask searchContractTask = new SearchContractTask(webClient,
											contractId);
									Thread taskT = new Thread(searchContractTask);
									taskT.run();
									try {
										taskT.join();
									} catch (InterruptedException e) {
										e.printStackTrace();
									}

									if (searchContractTask.getContract() != null) {
										System.out.print(
												"Updating contract " + contractId + " settlement status to SETTLED...");
										UpdateContractSettlementStatusTask updateSettlementStatusTask = new UpdateContractSettlementStatusTask(
												webClient, searchContractTask.getContract(),
												ConsoleConfig.ACTING_PARTY);
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
						} else if (consoleConfig.getAuth_party().equals(args[1])) {
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
									ContractProposalConsole contractProposalConsole = new ContractProposalConsole();
									contractProposalConsole.execute(consoleIn, consoleConfig, webClient,
											(PartyRole.BORROWER.equals(ConsoleConfig.ACTING_AS)
													? ConsoleConfig.ACTING_PARTY
													: searchPartyTask.getParty()),
											(PartyRole.LENDER.equals(ConsoleConfig.ACTING_AS)
													? ConsoleConfig.ACTING_PARTY
													: searchPartyTask.getParty()),
											ConsoleConfig.ACTING_AS);
								}
							} catch (Exception u) {
								System.out.println("Invalid party id");
							}
						}
					} else {
						System.out.println("Unknown command");
					}
				}
				prompt();
			}
		} catch (Exception e) {
			logger.error("Exception with contracts command: " + input);
			e.printStackTrace();
		}

	}

	protected void printMenu() {
		System.out.println("Contracts Menu");
		System.out.println("-----------------------");
		System.out.println("L               - List all contracts");
		System.out.println("S <Contract Id> - Search a contract by Id");
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
