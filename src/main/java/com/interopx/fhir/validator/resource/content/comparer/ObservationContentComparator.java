package com.interopx.fhir.validator.resource.content.comparer;

import org.apache.commons.lang3.ObjectUtils;

import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.DateTimeType;
import org.hl7.fhir.r4.model.InstantType;
import org.hl7.fhir.r4.model.IntegerType;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Period;
import org.hl7.fhir.r4.model.Quantity;
import org.hl7.fhir.r4.model.Range;
import org.hl7.fhir.r4.model.Ratio;
import org.hl7.fhir.r4.model.Resource;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.TimeType;
import org.hl7.fhir.r4.model.Timing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.interopx.fhir.validator.util.ComparatorUtils;
import com.interopx.fhir.validator.util.ScenarioConstants.ResourceNames;
import com.interopx.fhir.validator.util.ScenarioConstants.ScenarioNameConstants;

public class ObservationContentComparator {
	public static final Logger logger = LoggerFactory.getLogger(ObservationContentComparator.class);

	public static void compare(Resource sourceResource, Resource targetResource, String scenarioName,
			OperationOutcome operationOutcome) {

		logger.info("Entry - ObservationContentComparator.compare");
		logger.info("Entry - ObservationContentComparator.compare - sourceResource ::\n" + sourceResource);
		logger.info("Entry - ObservationContentComparator.compare - targetResource ::\n" + targetResource);

		if (ObjectUtils.isNotEmpty(sourceResource) && ObjectUtils.isNotEmpty(targetResource)) {
			Observation sourceObservation = (Observation) sourceResource;
			Observation targetObservation = (Observation) targetResource;
			logger.info("sourceProcedure :::::\n" + sourceObservation);
			if (scenarioName.equalsIgnoreCase(ScenarioNameConstants.USCDI_FULL_OBSERVATION)) {
				logger.info("sourceProcedure ::::: compareFullContent\n");
				compareFullContent(sourceObservation, targetObservation, operationOutcome, scenarioName);
				logger.info("sourceProcedure ::::: compareFullContent\n");
			} else if (scenarioName.equalsIgnoreCase(ScenarioNameConstants.USCDI_ADDITIONAL_OBSERVATION)) {
				compareAdditionalContent(sourceObservation, targetObservation, operationOutcome, scenarioName);
			}
		}
		logger.info("Exit - ObservationContentComparator.compare");
	}

