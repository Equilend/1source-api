package com.os.replay.model;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import org.springframework.jdbc.core.RowMapper;

public class HarvestedRecordMapper implements RowMapper<HarvestedRecord> {

	@Override
	public HarvestedRecord mapRow(final ResultSet rs, final int rowNum) throws SQLException {

		final HarvestedRecord record = new HarvestedRecord();

		record.setRowtype(rs.getString("rowtype"));
		if (rs.getDate("file_date") != null) {
			record.setFileDate(rs.getDate("file_date").toLocalDate());
		}
		record.setInternalRefId(rs.getString("internal_ref_id"));
		record.setTicker(rs.getString("ticker"));
		record.setCusip(rs.getString("cusip"));
		record.setIsin(rs.getString("isin"));
		record.setSedol(rs.getString("sedol"));
		record.setSecurityDescription(rs.getString("security_description"));
		record.setCountryCd(rs.getString("country_cd"));
		record.setQuantity(rs.getLong("unit_quantity"));
		record.setCurrencyCd(rs.getString("collateral_currency_cd"));
		record.setDividendRate(rs.getBigDecimal("dividend_rate"));
		if (rs.getDate("trade_dt") != null) {
			record.setTradeDate(rs.getDate("trade_dt").toLocalDate());
		}
		if (rs.getDate("settlement_dt") != null) {
			record.setSettlementDate(rs.getDate("settlement_dt").toLocalDate());
		}
		record.setContractPrice(rs.getBigDecimal("contract_price"));
		record.setCollateralMargin(rs.getBigDecimal("collateral_margin"));
		record.setEffectiveRate(rs.getBigDecimal("effective_rate"));
		record.setBenchmarkRate(rs.getBigDecimal("obfr_rate_amt"));
		record.setSpreadRate(rs.getBigDecimal("spread_amt"));

		return record;
	}

	public HarvestedRecord mapRow(final String[] csv, final int rowNum) throws SQLException {

		final HarvestedRecord record = new HarvestedRecord();

		int var = 0;

		try {
			record.setRowtype(parseString(csv[var++]));
			record.setFileDate(parseDate(csv[var++]));
			record.setInternalRefId(parseString(csv[var++]));
			record.setTicker(parseString(csv[var++]));
			record.setCusip(parseString(csv[var++]));
			record.setIsin(parseString(csv[var++]));
			record.setSedol(parseString(csv[var++]));
			record.setSecurityDescription(parseString(csv[var++]));
			record.setCountryCd(parseString(csv[var++]));
			record.setQuantity(parseLong(csv[var++]));
			record.setCurrencyCd(parseString(csv[var++]));
			record.setDividendRate(parseBigDecimal(csv[var++]));
			record.setTradeDate(parseDate(csv[var++]));
			record.setSettlementDate(parseDate(csv[var++]));
			record.setContractPrice(parseBigDecimal(csv[var++]));
			record.setCollateralMargin(parseBigDecimal(csv[var++]));
			record.setEffectiveRate(parseBigDecimal(csv[var++]));
			record.setBenchmarkRate(parseBigDecimal(csv[var++]));
			record.setSpreadRate(parseBigDecimal(csv[var++]));
		} catch (ArrayIndexOutOfBoundsException e) {
			// skip
		}

		return record;
	}

	private String parseString(String s) {
		if (s == null || s.trim().length() == 0) {
			return null;
		}

		return s;
	}

	private LocalDate parseDate(String s) {
		if (s == null || s.trim().length() == 0) {
			return null;
		}

		return LocalDate.parse(s);
	}

	private Long parseLong(String s) {
		if (s == null || s.trim().length() == 0) {
			return null;
		}

		return Long.valueOf(s);
	}

	private BigDecimal parseBigDecimal(String s) {
		if (s == null || s.trim().length() == 0) {
			return null;
		}

		return new BigDecimal(s);
	}
}
