package com.os.console.api.tasks;

import org.springframework.web.reactive.function.client.WebClient;

import com.os.client.model.Contract;
import com.os.client.model.Party;
import com.os.client.model.PartyRole;
import com.os.client.model.ReturnProposal;
import com.os.client.model.TransactingParties;
import com.os.client.model.TransactingParty;
import com.os.console.util.RESTUtil;

public class ProposeReturnTask implements Runnable {

	private WebClient webClient;
	private Contract contract;
	private ReturnProposal returnProposal;
	private Party actingParty;

	public ProposeReturnTask(WebClient webClient, Contract contract, ReturnProposal returnProposal, Party actingParty) {
		this.webClient = webClient;
		this.contract = contract;
		this.returnProposal = returnProposal;
		this.actingParty = actingParty;
	}

	@Override
	public void run() {

		TransactingParties parties = contract.getTrade().getTransactingParties();
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

		RESTUtil.postRequest(webClient, "/contracts/" + contract.getContractId() + "/returns", returnProposal);

	}
}
