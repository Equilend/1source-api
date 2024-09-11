package com.os.events.websockets;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.os.client.model.Event;
import com.os.client.model.Events;
import com.os.events.api.AuthConfig;
import com.os.events.api.AuthToken;

import reactor.core.publisher.Mono;

@Service
@Configurable
public class ScheduledPushEvents {

	private static final Logger logger = LoggerFactory.getLogger(ScheduledPushEvents.class);

	@Autowired
	AuthConfig authConfig;

	@Autowired
	WebClient restWebClient;
	
    private final SimpMessagingTemplate simpMessagingTemplate;
    
    private final Gson gson;
    
    private AuthToken authToken;
    
    private OffsetDateTime sinceDateTime;
    
    public ScheduledPushEvents(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
		gson = new GsonBuilder()
//			    .registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())
			    .registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeTypeAdapter())
			    .create();
    }

    public void initAuthToken() {

    	logger.info(authConfig.toString());
    	
		MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
		formData.add("grant_type", authConfig.getAuth_grant_type());
		formData.add("client_id", authConfig.getAuth_client_id());
		formData.add("username", authConfig.getAuth_username());
		formData.add("password", authConfig.getAuth_password());
		formData.add("client_secret", authConfig.getAuth_client_secret());

		WebClient authClient = WebClient.create("https://stageauth.equilend.com");
		
		authToken = authClient.post()
	      .uri("/auth/realms/1Source/protocol/openid-connect/token")
	      .contentType(MediaType.APPLICATION_FORM_URLENCODED)
	      .body(BodyInserters.fromFormData(formData))
	      .retrieve()
	      .bodyToMono(AuthToken.class)
	      .block();

    }

    public void refreshAuthToken() {

		MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
		formData.add("grant_type", "refresh_token");
		formData.add("client_id", authConfig.getAuth_client_id());
		formData.add("client_secret", authConfig.getAuth_client_secret());
		formData.add("refresh_token", authToken.getRefresh_token());

		WebClient authClient = WebClient.create("https://stageauth.equilend.com");
		
		authToken = authClient.post()
	      .uri("/auth/realms/1Source/protocol/openid-connect/token")
	      .contentType(MediaType.APPLICATION_FORM_URLENCODED)
	      .body(BodyInserters.fromFormData(formData))
	      .retrieve()
	      .bodyToMono(AuthToken.class)
	      .block();

    }

    @Scheduled(fixedRate = 5000)
    public void sendMessage() {
    	
    	if (authToken == null) {
    		initAuthToken();
    		sinceDateTime = OffsetDateTime.now(ZoneId.of("UTC")).minusMinutes(30);
    		logger.debug("Token Init: " + sinceDateTime);
    	} else {
    		Long tokenTtl = (authToken.getCreateMillis() + authToken.getExpires_in()*1000) - System.currentTimeMillis();
    		logger.debug("Token TTL: " + tokenTtl);
    		if (tokenTtl <= 5000) {
        		refreshAuthToken();
        		logger.debug("Token Refresh: " + OffsetDateTime.now(ZoneId.of("UTC")));
    		}
    	}
    	
    	String eventUri = "/events?since=" + sinceDateTime.format(DateTimeFormatter.ISO_INSTANT);
    	
    	logger.debug(eventUri);
    	
		Events events = restWebClient.get().uri(eventUri)
				.headers(h -> h.setBearerAuth(authToken.getAccess_token())).retrieve()
				.onStatus(HttpStatusCode.valueOf(404)::equals, response -> {
					logger.error(HttpStatus.NOT_FOUND.toString());
					return Mono.empty();
				}).bodyToMono(Events.class).block();

		logger.debug(gson.toJson(events));

		for (Event event : events) {
			if (event.getEventDateTime().isAfter(sinceDateTime)) {
				sinceDateTime = event.getEventDateTime().plusNanos(1000000);
				logger.debug("Updated last event date time: " + sinceDateTime);
			}
			simpMessagingTemplate.convertAndSend("/topic/pushmessages", event);
		}
    }
    
}
