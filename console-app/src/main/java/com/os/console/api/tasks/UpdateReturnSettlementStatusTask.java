package com.os.console.api.tasks;

import org.springframework.web.reactive.function.client.WebClient;

import com.os.client.model.Contract;
import com.os.client.model.ModelReturn;
import com.os.client.model.Party;
import com.os.client.model.SettlementStatus;
import com.os.client.model.SettlementStatusUpdate;
import com.os.client.model.TransactingParties;
import com.os.client.model.TransactingParty;
import com.os.console.util.RESTUtil;

public class UpdateReturnSettlementStatusTask implements Runnable {

	private WebClient webClient;
	private Contract contract;
	private ModelReturn modelReturn;
	private Party actingParty;

	public UpdateReturnSettlementStatusTask(WebClient webClient, Contract contract, ModelReturn modelReturn, Party actingParty) {
		this.webClient = webClient;
		this.contract = contract;
		this.modelReturn = modelReturn;
		this.actingParty = actingParty;
	}

	@Override
	public void run() {

		TransactingParties parties = contract.getTrade().getTransactingParties();
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

		RESTUtil.patchRequest(webClient, "/contracts/" + contract.getContractId() + "/returns/" + modelReturn.getReturnId(), settlementStatusUpdate);

	}
}
