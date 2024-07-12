package com.os.console.util;

import java.util.UUID;

import com.os.client.model.ContractProposalApproval;
import com.os.client.model.PartyRole;
import com.os.client.model.PartySettlementInstruction;
import com.os.client.model.RoundingMode;
import com.os.client.model.SettlementInstruction;
import com.os.client.model.SettlementStatus;
import com.os.console.api.ConsoleConfig;

public class PayloadUtil {

	public static ContractProposalApproval createContractProposalApproval(ConsoleConfig consoleConfig) {

		ContractProposalApproval proposalApproval = new ContractProposalApproval();

		proposalApproval.setInternalRefId(UUID.randomUUID().toString());

		if (PartyRole.LENDER.equals(ConsoleConfig.ACTING_AS)) {
			proposalApproval.setRoundingRule(10d);
			proposalApproval.setRoundingMode(RoundingMode.ALWAYSUP);
		}

		PartySettlementInstruction partySettlementInstruction = new PartySettlementInstruction();
		partySettlementInstruction.setPartyRole(ConsoleConfig.ACTING_AS);
		partySettlementInstruction.setSettlementStatus(SettlementStatus.NONE);
		partySettlementInstruction.setInternalAcctCd(consoleConfig.getSettlement_internalAcctCd());

		SettlementInstruction instruction = new SettlementInstruction();
		partySettlementInstruction.setInstruction(instruction);
		instruction.setSettlementBic(consoleConfig.getSettlement_settlementBic());
		instruction.setLocalAgentBic(consoleConfig.getSettlement_localAgentBic());
		instruction.setLocalAgentName(consoleConfig.getSettlement_localAgentName());
		instruction.setLocalAgentAcct(consoleConfig.getSettlement_localAgentAcct());
		instruction.setCustodianBic(consoleConfig.getSettlement_custodianBic());
		instruction.setCustodianName(consoleConfig.getSettlement_custodianName());
		instruction.setCustodianAcct(consoleConfig.getSettlement_custodianAcct());
		instruction.setDtcParticipantNumber(consoleConfig.getSettlement_dtcParticipantNumber());

		proposalApproval.setSettlement(partySettlementInstruction);

		return proposalApproval;
	}

}
