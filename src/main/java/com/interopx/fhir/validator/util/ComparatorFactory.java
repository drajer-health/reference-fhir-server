package com.interopx.fhir.validator.util;

import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.interopx.fhir.validator.resource.content.comparer.AllergyIntoleranceContentComparator;
import com.interopx.fhir.validator.resource.content.comparer.CarePlanContentComparator;
import com.interopx.fhir.validator.resource.content.comparer.CareTeamContentComparator;
import com.interopx.fhir.validator.resource.content.comparer.ConditionContentComparator;
import com.interopx.fhir.validator.resource.content.comparer.DeviceContentComparator;
import com.interopx.fhir.validator.resource.content.comparer.DiagnosticReportContentComparator;
import com.interopx.fhir.validator.resource.content.comparer.DocumentReferenceContentComparator;
import com.interopx.fhir.validator.resource.content.comparer.EncounterContentComparator;
import com.interopx.fhir.validator.resource.content.comparer.GoalContentComparator;
import com.interopx.fhir.validator.resource.content.comparer.ImmunizationContentComparator;
import com.interopx.fhir.validator.resource.content.comparer.MedicationRequestContentComparator;
import com.interopx.fhir.validator.resource.content.comparer.ObservationContentComparator;
import com.interopx.fhir.validator.resource.content.comparer.PatientContentComparator;
import com.interopx.fhir.validator.resource.content.comparer.ProcedureContentComparator;
import com.interopx.fhir.validator.resource.content.comparer.ProvenanceContentComparator;
import com.interopx.fhir.validator.util.ScenarioConstants.ResourceNames;
/**
 * This Class invokes respective Resource Comparator class to validate source/input resource content with goldJson
 * 
 * @author Swarna Nelaturi
 *
 */
public class ComparatorFactory {
	
	private static final Logger logger = LoggerFactory.getLogger(ComparatorFactory.class);
    public  static OperationOutcome compareContent(String resourceType, Resource sourceResource, Resource targetResource,String scenarioName) throws Exception{
    	logger.info("Entry -ComparatorFactory.compareContent : "+resourceType);
    	OperationOutcome operationOutcome= new OperationOutcome();
    	switch(resourceType) {
	    	case ResourceNames.PATIENT : 
	    		//PatientContentComparator
	    		PatientContentComparator.compare(sourceResource, targetResource,scenarioName,operationOutcome);    		
	    		break;
	    	case ResourceNames.CONDITION : 
	    		//ConditionContentComparator
	    		ConditionContentComparator.compare(sourceResource, targetResource,scenarioName,operationOutcome);    		
	    		break;
	    	case ResourceNames.ENCOUNTER : 
	    		//EncounterContentComparator
	    		EncounterContentComparator.compare(sourceResource, targetResource,scenarioName,operationOutcome);    		
	    		break;
	    	case ResourceNames.ALLERGY_INTOLERANCE : 
	    		//AllergyIntolearnceContentComparator
	    		AllergyIntoleranceContentComparator.compare(sourceResource, targetResource,scenarioName,operationOutcome);    		
	    		break;
	    	case ResourceNames.PROCEDURE : 
	    		//ProcedureContentComparator
	    		ProcedureContentComparator.compare(sourceResource, targetResource,scenarioName,operationOutcome);    		
	    		break;
	    	case ResourceNames.CARETEAM : 
	    		//CareTeamContentComparator
	    		CareTeamContentComparator.compare(sourceResource, targetResource,scenarioName,operationOutcome);    		
	    		break;
	    	case ResourceNames.CAREPLAN : 
	    		//CarePlanContentComparator
	    		CarePlanContentComparator.compare(sourceResource, targetResource,scenarioName,operationOutcome); 
	    		break;  		
	    	case ResourceNames.IMMUNIZATION : 
	    		//ImmunizationContentComparator
	    		ImmunizationContentComparator.compare(sourceResource, targetResource,scenarioName,operationOutcome);    		
	    		break;
	    	case ResourceNames.DIAGNOSTICREPORT : 
	    		//DiagnosticReportContentComparator
	    		DiagnosticReportContentComparator.compare(sourceResource, targetResource,scenarioName,operationOutcome);    		
	    		break;
	    	case ResourceNames.GOAL : 
	    		//GoalContentComparator
	    		GoalContentComparator.compare(sourceResource, targetResource,scenarioName,operationOutcome);    		
	    		break;
	    	case ResourceNames.MEDICATIONREQUEST : 
	    		//MedicationRequestContentComparator
	    		MedicationRequestContentComparator.compare(sourceResource, targetResource,scenarioName,operationOutcome);
	    		break;
	    	case ResourceNames.DEVICE : 
	    		//DeviceContentComparator
	    		DeviceContentComparator.compare(sourceResource, targetResource,scenarioName,operationOutcome);    		
	    		break;
	    	case ResourceNames.DOCUMENTREFERENCE: 
	    		//DocumentReferenceContentComparator
	    		DocumentReferenceContentComparator.compare(sourceResource, targetResource,scenarioName,operationOutcome);    		
	    		break;
	    	case ResourceNames.PROVENANCE : 
	    		//ProvenanceContentComparator
	    		ProvenanceContentComparator.compare(sourceResource, targetResource,scenarioName,operationOutcome);    		
	    		break;
	    	case ResourceNames.OBSERVATION : 
	    		//ObservationContentComparator
	    		ObservationContentComparator.compare(sourceResource, targetResource,scenarioName,operationOutcome);    		
	    		break;
	    	default : 
	    		logger.info("No Resource Found");
	    		throw new Exception("No Comparator Found For Resource : "+resourceType);	    		
    	}
    	logger.info("Exit -ComparatorFactory.compareContent : "+resourceType);
		return operationOutcome;       
    }
    
}
