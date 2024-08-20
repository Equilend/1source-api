package com.os.console.api.tasks;

import org.springframework.web.reactive.function.client.WebClient;

import com.os.client.model.Loan;
import com.os.client.model.Party;
import com.os.client.model.SettlementStatus;
import com.os.client.model.SettlementStatusUpdate;
import com.os.client.model.TransactingParties;
import com.os.client.model.TransactingParty;
import com.os.console.util.RESTUtil;

public class UpdateLoanSettlementStatusTask implements Runnable {

	private WebClient webClient;
	private Loan loan;
	private Party actingParty;

	public UpdateLoanSettlementStatusTask(WebClient webClient, Loan loan, Party actingParty) {
		this.webClient = webClient;
		this.loan = loan;
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

		SettlementStatusUpdate settlementStatusUpdate = new SettlementStatusUpdate();

		settlementStatusUpdate.setSettlementStatus(SettlementStatus.SETTLED);

		RESTUtil.patchRequest(webClient, "/loans/" + loan.getLoanId(), settlementStatusUpdate);

	}
}
