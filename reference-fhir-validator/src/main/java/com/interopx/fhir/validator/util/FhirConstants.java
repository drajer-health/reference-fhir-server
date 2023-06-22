package com.interopx.fhir.validator.util;

import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FhirConstants {
	
	private static final Logger logger = LoggerFactory.getLogger(FhirConstants.class);

	public FhirConstants() {
		logger.info("To escape sonarqube complaint added constructor");
	}

	public static final String TARGET_VERSION = "R4";
	
	public static Map<String, String> getProfile() {
		Map<String, String> usCoreProfilesMap = new HashMap<>();
		usCoreProfilesMap.put("Organization", UsCoreProfiles.ORGANIZATION_URL);
		usCoreProfilesMap.put("Patient", UsCoreProfiles.PATIENT_URL);
		usCoreProfilesMap.put("Practitioner", UsCoreProfiles.PRACTITIONER_URL);
		usCoreProfilesMap.put("PractitionerRole", UsCoreProfiles.PRACTITIONERROLE_URL);
		usCoreProfilesMap.put("Location", UsCoreProfiles.LOCATION_URL);
		usCoreProfilesMap.put("Encounter", UsCoreProfiles.ENCOUNTER_URL);
		usCoreProfilesMap.put("Condition", UsCoreProfiles.CONDITION_URL);
		usCoreProfilesMap.put("Procedure", UsCoreProfiles.PROCEDURE_URL);
		usCoreProfilesMap.put("CarePlan", UsCoreProfiles.CAREPLAN_URL);
		usCoreProfilesMap.put("CareTeam", UsCoreProfiles.CARETEAM_URL);
		usCoreProfilesMap.put("MedicationRequest", UsCoreProfiles.MEDICATIONREQUEST_URL);
		usCoreProfilesMap.put("MedicationStatement", UsCoreProfiles.MEDICATIONSTATEMENT_URL);
		usCoreProfilesMap.put("Observation-laboratory", UsCoreProfiles.OBSERVATION_LAB_URL);
		usCoreProfilesMap.put("Observation-vitalsigns", UsCoreProfiles.OBSERVATION_VITALSIGNS_URL);
		usCoreProfilesMap.put("Observation-socialhistory", UsCoreProfiles.OBSERVATION_SOCIALHISTORY_URL);
		usCoreProfilesMap.put("Immunization", UsCoreProfiles.IMMUNIZATION_URL);
		usCoreProfilesMap.put("AllergyIntolerance", UsCoreProfiles.ALLERGYINTOLERANCE_URL);
		usCoreProfilesMap.put("RelatedPerson", UsCoreProfiles.RELATEDPERSON_URL);
		usCoreProfilesMap.put("Coverage", UsCoreProfiles.COVERAGE_URL);
		usCoreProfilesMap.put("Claim", UsCoreProfiles.CLAIM_URL);
		usCoreProfilesMap.put("ServiceRequest", UsCoreProfiles.SERVICEREQUEST_URL);
		usCoreProfilesMap.put("DiagnosticReport-laboratory", UsCoreProfiles.DIAGNOSTICREPORT_LAB_URL);
		usCoreProfilesMap.put("DiagnosticReport-note", UsCoreProfiles.DIAGNOSTICREPORT_NOTE_URL);
		usCoreProfilesMap.put("DocumentReference", UsCoreProfiles.DOCUMENTREFERENCE_URL);
		usCoreProfilesMap.put("Device", UsCoreProfiles.DEVICE_URL);
		usCoreProfilesMap.put("FamilyMemberHistory", UsCoreProfiles.FAMILYMEMBERHISTORY_URL);
		usCoreProfilesMap.put("ExplanationOfBenefit", UsCoreProfiles.EXPLANATIONOFBENEFITS_URL);
		usCoreProfilesMap.put("ClaimResponse", UsCoreProfiles.CLAIMRESPONSE_URL);
		usCoreProfilesMap.put("Observation-pediatric-bmi-age", UsCoreProfiles.OBSERVATION_PEDIATRIC_BMI_FOR_AGE_URL);
		usCoreProfilesMap.put("Observation-pediatric-head-occipital",
				UsCoreProfiles.OBSERVATION_PEDIATRIC_HEAD_OCCIPITAL_URL);
		usCoreProfilesMap.put("Observation-pediatric-hight-weight",
				UsCoreProfiles.OBSERVATION_PEDIATRIC_WEIGHT_FOR_HEIGHT_URL);
		usCoreProfilesMap.put("Goal", UsCoreProfiles.GOAL_URL);
		usCoreProfilesMap.put("Observation-pulse-oximetry", UsCoreProfiles.OBSERVATION_PULSE_OXIMETRY);
		usCoreProfilesMap.put("Medication", UsCoreProfiles.MEDICATION_URL);
		usCoreProfilesMap.put("Provenance", UsCoreProfiles.PROVENANCE_URL);
		return usCoreProfilesMap;
	}

	public static class UsCoreProfiles {
		private UsCoreProfiles() {

		}

		public static final String ORGANIZATION_URL = "http://hl7.org/fhir/us/core/StructureDefinition/us-core-organization";
		public static final String PATIENT_URL = "http://hl7.org/fhir/us/core/StructureDefinition/us-core-patient";
		public static final String PRACTITIONER_URL = "http://hl7.org/fhir/us/core/StructureDefinition/us-core-practitioner";
		public static final String PRACTITIONERROLE_URL = "http://hl7.org/fhir/us/core/StructureDefinition/us-core-practitionerrole";
		public static final String LOCATION_URL = "http://hl7.org/fhir/us/core/StructureDefinition/us-core-location";
		public static final String ENCOUNTER_URL = "http://hl7.org/fhir/us/core/StructureDefinition/us-core-encounter";
		public static final String CONDITION_URL = "http://hl7.org/fhir/us/core/StructureDefinition/us-core-condition";
		public static final String PROCEDURE_URL = "http://hl7.org/fhir/us/core/StructureDefinition/us-core-procedure";
		public static final String CAREPLAN_URL = "http://hl7.org/fhir/us/core/StructureDefinition/us-core-careplan";
		public static final String CARETEAM_URL = "http://hl7.org/fhir/us/core/StructureDefinition/us-core-careteam";
		public static final String MEDICATIONREQUEST_URL = "http://hl7.org/fhir/us/core/StructureDefinition/us-core-medicationrequest";
		public static final String MEDICATIONSTATEMENT_URL = "http://hl7.org/fhir/StructureDefinition/MedicationStatement";
		public static final String OBSERVATION_LAB_URL = "http://hl7.org/fhir/us/core/StructureDefinition/us-core-observation-lab";
		public static final String OBSERVATION_VITALSIGNS_URL = "http://hl7.org/fhir/StructureDefinition/vitalsigns";
		public static final String OBSERVATION_SOCIALHISTORY_URL = "http://hl7.org/fhir/us/core/StructureDefinition/us-core-smokingstatus";
		public static final String IMMUNIZATION_URL = "http://hl7.org/fhir/us/core/StructureDefinition/us-core-immunization";
		public static final String ALLERGYINTOLERANCE_URL = "http://hl7.org/fhir/us/core/StructureDefinition/us-core-allergyintolerance";
		public static final String COVERAGE_URL = "http://hl7.org/fhir/StructureDefinition/Coverage";
		public static final String CLAIM_URL = "http://hl7.org/fhir/StructureDefinition/Claim";
		public static final String RELATEDPERSON_URL = "http://hl7.org/fhir/StructureDefinition/RelatedPerson";
		public static final String SERVICEREQUEST_URL = "http://hl7.org/fhir/StructureDefinition/ServiceRequest";
		public static final String DIAGNOSTICREPORT_LAB_URL = "http://hl7.org/fhir/us/core/StructureDefinition/us-core-diagnosticreport-lab";
		public static final String DIAGNOSTICREPORT_NOTE_URL = "http://hl7.org/fhir/us/core/StructureDefinition/us-core-diagnosticreport-note";
		public static final String DOCUMENTREFERENCE_URL = "http://hl7.org/fhir/us/core/StructureDefinition/us-core-documentreference";
		public static final String DEVICE_URL = "http://hl7.org/fhir/us/core/StructureDefinition/us-core-implantable-device";
		public static final String FAMILYMEMBERHISTORY_URL = "http://hl7.org/fhir/StructureDefinition/FamilyMemberHistory";
		public static final String EXPLANATIONOFBENEFITS_URL = "http://hl7.org/fhir/StructureDefinition/ExplanationOfBenefit";
		public static final String CLAIMRESPONSE_URL = "http://hl7.org/fhir/StructureDefinition/ClaimResponse";
		public static final String OBSERVATION_PEDIATRIC_BMI_FOR_AGE_URL = "http://hl7.org/fhir/us/core/StructureDefinition/pediatric-bmi-for-age";
		public static final String OBSERVATION_PEDIATRIC_HEAD_OCCIPITAL_URL = "http://hl7.org/fhir/us/core/StructureDefinition/head-occipital-frontal-circumference-percentile";
		public static final String OBSERVATION_PEDIATRIC_WEIGHT_FOR_HEIGHT_URL = "http://hl7.org/fhir/us/core/StructureDefinition/pediatric-weight-for-height";
		public static final String GOAL_URL = "http://hl7.org/fhir/us/core/StructureDefinition/us-core-goal";
		public static final String OBSERVATION_PULSE_OXIMETRY = "http://hl7.org/fhir/us/core/StructureDefinition/us-core-pulse-oximetry";
		public static final String MEDICATION_URL = "http://hl7.org/fhir/us/core/StructureDefinition/us-core-medication";
		public static final String PROVENANCE_URL = "http://hl7.org/fhir/us/core/StructureDefinition/us-core-provenance";

	}

}
