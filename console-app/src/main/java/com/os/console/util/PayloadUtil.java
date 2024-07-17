package com.os.console.util;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Random;
import java.util.UUID;

import com.os.client.model.AcknowledgementType;
import com.os.client.model.BenchmarkCd;
import com.os.client.model.Collateral;
import com.os.client.model.CollateralType;
import com.os.client.model.Contract;
import com.os.client.model.ContractProposal;
import com.os.client.model.ContractProposalApproval;
import com.os.client.model.CurrencyCd;
import com.os.client.model.DelegationAuthorization;
import com.os.client.model.DelegationAuthorizationType;
import com.os.client.model.DelegationProposal;
import com.os.client.model.FeeRate;
import com.os.client.model.FixedRate;
import com.os.client.model.FloatingRate;
import com.os.client.model.FloatingRateDef;
import com.os.client.model.Instrument;
import com.os.client.model.InternalReference;
import com.os.client.model.Party;
import com.os.client.model.PartyRole;
import com.os.client.model.PartySettlementInstruction;
import com.os.client.model.RebateRate;
import com.os.client.model.RecallProposal;
import com.os.client.model.RerateProposal;
import com.os.client.model.ReturnAcknowledgement;
import com.os.client.model.ReturnProposal;
import com.os.client.model.RoundingMode;
import com.os.client.model.SettlementInstruction;
import com.os.client.model.SettlementStatus;
import com.os.client.model.SettlementType;
import com.os.client.model.TermType;
import com.os.client.model.TradeAgreement;
import com.os.client.model.TransactingParties;
import com.os.client.model.TransactingParty;
import com.os.client.model.Venue;
import com.os.client.model.VenueType;
import com.os.client.model.Venues;
import com.os.console.api.ConsoleConfig;

public class PayloadUtil {

	public static ContractProposal createContractProposal(ConsoleConfig consoleConfig, Party borrowerParty, Party lenderParty,
			PartyRole proposingPartyRole) {

		Random random = new Random();

		ContractProposal contractProposal = new ContractProposal();

		TradeAgreement trade = new TradeAgreement();

		TransactingParties transactingParties = new TransactingParties();

		TransactingParty borrowerTransactingParty = new TransactingParty();
		borrowerTransactingParty.setPartyRole(PartyRole.BORROWER);
		borrowerTransactingParty.setParty(borrowerParty);
		transactingParties.add(borrowerTransactingParty);

		InternalReference borrowerInternalRef = new InternalReference();
		borrowerInternalRef.setAccountId(null);
		borrowerInternalRef.setBrokerCd(null);
		borrowerInternalRef
				.setInternalRefId(PartyRole.BORROWER.equals(proposingPartyRole) ? UUID.randomUUID().toString() : null);

		borrowerTransactingParty.setInternalRef(borrowerInternalRef);

		TransactingParty lenderTransactingParty = new TransactingParty();
		lenderTransactingParty.setPartyRole(PartyRole.LENDER);
		lenderTransactingParty.setParty(lenderParty);
		transactingParties.add(lenderTransactingParty);

		InternalReference lenderInternalRef = new InternalReference();
		lenderInternalRef.setAccountId(null);
		lenderInternalRef.setBrokerCd(null);
		lenderInternalRef
				.setInternalRefId(PartyRole.LENDER.equals(proposingPartyRole) ? UUID.randomUUID().toString() : null);

		lenderTransactingParty.setInternalRef(lenderInternalRef);

		trade.setTransactingParties(transactingParties);

		Venues venues = new Venues();

		Venue venue = new Venue();
		venue.setType(VenueType.OFFPLATFORM);
		venue.setVenueRefKey("CONSOLE" + System.currentTimeMillis());

		venues.add(venue);

		trade.setVenues(venues);

		Instrument instrument = InstrumentUtil.getRandomInstrument();

		trade.setInstrument(instrument);

		LocalDate tradeDate = LocalDate.now(ZoneId.of("UTC"));

		CollateralType collateralType = CollateralType.CASH;

		FloatingRateDef floatingRateDef = new FloatingRateDef();
		floatingRateDef.setSpread(Double.valueOf(random.nextInt((100 - 1) + 100)));
		floatingRateDef.setCutoffTime("18:00");
		floatingRateDef.setEffectiveDate(tradeDate);
		floatingRateDef.setBenchmark(BenchmarkCd.OBFR);
		floatingRateDef.setIsAutoRerate(true);

		FloatingRate floatingRate = new FloatingRate();
		floatingRate.setFloating(floatingRateDef);

		RebateRate rebateRate = new RebateRate();
		rebateRate.setRebate(floatingRate);

		trade.setRate(rebateRate);

		trade.setQuantity(((((random.nextInt(100000 - 10000) + 10000)) + 99) / 100) * 100);
		trade.setBillingCurrency(CurrencyCd.USD);
		trade.setDividendRatePct(85d);
		trade.setTradeDate(tradeDate);
		trade.setTermType(TermType.OPEN);
		trade.setTermDate(null);

		trade.setSettlementDate(tradeDate.plusDays(1));
		trade.setSettlementType(SettlementType.DVP);

		Collateral collateral = new Collateral();
		collateral.setContractPrice(trade.getInstrument().getPrice().getValue().doubleValue());

		BigDecimal contractValue = BigDecimal.valueOf(
				trade.getQuantity().doubleValue() * (trade.getInstrument().getPrice().getValue().doubleValue()));
		contractValue = contractValue.setScale(2, java.math.RoundingMode.HALF_UP);
		collateral.setContractValue(contractValue.doubleValue());

		BigDecimal collateralValue = BigDecimal
				.valueOf(trade.getQuantity().doubleValue() * collateral.getContractPrice().doubleValue() * 1.02);
		collateralValue = collateralValue.setScale(2, java.math.RoundingMode.HALF_UP);
		collateral.setCollateralValue(collateralValue.doubleValue());

		collateral.setCurrency(CurrencyCd.USD);
		collateral.setType(collateralType);
		collateral.setMargin(102d);

		// only add rounding rules if proposer is the lender
		if (PartyRole.LENDER.equals(proposingPartyRole)) {
			collateral.setRoundingRule(10);
			collateral.setRoundingMode(RoundingMode.ALWAYSUP);
		}

		trade.setCollateral(collateral);

		contractProposal.setTrade(trade);

		PartySettlementInstruction partySettlementInstruction = new PartySettlementInstruction();
		partySettlementInstruction.setPartyRole(proposingPartyRole);
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

		contractProposal.setSettlement(Collections.singletonList(partySettlementInstruction));

		return contractProposal;
	}

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

