package com.interopx.fhir.validator.resource.content.comparer;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.ObjectUtils;
import org.hl7.fhir.r4.model.DateTimeType;
import org.hl7.fhir.r4.model.Immunization;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Resource;
import org.hl7.fhir.r4.model.StringType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.interopx.fhir.validator.util.ComparatorUtils;
import com.interopx.fhir.validator.util.ScenarioConstants.ResourceNames;
import com.interopx.fhir.validator.util.ScenarioConstants.ScenarioNameConstants;

public class ImmunizationContentComparator {
	
	public static final Logger logger=LoggerFactory.getLogger(ImmunizationContentComparator.class);
	
	protected static final List<String> immunizationStatusArray= Arrays.asList("completed","entered-in-error","not-done");
	
	public static void compare(Resource sourceResource, Resource targetResource, String scenarioName,
			OperationOutcome operationOutcome) {
		logger.info("Entry - ImmunizationContentComparator.compare");
		logger.info("Entry - ImmunizationContentComparator.compare - sourceResource ::\n" + sourceResource);
		logger.info("Entry - ImmunizationContentComparator.compare - targetResource ::\n" + targetResource);
		if (ObjectUtils.isNotEmpty(sourceResource) && ObjectUtils.isNotEmpty(targetResource)) {
			Immunization sourceImmunization = (Immunization) sourceResource;
			Immunization targetImmunization = (Immunization) targetResource;
			logger.info("sourceEncounter :::::\n" + sourceImmunization);
			if (scenarioName.equalsIgnoreCase(ScenarioNameConstants.USCDI_FULL_IMMUNIZATION)) {
				logger.info("sourceEncounter ::::: compareFullContent\n");
				compareFullContent(sourceImmunization, targetImmunization, operationOutcome,scenarioName);
				logger.info("sourceEncounter ::::: compareFullContent\n");
			} else if (scenarioName.equalsIgnoreCase(ScenarioNameConstants.USCDI_ADDITIONAL_IMMUNIZATION)) {
				compareAdditionalContent(sourceImmunization, targetImmunization, operationOutcome,scenarioName);
			}
		}
		logger.info("Exit - ImmunizationContentComparator.compare");
	}

	private static void compareAdditionalContent(Immunization sourceImmunization, Immunization targetImmunization,
			OperationOutcome operationOutcome,String scenarioName) {
		logger.info("Entry - compareAdditionalContent.compare");
		
//		logger.info("Status comparision started "+sourceImmunization.getStatus().toString());
//		if (immunizationStatusArray.contains(sourceImmunization.getStatus().toString())
//				&& immunizationStatusArray.contains(targetImmunization.getStatus().toString())) {
//			logger.info("==========" + sourceImmunization.getStatus().toString());
//			logger.info("==========+++++++++++" + targetImmunization.getStatus().toString());
//			ComparatorUtils.compareString(ResourceNames.STATUS, sourceImmunization.getStatus().toString(),
//					targetImmunization.getStatus().toString(), operationOutcome);
//		} else {
		 //status
				String source = null;
				String target=null;
				if(sourceImmunization.getStatus()!=null) {
					source = sourceImmunization.getStatus().getDisplay();
				}
				if(targetImmunization.getStatus()!=null) {
					target = targetImmunization.getStatus().getDisplay();	
				}
				ComparatorUtils.compareString(ResourceNames.STATUS,source,target,operationOutcome,scenarioName);
//		}
				
		//statusReason
		ComparatorUtils.compareCodeableConcept(ResourceNames.STATUS_REASON,sourceImmunization.getStatusReason(),targetImmunization.getStatusReason(),operationOutcome,scenarioName);
				
		//vaccineCode
		ComparatorUtils.compareCodeableConcept(ResourceNames.VACCINE_CODE,sourceImmunization.getVaccineCode(),targetImmunization.getVaccineCode(),operationOutcome,scenarioName);
				
		//patient
		ComparatorUtils.compareReference(ResourceNames.PATIENT,sourceImmunization.getPatient(),targetImmunization.getPatient(),operationOutcome,scenarioName);
		
		//occurrenceDateTime
		if (sourceImmunization.getOccurrence() instanceof DateTimeType || targetImmunization.getOccurrence() instanceof DateTimeType ) {
			ComparatorUtils.compareDateTimeType(ResourceNames.OCCURRENCE_DATA_TIME,sourceImmunization.getOccurrenceDateTimeType(),targetImmunization.getOccurrenceDateTimeType(),operationOutcome,scenarioName);
		}
		//occurrenceString
		if (sourceImmunization.getOccurrence() instanceof StringType || targetImmunization.getOccurrence() instanceof StringType )
		   ComparatorUtils.compareString(ResourceNames.OCCURRENCE_STRING,sourceImmunization.getOccurrenceStringType().getValue(),targetImmunization.getOccurrenceStringType().getValue(),operationOutcome,scenarioName);
		
		//primarySource
		ComparatorUtils.compareBoolean(ResourceNames.OCCURRENCE_STRING,sourceImmunization.getPrimarySource(),targetImmunization.getPrimarySource(),operationOutcome,scenarioName);
		
				
		logger.info("Exit - compareAdditionalContent.compare");
	}

