package com.interopx.fhir.validator.resource.content.comparer;

import org.apache.commons.lang3.ObjectUtils;
import org.hl7.fhir.r4.model.Age;
import org.hl7.fhir.r4.model.AllergyIntolerance;
import org.hl7.fhir.r4.model.DateTimeType;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Period;
import org.hl7.fhir.r4.model.Range;
import org.hl7.fhir.r4.model.Resource;
import org.hl7.fhir.r4.model.StringType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.interopx.fhir.validator.util.ComparatorUtils;
import com.interopx.fhir.validator.util.ScenarioConstants.ResourceNames;
import com.interopx.fhir.validator.util.ScenarioConstants.ScenarioNameConstants;

/**
 * This Class compares source/input AllergyIntolerance resource content with
 * goldJson
 * 
 * @author Naik Nitesh S
 *
 */
public class AllergyIntoleranceContentComparator {
	public static final Logger logger = LoggerFactory.getLogger(AllergyIntoleranceContentComparator.class);

	public static void compare(Resource sourceResource, Resource targetResource, String scenarioName,
			OperationOutcome operationOutcome) {

		logger.info("Entry - AllergyIntoleranceContentComparator.compare");
		logger.info("Entry - AllergyIntoleranceContentComparator.compare - sourceResource ::\n" + sourceResource);
		logger.info("Entry - AllergyIntoleranceContentComparator.compare - targetResource ::\n" + targetResource);

		if (ObjectUtils.isNotEmpty(sourceResource) && ObjectUtils.isNotEmpty(targetResource)) {
			AllergyIntolerance sourceAllergyIntolerance = (AllergyIntolerance) sourceResource;
			AllergyIntolerance targetAllergyIntolerance = (AllergyIntolerance) targetResource;
			logger.info("sourceAllergyIntolerance :::::\n" + sourceAllergyIntolerance);
			if (scenarioName.equalsIgnoreCase(ScenarioNameConstants.USCDI_FULL_ALLERGY_INTOLERANCE)) {
				logger.info("sourceAllergyIntolerance ::::: compareFullContent\n");
				compareFullContent(sourceAllergyIntolerance, targetAllergyIntolerance, operationOutcome,scenarioName);
				logger.info("sourceAllergyIntolerance ::::: compareFullContent\n");
			} else if (scenarioName.equalsIgnoreCase(ScenarioNameConstants.USCDI_ADDITIONAL_ALLERGY_INTOLERANCE)) {
				compareAdditionalContent(sourceAllergyIntolerance, targetAllergyIntolerance, operationOutcome,scenarioName);
			}
		}
		logger.info("Exit - AllergyIntoleranceContentComparator.compare");
	}