	public static ReturnProposal createReturnProposal(ConsoleConfig consoleConfig, Contract contract, Integer quantity) {

		ReturnProposal proposal = new ReturnProposal();

		Venue venue = new Venue();
		venue.setType(VenueType.OFFPLATFORM);
		venue.setVenueRefKey("CONSOLE" + System.currentTimeMillis());

		proposal.setExecutionVenue(venue);
		
		proposal.setQuantity(quantity);
		proposal.setReturnDate(LocalDate.now(ZoneId.of("UTC")));
		
		LocalDate returnSettlementDate = proposal.getReturnDate().plusDays(1);
		if (returnSettlementDate.getDayOfWeek().equals(DayOfWeek.SATURDAY)) {
			returnSettlementDate = returnSettlementDate.plusDays(2);
		} else if (returnSettlementDate.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
			returnSettlementDate = returnSettlementDate.plusDays(1);
		}
		proposal.setReturnSettlementDate(returnSettlementDate);
		proposal.setSettlementType(SettlementType.DVP);

		BigDecimal collateralValue = BigDecimal
				.valueOf(quantity.doubleValue() * contract.getTrade().getCollateral().getContractPrice().doubleValue() * 1.02);
		collateralValue = collateralValue.setScale(2, java.math.RoundingMode.HALF_UP);
		proposal.setCollateralValue(collateralValue.doubleValue());

//		PartySettlementInstruction partySettlementInstruction = new PartySettlementInstruction();
//		partySettlementInstruction.setPartyRole(ConsoleConfig.ACTING_AS);
//		partySettlementInstruction.setSettlementStatus(SettlementStatus.NONE);
//		partySettlementInstruction.setInternalAcctCd(consoleConfig.getSettlement_internalAcctCd());
//
//		SettlementInstruction instruction = new SettlementInstruction();
//		partySettlementInstruction.setInstruction(instruction);
//		instruction.setSettlementBic(consoleConfig.getSettlement_settlementBic());
//		instruction.setLocalAgentBic(consoleConfig.getSettlement_localAgentBic());
//		instruction.setLocalAgentName(consoleConfig.getSettlement_localAgentName());
//		instruction.setLocalAgentAcct(consoleConfig.getSettlement_localAgentAcct());
//		instruction.setCustodianBic(consoleConfig.getSettlement_custodianBic());
//		instruction.setCustodianName(consoleConfig.getSettlement_custodianName());
//		instruction.setCustodianAcct(consoleConfig.getSettlement_custodianAcct());
//		instruction.setDtcParticipantNumber(consoleConfig.getSettlement_dtcParticipantNumber());
//
//		proposal.setSettlement(partySettlementInstruction);

		return proposal;
	}

