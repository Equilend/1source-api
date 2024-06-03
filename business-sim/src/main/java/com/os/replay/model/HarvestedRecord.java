package com.os.replay.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class HarvestedRecord {

	String rowtype;
	LocalDate fileDate;
	String internalRefId;
	String ticker;
	String cusip;
	String isin;
	String sedol;
	String securityDescription;
	String countryCd;
	Long quantity;
	String currencyCd;
	BigDecimal dividendRate;
	LocalDate tradeDate;
	LocalDate settlementDate;
	BigDecimal contractPrice;
	BigDecimal collateralMargin;
	BigDecimal effectiveRate;
	BigDecimal benchmarkRate;
	BigDecimal spreadRate;

	public String getRowtype() {
		return rowtype;
	}

	public void setRowtype(String rowtype) {
		this.rowtype = rowtype;
	}

	public LocalDate getFileDate() {
		return fileDate;
	}

	public void setFileDate(LocalDate fileDate) {
		this.fileDate = fileDate;
	}

	public String getInternalRefId() {
		return internalRefId;
	}

	public void setInternalRefId(String internalRefId) {
		this.internalRefId = internalRefId;
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

	public String getIsin() {
		return isin;
	}

	public void setIsin(String isin) {
		this.isin = isin;
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

	public LocalDate getTradeDate() {
		return tradeDate;
	}

	public void setTradeDate(LocalDate tradeDate) {
		this.tradeDate = tradeDate;
	}

	public LocalDate getSettlementDate() {
		return settlementDate;
	}

	public void setSettlementDate(LocalDate settlementDate) {
		this.settlementDate = settlementDate;
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

	@Override
	public String toString() {
		return "HarvestedRecord [rowtype=" + rowtype + ", fileDate=" + fileDate + ", internalRefId=" + internalRefId
				+ ", ticker=" + ticker + ", cusip=" + cusip + ", isin=" + isin + ", sedol=" + sedol
				+ ", securityDescription=" + securityDescription + ", countryCd=" + countryCd + ", quantity=" + quantity
				+ ", currencyCd=" + currencyCd + ", dividendRate=" + dividendRate + ", tradeDate=" + tradeDate
				+ ", settlementDate=" + settlementDate + ", contractPrice=" + contractPrice + ", collateralMargin="
				+ collateralMargin + ", effectiveRate=" + effectiveRate + ", benchmarkRate=" + benchmarkRate
				+ ", spreadRate=" + spreadRate + "]";
	}

	public String toCSV() {
		
		StringBuffer buf = new StringBuffer();
		buf.append(rowtype).append(",");
		buf.append(fileDate).append(",");
		buf.append(internalRefId).append(",");
		buf.append(ticker == null ? "" : ticker).append(",");
		buf.append(cusip == null ? "" : cusip).append(",");
		buf.append(isin == null ? "" : isin).append(",");
		buf.append(sedol == null ? "" : sedol).append(",");
		buf.append(securityDescription == null ? "" : securityDescription).append(",");
		buf.append(countryCd == null ? "" : countryCd).append(",");
		buf.append(quantity == null ? "" : quantity).append(",");
		buf.append(currencyCd == null ? "" : currencyCd).append(",");
		buf.append(dividendRate == null ? "" : dividendRate).append(",");
		buf.append(tradeDate == null ? "" : tradeDate).append(",");
		buf.append(settlementDate == null ? "" : settlementDate).append(",");
		buf.append(contractPrice == null ? "" : contractPrice).append(",");
		buf.append(collateralMargin == null ? "" : collateralMargin).append(",");
		buf.append(effectiveRate == null ? "" : effectiveRate).append(",");
		buf.append(benchmarkRate == null ? "" : benchmarkRate).append(",");
		buf.append(spreadRate == null ? "" : spreadRate);
		
		return buf.toString();		
	}
}
