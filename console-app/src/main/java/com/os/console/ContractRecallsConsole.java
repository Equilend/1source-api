package com.os.console;

import java.io.BufferedReader;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.WebClient;

import com.os.client.model.Contract;
import com.os.client.model.RecallProposal;
import com.os.console.api.ConsoleConfig;
import com.os.console.api.tasks.ProposeRecallTask;
import com.os.console.api.tasks.SearchContractRecallTask;
import com.os.console.api.tasks.SearchContractRecallsTask;

public class ContractRecallsConsole extends AbstractConsole {

	private static final Logger logger = LoggerFactory.getLogger(ContractRecallsConsole.class);

	Contract contract;

	protected void prompt() {
		System.out.print("/contracts/" + contract.getContractId() + "/recalls > ");
	}

	public void execute(BufferedReader consoleIn, ConsoleConfig consoleConfig, WebClient webClient,
			Contract origContract) {

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
					if (command.equals("A")) {
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
					} else if (command.startsWith("S ")) {
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
					} else if (command.startsWith("R ")) {
						if (command.length() > 15) {
							System.out.println("Invalid quantity");
						} else {
							Integer quantity = Integer.valueOf(command.substring(2));
							try {
								System.out.print("Notifying recall...");
								ProposeRecallTask proposeRecallTask = new ProposeRecallTask(webClient,
										contract.getContractId(), createRecallProposal(consoleConfig, quantity));
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

				prompt();
			}
		} catch (Exception e) {
			logger.error("Exception with contract recalls command: " + command);
			e.printStackTrace();
		}

	}

	private RecallProposal createRecallProposal(ConsoleConfig consoleConfig, Integer quantity) {

		RecallProposal proposal = new RecallProposal();

		proposal.setQuantity(quantity);
		proposal.setRecallDate(LocalDate.now(ZoneId.of("UTC")));
		
		LocalDate recallDueDate = proposal.getRecallDate().plusDays(3);
		if (recallDueDate.getDayOfWeek().equals(DayOfWeek.SATURDAY)) {
			recallDueDate = recallDueDate.plusDays(2);
		} else if (recallDueDate.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
			recallDueDate = recallDueDate.plusDays(1);
		}
		proposal.setRecallDueDate(recallDueDate);

//		PartySettlementInstruction partySettlementInstruction = new PartySettlementInstruction();
//		partySettlementInstruction.setPartyRole(ConsoleConfig.ACTING_AS);
//		partySettlementInstruction.setSettlementStatus(SettlementStatus.NONE);
//		partySettlementInstruction.setInternalAcctCd(consoleConfig.getSettlement_internalAcctCd());
//
//		SettlementInstruction instruction = new SettlementInstruction();
//		partySettlementInstruction.setInstruction(instruction);
//		instruction.setSettlementBic(consoleConfig.getSettlement_settlementBic());
//		instruction.setLocalAgentBic(consoleConfig.getSettlement_localAgentBic());
//		instruction.setLocalAgentName(consoleConfig.getSettlement_localAgentName());
//		instruction.setLocalAgentAcct(consoleConfig.getSettlement_localAgentAcct());
//		instruction.setCustodianBic(consoleConfig.getSettlement_custodianBic());
//		instruction.setCustodianName(consoleConfig.getSettlement_custodianName());
//		instruction.setCustodianAcct(consoleConfig.getSettlement_custodianAcct());
//		instruction.setDtcParticipantNumber(consoleConfig.getSettlement_dtcParticipantNumber());
//
//		proposal.setSettlement(partySettlementInstruction);

		return proposal;
	}

	protected void printMenu() {
		System.out.println("Contract Recalls Menu");
		System.out.println("-----------------------");
		System.out.println("A                   - List Recalls");
		System.out.println("S <Recall ID>       - Load recall by Id");
		System.out.println("R <Quantity>        - Notify recall");
		System.out.println("X                   - Go back");
	}

}
