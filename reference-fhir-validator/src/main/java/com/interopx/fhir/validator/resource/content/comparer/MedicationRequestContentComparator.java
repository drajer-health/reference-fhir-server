package com.interopx.fhir.validator.resource.content.comparer;

import org.apache.commons.lang3.ObjectUtils;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.DateTimeType;
import org.hl7.fhir.r4.model.Goal;
import org.hl7.fhir.r4.model.MedicationRequest;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.interopx.fhir.validator.util.ComparatorUtils;
import com.interopx.fhir.validator.util.ScenarioConstants.ResourceNames;
import com.interopx.fhir.validator.util.ScenarioConstants.ScenarioNameConstants;

public class MedicationRequestContentComparator {
public static final Logger logger=LoggerFactory.getLogger(MedicationRequestContentComparator.class);
	
	public static void compare(Resource sourceResource, Resource targetResource,String scenarioName,OperationOutcome operationOutcome) {
		
		logger.info("Entry - MedicationRequestComparator.compare");
		logger.info("Entry - MedicationRequestComparator.compare - sourceResource ::\n"+sourceResource);
		logger.info("Entry - MedicationRequestContentComparator.compare - targetResource ::\n"+targetResource);
		
		if (ObjectUtils.isNotEmpty(sourceResource) && ObjectUtils.isNotEmpty(targetResource)) {
			MedicationRequest sourceMedicationRequest = (MedicationRequest) sourceResource;
			MedicationRequest targetMedicationRequest = (MedicationRequest) targetResource;
			logger.info("sourceMedicationRequest :::::\n"+sourceMedicationRequest);
			if (scenarioName.equalsIgnoreCase(ScenarioNameConstants.USCDI_FULL_MEDICATION_REQUEST)) {
				logger.info("sourceMedicationRequest ::::: compareFullContent\n");
				compareFullContent(sourceMedicationRequest, targetMedicationRequest,operationOutcome,scenarioName);
				logger.info("sourceMedicationRequest ::::: compareFullContent\n");
			}else if (scenarioName.equalsIgnoreCase(ScenarioNameConstants.USCDI_ADDITIONAL_MEDICATION_REQUEST)) {
				compareAdditionalContent(sourceMedicationRequest, targetMedicationRequest,operationOutcome,scenarioName);
			}
		}
		logger.info("Exit - MedicationRequestContentComparator.compare");
	}

