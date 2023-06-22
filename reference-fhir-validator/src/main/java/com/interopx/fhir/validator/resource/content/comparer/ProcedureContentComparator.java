package com.interopx.fhir.validator.resource.content.comparer;

import org.apache.commons.lang3.ObjectUtils;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Procedure;
import org.hl7.fhir.r4.model.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.interopx.fhir.validator.util.ComparatorUtils;
import com.interopx.fhir.validator.util.ScenarioConstants.ResourceNames;
import com.interopx.fhir.validator.util.ScenarioConstants.ScenarioNameConstants;

public class ProcedureContentComparator {
	public static final Logger logger = LoggerFactory.getLogger(ProcedureContentComparator.class);

	public static void compare(Resource sourceResource, Resource targetResource, String scenarioName,
			OperationOutcome operationOutcome) {

		logger.info("Entry - ProcedureContentComparator.compare");
		logger.info("Entry - ProcedureContentComparator.compare - sourceResource ::\n" + sourceResource);
		logger.info("Entry - ProcedureContentComparator.compare - targetResource ::\n" + targetResource);

		if (ObjectUtils.isNotEmpty(sourceResource) && ObjectUtils.isNotEmpty(targetResource)) {
			Procedure sourceProcedure = (Procedure) sourceResource;
			Procedure targetProcedure = (Procedure) targetResource;
			logger.info("sourceProcedure :::::\n" + sourceProcedure);
			if (scenarioName.equalsIgnoreCase(ScenarioNameConstants.USCDI_FULL_PROCEDURE)) {
				logger.info("sourceProcedure ::::: compareFullContent\n");
				compareFullContent(sourceProcedure, targetProcedure, operationOutcome,scenarioName);
				logger.info("sourceProcedure ::::: compareFullContent\n");
			} else if (scenarioName.equalsIgnoreCase(ScenarioNameConstants.USCDI_ADDITIONAL_PROCEDURE)) {
				compareAdditionalContent(sourceProcedure, targetProcedure, operationOutcome,scenarioName);
			}
		}
		logger.info("Exit - ProcedureContentComparator.compare");
	}

	private static void compareAdditionalContent(Procedure sourceProcedure, Procedure targetProcedure,
			OperationOutcome operationOutcome,String scenarioName) {
		// status
		String source = null;
		String target=null;
		if(sourceProcedure.getStatus()!=null) {
			source = sourceProcedure.getStatus().toString();	
		}
		if(targetProcedure.getStatus()!=null) {
			target = targetProcedure.getStatus().toString();	
		}
		ComparatorUtils.compareString(ResourceNames.STATUS, source,target, operationOutcome,scenarioName);
		// code
		ComparatorUtils.compareCodeableConcept(ResourceNames.CODE, sourceProcedure.getCode(), targetProcedure.getCode(),
				operationOutcome,scenarioName);
		// subject
		ComparatorUtils.compareReference(ResourceNames.SUBJECT, sourceProcedure.getSubject(),
				targetProcedure.getSubject(), operationOutcome,scenarioName);
		// performed
		if (sourceProcedure.getPerformed() instanceof org.hl7.fhir.r4.model.DateTimeType || targetProcedure.getPerformed() instanceof org.hl7.fhir.r4.model.DateTimeType ) {
			ComparatorUtils.comparePerformedDate(ResourceNames.PERFORMED_DATE,
					sourceProcedure.getPerformedDateTimeType(), targetProcedure.getPerformedDateTimeType(),
					operationOutcome,scenarioName);
		}
		// performedPeriod
		if (sourceProcedure.getPerformed() instanceof org.hl7.fhir.r4.model.Period || targetProcedure.getPerformed() instanceof org.hl7.fhir.r4.model.Period ) {
			ComparatorUtils.comparePeriod(ResourceNames.PERFORMED_PERIOD, sourceProcedure.getPerformedPeriod(),
					targetProcedure.getPerformedPeriod(), operationOutcome,scenarioName);
		}

	}

