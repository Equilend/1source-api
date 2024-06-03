drop table if exists ledger_a_ssi;

create table ledger_a_ssi (
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

drop index if exists idx1_ledger_a_ssi;
CREATE INDEX idx1_ledger_a_ssi ON ledger_a_ssi USING btree (org_id, counterparty_org_id);

insert into ledger_a_ssi values ('SSI001', 'USPRIMELEND', 'BORROWER-X', 'US', 'DTCYUS33', 'IRVTBEBBXXX', 'THE BANK OF NEW YORK MELLON SA/NV', 'A12345', 'IRVTBEBBXXX', 'THE BANK OF NEW YORK MELLON SA/NV', 'C12345', '0901', null);
insert into ledger_a_ssi values ('SSI002', 'USPRIMELEND', 'BORROWER-X', 'CA', 'CDSLCATT', 'MELNUS3PGSS', 'THE BANK OF NEW YORK MELLON', 'BNXF1999412', 'IRVTBEBBXXX', 'THE BANK OF NEW YORK MELLON SA/NV', 'C67890', null, 'COMO');
