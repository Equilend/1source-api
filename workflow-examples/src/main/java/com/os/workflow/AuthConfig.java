package com.os.workflow;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

@ConfigurationProperties(prefix = "com.os.auth")
public class AuthConfig {

	private final String grant_type;
	private final String client_id;
	private final String client_secret;
	private final String username;
	private final String password;
	private final String contract_id;

	@ConstructorBinding
	public AuthConfig(String grant_type, String client_id, String client_secret, String username, String password,
			String contract_id) {
		super();
		this.grant_type = grant_type;
		this.client_id = client_id;
		this.client_secret = client_secret;
		this.username = username;
		this.password = password;
		this.contract_id = contract_id;
	}

	public String getGrant_type() {
		return grant_type;
	}

	public String getClient_id() {
		return client_id;
	}

	public String getClient_secret() {
		return client_secret;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getContract_id() {
		return contract_id;
	}

}
