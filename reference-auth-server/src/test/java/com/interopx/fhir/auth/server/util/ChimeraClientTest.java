package com.interopx.fhir.auth.server.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

@SpringBootTest
@ActiveProfiles("test")
class ChimeraClientTest {

	@InjectMocks
	ChimeraClient chimeraClient;

	@Mock
	RestTemplate restTemplate;

	protected static final TestRestTemplate testRestTemplate = new TestRestTemplate();

	@Test
	void getAuth0UserDetailsException() {
		assertThrows(ResponseStatusException.class, () -> {
			chimeraClient.getAuth0UserDetails("wsedrfghjsdfghjjkgfdxcfghg");
		});
	}

//	@Test
//	void getAuth0UserDetails() {
//
//		ResponseEntity<HashMap> exchange = null;
//
//		HashMap auth0UserDetailsMap = null;
//
//		MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
//		
//		headers.add("Accept", "application/fhir+json;fhirVersion=4.0");
//		headers.add("Content-Type", "application/octet-stream");
////		headers.add("Authorization", "bearer test");
//		headers.add("authorization",
//				"Bearer eyJhbGciOiJkaXIiLCJlbmMiOiJBMjU2R0NNIiwiaXNzIjoiaHR0cHM6Ly9kZXYtMnlxNHl4emcudXMuYXV0aDAuY29tLyJ9..NZrFolAwc_8NJuxr.v4QCd2GzwELvmbsbDpknYingrx4qq-qh20Gf7y0SIORvooIcqHa2g4xKFqce1SMTc4Y1ZeuyavWUktnRo72GetjOtwC4dMaa-m5xXqsCL0V2TM_IielhTPVEH70i57kBqlPwKYyAJ2YpTCR2WWU6LOOITyv9IEVb4Hx1e-vbCoziihHvinCirs2oZRU-bmhkO3mz4j-N-XdFD1OA6SiJsvoWkxu1Jvh6aUTia1HlENT5d45lC7Qd1U64DKFe2LNvkMvVCWgSJAGcJUM8JlCQ4IDjUNN9eDHiZ7BDKh1C1AJBugqfoGdUqnaBQy_32YOj4DIuXcincCAPOVMm0EyHgL6KElnuw4EMaDC3H7nMgy1wFW7K_gBRz9d1Fwwp4bmYkw.soQN9aaUCnb9HZOI13bwyw");
//		HttpEntity<Object> request = new HttpEntity<>(headers);
//		
////		List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();        
////		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
////
////		converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));        
////		messageConverters.add(converter);  
////		restTemplate.setMessageConverters(messageConverters); 
//
//		exchange = this.testRestTemplate.exchange("https://default-development-arcadia.us.auth0.com" + "/userinfo",
//				HttpMethod.GET, request, HashMap.class);
//
////		auth0UserDetailsMap = exchange.getBody();
//
////		Mockito.when(restTemplate.exchange("https://default-development-arcadia.us.auth0.com" + "/userinfo", HttpMethod.GET, request, HashMap.class)).thenReturn(exchange);
//		assertEquals(auth0UserDetailsMap, chimeraClient.getAuth0UserDetails("fhsdxfhjkdfghkdfghrgh"));
//	}

	@Test
	void getRoleFromResponseException() {
		HashMap map = new HashMap<>();
		map.put("test", "dfghfghj");

		assertThrows(ResponseStatusException.class, () -> {
			chimeraClient.getRoleFromResponse(map, "test");
		});
	}

	@Test
	void getRoleFromResponse() {
		List<String> roleList = new ArrayList<>();
		roleList.add("admin");

		HashMap responseObject = new HashMap<>();
		responseObject.put("role", roleList);

		assertEquals(roleList, chimeraClient.getRoleFromResponse(responseObject, "role"));
	}

}
