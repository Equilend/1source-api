drop table if exists ledger_a_parties;

create table ledger_a_parties (
	org_id varchar,
	org_name varchar,
	gleif_lei varchar,
	os_party_id varchar
);

drop index if exists idx1_ledger_a_parties;
CREATE INDEX idx1_ledger_a_parties ON ledger_a_parties USING btree (org_id);

insert into ledger_a_parties values ('USPRIMELEND', 'US Prime Lender', 'KTB500SKZSDI75VSFU40', 'TLEN-US');
insert into ledger_a_parties values ('BORROWER-X', 'Borrower US X', 'KTB500SKZSDI75VSFU40', 'TBORR-US');

