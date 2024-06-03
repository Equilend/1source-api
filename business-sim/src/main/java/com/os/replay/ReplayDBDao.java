package com.os.replay;


import java.math.BigDecimal;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Component;

import com.os.replay.model.HarvestedRecord;
import com.os.replay.model.LedgerRecord;
import com.os.replay.model.LedgerRecordMapper;
import com.os.replay.model.OpenFigiV3ResponseDataItem;

@Component
public class ReplayDBDao {

	private static final Logger logger = LoggerFactory.getLogger(ReplayDBDao.class);

	@Autowired
	@Qualifier("mdJdbcTemplate")
	JdbcTemplate jdbcTemplate;

	public void truncate(String ledgerId) {
		this.jdbcTemplate.update("truncate table ledger_" + ledgerId);
	}

	public void insert(HarvestedRecord record, OpenFigiV3ResponseDataItem figi, String ledgerName, String borrowLoan, String orgId, String counterpartyId) {

		jdbcTemplate.update("insert into ledger_" + ledgerName + " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
				new PreparedStatementSetter() {

					@Override
					public void setValues(PreparedStatement ps) throws SQLException {
						int var = 1;
						ps.setString(var++, record.getInternalRefId());
						if (record.getTradeDate() != null) {
							ps.setDate(var++, Date.valueOf(record.getTradeDate()));
						} else {
							ps.setNull(var++, Types.DATE);
						}
						ps.setString(var++, borrowLoan);
						ps.setString(var++, figi != null ? figi.getFigi() : null);
						ps.setString(var++, record.getTicker());
						ps.setString(var++, record.getCusip());
						ps.setString(var++, record.getSedol());
						ps.setString(var++, figi != null ? figi.getSecurityType2() : null);
						ps.setString(var++, figi != null ? figi.getExchCode() : null);
						ps.setString(var++, record.getSecurityDescription());
						ps.setString(var++, record.getCountryCd());
						ps.setLong(var++, record.getQuantity());
						ps.setString(var++, record.getCurrencyCd());
						ps.setBigDecimal(var++, record.getDividendRate());
						ps.setBigDecimal(var++, record.getContractPrice());
						ps.setBigDecimal(var++, record.getCollateralMargin());
						ps.setBigDecimal(var++, record.getEffectiveRate());
						ps.setBigDecimal(var++, record.getBenchmarkRate());
						ps.setBigDecimal(var++, record.getSpreadRate());
						ps.setString(var++, orgId);
						ps.setString(var++, counterpartyId);
					}

				});
	}
	
