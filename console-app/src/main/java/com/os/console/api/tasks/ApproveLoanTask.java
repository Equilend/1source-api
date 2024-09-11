package com.os.console.api.tasks;

import org.springframework.web.reactive.function.client.WebClient;

import com.os.client.model.Loan;
import com.os.client.model.LoanProposalApproval;
import com.os.client.model.TransactingParties;
import com.os.client.model.TransactingParty;
import com.os.console.api.ConsoleConfig;
import com.os.console.util.RESTUtil;

public class ApproveLoanTask implements Runnable {

	private WebClient webClient;
	private Loan loan;
	private LoanProposalApproval loanProposalApproval;

	public ApproveLoanTask(WebClient webClient, Loan loan, LoanProposalApproval loanProposalApproval) {
		this.webClient = webClient;
		this.loan = loan;
		this.loanProposalApproval = loanProposalApproval;
	}

	@Override
	public void run() {

		TransactingParties parties = loan.getTrade().getTransactingParties();
		boolean canAct = false;
		for (TransactingParty transactingParty : parties) {
			if (ConsoleConfig.ACTING_PARTY.equals(transactingParty.getParty())) {
				canAct = true;
				break;
			}
		}
		
		if (!canAct) {
			System.out.println("Not a transacting party");			
			return;
		}

		RESTUtil.postRequest(webClient, "/loans/" + loan.getLoanId() + "/approve", loanProposalApproval);
		
	}
}
