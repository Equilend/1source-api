package com.os.replay.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class LedgerRecordMapper implements RowMapper<LedgerRecord> {

	@Override
	public LedgerRecord mapRow(final ResultSet rs, final int rowNum) throws SQLException {

		final LedgerRecord record = new LedgerRecord();

		record.setInternalRefId(rs.getString("reference_id"));
		if (rs.getDate("trade_dt") != null) {
			record.setTradeDate(rs.getDate("trade_dt").toLocalDate());
		}
		record.setBorrowLoan(rs.getString("borrowLoan"));
		record.setFigi(rs.getString("figi"));
		record.setTicker(rs.getString("ticker"));
		record.setCusip(rs.getString("cusip"));
		record.setSedol(rs.getString("sedol"));
		record.setSecurityDescription(rs.getString("description"));
		record.setCountryCd(rs.getString("country"));
		record.setQuantity(rs.getLong("quantity"));
		record.setCurrencyCd(rs.getString("currency"));
		record.setDividendRate(rs.getBigDecimal("dividend"));
		record.setContractPrice(rs.getBigDecimal("price"));
		record.setCollateralMargin(rs.getBigDecimal("margin"));
		record.setEffectiveRate(rs.getBigDecimal("rate"));
		record.setBenchmarkRate(rs.getBigDecimal("benchmark"));
		record.setSpreadRate(rs.getBigDecimal("spread"));
		record.setOneSourcePartyId(rs.getString("os_party_id"));
		record.setOneSourcePartyName(rs.getString("os_party_name"));
		record.setOneSourcePartyGleifLei(rs.getString("os_party_gleif_lei"));
		record.setOneSourceCounterpartyId(rs.getString("os_counterparty_id"));
		record.setOneSourceCounterpartyName(rs.getString("os_counterparty_name"));
		record.setOneSourceCounterpartyGleifLei(rs.getString("os_counterparty_gleif_lei"));
		record.setOneSourceLoanId(rs.getString("os_contract_id"));
		record.setOneSourceLoanStatus(rs.getString("os_status"));
		
		record.setSsiInternalAcctCd(rs.getString("ssi_internal_acct_cd"));
		record.setSsiSettlementBic(rs.getString("ssi_settlement_bic"));
		record.setSsiLocalAgentBic(rs.getString("ssi_local_agent_bic"));
		record.setSsiLocalAgentName(rs.getString("ssi_local_agent_name"));
		record.setSsiLocalAgentAcct(rs.getString("ssi_local_agent_acct"));
		record.setSsiCustodianBic(rs.getString("ssi_custodian_bic"));
		record.setSsiCustodianName(rs.getString("ssi_custodian_name"));
		record.setSsiCustodianAcct(rs.getString("ssi_custodian_acct"));
		record.setDtcParticipantNum(rs.getString("dtc_participant_num"));
		record.setCdsParticipantNum(rs.getString("cds_participant_num"));
		
		return record;
	}
}
