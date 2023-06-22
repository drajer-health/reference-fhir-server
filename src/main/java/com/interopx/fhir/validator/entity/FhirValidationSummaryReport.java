package com.interopx.fhir.validator.entity;

import java.sql.Timestamp;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import com.interopx.fhir.validator.configuration.JSONObjectUserType;

@Entity
// @TypeDefs({ @TypeDef(name = "StringJsonObject", typeClass = JSONObjectUserType.class) })
@Table(name = "fhir_validation_summary_report")
public class FhirValidationSummaryReport {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	// @SequenceGenerator(name = "FHIR_VALIDATION_SUMMARY_REPORT_SEQ", sequenceName = "fhir_validation_summary_report_id_seq", allocationSize = 1)
	@Column(name = "id")
	Integer id;

	@Column(name = "transaction_id")
	private String transactionId;

	@Column(name = "patient_name")
	private String patientName;

	@Column(name = "gold_file_name")
	private String goldFileName;

	@Column(name = "apu_id")
	private String apuId;

	@Column(name = "emr_version")
	private String emrVersion;

	@Column(name = "adaptor_version")
	private String adaptorVersion;

	@Column(name = "modality")
	private String modality;

	@Column(name = "error")
	private String error;

	@Column(name = "scores")
	private int scores;

	@Column(name = "validated_on")
	private Timestamp validatedOn;

	@Column(name = "fhir_ig_vacabulary_error_count")
	private int fhirIgVacabularyError;

	@Column(name = "fhir_ig_vacabulary_warning_count")
	private int fhirIgVacabularyWarning;

	@Column(name = "fhir_ig_vacabulary_info_count")
	private int fhirIgVacabularyInfo;

	@Column(name = "fhir_reference_error_count")
	private int fhirReferenceError;

	@Column(name = "fhir_reference_warning_count")
	private int fhirReferenceWarning;

	@Column(name = "fhir_reference_info_count")
	private int fhiraReferenceInfo;

	@Column(name = "fhir_file_Contents")
	private String resourceContents;

	@Column(name = "criterion_name")
	private String criterionName;

	@Column(name = "fhir_file_name")
	private String resourceName;

	@Column(name = "fhir_version")
	private String fhirVersion;

	@Column(name = "fhir_validation_results", columnDefinition = "TEXT")
	// @Type(type = "StringJsonObject")
	private String fhirValidationResults;

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
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


	public Timestamp getValidatedOn() {
		return validatedOn;
	}

	public void setValidatedOn(Timestamp validatedOn) {
		this.validatedOn = validatedOn;
	}

	public int getFhirIgVacabularyError() {
		return fhirIgVacabularyError;
	}

	public void setFhirIgVacabularyError(int fhirIgVacabularyError) {
		this.fhirIgVacabularyError = fhirIgVacabularyError;
	}

	public int getFhirIgVacabularyWarning() {
		return fhirIgVacabularyWarning;
	}

	public void setFhirIgVacabularyWarning(int fhirIgVacabularyWarning) {
		this.fhirIgVacabularyWarning = fhirIgVacabularyWarning;
	}

	public int getFhirIgVacabularyInfo() {
		return fhirIgVacabularyInfo;
	}

	public void setFhirIgVacabularyInfo(int fhirIgVacabularyInfo) {
		this.fhirIgVacabularyInfo = fhirIgVacabularyInfo;
	}

	public int getFhirReferenceError() {
		return fhirReferenceError;
	}

	public void setFhirReferenceError(int fhirReferenceError) {
		this.fhirReferenceError = fhirReferenceError;
	}

	public int getFhirReferenceWarning() {
		return fhirReferenceWarning;
	}

	public void setFhirReferenceWarning(int fhirReferenceWarning) {
		this.fhirReferenceWarning = fhirReferenceWarning;
	}

	public int getFhiraReferenceInfo() {
		return fhiraReferenceInfo;
	}

	public void setFhiraReferenceInfo(int fhiraReferenceInfo) {
		this.fhiraReferenceInfo = fhiraReferenceInfo;
	}

	public String getresourceContents() {
		return resourceContents;
	}

	public void setresourceContents(String resourceContents) {
		this.resourceContents = resourceContents;
	}

	public String getCriterionName() {
		return criterionName;
	}

	public void setCriterionName(String criterionName) {
		this.criterionName = criterionName;
	}

	public String getResourceName() {
		return resourceName;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	public String getFhirVersion() {
		return fhirVersion;
	}

	public void setFhirVersion(String fhirVersion) {
		this.fhirVersion = fhirVersion;
	}

	public String getFhirValidationResults() {
		return fhirValidationResults;
	}

	public void setFhirValidationResults(String fhirValidationResults) {
		this.fhirValidationResults = fhirValidationResults;
	}

}