	private static void compareAdditionalContent(Observation sourceObservation, Observation targetObservation,
			OperationOutcome operationOutcome, String scenarioName) {

		logger.info("Entry - ObservationContentComparator.compareAdditionalContent");
		// status
		String source = null;
		String target = null;
		if (sourceObservation.getStatus() != null) {
			source = sourceObservation.getStatus().getDisplay();
		}
		if (targetObservation.getStatus() != null) {
			target = targetObservation.getStatus().getDisplay();
		}
		ComparatorUtils.compareString(ResourceNames.STATUS, source, target, operationOutcome, scenarioName);
		// category
		ComparatorUtils.compareListOfCodeableConcept(ResourceNames.CATEGORY, sourceObservation.getCategory(),
				targetObservation.getCategory(), operationOutcome, scenarioName);
		// code
		ComparatorUtils.compareCodeableConcept(ResourceNames.CODE, sourceObservation.getCode(),
				targetObservation.getCode(), operationOutcome, scenarioName);
		// subject
		ComparatorUtils.compareReference(ResourceNames.SUBJECT, sourceObservation.getSubject(),
				targetObservation.getSubject(), operationOutcome, scenarioName);

		// effective
		if (sourceObservation.getEffective() instanceof DateTimeType
				|| targetObservation.getEffective() instanceof DateTimeType) {
			// onsetDateTime
			ComparatorUtils.compareDateTimeType(ResourceNames.EFFECTIVE_DATE_TIME,
					sourceObservation.getEffectiveDateTimeType(), targetObservation.getEffectiveDateTimeType(),
					operationOutcome, scenarioName);
		}

		else if (sourceObservation.getEffective() instanceof Period
				|| targetObservation.getEffective() instanceof Period) {
			// effectivePeriod
			ComparatorUtils.comparePeriod(ResourceNames.EFFECTIVE_DATE_PERIOD, sourceObservation.getEffectivePeriod(),
					targetObservation.getEffectivePeriod(), operationOutcome, scenarioName);
		} else if (sourceObservation.getEffective() instanceof Timing
				|| targetObservation.getEffective() instanceof Timing) {
			// effectiveTiming
			ComparatorUtils.compareTiming(ResourceNames.EFFECTIVE_TIME, sourceObservation.getEffectiveTiming(),
					targetObservation.getEffectiveTiming(), operationOutcome, scenarioName);
		} else if (sourceObservation.getEffective() instanceof InstantType
				|| targetObservation.getEffective() instanceof InstantType) {

			// effectiveInstant
			ComparatorUtils.compareDate(ResourceNames.EFFECTIVE_INSTANT,
					sourceObservation.getEffectiveInstantType().getValue(),
					targetObservation.getEffectiveInstantType().getValue(), operationOutcome, scenarioName);
		}

		// value[x]

		if (sourceObservation.getValue() instanceof Quantity || targetObservation.getValue() instanceof Quantity) {
			// valueQuantity
			ComparatorUtils.compareQuantity(ResourceNames.VALUE_QUANTITY, sourceObservation.getValueQuantity(),
					targetObservation.getValueQuantity(), operationOutcome, scenarioName);
		}

		else if (sourceObservation.getValue() instanceof CodeableConcept
				|| targetObservation.getValue() instanceof CodeableConcept) {
			// valueCodeableConcept
			ComparatorUtils.compareCodeableConcept(ResourceNames.VALUE_CODEABLE_CONCEPT,
					sourceObservation.getValueCodeableConcept(), targetObservation.getValueCodeableConcept(),
					operationOutcome, scenarioName);
		} else if (sourceObservation.getValue() instanceof StringType
				|| targetObservation.getValue() instanceof StringType) {
			// valueString
			ComparatorUtils.compareString(ResourceNames.VALUE_STRING, sourceObservation.getValueStringType().toString(),
					targetObservation.getValueStringType().toString(), operationOutcome, scenarioName);
		} else if (sourceObservation.getValue() instanceof BooleanType
				|| targetObservation.getValue() instanceof BooleanType) {

			// valueBoolean
			ComparatorUtils.compareBooleanType(ResourceNames.VALUE_BOOLEAN, sourceObservation.getValueBooleanType(),
					targetObservation.getValueBooleanType(), operationOutcome, scenarioName);
		} else if (sourceObservation.getValue() instanceof IntegerType
				|| targetObservation.getValue() instanceof IntegerType) {

			// valueInteger
			ComparatorUtils.compareIntegerType(ResourceNames.VALUE_INTEGER, sourceObservation.getValueIntegerType(),
					targetObservation.getValueIntegerType(), operationOutcome, scenarioName);
		}

		else if (sourceObservation.getValue() instanceof Range || targetObservation.getValue() instanceof Range) {

			// valueRange
			ComparatorUtils.compareRange(ResourceNames.VALUE_RANGE, sourceObservation.getValueRange(),
					targetObservation.getValueRange(), operationOutcome, scenarioName);
		}

		else if (sourceObservation.getValue() instanceof Ratio || targetObservation.getValue() instanceof Ratio) {

			// valueRatio
			ComparatorUtils.compareRatio(ResourceNames.VALUE_RATIO, sourceObservation.getValueRatio(),
					targetObservation.getValueRatio(), operationOutcome, scenarioName);
		}

		else if (sourceObservation.getValue() instanceof TimeType || targetObservation.getValue() instanceof TimeType) {

			// valueTime
			ComparatorUtils.compareTimeType(ResourceNames.VALUE_TIME_TYPE, sourceObservation.getValueTimeType(),
					targetObservation.getValueTimeType(), operationOutcome, scenarioName);
		}

		else if (sourceObservation.getValue() instanceof DateTimeType
				|| targetObservation.getValue() instanceof DateTimeType) {

			// valueDateTime
			ComparatorUtils.compareDateTimeType(ResourceNames.VALUE_DATE_TIME, sourceObservation.getValueDateTimeType(),
					targetObservation.getValueDateTimeType(), operationOutcome, scenarioName);
		}

		else if (sourceObservation.getValue() instanceof Period || targetObservation.getValue() instanceof Period) {

			// valuePeriod
			ComparatorUtils.comparePeriod(ResourceNames.VALUE_PERIOD, sourceObservation.getValuePeriod(),
					targetObservation.getValuePeriod(), operationOutcome, scenarioName);
		}

		// dataAbsentReason
		ComparatorUtils.compareListOfReference(ResourceNames.DATE_ABSENT_REASON, sourceObservation.getPerformer(),
				targetObservation.getPerformer(), operationOutcome, scenarioName);

		logger.info("Exit - ObservationContentComparator.compareAdditionalContent");
	}

