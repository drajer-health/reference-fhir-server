package com.interopx.fhir.validator.resource.content.comparer;

import org.apache.commons.lang3.ObjectUtils;
import org.hl7.fhir.r4.model.AllergyIntolerance;
import org.hl7.fhir.r4.model.CareTeam;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.interopx.fhir.validator.util.ComparatorUtils;
import com.interopx.fhir.validator.util.ScenarioConstants.ResourceNames;
import com.interopx.fhir.validator.util.ScenarioConstants.ScenarioNameConstants;

public class CareTeamContentComparator {
public static final Logger logger=LoggerFactory.getLogger(CareTeamContentComparator.class);
	
	public static void compare(Resource sourceResource, Resource targetResource,String scenarioName,OperationOutcome operationOutcome) {
		
		logger.info("Entry - CareTeamContentComparator.compare");
		logger.info("Entry - CareTeamContentComparator.compare - sourceResource ::\n"+sourceResource);
		logger.info("Entry - CareTeamContentComparator.compare - targetResource ::\n"+targetResource);
		
		if (ObjectUtils.isNotEmpty(sourceResource) && ObjectUtils.isNotEmpty(targetResource)) {
			CareTeam sourceCareTeam = (CareTeam) sourceResource;
			CareTeam targetCareTeam = (CareTeam) targetResource;
			logger.info("sourceCareTeam :::::\n"+sourceCareTeam);
			if (scenarioName.equalsIgnoreCase(ScenarioNameConstants.USCDI_FULL_CARETEAM)) {
				logger.info("sourceCareTeam ::::: compareFullContent\n");
				compareFullContent(sourceCareTeam, targetCareTeam,operationOutcome,scenarioName);
				logger.info("sourceCareTeam ::::: compareFullContent\n");
			}else if (scenarioName.equalsIgnoreCase(ScenarioNameConstants.USCDI_ADDITIONAL_CARETEAM)) {
				compareAdditionalContent(sourceCareTeam, targetCareTeam,operationOutcome,scenarioName);
			}
		}
		logger.info("Exit - CareTeamContentComparator.compare");
	}

	private static void compareAdditionalContent(CareTeam sourceCareTeam, CareTeam targetCareTeam,
			OperationOutcome operationOutcome,String scenarioName) {
		//status
		String source = null;
		String target=null;
		if(sourceCareTeam.getStatus()!=null) {
			source = sourceCareTeam.getStatus().toString();	
		}
		if(targetCareTeam.getStatus()!=null) {
			target = targetCareTeam.getStatus().toString();	
		}
		ComparatorUtils.compareString(ResourceNames.STATUS, source,target, operationOutcome,scenarioName);
		//subject
		ComparatorUtils.compareReference(ResourceNames.SUBJECT, sourceCareTeam.getSubject(),targetCareTeam.getSubject(), operationOutcome,scenarioName);	
		//participant
		ComparatorUtils.compareListOfCareTeamParticipantComponent(ResourceNames.PARTICIPANT,sourceCareTeam.getParticipant(), targetCareTeam.getParticipant(), operationOutcome,scenarioName);
				
	}

	private static void compareFullContent(CareTeam sourceCareTeam, CareTeam targetCareTeam,
			OperationOutcome operationOutcome,String scenarioName) {
		//id
        ComparatorUtils.compareString(ResourceNames.ID, sourceCareTeam.getId(), targetCareTeam.getId(), operationOutcome,scenarioName);
        //implicitRules
		ComparatorUtils.compareString(ResourceNames.IMPLICIT_RULES, sourceCareTeam.getImplicitRules(), targetCareTeam.getImplicitRules(), operationOutcome,scenarioName);
		//language
		ComparatorUtils.compareLanguage(ResourceNames.LANGUAGE, sourceCareTeam.getLanguage(), targetCareTeam.getLanguage(), operationOutcome,scenarioName);
		//extension
		ComparatorUtils.compareListOfExtension(ResourceNames.EXTENSION,sourceCareTeam.getExtension(), targetCareTeam.getExtension(), operationOutcome,scenarioName);
		//modifierExtension
		ComparatorUtils.compareListOfExtension(ResourceNames.MODIFIER_EXTENSION, sourceCareTeam.getModifierExtension(), targetCareTeam.getModifierExtension(), operationOutcome,scenarioName);
		//identifier
		ComparatorUtils.compareListOfIdentifier(ResourceNames.IDENTIFIER,sourceCareTeam.getIdentifier(),targetCareTeam.getIdentifier(),operationOutcome,scenarioName);
		//category
		ComparatorUtils.compareListOfCodeableConcept(ResourceNames.CATEGORY, sourceCareTeam.getCategory(), targetCareTeam.getCategory(), operationOutcome,scenarioName);
		//text
		ComparatorUtils.compareNarrative(ResourceNames.TEXT, sourceCareTeam.getText(), targetCareTeam.getText(), operationOutcome,scenarioName);
		//name
		ComparatorUtils.compareString(ResourceNames.NAME,sourceCareTeam.getName(), targetCareTeam.getName(),operationOutcome,scenarioName);
		//status
		String source = null;
		String target=null;
		if(sourceCareTeam.getStatus()!=null) {
			source = sourceCareTeam.getStatus().toString();	
		}
		if(targetCareTeam.getStatus()!=null) {
			target = targetCareTeam.getStatus().toString();	
		}
		ComparatorUtils.compareString(ResourceNames.STATUS, source,target, operationOutcome,scenarioName);
		//subject
		ComparatorUtils.compareReference(ResourceNames.SUBJECT, sourceCareTeam.getSubject(),targetCareTeam.getSubject(), operationOutcome,scenarioName);
		//encounter
		ComparatorUtils.compareReference(ResourceNames.ENCOUNTER, sourceCareTeam.getEncounter(),targetCareTeam.getEncounter(), operationOutcome,scenarioName);
		//period
		ComparatorUtils.comparePeriod(ResourceNames.PERIOD, sourceCareTeam.getPeriod(), targetCareTeam.getPeriod(),operationOutcome,scenarioName);
		//reasonCode
		ComparatorUtils.compareListOfCodeableConcept(ResourceNames.REASON_CODE, sourceCareTeam.getReasonCode(),targetCareTeam.getReasonCode(), operationOutcome,scenarioName);
		//reasonReference
		ComparatorUtils.compareListOfReference(ResourceNames.REASON_REFERENCE, sourceCareTeam.getReasonReference(),targetCareTeam.getReasonReference(), operationOutcome,scenarioName);
		//managingOrganization
		ComparatorUtils.compareListOfReference(ResourceNames.MANAGIGN_ORGANIZATION,sourceCareTeam.getManagingOrganization(), targetCareTeam.getManagingOrganization(),operationOutcome,scenarioName);
		//telecom
		ComparatorUtils.compareListOfContactPoint(ResourceNames.TELECOM,sourceCareTeam.getTelecom(), targetCareTeam.getTelecom(),operationOutcome,scenarioName);
		//note
		ComparatorUtils.compareListOfAnnotation(ResourceNames.NOTE, sourceCareTeam.getNote(), targetCareTeam.getNote(), operationOutcome,scenarioName);
		//participant
		ComparatorUtils.compareListOfCareTeamParticipantComponent(ResourceNames.PARTICIPANT,sourceCareTeam.getParticipant(), targetCareTeam.getParticipant(), operationOutcome,scenarioName);
			
	}

}
