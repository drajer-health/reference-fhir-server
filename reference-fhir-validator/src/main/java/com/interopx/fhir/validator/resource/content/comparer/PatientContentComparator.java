package com.interopx.fhir.validator.resource.content.comparer;

import org.apache.commons.lang3.ObjectUtils;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.DateTimeType;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.interopx.fhir.validator.util.ComparatorUtils;
import com.interopx.fhir.validator.util.ScenarioConstants.ResourceNames;
import com.interopx.fhir.validator.util.ScenarioConstants.ScenarioNameConstants;
/**
 * This Class compares source/input Patient resource content with goldJson
 * 
 * @author Swarna Nelaturi
 *
 */
public class PatientContentComparator {
	public static final Logger logger=LoggerFactory.getLogger(PatientContentComparator.class);
	
	public static void compare(Resource sourceResource, Resource targetResource,String scenarioName,OperationOutcome operationOutcome) {
		logger.info("Entry - PatientContentComparator.compare");
		logger.info("Entry - PatientContentComparator.compare - sourceResource ::\n"+sourceResource);
		logger.info("Entry - PatientContentComparator.compare - targetResource ::\n"+targetResource);
		if (ObjectUtils.isNotEmpty(sourceResource) && ObjectUtils.isNotEmpty(targetResource)) {
			Patient sourcePatient = (Patient) sourceResource;
			Patient targetPatient = (Patient) targetResource;
			logger.info("sourcePatient :::::\n"+sourcePatient);
			if (scenarioName.equalsIgnoreCase(ScenarioNameConstants.USCDI_FULL_PATIENT)) {
				logger.info("sourcePatient ::::: compareFullContent\n");
				compareFullContent(sourcePatient, targetPatient,operationOutcome,scenarioName);
				logger.info("sourcePatient ::::: compareFullContent\n");
			}else if (scenarioName.equalsIgnoreCase(ScenarioNameConstants.USCDI_ADDITIONAL_PATIENT)) {
				compareAdditionalContent(sourcePatient, targetPatient,operationOutcome,scenarioName);
			}
		}
		logger.info("Exit - PatientContentComparator.compare");
	}

