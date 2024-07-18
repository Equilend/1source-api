package com.os.console.api.tasks;

import org.springframework.web.reactive.function.client.WebClient;

import com.os.client.model.Contract;
import com.os.client.model.ContractProposalApproval;
import com.os.client.model.TransactingParties;
import com.os.client.model.TransactingParty;
import com.os.console.api.ConsoleConfig;
import com.os.console.util.RESTUtil;

public class ApproveContractTask implements Runnable {

	private WebClient webClient;
	private Contract contract;
	private ContractProposalApproval contractProposalApproval;

	public ApproveContractTask(WebClient webClient, Contract contract, ContractProposalApproval contractProposalApproval) {
		this.webClient = webClient;
		this.contract = contract;
		this.contractProposalApproval = contractProposalApproval;
	}

	@Override
	public void run() {

		TransactingParties parties = contract.getTrade().getTransactingParties();
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

		RESTUtil.postRequest(webClient, "/contracts/" + contract.getContractId() + "/approve", contractProposalApproval);
		
	}
}
