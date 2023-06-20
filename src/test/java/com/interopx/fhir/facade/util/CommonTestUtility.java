package com.interopx.fhir.facade.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class CommonTestUtility {
	private static String TEST_TOKEN;

	@Value("${test.token}")
	public void setApplicationName(String testToken) {
		TEST_TOKEN = testToken;
	}

	public static MultiValueMap<String, String> getHeadersForHttpRequest() {
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
		headers.add("Accept", "application/fhir+json;fhirVersion=4.0");
		headers.add("Content-Type", "application/fhir+json;fhirVersion=4.0");	
		headers.add("X-AUTH-TOKEN", TEST_TOKEN);
		headers.add("Authorization", "bearer "+TEST_TOKEN);
		return headers;
	}
}
