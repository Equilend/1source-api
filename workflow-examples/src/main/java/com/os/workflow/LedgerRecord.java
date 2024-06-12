package com.os.workflow;

import java.math.BigDecimal;
import java.util.Date;

public class LedgerRecord {

	String internalRefId;
	Date tradeDate;
	String borrowLoan;
	String figi;
	String ticker;
	String cusip;
	String sedol;
	String securityDescription;
	String countryCd;
	Long quantity;
	String currencyCd;
	BigDecimal dividendRate;
	BigDecimal contractPrice;
	BigDecimal collateralMargin;
	BigDecimal effectiveRate;
	BigDecimal benchmarkRate;
	BigDecimal spreadRate;

	String oneSourcePartyId;
	String oneSourcePartyName;
	String oneSourcePartyGleifLei;
	String oneSourceCounterpartyId;
	String oneSourceCounterpartyName;
	String oneSourceCounterpartyGleifLei;
	String oneSourceContractId;
	String oneSourceContractStatus;

	String ssiInternalAcctCd;
	String ssiSettlementBic;
	String ssiLocalAgentBic;
	String ssiLocalAgentName;
	String ssiLocalAgentAcct;
	String ssiCustodianBic;
	String ssiCustodianName;
	String ssiCustodianAcct;
	String dtcParticipantNum;
	String cdsParticipantNum;

	public String getInternalRefId() {
		return internalRefId;
	}

	public void setInternalRefId(String internalRefId) {
		this.internalRefId = internalRefId;
	}

	public Date getTradeDate() {
		return tradeDate;
	}

	public void setTradeDate(Date tradeDate) {
		this.tradeDate = tradeDate;
	}

	public String getBorrowLoan() {
		return borrowLoan;
	}

	public void setBorrowLoan(String borrowLoan) {
		this.borrowLoan = borrowLoan;
	}

	public String getFigi() {
		return figi;
	}

	public void setFigi(String figi) {
		this.figi = figi;
	}

	public String getTicker() {
		return ticker;
	}

	public void setTicker(String ticker) {
		this.ticker = ticker;
	}

	public String getCusip() {
		return cusip;
	}

	public void setCusip(String cusip) {
		this.cusip = cusip;
	}

	public String getSedol() {
		return sedol;
	}

	public void setSedol(String sedol) {
		this.sedol = sedol;
	}

	public String getSecurityDescription() {
		return securityDescription;
	}

	public void setSecurityDescription(String securityDescription) {
		this.securityDescription = securityDescription;
	}

	public String getCountryCd() {
		return countryCd;
	}

	public void setCountryCd(String countryCd) {
		this.countryCd = countryCd;
	}

