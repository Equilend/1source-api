package com.os.console;

import java.io.BufferedReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Random;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.WebClient;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.os.client.model.BenchmarkCd;
import com.os.client.model.Collateral;
import com.os.client.model.CollateralType;
import com.os.client.model.ContractProposal;
import com.os.client.model.CurrencyCd;
import com.os.client.model.FloatingRate;
import com.os.client.model.FloatingRateDef;
import com.os.client.model.Instrument;
import com.os.client.model.InternalReference;
import com.os.client.model.Party;
import com.os.client.model.PartyRole;
import com.os.client.model.PartySettlementInstruction;
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
import com.os.console.api.ConsoleConfig;
import com.os.console.api.LocalDateTypeGsonAdapter;
import com.os.console.api.OffsetDateTimeTypeGsonAdapter;
import com.os.console.util.InstrumentUtil;

public class ContractProposalConsole {

	private static final Logger logger = LoggerFactory.getLogger(ContractProposalConsole.class);

	private ContractProposal contractProposal;
	
	public void execute(BufferedReader consoleIn, ConsoleConfig consoleConfig, WebClient webClient, Party borrowerParty, Party lenderParty, PartyRole proposingParty) {

		Gson gson = new GsonBuilder()
				.setPrettyPrinting()
			    .registerTypeAdapter(LocalDate.class, new LocalDateTypeGsonAdapter())
			    .registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeTypeGsonAdapter())
			    .create();
		
		contractProposal = createContractProposal(consoleConfig, borrowerParty, lenderParty, proposingParty);

		System.out.println(gson.toJson(contractProposal));
		System.out.println();

		String command = null;
		System.out.print("/contracts/ proposal > ");

		try {
			while ((command = consoleIn.readLine()) != null) {
				command = command.trim();
				if (command.equals("?") || command.equalsIgnoreCase("help")) {
					printMainContractHelp();
				} else if (command.equalsIgnoreCase("quit") || command.equalsIgnoreCase("exit") || command.equalsIgnoreCase("q")) {
					System.exit(0);
				} else if (command.equalsIgnoreCase("x")) {
					break;
				} else if (command.equalsIgnoreCase("r")) {

					contractProposal = createContractProposal(consoleConfig, borrowerParty, lenderParty, proposingParty);

					System.out.println(gson.toJson(contractProposal));
					System.out.println();

				} else if (command.equalsIgnoreCase("s")) {
				
					System.out.println("TODO...submit contract propsal");
					System.out.println();

				} else {
					System.out.println("Unknown command");
				}

				System.out.print("/contracts/ proposal > ");
			}
		} catch (Exception e) {
			logger.error("Exception with returns command: " + command);
			e.printStackTrace();
		}

	}

	public ContractProposal createContractProposal(ConsoleConfig consoleConfig, Party borrowerParty, Party lenderParty, PartyRole proposingParty) {

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
		borrowerInternalRef.setInternalRefId(PartyRole.BORROWER.equals(proposingParty) ? UUID.randomUUID().toString() : null);

		borrowerTransactingParty.setInternalRef(borrowerInternalRef);

		TransactingParty lenderTransactingParty = new TransactingParty();
		lenderTransactingParty.setPartyRole(PartyRole.LENDER);
		lenderTransactingParty.setParty(lenderParty);
		transactingParties.add(lenderTransactingParty);

		InternalReference lenderInternalRef = new InternalReference();
		lenderInternalRef.setAccountId(null);
		lenderInternalRef.setBrokerCd(null);
		lenderInternalRef.setInternalRefId(PartyRole.LENDER.equals(proposingParty) ? UUID.randomUUID().toString() : null);

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
		floatingRateDef.setSpread(Double.valueOf(random.nextInt((100-1) + 100)));
		floatingRateDef.setCutoffTime("18:00");
		floatingRateDef.setEffectiveDate(tradeDate);
		floatingRateDef.setBenchmark(BenchmarkCd.OBFR);
		floatingRateDef.setIsAutoRerate(true);

		FloatingRate floatingRate = new FloatingRate();
		floatingRate.setFloating(floatingRateDef);

		RebateRate rebateRate = new RebateRate();
		rebateRate.setRebate(floatingRate);

		trade.setRate(rebateRate);

		trade.setQuantity(((((random.nextInt(100000 - 10000) + 10000))+99)/100)*100);
		trade.setBillingCurrency(CurrencyCd.USD);
		trade.setDividendRatePct(85d);
		trade.setTradeDate(tradeDate);
		trade.setTermType(TermType.OPEN);
		trade.setTermDate(null);
		
		trade.setSettlementDate(tradeDate.plusDays(1));
		trade.setSettlementType(SettlementType.DVP);

		Collateral collateral = new Collateral();
		collateral.setContractPrice(trade.getInstrument().getPrice().getValue().doubleValue());
		
		BigDecimal contractValue = BigDecimal.valueOf(trade.getQuantity().doubleValue() * (trade.getInstrument().getPrice().getValue().doubleValue()));
		contractValue = contractValue.setScale(2, java.math.RoundingMode.HALF_UP);
		collateral.setContractValue(contractValue.doubleValue());
		
		BigDecimal collateralValue = BigDecimal.valueOf(trade.getQuantity().doubleValue() * collateral.getContractPrice().doubleValue() * 1.02);
		collateralValue = collateralValue.setScale(2, java.math.RoundingMode.HALF_UP);
		collateral.setCollateralValue(collateralValue.doubleValue());

		collateral.setCurrency(CurrencyCd.USD);
		collateral.setType(collateralType);
		collateral.setMargin(102d);

		// only add rounding rules if proposer is the lender
		if (PartyRole.LENDER.equals(proposingParty)) {
			collateral.setRoundingRule(10);
			collateral.setRoundingMode(RoundingMode.ALWAYSUP);
		}

		trade.setCollateral(collateral);

		contractProposal.setTrade(trade);

		PartySettlementInstruction partySettlementInstruction = new PartySettlementInstruction();
		partySettlementInstruction.setPartyRole(proposingParty);
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

	private void printMainContractHelp() {
		System.out.println();
		System.out.println("Contract Proposal Menu");
		System.out.println("-----------------------");
		System.out.println("S             - Submit contract proposal");
		System.out.println("R             - Regenerate contract");
		System.out.println("X             - Go back");
		System.out.println();
	}

}