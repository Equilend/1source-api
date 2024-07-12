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

public class DelegationProposalConsole extends AbstractConsole {

	private static final Logger logger = LoggerFactory.getLogger(DelegationProposalConsole.class);

	Party counterParty;

	protected void prompt() {
		System.out.print("/delegations/ proposal:" + counterParty.getPartyId() + " > ");
	}

	public void execute(BufferedReader consoleIn, ConsoleConfig consoleConfig, WebClient webClient,
			Party origCounterParty) {

		this.counterParty = origCounterParty;
		
		Gson gson = new GsonBuilder().setPrettyPrinting()
				.registerTypeAdapter(LocalDate.class, new LocalDateTypeGsonAdapter())
				.registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeTypeGsonAdapter()).create();

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
					if (command.startsWith("L ") || command.startsWith("R ") || command.startsWith("E ")
							|| command.startsWith("T ")) {

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
							// venueParty.setPartyName("Venue " + venuePartyId);
							venueParty.setGleifLei("213800BN4DRR1ADYGP92");

							DelegationAuthorizationType authorizationType = (command.startsWith("L ")
									? DelegationAuthorizationType.LOANS
									: command.startsWith("R ") ? DelegationAuthorizationType.RETURNS
											: command.startsWith("E ") ? DelegationAuthorizationType.RECALLS
													: command.startsWith("T ") ? DelegationAuthorizationType.RERATES
															: null);

							try {
								DelegationProposal delegationProposal = createDelegationProposal(counterParty,
										venueParty, authorizationType);

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
				}

				prompt();
			}
		} catch (Exception e) {
			logger.error("Exception with returns command: " + command);
			e.printStackTrace();
		}

	}

	private DelegationProposal createDelegationProposal(Party counterParty, Party venueParty,
			DelegationAuthorizationType authorizationType) {

		DelegationProposal delegationProposal = new DelegationProposal();

		DelegationAuthorization authorization = new DelegationAuthorization();
		authorization.setAuthorizationType(authorizationType);

		delegationProposal.setAuthorization(authorization);
		delegationProposal.setDelegationParty(venueParty);
		delegationProposal.setCounterparty(counterParty);

		return delegationProposal;
	}

	protected void printMenu() {
		System.out.println("Delegation Proposal Menu");
		System.out.println("-----------------------");
		System.out.println("L <Venue Party Id> - Delegate Loan processing to Venue Party Id");
		System.out.println("R <Venue Party Id> - Delegate Returns processing to Venue Party Id");
		System.out.println("E <Venue Party Id> - Delegate Recalls processing to Venue Party Id");
		System.out.println("T <Venue Party Id> - Delegate Rerates processing to Venue Party Id");
		System.out.println("X             - Go back");
	}

}
