package com.interopx.fhir.validator.service;

import org.hl7.fhir.r5.model.OperationOutcome;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.fhir.validation.ValidationResult;

public interface ResourceValidationService {
	ValidationResult validateDSTU2Resource(FhirContext dstu2hl7FhirContext, FhirValidator validator, String bodyStr);

	ValidationResult validateSTU3Resource(FhirContext dstu3Context, FhirValidator validator, String bodyStr);

	ValidationResult validateR4Resource(FhirContext r4Context, FhirValidator validator, String bodyStr);
	
	public OperationOutcome validate(byte[] resource, String profile) throws Exception;

	public OperationOutcome validateID(String resource, String profile,int resourceIndex) throws Exception;

}
