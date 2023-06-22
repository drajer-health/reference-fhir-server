package com.interopx.fhir.validator.resource.content.comparer;

import org.apache.commons.lang3.ObjectUtils;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.interopx.fhir.validator.util.ComparatorUtils;
import com.interopx.fhir.validator.util.ScenarioConstants.ResourceNames;
import com.interopx.fhir.validator.util.ScenarioConstants.ScenarioNameConstants;
/**
 * This Class compares source/input Encounter resource content with goldJson
 * 
 * @author Swarna Nelaturi
 *
 */
public class EncounterContentComparator {

	public static final Logger logger = LoggerFactory.getLogger(PatientContentComparator.class);

	public static void compare(Resource sourceResource, Resource targetResource, String scenarioName,
			OperationOutcome operationOutcome) {
		logger.info("Entry - EncounterContentComparator.compare");
		logger.info("Entry - EncounterContentComparator.compare - sourceResource ::\n" + sourceResource);
		logger.info("Entry - EncounterContentComparator.compare - targetResource ::\n" + targetResource);
		if (ObjectUtils.isNotEmpty(sourceResource) && ObjectUtils.isNotEmpty(targetResource)) {
			Encounter sourceEncounter = (Encounter) sourceResource;
			Encounter targetEncounter = (Encounter) targetResource;
			logger.info("sourceEncounter :::::\n" + sourceEncounter);
			if (scenarioName.equalsIgnoreCase(ScenarioNameConstants.USCDI_FULL_ENCOUNTER)) {
				logger.info("sourceEncounter ::::: compareFullContent\n");
				compareFullContent(sourceEncounter, targetEncounter, operationOutcome,scenarioName);
				logger.info("sourceEncounter ::::: compareFullContent\n");
			} else if (scenarioName.equalsIgnoreCase(ScenarioNameConstants.USCDI_ADDITIONAL_ENCOUNTER)) {
				compareAdditionalContent(sourceEncounter, targetEncounter, operationOutcome,scenarioName);
			}
		}
		logger.info("Exit - EncounterContentComparator.compare");
	}

	private static void compareFullContent(Encounter sourceEncounter, Encounter targetEncounter,
			OperationOutcome operationOutcome,String scenarioName) {

		logger.info("Entry - EncounterContentComparator.compareFullContent");

		//id
		ComparatorUtils.compareString(ResourceNames.ID, sourceEncounter.getId(),
				targetEncounter.getId(), operationOutcome,scenarioName);
		// identifier
		ComparatorUtils.compareListOfIdentifier(ResourceNames.IDENTIFIER, sourceEncounter.getIdentifier(),
				targetEncounter.getIdentifier(), operationOutcome,scenarioName);
		// class
		ComparatorUtils.compareCoding(ResourceNames.CLASS, sourceEncounter.getClass_(), targetEncounter.getClass_(),
						operationOutcome,scenarioName);
		// status
		String source = null;
		String target=null;
		if(sourceEncounter.getStatus()!=null) {
			source = sourceEncounter.getStatus().toString();	
		}
		if(targetEncounter.getStatus()!=null) {
			target = targetEncounter.getStatus().toString();	
		}
		ComparatorUtils.compareString(ResourceNames.STATUS, source,target, operationOutcome,scenarioName);
		// type
		ComparatorUtils.compareListOfCodeableConcept(ResourceNames.TYPE, sourceEncounter.getType(),
				targetEncounter.getType(), operationOutcome,scenarioName);
		// service type
		ComparatorUtils.compareCodeableConcept(ResourceNames.SERVICE_TYPE, sourceEncounter.getServiceType(),
				targetEncounter.getServiceType(), operationOutcome,scenarioName);
		// priority
		ComparatorUtils.compareCodeableConcept(ResourceNames.PRIORITY, sourceEncounter.getPriority(),
				targetEncounter.getPriority(), operationOutcome,scenarioName);
		
		//contained
     	ComparatorUtils.compareListOfResource(ResourceNames.CONTAINED, sourceEncounter.getContained(), sourceEncounter.getContained(), operationOutcome,scenarioName);

		// reasonCode
		ComparatorUtils.compareListOfCodeableConcept(ResourceNames.REASON_CODE, sourceEncounter.getReasonCode(),
				targetEncounter.getReasonCode(), operationOutcome,scenarioName);
		// subject
		ComparatorUtils.compareReference(ResourceNames.SUBJECT, sourceEncounter.getSubject(),
				targetEncounter.getSubject(), operationOutcome,scenarioName);
		// episodeOfCare
		ComparatorUtils.compareListOfReference(ResourceNames.EPISODE_OF_CARE, sourceEncounter.getEpisodeOfCare(),
				targetEncounter.getEpisodeOfCare(), operationOutcome,scenarioName);
		// basedOn
		ComparatorUtils.compareListOfReference(ResourceNames.BASED_ON, sourceEncounter.getBasedOn(),
				targetEncounter.getBasedOn(), operationOutcome,scenarioName);
		// appointment
		ComparatorUtils.compareListOfReference(ResourceNames.APPOINTMENT, sourceEncounter.getAppointment(),
				targetEncounter.getAppointment(), operationOutcome,scenarioName);
		// serviceProvider
		ComparatorUtils.compareReference(ResourceNames.SERVICE_PROVIDER, sourceEncounter.getServiceProvider(),
				targetEncounter.getServiceProvider(), operationOutcome,scenarioName);
		// partOf
		ComparatorUtils.compareReference(ResourceNames.PART_OF, sourceEncounter.getPartOf(),
				targetEncounter.getPartOf(), operationOutcome,scenarioName);
		// reasonReference
		ComparatorUtils.compareListOfReference(ResourceNames.REASON_REFERENCE, sourceEncounter.getReasonReference(),
				targetEncounter.getReasonReference(), operationOutcome,scenarioName);
		// account
		ComparatorUtils.compareListOfReference(ResourceNames.ACCOUNT, sourceEncounter.getAccount(),
				targetEncounter.getAccount(), operationOutcome,scenarioName);
		// statusHistory
		ComparatorUtils.compareListOfStatusHistory(ResourceNames.STATUS_HISTORY, sourceEncounter.getStatusHistory(),
				targetEncounter.getStatusHistory(), operationOutcome,scenarioName);
		//length
		ComparatorUtils.compareQuantity(ResourceNames.LENGTH, sourceEncounter.getLength(),
				targetEncounter.getLength(), operationOutcome,scenarioName);
		// period
		ComparatorUtils.comparePeriod(ResourceNames.PERIOD, sourceEncounter.getPeriod(), targetEncounter.getPeriod(),
						operationOutcome,scenarioName);
		// participant
		ComparatorUtils.compareListOfEncounterParticipantComponent(ResourceNames.PARTICIPANT,
				sourceEncounter.getParticipant(), targetEncounter.getParticipant(), operationOutcome,scenarioName);
		// diagnosis
		ComparatorUtils.compareListOfDiagnosisComponent(ResourceNames.DIAGNOSIS, sourceEncounter.getDiagnosis(),
				targetEncounter.getDiagnosis(), operationOutcome,scenarioName);
		// location
		ComparatorUtils.compareListOfEncounterLocationComponent(ResourceNames.LOCATION, sourceEncounter.getLocation(),
				targetEncounter.getLocation(), operationOutcome,scenarioName);
		// classHistory
		ComparatorUtils.compareListOfClassHistoryComponent(ResourceNames.CLASS_HISTORY,
				sourceEncounter.getClassHistory(), targetEncounter.getClassHistory(), operationOutcome,scenarioName);
		// hospitalization
		ComparatorUtils.compareEncounterHospitalizationComponent(ResourceNames.HOSPITALIZATION,
				sourceEncounter.getHospitalization(), targetEncounter.getHospitalization(), operationOutcome,scenarioName);
		
		logger.info("Exit - EncounterContentComparator.compareFullContent");
	}

