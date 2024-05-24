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
		partyAccessList.add(new PartyAccess("user1", "password"));
		partyAccessList.add(new PartyAccess("user2", "password"));
		partyAccessList.add(new PartyAccess("user3", "password"));

		return partyAccessList;
	}
	
	
}
