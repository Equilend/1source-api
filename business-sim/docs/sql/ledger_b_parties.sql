drop table if exists ledger_b_parties;

create table ledger_b_parties (
	org_id varchar,
	org_name varchar,
	gleif_lei varchar,
	os_party_id varchar
);

drop index if exists idx1_ledger_b_parties;
CREATE INDEX idx1_ledger_b_parties ON ledger_b_parties USING btree (org_id);

insert into ledger_b_parties values ('MY_BORROWER', 'My Primary Borrower US', 'FOR8UP27PHTHYVLBNG30', 'GSCO US');
insert into ledger_b_parties values ('LENDER-ABC', 'Lender ABC', 'WFLLPEPC7FZXENRZV188', 'BNYMELLON');
