package com.interopx.fhir.validator.model;

import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class ValidationResultsDto {

	private ValidationResultsMetaData resultsMetaData;
	private List<ReferenceError> fhirValidationResults;

	public ValidationResultsMetaData getResultsMetaData() {
		return resultsMetaData;
	}

	public void setResultsMetaData(ValidationResultsMetaData resultsMetaData) {
		this.resultsMetaData = resultsMetaData;
	}

	public List<ReferenceError> getFhirValidationResults() {
		return fhirValidationResults;
	}

	public void setFhirValidationResults(List<ReferenceError> fhirValidationResults) {
		this.fhirValidationResults = fhirValidationResults;
	}

	
}
