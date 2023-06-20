package com.interopx.fhir.auth.server.service;

import java.util.Map;

public interface BackendTokenService {

	Map<String, String> getAccessTokenResponse(Map<String, String> formData, Map<String, String> headerMap)
			throws Exception;

}
