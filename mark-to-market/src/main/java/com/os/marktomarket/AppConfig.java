package com.os.marktomarket;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

@ConfigurationProperties(prefix = "com.os.config")
public class AppConfig {

	private final String api_uri;

	private final String auth_uri;
	private final String auth_client_id;
	private final String auth_client_secret;
	private final String auth_username;
	private final String auth_password;

	@ConstructorBinding
	public AppConfig(String api_uri, String auth_uri, String auth_client_id, String auth_client_secret,
			String auth_username, String auth_password) {
		super();
		this.api_uri = api_uri;
		this.auth_uri = auth_uri;
		this.auth_client_id = auth_client_id;
		this.auth_client_secret = auth_client_secret;
		this.auth_username = auth_username;
		this.auth_password = auth_password;
	}

	public String getApi_uri() {
		return api_uri;
	}

	public String getAuth_uri() {
		return auth_uri;
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

}
