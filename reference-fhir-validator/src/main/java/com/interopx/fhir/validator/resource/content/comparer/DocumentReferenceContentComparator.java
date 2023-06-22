package com.interopx.fhir.validator.resource.content.comparer;

import org.apache.commons.lang3.ObjectUtils;
import org.hl7.fhir.r4.model.DocumentReference;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.interopx.fhir.validator.util.ComparatorUtils;
import com.interopx.fhir.validator.util.ScenarioConstants.ResourceNames;
import com.interopx.fhir.validator.util.ScenarioConstants.ScenarioNameConstants;

public class DocumentReferenceContentComparator {
	public static final Logger logger = LoggerFactory.getLogger(DocumentReferenceContentComparator.class);

	public static void compare(Resource sourceResource, Resource targetResource, String scenarioName,
			OperationOutcome operationOutcome) {

		logger.info("Entry - DocumentReferenceContentComparator.compare");
		logger.info("Entry - DocumentReferenceContentComparator.compare - sourceResource ::\n" + sourceResource);
		logger.info("Entry - DocumentReferenceContentComparator.compare - targetResource ::\n" + targetResource);

		if (ObjectUtils.isNotEmpty(sourceResource) && ObjectUtils.isNotEmpty(targetResource)) {
			DocumentReference sourceDocumentReference = (DocumentReference) sourceResource;
			DocumentReference targetDocumentReference = (DocumentReference) targetResource;
			logger.info("sourceProcedure :::::\n" + sourceDocumentReference);
			if (scenarioName.equalsIgnoreCase(ScenarioNameConstants.USCDI_FULL_DOCUMENTREFERENCE)) {
				logger.info("sourceProcedure ::::: compareFullContent\n");
				compareFullContent(sourceDocumentReference, targetDocumentReference, operationOutcome, scenarioName);
				logger.info("sourceProcedure ::::: compareFullContent\n");
			} else if (scenarioName.equalsIgnoreCase(ScenarioNameConstants.USCDI_ADDITIONAL_DOCUMENTREFERENCE)) {
				compareAdditionalContent(sourceDocumentReference, targetDocumentReference, operationOutcome,
						scenarioName);
			}
		}
		logger.info("Exit - DocumentReferenceContentComparator.compare");
	}

	private static void compareAdditionalContent(DocumentReference sourceDocumentReference,
			DocumentReference targetDocumentReference, OperationOutcome operationOutcome, String scenarioName) {

		logger.info("Entry - DocumentReferenceContentComparator.compareAdditionalContent");
		// identifier
		ComparatorUtils.compareListOfIdentifier(ResourceNames.IDENTIFIER, sourceDocumentReference.getIdentifier(),
				targetDocumentReference.getIdentifier(), operationOutcome, scenarioName);

		// status
		String source = null;
		String target = null;
		if (sourceDocumentReference.getStatus() != null) {
			source = sourceDocumentReference.getStatus().toString();
		}
		if (targetDocumentReference.getStatus() != null) {
			target = targetDocumentReference.getStatus().toString();
		}
		ComparatorUtils.compareString(ResourceNames.STATUS, source, target, operationOutcome, scenarioName);

		// type
		ComparatorUtils.compareCodeableConcept(ResourceNames.STATUS_REASON, sourceDocumentReference.getType(),
				targetDocumentReference.getType(), operationOutcome, scenarioName);
		// subject
		ComparatorUtils.compareReference(ResourceNames.SUBJECT, sourceDocumentReference.getSubject(),
				targetDocumentReference.getSubject(), operationOutcome, scenarioName);

		// date
		ComparatorUtils.compareDate(ResourceNames.CREATED, sourceDocumentReference.getDate(),
				targetDocumentReference.getDate(), operationOutcome, scenarioName);

		// author
		ComparatorUtils.compareListOfReference(ResourceNames.AUTHOR, sourceDocumentReference.getAuthor(),
				targetDocumentReference.getAuthor(), operationOutcome, scenarioName);

		// content
		ComparatorUtils.compareListOfDocumentReferenceContentComponent(ResourceNames.CONTENT,
				sourceDocumentReference.getContent(), targetDocumentReference.getContent(), operationOutcome,
				scenarioName);

		// context
		ComparatorUtils.compareDocumentReferenceContextComponent(ResourceNames.CONTEXT,
				sourceDocumentReference.getContext(), targetDocumentReference.getContext(), operationOutcome,
				scenarioName);

		logger.info("Exit - DocumentReferenceContentComparator.compareAdditionalContent");
	}

	private static void compareFullContent(DocumentReference sourceDocumentReference,
			DocumentReference targetDocumentReference, OperationOutcome operationOutcome, String scenarioName) {

		logger.info("Entry - DocumentReferenceContentComparator.compareFullContent");
		// id
		ComparatorUtils.compareString(ResourceNames.ID, sourceDocumentReference.getId(),
				targetDocumentReference.getId(), operationOutcome, scenarioName);

		compareAdditionalContent(sourceDocumentReference, targetDocumentReference, operationOutcome, scenarioName);

		// docStatus
		String sourceStatus = null;
		String targetStatus = null;
		if (sourceDocumentReference.getStatus() != null) {
			sourceStatus = sourceDocumentReference.getDocStatus().toString();
		}
		if (targetDocumentReference.getStatus() != null) {
			targetStatus = targetDocumentReference.getDocStatus().toString();
		}
		ComparatorUtils.compareString(ResourceNames.DOC_STATUS, sourceStatus, targetStatus, operationOutcome,
				scenarioName);

		// custodian
		ComparatorUtils.compareReference(ResourceNames.CUSTODIAN, sourceDocumentReference.getCustodian(),
				targetDocumentReference.getCustodian(), operationOutcome, scenarioName);
		// description
		ComparatorUtils.compareString(ResourceNames.DESCRIPTION, sourceDocumentReference.getDescription(),
				targetDocumentReference.getDescription(), operationOutcome, scenarioName);

		// securityLabel
		ComparatorUtils.compareListOfCodeableConcept(ResourceNames.SECURITY_LABEL,
				sourceDocumentReference.getSecurityLabel(), targetDocumentReference.getSecurityLabel(),
				operationOutcome, scenarioName);
		// relatesTo
		ComparatorUtils.compareListOfDocumentReferenceRelatesToComponent(ResourceNames.RELATES_TO,
				sourceDocumentReference.getRelatesTo(), targetDocumentReference.getRelatesTo(), operationOutcome,
				scenarioName);

		logger.info("Exit - DocumentReferenceContentComparator.compareFullContent");

	}

}
