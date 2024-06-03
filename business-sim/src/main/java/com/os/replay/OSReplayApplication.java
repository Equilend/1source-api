package com.os.replay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;

@SpringBootApplication
public class OSReplayApplication {

	public static void main(String[] args) {
		SpringApplication.run(OSReplayApplication.class, args);
	}

	@Bean
	public ConversionService conversionService() {
	    return new DefaultConversionService();
	}
	
}
