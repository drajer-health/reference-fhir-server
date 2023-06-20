package com.interopx.fhir.facade.dao;

public interface FacadeConfigurationDao {
	String getFhirValidatorUrl();
	Boolean getFhirValidationStatus();
}
