package com.interopx.fhir.validator.util;

import java.util.HashMap;
import java.util.Map;
/**
 * This class defined with all require resource constants and supported scenario constants to validate source/input resource content with goldJson
 * 
 * @author Swarna Nelaturi
 *
 */
public class ScenarioConstants {

	public static Map<String, String> getScenarioName() {
		Map<String, String> scenarioNameMap = new HashMap<>();
		scenarioNameMap.put("uscdi-full-patient", ScenarioNameConstants.USCDI_FULL_PATIENT);
		scenarioNameMap.put("uscdi-additional-patient", ScenarioNameConstants.USCDI_ADDITIONAL_PATIENT);
		scenarioNameMap.put("uscdi-full-condition", ScenarioNameConstants.USCDI_FULL_CONDITION);
		scenarioNameMap.put("uscdi-additional-condition", ScenarioNameConstants.USCDI_ADDITIONAL_CONDITION);
		scenarioNameMap.put("uscdi-full-encounter", ScenarioNameConstants.USCDI_FULL_ENCOUNTER);
		scenarioNameMap.put("uscdi-additional-encounter", ScenarioNameConstants.USCDI_ADDITIONAL_ENCOUNTER);
		scenarioNameMap.put("uscdi-full-allergyintolerance", ScenarioNameConstants.USCDI_FULL_ALLERGY_INTOLERANCE);
		scenarioNameMap.put("uscdi-additional-allergyintolerance", ScenarioNameConstants.USCDI_ADDITIONAL_ALLERGY_INTOLERANCE);
		scenarioNameMap.put("uscdi-full-procedure", ScenarioNameConstants.USCDI_FULL_PROCEDURE);
		scenarioNameMap.put("uscdi-additional-procedure", ScenarioNameConstants.USCDI_ADDITIONAL_PROCEDURE);
		scenarioNameMap.put("uscdi-full-careteam", ScenarioNameConstants.USCDI_FULL_CARETEAM);
		scenarioNameMap.put("uscdi-additional-careteam", ScenarioNameConstants.USCDI_ADDITIONAL_CARETEAM);
		scenarioNameMap.put("uscdi-full-careplan", ScenarioNameConstants.USCDI_FULL_CAREPLAN);
		scenarioNameMap.put("uscdi-additional-careplan", ScenarioNameConstants.USCDI_ADDITIONAL_CAREPLAN);
		scenarioNameMap.put("uscdi-full-immunization", ScenarioNameConstants.USCDI_FULL_IMMUNIZATION);
		scenarioNameMap.put("uscdi-additional-immunization", ScenarioNameConstants.USCDI_ADDITIONAL_IMMUNIZATION);
		scenarioNameMap.put("uscdi-full-diagnosticReport", ScenarioNameConstants. USCDI_FULL_DIAGNOSTICREPORT);
		scenarioNameMap.put("uscdi-additional-diagnosticReport", ScenarioNameConstants.USCDI_ADDITIONAL_DIAGNOSTICREPORT);
		scenarioNameMap.put("uscdi-full-goal", ScenarioNameConstants.USCDI_FULL_GOAL);
		scenarioNameMap.put("uscdi-additional-goal", ScenarioNameConstants.USCDI_ADDITIONAL_GOAL);
		scenarioNameMap.put("uscdi-full-medicationRequest", ScenarioNameConstants.USCDI_FULL_MEDICATION_REQUEST);
		scenarioNameMap.put("uscdi-additional-medicationRequest", ScenarioNameConstants.USCDI_ADDITIONAL_MEDICATION_REQUEST);
		scenarioNameMap.put("uscdi-full-device", ScenarioNameConstants. USCDI_FULL_DEVICE);
		scenarioNameMap.put("uscdi-additional-device", ScenarioNameConstants.USCDI_ADDITIONAL_DEVICE);
		scenarioNameMap.put("uscdi-full-provenance", ScenarioNameConstants. USCDI_FULL_PROVENANCE);
		scenarioNameMap.put("uscdi-additional-provenance", ScenarioNameConstants.USCDI_ADDITIONAL_PROCEDURE);
		scenarioNameMap.put("uscdi-full-documentreference", ScenarioNameConstants. USCDI_FULL_DOCUMENTREFERENCE);
		scenarioNameMap.put("uscdi-additional-documentreference", ScenarioNameConstants.USCDI_ADDITIONAL_DOCUMENTREFERENCE);
		scenarioNameMap.put("uscdi-full-observation", ScenarioNameConstants. USCDI_FULL_OBSERVATION);
		scenarioNameMap.put("uscdi-additional-observation", ScenarioNameConstants.USCDI_ADDITIONAL_OBSERVATION);
		return scenarioNameMap;
	}

