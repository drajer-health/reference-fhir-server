package com.interopx.fhir.auth.server.controller;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.interopx.fhir.auth.server.model.AuthorizationDetails;
import com.interopx.fhir.auth.server.model.Clients;
import com.interopx.fhir.auth.server.service.AuthorizationDetailsService;
import com.interopx.fhir.auth.server.service.ClientsService;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class IntrospectionEndpointTest {


	protected static final TestRestTemplate testRestTemplate = new TestRestTemplate();

	@MockBean
	RestTemplate restTemplate;

	@LocalServerPort
	private Integer port;

	@Autowired
	IntrospectionEndpoint introspectionEndpoint;

	@MockBean
	AuthorizationDetailsService authorizationDetailsService;

	@MockBean
	ClientsService clientRegistrationService;

	@Test
	void introspection() throws ParseException, IOException, URISyntaxException {
		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse response = mock(HttpServletResponse.class);

		Map<String, Object> map = new HashMap<String, Object>();

		String token = "dcf54d771abdab93bbaca51e9ebc8f39";

		MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
//        headers.add("Content-Type", "application/json");
//        headers.add("Cache-Control", "no-store");
//        headers.add("Pragma", "no-cache");
		HttpEntity<Object> entity = new HttpEntity<>(headers);

		String client_id = "n-WW3PPCp-41DdYX2-i0Ksihe123";
		String client_secret = "secret";
		String expiry = "2030-05-12 03:15:20";
		String scope = "scope";
		String userId = "87246";
		String contactName = "navya";
		String customerId = "cradmo";
		String centerId = "127";
		String isBackendClient = "true";
		String sub = "navya";
		String grantType = "authorization_code";
		String tokenType = "Bearer";

		AuthorizationDetails authData = new AuthorizationDetails();
		authData.setClientId(client_id);
		authData.setExpiry(expiry);
		authData.setScope(scope);
		when(authorizationDetailsService.getAuthorizationByAccessToken(Mockito.anyString())).thenReturn(authData);

		Clients clients = new Clients();
		clients.setCenterId(centerId);
		clients.setCustomerId(customerId);
		clients.setContactName(contactName);
		when(clientRegistrationService.getClient(Mockito.anyString())).thenReturn(clients);

		map.put("sub", null);
		map.put("centerId", centerId);
		map.put("grant_type", grantType);
		map.put("scope", scope);
		map.put("customerId", customerId);
		map.put("active", isBackendClient);
		map.put("exp", 1904786120);
		map.put("token_type", tokenType);
		map.put("iat", null);
		map.put("client_id", client_id);

		String url = "http://localhost:" + port + "/api/introspect";
		Map<String, String> params = new HashMap<String, String>();
		params.put("token", "dcf54d771abdab93bbaca51e9ebc8f39");
		URI uri = UriComponentsBuilder.fromUriString(url).buildAndExpand(params).toUri();
		uri = UriComponentsBuilder.fromUri(uri).queryParam("token", "dcf54d771abdab93bbaca51e9ebc8f39").build().toUri();
		ResponseEntity<Map> exchange = this.testRestTemplate.exchange(uri, HttpMethod.POST, entity, Map.class);
		String expected = new ObjectMapper().writeValueAsString(map);
		System.out.println(expected);
		System.out.println(exchange.getBody());
		String actual = new ObjectMapper().writeValueAsString(exchange.getBody());
		assertEquals(expected, actual);

	}

	@Test
	void getScopeByAccessToken() throws IOException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();

		String scopes = "system/AllergyIntolerance.read,system/Patient.read,system/Encounter.read,system/Procedure.read,system/MedicationRequest.read,system/Immunization.read,system/Observation.read,system/Condition.read,system/ServiceRequest.read";
		request.setParameter("token", "492e5e10d7cff09b097dc1c92a6de91bhgh");
		AuthorizationDetails authData = new AuthorizationDetails();
		authData.setScope(scopes);
		authData.setAccessToken("492e5e10d7cff09b097dc1c92a6de91bhgh");
		when(authorizationDetailsService.getAuthorizationByAccessToken(Mockito.anyString())).thenReturn(authData);
		assertEquals(authData.getScope(), introspectionEndpoint.getScopeByAccessToken(request, response));

	}

	@Test
	void introspection1() throws ParseException, IOException, URISyntaxException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();

		request.setParameter("patientId", "59662");

		Map<String, Object> map = new HashMap<String, Object>();

		String token = "dcf54d771abdab93bbaca51e9ebc8f39";