	private static void compareFullContent(AllergyIntolerance sourceAllergyIntolerance,
			AllergyIntolerance targetAllergyIntolerance, OperationOutcome operationOutcome,String scenarioName) {

		logger.info("Entry - AllergyIntoleranceContentComparator.compareFullContent");

		// Identifier
		ComparatorUtils.compareListOfIdentifier(ResourceNames.IDENTIFIER, sourceAllergyIntolerance.getIdentifier(),
				targetAllergyIntolerance.getIdentifier(), operationOutcome,scenarioName);
		// Extension
		ComparatorUtils.compareListOfExtension(ResourceNames.EXTENSION, sourceAllergyIntolerance.getExtension(),
				targetAllergyIntolerance.getExtension(), operationOutcome,scenarioName);
		// VerificationStatus
		ComparatorUtils.compareCodeableConcept(ResourceNames.VERIFICATION_STATUS,
				sourceAllergyIntolerance.getVerificationStatus(), targetAllergyIntolerance.getVerificationStatus(),
				operationOutcome,scenarioName);
		// Language
		ComparatorUtils.compareLanguage(ResourceNames.LANGUAGE, sourceAllergyIntolerance.getLanguage(),
				targetAllergyIntolerance.getLanguage(), operationOutcome,scenarioName);
		// Contained
		ComparatorUtils.compareListOfResource(ResourceNames.CONTAINED, sourceAllergyIntolerance.getContained(),
				targetAllergyIntolerance.getContained(), operationOutcome,scenarioName);
		// ModifierExtension
		ComparatorUtils.compareListOfExtension(ResourceNames.MODIFIER_EXTENSION,
				sourceAllergyIntolerance.getModifierExtension(), targetAllergyIntolerance.getModifierExtension(),
				operationOutcome,scenarioName);
		// ImplicitRules
		ComparatorUtils.compareString(ResourceNames.IMPLICIT_RULES, sourceAllergyIntolerance.getImplicitRules(),
				targetAllergyIntolerance.getImplicitRules(), operationOutcome,scenarioName);
		// Id
		ComparatorUtils.compareString(ResourceNames.ID, sourceAllergyIntolerance.getId(),
				targetAllergyIntolerance.getId(), operationOutcome,scenarioName);
		// ClinicalStatus
		ComparatorUtils.compareCodeableConcept(ResourceNames.CLINICAL_STATUS,
				sourceAllergyIntolerance.getClinicalStatus(), targetAllergyIntolerance.getClinicalStatus(),
				operationOutcome,scenarioName);
		// type
		String source = null;
		String target = null;
		if (sourceAllergyIntolerance.getType() != null) {
			source = sourceAllergyIntolerance.getType().toString();
		}
		if (targetAllergyIntolerance.getType() != null) {
			target = targetAllergyIntolerance.getType().toString();
		}
		ComparatorUtils.compareString(ResourceNames.TYPE, source, target, operationOutcome,scenarioName);
		// category
		ComparatorUtils.compareListOfAllergyIntoleranceCategory(ResourceNames.CATEGORY,
				sourceAllergyIntolerance.getCategory(), targetAllergyIntolerance.getCategory(), operationOutcome,scenarioName);
		// criticality
		String sourcecriticality = null;
		String targetcriticality  = null;
		if (sourceAllergyIntolerance.getCriticality() != null) {
			sourcecriticality = sourceAllergyIntolerance.getCriticality().toString();
		}
		if (targetAllergyIntolerance.getCriticality() != null) {
			targetcriticality = targetAllergyIntolerance.getCriticality().toString();
		}
		ComparatorUtils.compareString(ResourceNames.CRITICALITY, sourcecriticality, targetcriticality, operationOutcome,scenarioName);
		// encounter
		ComparatorUtils.compareReference(ResourceNames.ENCOUNTER, sourceAllergyIntolerance.getEncounter(),
				targetAllergyIntolerance.getEncounter(), operationOutcome,scenarioName);
		// patient
		ComparatorUtils.compareReference(ResourceNames.PATIENT, sourceAllergyIntolerance.getPatient(),
				targetAllergyIntolerance.getPatient(), operationOutcome,scenarioName);
		// recorder
		ComparatorUtils.compareReference(ResourceNames.RECORDER, sourceAllergyIntolerance.getRecorder(),
				targetAllergyIntolerance.getRecorder(), operationOutcome,scenarioName);
		// asserter
		ComparatorUtils.compareReference(ResourceNames.ASSERTER, sourceAllergyIntolerance.getAsserter(),
				targetAllergyIntolerance.getAsserter(), operationOutcome,scenarioName);
		// lastOccurence
		ComparatorUtils.compareDate(ResourceNames.LAST_OCCURENCE, sourceAllergyIntolerance.getLastOccurrence(),
				targetAllergyIntolerance.getLastOccurrence(), operationOutcome,scenarioName);
		// note
		ComparatorUtils.compareListOfAnnotation(ResourceNames.NOTE, sourceAllergyIntolerance.getNote(),
				targetAllergyIntolerance.getNote(), operationOutcome,scenarioName);
		// reaction
		ComparatorUtils.compareListOfAllergyIntoleranceReactionComponent(ResourceNames.REACTION,
				sourceAllergyIntolerance.getReaction(), targetAllergyIntolerance.getReaction(), operationOutcome,scenarioName);
		// code
		ComparatorUtils.compareCodeableConcept(ResourceNames.CODE, sourceAllergyIntolerance.getCode(),
				targetAllergyIntolerance.getCode(), operationOutcome,scenarioName);
		//onset
		if (sourceAllergyIntolerance.getOnset() instanceof DateTimeType
				|| targetAllergyIntolerance.getOnset() instanceof DateTimeType) {
			// onsetDateTime
			ComparatorUtils.compareDateTimeType(ResourceNames.ONSET_DATE_TIME,
					sourceAllergyIntolerance.getOnsetDateTimeType(), targetAllergyIntolerance.getOnsetDateTimeType(),
					operationOutcome,scenarioName);
		}

		else if (sourceAllergyIntolerance.getOnset() instanceof Age
				|| targetAllergyIntolerance.getOnset() instanceof Age) {
			// onsetAge
			ComparatorUtils.compareAge(ResourceNames.ONSET_AGE, sourceAllergyIntolerance.getOnsetAge(),
					targetAllergyIntolerance.getOnsetAge(), operationOutcome,scenarioName);
		} else if (sourceAllergyIntolerance.getOnset() instanceof Period
				|| targetAllergyIntolerance.getOnset() instanceof Period) {
			// onsetPeriod
			ComparatorUtils.comparePeriod(ResourceNames.ONSET_PERIOD, sourceAllergyIntolerance.getOnsetPeriod(),
					targetAllergyIntolerance.getOnsetPeriod(), operationOutcome,scenarioName);
		} else if (sourceAllergyIntolerance.getOnset() instanceof Range
				|| targetAllergyIntolerance.getOnset() instanceof Range) {
			// onsetRange
			ComparatorUtils.compareRange(ResourceNames.ONSET_RANGE, sourceAllergyIntolerance.getOnsetRange(),
					targetAllergyIntolerance.getOnsetRange(), operationOutcome,scenarioName);
		} else if (sourceAllergyIntolerance.getOnset() instanceof StringType
				|| targetAllergyIntolerance.getOnset() instanceof StringType) {

			// onsetString
			ComparatorUtils.compareString(ResourceNames.ONSET_STRING,
					sourceAllergyIntolerance.getOnsetStringType().getValue(),
					targetAllergyIntolerance.getOnsetStringType().getValue(), operationOutcome,scenarioName);
		}
		logger.info("Exit - AllergyIntoleranceContentComparator.compareFullContent");

		// recordedDate
		ComparatorUtils.compareDate(ResourceNames.RECORDED_DATE, sourceAllergyIntolerance.getRecordedDate(),
				targetAllergyIntolerance.getRecordedDate(), operationOutcome,scenarioName);

	}

