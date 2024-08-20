package com.os.console.api.tasks;

import org.springframework.web.reactive.function.client.WebClient;

import com.os.client.model.Loan;
import com.os.client.model.Party;
import com.os.client.model.PartyRole;
import com.os.client.model.RecallProposal;
import com.os.client.model.TransactingParties;
import com.os.client.model.TransactingParty;
import com.os.console.util.RESTUtil;

public class ProposeRecallTask implements Runnable {

	private WebClient webClient;
	private Loan loan;
	private RecallProposal recallProposal;
	private Party actingParty;

	public ProposeRecallTask(WebClient webClient, Loan loan, RecallProposal recallProposal, Party actingParty) {
		this.webClient = webClient;
		this.loan = loan;
		this.recallProposal = recallProposal;
		this.actingParty = actingParty;
	}

	@Override
	public void run() {

		TransactingParties parties = loan.getTrade().getTransactingParties();
		boolean canAct = false;
		for (TransactingParty transactingParty : parties) {
			if (PartyRole.LENDER.equals(transactingParty.getPartyRole())
					&& actingParty.equals(transactingParty.getParty())) {
				canAct = true;
				break;
			}
		}
		
		if (!canAct) {
			System.out.println("Not the lending party");			
			return;
		}

		RESTUtil.postRequest(webClient, "/loans/" + loan.getLoanId() + "/recalls", recallProposal);

	}
}
