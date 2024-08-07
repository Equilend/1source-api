package com.os.console;

import java.io.BufferedReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.WebClient;

import com.os.client.model.ContractProposal;
import com.os.client.model.Party;
import com.os.client.model.PartyRole;
import com.os.console.api.tasks.ProposeContractTask;
import com.os.console.util.ConsoleOutputUtil;
import com.os.console.util.PayloadUtil;

public class ContractProposalConsole extends AbstractConsole {

	private static final Logger logger = LoggerFactory.getLogger(ContractProposalConsole.class);

	private ContractProposal contractProposal;

	private Party borrowerParty;
	private Party lenderParty;
	private PartyRole proposingPartyRole;

	public ContractProposalConsole(Party borrowerParty, Party lenderParty, PartyRole proposingPartyRole) {

		this.borrowerParty = borrowerParty;
		this.lenderParty = lenderParty;
		this.proposingPartyRole = proposingPartyRole;
	}

	protected boolean prompt() {
		System.out.print("/contracts/ proposal > ");
		return true;
	}

	public void handleArgs(String args[], BufferedReader consoleIn, WebClient webClient) {

		contractProposal = PayloadUtil.createContractProposal(borrowerParty, lenderParty, proposingPartyRole);

		ConsoleOutputUtil.printObject(contractProposal);

		if (args[0].equals("R")) {

			contractProposal = PayloadUtil.createContractProposal(borrowerParty, lenderParty, proposingPartyRole);

			ConsoleOutputUtil.printObject(contractProposal);

		} else if (args[0].equals("S")) {

			try {
				System.out.print("Proposing contract...");
				ProposeContractTask proposeContractTask = new ProposeContractTask(webClient, contractProposal);
				Thread taskT = new Thread(proposeContractTask);
				taskT.run();
				try {
					taskT.join();
				} catch (InterruptedException e) {
					logger.error("Propose contract interrupted.", e);
					e.printStackTrace();
				}
			} catch (Exception u) {
				logger.error("Error proposing contract.", u);
				System.out.println("Error proposing contract");
			}

		} else {
			System.out.println("Unknown command");
		}
	}

	protected void printMenu() {
		System.out.println("Contract Proposal Menu");
		System.out.println("-----------------------");
		System.out.println("S             - Submit contract proposal");
		System.out.println("R             - Regenerate contract");
		System.out.println();
		System.out.println("X             - Go back");
	}

}
