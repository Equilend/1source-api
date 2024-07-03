package com.os.console.api;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

@ConfigurationProperties(prefix = "com.os.config")
public class AuthConfig {

	public static AuthToken TOKEN = null;

	private final String auth_uri;
	private final String auth_client_id;
	private final String auth_client_secret;

	private String auth_username;
	private String auth_password;
	private String party_id;

	private final String api_uri;

	@ConstructorBinding
	public AuthConfig(String auth_uri, String auth_client_id, String auth_client_secret, String auth_username,
			String auth_password, String party_id, String api_uri) {
		super();
		this.auth_uri = auth_uri;
		this.auth_client_id = auth_client_id;
		this.auth_client_secret = auth_client_secret;
		this.auth_username = auth_username;
		this.auth_password = auth_password;
		this.party_id = party_id;
		this.api_uri = api_uri;
	}

	public String getAuth_uri() {
		return auth_uri;
	}

	public String getAuth_username() {
		return auth_username;
	}

	public void setAuth_username(String auth_username) {
		this.auth_username = auth_username;
	}

	public String getAuth_password() {
		return auth_password;
	}

	public void setAuth_password(String auth_password) {
		this.auth_password = auth_password;
	}

	public String getParty_id() {
		return party_id;
	}

	public void setParty_id(String party_id) {
		this.party_id = party_id;
	}

	public String getAuth_client_id() {
		return auth_client_id;
	}

	public String getAuth_client_secret() {
		return auth_client_secret;
	}

	public String getApi_uri() {
		return api_uri;
	}

}
