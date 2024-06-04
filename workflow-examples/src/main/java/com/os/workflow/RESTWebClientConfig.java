package com.os.workflow;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class RESTWebClientConfig {

	@Bean
	public WebClient restWebClient() {
		return WebClient.create("https://stageapi.equilend.com/v1/ledger");
	}
	
}
