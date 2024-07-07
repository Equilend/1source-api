package com.os.console.api;

import java.time.OffsetDateTime;
import java.time.ZoneId;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@Configurable
public class ScheduledKeepAlive {

	private static final Logger logger = LoggerFactory.getLogger(ScheduledKeepAlive.class);

	@Autowired
	ConsoleConfig authConfig;

	public void refreshAuthToken() {

		MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
		formData.add("grant_type", "refresh_token");
		formData.add("client_id", authConfig.getAuth_client_id());
		formData.add("client_secret", authConfig.getAuth_client_secret());
		formData.add("refresh_token", ConsoleConfig.TOKEN.getRefresh_token());

		WebClient authClient = WebClient.create(authConfig.getAuth_uri());

		ConsoleConfig.TOKEN = authClient.post().uri("/auth/realms/1Source/protocol/openid-connect/token")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED).body(BodyInserters.fromFormData(formData))
				.retrieve().bodyToMono(AuthToken.class).block();

	}

	@Scheduled(fixedRate = 5000)
	public void sendMessage() {

		if (ConsoleConfig.TOKEN != null) {
			Long tokenTtl = (ConsoleConfig.TOKEN.getCreateMillis() + ConsoleConfig.TOKEN.getExpires_in() * 1000)
					- System.currentTimeMillis();
			logger.debug("Token TTL: " + tokenTtl);
			if (tokenTtl <= 5000) {
				refreshAuthToken();
				logger.debug("Token Refresh: " + OffsetDateTime.now(ZoneId.of("UTC")));
			}
		}

	}

}
