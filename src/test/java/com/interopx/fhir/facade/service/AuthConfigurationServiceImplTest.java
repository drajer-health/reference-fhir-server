package com.interopx.fhir.facade.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;

import com.interopx.fhir.facade.dao.AuthConfigurationDao;
import com.interopx.fhir.facade.model.AuthConfiguration;
import com.interopx.fhir.facade.service.impl.AuthConfigurationServiceImpl;
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class AuthConfigurationServiceImplTest {
	@InjectMocks
	AuthConfigurationServiceImpl  authConfigurationService;
	@Mock
	AuthConfigurationDao authConfigurationDao;
	AuthConfiguration authConfiguration = null;
	
	@BeforeEach
	void setUp() {
		authConfiguration = new AuthConfiguration();
		authConfiguration.setAuthConfigId(123);
		authConfiguration.setAuthorizationEndpointUrl("authorizationUrl");
		authConfiguration.setClientId("clientId");
		authConfiguration.setClientSecret("secret");
		authConfiguration.setIntrospectionEndpointUrl("endpointUrl");
		authConfiguration.setTokenEndpointUrl("tokenUrl");
	}

	@Test
	void testAuthConfiguration() {
		List<AuthConfiguration> authConfigurationList = new ArrayList<>();
		authConfigurationList.add(authConfiguration);
		when(authConfigurationDao.getAuthConfiguration()).thenReturn(authConfigurationList);
		
		assertEquals(authConfigurationList,authConfigurationService.getAuthConfiguration());
		
	}
	
	@Test
	void testGetBaseURL() {
		
		assertEquals("http://localhost:8089/fhir/r4/1005", authConfigurationService.getBaseURL("http://localhost:8089/fhir/r4/1005/Organization/1234"));
	}


	@Test
	void testGetBaseCompleteURL() {
		
		assertEquals("http://localhost:8089/fhir/r4/1005", authConfigurationService.getBaseURL("http://localhost:8089/fhir/r4/1005/Encounter?patient=1137192&type=http://www.ama-assn.org/go/cpt code%7C99201"));
	}
}
