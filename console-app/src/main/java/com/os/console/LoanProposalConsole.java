package com.os.console;

import java.io.BufferedReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.WebClient;

import com.os.client.model.LoanProposal;
import com.os.client.model.Party;
import com.os.client.model.PartyRole;
import com.os.console.api.tasks.ProposeLoanTask;
import com.os.console.util.ConsoleOutputUtil;
import com.os.console.util.PayloadUtil;

public class LoanProposalConsole extends AbstractConsole {

	private static final Logger logger = LoggerFactory.getLogger(LoanProposalConsole.class);

	private LoanProposal loanProposal;

	private Party borrowerParty;
	private Party lenderParty;
	private PartyRole proposingPartyRole;

	public LoanProposalConsole(Party borrowerParty, Party lenderParty, PartyRole proposingPartyRole) {

		this.borrowerParty = borrowerParty;
		this.lenderParty = lenderParty;
		this.proposingPartyRole = proposingPartyRole;
	}

	protected boolean prompt() {
		System.out.print("/loans/ proposal > ");
		return true;
	}

	public void handleArgs(String args[], BufferedReader consoleIn, WebClient webClient) {

		loanProposal = PayloadUtil.createLoanProposal(borrowerParty, lenderParty, proposingPartyRole);

		ConsoleOutputUtil.printObject(loanProposal);

		if (args[0].equals("R")) {

			loanProposal = PayloadUtil.createLoanProposal(borrowerParty, lenderParty, proposingPartyRole);

			ConsoleOutputUtil.printObject(loanProposal);

		} else if (args[0].equals("S")) {

			try {
				System.out.print("Proposing loan...");
				ProposeLoanTask proposeLoanTask = new ProposeLoanTask(webClient, loanProposal);
				Thread taskT = new Thread(proposeLoanTask);
				taskT.run();
				try {
					taskT.join();
				} catch (InterruptedException e) {
					logger.error("Propose loan interrupted.", e);
					e.printStackTrace();
				}
			} catch (Exception u) {
				logger.error("Error proposing loan.", u);
				System.out.println("Error proposing loan");
			}

		} else {
			System.out.println("Unknown command");
		}
	}

	protected void printMenu() {
		System.out.println("Loan Proposal Menu");
		System.out.println("-----------------------");
		System.out.println("S             - Submit loan proposal");
		System.out.println("R             - Regenerate loan");
		System.out.println();
		System.out.println("X             - Go back");
	}

}
