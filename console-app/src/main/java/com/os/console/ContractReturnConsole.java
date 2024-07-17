package com.os.console;

import java.io.BufferedReader;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.WebClient;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.os.client.model.AcknowledgementType;
import com.os.client.model.Contract;
import com.os.client.model.ModelReturn;
import com.os.console.api.ConsoleConfig;
import com.os.console.api.LocalDateTypeGsonAdapter;
import com.os.console.api.OffsetDateTimeTypeGsonAdapter;
import com.os.console.api.tasks.AcknowledgeReturnTask;
import com.os.console.api.tasks.SearchContractReturnTask;
import com.os.console.api.tasks.UpdateReturnSettlementStatusTask;
import com.os.console.util.PayloadUtil;

public class ContractReturnConsole extends AbstractConsole {

	private static final Logger logger = LoggerFactory.getLogger(ContractReturnConsole.class);

	Contract contract;
	ModelReturn modelReturn;

	protected void prompt() {
		System.out.print("/contracts/" + contract.getContractId() + "/returns/" + modelReturn.getReturnId() + " > ");
	}

	public void execute(BufferedReader consoleIn, WebClient webClient, Contract origContract,
			ModelReturn origModelReturn) {

		this.contract = origContract;
		this.modelReturn = origModelReturn;

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
					if (args[0].equals("J")) {

						Gson gson = new GsonBuilder().setPrettyPrinting()
								.registerTypeAdapter(LocalDate.class, new LocalDateTypeGsonAdapter())
								.registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeTypeGsonAdapter())
								.create();

						System.out.println(gson.toJson(modelReturn));
						System.out.println();

					} else if (args[0].equals("P")) {
						String message = null;
						if (args.length == 2) {
							message = args[1];
							if (message.length() > 100) {
								System.out.println("Invalid acknowledgement message");
								message = null;
							}
						}
						try {
							System.out.print("Acknowledging return " + modelReturn.getReturnId() + "...");
							AcknowledgeReturnTask acknowledgeReturnTask = new AcknowledgeReturnTask(webClient,
									modelReturn,
									PayloadUtil.createReturnAcknowledgement(AcknowledgementType.POSITIVE, message));
							Thread taskT = new Thread(acknowledgeReturnTask);
							taskT.run();
							try {
								taskT.join();
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						} catch (Exception u) {
							System.out.println("Invalid acknowledgement message");
						}
					} else if (args[0].equals("N")) {
						String message = null;
						if (args.length == 2) {
							message = args[1];
							if (message.length() > 100) {
								System.out.println("Invalid acknowledgement message");
								message = null;
							}
						}
						try {
							System.out.print("Acknowledging return " + modelReturn.getReturnId() + "...");
							AcknowledgeReturnTask acknowledgeReturnTask = new AcknowledgeReturnTask(webClient,
									modelReturn,
									PayloadUtil.createReturnAcknowledgement(AcknowledgementType.NEGATIVE, message));
							Thread taskT = new Thread(acknowledgeReturnTask);
							taskT.run();
							try {
								taskT.join();
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						} catch (Exception u) {
							System.out.println("Invalid acknowledgement message");
						}
					} else if (args[0].equals("U")) {
						System.out.print("Updating settlement status to SETTLED...");
						UpdateReturnSettlementStatusTask updateReturnSettlementStatusTask = new UpdateReturnSettlementStatusTask(
								webClient, contract, modelReturn, ConsoleConfig.ACTING_PARTY);
						Thread taskT = new Thread(updateReturnSettlementStatusTask);
						taskT.run();
						try {
							taskT.join();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						refreshModelReturn(webClient);
					} else {
						System.out.println("Unknown command");
					}
				}
				prompt();
			}
		} catch (Exception e) {
			logger.error("Exception with returns command: " + input);
			e.printStackTrace();
		}

	}

	private void refreshModelReturn(WebClient webClient) {

		System.out.print("Refreshing return " + modelReturn.getReturnId() + "...");
		SearchContractReturnTask searchContractReturnTask = new SearchContractReturnTask(webClient, contract, modelReturn.getReturnId());
		Thread taskT = new Thread(searchContractReturnTask);
		taskT.run();
		try {
			taskT.join();
		} catch (InterruptedException e) {
			logger.error(e.getMessage(), e);
		}
		modelReturn = searchContractReturnTask.getReturn();
	}

	protected void printMenu() {
		System.out.println("Return Menu");
		System.out.println("-----------------------");
		System.out.println("J             - Print JSON");
		System.out.println();
		System.out.println("P <Message>   - Positively acknowledge with optional message");
		System.out.println("N <Message>   - Negatively acknowledge with optional message");
		System.out.println();
		System.out.println("U             - Update settlement status to SETTLED");
		System.out.println();
		System.out.println("X             - Go back");
	}

}
