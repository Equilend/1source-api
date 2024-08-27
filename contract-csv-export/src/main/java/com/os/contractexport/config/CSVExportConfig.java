package com.os.contractexport.config;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CSVExportConfig {

	@Bean
	public PrintWriter csvWriter() {
		
		PrintWriter writer = null;
		
		try {
			
			writer = new PrintWriter("contracts.csv");
			
			StringBuffer sb = new StringBuffer();
			sb.append("loanId").append(",");
			sb.append("loanStatus").append(",");
			sb.append("lastUpdateDatetime").append(",");
			
			sb.append("figi").append(",");
			sb.append("ticker").append(",");
			sb.append("cusip").append(",");
			sb.append("isin").append(",");
			sb.append("sedol").append(",");
			sb.append("description").append(",");
			sb.append("marketCd").append(",");
			
			sb.append("quantity").append(",");
			sb.append("openQuantity").append(",");
			sb.append("billingCurrency").append(",");
			sb.append("dividendRatePct").append(",");
			sb.append("tradeDate").append(",");
			sb.append("settlementDate").append(",");
			sb.append("settlementType").append(",");
			
			sb.append("collateralType").append(",");
			sb.append("contratPrice").append(",");
			sb.append("contractValue").append(",");
			sb.append("contractCurrency").append(",");
			sb.append("roundingRule").append(",");
			sb.append("roundingMode").append(",");
			sb.append("margin").append(",");
			
			sb.append("borrowerPartyId").append(",");
			sb.append("borrowerPartyName").append(",");
			sb.append("borrowerLei").append(",");
			sb.append("borrowerBrokerCd").append(",");
			sb.append("borrowerAccountId").append(",");
			sb.append("borrowerInternalRefId").append(",");

			sb.append("lenderPartyId").append(",");
			sb.append("lenderPartyName").append(",");
			sb.append("lenderLei").append(",");
			sb.append("lenderBrokerCd").append(",");
			sb.append("lenderAccountId").append(",");
			sb.append("lenderInternalRefId").append(",");

			sb.append("endOfLine");
			
			writer.println(sb.toString());
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return writer;
	}
	
	
}
