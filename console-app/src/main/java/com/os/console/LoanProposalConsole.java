package com.os.console;

import java.io.BufferedReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.WebClient;

import com.os.client.model.Instrument;
import com.os.client.model.LoanProposal;
import com.os.client.model.Party;
import com.os.client.model.PartyRole;
import com.os.console.api.tasks.ProposeLoanTask;
import com.os.console.util.ConsoleOutputUtil;
import com.os.console.util.InstrumentUtil;
import com.os.console.util.PayloadUtil;

public class LoanProposalConsole extends AbstractConsole {

	private static final Logger logger = LoggerFactory.getLogger(LoanProposalConsole.class);

	private LoanProposal loanProposal;

	private Party borrowerParty;
	private Party lenderParty;
	private PartyRole proposingPartyRole;

	private boolean firstPrompt = true;

	public LoanProposalConsole(Party borrowerParty, Party lenderParty, PartyRole proposingPartyRole) {

		this.borrowerParty = borrowerParty;
		this.lenderParty = lenderParty;
		this.proposingPartyRole = proposingPartyRole;
	}

	protected boolean prompt() {

		if (firstPrompt) {
			loanProposal = PayloadUtil.createLoanProposal(borrowerParty, lenderParty, proposingPartyRole,
					InstrumentUtil.getInstance().getRandomInstrument());
			ConsoleOutputUtil.printObject(loanProposal);
			firstPrompt = false;
		}
		System.out.print("/loans/ proposal > ");
		return true;
	}

	public void handleArgs(String args[], BufferedReader consoleIn, WebClient webClient) {

		if (args[0].equals("R")) {

			loanProposal = PayloadUtil.createLoanProposal(borrowerParty, lenderParty, proposingPartyRole,
					InstrumentUtil.getInstance().getRandomInstrument());

			ConsoleOutputUtil.printObject(loanProposal);

		} else if (args[0].equals("G")) {
			if (args.length != 2 || args[1].length() == 0 || args[1].length() > 50) {
				System.out.println("Invalid security ID");
			} else {
				String securityId = args[1];
				System.out.print("Looking up " + securityId + "...");
				Instrument instrument = InstrumentUtil.getInstance().getInstrument(securityId);

				if (instrument == null) {
					System.out.println("not found");
				} else {
					loanProposal = PayloadUtil.createLoanProposal(borrowerParty, lenderParty, proposingPartyRole,
							instrument);
					ConsoleOutputUtil.printObject(loanProposal);
				}
			}
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
		System.out.println("S               - Submit loan proposal");
		System.out.println("R               - Generate random loan");
		System.out.println("G <Security Id> - Generate loan with Security Id");
		System.out.println();
		System.out.println("X               - Go back");
	}

}