	private static void compareFullContent(Immunization sourceImmunization, Immunization targetImmunization,
			OperationOutcome operationOutcome,String scenarioName) {
		
		logger.info("Entry - ImmunizationContentComparator.compareFullContent");
		
		//identifier
		ComparatorUtils.compareListOfIdentifier(ResourceNames.IDENTIFIER,sourceImmunization.getIdentifier(),targetImmunization.getIdentifier(),operationOutcome,scenarioName);
		
		//Id
        ComparatorUtils.compareString(ResourceNames.ID, sourceImmunization.getId(), targetImmunization.getId(), operationOutcome,scenarioName);	
        
		//status
		String source = null;
		String target=null;
		if(sourceImmunization.getStatus()!=null) {
			source = sourceImmunization.getStatus().getDisplay();	
		}
		if(targetImmunization.getStatus()!=null) {
			target = targetImmunization.getStatus().getDisplay();	
		}
		ComparatorUtils.compareString(ResourceNames.STATUS,source,target,operationOutcome,scenarioName);
		
		//statusReason
		ComparatorUtils.compareCodeableConcept(ResourceNames.STATUS_REASON,sourceImmunization.getStatusReason(),targetImmunization.getStatusReason(),operationOutcome,scenarioName);
		
		//vaccineCode
		ComparatorUtils.compareCodeableConcept(ResourceNames.VACCINE_CODE,sourceImmunization.getVaccineCode(),targetImmunization.getVaccineCode(),operationOutcome,scenarioName);
		
		//patient
		ComparatorUtils.compareReference(ResourceNames.PATIENT,sourceImmunization.getPatient(),targetImmunization.getPatient(),operationOutcome,scenarioName);
		
		//encounter
		ComparatorUtils.compareReference(ResourceNames.ENCOUNTER,sourceImmunization.getEncounter(),targetImmunization.getEncounter(),operationOutcome,scenarioName);
		
		//recorded
		ComparatorUtils.compareDate(ResourceNames.RECORDED,sourceImmunization.getRecorded(),targetImmunization.getRecorded(),operationOutcome,scenarioName);
		
		//primarySource
		ComparatorUtils.compareBoolean(ResourceNames.PRIMARY_SOURCE,sourceImmunization.getPrimarySource(),targetImmunization.getPrimarySource(),operationOutcome,scenarioName);
		
		//reportOrigin
		ComparatorUtils.compareCodeableConcept(ResourceNames.REPORT_ORIGIN,sourceImmunization.getReportOrigin(),targetImmunization.getReportOrigin(),operationOutcome,scenarioName);
		
		//reportOrigin
		ComparatorUtils.compareReference(ResourceNames.LOCATION,sourceImmunization.getLocation(),targetImmunization.getLocation(),operationOutcome,scenarioName);
		
		//reportOrigin
		ComparatorUtils.compareReference(ResourceNames.MANUFACTURER,sourceImmunization.getManufacturer(),targetImmunization.getManufacturer(),operationOutcome,scenarioName);
		
		//lotNumber
		ComparatorUtils.compareString(ResourceNames.LOT_NUMBER,sourceImmunization.getLotNumber(),targetImmunization.getLotNumber(),operationOutcome,scenarioName);
		
		//expirationDate
		ComparatorUtils.compareDate(ResourceNames.EXPIRATION_DATE,sourceImmunization.getExpirationDate(),targetImmunization.getExpirationDate(),operationOutcome,scenarioName);
		
		//site
		ComparatorUtils.compareCodeableConcept(ResourceNames.SITE,sourceImmunization.getSite(),targetImmunization.getSite(),operationOutcome,scenarioName);
		
		//route
		ComparatorUtils.compareCodeableConcept(ResourceNames.ROUTE,sourceImmunization.getRoute(),targetImmunization.getRoute(),operationOutcome,scenarioName);
		
		//doseQuantity
		ComparatorUtils.compareQuantity(ResourceNames.DOSE_QUANTITY,sourceImmunization.getDoseQuantity(),targetImmunization.getDoseQuantity(),operationOutcome,scenarioName);
		
		//performer
		ComparatorUtils.compareListOfImmunizationPerformerComponent(ResourceNames.PERFORMER,sourceImmunization.getPerformer(),targetImmunization.getPerformer(),operationOutcome,scenarioName);
		
		//note
		ComparatorUtils.compareListOfAnnotation(ResourceNames.NOTE,sourceImmunization.getNote(),targetImmunization.getNote(),operationOutcome,scenarioName);
		
		//reasonCode
		ComparatorUtils.compareListOfCodeableConcept(ResourceNames.REASON_CODE,sourceImmunization.getReasonCode(),targetImmunization.getReasonCode(),operationOutcome,scenarioName);
		
		//reasonReference
		ComparatorUtils.compareListOfReference(ResourceNames.REASON_REFERENCE,sourceImmunization.getReasonReference(),targetImmunization.getReasonReference(),operationOutcome,scenarioName);
		
		//isSubpotent
		ComparatorUtils.compareBoolean(ResourceNames.IS_SUBPOTENT,sourceImmunization.getIsSubpotent(),targetImmunization.getIsSubpotent(),operationOutcome,scenarioName);
		
		//subpotentReason
		ComparatorUtils.compareListOfCodeableConcept(ResourceNames.SUBPOTENT_REASON,sourceImmunization.getSubpotentReason(),targetImmunization.getSubpotentReason(),operationOutcome,scenarioName);
		
		//education
		ComparatorUtils.compareListOfImmunizationEducationComponent(ResourceNames.EDUCATION,sourceImmunization.getEducation(),targetImmunization.getEducation(),operationOutcome,scenarioName);
		
		//programEligibility
		ComparatorUtils.compareListOfCodeableConcept(ResourceNames.PROGRAM_ELIGIBILITY,sourceImmunization.getProgramEligibility(),targetImmunization.getProgramEligibility(),operationOutcome,scenarioName);
		
		//fundingSource
		ComparatorUtils.compareCodeableConcept(ResourceNames.FUNCTION_SOURCE,sourceImmunization.getFundingSource(),targetImmunization.getFundingSource(),operationOutcome,scenarioName);
		//occurrenceDateTime
				if (sourceImmunization.getOccurrence() instanceof DateTimeType || targetImmunization.getOccurrence() instanceof DateTimeType ) {
					ComparatorUtils.compareDateTimeType(ResourceNames.OCCURRENCE_DATA_TIME,sourceImmunization.getOccurrenceDateTimeType(),targetImmunization.getOccurrenceDateTimeType(),operationOutcome,scenarioName);
				}
		//occurrenceString
				if (sourceImmunization.getOccurrence() instanceof StringType || targetImmunization.getOccurrence() instanceof StringType )
				   ComparatorUtils.compareString(ResourceNames.OCCURRENCE_STRING,sourceImmunization.getOccurrenceStringType().getValue(),targetImmunization.getOccurrenceStringType().getValue(),operationOutcome,scenarioName);
				
		//reaction
		ComparatorUtils.compareListOfImmunizationReactionComponent(ResourceNames.REACTION,sourceImmunization.getReaction(),targetImmunization.getReaction(),operationOutcome,scenarioName);
		
		//protocolApplied
		ComparatorUtils.compareListOfImmunizationProtocolAppliedComponent(ResourceNames.PROTOCOL_APPLIED,sourceImmunization.getProtocolApplied(),targetImmunization.getProtocolApplied(),operationOutcome,scenarioName);
		
		logger.info("Exit - ImmunizationContentComparator.compareFullContent");
	}

	

}
