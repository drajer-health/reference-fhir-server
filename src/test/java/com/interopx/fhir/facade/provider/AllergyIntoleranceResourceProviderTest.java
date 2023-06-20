package com.interopx.fhir.facade.provider;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import org.json.JSONException;
import org.json.JSONObject;
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
import com.interopx.fhir.facade.util.CommonTestUtility;
import com.interopx.fhir.facade.util.Status;

import ca.uhn.fhir.context.FhirContext;
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class AllergyIntoleranceResourceProviderTest {
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
	
	@MockBean
	AuthConfigurationService authConfigurationService;
	
	@LocalServerPort
	private Integer port;

	
	@BeforeEach
	private void setUp() throws IOException, JSONException {
		String jsonString = TestUtil.convertJsonToJsonString("bundle/allergyintolerance.json");
		response = new RequestResponseLog();
		response.setPayload(jsonString);
		response.setRequestId("4bf0f3e7-7b0e-4ed1-b486-0d3bf4e47ac9");
		response.setStatus("Completed");
		response.setTimestamp(new Date());	
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes", "static-access" })
	@Test
	 void readTest() throws URISyntaxException, InterruptedException, IOException {
		
		doNothing().when(azureQueueService).sendMessage(anyString(),anyString());
		doReturn(response).when(azureQueueDao).getRequestResponseLogById(anyString());
		ReflectionTestUtils.setField(azureQueueService, "threadTimeOutTime", 30000);
		
		MultiValueMap<String, String> headers = CommonTestUtility.getHeadersForHttpRequest();
		HttpEntity<Object> request = new HttpEntity<>(headers);
		Mockito.when(authConfigurationService.getBaseURL(anyString())).thenReturn("http://localhost:8090/fhir/r4/1005");
		String responseBody = TestUtil.convertJsonToJsonString("bundle/tokenresponse.json");

		when(authConfigurationService.getAuthConfiguration()).thenReturn(TestUtil.getAuthConfiguration());		
		
		ResponseEntity<String> responseEntity = new ResponseEntity(responseBody, HttpStatus.OK);
		when(resttemplate.postForEntity(anyString(), any(), eq(String.class))).thenReturn(responseEntity);																																																																																																																	

		ResponseEntity<String> exchange = this.testRestTemplate.exchange(
				"http://localhost:"+port+"/fhir/r4/1005"+"/AllergyIntolerance/1234", HttpMethod.GET, request, String.class);
		String body = exchange.getBody();
		assertEquals("200 OK",exchange.getStatusCode().toString());
		assertNotNull(body);
	
	}	
	@SuppressWarnings({ "rawtypes", "unchecked", "static-access" })
	@Test
	 void searchTest() throws URISyntaxException, InterruptedException, IOException {
		
		doNothing().when(azureQueueService).sendMessage(anyString(),anyString());
		doReturn(response).when(azureQueueDao).getRequestResponseLogById(anyString());
		ReflectionTestUtils.setField(azureQueueService, "threadTimeOutTime", 30000);
		
		MultiValueMap<String, String> headers = CommonTestUtility.getHeadersForHttpRequest();
		HttpEntity<Object> request = new HttpEntity<>(headers);
		Mockito.when(authConfigurationService.getBaseURL(anyString())).thenReturn("http://localhost:8090/fhir/r4/1005");
		String responseBody = TestUtil.convertJsonToJsonString("bundle/tokenresponse.json");
		
		when(authConfigurationService.getAuthConfiguration()).thenReturn(TestUtil.getAuthConfiguration());																																																																																																																																																																																																																																	

		ResponseEntity<String> responseEntity = new ResponseEntity(responseBody, HttpStatus.OK);
		when(resttemplate.postForEntity(anyString(), any(), eq(String.class))).thenReturn(responseEntity);																																																																																																																																																																																																																																		

		ResponseEntity<String> exchange = this.testRestTemplate.exchange(
				"http://localhost:"+port+"/fhir/r4/1005"+"/AllergyIntolerance?patient=1137192", HttpMethod.GET, request, String.class);
		String body = exchange.getBody();
		assertEquals("200 OK",exchange.getStatusCode().toString());
		assertNotNull(body);
	
	}
	@SuppressWarnings({ "rawtypes", "unchecked", "static-access" })
	@Test
	void searchTestForOperationOutComeForInvalidRequest()
			throws URISyntaxException, InterruptedException, IOException, JSONException {

		doNothing().when(azureQueueService).sendMessage(anyString(),anyString());
		doReturn(response).when(azureQueueDao).getRequestResponseLogById(anyString());
		ReflectionTestUtils.setField(azureQueueService, "threadTimeOutTime", 30000);

		MultiValueMap<String, String> headers = CommonTestUtility.getHeadersForHttpRequest();
		HttpEntity<Object> request = new HttpEntity<>(headers);
		Mockito.when(authConfigurationService.getBaseURL(anyString())).thenReturn("http://localhost:8090/fhir/r4/1005");
		String responseBody = TestUtil.convertJsonToJsonString("bundle/tokenresponse.json");
		
		when(authConfigurationService.getAuthConfiguration()).thenReturn(TestUtil.getAuthConfiguration());		
		
		ResponseEntity<String> responseEntity = new ResponseEntity(responseBody, HttpStatus.OK);
		when(resttemplate.postForEntity(anyString(), any(), eq(String.class))).thenReturn(responseEntity);																																																																																																																																																																																																																																

		ResponseEntity<String> exchange = this.testRestTemplate
				.exchange("http://localhost:"+port+"/fhir/r4/1005" + "/AllergyIntolerance?clinical-status=http://terminology.hl7.org/CodeSystem/allergyintolerance-clinical%7Cactive", HttpMethod.GET, request, String.class);
		String body = exchange.getBody();
		JSONObject obj = new JSONObject(body);
		assertEquals("OperationOutcome", obj.getString("resourceType"));
		assertEquals("400 BAD_REQUEST",exchange.getStatusCode().toString());

	}
	
	@SuppressWarnings({ "rawtypes", "unchecked", "static-access" })
	@Test
	void searchTestForOperationOutComeForRequestTimeOut()
			throws URISyntaxException, InterruptedException, IOException, JSONException, ExecutionException {

		doNothing().when(azureQueueService).sendMessage(anyString(),anyString());
		RequestResponseLog resLog = new RequestResponseLog();
		resLog.setRequestId("12345");
		resLog.setTimestamp(new Date());
		resLog.setPayload("");
		resLog.setStatus(Status.ABORTED.toString());
		ReflectionTestUtils.setField(azureQueueService, "threadTimeOutTime", 3000);
		MultiValueMap<String, String> headers = CommonTestUtility.getHeadersForHttpRequest();
		HttpEntity<Object> request = new HttpEntity<>(headers);
		Mockito.when(authConfigurationService.getBaseURL(anyString())).thenReturn("http://localhost:8090/fhir/r4/1005");
		String responseBody = TestUtil.convertJsonToJsonString("bundle/tokenresponse.json");
		
		when(authConfigurationService.getAuthConfiguration()).thenReturn(TestUtil.getAuthConfiguration());		
		
		ResponseEntity<String> responseEntity = new ResponseEntity(responseBody, HttpStatus.OK);
		when(resttemplate.postForEntity(anyString(), any(), eq(String.class))).thenReturn(responseEntity);																																																																																																																																																																																																																																		

		ResponseEntity<String> exchange = this.testRestTemplate.exchange("http://localhost:"+port+"/fhir/r4/1005" + "/AllergyIntolerance?patient=1137192",
				HttpMethod.GET, request, String.class);
		String body = exchange.getBody();
		JSONObject obj = new JSONObject(body);
		assertEquals("OperationOutcome", obj.getString("resourceType"));
		assertEquals("408 REQUEST_TIMEOUT",exchange.getStatusCode().toString());
	}	
	
	@SuppressWarnings({ "rawtypes", "unchecked", "static-access" })
	@Test
	 void testOperationOutComeForSearch() throws URISyntaxException, InterruptedException, IOException {
		String jsonString = TestUtil.convertJsonToJsonString("bundle/operationoutcome.json");
		RequestResponseLog response = new RequestResponseLog();
		response = new RequestResponseLog();
		response.setPayload(jsonString);
		response.setRequestId("4bf0f3e7-7b0e-4ed1-b486-0d3bf4e47ac9");
		response.setStatus("Completed");
		response.setTimestamp(new Date());	
		
		doNothing().when(azureQueueService).sendMessage(anyString(),anyString());
		doReturn(response).when(azureQueueDao).getRequestResponseLogById(anyString());
		ReflectionTestUtils.setField(azureQueueService, "threadTimeOutTime", 30000);
		
		MultiValueMap<String, String> headers = CommonTestUtility.getHeadersForHttpRequest();
		HttpEntity<Object> request = new HttpEntity<>(headers);
		Mockito.when(authConfigurationService.getBaseURL(anyString())).thenReturn("http://localhost:8090/fhir/r4/1005");
		String responseBody = TestUtil.convertJsonToJsonString("bundle/tokenresponse.json");
		when(authConfigurationService.getAuthConfiguration()).thenReturn(TestUtil.getAuthConfiguration());		

		ResponseEntity<String> responseEntity = new ResponseEntity(responseBody, HttpStatus.OK);
		when(resttemplate.postForEntity(anyString(), any(), eq(String.class))).thenReturn(responseEntity);
		ResponseEntity<String> exchange = this.testRestTemplate.exchange(
				"http://localhost:"+port+"/fhir/r4/1005"+"/AllergyIntolerance?patient=1137192", HttpMethod.GET, request, String.class);
		String body = exchange.getBody();
		JSONObject obj = new JSONObject(body);
		assertEquals("OperationOutcome", obj.getString("resourceType"));
		assertEquals("400 BAD_REQUEST",exchange.getStatusCode().toString());		
	
	}
	@SuppressWarnings({ "rawtypes", "unchecked", "static-access" })
	@Test
	 void testOperationOutComeForRead() throws URISyntaxException, InterruptedException, IOException {
		String jsonString = TestUtil.convertJsonToJsonString("bundle/operationoutcome.json");
		RequestResponseLog response = new RequestResponseLog();
		response = new RequestResponseLog();
		response.setPayload(jsonString);
		response.setRequestId("4bf0f3e7-7b0e-4ed1-b486-0d3bf4e47ac9");
		response.setStatus("Completed");
		response.setTimestamp(new Date());	
		
		doNothing().when(azureQueueService).sendMessage(anyString(),anyString());
		doReturn(response).when(azureQueueDao).getRequestResponseLogById(anyString());
		ReflectionTestUtils.setField(azureQueueService, "threadTimeOutTime", 30000);
		
		MultiValueMap<String, String> headers = CommonTestUtility.getHeadersForHttpRequest();
		HttpEntity<Object> request = new HttpEntity<>(headers);
		Mockito.when(authConfigurationService.getBaseURL(anyString())).thenReturn("http://localhost:8090/fhir/r4/1005");
		String responseBody = TestUtil.convertJsonToJsonString("bundle/tokenresponse.json");
		when(authConfigurationService.getAuthConfiguration()).thenReturn(TestUtil.getAuthConfiguration());		

		ResponseEntity<String> responseEntity = new ResponseEntity(responseBody, HttpStatus.OK);
		when(resttemplate.postForEntity(anyString(), any(), eq(String.class))).thenReturn(responseEntity);
		ResponseEntity<String> exchange = this.testRestTemplate.exchange(
				"http://localhost:"+port+"/fhir/r4/1005"+"/AllergyIntolerance/1234", HttpMethod.GET, request, String.class);
		String body = exchange.getBody();
		JSONObject obj = new JSONObject(body);
		assertEquals("OperationOutcome", obj.getString("resourceType"));
		assertEquals("400 BAD_REQUEST",exchange.getStatusCode().toString());		
	
	}

}
