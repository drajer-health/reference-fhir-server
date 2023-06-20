package com.interopx.fhir.facade.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;

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
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.interopx.fhir.facade.model.RequestResponseLog;
import com.interopx.fhir.facade.util.CommonTestUtility;
import com.interopx.fhir.facade.util.CommonUtil;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class HealthStatusControllerTest {
	protected static final TestRestTemplate testRestTemplate = new TestRestTemplate();

	@SuppressWarnings("unused")
	private MockMvc mockMvc;
	@Value("${jdbc.driverClassName}")
	private String jdbcDriverClassName;
	@Mock
	private HttpServletRequest request;
	RequestResponseLog response = null;
	@MockBean
	RestTemplate resttemplate;
	@LocalServerPort
	private Integer port;
	@SpyBean
	CommonUtil commonUtil;
	@Value("${hapi.fhir.server.path}")
	private String hapiFhirServerPath;
	@SpyBean
	HealthStatusController healthStatusController;

	@SuppressWarnings("deprecation")
	@BeforeEach
	public void setup() {

		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(this).build();
	}

	@Test
	public void checkHealthServiceStatusTest() throws Exception { 
		MultiValueMap<String, String> headers = CommonTestUtility.getHeadersForHttpRequest();
		HttpEntity<Object> request = new HttpEntity<>(headers);
		String baseUrl = "http://localhost:" + port;
		Mockito.when(commonUtil.getAzureConfigurationServiceFhirServerUrl(anyString())).thenReturn(baseUrl);
		ResponseEntity<String> exchange = testRestTemplate
				.exchange("http://localhost:" + port + "/health/service/status", HttpMethod.GET, request, String.class);
		assertEquals("200 OK", exchange.getStatusCode().toString());
		assertEquals("Success", exchange.getBody());
	}


	@Test
	public void checkHealthServiceStatusExceptionTest() throws Exception {
		MultiValueMap<String, String> headers = CommonTestUtility.getHeadersForHttpRequest();

		HttpEntity<Object> request = new HttpEntity<>(headers);
		Mockito.when(commonUtil.getAzureConfigurationServiceFhirServerUrl(anyString())).thenReturn(null);
		ResponseEntity<String> exchange = testRestTemplate
				.exchange("http://localhost:" + port + "/health/service/status", HttpMethod.GET, request, String.class);
		assertEquals("400 BAD_REQUEST", exchange.getStatusCode().toString());
		assertEquals("Failed", exchange.getBody());
	}

	@Test
	public void checkDataBaseStatusTest() throws Exception {
		MultiValueMap<String, String> headers = CommonTestUtility.getHeadersForHttpRequest();
		ReflectionTestUtils.setField(healthStatusController, "jdbcDriverClassName", jdbcDriverClassName);
		HttpEntity<Object> request = new HttpEntity<>(headers);
		ResponseEntity<String> exchange = testRestTemplate
				.exchange("http://localhost:" + port + "/health/db/status", HttpMethod.GET, request, String.class);
		assertEquals("200 OK", exchange.getStatusCode().toString());
		assertEquals("Success", exchange.getBody());
	}

	@Test
	public void checkDataBaseStatusExceptionTest() throws Exception {
		MultiValueMap<String, String> headers = CommonTestUtility.getHeadersForHttpRequest();
		ReflectionTestUtils.setField(healthStatusController, "jdbcDriverClassName", "xyz");
		HttpEntity<Object> request = new HttpEntity<>(headers);
		ResponseEntity<String> exchange = testRestTemplate
				.exchange("http://localhost:" + port + "/health/db/status", HttpMethod.GET, request, String.class);
		assertEquals("400 BAD_REQUEST", exchange.getStatusCode().toString());
		assertEquals("Failed", exchange.getBody());
	}
}