	public static RecallProposal createRecallProposal(ConsoleConfig consoleConfig, Integer quantity) {

		RecallProposal proposal = new RecallProposal();

		Venue venue = new Venue();
		venue.setType(VenueType.OFFPLATFORM);
		venue.setVenueRefKey("CONSOLE" + System.currentTimeMillis());

		proposal.setExecutionVenue(venue);

		proposal.setQuantity(quantity);
		proposal.setRecallDate(LocalDate.now(ZoneId.of("UTC")));
		
		LocalDate recallDueDate = proposal.getRecallDate().plusDays(3);
		if (recallDueDate.getDayOfWeek().equals(DayOfWeek.SATURDAY)) {
			recallDueDate = recallDueDate.plusDays(2);
		} else if (recallDueDate.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
			recallDueDate = recallDueDate.plusDays(1);
		}
		proposal.setRecallDueDate(recallDueDate);

//		PartySettlementInstruction partySettlementInstruction = new PartySettlementInstruction();
//		partySettlementInstruction.setPartyRole(ConsoleConfig.ACTING_AS);
//		partySettlementInstruction.setSettlementStatus(SettlementStatus.NONE);
//		partySettlementInstruction.setInternalAcctCd(consoleConfig.getSettlement_internalAcctCd());
//
//		SettlementInstruction instruction = new SettlementInstruction();
//		partySettlementInstruction.setInstruction(instruction);
//		instruction.setSettlementBic(consoleConfig.getSettlement_settlementBic());
//		instruction.setLocalAgentBic(consoleConfig.getSettlement_localAgentBic());
//		instruction.setLocalAgentName(consoleConfig.getSettlement_localAgentName());
//		instruction.setLocalAgentAcct(consoleConfig.getSettlement_localAgentAcct());
//		instruction.setCustodianBic(consoleConfig.getSettlement_custodianBic());
//		instruction.setCustodianName(consoleConfig.getSettlement_custodianName());
//		instruction.setCustodianAcct(consoleConfig.getSettlement_custodianAcct());
//		instruction.setDtcParticipantNumber(consoleConfig.getSettlement_dtcParticipantNumber());
//
//		proposal.setSettlement(partySettlementInstruction);

		return proposal;
	}

	public static RerateProposal createRerateProposal(ConsoleConfig consoleConfig, Contract contract, Double rerate) {

		RerateProposal proposal = new RerateProposal();

		Venue venue = new Venue();
		venue.setType(VenueType.OFFPLATFORM);
		venue.setVenueRefKey("CONSOLE" + System.currentTimeMillis());

		proposal.setExecutionVenue(venue);

		LocalDate rerateDate = LocalDate.now(ZoneId.of("UTC"));
		
		if (contract.getTrade().getRate() instanceof FeeRate) {
			
			((FeeRate) contract.getTrade().getRate()).getFee().setBaseRate(rerate);
			((FeeRate) contract.getTrade().getRate()).getFee().setEffectiveDate(rerateDate);

			proposal.setRate(((FeeRate) contract.getTrade().getRate()));

		} else if (contract.getTrade().getRate() instanceof RebateRate) {
			
			if (((RebateRate) contract.getTrade().getRate()).getRebate() instanceof FixedRate) {
			
				((FixedRate)((RebateRate) contract.getTrade().getRate()).getRebate()).getFixed().setBaseRate(rerate);
				((FixedRate)((RebateRate) contract.getTrade().getRate()).getRebate()).getFixed().setEffectiveDate(rerateDate);
				
				proposal.setRate(((RebateRate) contract.getTrade().getRate()));

			} else if (((RebateRate) contract.getTrade().getRate()).getRebate() instanceof FloatingRate) {
				
				((FloatingRate)((RebateRate) contract.getTrade().getRate()).getRebate()).getFloating().setSpread(rerate);
				((FloatingRate)((RebateRate) contract.getTrade().getRate()).getRebate()).getFloating().setEffectiveDate(rerateDate);

				proposal.setRate(((RebateRate) contract.getTrade().getRate()));
			}
		}

		return proposal;
	}

	public static DelegationProposal createDelegationProposal(Party counterParty, Party venueParty,
			DelegationAuthorizationType authorizationType) {

		DelegationProposal delegationProposal = new DelegationProposal();

		DelegationAuthorization authorization = new DelegationAuthorization();
		authorization.setAuthorizationType(authorizationType);

		delegationProposal.setAuthorization(authorization);
		delegationProposal.setDelegationParty(venueParty);
		delegationProposal.setCounterparty(counterParty);

		return delegationProposal;
	}

	public static ReturnAcknowledgement createReturnAcknowledgement(AcknowledgementType acknowledgementType, String message) {
		
		ReturnAcknowledgement returnAcknowledgement = new ReturnAcknowledgement();
		
		returnAcknowledgement.setAcknowledgementType(acknowledgementType);
		returnAcknowledgement.setDescription(message);
		
		return returnAcknowledgement;
	}
}