	public List<LedgerRecord> getLedgerRecords(LocalDate targetDate, String ledgerName) {
		
		String sql = "SELECT reference_id\n"
				+ ", trade_dt\n"
				+ ", borrowLoan\n"
				+ ", figi\n"
				+ ", ticker\n"
				+ ", cusip\n"
				+ ", sedol\n"
				+ ", description\n"
				+ ", country\n"
				+ ", quantity\n"
				+ ", currency\n"
				+ ", dividend\n"
				+ ", price\n"
				+ ", margin\n"
				+ ", rate\n"
				+ ", benchmark\n"
				+ ", spread\n"
				+ ", x.os_party_id as os_party_id\n"
				+ ", x.org_name as os_party_name\n"
				+ ", x.gleif_lei as os_party_gleif_lei\n"
				+ ", y.os_party_id as os_counterparty_id\n"
				+ ", y.org_name as os_counterparty_name\n"
				+ ", y.gleif_lei as os_counterparty_gleif_lei\n"
				+ ", s.ssi_internal_acct_cd \n"
				+ ", s.ssi_settlement_bic \n"
				+ ", s.ssi_local_agent_bic \n"
				+ ", s.ssi_local_agent_name \n"
				+ ", s.ssi_local_agent_acct\n"
				+ ", s.ssi_custodian_bic \n"
				+ ", s.ssi_custodian_name \n"
				+ ", s.ssi_custodian_acct\n"
				+ ", s.dtc_participant_num \n"
				+ ", s.cds_participant_num \n"
				+ ", os_contract_id\n"
				+ ", os_status\n"
				+ "FROM ledger_" + ledgerName + " a\n"
				+ "join ledger_" + ledgerName + "_parties x on x.org_id = a.org_id\n"
				+ "join ledger_" + ledgerName + "_parties y on y.org_id = a.counterparty_org_id\n"
				+ "join ledger_" + ledgerName + "_ssi s on s.org_id = a.org_id and s.counterparty_org_id = a.counterparty_org_id and s.country_cd = a.country  \n"
				+ "where a.trade_dt = date(?)";

		List<LedgerRecord> ledgerRecords = jdbcTemplate.query(sql, new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setDate(1, Date.valueOf(targetDate));
			}

		}, new LedgerRecordMapper());

		return ledgerRecords;
	}

	public List<LedgerRecord> getLedgerRecords(LocalDate targetDate, String ledgerName, String borrowLoan, String osParty, String osCounterparty, String figi, Long quantity, BigDecimal rate) {
		
		String sql = "SELECT reference_id\n"
				+ ", trade_dt\n"
				+ ", borrowLoan\n"
				+ ", figi\n"
				+ ", ticker\n"
				+ ", cusip\n"
				+ ", sedol\n"
				+ ", description\n"
				+ ", country\n"
				+ ", quantity\n"
				+ ", currency\n"
				+ ", dividend\n"
				+ ", price\n"
				+ ", margin\n"
				+ ", rate\n"
				+ ", benchmark\n"
				+ ", spread\n"
				+ ", x.os_party_id as os_party_id\n"
				+ ", x.org_name as os_party_name\n"
				+ ", x.gleif_lei as os_party_gleif_lei\n"
				+ ", y.os_party_id as os_counterparty_id\n"
				+ ", y.org_name as os_counterparty_name\n"
				+ ", y.gleif_lei as os_counterparty_gleif_lei\n"
				+ ", s.ssi_internal_acct_cd \n"
				+ ", s.ssi_settlement_bic \n"
				+ ", s.ssi_local_agent_bic \n"
				+ ", s.ssi_local_agent_name \n"
				+ ", s.ssi_local_agent_acct\n"
				+ ", s.ssi_custodian_bic \n"
				+ ", s.ssi_custodian_name \n"
				+ ", s.ssi_custodian_acct\n"
				+ ", s.dtc_participant_num \n"
				+ ", s.cds_participant_num \n"
				+ ", os_contract_id\n"
				+ ", os_status\n"
				+ "FROM ledger_" + ledgerName + " a\n"
				+ "join ledger_" + ledgerName + "_parties x on x.org_id = a.org_id and x.os_party_id = ?\n"
				+ "join ledger_" + ledgerName + "_parties y on y.org_id = a.counterparty_org_id and y.os_party_id = ?\n"
				+ "join ledger_" + ledgerName + "_ssi s on s.org_id = a.org_id and s.counterparty_org_id = a.counterparty_org_id and s.country_cd = a.country  \n"
				+ "where a.trade_dt = date(?)\n"
				+ "and a.borrowloan = ?\n"
				+ "and a.figi = ?\n"
				+ "and a.quantity = ?\n"
				+ "and a.rate = ?\n";

		List<LedgerRecord> ledgerRecords = jdbcTemplate.query(sql, new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, osParty);
				ps.setString(2, osCounterparty);
				ps.setDate(3, Date.valueOf(targetDate));
				ps.setString(4, borrowLoan);
				ps.setString(5, figi);
				ps.setLong(6, quantity);
				ps.setBigDecimal(7, rate);
			}

		}, new LedgerRecordMapper());

		return ledgerRecords;
	}

	public void updateOSContractId(String ledgerName, String referenceId, String osContractId) {
		jdbcTemplate.update("update ledger_" + ledgerName + " set os_contract_id = ? where reference_id = ?",
				new PreparedStatementSetter() {

					@Override
					public void setValues(PreparedStatement ps) throws SQLException {
						int var = 1;
						ps.setString(var++, osContractId);
						ps.setString(var++, referenceId);
					}

				});
	}

}