	public static class ScenarioNameConstants {
		
		private ScenarioNameConstants() {

		}

		public static final String USCDI_FULL_PATIENT = "uscdi-full-patient";
		public static final String USCDI_ADDITIONAL_PATIENT = "uscdi-additional-patient";
		public static final String USCDI_FULL_CONDITION = "uscdi-full-condition";
		public static final String USCDI_ADDITIONAL_CONDITION = "uscdi-additional-condition";
		public static final String USCDI_FULL_ENCOUNTER = "uscdi-full-encounter";
		public static final String USCDI_ADDITIONAL_ENCOUNTER = "uscdi-additional-encounter";
		public static final String USCDI_FULL_ALLERGY_INTOLERANCE = "uscdi-full-allergyintolerance";
		public static final String USCDI_ADDITIONAL_ALLERGY_INTOLERANCE = "uscdi-additional-allergyintolerance";
		public static final String USCDI_FULL_PROCEDURE = "uscdi-full-procedure";
		public static final String USCDI_ADDITIONAL_PROCEDURE = "uscdi-additional-procedure";
		public static final String USCDI_FULL_CARETEAM = "uscdi-full-careteam";
		public static final String USCDI_ADDITIONAL_CARETEAM = "uscdi-additional-careteam";
		public static final String USCDI_FULL_CAREPLAN = "uscdi-full-careplan";
		public static final String USCDI_ADDITIONAL_CAREPLAN = "uscdi-additional-careplan";
		public static final String USCDI_ADDITIONAL_IMMUNIZATION = "uscdi-additional-immunization";
		public static final String USCDI_FULL_IMMUNIZATION = "uscdi-full-immunization";
		public static final String USCDI_FULL_DIAGNOSTICREPORT = "uscdi-full-diagnosticReport";
		public static final String USCDI_ADDITIONAL_DIAGNOSTICREPORT = "uscdi-additional-diagnosticReport";
		public static final String USCDI_FULL_GOAL = "uscdi-full-goal";
		public static final String USCDI_ADDITIONAL_GOAL = "uscdi-additional-goal";
		public static final String USCDI_FULL_MEDICATION_REQUEST = "uscdi-full-medicationRequest";
		public static final String USCDI_ADDITIONAL_MEDICATION_REQUEST = "uscdi-additional-medicationRequest";
		public static final String USCDI_FULL_DEVICE = "uscdi-full-device";
		public static final String USCDI_ADDITIONAL_DEVICE = "uscdi-additional-device";
		public static final String USCDI_FULL_PROVENANCE = "uscdi-full-provenance";
		public static final String USCDI_ADDITIONAL_PROVENANCE = "uscdi-additional-provenance";
		public static final String USCDI_FULL_DOCUMENTREFERENCE = "uscdi-full-documentreference";
		public static final String USCDI_ADDITIONAL_DOCUMENTREFERENCE = "uscdi-additional-documentreference";
		public static final String USCDI_FULL_OBSERVATION = "uscdi-full-observation";
		public static final String USCDI_ADDITIONAL_OBSERVATION = "uscdi-additional-observation";
		
	}
	/**
	 * ResourceConstants
	 */
	public static class ResourceNames {
		public static final String ORGANIZATION = "Organization";
		public static final String PRACTITIONER = "Practitioner";
		public static final String PRACTITIONER_ROLE = "PractitionerRole";
		public static final String LOCATION = "Location";
		public static final String PATIENT = "Patient";
		public static final String CONDITION = "Condition";
		public static final String ENCOUNTER = "Encounter";
		public static final String MEDICATIONREQUEST = "MedicationRequest";
		public static final String MEDICATIONSTATEMENT = "MedicationStatement";
		public static final String OBSERVATION = "Observation";
		public static final String IMMUNIZATION = "Immunization";
		public static final String CAREPLAN = "CarePlan";
		public static final String CARETEAM = "CareTeam";
		public static final String PROCEDURE = "Procedure";
		public static final String DOCUMENTREFERENCE = "DocumentReference";
		public static final String OBSERVATION_VITAL_SIGNS = "Observation_vital-signs";
		public static final String OBSERVATION_SOCIAL_STATUS = "Observation_social-history";
		public static final String OBSERVATION_LABORATORY = "Observation_laboratory";
		public static final String ALLERGY_INTOLERANCE = "AllergyIntolerance";
		public static final String COVERAGE = "Coverage";
		public static final String RELATED_PERSON = "RelatedPerson";
		public static final String CLAIM = "Claim";
		public static final String ORGANIZATION_INSURANCE = "Organization_Insurance";
		public static final String ORGANIZATION_EMPLOYER = "Organization_Employer";
		public static final String RELATEDPERSON = "RelatedPerson";
		public static final String DIAGNOSTICREPORT = "DiagnosticReport";
		public static final String DEVICE = "Device";
		public static final String EXPLANATIONOFBENEFIT = "ExplanationOfBenefit";
		public static final String FAMILYMEMBERHISTORY = "FamilyMemberHistory";
		public static final String SERVICEREQUEST = "ServiceRequest";
		public static final String CLAIMRESPONSE = "ClaimResponse";
		public static final String GOAL = "Goal";
		public static final String MEDICATION = "Medication";
		public static final String PROVENANCE = "Provenance";
		public static final String IDENTIFIER=  "identifier";
		public static final String EXTENSION = "extension";
		public static final String STATUS=  "status";
		public static final String DOC_STATUS=  "docStatus";
		public static final String SUBJECT="subject";
		public static final String TYPE= "type";
		public static final String SERVICE_TYPE= "serviceType";
		public static final String PRIORITY ="priority";
		public static final String REASON_CODE  ="reasonCode";
		public static final String EPISODE_OF_CARE ="episodeOfCare";
		public static final String BASED_ON="basedOn";
		public static final String APPOINTMENT="appointment";
		public static final String SERVICE_PROVIDER = "serviceProvider";
		public static final String PART_OF = "partOf";
		public static final String REASON_REFERENCE = "reasonReference";
		public static final String ACCOUNT = "account";
		public static final String STATUS_HISTORY = "statusHistory";
		public static final String PARTICIPANT = "participant";
		public static final String DIAGNOSIS = "diagnosis";
		public static final String CLASS_HISTORY = "classHistory";
		public static final String HOSPITALIZATION = "hospitalization";
		public static final String CLASS = "class";
		public static final String PERIOD = "period";
		public static final String VERIFICATION_STATUS = "verificationStatus";
		public static final String CLINICAL_STATUS = "clinicalStatus";
		public static final String CATEGORY = "category";
		public static final String SEVERITY = "severity";
		public static final String CODE = "code";
		public static final String BODY_SITE = "bodySite";
		public static final String RECORDER = "recorder";
		public static final String ASSERTER = "asserter";
		public static final String NAME = "name";
		public static final String GENDER = "gender";
		public static final String BIRTH_DATE = "birthDate";
		public static final String ADDRESS = "address";
		public static final String TELECOM = "telecom";
		public static final String MARITALSTATUS = "maritalStatus";
		public static final String MULTIPLEBIRTH = "multipleBirth";
		public static final String COMMUNICATION = "communication";
		public static final String MANAGIGN_ORGANIZATION = "managingOrganization";
		public static final String GENERAL_PRACTITIONER = "generalPractitioner";
		public static final String LANGUAGE = "language";
		public static final String CONTAINED = "contained";
		public static final String MODIFIER_EXTENSION = "modifierExtension";
		public static final String IMPLICIT_RULES = "implicitRules";
		public static final String ID = "id";
		public static final String CRITICALITY = "criticality";
		public static final String LAST_OCCURENCE = "lastOccurrence";
		public static final String NOTE = "note";
		public static final String REACTION = "reaction";
		public static final String MANIFESTATION = "reaction.manifestation";
		public static final String TEXT="text";
		public static final String OUTCOME = "outcome";
		public static final String COMPLICATION_DETAIL = "complicationDetail";
		public static final String FOLLOW_UP = "followUp";
		public static final String USED_REFERENCE = "usedReference";
		public static final String USED_CODE = "usedCode";
		public static final String INSTANTIATES_CANONICAL = "instantiatesCanonical";
		public static final String PERFORMED = "performed"; 
		public static final String PERFORMED_DATE = "performedDateTime";
		public static final String PERFORMED_PERIOD = "performedPeriod";
		public static final String REPLACES = "replaces";
		public static final String INTENT = "intent";
		public static final String TITLE = "title";
		public static final String DESCRIPTION = "description";
		public static final String CREATED = "created";
		public static final String CONTRIBUTOR = "contributor";
		public static final String AUTHOR = "author";
		public static final String ADDRESSES = "addresses";
		public static final String SUPPORTING_INFO = "supportingInfo";
		public static final String ACTIVITY = "activity";
		public static final String DETAIL = "detail";

