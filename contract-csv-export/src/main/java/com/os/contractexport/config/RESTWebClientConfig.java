package com.os.contractexport.config;


import java.time.Duration;
import java.time.LocalDate;
import java.time.OffsetDateTime;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.os.client.model.Rate;

import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

@Configuration
public class RESTWebClientConfig {

	@Bean
	public WebClient restWebClient() {
		ObjectMapper objectMapper = new ObjectMapper()
				.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
				.registerModule(new SimpleModule().addDeserializer(Rate.class, new RateDeserializer()))
				.registerModule(new SimpleModule().addDeserializer(LocalDate.class, new LocalDateJacksonDeserializer()))
				.registerModule(new SimpleModule().addDeserializer(OffsetDateTime.class, new OffsetDateTimeJacksonDeserializer()));
		
		ConnectionProvider provider = ConnectionProvider.builder("restConnectionBuilder")
				.maxConnections(500)
				.maxIdleTime(Duration.ofSeconds(20))
				.maxLifeTime(Duration.ofSeconds(60))
				.pendingAcquireTimeout(Duration.ofSeconds(60))
				.evictInBackground(Duration.ofSeconds(120)).build();
		
		return WebClient.builder().baseUrl("https://stageapi.equilend.com/v1/ledger")
				.exchangeStrategies(ExchangeStrategies.builder().codecs(clientDefaultCodecsConfigurer -> {
					clientDefaultCodecsConfigurer.defaultCodecs()
							.jackson2JsonEncoder(new Jackson2JsonEncoder(objectMapper, MediaType.APPLICATION_JSON));
					clientDefaultCodecsConfigurer.defaultCodecs()
							.jackson2JsonDecoder(new Jackson2JsonDecoder(objectMapper, MediaType.APPLICATION_JSON));
				}).build())
				.clientConnector(new ReactorClientHttpConnector(HttpClient.create(provider)))
				.build();
	}
	
}