	private static void compareFullContent(Procedure sourceProcedure, Procedure targetProcedure,
			OperationOutcome operationOutcome,String scenarioName) {

		logger.info("Entry - ProcedureContentComparator.compareFullContent");

		// id
		ComparatorUtils.compareString(ResourceNames.ID, sourceProcedure.getId(),
				targetProcedure.getId(), operationOutcome,scenarioName);
		// implicitRules
		ComparatorUtils.compareString(ResourceNames.IMPLICIT_RULES, sourceProcedure.getImplicitRules(),
				targetProcedure.getImplicitRules(), operationOutcome,scenarioName);
		// language
		ComparatorUtils.compareString(ResourceNames.LANGUAGE, sourceProcedure.getLanguage(),
				targetProcedure.getLanguage(), operationOutcome,scenarioName);
		// contained
		ComparatorUtils.compareListOfResource(ResourceNames.CONTAINED, sourceProcedure.getContained(),
				targetProcedure.getContained(), operationOutcome,scenarioName);
		// Identifier
		ComparatorUtils.compareListOfIdentifier(ResourceNames.IDENTIFIER, sourceProcedure.getIdentifier(),
				targetProcedure.getIdentifier(), operationOutcome,scenarioName);
		// Extension
		ComparatorUtils.compareListOfExtension(ResourceNames.EXTENSION, sourceProcedure.getExtension(),
				targetProcedure.getExtension(), operationOutcome,scenarioName);
		// ModifierExtension
		ComparatorUtils.compareListOfExtension(ResourceNames.MODIFIER_EXTENSION, sourceProcedure.getModifierExtension(),
				targetProcedure.getModifierExtension(), operationOutcome,scenarioName);
		// basedOn
		ComparatorUtils.compareListOfReference(ResourceNames.BASED_ON, sourceProcedure.getBasedOn(),
				targetProcedure.getBasedOn(), operationOutcome,scenarioName);
		// partOf
		ComparatorUtils.compareListOfReference(ResourceNames.PART_OF, sourceProcedure.getPartOf(),
				targetProcedure.getPartOf(), operationOutcome,scenarioName);
		// status
		String source = null;
		String target=null;
		if(sourceProcedure.getStatus()!=null) {
			source = sourceProcedure.getStatus().getDisplay();	
		}
		if(targetProcedure.getStatus()!=null) {
			target = targetProcedure.getStatus().getDisplay();	
		}
		ComparatorUtils.compareString(ResourceNames.STATUS, source,target, operationOutcome,scenarioName);
		// category
		ComparatorUtils.compareCodeableConcept(ResourceNames.CATEGORY, sourceProcedure.getCategory(),
				targetProcedure.getCategory(), operationOutcome,scenarioName);
		// code
		ComparatorUtils.compareCodeableConcept(ResourceNames.CODE, sourceProcedure.getCode(), targetProcedure.getCode(),
				operationOutcome,scenarioName);
		// subject
		ComparatorUtils.compareReference(ResourceNames.SUBJECT, sourceProcedure.getSubject(),
				targetProcedure.getSubject(), operationOutcome,scenarioName);
		// encounter
		ComparatorUtils.compareReference(ResourceNames.ENCOUNTER, sourceProcedure.getEncounter(),
				targetProcedure.getEncounter(), operationOutcome,scenarioName);
		// recorder
		ComparatorUtils.compareReference(ResourceNames.RECORDER, sourceProcedure.getRecorder(),
				targetProcedure.getRecorder(), operationOutcome,scenarioName);
		// asserter
		ComparatorUtils.compareReference(ResourceNames.ASSERTER, sourceProcedure.getAsserter(),
				targetProcedure.getAsserter(), operationOutcome,scenarioName);
		// location
		ComparatorUtils.compareReference(ResourceNames.LOCATION, sourceProcedure.getLocation(),
				targetProcedure.getLocation(), operationOutcome,scenarioName);
		// reasonCode
		ComparatorUtils.compareListOfCodeableConcept(ResourceNames.REASON_CODE, sourceProcedure.getReasonCode(),
				targetProcedure.getReasonCode(), operationOutcome,scenarioName);
		// reasonReference
		ComparatorUtils.compareListOfReference(ResourceNames.REASON_REFERENCE, sourceProcedure.getReasonReference(),
				targetProcedure.getReasonReference(), operationOutcome,scenarioName);
		// bodySite
		ComparatorUtils.compareListOfCodeableConcept(ResourceNames.BODY_SITE, sourceProcedure.getBodySite(),
				targetProcedure.getBodySite(), operationOutcome,scenarioName);
		// outcome
		ComparatorUtils.compareCodeableConcept(ResourceNames.OUTCOME, sourceProcedure.getOutcome(),
				targetProcedure.getOutcome(), operationOutcome,scenarioName);
		// complicationDetail
		ComparatorUtils.compareListOfReference(ResourceNames.COMPLICATION_DETAIL,
				sourceProcedure.getComplicationDetail(), targetProcedure.getComplicationDetail(), operationOutcome,scenarioName);
		// followUp
		ComparatorUtils.compareListOfCodeableConcept(ResourceNames.FOLLOW_UP, sourceProcedure.getFollowUp(),
				targetProcedure.getFollowUp(), operationOutcome,scenarioName);
		//statusReason
		ComparatorUtils.compareCodeableConcept(ResourceNames.STATUS_REASON, sourceProcedure.getStatusReason(),
				targetProcedure.getStatusReason(), operationOutcome,scenarioName);
		//usedReference
		ComparatorUtils.compareListOfReference(ResourceNames.USED_REFERENCE, sourceProcedure.getUsedReference(),
				targetProcedure.getUsedReference(), operationOutcome,scenarioName);
		// note
		ComparatorUtils.compareListOfAnnotation(ResourceNames.NOTE, sourceProcedure.getNote(),
				targetProcedure.getNote(), operationOutcome,scenarioName);
		// performer
		ComparatorUtils.compareListOfProcedurePerformerComponent(ResourceNames.PERFORMER,
				sourceProcedure.getPerformer(), targetProcedure.getPerformer(), operationOutcome,scenarioName);
		// performed
		if (sourceProcedure.getPerformed() instanceof org.hl7.fhir.r4.model.DateTimeType || targetProcedure.getPerformed() instanceof org.hl7.fhir.r4.model.DateTimeType ) {
			ComparatorUtils.comparePerformedDate(ResourceNames.PERFORMED_DATE,
					sourceProcedure.getPerformedDateTimeType(), targetProcedure.getPerformedDateTimeType(),
					operationOutcome,scenarioName);
		}
		// performedPeriod
		if (sourceProcedure.getPerformed() instanceof org.hl7.fhir.r4.model.Period || targetProcedure.getPerformed() instanceof org.hl7.fhir.r4.model.Period ) {
			ComparatorUtils.comparePeriod(ResourceNames.PERFORMED_PERIOD, sourceProcedure.getPerformedPeriod(),
					targetProcedure.getPerformedPeriod(), operationOutcome,scenarioName);
		}
		
		//focalDevice
		ComparatorUtils.compareListOfProcedureFocalDeviceComponent(ResourceNames.FOCAL_DEVICE_COMPONENT, sourceProcedure.getFocalDevice(),
				targetProcedure.getFocalDevice(), operationOutcome,scenarioName);
		
		//usedCode
		ComparatorUtils.compareListOfCodeableConcept(ResourceNames.USED_CODE, sourceProcedure.getUsedCode(),
				targetProcedure.getUsedCode(), operationOutcome,scenarioName);
		//complication
		ComparatorUtils.compareListOfCodeableConcept(ResourceNames.COMPLICATION, sourceProcedure.getComplication(),
				targetProcedure.getComplication(), operationOutcome,scenarioName);
		
		logger.info("Exit - ProcedureContentComparator.compareFullContent");

	}
	
}