		public static final String STATUS_REASON = "statusReason";
		public static final String VACCINE_CODE = "vaccineCode";
		public static final String OCCURRENCE_DATA_TIME = "occurrenceDateTime";
		public static final String OCCURRENCE_STRING = "occurrenceString"; 
		public static final String RECORDED = "recorded";
		public static final String PRIMARY_SOURCE = "primarySource";
		public static final String REPORT_ORIGIN = "reportOrigin";
		public static final String MANUFACTURER = "manufacturer";
		public static final String LOT_NUMBER = "lotNumber";
		public static final String EXPIRATION_DATE = "expirationDate";
		public static final String SITE = " site";
		public static final String ROUTE = "route";
		public static final String DOSE_QUANTITY = "doseQuantity";
		public static final String PERFORMER = "performer";
		public static final String FUNCTION = " function";
		public static final String ACTOR = " actor";
		public static final String IS_SUBPOTENT = " isSubpotent";
		public static final String SUBPOTENT_REASON = "subpotentReason";
		public static final String PROGRAM_ELIGIBILITY = "programEligibility";
		public static final String FUNCTION_SOURCE = "fundingSource";
		public static final String EDUCATION = "education";
		public static final String PROTOCOL_APPLIED = "protocolApplied";
		public static final String EFFECTIVE_DATE_TIME = "effectiveDateTime";
		public static final String EFFECTIVE_DATE_PERIOD = "effectivePeriod";
		public static final String ISSUED = "issued";
		public static final String RESULTS_INTERPRETER = "resultsInterpreter";
		public static final String SPECIMEN = "specimen";
		public static final String RESULT = "result";
		public static final String IMAGING_STUDY = "imagingStudy";
		public static final String MEDIA = "media";
		public static final String CONCLUSION = "conclusion";
		public static final String CONCLUSION_CODE = "conclusionCode";
		public static final String PRESENTED_FORM = "presentedForm";
		public static final String LIFE_CYCLE_STATUS = "lifecycleStatus";
		public static final String ACHIEVEMENT_STATUS = "achievementStatus";
		public static final String OUTCOME_CODE = "outcomeCode";
		public static final String STATUS_DATE = "statusDate";
		public static final String OUTCOME_REFERENCE = "outcomeReference";

