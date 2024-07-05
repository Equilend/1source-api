package com.os.console.api.tasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.os.console.api.AuthConfig;
import com.os.console.api.AuthToken;

public class AuthTask implements Runnable {

	private static final Logger logger = LoggerFactory.getLogger(AuthTask.class);

	AuthConfig authConfig;

	public AuthTask(AuthConfig authConfig) {
		this.authConfig = authConfig;
	}
	
	@Override
	public void run() {
		
		MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
		formData.add("grant_type", "password");
		formData.add("client_id", authConfig.getAuth_client_id());
		formData.add("username", authConfig.getAuth_username());
		formData.add("password", authConfig.getAuth_password());
		formData.add("client_secret", authConfig.getAuth_client_secret());

		WebClient authClient = WebClient.create(authConfig.getAuth_uri());
		
		AuthToken ledgerToken = authClient.post()
	      .uri("/auth/realms/1Source/protocol/openid-connect/token")
	      .contentType(MediaType.APPLICATION_FORM_URLENCODED)
	      .body(BodyInserters.fromFormData(formData))
	      .retrieve()
	      .bodyToMono(AuthToken.class)
	      .block();
		
		if (ledgerToken != null) {
			AuthConfig.TOKEN = ledgerToken;
			logger.debug("Ledger access token: " + AuthConfig.TOKEN.getAccess_token());
		} else {
			logger.warn("Not authorized");
		}
		
	}
}
