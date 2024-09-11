package com.os.contractexport.config;

import java.util.ArrayList;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.os.contractexport.model.PartyAccess;

@Configuration
public class PartyAccessListConfig {

	/**
	 * Add your ledger api users and passwords here or refactor to pull from some other source.
	 * 
	 * @return
	 */
	@Bean
	public ArrayList<PartyAccess> partyAccessList() {
		ArrayList<PartyAccess> partyAccessList = new ArrayList<>();
		partyAccessList.add(new PartyAccess("TestLender1User", "fjmxVeKzpzUDg3YJ"));
		partyAccessList.add(new PartyAccess("TestBorrower1User", "FqnNQyUwaenQ8K3h"));
		partyAccessList.add(new PartyAccess("bnystageuser1", "cz5pCWPTzajCG5tk"));

		return partyAccessList;
	}
	
	
}
