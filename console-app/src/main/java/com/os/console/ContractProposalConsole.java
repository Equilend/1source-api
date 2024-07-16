package com.os.console;

import java.io.BufferedReader;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.WebClient;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.os.client.model.ContractProposal;
import com.os.client.model.Party;
import com.os.client.model.PartyRole;
import com.os.console.api.ConsoleConfig;
import com.os.console.api.LocalDateTypeGsonAdapter;
import com.os.console.api.OffsetDateTimeTypeGsonAdapter;
import com.os.console.api.tasks.ProposeContractTask;
import com.os.console.util.PayloadUtil;

public class ContractProposalConsole extends AbstractConsole {

	private static final Logger logger = LoggerFactory.getLogger(ContractProposalConsole.class);

	private ContractProposal contractProposal;

	protected void prompt() {
		System.out.print("/contracts/ proposal > ");
	}
	
	public void execute(BufferedReader consoleIn, ConsoleConfig consoleConfig, WebClient webClient, Party borrowerParty,
			Party lenderParty, PartyRole proposingPartyRole) {

		Gson gson = new GsonBuilder().setPrettyPrinting()
				.registerTypeAdapter(LocalDate.class, new LocalDateTypeGsonAdapter())
				.registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeTypeGsonAdapter()).create();

		contractProposal = PayloadUtil.createContractProposal(consoleConfig, borrowerParty, lenderParty, proposingPartyRole);

		System.out.println(gson.toJson(contractProposal));
		System.out.println();

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
					if (command.equals("R")) {

						contractProposal = PayloadUtil.createContractProposal(consoleConfig, borrowerParty, lenderParty,
								proposingPartyRole);

						System.out.println(gson.toJson(contractProposal));
						System.out.println();

					} else if (command.equals("S")) {

						try {
							System.out.print("Proposing contract...");
							ProposeContractTask proposeContractTask = new ProposeContractTask(webClient,
									contractProposal);
							Thread taskT = new Thread(proposeContractTask);
							taskT.run();
							try {
								taskT.join();
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						} catch (Exception u) {
							System.out.println("Error proposing contract");
						}

					} else {
						System.out.println("Unknown command");
					}
				}
				
				prompt();
			}
		} catch (Exception e) {
			logger.error("Exception with returns command: " + command);
			e.printStackTrace();
		}

	}

	protected void printMenu() {
		System.out.println("Contract Proposal Menu");
		System.out.println("-----------------------");
		System.out.println("S             - Submit contract proposal");
		System.out.println("R             - Regenerate contract");
		System.out.println("X             - Go back");
	}

}
