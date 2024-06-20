package com.os.events.api;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

@ConfigurationProperties(prefix = "com.os.config")
public class AuthConfig {

	private final String auth_grant_type;
	private final String auth_client_id;
	private final String auth_client_secret;
	private final String auth_username;
	private final String auth_password;
	private final String party_id;
	private final String acting_as;

	@ConstructorBinding
	public AuthConfig(String auth_grant_type, String auth_client_id, String auth_client_secret,
			String auth_username, String auth_password, String party_id, String acting_as) {
		super();
		this.auth_grant_type = auth_grant_type;
		this.auth_client_id = auth_client_id;
		this.auth_client_secret = auth_client_secret;
		this.auth_username = auth_username;
		this.auth_password = auth_password;
		this.party_id = party_id;
		this.acting_as = acting_as;
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

	public String getParty_id() {
		return party_id;
	}

	public String getActing_as() {
		return acting_as;
	}

}
