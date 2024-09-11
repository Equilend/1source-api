package com.os.contractexport.tasks;

import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.OffsetDateTime;
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

import com.os.client.model.Collateral;
import com.os.client.model.CollateralType;
import com.os.client.model.CurrencyCd;
import com.os.client.model.Instrument;
import com.os.client.model.Loan;
import com.os.client.model.LoanStatus;
import com.os.client.model.Loans;
import com.os.client.model.PartyRole;
import com.os.client.model.RoundingMode;
import com.os.client.model.SettlementType;
import com.os.client.model.TradeAgreement;
import com.os.client.model.TransactingParties;
import com.os.client.model.TransactingParty;
import com.os.contractexport.model.AuthToken;

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

		Loans loans = restWebClient.get().uri("/loans")
				.headers(h -> h.setBearerAuth(token.getAccess_token())).retrieve()
				.onStatus(HttpStatusCode::is4xxClientError, response -> {
					return Mono.empty();
				}).bodyToMono(Loans.class).block();

		for (Loan loan : loans) {
			csvWriter.println(toCSV(loan));
		}

		return RepeatStatus.FINISHED;
	}

	private String toCSV(Loan loan) {

		StringBuffer sb = new StringBuffer();

		sb.append(formatValue(loan.getLoanId())).append(DELIMITER);
		sb.append(formatValue(loan.getLoanStatus())).append(DELIMITER);
		sb.append(formatValue(loan.getLastUpdateDateTime())).append(DELIMITER);

		if (loan.getTrade() != null && loan.getTrade().getInstrument() != null) {
			
			Instrument instrument = loan.getTrade().getInstrument();
			
			sb.append(formatValue(instrument.getFigi())).append(DELIMITER);
			sb.append(formatValue(instrument.getTicker())).append(DELIMITER);
			sb.append(formatValue(instrument.getCusip())).append(DELIMITER);
			sb.append(formatValue(instrument.getIsin())).append(DELIMITER);
			sb.append(formatValue(instrument.getSedol())).append(DELIMITER);
			sb.append("\"").append(formatValue(instrument.getDescription())).append("\"").append(DELIMITER);
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

		if (loan.getTrade() != null) {
			
			TradeAgreement trade = loan.getTrade();

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

		if (loan.getTrade() != null && loan.getTrade().getCollateral() != null) {
			
			Collateral collateral = loan.getTrade().getCollateral();
			
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

		if (loan.getTrade() != null) {
			TransactingParties transactingParties = loan.getTrade().getTransactingParties();
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
				sb.append("\"").append(formatValue(borrowerParty.getParty().getPartyName())).append("\"").append(DELIMITER);
				sb.append(formatValue(borrowerParty.getParty().getGleifLei())).append(DELIMITER);
			} else {
				sb.append(DELIMITER);
				sb.append(DELIMITER);
				sb.append(DELIMITER);
			}
			if (borrowerParty.getInternalRef() != null) {
				sb.append(formatValue(borrowerParty.getInternalRef().getBrokerCd())).append(DELIMITER);
				sb.append("\"").append(formatValue(borrowerParty.getInternalRef().getAccountId())).append("\"").append(DELIMITER);
				sb.append("\"").append(formatValue(borrowerParty.getInternalRef().getInternalRefId())).append("\"").append(DELIMITER);
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
				sb.append("\"").append(formatValue(lenderParty.getParty().getPartyName())).append("\"").append(DELIMITER);
				sb.append(formatValue(lenderParty.getParty().getGleifLei())).append(DELIMITER);
			} else {
				sb.append(DELIMITER);
				sb.append(DELIMITER);
				sb.append(DELIMITER);
			}
			if (lenderParty.getInternalRef() != null) {
				sb.append(formatValue(lenderParty.getInternalRef().getBrokerCd())).append(DELIMITER);
				sb.append("\"").append(formatValue(lenderParty.getInternalRef().getAccountId())).append("\"").append(DELIMITER);
				sb.append("\"").append(formatValue(lenderParty.getInternalRef().getInternalRefId())).append("\"").append(DELIMITER);
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

	private String formatValue(OffsetDateTime b) {
		return b != null ? b.format(DateTimeFormatter.ISO_DATE_TIME) : "";
	}

	private String formatValue(LoanStatus b) {
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
