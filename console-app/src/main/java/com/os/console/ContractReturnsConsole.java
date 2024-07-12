package com.os.console;

import java.io.BufferedReader;
import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.WebClient;

import com.os.client.model.Contract;
import com.os.client.model.ReturnProposal;
import com.os.client.model.SettlementType;
import com.os.console.api.ConsoleConfig;
import com.os.console.api.tasks.ProposeReturnTask;
import com.os.console.api.tasks.SearchContractReturnTask;
import com.os.console.api.tasks.SearchContractReturnsTask;

public class ContractReturnsConsole extends AbstractConsole {

	private static final Logger logger = LoggerFactory.getLogger(ContractReturnsConsole.class);

	Contract contract;

	protected void prompt() {
		System.out.print("/contracts/" + contract.getContractId() + "/returns > ");
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
					if (command.equals("L")) {
						System.out.print("Listing all returns...");
						SearchContractReturnsTask searchContractReturnsTask = new SearchContractReturnsTask(webClient,
								contract);
						Thread taskT = new Thread(searchContractReturnsTask);
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
							String returnId = command.substring(2).toLowerCase();
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
					} else if (command.startsWith("R ")) {
						if (command.length() > 15) {
							System.out.println("Invalid quantity");
						} else {
							Integer quantity = Integer.valueOf(command.substring(2));
							try {
								System.out.print("Notifying return...");
								ProposeReturnTask proposeReturnTask = new ProposeReturnTask(webClient,
										contract.getContractId(), createReturnProposal(consoleConfig, quantity));
								Thread taskT = new Thread(proposeReturnTask);
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
			logger.error("Exception with contract returns command: " + command);
			e.printStackTrace();
		}

	}

	private ReturnProposal createReturnProposal(ConsoleConfig consoleConfig, Integer quantity) {

		ReturnProposal proposal = new ReturnProposal();

		proposal.setQuantity(quantity);
		proposal.setReturnDate(LocalDate.now(ZoneId.of("UTC")));
		
		LocalDate returnSettlementDate = proposal.getReturnDate().plusDays(1);
		if (returnSettlementDate.getDayOfWeek().equals(DayOfWeek.SATURDAY)) {
			returnSettlementDate = returnSettlementDate.plusDays(2);
		} else if (returnSettlementDate.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
			returnSettlementDate = returnSettlementDate.plusDays(1);
		}
		proposal.setReturnSettlementDate(returnSettlementDate);
		proposal.setSettlementType(SettlementType.DVP);

		BigDecimal collateralValue = BigDecimal
				.valueOf(quantity.doubleValue() * contract.getTrade().getCollateral().getContractPrice().doubleValue() * 1.02);
		collateralValue = collateralValue.setScale(2, java.math.RoundingMode.HALF_UP);
		proposal.setCollateralValue(collateralValue.doubleValue());

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
		System.out.println("Contract Returns Menu");
		System.out.println("-----------------------");
		System.out.println("L                   - List all returns");
		System.out.println("S <Return ID>       - Load return by Id");
		System.out.println("R <Quantity>        - Notify return");
		System.out.println("X                   - Go back");
	}

}
