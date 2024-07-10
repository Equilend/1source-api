package com.os.console;

import java.io.BufferedReader;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.WebClient;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.os.client.model.Contract;
import com.os.client.model.ContractProposalApproval;
import com.os.client.model.PartyRole;
import com.os.client.model.PartySettlementInstruction;
import com.os.client.model.RoundingMode;
import com.os.client.model.SettlementInstruction;
import com.os.client.model.SettlementStatus;
import com.os.console.api.ConsoleConfig;
import com.os.console.api.LocalDateTypeGsonAdapter;
import com.os.console.api.OffsetDateTimeTypeGsonAdapter;
import com.os.console.api.tasks.ApproveContractTask;
import com.os.console.api.tasks.CancelContractTask;
import com.os.console.api.tasks.DeclineContractTask;
import com.os.console.api.tasks.SearchContractRecallTask;
import com.os.console.api.tasks.SearchContractRecallsTask;
import com.os.console.api.tasks.SearchContractRerateTask;
import com.os.console.api.tasks.SearchContractReratesTask;
import com.os.console.api.tasks.SearchContractReturnTask;
import com.os.console.api.tasks.SearchContractReturnsTask;
import com.os.console.api.tasks.SearchContractTask;
import com.os.console.api.tasks.UpdateContractVenueKeyTask;
import com.os.console.api.tasks.UpdateSettlementStatusTask;

public class ContractConsole extends AbstractConsole {

	private static final Logger logger = LoggerFactory.getLogger(ContractConsole.class);

	Contract contract;
	
	protected void prompt() {
		System.out.print("/contracts/" + contract.getContractId() + " > ");
	}
	
