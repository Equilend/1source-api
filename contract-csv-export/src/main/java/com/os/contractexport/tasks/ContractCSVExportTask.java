package com.os.contractexport.tasks;

import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.WebClient;

import com.os.contractexport.model.AuthToken;
import io.swagger.client.model.Collateral;
import io.swagger.client.model.CollateralType;
import io.swagger.client.model.Contract;
import io.swagger.client.model.ContractStatus;
import io.swagger.client.model.Contracts;
import io.swagger.client.model.CurrencyCd;
import io.swagger.client.model.Instrument;
import io.swagger.client.model.PartyRole;
import io.swagger.client.model.RoundingMode;
import io.swagger.client.model.SettlementType;
import io.swagger.client.model.TradeAgreement;
import io.swagger.client.model.TransactingParties;
import io.swagger.client.model.TransactingParty;

import reactor.core.publisher.Mono;

public class ContractCSVExportTask extends RecordReader implements Tasklet, StepExecutionListener {

	private static final Logger logger = LoggerFactory.getLogger(ContractCSVExportTask.class);

	String DELIMITER = ",";
	String EOL = "EOL";
	
	private AuthToken token;

	@Autowired
	WebClient restWebClient;

	@Autowired
	PrintWriter csvWriter;

	@Override
	public void beforeStep(StepExecution stepExecution) {
		token = (AuthToken) stepExecution.getJobExecution().getExecutionContext().get("token");
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

		logger.info("Export complete for token: " + token.getAccess_token());

		Contracts contracts = restWebClient.get().uri("/contracts")
				.headers(h -> h.setBearerAuth(token.getAccess_token())).retrieve()
				.onStatus(HttpStatusCode::is4xxClientError, response -> {
					return Mono.empty();
				}).bodyToMono(Contracts.class).block();

		for (Contract contract : contracts) {
			csvWriter.println(toCSV(contract));
		}

		return RepeatStatus.FINISHED;
	}

