package com.onesource.demo;

import jakarta.annotation.PreDestroy;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;

@SpringBootApplication
@ConfigurationPropertiesScan
public class ApiDemoSpringApp  {

	private ConfigurableApplicationContext applicationContext;

	public static void main(String[] args) {
		ConfigurableApplicationContext applicationContext = SpringApplication.run(ApiDemoSpringApp.class, args);
//		SpringApplication.exit(applicationContext);
	}

	@Bean
	public ConversionService conversionService() {
		return new DefaultConversionService();
	}

	@PreDestroy
	public void shutdown() {
		System.out.println("Gracefully shutting down the application...");
		SpringApplication.exit(applicationContext);
	}
}
