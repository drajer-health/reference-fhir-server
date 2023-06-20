package com.interopx.fhir.facade.dao;

import java.util.List;

import com.interopx.fhir.facade.model.AuthConfiguration;

public interface AuthConfigurationDao {

	List<AuthConfiguration> getAuthConfiguration();
	

}
