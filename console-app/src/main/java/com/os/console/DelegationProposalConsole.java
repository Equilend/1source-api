package com.os.console;

import java.io.BufferedReader;
import org.springframework.web.reactive.function.client.WebClient;

import com.os.client.model.DelegationAuthorizationType;
import com.os.client.model.DelegationProposal;
import com.os.client.model.Party;
import com.os.console.api.ConsoleConfig;
import com.os.console.api.tasks.ProposeDelegationTask;
import com.os.console.util.ConsoleOutputUtil;
import com.os.console.util.PayloadUtil;

public class DelegationProposalConsole extends AbstractConsole {

	Party counterParty;

	public DelegationProposalConsole(Party counterParty) {
		this.counterParty = counterParty;
	}

	protected void prompt() {
		System.out.print("/delegations/ proposal:" + counterParty.getPartyId() + " > ");
	}

	public void handleArgs(String args[], BufferedReader consoleIn, WebClient webClient) {

		if (args[0].equals("L") || args[0].equals("R") || args[0].equals("E") || args[0].equals("T")) {

			if (args.length != 2 || args[1].length() > 100) {
				System.out.println("Invalid Venue Party Id");
			} else if (ConsoleConfig.ACTING_PARTY.getPartyId().equalsIgnoreCase(counterParty.getPartyId())) {
				System.out.println("You cannot propose a delegation to yourself");
			} else if (ConsoleConfig.ACTING_PARTY.getPartyId().equalsIgnoreCase(args[1])) {
				System.out.println("You cannot delegate to yourself");
			} else {

				String venuePartyId = args[1];
				Party venueParty = new Party();
				venueParty.setPartyId(venuePartyId);
				// venueParty.setPartyName("Venue " + venuePartyId);
				venueParty.setGleifLei("213800BN4DRR1ADYGP92");

				DelegationAuthorizationType authorizationType = (args[0].equals("L") ? DelegationAuthorizationType.LOANS
						: args[0].equals("R") ? DelegationAuthorizationType.RETURNS
								: args[0].equals("E") ? DelegationAuthorizationType.RECALLS
										: args[0].equals("T") ? DelegationAuthorizationType.RERATES : null);

				try {

					System.out.print(
							"Proposing " + authorizationType + " delegation to " + venueParty.getPartyId() + "...");

					DelegationProposal delegationProposal = PayloadUtil.createDelegationProposal(counterParty,
							venueParty, authorizationType);

					ConsoleOutputUtil.printObject(delegationProposal);

					ProposeDelegationTask proposeDelegationTask = new ProposeDelegationTask(webClient,
							delegationProposal);

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

	protected void printMenu() {
		System.out.println("Delegation Proposal Menu");
		System.out.println("-----------------------");
		System.out.println("L <Venue Party Id> - Delegate Loan processing to Venue Party Id");
		System.out.println("R <Venue Party Id> - Delegate Returns processing to Venue Party Id");
		System.out.println("E <Venue Party Id> - Delegate Recalls processing to Venue Party Id");
		System.out.println("T <Venue Party Id> - Delegate Rerates processing to Venue Party Id");
		System.out.println();
		System.out.println("X             - Go back");
	}

}