	public Long getQuantity() {
		return quantity;
	}

	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}

	public String getCurrencyCd() {
		return currencyCd;
	}

	public void setCurrencyCd(String currencyCd) {
		this.currencyCd = currencyCd;
	}

	public BigDecimal getDividendRate() {
		return dividendRate;
	}

	public void setDividendRate(BigDecimal dividendRate) {
		this.dividendRate = dividendRate;
	}

	public BigDecimal getContractPrice() {
		return contractPrice;
	}

	public void setContractPrice(BigDecimal contractPrice) {
		this.contractPrice = contractPrice;
	}

	public BigDecimal getCollateralMargin() {
		return collateralMargin;
	}

	public void setCollateralMargin(BigDecimal collateralMargin) {
		this.collateralMargin = collateralMargin;
	}

	public BigDecimal getEffectiveRate() {
		return effectiveRate;
	}

	public void setEffectiveRate(BigDecimal effectiveRate) {
		this.effectiveRate = effectiveRate;
	}

	public BigDecimal getBenchmarkRate() {
		return benchmarkRate;
	}

	public void setBenchmarkRate(BigDecimal benchmarkRate) {
		this.benchmarkRate = benchmarkRate;
	}

	public BigDecimal getSpreadRate() {
		return spreadRate;
	}

	public void setSpreadRate(BigDecimal spreadRate) {
		this.spreadRate = spreadRate;
	}

	public String getOneSourcePartyId() {
		return oneSourcePartyId;
	}

	public void setOneSourcePartyId(String oneSourcePartyId) {
		this.oneSourcePartyId = oneSourcePartyId;
	}

	public String getOneSourcePartyGleifLei() {
		return oneSourcePartyGleifLei;
	}

	public void setOneSourcePartyGleifLei(String oneSourcePartyGleifLei) {
		this.oneSourcePartyGleifLei = oneSourcePartyGleifLei;
	}

	public String getOneSourceCounterpartyId() {
		return oneSourceCounterpartyId;
	}

	public void setOneSourceCounterpartyId(String oneSourceCounterpartyId) {
		this.oneSourceCounterpartyId = oneSourceCounterpartyId;
	}

	public String getOneSourceCounterpartyGleifLei() {
		return oneSourceCounterpartyGleifLei;
	}

	public void setOneSourceCounterpartyGleifLei(String oneSourceCounterpartyGleifLei) {
		this.oneSourceCounterpartyGleifLei = oneSourceCounterpartyGleifLei;
	}

	public String getOneSourceContractId() {
		return oneSourceContractId;
	}

	public void setOneSourceContractId(String oneSourceContractId) {
		this.oneSourceContractId = oneSourceContractId;
	}

	public String getOneSourceContractStatus() {
		return oneSourceContractStatus;
	}

	public void setOneSourceContractStatus(String oneSourceContractStatus) {
		this.oneSourceContractStatus = oneSourceContractStatus;
	}

	public String getSsiInternalAcctCd() {
		return ssiInternalAcctCd;
	}

	public void setSsiInternalAcctCd(String ssiInternalAcctCd) {
		this.ssiInternalAcctCd = ssiInternalAcctCd;
	}

	public String getSsiSettlementBic() {
		return ssiSettlementBic;
	}

	public void setSsiSettlementBic(String ssiSettlementBic) {
		this.ssiSettlementBic = ssiSettlementBic;
	}

	public String getSsiLocalAgentBic() {
		return ssiLocalAgentBic;
	}

	public void setSsiLocalAgentBic(String ssiLocalAgentBic) {
		this.ssiLocalAgentBic = ssiLocalAgentBic;
	}

	public String getSsiLocalAgentName() {
		return ssiLocalAgentName;
	}

	public void setSsiLocalAgentName(String ssiLocalAgentName) {
		this.ssiLocalAgentName = ssiLocalAgentName;
	}

	public String getSsiLocalAgentAcct() {
		return ssiLocalAgentAcct;
	}

	public void setSsiLocalAgentAcct(String ssiLocalAgentAcct) {
		this.ssiLocalAgentAcct = ssiLocalAgentAcct;
	}

	public String getSsiCustodianBic() {
		return ssiCustodianBic;
	}

	public void setSsiCustodianBic(String ssiCustodianBic) {
		this.ssiCustodianBic = ssiCustodianBic;
	}

	public String getSsiCustodianName() {
		return ssiCustodianName;
	}

	public void setSsiCustodianName(String ssiCustodianName) {
		this.ssiCustodianName = ssiCustodianName;
	}

	public String getSsiCustodianAcct() {
		return ssiCustodianAcct;
	}

	public void setSsiCustodianAcct(String ssiCustodianAcct) {
		this.ssiCustodianAcct = ssiCustodianAcct;
	}

	public String getDtcParticipantNum() {
		return dtcParticipantNum;
	}

	public void setDtcParticipantNum(String dtcParticipantNum) {
		this.dtcParticipantNum = dtcParticipantNum;
	}

	public String getCdsParticipantNum() {
		return cdsParticipantNum;
	}

	public void setCdsParticipantNum(String cdsParticipantNum) {
		this.cdsParticipantNum = cdsParticipantNum;
	}

	public String getOneSourcePartyName() {
		return oneSourcePartyName;
	}

	public void setOneSourcePartyName(String oneSourcePartyName) {
		this.oneSourcePartyName = oneSourcePartyName;
	}

	public String getOneSourceCounterpartyName() {
		return oneSourceCounterpartyName;
	}

	public void setOneSourceCounterpartyName(String oneSourceCounterpartyName) {
		this.oneSourceCounterpartyName = oneSourceCounterpartyName;
	}

}