	private static void compareFullContent(Observation sourceObservation, Observation targetObservation,
			OperationOutcome operationOutcome, String scenarioName) {

		logger.info("Entry - ObservationContentComparator.compareFullContent");

		compareAdditionalContent(sourceObservation, targetObservation, operationOutcome, scenarioName);

		// Identifier
		ComparatorUtils.compareListOfIdentifier(ResourceNames.IDENTIFIER, sourceObservation.getIdentifier(),
				targetObservation.getIdentifier(), operationOutcome, scenarioName);

		// basedOn
		ComparatorUtils.compareListOfReference(ResourceNames.BASED_ON, sourceObservation.getBasedOn(),
				targetObservation.getBasedOn(), operationOutcome, scenarioName);
		// partOf
		ComparatorUtils.compareListOfReference(ResourceNames.PART_OF, sourceObservation.getPartOf(),
				targetObservation.getPartOf(), operationOutcome, scenarioName);

		// focus
		ComparatorUtils.compareListOfReference(ResourceNames.FOCUS, sourceObservation.getFocus(),
				targetObservation.getFocus(), operationOutcome, scenarioName);
		// encounter
		ComparatorUtils.compareReference(ResourceNames.ENCOUNTER, sourceObservation.getEncounter(),
				targetObservation.getEncounter(), operationOutcome, scenarioName);

		// issued
		ComparatorUtils.compareDate(ResourceNames.ISSUED, sourceObservation.getIssued(), targetObservation.getIssued(),
				operationOutcome, scenarioName);

		// performer
		ComparatorUtils.compareListOfReference(ResourceNames.PERFORMER, sourceObservation.getPerformer(),
				targetObservation.getPerformer(), operationOutcome, scenarioName);

		// interpretation
		ComparatorUtils.compareListOfCodeableConcept(ResourceNames.INTERPRETATION,
				sourceObservation.getInterpretation(), targetObservation.getInterpretation(), operationOutcome,
				scenarioName);
		// note
		ComparatorUtils.compareListOfAnnotation(ResourceNames.NOTE, sourceObservation.getNote(),
				targetObservation.getNote(), operationOutcome, scenarioName);

		// bodySite
		ComparatorUtils.compareCodeableConcept(ResourceNames.BODY_SITE, sourceObservation.getBodySite(),
				targetObservation.getBodySite(), operationOutcome, scenarioName);

		// method
		ComparatorUtils.compareCodeableConcept(ResourceNames.METHOD, sourceObservation.getMethod(),
				targetObservation.getMethod(), operationOutcome, scenarioName);

		// specimen
		ComparatorUtils.compareReference(ResourceNames.SPECIMEN, sourceObservation.getSpecimen(),
				targetObservation.getSpecimen(), operationOutcome, scenarioName);
		// device
		ComparatorUtils.compareReference(ResourceNames.DEVICE, sourceObservation.getDevice(),
				targetObservation.getDevice(), operationOutcome, scenarioName);

		// referenceRange
		ComparatorUtils.compareListOfObservationReferenceRangeComponent(ResourceNames.REFERENCE_RANGE,
				sourceObservation.getReferenceRange(), targetObservation.getReferenceRange(), operationOutcome,
				scenarioName);

		// hasMember
		ComparatorUtils.compareListOfReference(ResourceNames.DEVICE, sourceObservation.getHasMember(),
				targetObservation.getHasMember(), operationOutcome, scenarioName);

		// derivedFrom
		ComparatorUtils.compareListOfReference(ResourceNames.DEVICE, sourceObservation.getDerivedFrom(),
				targetObservation.getDerivedFrom(), operationOutcome, scenarioName);

		// component
		ComparatorUtils.compareListOfObservationComponentComponent(ResourceNames.COMPONENT,
				sourceObservation.getComponent(), targetObservation.getComponent(), operationOutcome, scenarioName);

		logger.info("Exit - ObservationContentComparator.compareFullContent");

	}

}
