drop table if exists ledger_b_ssi;

create table ledger_b_ssi (
	ssi_internal_acct_cd varchar,
	org_id varchar,
	counterparty_org_id varchar,
	country_cd varchar,
	ssi_settlement_bic varchar,
	ssi_local_agent_bic varchar,
	ssi_local_agent_name varchar,
	ssi_local_agent_acct varchar,
	ssi_custodian_bic varchar,
	ssi_custodian_name varchar,
	ssi_custodian_acct varchar,
	dtc_participant_num varchar,
	cds_participant_num varchar
);

drop index if exists idx1_ledger_b_ssi;
CREATE INDEX idx1_ledger_b_ssi ON ledger_b_ssi USING btree (org_id, counterparty_org_id);

insert into ledger_b_ssi values ('SSI001', 'MY_BORROWER', 'LENDER-ABC', 'US', 'DTCYUS33', 'GSCMUS33XXX', 'GOLDMAN SACHS BANK USA', 'A12345', null, null, null, '0005', null);
insert into ledger_b_ssi values ('SSI002', 'MY_BORROWER', 'LENDER-ABC', 'CA', 'CDSLCATT', 'GSLTCAT10GS', 'GOLDMAN SACHS CANADA INC.', 'B12345', null, null, null, null, 'GSCO');
