package com.interopx.fhir.validator.service;

import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.interopx.fhir.validator.util.ComparatorFactory;
import com.interopx.fhir.validator.util.JSONUtil;
import com.interopx.fhir.validator.util.ScenarioLoader;

import ca.uhn.fhir.context.FhirContext;

/**
 * This service will loads corresponding GoldJson for the scenario , validate
 * source/input resource content with goldJson, then send OperationOutCome
 * 
 * @author Swarna Nelaturi
 *
 */
@Service
public class ResourceContentValidationServiceImpl implements ResourceContentValidationService {

	private static final Logger logger = LoggerFactory.getLogger(ResourceContentValidationServiceImpl.class);

	@Autowired
	@Qualifier("r4FhirContext")
	private FhirContext r4Context;

	@Autowired
	private ScenarioLoader scenarioLoader;

	@Override
	public OperationOutcome validateResourceContent(Resource resource, String scenarioName) {
		logger.info("Entry - ResourceContentValidationServiceImpl.validateResourceContent");
		try {

			String goldJson = scenarioLoader.loadGoldJsonBasedOnScenario(r4Context, resource, scenarioName);
			return compareResourceContent(r4Context, resource, goldJson, scenarioName);

		} catch (Exception e) {
			logger.error("Error - ResourceContentValidationServiceImpl.validateResourceContent", e);
		}
		logger.info("Exit - ResourceContentValidationServiceImpl.validateResourceContent");
		return null;
	}

	private OperationOutcome compareResourceContent(FhirContext r4Context, Resource sourceResource, String goldJson,
			String ScenarioName) {
		logger.info("Entry - ResourceContentValidationServiceImpl.compareResourceContent");
		OperationOutcome operationOutcome = new OperationOutcome();
		logger.info("Gold Resource::::::{}",goldJson);
		Resource targetResource = JSONUtil.stringToFhirResource(r4Context, goldJson);
		try {

			return ComparatorFactory.compareContent(sourceResource.getResourceType().toString(), sourceResource,
					targetResource, ScenarioName);

		} catch (Exception e) {
			logger.info("Error - ResourceContentValidationServiceImpl.compareResourceContent", e);
		}
		logger.info("Exit - ResourceContentValidationServiceImpl.compareResourceContent");
		return operationOutcome;
	}

}