	private static void compareAdditionalContent(AllergyIntolerance sourceAllergyIntolerance,
			AllergyIntolerance targetAllergyIntolerance, OperationOutcome operationOutcome,String scenarioName) {

		logger.info("Entry - AllergyIntoleranceContentComparator.compareAdditionalContent");

		// VerificationStatus
		ComparatorUtils.compareCodeableConcept(ResourceNames.VERIFICATION_STATUS,
				sourceAllergyIntolerance.getVerificationStatus(), targetAllergyIntolerance.getVerificationStatus(),
				operationOutcome,scenarioName);
		// ClinicalStatus
		ComparatorUtils.compareCodeableConcept(ResourceNames.CLINICAL_STATUS,
				sourceAllergyIntolerance.getClinicalStatus(), targetAllergyIntolerance.getClinicalStatus(),
				operationOutcome,scenarioName);
		// patient
		ComparatorUtils.compareReference(ResourceNames.PATIENT, sourceAllergyIntolerance.getPatient(),
				targetAllergyIntolerance.getPatient(), operationOutcome,scenarioName);
		// reaction
		ComparatorUtils.compareListOfAllergyIntoleranceReactionComponent(ResourceNames.REACTION,
				sourceAllergyIntolerance.getReaction(), targetAllergyIntolerance.getReaction(), operationOutcome,scenarioName);
		// code
		ComparatorUtils.compareCodeableConcept(ResourceNames.CODE, sourceAllergyIntolerance.getCode(),
				targetAllergyIntolerance.getCode(), operationOutcome,scenarioName);

		logger.info("Exit - AllergyIntoleranceContentComparator.compareAdditionalContent");
	}
}
