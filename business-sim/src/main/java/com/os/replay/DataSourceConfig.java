package com.os.replay;


import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class DataSourceConfig {

	@Bean
	@ConfigurationProperties("spring.datasource.load")
	public DataSourceProperties mdDataSourceProperties() {
		return new DataSourceProperties();
	}
	
	@Bean
	public DataSource mdDataSource() {
	    return mdDataSourceProperties()
	      .initializeDataSourceBuilder()
	      .build();
	}
	
	@Bean
	public JdbcTemplate mdJdbcTemplate(@Qualifier("mdDataSource") DataSource dataSource) {
	    return new JdbcTemplate(dataSource);
	}
}
