package com.os.console;

import java.io.BufferedReader;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.WebClient;

import com.os.client.model.Contract;
import com.os.client.model.FeeRate;
import com.os.client.model.FixedRate;
import com.os.client.model.FloatingRate;
import com.os.client.model.RebateRate;
import com.os.client.model.RerateProposal;
import com.os.console.api.ConsoleConfig;
import com.os.console.api.tasks.ProposeRerateTask;
import com.os.console.api.tasks.SearchContractRerateTask;
import com.os.console.api.tasks.SearchContractReratesTask;

public class ContractReratesConsole extends AbstractConsole {

	private static final Logger logger = LoggerFactory.getLogger(ContractReratesConsole.class);

	Contract contract;

	protected void prompt() {
		System.out.print("/contracts/" + contract.getContractId() + "/rerates > ");
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
						System.out.print("Listing all rerates...");
						SearchContractReratesTask searchContractReratesTask = new SearchContractReratesTask(webClient,
								contract);
						Thread taskT = new Thread(searchContractReratesTask);
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
							String rerateId = command.substring(2).toLowerCase();
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
					} else if (command.startsWith("P ")) {
						if (command.length() > 15) {
							System.out.println("Invalid spread/fee");
						} else {
							Double rerate = Double.valueOf(command.substring(2));
							try {
								System.out.print("Proposing rerate...");
								ProposeRerateTask proposeRerateTask = new ProposeRerateTask(webClient,
										contract.getContractId(), createRerateProposal(consoleConfig, rerate));
								Thread taskT = new Thread(proposeRerateTask);
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
			logger.error("Exception with contract rerates command: " + command);
			e.printStackTrace();
		}

	}

	private RerateProposal createRerateProposal(ConsoleConfig consoleConfig, Double rerate) {

		RerateProposal proposal = new RerateProposal();

		LocalDate rerateDate = LocalDate.now(ZoneId.of("UTC"));
		
		if (contract.getTrade().getRate() instanceof FeeRate) {
			
			((FeeRate) contract.getTrade().getRate()).getFee().setBaseRate(rerate);
			((FeeRate) contract.getTrade().getRate()).getFee().setEffectiveDate(rerateDate);

			proposal.setRate(((FeeRate) contract.getTrade().getRate()));

		} else if (contract.getTrade().getRate() instanceof RebateRate) {
			
			if (((RebateRate) contract.getTrade().getRate()).getRebate() instanceof FixedRate) {
			
				((FixedRate)((RebateRate) contract.getTrade().getRate()).getRebate()).getFixed().setBaseRate(rerate);
				((FixedRate)((RebateRate) contract.getTrade().getRate()).getRebate()).getFixed().setEffectiveDate(rerateDate);
				
				proposal.setRate(((RebateRate) contract.getTrade().getRate()));

			} else if (((RebateRate) contract.getTrade().getRate()).getRebate() instanceof FloatingRate) {
				
				((FloatingRate)((RebateRate) contract.getTrade().getRate()).getRebate()).getFloating().setSpread(rerate);
				((FloatingRate)((RebateRate) contract.getTrade().getRate()).getRebate()).getFloating().setEffectiveDate(rerateDate);

				proposal.setRate(((RebateRate) contract.getTrade().getRate()));
			}
		}

		return proposal;
	}

	protected void printMenu() {
		System.out.println("Contract Rerates Menu");
		System.out.println("-----------------------");
		System.out.println("L                   - List all rerates");
		System.out.println("S <Recall ID>       - Load rerate by Id");
		System.out.println("P <Spread/Fee>      - Propose rerate");
		System.out.println("X                   - Go back");
	}

}
