package com.interopx.fhir.validator.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.interopx.fhir.validator.model.DataSource;

@Component
public class ValidatorClient {
	private static Logger logger = LoggerFactory.getLogger(ValidatorClient.class);
	
	@Value("${datasource.serviceurl}")
	  public String dataSourceUrl;
	
	@Autowired
	RestTemplate restTemplate;
	 public DataSource getDataSourceObjectById(int dataSourceId) {
		    logger.info("**********Invoking DataSource service strated **********");
		    DataSource dataSource =
		        restTemplate.getForObject(dataSourceUrl + dataSourceId, DataSource.class);
		    logger.info("**********Invoking DataSource service End **********");
		    return dataSource;
		  }

}
