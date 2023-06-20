package com.interopx.fhir.facade.provider;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Date;

import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
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
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.interopx.fhir.facade.dao.AzureQueueDao;
import com.interopx.fhir.facade.model.RequestResponseLog;
import com.interopx.fhir.facade.service.AuthConfigurationService;
import com.interopx.fhir.facade.service.AzureQueueService;

import ca.uhn.fhir.rest.api.server.RequestDetails;
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class CapabilityStatementResourceProviderTest {
	protected static final TestRestTemplate testRestTemplate = new TestRestTemplate();
	@Value("${api.base.url}")
	private String baseUrl;

	@SpyBean
	AzureQueueService azureQueueService;
	@SpyBean
	AzureQueueDao azureQueueDao;
	RequestResponseLog response = null;
	@MockBean
	RestTemplate resttemplate;
	@LocalServerPort
	private Integer port;
	@SpyBean
	AuthConfigurationService authConfigurationService;
	@BeforeEach
	private void setUp() throws IOException, JSONException {
		String jsonString = TestUtil.convertJsonToJsonString("bundle/immunization.json");
		response = new RequestResponseLog();
		response.setPayload(jsonString);
		response.setRequestId("4bf0f3e7-7b0e-4ed1-b486-0d3bf4e47ac9");
		response.setStatus("Completed");
		response.setTimestamp(new Date());
	}

	@Test
	void test() throws InterruptedException, IOException {
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
		headers.add("Accept", "application/fhir+json;fhirVersion=4.0");
		headers.add("Content-Type", "application/fhir+json;fhirVersion=4.0");
		headers.add("X-AUTH-TOKEN", "test.test.test");
		headers.add("Authorization", "bearer test.test.test");
		HttpEntity<Object> request = new HttpEntity<>(headers);
		RequestDetails requestDetails = Mockito.mock(RequestDetails.class);																																																																																																																		
		Mockito.when(requestDetails.getCompleteUrl()).thenReturn("/fhir/r4/Immunization");
		String responseBody = TestUtil.convertJsonToJsonString("bundle/tokenresponse.json");
		when(authConfigurationService.getAuthConfiguration()).thenReturn(TestUtil.getAuthConfiguration());
		ResponseEntity<String> responseEntity = new ResponseEntity(responseBody, HttpStatus.OK);
		when(resttemplate.postForEntity(anyString(), any(), eq(String.class))).thenReturn(responseEntity);
		ResponseEntity<String> exchange = testRestTemplate.exchange("http://localhost:"+port+"/fhir/r4/1005" + "/metadata", HttpMethod.GET,
				request, String.class);
		String body = exchange.getBody();
		assertEquals("200 OK",exchange.getStatusCode().toString());
		assertNotNull(body);
	
	}

}
