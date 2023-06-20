package com.interopx.fhir.auth.server.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConfigurationController {

	@Value("${AUTH0_DOMAIN}")
	private String auth0Domain;

	@Value("${AUTH0_CLIENT_ID}")
	private String auth0ClientClientId;

	@Value("${SECURE_API_LIST}")
	private List secureApiList;

	@Value("${OPEN_API_LIST}")
	private String[] openApiList;

	private static final Logger logger = LoggerFactory.getLogger(ConfigurationController.class);

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/config", method = RequestMethod.GET)
	public HashMap getAuth0Configuration() {
		HashMap auth0Configuration = new HashMap<>();

		HashMap configValues = new HashMap<>();
		configValues.put("domain", auth0Domain);
		configValues.put("clientId", auth0ClientClientId);

		HashMap httpInterceptor = new HashMap<>();
		ArrayList secureApis = new ArrayList<>(secureApiList);
		httpInterceptor.put("allowedList", secureApis);
		if (openApiList.length > 0) {
			HashMap openApis = new HashMap<>();
			for (String url : openApiList) {
				openApis.put("allowAnonymous", true);
				openApis.put("uri", url);
			}
			secureApis.add(openApis);
		}

		configValues.put("httpInterceptor", httpInterceptor);

		auth0Configuration.put("auth0", configValues);
		return auth0Configuration;
	}
}
