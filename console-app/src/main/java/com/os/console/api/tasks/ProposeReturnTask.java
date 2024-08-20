package com.os.console.api.tasks;

import org.springframework.web.reactive.function.client.WebClient;

import com.os.client.model.Loan;
import com.os.client.model.Party;
import com.os.client.model.PartyRole;
import com.os.client.model.ReturnProposal;
import com.os.client.model.TransactingParties;
import com.os.client.model.TransactingParty;
import com.os.console.util.RESTUtil;

public class ProposeReturnTask implements Runnable {

	private WebClient webClient;
	private Loan loan;
	private ReturnProposal returnProposal;
	private Party actingParty;

	public ProposeReturnTask(WebClient webClient, Loan loan, ReturnProposal returnProposal, Party actingParty) {
		this.webClient = webClient;
		this.loan = loan;
		this.returnProposal = returnProposal;
		this.actingParty = actingParty;
	}

	@Override
	public void run() {

		TransactingParties parties = loan.getTrade().getTransactingParties();
		boolean canAct = false;
		for (TransactingParty transactingParty : parties) {
			if (PartyRole.BORROWER.equals(transactingParty.getPartyRole())
					&& actingParty.equals(transactingParty.getParty())) {
				canAct = true;
				break;
			}
		}
		
		if (!canAct) {
			System.out.println("Not the borrowing party");			
			return;
		}

		RESTUtil.postRequest(webClient, "/loans/" + loan.getLoanId() + "/returns", returnProposal);

	}
}
