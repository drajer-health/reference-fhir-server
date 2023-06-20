package com.interopx.fhir.facade.provider;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.core.io.ClassPathResource;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.interopx.fhir.facade.model.AuthConfiguration;

public class TestUtil {

	public static String convertJsonToJsonString(String file) throws IOException {
		return new String(Files.readAllBytes(new ClassPathResource(file).getFile().toPath()));
	}

	public static Map<String, Object> jsonToMap(String json) {
		HashMap<String, Object> map = new HashMap<>();

		ObjectMapper mapper = new ObjectMapper();
		try {
			// Convert JSON to map
			map = (HashMap<String, Object>) mapper.readValue(json, new TypeReference<Map<String, Object>>() {
			});

		} catch (IOException e) {
			e.printStackTrace();
		}

		return map;
	}

	public static List<AuthConfiguration> getAuthConfiguration() {
		try {
			AuthConfiguration authConfigurationDto = new AuthConfiguration();
			authConfigurationDto.setAuthConfigId(1);
			authConfigurationDto.setAuthorizationEndpointUrl("rawstring");
			authConfigurationDto.setClientId("rawstring");
			authConfigurationDto.setClientSecret("rawstring");
			authConfigurationDto.setIntrospectionEndpointUrl("rawstring");
			authConfigurationDto.setTokenEndpointUrl("rawstring");
			List<AuthConfiguration> authConfigurationDtoList = new ArrayList<>();
			authConfigurationDtoList.add(authConfigurationDto);
			return authConfigurationDtoList;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
