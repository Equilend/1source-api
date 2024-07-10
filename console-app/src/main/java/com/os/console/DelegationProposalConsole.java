package com.os.console;

import java.io.BufferedReader;
import java.time.LocalDate;
import java.time.OffsetDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.WebClient;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.os.client.model.DelegationAuthorization;
import com.os.client.model.DelegationAuthorizationType;
import com.os.client.model.DelegationProposal;
import com.os.client.model.Party;
import com.os.console.api.ConsoleConfig;
import com.os.console.api.LocalDateTypeGsonAdapter;
import com.os.console.api.OffsetDateTimeTypeGsonAdapter;
import com.os.console.api.tasks.ProposeDelegationTask;

public class DelegationProposalConsole {

	private static final Logger logger = LoggerFactory.getLogger(DelegationProposalConsole.class);

	public void execute(BufferedReader consoleIn, ConsoleConfig consoleConfig, WebClient webClient,
			Party counterParty) {

		Gson gson = new GsonBuilder().setPrettyPrinting()
				.registerTypeAdapter(LocalDate.class, new LocalDateTypeGsonAdapter())
				.registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeTypeGsonAdapter()).create();

		String command = null;
		System.out.print("/delegations/ proposal:" + counterParty.getPartyId() + " > ");

		try {
			while ((command = consoleIn.readLine()) != null) {
				command = command.trim();
				if (command.equals("?") || command.equalsIgnoreCase("HELP")) {
					printMainDelegationHelp();
				} else if (command.equalsIgnoreCase("QUIT") || command.equalsIgnoreCase("EXIT")
						|| command.equalsIgnoreCase("Q")) {
					System.exit(0);
				} else if (command.equalsIgnoreCase("X")) {
					break;
				} else if (command.toUpperCase().startsWith("L ") || command.toUpperCase().startsWith("R ")
						|| command.toUpperCase().startsWith("E ") || command.toUpperCase().startsWith("T ")) {

					if (command.length() > 100) {
						System.out.println("Invalid Venue Party Id");
					} else if (consoleConfig.getAuth_party().equalsIgnoreCase(counterParty.getPartyId())) {
						System.out.println("You cannot propose a delegation to yourself");
					} else if (consoleConfig.getAuth_party().equalsIgnoreCase(command)) {
						System.out.println("You cannot delegate to yourself");
					} else {

						String venuePartyId = command.substring(2).toUpperCase();
						Party venueParty = new Party();
						venueParty.setPartyId(venuePartyId);
						//venueParty.setPartyName("Venue " + venuePartyId);
						venueParty.setGleifLei("213800BN4DRR1ADYGP92");

						DelegationAuthorizationType authorizationType = (command.toUpperCase().startsWith("L ")
								? DelegationAuthorizationType.LOANS
								: command.toUpperCase().startsWith("R ") ? DelegationAuthorizationType.RETURNS
										: command.toUpperCase().startsWith("E ") ? DelegationAuthorizationType.RECALLS
												: command.toUpperCase().startsWith("T ")
														? DelegationAuthorizationType.RERATES
														: null);

						try {
							DelegationProposal delegationProposal = createDelegationProposal(counterParty, venueParty,
									authorizationType);

							System.out.println(gson.toJson(delegationProposal));
							System.out.println();

							ProposeDelegationTask proposeDelegationTask = new ProposeDelegationTask(webClient,
									delegationProposal);

							System.out.print("Proposing " + authorizationType + " delegation to "
									+ venueParty.getPartyId() + "...");

							Thread taskT = new Thread(proposeDelegationTask);
							taskT.run();
							try {
								taskT.join();
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						} catch (Exception u) {
							System.out.println("Error proposing delegation");
						}
					}
				} else {
					System.out.println("Unknown command");
				}

				System.out.print("/delegations/ proposal:" + counterParty.getPartyId() + " > ");
			}
		} catch (Exception e) {
			logger.error("Exception with returns command: " + command);
			e.printStackTrace();
		}

	}

	public DelegationProposal createDelegationProposal(Party counterParty, Party venueParty,
			DelegationAuthorizationType authorizationType) {

		DelegationProposal delegationProposal = new DelegationProposal();

		DelegationAuthorization authorization = new DelegationAuthorization();
		authorization.setAuthorizationType(authorizationType);

		delegationProposal.setAuthorization(authorization);
		delegationProposal.setDelegationParty(venueParty);
		delegationProposal.setCounterparty(counterParty);

		return delegationProposal;
	}

	private void printMainDelegationHelp() {
		System.out.println();
		System.out.println("Delegation Proposal Menu");
		System.out.println("-----------------------");
		System.out.println("L <Venue Party Id> - Delegate Loan processing to Venue Party Id");
		System.out.println("R <Venue Party Id> - Delegate Returns processing to Venue Party Id");
		System.out.println("E <Venue Party Id> - Delegate Recalls processing to Venue Party Id");
		System.out.println("T <Venue Party Id> - Delegate Rerates processing to Venue Party Id");
		System.out.println("X             - Go back");
		System.out.println();
	}

}
