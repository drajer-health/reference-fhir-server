/*******************************************************************************
 * Copyright (c) 2020-2022 InteropX LLC, www.interopx.com
 *
 * All Rights Reserved.
 *
 * The source code contained herein, and the packages generated from the source code, are and remain the property of InteropX, LLC. 
 * Access to this source code and the generated packages is granted to authorized customers of InteropX under an InteropX Software License Agreement (SLA).
 * Any use of the source code or the generated packages, not explicitly covered under the signed SLA, is prohibited.
 *******************************************************************************/
package com.interopx.fhir.facade.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.r4.model.AllergyIntolerance;
import org.hl7.fhir.r4.model.CarePlan;
import org.hl7.fhir.r4.model.CareTeam;
import org.hl7.fhir.r4.model.Condition;
import org.hl7.fhir.r4.model.Device;
import org.hl7.fhir.r4.model.DiagnosticReport;
import org.hl7.fhir.r4.model.DocumentReference;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.Goal;
import org.hl7.fhir.r4.model.Immunization;
import org.hl7.fhir.r4.model.Location;
import org.hl7.fhir.r4.model.Medication;
import org.hl7.fhir.r4.model.MedicationAdministration;
import org.hl7.fhir.r4.model.MedicationRequest;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Practitioner;
import org.hl7.fhir.r4.model.PractitionerRole;
import org.hl7.fhir.r4.model.Procedure;
import org.hl7.fhir.r4.model.Provenance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResourceProviderValidator {
	private static final Logger logger = LoggerFactory.getLogger(ResourceProviderValidator.class);

	// Resource to Parameter Mapping
	public static HashMap<String, List<String>> resourceParameterMap = new HashMap<>();

	// Static block for Resource to Parameter Mapping
	static {
		try {
			resourceParameterMap.put(ResourceTypes.ALLERGYINTOLERANCE.toString(), Arrays.asList(
					AllergyIntolerance.SP_RES_ID,
					AllergyIntolerance.SP_PATIENT + AppConstants.PIPE_CHARACTER + AllergyIntolerance.SP_CLINICAL_STATUS,
					AllergyIntolerance.SP_PATIENT + AppConstants.PIPE_CHARACTER + AppConstants.ENCOUNTER_PARAMETER,
					AllergyIntolerance.SP_PATIENT));

			resourceParameterMap.put(ResourceTypes.CAREPLAN.toString(),
					Arrays.asList(CarePlan.SP_RES_ID,
							CarePlan.SP_PATIENT + AppConstants.PIPE_CHARACTER + CarePlan.SP_CATEGORY
									+ AppConstants.PIPE_CHARACTER + CarePlan.SP_STATUS + AppConstants.PIPE_CHARACTER
									+ CarePlan.SP_DATE,

							CarePlan.SP_PATIENT + AppConstants.PIPE_CHARACTER + CarePlan.SP_CATEGORY
									+ AppConstants.PIPE_CHARACTER + CarePlan.SP_STATUS,

							CarePlan.SP_PATIENT + AppConstants.PIPE_CHARACTER + CarePlan.SP_CATEGORY
									+ AppConstants.PIPE_CHARACTER + CarePlan.SP_DATE,

							CarePlan.SP_PATIENT + AppConstants.PIPE_CHARACTER + CarePlan.SP_CATEGORY,

							CarePlan.SP_PATIENT + AppConstants.PIPE_CHARACTER + AppConstants.ENCOUNTER_PARAMETER));

			resourceParameterMap.put(ResourceTypes.CARETEAM.toString(), Arrays.asList(CareTeam.SP_RES_ID,
					CareTeam.SP_PATIENT + AppConstants.PIPE_CHARACTER + CareTeam.SP_STATUS));

			resourceParameterMap.put(ResourceTypes.CONDITION.toString(), Arrays.asList(Condition.SP_RES_ID,

					Condition.SP_PATIENT + AppConstants.PIPE_CHARACTER + Condition.SP_ONSET_DATE,

					Condition.SP_PATIENT + AppConstants.PIPE_CHARACTER + Condition.SP_CATEGORY
							+ AppConstants.PIPE_CHARACTER + AppConstants.ENCOUNTER_PARAMETER,

					Condition.SP_PATIENT + AppConstants.PIPE_CHARACTER + Condition.SP_CATEGORY,

					Condition.SP_PATIENT));

			resourceParameterMap.put(ResourceTypes.DEVICE.toString(), Arrays.asList(Device.SP_RES_ID,

					Device.SP_PATIENT + AppConstants.PIPE_CHARACTER + Device.SP_TYPE,

					Device.SP_PATIENT + AppConstants.PIPE_CHARACTER + AppConstants.ENCOUNTER_PARAMETER,

					Device.SP_PATIENT));

			resourceParameterMap.put(ResourceTypes.DIAGNOSTICREPORT.toString(),
					Arrays.asList(DiagnosticReport.SP_RES_ID,

							DiagnosticReport.SP_PATIENT + AppConstants.PIPE_CHARACTER + DiagnosticReport.SP_CATEGORY
									+ AppConstants.PIPE_CHARACTER + DiagnosticReport.SP_DATE,
							DiagnosticReport.SP_PATIENT + AppConstants.PIPE_CHARACTER + DiagnosticReport.SP_CODE
									+ AppConstants.PIPE_CHARACTER + DiagnosticReport.SP_DATE,

							DiagnosticReport.SP_PATIENT + AppConstants.PIPE_CHARACTER + DiagnosticReport.SP_CATEGORY,

							DiagnosticReport.SP_PATIENT + AppConstants.PIPE_CHARACTER + DiagnosticReport.SP_CODE,
							DiagnosticReport.SP_PATIENT + AppConstants.PIPE_CHARACTER + DiagnosticReport.SP_STATUS,

							DiagnosticReport.SP_PATIENT + AppConstants.PIPE_CHARACTER
									+ AppConstants.ENCOUNTER_PARAMETER,

							DiagnosticReport.SP_PATIENT));

			resourceParameterMap.put(ResourceTypes.DOCUMENTREFERENCE.toString(),
					Arrays.asList(DocumentReference.SP_RES_ID,

							DocumentReference.SP_PATIENT + AppConstants.PIPE_CHARACTER + DocumentReference.SP_CATEGORY
									+ AppConstants.PIPE_CHARACTER + DocumentReference.SP_DATE,
							DocumentReference.SP_PATIENT + AppConstants.PIPE_CHARACTER + DocumentReference.SP_TYPE
									+ AppConstants.PIPE_CHARACTER + DocumentReference.SP_DATE,

							DocumentReference.SP_PATIENT + AppConstants.PIPE_CHARACTER + DocumentReference.SP_CATEGORY,

							DocumentReference.SP_PATIENT + AppConstants.PIPE_CHARACTER + DocumentReference.SP_TYPE,
							DocumentReference.SP_PATIENT + AppConstants.PIPE_CHARACTER + DocumentReference.SP_STATUS,

							DocumentReference.SP_PATIENT + AppConstants.PIPE_CHARACTER
									+ AppConstants.ENCOUNTER_PARAMETER,

							DocumentReference.SP_PATIENT));

			resourceParameterMap.put(ResourceTypes.ENCOUNTER.toString(),
					Arrays.asList(Encounter.SP_RES_ID,
							Encounter.SP_PATIENT + AppConstants.PIPE_CHARACTER + Encounter.SP_DATE,
							Encounter.SP_PATIENT + AppConstants.PIPE_CHARACTER + Encounter.SP_CLASS,
							Encounter.SP_PATIENT + AppConstants.PIPE_CHARACTER + Encounter.SP_TYPE,
							Encounter.SP_PATIENT + AppConstants.PIPE_CHARACTER + Encounter.SP_STATUS,
							Encounter.SP_PATIENT, Encounter.SP_IDENTIFIER));

			resourceParameterMap.put(ResourceTypes.GOAL.toString(), Arrays.asList(Goal.SP_RES_ID,
					Goal.SP_PATIENT + AppConstants.PIPE_CHARACTER + Goal.SP_LIFECYCLE_STATUS,
					Goal.SP_PATIENT + AppConstants.PIPE_CHARACTER + AppConstants.ENCOUNTER_PARAMETER, Goal.SP_PATIENT));

			resourceParameterMap.put(ResourceTypes.LOCATION.toString(),
					Arrays.asList(Location.SP_RES_ID, Location.SP_NAME, Location.SP_ADDRESS, Location.SP_ADDRESS_CITY,
							Location.SP_ADDRESS_POSTALCODE, Location.SP_ADDRESS_STATE));

			resourceParameterMap.put(ResourceTypes.ORGANIZATION.toString(),
					Arrays.asList(Organization.SP_RES_ID, Organization.SP_ADDRESS, Organization.SP_NAME));

			resourceParameterMap.put(ResourceTypes.MEDICATIONREQUEST.toString(), Arrays.asList(
					MedicationRequest.SP_RES_ID,

					MedicationRequest.SP_PATIENT + AppConstants.PIPE_CHARACTER + MedicationRequest.SP_INTENT,

					MedicationRequest.SP_PATIENT + AppConstants.PIPE_CHARACTER + MedicationRequest.SP_INTENT
							+ AppConstants.PIPE_CHARACTER + AppConstants.MEDICATIONREQUEST_MEDICATION,

					MedicationRequest.SP_PATIENT + AppConstants.PIPE_CHARACTER + MedicationRequest.SP_INTENT
							+ AppConstants.PIPE_CHARACTER + MedicationRequest.SP_ENCOUNTER,

					MedicationRequest.SP_PATIENT + AppConstants.PIPE_CHARACTER + MedicationRequest.SP_INTENT
							+ AppConstants.PIPE_CHARACTER + MedicationRequest.SP_ENCOUNTER + AppConstants.PIPE_CHARACTER
							+ AppConstants.MEDICATIONREQUEST_MEDICATION,

					MedicationRequest.SP_PATIENT + AppConstants.PIPE_CHARACTER + MedicationRequest.SP_ENCOUNTER,

					MedicationRequest.SP_PATIENT + AppConstants.PIPE_CHARACTER + MedicationRequest.SP_ENCOUNTER
							+ AppConstants.PIPE_CHARACTER + AppConstants.MEDICATIONREQUEST_MEDICATION,

					MedicationRequest.SP_PATIENT + AppConstants.PIPE_CHARACTER + MedicationRequest.SP_INTENT
							+ AppConstants.PIPE_CHARACTER + MedicationRequest.SP_STATUS,

					MedicationRequest.SP_PATIENT + AppConstants.PIPE_CHARACTER + MedicationRequest.SP_INTENT
							+ AppConstants.PIPE_CHARACTER + MedicationRequest.SP_STATUS + AppConstants.PIPE_CHARACTER
							+ AppConstants.MEDICATIONREQUEST_MEDICATION,

					MedicationRequest.SP_PATIENT + AppConstants.PIPE_CHARACTER + MedicationRequest.SP_INTENT
							+ AppConstants.PIPE_CHARACTER + MedicationRequest.SP_STATUS + AppConstants.PIPE_CHARACTER
							+ MedicationRequest.SP_ENCOUNTER,

					MedicationRequest.SP_PATIENT + AppConstants.PIPE_CHARACTER + MedicationRequest.SP_INTENT
							+ AppConstants.PIPE_CHARACTER + MedicationRequest.SP_STATUS + AppConstants.PIPE_CHARACTER
							+ MedicationRequest.SP_ENCOUNTER + AppConstants.PIPE_CHARACTER
							+ AppConstants.MEDICATIONREQUEST_MEDICATION,

					MedicationRequest.SP_PATIENT + AppConstants.PIPE_CHARACTER + MedicationRequest.SP_INTENT
							+ AppConstants.PIPE_CHARACTER + MedicationRequest.SP_AUTHOREDON,

					MedicationRequest.SP_PATIENT + AppConstants.PIPE_CHARACTER + MedicationRequest.SP_INTENT
							+ AppConstants.PIPE_CHARACTER + MedicationRequest.SP_AUTHOREDON
							+ AppConstants.PIPE_CHARACTER + AppConstants.MEDICATIONREQUEST_MEDICATION,

					MedicationRequest.SP_PATIENT + AppConstants.PIPE_CHARACTER + MedicationRequest.SP_AUTHOREDON,

					MedicationRequest.SP_PATIENT + AppConstants.PIPE_CHARACTER + MedicationRequest.SP_AUTHOREDON
							+ AppConstants.PIPE_CHARACTER + AppConstants.MEDICATIONREQUEST_MEDICATION,

					MedicationRequest.SP_PATIENT));

			resourceParameterMap.put(ResourceTypes.OBSERVATION.toString(), Arrays.asList(Observation.SP_RES_ID,

					Observation.SP_PATIENT + AppConstants.PIPE_CHARACTER + Observation.SP_CATEGORY
							+ AppConstants.PIPE_CHARACTER + Observation.SP_DATE,
					Observation.SP_PATIENT + AppConstants.PIPE_CHARACTER + Observation.SP_CODE
							+ AppConstants.PIPE_CHARACTER + Observation.SP_DATE,

					Observation.SP_PATIENT + AppConstants.PIPE_CHARACTER + Observation.SP_CATEGORY
							+ AppConstants.PIPE_CHARACTER + Observation.SP_STATUS,
					Observation.SP_PATIENT + AppConstants.PIPE_CHARACTER + Observation.SP_CATEGORY
							+ AppConstants.PIPE_CHARACTER + Observation.SP_ENCOUNTER,

					Observation.SP_PATIENT + AppConstants.PIPE_CHARACTER + Observation.SP_CATEGORY,
					Observation.SP_PATIENT + AppConstants.PIPE_CHARACTER + Observation.SP_CODE));
			resourceParameterMap.put(ResourceTypes.PATIENT.toString(),
					Arrays.asList(Patient.SP_RES_ID, Patient.SP_NAME + AppConstants.PIPE_CHARACTER + Patient.SP_GENDER,
							Patient.SP_NAME + AppConstants.PIPE_CHARACTER + Patient.SP_BIRTHDATE,
							Patient.SP_FAMILY + AppConstants.PIPE_CHARACTER + Patient.SP_BIRTHDATE,
							Patient.SP_FAMILY + AppConstants.PIPE_CHARACTER + Patient.SP_GENDER, Patient.SP_NAME,
							Patient.SP_IDENTIFIER));

			resourceParameterMap.put(ResourceTypes.PRACTITIONER.toString(),
					Arrays.asList(Practitioner.SP_RES_ID, Practitioner.SP_NAME, Practitioner.SP_IDENTIFIER));

			resourceParameterMap.put(ResourceTypes.PRACTITIONERROLE.toString(),
					Arrays.asList(PractitionerRole.SP_RES_ID, PractitionerRole.SP_SPECIALTY,
							AppConstants.PRACTITIONER_IDENTIFIER, AppConstants.PRACTITIONER_NAME));
			resourceParameterMap.put(ResourceTypes.IMMUNIZATION.toString(),
					Arrays.asList(Immunization.SP_RES_ID,
							Procedure.SP_PATIENT + AppConstants.PIPE_CHARACTER + Procedure.SP_DATE,
							Procedure.SP_PATIENT + AppConstants.PIPE_CHARACTER + Procedure.SP_STATUS,
							AppConstants.ENCOUNTER_PARAMETER, Immunization.SP_PATIENT));

			resourceParameterMap.put(ResourceTypes.PROCEDURE.toString(), Arrays.asList(Procedure.SP_RES_ID,
					Procedure.SP_PATIENT + AppConstants.PIPE_CHARACTER + Procedure.SP_CODE + AppConstants.PIPE_CHARACTER
							+ Procedure.SP_DATE,
					Procedure.SP_PATIENT + AppConstants.PIPE_CHARACTER + Procedure.SP_DATE,
					Procedure.SP_PATIENT + AppConstants.PIPE_CHARACTER + Procedure.SP_STATUS,
					AppConstants.ENCOUNTER_PARAMETER, Procedure.SP_PATIENT));

			resourceParameterMap.put(ResourceTypes.MEDICATIONADMINISTRATION.toString(),
					Arrays.asList(
							MedicationAdministration.SP_RES_ID, MedicationAdministration.SP_PATIENT
									+ AppConstants.PIPE_CHARACTER + MedicationAdministration.SP_CONTEXT,
							MedicationAdministration.SP_PATIENT));

			resourceParameterMap.put(ResourceTypes.MEDICATION.toString(), Arrays.asList(Medication.SP_RES_ID));
			resourceParameterMap.put(ResourceTypes.PROVENANCE.toString(), Arrays.asList(Provenance.SP_RES_ID));
		} catch (Exception ex) {
			logger.error("Exception while loading Resource to Parameter Mapping ", ex);
		}
	}

	static boolean findValidParameters(String resourceType, String parameters) {
		try {
			logger.debug(" parameters >>>>{} ", parameters);
			if (StringUtils.isNotBlank(resourceType) && StringUtils.isNotBlank(parameters)) {
				if (resourceParameterMap.containsKey(resourceType)) {
					List<String> combinations = resourceParameterMap.get(resourceType);
					if (combinations.contains(parameters)) {
						return true;
					}
				}
			} else if (resourceType.equals(ResourceTypes.PATIENT.toString()) && StringUtils.isBlank(parameters)) {
				return true;
			}
		} catch (Exception e) {
			logger.error("Exception in findValidParameters() of ResourceProviderValidator class ", e);
		}
		return false;
	}

	public static String removeLastCharacter(String str) {
		if (StringUtils.isNotBlank(str) && str.charAt(str.length() - 1) == '|') {
			str = str.substring(0, str.length() - 1);
		}
		return str;
	}

	public static boolean validateRequest(SearchParameterMap paramMap, String Resource) {
		boolean isValid = false;
		StringBuilder currentParameters = new StringBuilder();

		if (Resource.equals(ResourceTypes.ALLERGYINTOLERANCE.toString())) {

			if (paramMap.get(AllergyIntolerance.SP_PATIENT) != null) {
				currentParameters.append(AllergyIntolerance.SP_PATIENT);
				currentParameters.append(AppConstants.PIPE_CHARACTER);
			}

			if (paramMap.get(AllergyIntolerance.SP_CLINICAL_STATUS) != null) {
				currentParameters.append(AllergyIntolerance.SP_CLINICAL_STATUS);
				currentParameters.append(AppConstants.PIPE_CHARACTER);
			}

			if (paramMap.get(AppConstants.ENCOUNTER_PARAMETER) != null) {
				currentParameters.append(AppConstants.ENCOUNTER_PARAMETER);
				currentParameters.append(AppConstants.PIPE_CHARACTER);
			}

			if (paramMap.get(AllergyIntolerance.SP_RES_ID) != null) {
				currentParameters.append(AllergyIntolerance.SP_RES_ID);
				currentParameters.append(AppConstants.PIPE_CHARACTER);
			}
			isValid = findValidParameters(ResourceTypes.ALLERGYINTOLERANCE.toString(),
					removeLastCharacter(currentParameters.toString()));
			logger.debug(" Allergy Intolerance :: isValid {} ", isValid);
		}

		if (Resource.equals(ResourceTypes.CAREPLAN.toString())) {

			if (paramMap.get(CarePlan.SP_PATIENT) != null) {
				currentParameters.append(CarePlan.SP_PATIENT);
				currentParameters.append(AppConstants.PIPE_CHARACTER);
			}

			if (paramMap.get(CarePlan.SP_CATEGORY) != null) {
				currentParameters.append(CarePlan.SP_CATEGORY);
				currentParameters.append(AppConstants.PIPE_CHARACTER);
			}

			if (paramMap.get(CarePlan.SP_STATUS) != null) {
				currentParameters.append(CarePlan.SP_STATUS);
				currentParameters.append(AppConstants.PIPE_CHARACTER);
			}

			if (paramMap.get(CarePlan.SP_DATE) != null) {
				currentParameters.append(CarePlan.SP_DATE);
				currentParameters.append(AppConstants.PIPE_CHARACTER);
			}

			if (paramMap.get(AppConstants.ENCOUNTER_PARAMETER) != null) {
				currentParameters.append(AppConstants.ENCOUNTER_PARAMETER);
				currentParameters.append(AppConstants.PIPE_CHARACTER);
			}

			if (paramMap.get(CarePlan.SP_RES_ID) != null) {
				currentParameters.append(CarePlan.SP_RES_ID);
				currentParameters.append(AppConstants.PIPE_CHARACTER);
			}

			isValid = findValidParameters(ResourceTypes.CAREPLAN.toString(),
					removeLastCharacter(currentParameters.toString()));
			logger.debug(" CarePlan :: isValid {} ", isValid);

		}
		if (Resource.equals(ResourceTypes.CARETEAM.toString())) {

			if (paramMap.get(CareTeam.SP_PATIENT) != null) {
				currentParameters.append(CareTeam.SP_PATIENT);
				currentParameters.append(AppConstants.PIPE_CHARACTER);
			}

			if (paramMap.get(CareTeam.SP_STATUS) != null) {
				currentParameters.append(CareTeam.SP_STATUS);
				currentParameters.append(AppConstants.PIPE_CHARACTER);
			}

			if (paramMap.get(CareTeam.SP_RES_ID) != null) {
				currentParameters.append(CareTeam.SP_RES_ID);
				currentParameters.append(AppConstants.PIPE_CHARACTER);
			}

			isValid = findValidParameters(ResourceTypes.CARETEAM.toString(),
					removeLastCharacter(currentParameters.toString()));
			logger.debug(" CareTeam :: isValid {} ", isValid);
		}

		if (Resource.equals(ResourceTypes.CONDITION.toString())) {

			if (paramMap.get(Condition.SP_PATIENT) != null) {
				currentParameters.append(Condition.SP_PATIENT);
				currentParameters.append(AppConstants.PIPE_CHARACTER);
			}

			if (paramMap.get(Condition.SP_ONSET_DATE) != null) {
				currentParameters.append(Condition.SP_ONSET_DATE);
				currentParameters.append(AppConstants.PIPE_CHARACTER);
			}
			if (paramMap.get(Condition.SP_CATEGORY) != null) {
				currentParameters.append(Condition.SP_CATEGORY);
				currentParameters.append(AppConstants.PIPE_CHARACTER);
			}

			if (paramMap.get(Condition.SP_ENCOUNTER) != null) {
				currentParameters.append(Condition.SP_ENCOUNTER);
				currentParameters.append(AppConstants.PIPE_CHARACTER);
			}

			if (paramMap.get(Condition.SP_RES_ID) != null) {
				currentParameters.append(Condition.SP_RES_ID);
				currentParameters.append(AppConstants.PIPE_CHARACTER);
			}

			isValid = findValidParameters(ResourceTypes.CONDITION.toString(),
					removeLastCharacter(currentParameters.toString()));
			logger.debug(" Condition :: isValid {} ", isValid);
		}

		if (Resource.equals(ResourceTypes.DEVICE.toString())) {

			if (paramMap.get(Device.SP_PATIENT) != null) {
				currentParameters.append(Device.SP_PATIENT);
				currentParameters.append(AppConstants.PIPE_CHARACTER);
			}
			if (paramMap.get(Device.SP_TYPE) != null) {
				currentParameters.append(Device.SP_TYPE);
				currentParameters.append(AppConstants.PIPE_CHARACTER);
			}
			if (paramMap.get(AppConstants.ENCOUNTER_PARAMETER) != null) {
				currentParameters.append(AppConstants.ENCOUNTER_PARAMETER);
				currentParameters.append(AppConstants.PIPE_CHARACTER);
			}

			if (paramMap.get(Device.SP_RES_ID) != null) {
				currentParameters.append(Device.SP_RES_ID);
				currentParameters.append(AppConstants.PIPE_CHARACTER);
			}

			isValid = findValidParameters(ResourceTypes.DEVICE.toString(),
					removeLastCharacter(currentParameters.toString()));
			logger.debug(" Device :: isValid {}", isValid);
		}
		if (Resource.equals(ResourceTypes.DIAGNOSTICREPORT.toString())) {

			if (paramMap.get(DiagnosticReport.SP_PATIENT) != null) {
				currentParameters.append(DiagnosticReport.SP_PATIENT);
				currentParameters.append(AppConstants.PIPE_CHARACTER);
			}

			if (paramMap.get(DiagnosticReport.SP_CATEGORY) != null) {
				currentParameters.append(DiagnosticReport.SP_CATEGORY);
				currentParameters.append(AppConstants.PIPE_CHARACTER);
			}

			if (paramMap.get(DiagnosticReport.SP_CODE) != null) {
				currentParameters.append(DiagnosticReport.SP_CODE);
				currentParameters.append(AppConstants.PIPE_CHARACTER);
			}

			if (paramMap.get(DiagnosticReport.SP_DATE) != null) {
				currentParameters.append(DiagnosticReport.SP_DATE);
				currentParameters.append(AppConstants.PIPE_CHARACTER);
			}

			if (paramMap.get(DiagnosticReport.SP_STATUS) != null) {
				currentParameters.append(DiagnosticReport.SP_STATUS);
				currentParameters.append(AppConstants.PIPE_CHARACTER);
			}

			if (paramMap.get(AppConstants.ENCOUNTER_PARAMETER) != null) {
				currentParameters.append(AppConstants.ENCOUNTER_PARAMETER);
				currentParameters.append(AppConstants.PIPE_CHARACTER);
			}
			if (paramMap.get(DiagnosticReport.SP_RES_ID) != null) {
				currentParameters.append(DiagnosticReport.SP_RES_ID);
				currentParameters.append(AppConstants.PIPE_CHARACTER);
			}
			isValid = findValidParameters(ResourceTypes.DIAGNOSTICREPORT.toString(),
					removeLastCharacter(currentParameters.toString()));
			logger.debug(" DiagnosticReport :: isValid {}", isValid);
		}
		if (Resource.equals(ResourceTypes.DOCUMENTREFERENCE.toString())) {

			if (paramMap.get(DocumentReference.SP_PATIENT) != null) {
				currentParameters.append(DocumentReference.SP_PATIENT);
				currentParameters.append(AppConstants.PIPE_CHARACTER);
			}

			if (paramMap.get(DocumentReference.SP_CATEGORY) != null) {
				currentParameters.append(DocumentReference.SP_CATEGORY);
				currentParameters.append(AppConstants.PIPE_CHARACTER);
			}

			if (paramMap.get(DocumentReference.SP_TYPE) != null) {
				currentParameters.append(DocumentReference.SP_TYPE);
				currentParameters.append(AppConstants.PIPE_CHARACTER);
			}

			if (paramMap.get(DocumentReference.SP_DATE) != null) {
				currentParameters.append(DocumentReference.SP_DATE);
				currentParameters.append(AppConstants.PIPE_CHARACTER);
			}

			if (paramMap.get(DocumentReference.SP_STATUS) != null) {
				currentParameters.append(DocumentReference.SP_STATUS);
				currentParameters.append(AppConstants.PIPE_CHARACTER);
			}

			if (paramMap.get(AppConstants.ENCOUNTER_PARAMETER) != null) {
				currentParameters.append(AppConstants.ENCOUNTER_PARAMETER);
				currentParameters.append(AppConstants.PIPE_CHARACTER);
			}
			if (paramMap.get(DocumentReference.SP_RES_ID) != null) {
				currentParameters.append(DocumentReference.SP_RES_ID);
				currentParameters.append(AppConstants.PIPE_CHARACTER);
			}
			isValid = findValidParameters(ResourceTypes.DOCUMENTREFERENCE.toString(),
					removeLastCharacter(currentParameters.toString()));
			logger.debug(" DocumentReference :: isValid {}", isValid);

		}
		if (Resource.equals(ResourceTypes.ENCOUNTER.toString())) {

			if (paramMap.get(Encounter.SP_PATIENT) != null) {
				currentParameters.append(Encounter.SP_PATIENT);
				currentParameters.append(AppConstants.PIPE_CHARACTER);
			}
			if (paramMap.get(Encounter.SP_DATE) != null) {
				currentParameters.append(Encounter.SP_DATE);
				currentParameters.append(AppConstants.PIPE_CHARACTER);
			}
			if (paramMap.get(Encounter.SP_CLASS) != null) {
				currentParameters.append(Encounter.SP_CLASS);
				currentParameters.append(AppConstants.PIPE_CHARACTER);
			}
			if (paramMap.get(Encounter.SP_TYPE) != null) {
				currentParameters.append(Encounter.SP_TYPE);
				currentParameters.append(AppConstants.PIPE_CHARACTER);
			}
			if (paramMap.get(Encounter.SP_STATUS) != null) {
				currentParameters.append(Encounter.SP_STATUS);
				currentParameters.append(AppConstants.PIPE_CHARACTER);
			}
			if (paramMap.get(Encounter.SP_IDENTIFIER) != null) {
				currentParameters.append(Encounter.SP_IDENTIFIER);
				currentParameters.append(AppConstants.PIPE_CHARACTER);
			}
			if (paramMap.get(Encounter.SP_RES_ID) != null) {
				currentParameters.append(Encounter.SP_RES_ID);
				currentParameters.append(AppConstants.PIPE_CHARACTER);
			}
			isValid = findValidParameters(ResourceTypes.ENCOUNTER.toString(),
					removeLastCharacter(currentParameters.toString()));
			logger.debug(" Encounter :: isValid {}", isValid);
		}
		if (Resource.equals(ResourceTypes.GOAL.toString())) {

			if (paramMap.get(Goal.SP_PATIENT) != null) {
				currentParameters.append(Goal.SP_PATIENT);
				currentParameters.append(AppConstants.PIPE_CHARACTER);
			}
			if (paramMap.get(Goal.SP_LIFECYCLE_STATUS) != null) {
				currentParameters.append(Goal.SP_LIFECYCLE_STATUS);
				currentParameters.append(AppConstants.PIPE_CHARACTER);
			}

			if (paramMap.get(AppConstants.ENCOUNTER_PARAMETER) != null) {
				currentParameters.append(AppConstants.ENCOUNTER_PARAMETER);
				currentParameters.append(AppConstants.PIPE_CHARACTER);
			}
			if (paramMap.get(Goal.SP_RES_ID) != null) {
				currentParameters.append(Goal.SP_RES_ID);
				currentParameters.append(AppConstants.PIPE_CHARACTER);
			}
			isValid = findValidParameters(ResourceTypes.GOAL.toString(),
					removeLastCharacter(currentParameters.toString()));
			logger.debug(" Goal :: isValid {}", isValid);
		}

		if (Resource.equals(ResourceTypes.IMMUNIZATION.toString())) {

			if (paramMap.get(Immunization.SP_PATIENT) != null) {
				currentParameters.append(Immunization.SP_PATIENT);
				currentParameters.append(AppConstants.PIPE_CHARACTER);
			}

			if (paramMap.get(Immunization.SP_DATE) != null) {
				currentParameters.append(Immunization.SP_DATE);
				currentParameters.append(AppConstants.PIPE_CHARACTER);
			}

			if (paramMap.get(Immunization.SP_STATUS) != null) {
				currentParameters.append(Immunization.SP_STATUS);
				currentParameters.append(AppConstants.PIPE_CHARACTER);
			}

			if (paramMap.get(AppConstants.ENCOUNTER_PARAMETER) != null) {
				currentParameters.append(AppConstants.ENCOUNTER_PARAMETER);
				currentParameters.append(AppConstants.PIPE_CHARACTER);
			}
			if (paramMap.get(Immunization.SP_RES_ID) != null) {
				currentParameters.append(Immunization.SP_RES_ID);
				currentParameters.append(AppConstants.PIPE_CHARACTER);
			}
			isValid = findValidParameters(ResourceTypes.IMMUNIZATION.toString(),
					removeLastCharacter(currentParameters.toString()));
			logger.debug(" Immunization :: isValid {}", isValid);

		}

		if (Resource.equals(ResourceTypes.LOCATION.toString())) {
			if (paramMap.get(Location.SP_NAME) != null) {
				currentParameters.append(Location.SP_NAME);
				currentParameters.append(AppConstants.PIPE_CHARACTER);
			}
			if (paramMap.get(Location.SP_ADDRESS) != null) {
				currentParameters.append(Location.SP_ADDRESS);
				currentParameters.append(AppConstants.PIPE_CHARACTER);
			}

			if (paramMap.get(Location.SP_ADDRESS_CITY) != null) {
				currentParameters.append(Location.SP_ADDRESS_CITY);
				currentParameters.append(AppConstants.PIPE_CHARACTER);
			}
			if (paramMap.get(Location.SP_ADDRESS_STATE) != null) {
				currentParameters.append(Location.SP_ADDRESS_STATE);
				currentParameters.append(AppConstants.PIPE_CHARACTER);
			}
			if (paramMap.get(Location.SP_ADDRESS_POSTALCODE) != null) {
				currentParameters.append(Location.SP_ADDRESS_POSTALCODE);
				currentParameters.append(AppConstants.PIPE_CHARACTER);
			}
			if (paramMap.get(Location.SP_RES_ID) != null) {
				currentParameters.append(Location.SP_RES_ID);
				currentParameters.append(AppConstants.PIPE_CHARACTER);
			}
			isValid = findValidParameters(ResourceTypes.LOCATION.toString(),
					removeLastCharacter(currentParameters.toString()));
			logger.debug(" Location :: isValid {}", isValid);
		}
		if (Resource.equals(ResourceTypes.MEDICATIONREQUEST.toString())) {

			if (paramMap.get(MedicationRequest.SP_PATIENT) != null) {
				currentParameters.append(MedicationRequest.SP_PATIENT);
				currentParameters.append(AppConstants.PIPE_CHARACTER);
			}

			if (paramMap.get(MedicationRequest.SP_INTENT) != null) {
				currentParameters.append(MedicationRequest.SP_INTENT);
				currentParameters.append(AppConstants.PIPE_CHARACTER);
			}

			if (paramMap.get(MedicationRequest.SP_STATUS) != null) {
				currentParameters.append(MedicationRequest.SP_STATUS);
				currentParameters.append(AppConstants.PIPE_CHARACTER);
			}
			if (paramMap.get(MedicationRequest.SP_ENCOUNTER) != null) {
				currentParameters.append(MedicationRequest.SP_ENCOUNTER);
				currentParameters.append(AppConstants.PIPE_CHARACTER);
			}
			if (paramMap.get(MedicationRequest.SP_AUTHOREDON) != null) {
				currentParameters.append(MedicationRequest.SP_AUTHOREDON);
				currentParameters.append(AppConstants.PIPE_CHARACTER);
			}

			if (paramMap.get(MedicationRequest.SP_RES_ID) != null) {
				currentParameters.append(MedicationRequest.SP_RES_ID);
				currentParameters.append(AppConstants.PIPE_CHARACTER);
			}
			isValid = findValidParameters(ResourceTypes.MEDICATIONREQUEST.toString(),
					removeLastCharacter(currentParameters.toString()));
			logger.debug(" MedicationRequest :: isValid {}", isValid);
		}

		if (Resource.equals(ResourceTypes.OBSERVATION.toString())) {

			if (paramMap.get(Observation.SP_PATIENT) != null) {
				currentParameters.append(Observation.SP_PATIENT);
				currentParameters.append(AppConstants.PIPE_CHARACTER);
			}

			if (paramMap.get(Observation.SP_CATEGORY) != null) {
				currentParameters.append(Observation.SP_CATEGORY);
				currentParameters.append(AppConstants.PIPE_CHARACTER);
			}

			if (paramMap.get(Observation.SP_CODE) != null) {
				currentParameters.append(Observation.SP_CODE);
				currentParameters.append(AppConstants.PIPE_CHARACTER);
			}

			if (paramMap.get(Observation.SP_DATE) != null) {
				currentParameters.append(Observation.SP_DATE);
				currentParameters.append(AppConstants.PIPE_CHARACTER);
			}

			if (paramMap.get(Observation.SP_STATUS) != null) {
				currentParameters.append(Observation.SP_STATUS);
				currentParameters.append(AppConstants.PIPE_CHARACTER);
			}

			if (paramMap.get(Observation.SP_ENCOUNTER) != null) {
				currentParameters.append(Observation.SP_ENCOUNTER);
				currentParameters.append(AppConstants.PIPE_CHARACTER);
			}
			if (paramMap.get(Observation.SP_RES_ID) != null) {
				currentParameters.append(Observation.SP_RES_ID);
				currentParameters.append(AppConstants.PIPE_CHARACTER);
			}
			isValid = findValidParameters(ResourceTypes.OBSERVATION.toString(),
					removeLastCharacter(currentParameters.toString()));
			logger.debug(" Observation :: isValid {}", isValid);
		}
		if (Resource.equals(ResourceTypes.ORGANIZATION.toString())) {

			if (paramMap.get(Organization.SP_NAME) != null) {
				currentParameters.append(Organization.SP_NAME);
				currentParameters.append(AppConstants.PIPE_CHARACTER);
			}
			if (paramMap.get(Organization.SP_ADDRESS) != null) {
				currentParameters.append(Organization.SP_ADDRESS);
				currentParameters.append(AppConstants.PIPE_CHARACTER);
			}
			if (paramMap.get(Organization.SP_RES_ID) != null) {
				currentParameters.append(Organization.SP_RES_ID);
				currentParameters.append(AppConstants.PIPE_CHARACTER);
			}
			isValid = findValidParameters(ResourceTypes.ORGANIZATION.toString(),
					removeLastCharacter(currentParameters.toString()));
			logger.debug(" Organization :: isValid {}", isValid);

		}
		if (Resource.equals(ResourceTypes.PATIENT.toString())) {

			if (paramMap.get(Patient.SP_NAME) != null) {
				currentParameters.append(Patient.SP_NAME);
				currentParameters.append(AppConstants.PIPE_CHARACTER);
			}
			if (paramMap.get(Patient.SP_FAMILY) != null) {
				currentParameters.append(Patient.SP_FAMILY);
				currentParameters.append(AppConstants.PIPE_CHARACTER);
			}
			if (paramMap.get(Patient.SP_BIRTHDATE) != null) {
				currentParameters.append(Patient.SP_BIRTHDATE);
				currentParameters.append(AppConstants.PIPE_CHARACTER);
			}
			if (paramMap.get(Patient.SP_GENDER) != null) {
				currentParameters.append(Patient.SP_GENDER);
				currentParameters.append(AppConstants.PIPE_CHARACTER);
			}
			if (paramMap.get(Patient.SP_IDENTIFIER) != null) {
				currentParameters.append(Patient.SP_IDENTIFIER);
				currentParameters.append(AppConstants.PIPE_CHARACTER);
			}
			if (paramMap.get(Patient.SP_RES_ID) != null) {
				currentParameters.append(Patient.SP_RES_ID);
				currentParameters.append(AppConstants.PIPE_CHARACTER);
			}

			isValid = findValidParameters(ResourceTypes.PATIENT.toString(),
					removeLastCharacter(currentParameters.toString()));
			logger.debug(" Patient :: isValid {}", isValid);
		}

		if (Resource.equals(ResourceTypes.PRACTITIONER.toString())) {

			if (paramMap.get(Practitioner.SP_NAME) != null) {
				currentParameters.append(Practitioner.SP_NAME);
				currentParameters.append(AppConstants.PIPE_CHARACTER);
			}
			if (paramMap.get(Practitioner.SP_IDENTIFIER) != null) {
				currentParameters.append(Practitioner.SP_IDENTIFIER);
				currentParameters.append(AppConstants.PIPE_CHARACTER);
			}
			if (paramMap.get(Practitioner.SP_RES_ID) != null) {
				currentParameters.append(Practitioner.SP_RES_ID);
				currentParameters.append(AppConstants.PIPE_CHARACTER);
			}
			isValid = findValidParameters(ResourceTypes.PRACTITIONER.toString(),
					removeLastCharacter(currentParameters.toString()));
			logger.debug(" Practitioner :: isValid {}", isValid);
		}
		if (Resource.equals(ResourceTypes.PRACTITIONERROLE.toString())) {

			if (paramMap.get(PractitionerRole.SP_SPECIALTY) != null) {
				currentParameters.append(PractitionerRole.SP_SPECIALTY);
				currentParameters.append(AppConstants.PIPE_CHARACTER);
			}

			if ((paramMap.get(AppConstants.PRACTITIONER_IDENTIFIER) != null)
					&& !paramMap.get(AppConstants.PRACTITIONER_IDENTIFIER).isEmpty()) {
				logger.debug(" Practitioner.identifier");
				currentParameters.append(AppConstants.PRACTITIONER_IDENTIFIER);
				currentParameters.append(AppConstants.PIPE_CHARACTER);
			}

			if ((paramMap.get(AppConstants.PRACTITIONER_NAME) != null)
					&& !paramMap.get(AppConstants.PRACTITIONER_NAME).isEmpty()) {
				logger.debug(" Practitioner.name");
				currentParameters.append(AppConstants.PRACTITIONER_NAME);
				currentParameters.append(AppConstants.PIPE_CHARACTER);
			}
			if (paramMap.get(PractitionerRole.SP_RES_ID) != null) {
				currentParameters.append(PractitionerRole.SP_RES_ID);
				currentParameters.append(AppConstants.PIPE_CHARACTER);
			}

			isValid = findValidParameters(ResourceTypes.PRACTITIONERROLE.toString(),
					removeLastCharacter(currentParameters.toString()));
			logger.debug(" PractitionerRole :: isValid {}", isValid);
		}
		if (Resource.equals(ResourceTypes.PROCEDURE.toString())) {

			if (paramMap.get(Procedure.SP_PATIENT) != null) {
				currentParameters.append(Procedure.SP_PATIENT);
				currentParameters.append(AppConstants.PIPE_CHARACTER);
			}

			if (paramMap.get(Procedure.SP_CODE) != null) {
				currentParameters.append(Procedure.SP_CODE);
				currentParameters.append(AppConstants.PIPE_CHARACTER);
			}

			if (paramMap.get(Procedure.SP_DATE) != null) {
				currentParameters.append(Procedure.SP_DATE);
				currentParameters.append(AppConstants.PIPE_CHARACTER);
			}

			if (paramMap.get(Procedure.SP_STATUS) != null) {
				currentParameters.append(Procedure.SP_STATUS);
				currentParameters.append(AppConstants.PIPE_CHARACTER);
			}

			if (paramMap.get(AppConstants.ENCOUNTER_PARAMETER) != null) {
				currentParameters.append(AppConstants.ENCOUNTER_PARAMETER);
				currentParameters.append(AppConstants.PIPE_CHARACTER);
			}
			if (paramMap.get(Procedure.SP_RES_ID) != null) {
				currentParameters.append(Procedure.SP_RES_ID);
				currentParameters.append(AppConstants.PIPE_CHARACTER);
			}
			isValid = findValidParameters(ResourceTypes.PROCEDURE.toString(),
					removeLastCharacter(currentParameters.toString()));
			logger.debug(" Procedure :: isValid {}", isValid);

		}

		if (Resource.equals(ResourceTypes.MEDICATIONADMINISTRATION.toString())) {

			if (paramMap.get(MedicationAdministration.SP_PATIENT) != null) {
				currentParameters.append(MedicationAdministration.SP_PATIENT);
				currentParameters.append(AppConstants.PIPE_CHARACTER);
			}

			if (paramMap.get(MedicationAdministration.SP_CONTEXT) != null) {
				currentParameters.append(MedicationAdministration.SP_CONTEXT);
				currentParameters.append(AppConstants.PIPE_CHARACTER);
			}

			if (paramMap.get(MedicationAdministration.SP_RES_ID) != null) {
				currentParameters.append(MedicationAdministration.SP_RES_ID);
				currentParameters.append(AppConstants.PIPE_CHARACTER);
			}

			isValid = findValidParameters(ResourceTypes.MEDICATIONADMINISTRATION.toString(),
					removeLastCharacter(currentParameters.toString()));
			logger.debug(" MedicationAdministration :: isValid {}", isValid);
		}
		if (Resource.equals(ResourceTypes.MEDICATION.toString())) {
			if (paramMap.get(Medication.SP_RES_ID) != null) {
				currentParameters.append(Medication.SP_RES_ID);
				currentParameters.append(AppConstants.PIPE_CHARACTER);
			}

			isValid = findValidParameters(ResourceTypes.MEDICATION.toString(),
					removeLastCharacter(currentParameters.toString()));
			logger.debug(" Medication :: isValid {}", isValid);
		}
		if (Resource.equals(ResourceTypes.PROVENANCE.toString())) {
			if (paramMap.get(Provenance.SP_RES_ID) != null) {
				currentParameters.append(Provenance.SP_RES_ID);
				currentParameters.append(AppConstants.PIPE_CHARACTER);
			}

			isValid = findValidParameters(ResourceTypes.PROVENANCE.toString(),
					removeLastCharacter(currentParameters.toString()));
			logger.debug(" Provenance :: isValid {}", isValid);
		}

		return isValid;
	}

}
