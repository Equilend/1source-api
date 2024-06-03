drop table if exists ledger_a;

create table ledger_a (
	reference_id varchar not null,
	trade_dt date,
	borrowLoan varchar,
	figi varchar,
	ticker varchar,
	cusip varchar,
	sedol varchar,
	security_type varchar,
	exchange_cd varchar,
	description varchar,
	country varchar,
	quantity numeric,
	currency varchar,
	dividend numeric,
	price numeric,
	margin numeric,
	rate numeric,
	benchmark numeric,
	spread numeric,
	org_id varchar,
	counterparty_org_id varchar,
	os_contract_id varchar,
	os_status varchar
);

drop index if exists idx1_ledger_a;
CREATE INDEX idx1_ledger_a ON ledger_a USING btree (reference_id);
drop index if exists idx2_ledger_a;
CREATE INDEX idx2_ledger_a ON ledger_a USING btree (sedol);
drop index if exists idx3_ledger_a;
CREATE INDEX idx3_ledger_a ON ledger_a USING btree (os_contract_id);