	private static void compareAdditionalContent(MedicationRequest sourceMedicationRequest,
			MedicationRequest targetMedicationRequest, OperationOutcome operationOutcome,String scenarioName) {
		// status
		String source = null;
		String target=null;
		if(sourceMedicationRequest.getStatus()!=null) {
			source = sourceMedicationRequest.getStatus().toString();	
		}
		if(targetMedicationRequest.getStatus()!=null) {
			target = targetMedicationRequest.getStatus().toString();	
		}
		ComparatorUtils.compareString(ResourceNames.STATUS, source,target, operationOutcome, scenarioName);		
		//intent
		String sourceintent = null;
		String targetintent=null;
		if(sourceMedicationRequest.getIntent()!=null) {
			sourceintent = sourceMedicationRequest.getIntent().toString();	
		}
		if(targetMedicationRequest.getIntent()!=null) {
			targetintent = targetMedicationRequest.getIntent().toString();	
		}
		ComparatorUtils.compareString(ResourceNames.INTENT, sourceintent,targetintent, operationOutcome, scenarioName);	
		
		//medication
		if (sourceMedicationRequest.getMedication() instanceof CodeableConcept || targetMedicationRequest.getMedication() instanceof CodeableConcept ) {
			ComparatorUtils.compareCodeableConcept(ResourceNames.MEDICATION_CODEABLECONCEPT,sourceMedicationRequest.getMedicationCodeableConcept(),targetMedicationRequest.getMedicationCodeableConcept(),operationOutcome,scenarioName);
		}
		if (sourceMedicationRequest.getMedication() instanceof Reference || targetMedicationRequest.getMedication() instanceof Reference ) {
			ComparatorUtils.compareReference(ResourceNames.MEDICATION_REFERENCE,sourceMedicationRequest.getMedicationReference(),targetMedicationRequest.getMedicationReference(),operationOutcome,scenarioName);
		}

		//reported
		if (sourceMedicationRequest.getReported() instanceof BooleanType || targetMedicationRequest.getReported() instanceof BooleanType ) {
			ComparatorUtils.compareBooleanType(ResourceNames.REPORTED_BOOLEAN,sourceMedicationRequest.getReportedBooleanType(),targetMedicationRequest.getReportedBooleanType(),operationOutcome,scenarioName);
		}
		if (sourceMedicationRequest.getReported() instanceof Reference || targetMedicationRequest.getReported() instanceof Reference ) {
			ComparatorUtils.compareReference(ResourceNames.REPORTED_REFERENCE,sourceMedicationRequest.getReportedReference(),targetMedicationRequest.getReportedReference(),operationOutcome,scenarioName);
		}
		
		//encounter
		ComparatorUtils.compareReference(ResourceNames.ENCOUNTER, sourceMedicationRequest.getEncounter(), targetMedicationRequest.getEncounter(), operationOutcome,scenarioName);
		// subject
		ComparatorUtils.compareReference(ResourceNames.SUBJECT, sourceMedicationRequest.getSubject(),targetMedicationRequest.getSubject(), operationOutcome,scenarioName);
		//authoredOn
		ComparatorUtils.compareDate(ResourceNames.AUTHORED_ON, sourceMedicationRequest.getAuthoredOn(),targetMedicationRequest.getAuthoredOn(), operationOutcome,scenarioName);
		//requester
		ComparatorUtils.compareReference(ResourceNames.REQUESTER, sourceMedicationRequest.getRequester(),targetMedicationRequest.getRequester(), operationOutcome,scenarioName);
		//dosageInstruction
		ComparatorUtils.compareListOfDosage(ResourceNames.DOSAGE_INSTRUCTION, sourceMedicationRequest.getDosageInstruction(),targetMedicationRequest.getDosageInstruction(), operationOutcome,scenarioName);
	}

