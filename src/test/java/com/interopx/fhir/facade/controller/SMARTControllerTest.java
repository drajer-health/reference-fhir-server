package com.interopx.fhir.facade.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interopx.fhir.facade.model.AuthConfiguration;
import com.interopx.fhir.facade.provider.TestUtil;
import com.interopx.fhir.facade.service.AuthConfigurationService;
import com.interopx.fhir.facade.util.CommonTestUtility;

import ca.uhn.fhir.rest.api.server.RequestDetails;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class SMARTControllerTest {
	protected static final TestRestTemplate testRestTemplate = new TestRestTemplate();
	
	private MockMvc mockMvc;
	
	@Mock
    private HttpServletRequest request;
	
	@SpyBean
	AuthConfigurationService authConfigurationService;
	
	@SpyBean
	SMARTController smartController;	
	
	@Value("${api.base.url}")
	private String baseUrl;
	@MockBean
	RestTemplate resttemplate;
	@LocalServerPort
	private Integer port;
	
	
	@BeforeEach
	public void setup() {
				
		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(smartController).build();
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void getWellKnowllSMATConfigTest() throws Exception {
		
		ObjectMapper mapper = new ObjectMapper();
		when(authConfigurationService.getAuthConfiguration()).thenReturn(authConfigList());  
		MultiValueMap<String, String> headers = CommonTestUtility.getHeadersForHttpRequest();

		HttpEntity<Object> request = new HttpEntity<>(headers);
		RequestDetails requestDetails = Mockito.mock(RequestDetails.class);																																																																																																																		
		Mockito.when(requestDetails.getCompleteUrl()).thenReturn("/fhir/r4/Immunization");
		String responseBody = TestUtil.convertJsonToJsonString("bundle/tokenresponse.json");
		ResponseEntity<String> responseEntity = new ResponseEntity(responseBody, HttpStatus.OK);
		when(resttemplate.postForEntity(anyString(), any(), eq(String.class))).thenReturn(responseEntity);
		ResponseEntity<String> exchange = testRestTemplate
				.exchange("http://localhost:" + port + "/.well-known/smart-configuration", HttpMethod.GET, request, String.class);
		String expectedJson = TestUtil.convertJsonToJsonString("bundle/smart-configuration.json");
		Map<String, Object> expected = (Map<String, Object>) (mapper.readValue(expectedJson, Map.class));
		String actualResponseJson = exchange.getBody();
		Map<String, Object> actual = (Map<String, Object>) (mapper.readValue(actualResponseJson, Map.class));
		assertEquals(expected,actual);
	}
	
	@Test
	public void getAuthorizationTest() throws Exception {
		
	    when(authConfigurationService.getAuthConfiguration()).thenReturn(authConfigList());  
		MultiValueMap<String, String> headers = CommonTestUtility.getHeadersForHttpRequest();

		HttpEntity<Object> request = new HttpEntity<>(headers);
		RequestDetails requestDetails = Mockito.mock(RequestDetails.class);																																																																																																																		
		Mockito.when(requestDetails.getCompleteUrl()).thenReturn("/fhir/r4/Immunization");
		String responseBody = TestUtil.convertJsonToJsonString("bundle/tokenresponse.json");
		ResponseEntity<String> responseEntity = new ResponseEntity(responseBody, HttpStatus.OK);
		when(resttemplate.postForEntity(anyString(), any(), eq(String.class))).thenReturn(responseEntity);
		ResponseEntity<String> exchange = testRestTemplate.exchange("http://localhost:"+port+"/.well-known/openid-configuration", HttpMethod.GET,
				request, String.class);
		String actualResponseJson = exchange.getBody();
		assertEquals("200 OK",exchange.getStatusCode().toString());
		assertNotNull(actualResponseJson);
	}
	
	public List<AuthConfiguration> authConfigList() {
		List<AuthConfiguration> authConfigList = new ArrayList<>();
		AuthConfiguration authConfiguration = new AuthConfiguration();
		authConfiguration.setAuthConfigId(null);
		authConfiguration.setAuthorizationEndpointUrl("https://dev.interopx.com/ix-auth-server/api/authorize");
		authConfiguration.setClientId("client1");
		authConfiguration.setClientSecret("4065585f784fcfd8a968be9741ef29fa97979481");
		authConfiguration.setIntrospectionEndpointUrl("https://dev.interopx.com/ix-auth-server/api/token");
		authConfiguration.setTokenEndpointUrl("https://dev.interopx.com/ix-auth-server/api/token");
		authConfigList.add(authConfiguration);
		
		return authConfigList;
	}
	
	

}
