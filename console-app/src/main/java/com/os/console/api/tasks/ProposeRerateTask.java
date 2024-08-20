package com.os.console.api.tasks;

import org.springframework.web.reactive.function.client.WebClient;

import com.os.client.model.Loan;
import com.os.client.model.Party;
import com.os.client.model.RerateProposal;
import com.os.client.model.TransactingParties;
import com.os.client.model.TransactingParty;
import com.os.console.util.RESTUtil;

public class ProposeRerateTask implements Runnable {

	private WebClient webClient;
	private Loan loan;
	private RerateProposal rerateProposal;
	private Party actingParty;

	public ProposeRerateTask(WebClient webClient, Loan loan, RerateProposal rerateProposal, Party actingParty) {
		this.webClient = webClient;
		this.loan = loan;
		this.rerateProposal = rerateProposal;
		this.actingParty = actingParty;
	}

	@Override
	public void run() {

		TransactingParties parties = loan.getTrade().getTransactingParties();
		boolean canAct = false;
		for (TransactingParty transactingParty : parties) {
			if (actingParty.equals(transactingParty.getParty())) {
				canAct = true;
				break;
			}
		}
		
		if (!canAct) {
			System.out.println("Not a transacting party");			
			return;
		}

		RESTUtil.postRequest(webClient, "/loans/" + loan.getLoanId() + "/rerates", rerateProposal);

	}
}
