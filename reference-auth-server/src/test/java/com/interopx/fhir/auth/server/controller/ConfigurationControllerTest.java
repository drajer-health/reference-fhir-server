package com.interopx.fhir.auth.server.controller;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

import ca.uhn.fhir.util.ReflectionUtil;

@SpringBootTest
@ActiveProfiles("test")
class ConfigurationControllerTest {

	@InjectMocks
	ConfigurationController configurationController;
	
	@BeforeEach
	void setup() {
		List secureApiList = new ArrayList();
		secureApiList.add("http://localhost:8055/*");
		secureApiList.add("https://authserver.development.arcadiaanalytics.com/ix-auth-server/*");
		
		String[] openApiList = {"http://localhost:8055/*"};
		
		ReflectionTestUtils.setField(configurationController, "auth0Domain", "authServer");
		ReflectionTestUtils.setField(configurationController, "auth0ClientClientId", "cradmo6");
		ReflectionTestUtils.setField(configurationController, "secureApiList", secureApiList);
		ReflectionTestUtils.setField(configurationController, "openApiList", openApiList);
	}
	
	@Test
	void getAuth0Configuration() {
		
		HashMap auth0Configuration = new HashMap<>();

		HashMap configValues = new HashMap<>();
		configValues.put("domain", "authServer");
		configValues.put("clientId", "cradmo6");

		HashMap httpInterceptor = new HashMap<>();
		List secureApiList = new ArrayList();
		secureApiList.add("http://localhost:8055/*");
		secureApiList.add("https://authserver.development.arcadiaanalytics.com/ix-auth-server/*");
		
		String[] openApiList = {"http://localhost:8055/*"};
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
		
		assertEquals(auth0Configuration, configurationController.getAuth0Configuration());
	}

}