	private String toCSV(Contract contract) {

		StringBuffer sb = new StringBuffer();

		sb.append(formatValue(contract.getContractId())).append(DELIMITER);
		sb.append(formatValue(contract.getContractStatus())).append(DELIMITER);
		sb.append(formatValue(contract.getLastUpdateDateTime())).append(DELIMITER);

		if (contract.getTrade() != null && contract.getTrade().getInstrument() != null) {
			
			Instrument instrument = contract.getTrade().getInstrument();
			
			sb.append(formatValue(instrument.getFigi())).append(DELIMITER);
			sb.append(formatValue(instrument.getTicker())).append(DELIMITER);
			sb.append(formatValue(instrument.getCusip())).append(DELIMITER);
			sb.append(formatValue(instrument.getIsin())).append(DELIMITER);
			sb.append(formatValue(instrument.getSedol())).append(DELIMITER);
			sb.append(formatValue(instrument.getDescription())).append(DELIMITER);
			sb.append(formatValue(instrument.getMarketCd())).append(DELIMITER);
		} else {
			sb.append(DELIMITER);
			sb.append(DELIMITER);
			sb.append(DELIMITER);
			sb.append(DELIMITER);
			sb.append(DELIMITER);
			sb.append(DELIMITER);
			sb.append(DELIMITER);
		}

		if (contract.getTrade() != null) {
			
			TradeAgreement trade = contract.getTrade();

			sb.append(formatValue(trade.getQuantity())).append(DELIMITER);
			sb.append(formatValue(trade.getOpenQuantity())).append(DELIMITER);
			sb.append(formatValue(trade.getBillingCurrency())).append(DELIMITER);
			sb.append(formatValue(trade.getDividendRatePct())).append(DELIMITER);
			sb.append(formatValue(trade.getTradeDate())).append(DELIMITER);
			sb.append(formatValue(trade.getSettlementDate())).append(DELIMITER);
			sb.append(formatValue(trade.getSettlementType())).append(DELIMITER);
		} else {
			sb.append(DELIMITER);
			sb.append(DELIMITER);
			sb.append(DELIMITER);
			sb.append(DELIMITER);
			sb.append(DELIMITER);
			sb.append(DELIMITER);
			sb.append(DELIMITER);
		}

		if (contract.getTrade() != null && contract.getTrade().getCollateral() != null) {
			
			Collateral collateral = contract.getTrade().getCollateral();
			
			sb.append(formatValue(collateral.getType())).append(DELIMITER);
			sb.append(formatValue(collateral.getContractPrice())).append(DELIMITER);
			sb.append(formatValue(collateral.getContractValue())).append(DELIMITER);
			sb.append(formatValue(collateral.getCurrency())).append(DELIMITER);
			sb.append(formatValue(collateral.getRoundingRule())).append(DELIMITER);
			sb.append(formatValue(collateral.getRoundingMode())).append(DELIMITER);
			sb.append(formatValue(collateral.getMargin())).append(DELIMITER);
		} else {
			sb.append(DELIMITER);
			sb.append(DELIMITER);
			sb.append(DELIMITER);
			sb.append(DELIMITER);
			sb.append(DELIMITER);
			sb.append(DELIMITER);
			sb.append(DELIMITER);
		}

		TransactingParty borrowerParty = null;
		TransactingParty lenderParty = null;

		if (contract.getTrade() != null) {
			TransactingParties transactingParties = contract.getTrade().getTransactingParties();
			for (TransactingParty transactingParty : transactingParties) {
				if (PartyRole.BORROWER.equals(transactingParty.getPartyRole())) {
					borrowerParty = transactingParty;
				} else if (PartyRole.LENDER.equals(transactingParty.getPartyRole())) {
					lenderParty = transactingParty;
				}
			}
		}

		if (borrowerParty != null) {
			if (borrowerParty.getParty() != null) {
				sb.append(formatValue(borrowerParty.getParty().getPartyId())).append(DELIMITER);
				sb.append(formatValue(borrowerParty.getParty().getPartyName())).append(DELIMITER);
				sb.append(formatValue(borrowerParty.getParty().getGleifLei())).append(DELIMITER);
			} else {
				sb.append(DELIMITER);
				sb.append(DELIMITER);
				sb.append(DELIMITER);
			}
			if (borrowerParty.getInternalRef() != null) {
				sb.append(formatValue(borrowerParty.getInternalRef().getBrokerCd())).append(DELIMITER);
				sb.append(formatValue(borrowerParty.getInternalRef().getAccountId())).append(DELIMITER);
				sb.append(formatValue(borrowerParty.getInternalRef().getInternalRefId())).append(DELIMITER);
			} else {
				sb.append(DELIMITER);
				sb.append(DELIMITER);
				sb.append(DELIMITER);
			}

		} else {
			sb.append(DELIMITER);
			sb.append(DELIMITER);
			sb.append(DELIMITER);
			sb.append(DELIMITER);
			sb.append(DELIMITER);
			sb.append(DELIMITER);
		}

		if (lenderParty != null) {
			if (lenderParty.getParty() != null) {
				sb.append(formatValue(lenderParty.getParty().getPartyId())).append(DELIMITER);
				sb.append(formatValue(lenderParty.getParty().getPartyName())).append(DELIMITER);
				sb.append(formatValue(lenderParty.getParty().getGleifLei())).append(DELIMITER);
			} else {
				sb.append(DELIMITER);
				sb.append(DELIMITER);
				sb.append(DELIMITER);
			}
			if (lenderParty.getInternalRef() != null) {
				sb.append(formatValue(lenderParty.getInternalRef().getBrokerCd())).append(DELIMITER);
				sb.append(formatValue(lenderParty.getInternalRef().getAccountId())).append(DELIMITER);
				sb.append(formatValue(lenderParty.getInternalRef().getInternalRefId())).append(DELIMITER);
			} else {
				sb.append(DELIMITER);
				sb.append(DELIMITER);
				sb.append(DELIMITER);
			}
		} else {
			sb.append(DELIMITER);
			sb.append(DELIMITER);
			sb.append(DELIMITER);
			sb.append(DELIMITER);
			sb.append(DELIMITER);
			sb.append(DELIMITER);
		}

		sb.append(EOL);
		
		return sb.toString();

	}

	private String formatValue(Double b) {
		return b != null ? b.toString() : "";
	}

	private String formatValue(Integer b) {
		return b != null ? b.toString() : "";
	}

	private String formatValue(String b) {
		return b != null ? b : "";
	}

	private String formatValue(LocalDate b) {
		return b != null ? b.format(DateTimeFormatter.BASIC_ISO_DATE) : "";
	}

	private String formatValue(LocalDateTime b) {
		return b != null ? b.format(DateTimeFormatter.ISO_DATE_TIME) : "";
	}

	private String formatValue(ContractStatus b) {
		return b != null ? b.getValue() : "";
	}
	
	private String formatValue(CurrencyCd b) {
		return b != null ? b.getValue() : "";
	}
	
	private String formatValue(CollateralType b) {
		return b != null ? b.getValue() : "";
	}

	private String formatValue(SettlementType b) {
		return b != null ? b.getValue() : "";
	}

	private String formatValue(RoundingMode b) {
		return b != null ? b.getValue() : "";
	}
}
