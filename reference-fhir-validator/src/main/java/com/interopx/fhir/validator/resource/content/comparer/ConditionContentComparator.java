package com.interopx.fhir.validator.resource.content.comparer;

import org.apache.commons.lang3.ObjectUtils;

import org.hl7.fhir.r4.model.Age;
import org.hl7.fhir.r4.model.Condition;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Period;
import org.hl7.fhir.r4.model.Range;
import org.hl7.fhir.r4.model.Resource;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.DateTimeType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.interopx.fhir.validator.util.ComparatorUtils;
import com.interopx.fhir.validator.util.ScenarioConstants.ResourceNames;
import com.interopx.fhir.validator.util.ScenarioConstants.ScenarioNameConstants;

/**
 * This Class compares source/input Condition resource content with goldJson
 * 
 * @author Swarna Nelaturi
 *
 */
public class ConditionContentComparator {
	public static final Logger logger = LoggerFactory.getLogger(ConditionContentComparator.class);

	public static void compare(Resource sourceResource, Resource targetResource, String scenarioName,
			OperationOutcome operationOutcome) {
		logger.info("Entry - ConditionContentComparator.compare");
		logger.info("Entry - ConditionContentComparator.compare - sourceResource ::\n" + sourceResource);
		logger.info("Entry - ConditionContentComparator.compare - targetResource ::\n" + targetResource);
		if (ObjectUtils.isNotEmpty(sourceResource) && ObjectUtils.isNotEmpty(targetResource)) {
			Condition sourceCondition = (Condition) sourceResource;
			Condition targetCondition = (Condition) targetResource;
			logger.info("sourcePatient :::::\n" + sourceCondition);
			if (scenarioName.equalsIgnoreCase(ScenarioNameConstants.USCDI_FULL_CONDITION)) {
				logger.info("sourcePatient ::::: compareFullContent\n");
				compareFullContent(sourceCondition, targetCondition, operationOutcome,scenarioName);
				logger.info("sourcePatient ::::: compareFullContent\n");
			} else if (scenarioName.equalsIgnoreCase(ScenarioNameConstants.USCDI_ADDITIONAL_CONDITION)) {
				compareAdditionalContent(sourceCondition, targetCondition, operationOutcome,scenarioName);
			}
		}
		logger.info("Exit - ConditionContentComparator.compare");
	}

