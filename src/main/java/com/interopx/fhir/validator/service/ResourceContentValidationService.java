package com.interopx.fhir.validator.service;


import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Resource;
/**
 * It will validate source/input resource content with goldJson
 * 
 * @author Swarna Nelaturi
 *
 */
public interface ResourceContentValidationService {
	public OperationOutcome validateResourceContent(Resource sourceResources,String scenario);
	
}