	private static void compareFullContent(MedicationRequest sourceMedicationRequest,
			MedicationRequest targetMedicationRequest, OperationOutcome operationOutcome,String scenarioName) {
			//Identifier
			ComparatorUtils.compareListOfIdentifier(ResourceNames.IDENTIFIER,sourceMedicationRequest.getIdentifier(),targetMedicationRequest.getIdentifier(),operationOutcome,scenarioName);
			//Extension
			ComparatorUtils.compareListOfExtension(ResourceNames.EXTENSION,sourceMedicationRequest.getExtension(), targetMedicationRequest.getExtension(), operationOutcome,scenarioName);
			//id
	        ComparatorUtils.compareString(ResourceNames.ID, sourceMedicationRequest.getId(), targetMedicationRequest.getId(), operationOutcome,scenarioName);
	        //implicitRules
			ComparatorUtils.compareString(ResourceNames.IMPLICIT_RULES, sourceMedicationRequest.getImplicitRules(), targetMedicationRequest.getImplicitRules(), operationOutcome,scenarioName);
			//language
			ComparatorUtils.compareLanguage(ResourceNames.LANGUAGE, sourceMedicationRequest.getLanguage(), targetMedicationRequest.getLanguage(), operationOutcome,scenarioName);
			//text
			ComparatorUtils.compareNarrative(ResourceNames.TEXT, sourceMedicationRequest.getText(), targetMedicationRequest.getText(), operationOutcome,scenarioName);
			//contained
			ComparatorUtils.compareListOfResource(ResourceNames.CONTAINED, sourceMedicationRequest.getContained(), targetMedicationRequest.getContained(), operationOutcome,scenarioName);
			// status
			String source = null;
			String target=null;
			if(sourceMedicationRequest.getStatus()!=null) {
				source = sourceMedicationRequest.getStatus().toString();	
			}
			if(targetMedicationRequest.getStatus()!=null) {
				target = targetMedicationRequest.getStatus().toString();	
			}
			ComparatorUtils.compareString(ResourceNames.STATUS, source,target, operationOutcome,scenarioName);		
			//intent
			String sourceintent = null;
			String targetintent=null;
			if(sourceMedicationRequest.getIntent()!=null) {
				sourceintent = sourceMedicationRequest.getIntent().toString();	
			}
			if(targetMedicationRequest.getIntent()!=null) {
				targetintent = targetMedicationRequest.getIntent().toString();	
			}
			ComparatorUtils.compareString(ResourceNames.INTENT, sourceintent,targetintent, operationOutcome,scenarioName);	
			//statusReason
			ComparatorUtils.compareCodeableConcept(ResourceNames.STATUS_REASON,sourceMedicationRequest.getStatusReason(),targetMedicationRequest.getStatusReason(),operationOutcome,scenarioName);
			// category
			ComparatorUtils.compareListOfCodeableConcept(ResourceNames.CATEGORY, sourceMedicationRequest.getCategory(),targetMedicationRequest.getCategory(), operationOutcome,scenarioName);
			// priority
			String sourcepriority = null;
			String targetpriority=null;
			if(sourceMedicationRequest.getPriority()!=null) {
				sourcepriority = sourceMedicationRequest.getPriority().toString();	
			}
			if(targetMedicationRequest.getPriority()!=null) {
				targetpriority = targetMedicationRequest.getPriority().toString();	
			}
			ComparatorUtils.compareString(ResourceNames.PRIORITY, sourcepriority,targetpriority, operationOutcome,scenarioName);
			//doNotPerform
			ComparatorUtils.compareBoolean(ResourceNames.DO_NOT_PERFORM, sourceMedicationRequest.getDoNotPerform(),targetMedicationRequest.getDoNotPerform(), operationOutcome,scenarioName);
			//encounter
			ComparatorUtils.compareReference(ResourceNames.ENCOUNTER, sourceMedicationRequest.getEncounter(), targetMedicationRequest.getEncounter(), operationOutcome,scenarioName);
			// subject
			ComparatorUtils.compareReference(ResourceNames.SUBJECT, sourceMedicationRequest.getSubject(),targetMedicationRequest.getSubject(), operationOutcome,scenarioName);
			//supportingInformation
			ComparatorUtils.compareListOfReference(ResourceNames.SUPPORTING_INFORMATION, sourceMedicationRequest.getSupportingInformation(),targetMedicationRequest.getSupportingInformation(), operationOutcome,scenarioName);
			//authoredOn
			ComparatorUtils.compareDate(ResourceNames.AUTHORED_ON, sourceMedicationRequest.getAuthoredOn(),targetMedicationRequest.getAuthoredOn(), operationOutcome,scenarioName);
			//requester
			ComparatorUtils.compareReference(ResourceNames.REQUESTER, sourceMedicationRequest.getRequester(),targetMedicationRequest.getRequester(), operationOutcome,scenarioName);
			//performer
			ComparatorUtils.compareReference(ResourceNames.PERFORMER, sourceMedicationRequest.getPerformer(),targetMedicationRequest.getPerformer(), operationOutcome,scenarioName);
			//performerType
			ComparatorUtils.compareCodeableConcept(ResourceNames.PERFORMER_TYPE, sourceMedicationRequest.getPerformerType(),targetMedicationRequest.getPerformerType(), operationOutcome,scenarioName);
			//recorder
			ComparatorUtils.compareReference(ResourceNames.RECORDER, sourceMedicationRequest.getRecorder(),targetMedicationRequest.getRecorder(), operationOutcome,scenarioName);
			//reasonCode
			ComparatorUtils.compareListOfCodeableConcept(ResourceNames.REASON_CODE, sourceMedicationRequest.getReasonCode(),targetMedicationRequest.getReasonCode(), operationOutcome,scenarioName);		
			//reasonReference
			ComparatorUtils.compareListOfReference(ResourceNames.REASON_REFERENCE, sourceMedicationRequest.getReasonReference(),targetMedicationRequest.getReasonReference(), operationOutcome,scenarioName);
			//basedOn
			ComparatorUtils.compareListOfReference(ResourceNames.BASED_ON, sourceMedicationRequest.getBasedOn(),targetMedicationRequest.getBasedOn(), operationOutcome,scenarioName);
			//note
			ComparatorUtils.compareListOfAnnotation(ResourceNames.NOTE, sourceMedicationRequest.getNote(),targetMedicationRequest.getNote(), operationOutcome,scenarioName);
			//courseOfTherapyType
			ComparatorUtils.compareCodeableConcept(ResourceNames.COURSE_OF_THERAPY_TYPE, sourceMedicationRequest.getCourseOfTherapyType(),targetMedicationRequest.getCourseOfTherapyType(), operationOutcome,scenarioName);
			//insurance
			ComparatorUtils.compareListOfReference(ResourceNames.INSURANCE, sourceMedicationRequest.getInsurance(),targetMedicationRequest.getInsurance(), operationOutcome,scenarioName);
			//groupIdentifier
			ComparatorUtils.compareIdentifier(ResourceNames.GROUP_IDENTIFIER, sourceMedicationRequest.getGroupIdentifier(),targetMedicationRequest.getGroupIdentifier(), operationOutcome,scenarioName);
			//medication
			if (sourceMedicationRequest.getMedication() instanceof CodeableConcept || targetMedicationRequest.getMedication() instanceof CodeableConcept ) {
				ComparatorUtils.compareCodeableConcept(ResourceNames.MEDICATION_CODEABLECONCEPT,sourceMedicationRequest.getMedicationCodeableConcept(),targetMedicationRequest.getMedicationCodeableConcept(),operationOutcome,scenarioName);
			}
			if (sourceMedicationRequest.getMedication() instanceof Reference || targetMedicationRequest.getMedication() instanceof Reference ) {
				ComparatorUtils.compareReference(ResourceNames.MEDICATION_REFERENCE,sourceMedicationRequest.getMedicationReference(),targetMedicationRequest.getMedicationReference(),operationOutcome,scenarioName);
			}

			//reported
			if (sourceMedicationRequest.getReported() instanceof BooleanType || targetMedicationRequest.getReported() instanceof BooleanType ) {
				ComparatorUtils.compareBooleanType(ResourceNames.REPORTED_BOOLEAN,sourceMedicationRequest.getReportedBooleanType(),targetMedicationRequest.getReportedBooleanType(),operationOutcome,scenarioName);
			}
			if (sourceMedicationRequest.getReported() instanceof Reference || targetMedicationRequest.getReported() instanceof Reference ) {
				ComparatorUtils.compareReference(ResourceNames.REPORTED_REFERENCE,sourceMedicationRequest.getReportedReference(),targetMedicationRequest.getReportedReference(),operationOutcome,scenarioName);
			}
			
			//dosageInstruction
			ComparatorUtils.compareListOfDosage(ResourceNames.DOSAGE_INSTRUCTION, sourceMedicationRequest.getDosageInstruction(),targetMedicationRequest.getDosageInstruction(), operationOutcome,scenarioName);
				
			//dispenseRequest
			ComparatorUtils.compareMedicationRequestDispenseRequestComponent(ResourceNames.DISPENSE_REQUEST, sourceMedicationRequest.getDispenseRequest(),targetMedicationRequest.getDispenseRequest(), operationOutcome,scenarioName);

			//substitution
			ComparatorUtils.compareMedicationRequestSubstitutionComponent(ResourceNames.SUBSTITUTION, sourceMedicationRequest.getSubstitution(),targetMedicationRequest.getSubstitution(), operationOutcome,scenarioName);
			
			//priorPrescription
			ComparatorUtils.compareReference(ResourceNames.PRIOR_PRESCRIPTION, sourceMedicationRequest.getPriorPrescription(),targetMedicationRequest.getPriorPrescription(), operationOutcome,scenarioName);

			//detectedIssue
			ComparatorUtils.compareListOfReference(ResourceNames.DETECTED_ISSUE, sourceMedicationRequest.getDetectedIssue(),targetMedicationRequest.getDetectedIssue(), operationOutcome,scenarioName);

			//eventHistory
			ComparatorUtils.compareListOfReference(ResourceNames.EVENT_HISTORY, sourceMedicationRequest.getEventHistory(),targetMedicationRequest.getEventHistory(), operationOutcome,scenarioName);			
			
	}

}