	private static void compareFullContent(Condition sourceCondition, Condition targetCondition,
			OperationOutcome operationOutcome,String scenarioName) {

		logger.info("Entry - ConditionContentComparator.compareFullContent");

		//id
		ComparatorUtils.compareString(ResourceNames.ID,sourceCondition.getId(),targetCondition.getId(),operationOutcome,scenarioName);
		
		// identifier
		ComparatorUtils.compareListOfIdentifier(ResourceNames.IDENTIFIER, sourceCondition.getIdentifier(),
				targetCondition.getIdentifier(), operationOutcome, scenarioName);
		// clinicalStatus
		ComparatorUtils.compareCodeableConcept(ResourceNames.CLINICAL_STATUS, sourceCondition.getClinicalStatus(),
				targetCondition.getClinicalStatus(), operationOutcome,scenarioName);
		// verificationStatus
		ComparatorUtils.compareCodeableConcept(ResourceNames.VERIFICATION_STATUS,
				sourceCondition.getVerificationStatus(), targetCondition.getVerificationStatus(), operationOutcome,scenarioName);
		// category
		ComparatorUtils.compareListOfCodeableConcept(ResourceNames.CATEGORY, sourceCondition.getCategory(),
				targetCondition.getCategory(), operationOutcome,scenarioName);
		// severity
		ComparatorUtils.compareCodeableConcept(ResourceNames.SEVERITY, sourceCondition.getSeverity(),
				targetCondition.getSeverity(), operationOutcome,scenarioName);
		// code
		ComparatorUtils.compareCodeableConcept(ResourceNames.CODE, sourceCondition.getCode(), targetCondition.getCode(),
				operationOutcome,scenarioName);
		// bodySite
		ComparatorUtils.compareListOfCodeableConcept(ResourceNames.BODY_SITE, sourceCondition.getBodySite(),
				targetCondition.getBodySite(), operationOutcome,scenarioName);
		// encounter
		ComparatorUtils.compareReference(ResourceNames.ENCOUNTER, sourceCondition.getEncounter(),
				targetCondition.getEncounter(), operationOutcome,scenarioName);
		// subject
		ComparatorUtils.compareReference(ResourceNames.SUBJECT, sourceCondition.getSubject(),
				targetCondition.getSubject(), operationOutcome,scenarioName);
		// recorder
		ComparatorUtils.compareReference(ResourceNames.RECORDER, sourceCondition.getRecorder(),
				targetCondition.getRecorder(), operationOutcome,scenarioName);
		// asserter
		ComparatorUtils.compareReference(ResourceNames.ASSERTER, sourceCondition.getAsserter(),
				targetCondition.getAsserter(), operationOutcome,scenarioName);

		// onset
		if (sourceCondition.getOnset() instanceof DateTimeType || targetCondition.getOnset() instanceof DateTimeType) {
			// onsetDateTime
			ComparatorUtils.compareDateTimeType(ResourceNames.ONSET_DATE_TIME, sourceCondition.getOnsetDateTimeType(),
					targetCondition.getOnsetDateTimeType(), operationOutcome,scenarioName);
		}

		else if (sourceCondition.getOnset() instanceof Age || targetCondition.getOnset() instanceof Age) {
			// onsetAge
			ComparatorUtils.compareAge(ResourceNames.ONSET_AGE, sourceCondition.getOnsetAge(),
					targetCondition.getOnsetAge(), operationOutcome,scenarioName);
		} else if (sourceCondition.getOnset() instanceof Period || targetCondition.getOnset() instanceof Period) {
			// onsetPeriod
			ComparatorUtils.comparePeriod(ResourceNames.ONSET_PERIOD, sourceCondition.getOnsetPeriod(),
					targetCondition.getOnsetPeriod(), operationOutcome,scenarioName);
		} else if (sourceCondition.getOnset() instanceof Range || targetCondition.getOnset() instanceof Range) {
			// onsetRange
			ComparatorUtils.compareRange(ResourceNames.ONSET_RANGE, sourceCondition.getOnsetRange(),
					targetCondition.getOnsetRange(), operationOutcome,scenarioName);
		} else if (sourceCondition.getOnset() instanceof StringType
			|| targetCondition.getOnset() instanceof StringType) {

			// onsetString
			ComparatorUtils.compareString(ResourceNames.ONSET_STRING, sourceCondition.getOnsetStringType().getValue(),
					targetCondition.getOnsetStringType().getValue(), operationOutcome,scenarioName);
		}
		// abatement
		if (sourceCondition.getAbatement() instanceof DateTimeType
				|| targetCondition.getAbatement() instanceof DateTimeType) {

			// abatementDateTime
			ComparatorUtils.compareDateTimeType(ResourceNames.ABATEMENT_DATE,
					sourceCondition.getAbatementDateTimeType(), targetCondition.getAbatementDateTimeType(),
					operationOutcome,scenarioName);
		} else if (sourceCondition.getAbatement() instanceof Age || targetCondition.getAbatement() instanceof Age) {

			// abatementAge
			ComparatorUtils.compareAge(ResourceNames.ABATEMENT_AGE, sourceCondition.getAbatementAge(),
					targetCondition.getAbatementAge(), operationOutcome,scenarioName);
		} else if (sourceCondition.getAbatement() instanceof Period
				|| targetCondition.getAbatement() instanceof Period) {

			// abatementPeriod
			ComparatorUtils.comparePeriod(ResourceNames.ABATEMENT_PERIOD, sourceCondition.getAbatementPeriod(),
					targetCondition.getAbatementPeriod(), operationOutcome,scenarioName);
		} else if (sourceCondition.getAbatement() instanceof Range || targetCondition.getAbatement() instanceof Range) {

			// abatementRange
			ComparatorUtils.compareRange(ResourceNames.ABATEMENT_RANGE, sourceCondition.getAbatementRange(),
					targetCondition.getAbatementRange(), operationOutcome,scenarioName);
		} else if (sourceCondition.getAbatement() instanceof StringType
				|| targetCondition.getAbatement() instanceof StringType) {
			// abatementString
			ComparatorUtils.compareString(ResourceNames.ABATEMENT_STRING,
					sourceCondition.getAbatementStringType().getValueAsString(),
					targetCondition.getAbatementStringType().getValueAsString(), operationOutcome,scenarioName);
		}
		// recordedDate
		ComparatorUtils.compareDate(ResourceNames.RECORDED_DATE, sourceCondition.getRecordedDate(),
				targetCondition.getRecordedDate(), operationOutcome,scenarioName);

		// stage
		ComparatorUtils.compareListOfConditionStageComponent(ResourceNames.STAGE, sourceCondition.getStage(),
				targetCondition.getStage(), operationOutcome,scenarioName);

		// evidence
		ComparatorUtils.compareListOfConditionEvidenceComponent(ResourceNames.EVIDENCE, sourceCondition.getEvidence(),
				targetCondition.getEvidence(), operationOutcome,scenarioName);

		// note
		ComparatorUtils.compareListOfAnnotation(ResourceNames.NOTE, sourceCondition.getNote(),
				targetCondition.getNote(), operationOutcome,scenarioName);

		logger.info("Exit - ConditionContentComparator.compareFullContent");
	}

	private static void compareAdditionalContent(Condition sourceCondition, Condition targetCondition,
			OperationOutcome operationOutcome,String scenarioName) {

		logger.info("Entry - ConditionContentComparator.compareAdditionalContent");

		// clinicalStatus
		ComparatorUtils.compareCodeableConcept(ResourceNames.CLINICAL_STATUS, sourceCondition.getClinicalStatus(),
				targetCondition.getClinicalStatus(), operationOutcome,scenarioName);
		// verificationStatus
		ComparatorUtils.compareCodeableConcept(ResourceNames.VERIFICATION_STATUS,
				sourceCondition.getVerificationStatus(), targetCondition.getVerificationStatus(), operationOutcome,scenarioName);
		// category
		ComparatorUtils.compareListOfCodeableConcept(ResourceNames.CATEGORY, sourceCondition.getCategory(),
				targetCondition.getCategory(), operationOutcome,scenarioName);
		// code
		ComparatorUtils.compareCodeableConcept(ResourceNames.CODE, sourceCondition.getCode(), targetCondition.getCode(),
				operationOutcome,scenarioName);
		// subject
		ComparatorUtils.compareReference(ResourceNames.SUBJECT, sourceCondition.getSubject(),
				targetCondition.getSubject(), operationOutcome,scenarioName);

		logger.info("Exit - ConditionContentComparator.compareAdditionalContent");
	}

}
