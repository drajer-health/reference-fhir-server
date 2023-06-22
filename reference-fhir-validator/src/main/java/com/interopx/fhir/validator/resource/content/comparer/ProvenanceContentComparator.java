package com.interopx.fhir.validator.resource.content.comparer;

import java.util.List;

import org.apache.commons.lang3.ObjectUtils;
import org.hl7.fhir.r4.model.DateTimeType;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Period;
import org.hl7.fhir.r4.model.Provenance;
import org.hl7.fhir.r4.model.Resource;
import org.hl7.fhir.r4.model.Provenance.ProvenanceAgentComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.interopx.fhir.validator.util.ComparatorUtils;
import com.interopx.fhir.validator.util.ScenarioConstants.ResourceNames;
import com.interopx.fhir.validator.util.ScenarioConstants.ScenarioNameConstants;

public class ProvenanceContentComparator {
	public static final Logger logger = LoggerFactory.getLogger(ProvenanceContentComparator.class);

	public static void compare(Resource sourceResource, Resource targetResource, String scenarioName,
			OperationOutcome operationOutcome) {

		logger.info("Entry - ProvenanceContentComparator.compare");
		logger.info("Entry - ProvenanceContentComparator.compare - sourceResource ::\n" + sourceResource);
		logger.info("Entry - ProvenanceContentComparator.compare - targetResource ::\n" + targetResource);

		if (ObjectUtils.isNotEmpty(sourceResource) && ObjectUtils.isNotEmpty(targetResource)) {
			Provenance sourceProvenance = (Provenance) sourceResource;
			Provenance targetProvenance = (Provenance) targetResource;
			logger.info("sourceProcedure :::::\n" + sourceProvenance);
			if (scenarioName.equalsIgnoreCase(ScenarioNameConstants.USCDI_FULL_PROVENANCE)) {
				logger.info("sourceProcedure ::::: compareFullContent\n");
				compareFullContent(sourceProvenance, targetProvenance, operationOutcome, scenarioName);
				logger.info("sourceProcedure ::::: compareFullContent\n");
			} else if (scenarioName.equalsIgnoreCase(ScenarioNameConstants.USCDI_ADDITIONAL_PROVENANCE)) {
				compareAdditionalContent(sourceProvenance, targetProvenance, operationOutcome, scenarioName);
			}
		}
		logger.info("Exit - ProvenanceContentComparator.compare");
	}

	private static void compareAdditionalContent(Provenance sourceProvenance, Provenance targetProvenance,
			OperationOutcome operationOutcome, String scenarioName) {

		logger.info("Entry - ProvenanceContentComparator.compareAdditionalContent");
		// target
		ComparatorUtils.compareListOfReference(ResourceNames.TARGET, sourceProvenance.getTarget(),
				targetProvenance.getTarget(), operationOutcome, scenarioName);

		// recorded
		ComparatorUtils.compareDate(ResourceNames.RECORDED, sourceProvenance.getRecorded(),
				targetProvenance.getRecorded(), operationOutcome, scenarioName);
		// agent
		ComparatorUtils.compareListOfProvenanceAgentComponent(ResourceNames.AGENT, sourceProvenance.getAgent(),
				targetProvenance.getAgent(), operationOutcome, scenarioName);

		logger.info("Exit - ProvenanceContentComparator.compareAdditionalContent");
	}

	private static void compareFullContent(Provenance sourceProvenance, Provenance targetProvenance,
			OperationOutcome operationOutcome, String scenarioName) {

		logger.info("Entry - ProvenanceContentComparator.compareFullContent");

		// id
		ComparatorUtils.compareString(ResourceNames.ID, sourceProvenance.getId(), targetProvenance.getId(),
				operationOutcome, scenarioName);

		// implicitRules
		ComparatorUtils.compareString(ResourceNames.IMPLICIT_RULES, sourceProvenance.getImplicitRules(),
				targetProvenance.getImplicitRules(), operationOutcome, scenarioName);

		compareAdditionalContent(sourceProvenance, targetProvenance, operationOutcome, scenarioName);

		// occurred[x]
		if (sourceProvenance.getOccurred() instanceof DateTimeType
				|| targetProvenance.getOccurred() instanceof DateTimeType) {
			// occurredDateTime
			ComparatorUtils.compareDateTimeType(ResourceNames.OCCURRED_DATE_TIME,
					sourceProvenance.getOccurredDateTimeType(), targetProvenance.getOccurredDateTimeType(),
					operationOutcome, scenarioName);
		}

		else if (sourceProvenance.getOccurred() instanceof Period || targetProvenance.getOccurred() instanceof Period) {
			// occurredPeriod
			ComparatorUtils.comparePeriod(ResourceNames.OCCURRED_PERIOD, sourceProvenance.getOccurredPeriod(),
					targetProvenance.getOccurredPeriod(), operationOutcome, scenarioName);

			// location
			ComparatorUtils.compareReference(ResourceNames.LOCATION, sourceProvenance.getLocation(),
					targetProvenance.getLocation(), operationOutcome, scenarioName);

			// reason
			ComparatorUtils.compareListOfCodeableConcept(ResourceNames.REASON, sourceProvenance.getReason(),
					targetProvenance.getReason(), operationOutcome, scenarioName);

			// activity
			ComparatorUtils.compareCodeableConcept(ResourceNames.ACTIVITY, sourceProvenance.getActivity(),
					targetProvenance.getActivity(), operationOutcome, scenarioName);

			// entity
			ComparatorUtils.compareListOfProvenanceEntityComponent(ResourceNames.ENTITY, sourceProvenance.getEntity(),
					targetProvenance.getEntity(), operationOutcome, scenarioName);

			// signature
			ComparatorUtils.compareListOfSignature(ResourceNames.SIGNATURE, sourceProvenance.getSignature(),
					targetProvenance.getSignature(), operationOutcome, scenarioName);
		}

		logger.info("Exit - ProvenanceContentComparator.compareFullContent");

	}

}