		public static final String FOCAL_DEVICE_COMPONENT = "focalDevice";
		public static final String COMPLICATION = "complication";
		public static final String LENGTH = "length";
		public static final String CONTACT = "contact";
		
		
		

		//Status Constants
		public static final String COMPLETED = "completed";
		public static final String NOTDONE = "not-done";
		public static final String ENTEREDINERROR = "entered-in-error";
		public static final String EXPRESSED_BY = "expressedBy";
		public static final String START = "start";
		public static final String TARGET = "target";
		public static final String PHOTO = "photo";
		public static final String DECEASED = "deceased";
		
		
		
		


		
		
		
		
		public static final String DEFINITION = "definition";
		public static final String UDI_CARRIER = "udiCarrier";
		public static final String DISTINCT_IDENTIFIER = "distinctIdentifier";
		public static final String MANUFACTURER_DATE = "manufactureDate";
		public static final String SERIAL_NUMBER = "serialNumber";
		public static final String DEVICE_NAME = "deviceName";
		public static final String MODEL_NUMBER = "modelNumber";
		public static final String PART_NUMBER = "partNumber";
		public static final String SPECIALIZATION = "specialization";
		public static final String VERSION = "version";
		public static final String PROPERTY = "property";
		public static final String OWNER = "owner";
		public static final String URL = "url";
		public static final String SAFETY = "safety";
		public static final String PARENT = "parent";
		public static final String DO_NOT_PERFORM = "doNotPerform";
		public static final String SUPPORTING_INFORMATION = "supportingInformation";
		public static final String AUTHORED_ON = "authoredOn";
		public static final String REQUESTER = "requester";
		public static final String PERFORMER_TYPE = "performerType";
		public static final String COURSE_OF_THERAPY_TYPE = "courseOfTherapyType";
		public static final String INSURANCE = "insurance";
		public static final String GROUP_IDENTIFIER = "groupIdentifier";
		public static final String REPORTED = "reported";
		public static final String MEDICATION_CODEABLECONCEPT = "medicationCodeableConcept";
		public static final String MEDICATION_REFERENCE = "medicationReference";
		public static final String REPORTED_BOOLEAN = "reportdeBoolean";
		public static final String REPORTED_REFERENCE = "reportedReference";
		public static final String DOSAGE_INSTRUCTION = "dosageInstruction";
		public static final String DISPENSE_REQUEST = "dispenseRequest";
		public static final String SUBSTITUTION = "substitution";
		public static final String PRIOR_PRESCRIPTION = "priorPrescription";
		public static final String DETECTED_ISSUE = "detectedIssue";
		public static final String EVENT_HISTORY = "eventHistory";
		public static final String RECORDED_DATE = "recordedDate";
		public static final String STAGE = "stage";
		public static final String EVIDENCE = "evidence";
		public static final String ABATEMENT_AGE = "abatementAge";
		public static final String ABATEMENT_PERIOD = "abatementPeriod";
		public static final String ABATEMENT_DATE = "abatementDateTime";
		public static final String ABATEMENT_RANGE = "abatementRange";
		public static final String ABATEMENT_STRING = "abatementString";
		public static final String ONSET_DATE_TIME = "onsetDateTime";
		public static final String ONSET_AGE = "onsetAge";
		public static final String ONSET_PERIOD = "onsetPeriod";
		public static final String ONSET_RANGE = "onsetRange";
		public static final String ONSET_STRING = "onsetString";
		public static final String CUSTODIAN = "custodian";
		public static final String SECURITY_LABEL = "securityLabel";
		public static final String RELATES_TO = "relatesTo";
		public static final String CONTENT = "content";
		public static final String CONTEXT = "context";
		public static final String FOCUS = "focus";
		public static final String EFFECTIVE_TIME = "effectiveTiming";
		public static final String EFFECTIVE_INSTANT = "effectiveInstant";
		public static final String DATE_ABSENT_REASON = "dataAbsentReason";
		public static final String INTERPRETATION = "interpretation";
		public static final String METHOD = "method";
		public static final String REFERENCE_RANGE = "referenceRange";
		public static final String COMPONENT = "component";
		public static final String VALUE_QUANTITY = "valueQuantity";
		public static final String VALUE_CODEABLE_CONCEPT = "valueCodeableConcept";
		public static final String VALUE_STRING = "valueString";
		public static final String VALUE_BOOLEAN = "valueBoolean";
		public static final String VALUE_INTEGER = "valueInteger";
		public static final String VALUE_RANGE = "valueRange";
		public static final String VALUE_RATIO = "valueRatio";
		public static final String VALUE_DATE_TIME = "valueDateTime";
		public static final String VALUE_PERIOD = "valuePeriod";
		public static final String VALUE_TIME_TYPE = "valueTime";
		public static final String OCCURRED_DATE_TIME = "occurredDateTime";
		public static final String OCCURRED_PERIOD = "occurredPeriod";
		public static final String REASON = "reason";
		public static final String AGENT = "agent";
		public static final String ENTITY = "entity";
		public static final String SIGNATURE = "signature";
		

	}
}
