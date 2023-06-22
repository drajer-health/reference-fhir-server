package com.interopx.fhir.validator.resource.content.comparer;

import org.apache.commons.lang3.ObjectUtils;
import org.hl7.fhir.r4.model.DateTimeType;
import org.hl7.fhir.r4.model.DiagnosticReport;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Resource;
import org.hl7.fhir.r4.model.StringType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.interopx.fhir.validator.util.ComparatorUtils;
import com.interopx.fhir.validator.util.ScenarioConstants.ResourceNames;
import com.interopx.fhir.validator.util.ScenarioConstants.ScenarioNameConstants;

public class DiagnosticReportContentComparator {
	
	public static final Logger logger = LoggerFactory.getLogger(DiagnosticReportContentComparator.class);

	public static void compare(Resource sourceResource, Resource targetResource, String scenarioName,
			OperationOutcome operationOutcome) {
		logger.info("Entry - DiagnosticReportContentComparator.compare");
		logger.info("Entry - DiagnosticReportContentComparator.compare - sourceResource ::\n" + sourceResource);
		logger.info("Entry - DiagnosticReportContentComparator.compare - targetResource ::\n" + targetResource);
		if (ObjectUtils.isNotEmpty(sourceResource) && ObjectUtils.isNotEmpty(targetResource)) {
			DiagnosticReport sourceDiagnosticReport = (DiagnosticReport) sourceResource;
			DiagnosticReport targetDiagnosticReport = (DiagnosticReport) targetResource;
			logger.info("sourceEncounter :::::\n" + sourceDiagnosticReport);
			if (scenarioName.equalsIgnoreCase(ScenarioNameConstants.USCDI_FULL_DIAGNOSTICREPORT)) {
				logger.info("sourceDiagnosticReport ::::: compareFullContent\n");
				compareFullContent(sourceDiagnosticReport, targetDiagnosticReport, operationOutcome,scenarioName);
				logger.info("sourceDiagnosticReport ::::: compareFullContent\n");
			} else if (scenarioName.equalsIgnoreCase(ScenarioNameConstants.USCDI_ADDITIONAL_DIAGNOSTICREPORT)) {
				compareAdditionalContent(sourceDiagnosticReport, targetDiagnosticReport, operationOutcome,scenarioName);
			}
		}
		logger.info("Exit - DiagnosticReportContentComparator.compare");
	}

	private static void compareAdditionalContent(DiagnosticReport sourceDiagnosticReport,
			DiagnosticReport targetDiagnosticReport, OperationOutcome operationOutcome,String scenarioName) {

		// status
		String source = null;
		String target=null;
		if(sourceDiagnosticReport.getStatus()!=null) {
			source = sourceDiagnosticReport.getStatus().toString();	
		}
		if(targetDiagnosticReport.getStatus()!=null) {
			target = targetDiagnosticReport.getStatus().toString();	
		}
		ComparatorUtils.compareString(ResourceNames.STATUS, source,target, operationOutcome,scenarioName);
		// category
		ComparatorUtils.compareListOfCodeableConcept(ResourceNames.CATEGORY, sourceDiagnosticReport.getCategory(),
				targetDiagnosticReport.getCategory(), operationOutcome,scenarioName);

		// code
		ComparatorUtils.compareCodeableConcept(ResourceNames.CODE, sourceDiagnosticReport.getCode(),
				targetDiagnosticReport.getCode(), operationOutcome,scenarioName);

		// subject
		ComparatorUtils.compareReference(ResourceNames.SUBJECT, sourceDiagnosticReport.getSubject(),
				targetDiagnosticReport.getSubject(), operationOutcome,scenarioName);

		// encounter
		ComparatorUtils.compareReference(ResourceNames.ENCOUNTER, sourceDiagnosticReport.getEncounter(),
				targetDiagnosticReport.getEncounter(), operationOutcome,scenarioName);

		// effectiveDateTime
		if (sourceDiagnosticReport.getEffective() instanceof DateTimeType
				|| targetDiagnosticReport.getEffective() instanceof DateTimeType)
			ComparatorUtils.compareDateTimeType(ResourceNames.EFFECTIVE_DATE_TIME,
					sourceDiagnosticReport.getEffectiveDateTimeType(),
					targetDiagnosticReport.getEffectiveDateTimeType(), operationOutcome,scenarioName);

		// effectivePeriod
		if (sourceDiagnosticReport.getEffective() instanceof StringType
				&& targetDiagnosticReport.getEffective() instanceof StringType)
			ComparatorUtils.comparePeriod(ResourceNames.EFFECTIVE_DATE_PERIOD,
					sourceDiagnosticReport.getEffectivePeriod(), targetDiagnosticReport.getEffectivePeriod(),
					operationOutcome,scenarioName);

		// issued
		ComparatorUtils.compareDate(ResourceNames.ISSUED, sourceDiagnosticReport.getIssued(),
		targetDiagnosticReport.getIssued(), operationOutcome,scenarioName);
			
		
		// performer
		ComparatorUtils.compareListOfReference(ResourceNames.PERFORMER, sourceDiagnosticReport.getPerformer(),
			targetDiagnosticReport.getPerformer(), operationOutcome,scenarioName);
		
		//presentedForm
		ComparatorUtils.compareListOfAttachment(ResourceNames.PRESENTED_FORM,sourceDiagnosticReport.getPresentedForm(),targetDiagnosticReport.getPresentedForm(),operationOutcome,scenarioName);
	}

