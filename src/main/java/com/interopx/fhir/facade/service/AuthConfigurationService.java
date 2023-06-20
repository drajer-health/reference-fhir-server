package com.interopx.fhir.facade.service;

import java.util.List;

import com.interopx.fhir.facade.model.AuthConfiguration;

public interface AuthConfigurationService {
	List<AuthConfiguration> getAuthConfiguration();	
	String getBaseURL(String str) ;
	
}