	public void execute(BufferedReader consoleIn, ConsoleConfig consoleConfig, WebClient webClient, Contract origContract) {

		contract = origContract;
		
		String command = null;
		prompt();

		try {
			while ((command = consoleIn.readLine()) != null) {

				command = command.trim().toUpperCase();

				if (checkSystemCommand(command)) {
					continue;
				} else if (goBackMenu(command)) {
					break;
				} else {
					if (command.equals("J")) {

						Gson gson = new GsonBuilder().setPrettyPrinting()
								.registerTypeAdapter(LocalDate.class, new LocalDateTypeGsonAdapter())
								.registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeTypeGsonAdapter())
								.create();

						System.out.println(gson.toJson(contract));
						System.out.println();

					} else if (command.equals("A")) {
						System.out.print("Approving contract...");
						ApproveContractTask approveContractTask = new ApproveContractTask(webClient,
								contract.getContractId(), createContractProposalApproval(consoleConfig));
						Thread taskT = new Thread(approveContractTask);
						taskT.run();
						try {
							taskT.join();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						refreshContract(webClient);
					} else if (command.equals("C")) {
						System.out.print("Canceling contract...");
						CancelContractTask cancelContractTask = new CancelContractTask(webClient,
								contract.getContractId());
						Thread taskT = new Thread(cancelContractTask);
						taskT.run();
						try {
							taskT.join();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						refreshContract(webClient);
					} else if (command.equals("D")) {
						System.out.print("Declining contract...");
						DeclineContractTask declineContractTask = new DeclineContractTask(webClient,
								contract.getContractId());
						Thread taskT = new Thread(declineContractTask);
						taskT.run();
						try {
							taskT.join();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						refreshContract(webClient);
					} else if (command.equals("R")) {
						System.out.print("Retrieving all returns...");
						SearchContractReturnsTask searchContractReturnsTask = new SearchContractReturnsTask(webClient,
								contract);
						Thread taskT = new Thread(searchContractReturnsTask);
						taskT.run();
						try {
							taskT.join();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					} else if (command.startsWith("R ")) {
						if (command.length() != 38) {
							System.out.println("Invalid UUID");
						} else {
							String returnId = command.substring(2);
							try {
								if (UUID.fromString(returnId).toString().equals(returnId)) {
									System.out.print("Retrieving return " + returnId + "...");
									SearchContractReturnTask searchContractReturnTask = new SearchContractReturnTask(
											webClient, contract.getContractId(), returnId);
									Thread taskT = new Thread(searchContractReturnTask);
									taskT.run();
									try {
										taskT.join();
									} catch (InterruptedException e) {
										e.printStackTrace();
									}
									if (searchContractReturnTask.getReturn() != null) {
										ContractReturnConsole contractReturnConsole = new ContractReturnConsole();
										contractReturnConsole.execute(consoleIn, webClient, contract,
												searchContractReturnTask.getReturn());
									}
								} else {
									System.out.println("Invalid UUID");
								}
							} catch (Exception u) {
								System.out.println("Invalid UUID");
							}
						}
					} else if (command.equals("E")) {
						System.out.print("Retrieving all recalls...");
						SearchContractRecallsTask searchContractRecallsTask = new SearchContractRecallsTask(webClient,
								contract);
						Thread taskT = new Thread(searchContractRecallsTask);
						taskT.run();
						try {
							taskT.join();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					} else if (command.startsWith("E ")) {
						if (command.length() != 38) {
							System.out.println("Invalid UUID");
						} else {
							String recallId = command.substring(2);
							try {
								if (UUID.fromString(recallId).toString().equals(recallId)) {
									System.out.print("Retrieving recall " + recallId + "...");
									SearchContractRecallTask searchContractRecallTask = new SearchContractRecallTask(
											webClient, contract.getContractId(), recallId);
									Thread taskT = new Thread(searchContractRecallTask);
									taskT.run();
									try {
										taskT.join();
									} catch (InterruptedException e) {
										e.printStackTrace();
									}
									if (searchContractRecallTask.getRecall() != null) {
										ContractRecallConsole contractRecallConsole = new ContractRecallConsole();
										contractRecallConsole.execute(consoleIn, webClient, contract,
												searchContractRecallTask.getRecall());
									}
								} else {
									System.out.println("Invalid UUID");
								}
							} catch (Exception u) {
								System.out.println("Invalid UUID");
							}
						}
					} else if (command.equals("T")) {
						System.out.print("Retrieving all rerates...");
						SearchContractReratesTask searchContractReratesTask = new SearchContractReratesTask(webClient,
								contract);
						Thread taskT = new Thread(searchContractReratesTask);
						taskT.run();
						try {
							taskT.join();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					} else if (command.startsWith("T ")) {
						if (command.length() != 38) {
							System.out.println("Invalid UUID");
						} else {
							String rerateId = command.substring(2);
							try {
								if (UUID.fromString(rerateId).toString().equals(rerateId)) {
									System.out.print("Retrieving rerate " + rerateId + "...");
									SearchContractRerateTask searchContractRerateTask = new SearchContractRerateTask(
											webClient, contract.getContractId(), rerateId);
									Thread taskT = new Thread(searchContractRerateTask);
									taskT.run();
									try {
										taskT.join();
									} catch (InterruptedException e) {
										e.printStackTrace();
									}
									if (searchContractRerateTask.getRerate() != null) {
										ContractRerateConsole contractRerateConsole = new ContractRerateConsole();
										contractRerateConsole.execute(consoleIn, webClient, contract,
												searchContractRerateTask.getRerate());
									}
								} else {
									System.out.println("Invalid UUID");
								}
							} catch (Exception u) {
								System.out.println("Invalid UUID");
							}
						}
					} else if (command.equals("U")) {
						System.out.print("Updating settlement status to SETTLED...");
						UpdateSettlementStatusTask updateSettlementStatusTask = new UpdateSettlementStatusTask(
								webClient, contract, consoleConfig.getAuth_party());
						Thread taskT = new Thread(updateSettlementStatusTask);
						taskT.run();
						try {
							taskT.join();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						refreshContract(webClient);
					} else if (command.startsWith("V ")) {
						if (command.length() > 100) {
							System.out.println("Invalid reference key");
						} else {
							String venueRefKey = command.substring(2);
							try {
								System.out.print("Assigning venue reference " + venueRefKey + "...");
								UpdateContractVenueKeyTask updateContractVenueKeyTask = new UpdateContractVenueKeyTask(
										webClient, contract.getContractId(), venueRefKey);
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
					} else {
						System.out.println("Unknown command");
					}

				}

				prompt();
			}
		} catch (Exception e) {
			logger.error("Exception with contracts command: " + command);
			e.printStackTrace();
		}

	}

	private ContractProposalApproval createContractProposalApproval(ConsoleConfig consoleConfig) {
		
		ContractProposalApproval proposalApproval = new ContractProposalApproval();
		
		proposalApproval.setInternalRefId(UUID.randomUUID().toString());
		
		if (PartyRole.LENDER.equals(ConsoleConfig.ACTING_AS)) {
			proposalApproval.setRoundingRule(10d);
			proposalApproval.setRoundingMode(RoundingMode.ALWAYSUP);
		}
		
		PartySettlementInstruction partySettlementInstruction = new PartySettlementInstruction();
		partySettlementInstruction.setPartyRole(ConsoleConfig.ACTING_AS);
		partySettlementInstruction.setSettlementStatus(SettlementStatus.NONE);
		partySettlementInstruction.setInternalAcctCd(consoleConfig.getSettlement_internalAcctCd());

		SettlementInstruction instruction = new SettlementInstruction();
		partySettlementInstruction.setInstruction(instruction);
		instruction.setSettlementBic(consoleConfig.getSettlement_settlementBic());
		instruction.setLocalAgentBic(consoleConfig.getSettlement_localAgentBic());
		instruction.setLocalAgentName(consoleConfig.getSettlement_localAgentName());
		instruction.setLocalAgentAcct(consoleConfig.getSettlement_localAgentAcct());
		instruction.setCustodianBic(consoleConfig.getSettlement_custodianBic());
		instruction.setCustodianName(consoleConfig.getSettlement_custodianName());
		instruction.setCustodianAcct(consoleConfig.getSettlement_custodianAcct());
		instruction.setDtcParticipantNumber(consoleConfig.getSettlement_dtcParticipantNumber());

		proposalApproval.setSettlement(partySettlementInstruction);

		return proposalApproval;
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
		System.out.println("J             - Print JSON");
		System.out.println("A             - Approve");
		System.out.println("C             - Cancel");
		System.out.println("D             - Decline");
		System.out.println("R             - List Returns");
		System.out.println("R <Return ID> - Load return by Id");
		System.out.println("E             - List Recalls");
		System.out.println("E <Recall ID> - Load recall by Id");
		System.out.println("T             - List Rerates");
		System.out.println("T <Recall ID> - Load rerate by Id");
		System.out.println("U             - Update settlement status to SETTLED");
		System.out.println("V <Venue Ref> - Add a venue reference key");
		System.out.println("X             - Go back");
	}

}