//        when(request.getParameter("patientId")).thenReturn(patientId);
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
//        headers.add("Content-Type", "application/json");
//        headers.add("Cache-Control", "no-store");
//        headers.add("Pragma", "no-cache");
		HttpEntity<Object> entity = new HttpEntity<>(headers);

		String client_id = "n-WW3PPCp-41DdYX2-i0Ksihe123";
		String client_secret = "secret";
		String expiry = "2030-05-12 03:15:20";
		String scope = "scope";
		String userId = "87246";
		String contactName = "navya";
		String customerId = "cradmo";
		String centerId = "127";
		String isBackendClient = "true";

		AuthorizationDetails authData = new AuthorizationDetails();
		authData.setClientId(client_id);
		authData.setExpiry(expiry);
		authData.setScope(scope);
		authData.setLaunchPatientId("59662");
		when(authorizationDetailsService.getAuthorizationByAccessToken(Mockito.anyString())).thenReturn(authData);

		Clients clientDetails = new Clients();
		clientDetails.setCenterId(centerId);
		clientDetails.setCustomerId(customerId);
		clientDetails.setContactName(contactName);
		clientDetails.setDynamicClient(true);
		when(clientRegistrationService.getClient(Mockito.anyString())).thenReturn(clientDetails);

//		Mockito.when(clientDetails.isDynamicClient()).thenReturn(true);

		map.put("sub", null);
		map.put("centerId", centerId);
		map.put("grant_type", "authorization_code");
		map.put("scope", scope);
		map.put("customerId", customerId);
		map.put("active", "true");
		map.put("exp", 1904786120);
		map.put("token_type", "Bearer");
		map.put("iat", null);
		map.put("client_id", client_id);

		String url = "http://localhost:" + port + "/api/introspect";
		Map<String, String> params = new HashMap<String, String>();
		params.put("token", "dcf54d771abdab93bbaca51e9ebc8f39");
		URI uri = UriComponentsBuilder.fromUriString(url).buildAndExpand(params).toUri();
		uri = UriComponentsBuilder.fromUri(uri).queryParam("token", "dcf54d771abdab93bbaca51e9ebc8f39")
				.queryParam("patientId", "59662").build().toUri();
		ResponseEntity<Map> exchange = this.testRestTemplate.exchange(uri, HttpMethod.POST, entity, Map.class);
		String expected = new ObjectMapper().writeValueAsString(map);
		System.out.println(expected);
		System.out.println(exchange.getBody());
		String actual = new ObjectMapper().writeValueAsString(exchange.getBody());
		assertEquals(expected, actual);

	}

	@Test
	void introspection2() throws ParseException, IOException, URISyntaxException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();

		Map<String, Object> map = new HashMap<String, Object>();

		String token = "66fec5004cb6397974d8f4dbb142236e";

		MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
		HttpEntity<Object> entity = new HttpEntity<>(headers);

		map.put("grant_type", "authorization_code");
		map.put("active", "true");
		map.put("token_type", "Bearer");

		String url = "http://localhost:" + port + "/api/introspect";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("token", "dcf54d771abdab93bbaca51e9ebc8f39");
		URI uri = UriComponentsBuilder.fromUriString(url).buildAndExpand(params).toUri();
		uri = UriComponentsBuilder.fromUri(uri).queryParam("token", "66fec5004cb6397974d8f4dbb142236e").build().toUri();
		ResponseEntity<Map> exchange = this.testRestTemplate.exchange(uri, HttpMethod.POST, entity, Map.class);
		String expected = new ObjectMapper().writeValueAsString(map);
		System.out.println(expected);
		System.out.println(exchange.getBody());
		String actual = new ObjectMapper().writeValueAsString(exchange.getBody());
		assertNotNull(actual);

	}

}
