package com.os.workflow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;

@SpringBootApplication
@ConfigurationPropertiesScan
public class WorkflowApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext applicationContext = SpringApplication.run(WorkflowApplication.class, args);
		SpringApplication.exit(applicationContext);
	}

	@Bean
	public ConversionService conversionService() {
		return new DefaultConversionService();
	}

}
