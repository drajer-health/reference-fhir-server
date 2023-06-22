package com.interopx.fhir.validator.model;

import java.util.List;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = Include.NON_NULL)
@Component
public class ValidationResultsMetaData {
	
	  private String fhirVersion;
	  private boolean serviceError;
	  private String serviceErrorMessage;
	  private String resourceName;
	  private String resourceContents;
	  private List<ResultMetaData> resultMetaData;
	  private String patientName;
	  private String goldFileName;
	  private String apuId;
	  private String transactionId;
	  private String emrVersion;
	  private String adaptorVersion;
	  private String modality;
	  private String error;
	  private int scores;
	  private String validatedOn;
	  private String criterionName;
	  private String fhirValidationResults;
	  
	public String getFhirVersion() {
		return fhirVersion;
	}
	public void setFhirVersion(String fhirVersion) {
		this.fhirVersion = fhirVersion;
	}
	public boolean isServiceError() {
		return serviceError;
	}
	public void setServiceError(boolean serviceError) {
		this.serviceError = serviceError;
	}
	public String getServiceErrorMessage() {
		return serviceErrorMessage;
	}
	public void setServiceErrorMessage(String serviceErrorMessage) {
		this.serviceErrorMessage = serviceErrorMessage;
	}
	public String getResourceName() {
		return resourceName;
	}
	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}
	public String getResourceContents() {
		return resourceContents;
	}
	public void setResourceContents(String resourceContents) {
		this.resourceContents = resourceContents;
	}
	public List<ResultMetaData> getResultMetaData() {
		return resultMetaData;
	}
	public void setResultMetaData(List<ResultMetaData> resultMetaData) {
		this.resultMetaData = resultMetaData;
	}
	public String getPatientName() {
		return patientName;
	}
	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}
	public String getGoldFileName() {
		return goldFileName;
	}
	public void setGoldFileName(String goldFileName) {
		this.goldFileName = goldFileName;
	}
	public String getApuId() {
		return apuId;
	}
	public void setApuId(String apuId) {
		this.apuId = apuId;
	}
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	public String getEmrVersion() {
		return emrVersion;
	}
	public void setEmrVersion(String emrVersion) {
		this.emrVersion = emrVersion;
	}
	public String getAdaptorVersion() {
		return adaptorVersion;
	}
	public void setAdaptorVersion(String adaptorVersion) {
		this.adaptorVersion = adaptorVersion;
	}
	public String getModality() {
		return modality;
	}
	public void setModality(String modality) {
		this.modality = modality;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public int getScores() {
		return scores;
	}
	public void setScores(int scores) {
		this.scores = scores;
	}
	
	public String getValidatedOn() {
		return validatedOn;
	}
	public void setValidatedOn(String validatedOn) {
		this.validatedOn = validatedOn;
	}
	public String getCriterionName() {
		return criterionName;
	}
	public void setCriterionName(String criterionName) {
		this.criterionName = criterionName;
	}
	public String getFhirValidationResults() {
		return fhirValidationResults;
	}
	public void setFhirValidationResults(String fhirValidationResults) {
		this.fhirValidationResults = fhirValidationResults;
	}
  
}
