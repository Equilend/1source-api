package com.os.replay.util;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.os.client.model.BenchmarkCd;
import com.os.client.model.Collateral;
import com.os.client.model.CollateralType;
import com.os.client.model.CurrencyCd;
import com.os.client.model.FloatingRate;
import com.os.client.model.FloatingRateDef;
import com.os.client.model.Instrument;
import com.os.client.model.InternalReference;
import com.os.client.model.Loan;
import com.os.client.model.LoanProposal;
import com.os.client.model.Party;
import com.os.client.model.PartyRole;
import com.os.client.model.PartySettlementInstruction;
import com.os.client.model.Price;
import com.os.client.model.PriceUnit;
import com.os.client.model.RebateRate;
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
import com.os.replay.model.LedgerRecord;

public class LoanProposalUtil {

	private static final Logger logger = LoggerFactory.getLogger(LoanProposalUtil.class);

	public LoanProposal createLoanProposal(LedgerRecord ledgerRecord) {

		LoanProposal loanProposal = new LoanProposal();

		Party party = new Party();
		party.setPartyId(ledgerRecord.getOneSourcePartyId());
		party.setPartyName(ledgerRecord.getOneSourcePartyName());
		party.setGleifLei(ledgerRecord.getOneSourcePartyGleifLei());

		Party counterparty = new Party();
		counterparty.setPartyId(ledgerRecord.getOneSourceCounterpartyId());
		counterparty.setPartyName(ledgerRecord.getOneSourceCounterpartyName());
		counterparty.setGleifLei(ledgerRecord.getOneSourceCounterpartyGleifLei());

		TradeAgreement trade = new TradeAgreement();

		TransactingParties transactingParties = new TransactingParties();
		
		TransactingParty borrowerTransactingParty = new TransactingParty();
		borrowerTransactingParty.setPartyRole(PartyRole.BORROWER);
		borrowerTransactingParty
				.setParty(PartyRole.BORROWER.toString().equals(ledgerRecord.getBorrowLoan()) ? party : counterparty);
		transactingParties.add(borrowerTransactingParty);

		InternalReference borrowerInternalRef = new InternalReference();
		borrowerInternalRef.setAccountId(null);
		borrowerInternalRef.setBrokerCd(null);
		borrowerInternalRef.setInternalRefId(
				PartyRole.BORROWER.toString().equals(ledgerRecord.getBorrowLoan()) ? ledgerRecord.getInternalRefId()
						: null);

		borrowerTransactingParty.setInternalRef(borrowerInternalRef);

		TransactingParty lenderTransactingParty = new TransactingParty();
		lenderTransactingParty.setPartyRole(PartyRole.LENDER);
		lenderTransactingParty
				.setParty(PartyRole.LENDER.toString().equals(ledgerRecord.getBorrowLoan()) ? party : counterparty);
		transactingParties.add(lenderTransactingParty);

		InternalReference lenderInternalRef = new InternalReference();
		lenderInternalRef.setAccountId(null);
		lenderInternalRef.setBrokerCd(null);
		lenderInternalRef.setInternalRefId(
				PartyRole.LENDER.toString().equals(ledgerRecord.getBorrowLoan()) ? ledgerRecord.getInternalRefId()
						: null);

		lenderTransactingParty.setInternalRef(lenderInternalRef);

		trade.setTransactingParties(transactingParties);

		Venues venues = new Venues();

		Venue venue = new Venue();
		venue.setType(VenueType.OFFPLATFORM);
		venue.setTransactionDatetime(OffsetDateTime.now());

		venues.add(venue);

		trade.setVenues(venues);

		Instrument instrument = new Instrument();
		instrument.setFigi(ledgerRecord.getFigi());
		instrument.setTicker(ledgerRecord.getTicker());
		instrument.setCusip(ledgerRecord.getCusip());
		instrument.setSedol(ledgerRecord.getSedol());
		instrument.setDescription(ledgerRecord.getSecurityDescription());

		Price price = new Price();
		price.setCurrency(CurrencyCd.USD);
		price.setUnit(PriceUnit.SHARE);
		Double f = null;
		if (ledgerRecord.getContractPrice() != null) {
			try {
				f = Double.valueOf(ledgerRecord.getContractPrice().doubleValue());
			} catch (Exception p) {
				logger.warn("Bad price: {}", ledgerRecord.getContractPrice().toPlainString());
			}
		}
		price.setValue(f);

		instrument.setPrice(price);

		trade.setInstrument(instrument);

		LocalDate tradeDate = ledgerRecord.getTradeDate();

		Double r = null;
		if (ledgerRecord.getSpreadRate() != null) {
			try {
				r = Double.valueOf(ledgerRecord.getSpreadRate().doubleValue());
			} catch (Exception p) {
				logger.warn("Bad rate: {}", ledgerRecord.getSpreadRate());
			}
		}

		CollateralType collateralType = CollateralType.CASH;

		FloatingRateDef floatingRateDef = new FloatingRateDef();
		floatingRateDef.setSpread(r);
		floatingRateDef.setCutoffTime("18:00");
		floatingRateDef.setEffectiveDate(tradeDate);
		//floatingRateDef.setEffectiveRate(ledgerRecord.getEffectiveRate().doubleValue()); //TODO - based on autorerate setting
		floatingRateDef.setBenchmark(BenchmarkCd.OBFR);
		floatingRateDef.setIsAutoRerate(true);

		FloatingRate floatingRate = new FloatingRate();
		floatingRate.setFloating(floatingRateDef);

		RebateRate rebateRate = new RebateRate();
		rebateRate.setRebate(floatingRate);

		trade.setRate(rebateRate);

		BigDecimal q = null;
		if (ledgerRecord.getQuantity() != null) {
			try {
				q = new BigDecimal(ledgerRecord.getQuantity());
			} catch (Exception p) {
				logger.warn("Bad quantity: {}", ledgerRecord.getQuantity());
			}
		}

		trade.setQuantity(q.intValue());
		trade.setBillingCurrency(CurrencyCd.USD);
		trade.setDividendRatePct(ledgerRecord.getDividendRate().doubleValue());
		trade.setTradeDate(tradeDate);
		trade.setTermType(TermType.OPEN);
		trade.setTermDate(null);
		trade.setSettlementDate(LocalDate.now().plusDays(2));
		trade.setSettlementType(SettlementType.DVP);

			Collateral collateral = new Collateral();
			BigDecimal contractPrice = BigDecimal
					.valueOf(trade.getInstrument().getPrice().getValue().doubleValue() * 1.02);
			contractPrice = contractPrice.setScale(2, java.math.RoundingMode.HALF_UP);
			collateral.setContractPrice(contractPrice.doubleValue());
			BigDecimal contractValue = BigDecimal.valueOf(
					trade.getQuantity().doubleValue() * (trade.getInstrument().getPrice().getValue().doubleValue()));
			contractValue = contractValue.setScale(2, java.math.RoundingMode.HALF_UP);
			collateral.setContractValue(contractValue.doubleValue());
			BigDecimal collateralValue = BigDecimal
					.valueOf(trade.getQuantity().doubleValue() * contractPrice.doubleValue());
			collateralValue = collateralValue.setScale(2, java.math.RoundingMode.HALF_UP);
			collateral.setCollateralValue(collateralValue.doubleValue());
			collateral.setCurrency(CurrencyCd.USD);
			collateral.setType(collateralType);
			collateral.setMargin(ledgerRecord.getCollateralMargin().doubleValue());

			// only add rounding rules if proposer is the lender
			if (PartyRole.LENDER.toString().equals(ledgerRecord.getBorrowLoan())) {
				collateral.setRoundingRule(10);
				collateral.setRoundingMode(RoundingMode.ALWAYSUP);
			}

			trade.setCollateral(collateral);

			loanProposal.setTrade(trade);

			PartySettlementInstruction partySettlementInstruction = new PartySettlementInstruction();
			partySettlementInstruction.setPartyRole(PartyRole.fromValue(ledgerRecord.getBorrowLoan()));
			partySettlementInstruction.setSettlementStatus(SettlementStatus.NONE);
			partySettlementInstruction.setInternalAcctCd(ledgerRecord.getSsiInternalAcctCd());

			SettlementInstruction instruction = new SettlementInstruction();
			partySettlementInstruction.setInstruction(instruction);
			instruction.setSettlementBic(ledgerRecord.getSsiSettlementBic());
			instruction.setLocalAgentBic(ledgerRecord.getSsiLocalAgentBic());
			instruction.setLocalAgentName(ledgerRecord.getSsiLocalAgentName());
			instruction.setLocalAgentAcct(ledgerRecord.getSsiLocalAgentAcct());
			instruction.setCustodianBic(ledgerRecord.getSsiCustodianBic());
			instruction.setCustodianName(ledgerRecord.getSsiCustodianName());
			instruction.setCustodianAcct(ledgerRecord.getSsiCustodianAcct());
			instruction.setDtcParticipantNumber(ledgerRecord.getDtcParticipantNum());
			instruction.setCdsCustomerUnitId(ledgerRecord.getCdsParticipantNum());

			loanProposal.setSettlement(Collections.singletonList(partySettlementInstruction));

			return loanProposal;
	}
	
	public String parseResourceUri(String uri) {
		
		String contractId = uri.substring(uri.lastIndexOf("/")+1);
		
		return contractId;
	}
	
	public boolean actingAsLender(Loan loan, String partyId) {
		return false;
	}
}