	private static void compareAdditionalContent(Encounter sourceEncounter, Encounter targetEncounter,
			OperationOutcome operationOutcome,String scenarioName) {

		logger.info("Entry - EncounterContentComparator.compareAdditionalContent");
		
		// identifier
		ComparatorUtils.compareListOfIdentifier(ResourceNames.IDENTIFIER, sourceEncounter.getIdentifier(),
				targetEncounter.getIdentifier(), operationOutcome,scenarioName);
		// status
		String source = null;
		String target=null;
		if(sourceEncounter.getStatus()!=null) {
			source = sourceEncounter.getStatus().toString();	
		}
		if(targetEncounter.getStatus()!=null) {
			target = targetEncounter.getStatus().toString();	
		}
		ComparatorUtils.compareString(ResourceNames.STATUS, source,target, operationOutcome,scenarioName);
		// class
		ComparatorUtils.compareCoding(ResourceNames.CLASS, sourceEncounter.getClass_(), targetEncounter.getClass_(),
				operationOutcome,scenarioName);
		// type
		ComparatorUtils.compareListOfCodeableConcept(ResourceNames.TYPE, sourceEncounter.getType(),
				targetEncounter.getType(), operationOutcome,scenarioName);
		// subject
		ComparatorUtils.compareReference(ResourceNames.SUBJECT, sourceEncounter.getSubject(),
				targetEncounter.getSubject(), operationOutcome,scenarioName);
		// participant
		ComparatorUtils.compareListOfEncounterParticipantComponent(ResourceNames.PARTICIPANT,
				sourceEncounter.getParticipant(), targetEncounter.getParticipant(), operationOutcome,scenarioName);
		// period
		ComparatorUtils.comparePeriod(ResourceNames.PERIOD, sourceEncounter.getPeriod(), targetEncounter.getPeriod(),
				operationOutcome,scenarioName);
		// reasonCode
		ComparatorUtils.compareListOfCodeableConcept(ResourceNames.REASON_CODE, sourceEncounter.getReasonCode(),
				targetEncounter.getReasonCode(), operationOutcome,scenarioName);
		// hospitalization
		ComparatorUtils.compareEncounterHospitalizationComponent(ResourceNames.HOSPITALIZATION,
				sourceEncounter.getHospitalization(), targetEncounter.getHospitalization(), operationOutcome,scenarioName);
		// location
		ComparatorUtils.compareListOfEncounterLocationComponent(ResourceNames.LOCATION, sourceEncounter.getLocation(),
				targetEncounter.getLocation(), operationOutcome,scenarioName);
		
		logger.info("Exit - EncounterContentComparator.compareAdditionalContent");
	}

}