	private static void compareFullContent(DiagnosticReport sourceDiagnosticReport,
			DiagnosticReport targetDiagnosticReport, OperationOutcome operationOutcome,String scenarioName) {
		//id
		ComparatorUtils.compareString(ResourceNames.ID,sourceDiagnosticReport.getId(),targetDiagnosticReport.getId(),operationOutcome,scenarioName);
		
		//identifier
		ComparatorUtils.compareListOfIdentifier(ResourceNames.IDENTIFIER,sourceDiagnosticReport.getIdentifier(),targetDiagnosticReport.getIdentifier(),operationOutcome,scenarioName);
		
		//basedOn
		ComparatorUtils.compareListOfReference(ResourceNames.BASED_ON,sourceDiagnosticReport.getBasedOn(),targetDiagnosticReport.getBasedOn(),operationOutcome,scenarioName);
				
		//status
		String source = null;
		String target=null;
		if(sourceDiagnosticReport.getStatus()!=null) {
			source = sourceDiagnosticReport.getStatus().toString();	
		}
		if(targetDiagnosticReport.getStatus()!=null) {
			target = targetDiagnosticReport.getStatus().toString();	
		}
		ComparatorUtils.compareString(ResourceNames.STATUS, source,target, operationOutcome,scenarioName);		
		//category
		ComparatorUtils.compareListOfCodeableConcept(ResourceNames.CATEGORY,sourceDiagnosticReport.getCategory(),targetDiagnosticReport.getCategory(),operationOutcome,scenarioName);
		
		//code
		ComparatorUtils.compareCodeableConcept(ResourceNames.CODE,sourceDiagnosticReport.getCode(),targetDiagnosticReport.getCode(),operationOutcome,scenarioName);
		
		//subject
		ComparatorUtils.compareReference(ResourceNames.SUBJECT,sourceDiagnosticReport.getSubject(),targetDiagnosticReport.getSubject(),operationOutcome,scenarioName);
				
		//encounter
		ComparatorUtils.compareReference(ResourceNames.ENCOUNTER,sourceDiagnosticReport.getEncounter(),targetDiagnosticReport.getEncounter(),operationOutcome,scenarioName);
		
		//effectiveDateTime	
		if (sourceDiagnosticReport.getEffective() instanceof DateTimeType || targetDiagnosticReport.getEffective() instanceof DateTimeType ) {
		ComparatorUtils.compareDateTimeType(ResourceNames.EFFECTIVE_DATE_TIME,sourceDiagnosticReport.getEffectiveDateTimeType(),targetDiagnosticReport.getEffectiveDateTimeType(),operationOutcome,scenarioName);
		}
		//effectivePeriod
		if (sourceDiagnosticReport.getEffective() instanceof StringType && targetDiagnosticReport.getEffective() instanceof StringType ) {
		ComparatorUtils.comparePeriod(ResourceNames.EFFECTIVE_DATE_PERIOD,sourceDiagnosticReport.getEffectivePeriod(),targetDiagnosticReport.getEffectivePeriod(),operationOutcome,scenarioName);
		}
		//issued
		ComparatorUtils.compareDate(ResourceNames.ISSUED,sourceDiagnosticReport.getIssued(),targetDiagnosticReport.getIssued(),operationOutcome,scenarioName);
		
		//performer
		ComparatorUtils.compareListOfReference(ResourceNames.PERFORMER,sourceDiagnosticReport.getPerformer(),targetDiagnosticReport.getPerformer(),operationOutcome,scenarioName);
		
		//resultsInterpreter
		ComparatorUtils.compareListOfReference(ResourceNames.RESULTS_INTERPRETER,sourceDiagnosticReport.getResultsInterpreter(),targetDiagnosticReport.getResultsInterpreter(),operationOutcome,scenarioName);
		
		//specimen
		ComparatorUtils.compareListOfReference(ResourceNames.SPECIMEN,sourceDiagnosticReport.getSpecimen(),targetDiagnosticReport.getSpecimen(),operationOutcome,scenarioName);
		
		//result
		ComparatorUtils.compareListOfReference(ResourceNames.RESULT,sourceDiagnosticReport.getResult(),targetDiagnosticReport.getResult(),operationOutcome,scenarioName);
		
		//imagingStudy
		ComparatorUtils.compareListOfReference(ResourceNames.IMAGING_STUDY,sourceDiagnosticReport.getImagingStudy(),targetDiagnosticReport.getImagingStudy(),operationOutcome,scenarioName);
		
		//media
		ComparatorUtils.compareListOfDiagnosticReportMediaComponent(ResourceNames.MEDIA,sourceDiagnosticReport.getMedia(),targetDiagnosticReport.getMedia(),operationOutcome,scenarioName);
		
		//conclusion
		ComparatorUtils.compareString(ResourceNames.CONCLUSION,sourceDiagnosticReport.getConclusion(),targetDiagnosticReport.getConclusion(),operationOutcome,scenarioName);
		
		//conclusionCode
		ComparatorUtils.compareListOfCodeableConcept(ResourceNames.CONCLUSION_CODE,sourceDiagnosticReport.getConclusionCode(),targetDiagnosticReport.getConclusionCode(),operationOutcome,scenarioName);
		
		//presentedForm
		ComparatorUtils.compareListOfAttachment(ResourceNames.PRESENTED_FORM,sourceDiagnosticReport.getPresentedForm(),targetDiagnosticReport.getPresentedForm(),operationOutcome,scenarioName);
	}

}
