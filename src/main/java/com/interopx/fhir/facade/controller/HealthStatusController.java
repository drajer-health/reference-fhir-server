package com.interopx.fhir.facade.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.r4.model.CapabilityStatement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.interopx.fhir.facade.util.CommonUtil;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
/**
 * This class checks the overall health condition of the application and database connectivity
 * @author xyram
 *
 */
@RestController
@RequestMapping("/health")
public class HealthStatusController {
	private static final Logger logger = LoggerFactory.getLogger(HealthStatusController.class);

	private static final String practiceId = "10025";

	@Autowired
	FhirContext fhirContext;
	@Value("${hapi.fhir.server.path}")
	private String hapiFhirServerPath;
	@Value("${jdbc.driverClassName}")
	private String jdbcDriverClassName;
	@Value("${jdbc.url}")
	private String jdbcUrl;
	@Value("${jdbc.username}")
	private String jdbcUserName;
	@Value("${jdbc.password}")
	private String jdbcPassword;
	@Autowired
	CommonUtil commonUtil;

	/**
	 * This method checks the facade server is up and running by making request call
	 * to metadata url
	 * 
	 * @param request
	 * @return
	 */
	@GetMapping(value = "/service/status")
	public ResponseEntity<?> checkHealthServiceStatus(HttpServletRequest request) {
		ResponseEntity<?> response = null;
		try {
			String baseUrl = commonUtil.getAzureConfigurationServiceFhirServerUrl("FhirFacadeServerUrl");
			if(StringUtils.isNotBlank(baseUrl)) {
				logger.debug("Using the Server URL from DB::::{}",baseUrl);
				String fhirPath = hapiFhirServerPath.replace("*", "");
				String fhirMetaUrl = baseUrl + fhirPath + practiceId;
				logger.debug(" fhirMetaDataUrl {}", fhirMetaUrl);
				IGenericClient client = fhirContext.newRestfulGenericClient(fhirMetaUrl);
				if (client != null) {
					CapabilityStatement resource = client.capabilities().ofType(CapabilityStatement.class).execute();
					if (resource != null && !resource.isEmpty()) {
						logger.debug("Name: {}" ,resource.getClass().getCanonicalName());
						response = new ResponseEntity<>("Success", HttpStatus.OK);
					} else {
						response = new ResponseEntity<>("Failed", HttpStatus.BAD_REQUEST);
					}
				} else {
					response = new ResponseEntity<>("Failed", HttpStatus.BAD_REQUEST);
				}
			}else {
				response = new ResponseEntity<>("Failed", HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			logger.error("Exception in checkHealthServiceStatus() of  HealthStatusController class: ", e);
		}
		return response;
	}

	/**
	 * This method checks whether data base is up and running
	 * 
	 * @param request
	 * @return
	 */
	@GetMapping(value = "/db/status")
	public ResponseEntity<?> checkDataBaseStatus(HttpServletRequest request) {
		ResponseEntity<?> response = null;
		Connection connection = null;
		try {
			String url = jdbcUrl + ";" + "user=" + jdbcUserName + ";" + "password=" + jdbcPassword + ";";
			Class.forName(jdbcDriverClassName);
			logger.debug("Trying to connect database");
			connection = DriverManager.getConnection(url);
			logger.debug("Connection Established Successfull and the DATABASE NAME IS: {}"
					, connection.getMetaData().getDatabaseProductName());
			response = new ResponseEntity<>("Success", HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Unable to make connection with DB: ", e);
			response = new ResponseEntity<>("Failed", HttpStatus.BAD_REQUEST);
		}
		finally {
			if(connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					logger.error("Exception while closing DB Connection: ", e);
				}
			}
		}
		return response;
	}
}
