package com.os.workflow;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

@ConfigurationProperties(prefix = "com.os.config")
public class WorkflowConfig {

	private final String auth_grant_type;
	private final String auth_client_id;
	private final String auth_client_secret;
	private final String auth_username;
	private final String auth_password;
	private final String contract_id;
	private final String acting_as;
	private final String party_id;

	@ConstructorBinding
	public WorkflowConfig(String auth_grant_type, String auth_client_id, String auth_client_secret,
			String auth_username, String auth_password, String contract_id, String acting_as, String party_id) {
		super();
		this.auth_grant_type = auth_grant_type;
		this.auth_client_id = auth_client_id;
		this.auth_client_secret = auth_client_secret;
		this.auth_username = auth_username;
		this.auth_password = auth_password;
		this.contract_id = contract_id;
		this.acting_as = acting_as;
		this.party_id = party_id;
	}

	public String getAuth_grant_type() {
		return auth_grant_type;
	}

	public String getAuth_client_id() {
		return auth_client_id;
	}

	public String getAuth_client_secret() {
		return auth_client_secret;
	}

	public String getAuth_username() {
		return auth_username;
	}

	public String getAuth_password() {
		return auth_password;
	}

	public String getContract_id() {
		return contract_id;
	}

	public String getActing_as() {
		return acting_as;
	}

	public String getParty_id() {
		return party_id;
	}
}