	private static void compareFullContent(Patient sourcePatient,
			Patient targetPatient,OperationOutcome operationOutcome,String scenarioName) {
		
		
		logger.info("Entry - PatientContentComparator.compareFullContent");
		//id
		ComparatorUtils.compareString(ResourceNames.ID,sourcePatient.getId(),targetPatient.getId(),operationOutcome,scenarioName);
		//identifier
		ComparatorUtils.compareListOfIdentifier(ResourceNames.IDENTIFIER,sourcePatient.getIdentifier(),targetPatient.getIdentifier(),operationOutcome,scenarioName);
		//extension
		ComparatorUtils.compareListOfExtension(ResourceNames.EXTENSION, sourcePatient.getExtension(), targetPatient.getExtension(), operationOutcome,scenarioName);
		//name
		ComparatorUtils.compareListOfHumanName(ResourceNames.NAME,sourcePatient.getName(), targetPatient.getName(),operationOutcome,scenarioName);
		//gender
		ComparatorUtils.compareGender(ResourceNames.GENDER,sourcePatient.getGenderElement(), targetPatient.getGenderElement(),operationOutcome,scenarioName);
		//birthDate
		ComparatorUtils.compareDate(ResourceNames.BIRTH_DATE,sourcePatient.getBirthDate(),targetPatient.getBirthDate(),operationOutcome,scenarioName);
		//address
		ComparatorUtils.compareListOfAddress(ResourceNames.ADDRESS,sourcePatient.getAddress(),targetPatient.getAddress(),operationOutcome,scenarioName);
		//telecom
		ComparatorUtils.compareListOfContactPoint(ResourceNames.TELECOM,sourcePatient.getTelecom(), targetPatient.getTelecom(),operationOutcome,scenarioName);
		//photo
		ComparatorUtils.compareListOfAttachment(ResourceNames.PHOTO,sourcePatient.getPhoto(), targetPatient.getPhoto(),operationOutcome,scenarioName);
		//contact
		ComparatorUtils.compareListOfContactComponent(ResourceNames.CONTACT,sourcePatient.getContact(), targetPatient.getContact(),operationOutcome,scenarioName);
		//maritalStatus
		ComparatorUtils.compareCodeableConcept(ResourceNames.MARITALSTATUS,sourcePatient.getMaritalStatus(), targetPatient.getMaritalStatus(),operationOutcome,scenarioName);
		//multipleBirthBoolean
		if (sourcePatient.getMultipleBirth() instanceof BooleanType &&  targetPatient.getMultipleBirth() instanceof BooleanType) {
			ComparatorUtils.compareBooleanType(ResourceNames.MULTIPLEBIRTH,sourcePatient.getMultipleBirthBooleanType(), targetPatient.getMultipleBirthBooleanType(),operationOutcome,scenarioName);
		}
		//deceased
		if (sourcePatient.getDeceased() instanceof BooleanType ||  targetPatient.getDeceased() instanceof BooleanType) {
			ComparatorUtils.compareBooleanType(ResourceNames.DECEASED,sourcePatient.getDeceasedBooleanType(), targetPatient.getDeceasedBooleanType(),operationOutcome,scenarioName);
		}
		if (sourcePatient.getDeceased() instanceof DateTimeType ||  targetPatient.getDeceased() instanceof DateTimeType) {
			ComparatorUtils.compareDateTimeType(ResourceNames.DECEASED,sourcePatient.getDeceasedDateTimeType(), targetPatient.getDeceasedDateTimeType(),operationOutcome,scenarioName);
		}
		
		//communication
		ComparatorUtils.compareListOfCommunication(ResourceNames.COMMUNICATION,sourcePatient.getCommunication(), targetPatient.getCommunication(),operationOutcome,scenarioName);
		//managingOrganization
		ComparatorUtils.compareReference(ResourceNames.MANAGIGN_ORGANIZATION,sourcePatient.getManagingOrganization(), targetPatient.getManagingOrganization(),operationOutcome,scenarioName);
		//generalPractitioner
		ComparatorUtils.compareListOfReference(ResourceNames.GENERAL_PRACTITIONER,sourcePatient.getGeneralPractitioner(), targetPatient.getGeneralPractitioner(),operationOutcome,scenarioName);
		
		logger.info("Exit - PatientContentComparator.compareFullContent");
	}

	private static void compareAdditionalContent(Patient sourcePatient,
			Patient targetPatient,OperationOutcome operationOutcome,String scenarioName) {
		
		logger.info("Entry - PatientContentComparator.compareAdditionalContent");
		
		//extension for us-core-race ,us-core-ethnicity,us-core-birthsex
		ComparatorUtils.compareListOfExtension(ResourceNames.EXTENSION, sourcePatient.getExtension(), targetPatient.getExtension(), operationOutcome,scenarioName);
		//identifier
		ComparatorUtils.compareListOfIdentifier(ResourceNames.IDENTIFIER,sourcePatient.getIdentifier(),targetPatient.getIdentifier(),operationOutcome,scenarioName);
		// name
		ComparatorUtils.compareListOfHumanName(ResourceNames.NAME,sourcePatient.getName(), targetPatient.getName(),operationOutcome,scenarioName);
		//telecom
		ComparatorUtils.compareListOfContactPoint(ResourceNames.TELECOM,sourcePatient.getTelecom(), targetPatient.getTelecom(),operationOutcome,scenarioName);
		// gender
		ComparatorUtils.compareGender(ResourceNames.GENDER,sourcePatient.getGenderElement(), targetPatient.getGenderElement(),operationOutcome,scenarioName);
		// birthDate
		ComparatorUtils.compareDate(ResourceNames.BIRTH_DATE,sourcePatient.getBirthDate(), targetPatient.getBirthDate(),operationOutcome,scenarioName);
		// address
		ComparatorUtils.compareListOfAddress(ResourceNames.ADDRESS,sourcePatient.getAddress(), targetPatient.getAddress(),operationOutcome,scenarioName);
		//communication
		ComparatorUtils.compareListOfCommunication(ResourceNames.COMMUNICATION,sourcePatient.getCommunication(), targetPatient.getCommunication(),operationOutcome,scenarioName);
		
		
		logger.info("Entry - PatientContentComparator.compareAdditionalContent");
	}
}
