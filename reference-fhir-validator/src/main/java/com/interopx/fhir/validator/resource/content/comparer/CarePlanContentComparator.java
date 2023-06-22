package com.interopx.fhir.validator.resource.content.comparer;

import org.apache.commons.lang3.ObjectUtils;
import org.hl7.fhir.r4.model.CarePlan;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.interopx.fhir.validator.util.ComparatorUtils;
import com.interopx.fhir.validator.util.ScenarioConstants.ResourceNames;
import com.interopx.fhir.validator.util.ScenarioConstants.ScenarioNameConstants;

public class CarePlanContentComparator {

	
	
public static final Logger logger=LoggerFactory.getLogger(CarePlanContentComparator.class);
	
	public static void compare(Resource sourceResource, Resource targetResource,String scenarioName,OperationOutcome operationOutcome) {
		
		logger.info("Entry - CarePlanContentComparator.compare");
		logger.info("Entry - CarePlanContentComparator.compare - sourceResource ::\n"+sourceResource);
		logger.info("Entry - CarePlanContentComparator.compare - targetResource ::\n"+targetResource);
		
		if (ObjectUtils.isNotEmpty(sourceResource) && ObjectUtils.isNotEmpty(targetResource)) {
			CarePlan sourceCarePlan = (CarePlan) sourceResource;
			CarePlan targetCarePlan = (CarePlan) targetResource;
			logger.info("sourceCarePlan :::::\n"+sourceCarePlan);
			if (scenarioName.equalsIgnoreCase(ScenarioNameConstants.USCDI_FULL_CAREPLAN)) {
				logger.info("sourceCarePlan ::::: compareFullContent\n");
				compareFullContent(sourceCarePlan, targetCarePlan,operationOutcome,scenarioName);
				logger.info("sourceCarePlan ::::: compareFullContent\n");
			}else if (scenarioName.equalsIgnoreCase(ScenarioNameConstants.USCDI_ADDITIONAL_CAREPLAN)) {
				compareAdditionalContent(sourceCarePlan, targetCarePlan,operationOutcome,scenarioName);
			}
		}
		logger.info("Exit - AllergyIntoleranceContentComparator.compare");
	}

	private static void compareAdditionalContent(CarePlan sourceCarePlan, CarePlan targetCarePlan,
			OperationOutcome operationOutcome,String scenarioName) {
		// status
		String source = null;
		String target=null;
		if(sourceCarePlan.getStatus()!=null) {
			source = sourceCarePlan.getStatus().toString();	
		}
		if(targetCarePlan.getStatus()!=null) {
			target = targetCarePlan.getStatus().toString();	
		}
		ComparatorUtils.compareString(ResourceNames.STATUS, source,target, operationOutcome,scenarioName);		
		
		//intent
		String sourceintent = null;
		String targetintent=null;
		if(sourceCarePlan.getIntent()!=null) {
			sourceintent = sourceCarePlan.getIntent().toString();	
		}
		if(targetCarePlan.getIntent()!=null) {
			targetintent = targetCarePlan.getIntent().toString();	
		}
		ComparatorUtils.compareString(ResourceNames.INTENT, sourceintent,targetintent, operationOutcome,scenarioName);		
		
		//category
		ComparatorUtils.compareListOfCodeableConcept(ResourceNames.CATEGORY, sourceCarePlan.getCategory(), targetCarePlan.getCategory(),operationOutcome,scenarioName);
		//subject
		ComparatorUtils.compareReference(ResourceNames.SUBJECT, sourceCarePlan.getSubject(),targetCarePlan.getSubject(), operationOutcome,scenarioName);
		//text
		ComparatorUtils.compareNarrative(ResourceNames.TEXT, sourceCarePlan.getText(),targetCarePlan.getText(), operationOutcome,scenarioName);

	}

	private static void compareFullContent(CarePlan sourceCarePlan, CarePlan targetCarePlan,
			OperationOutcome operationOutcome,String scenarioName) {
		
		//id
        ComparatorUtils.compareString(ResourceNames.ID, sourceCarePlan.getId(), targetCarePlan.getId(), operationOutcome,scenarioName);
        //implicitRules
		ComparatorUtils.compareString(ResourceNames.IMPLICIT_RULES, sourceCarePlan.getImplicitRules(), targetCarePlan.getImplicitRules(), operationOutcome,scenarioName);
		//language
		ComparatorUtils.compareLanguage(ResourceNames.LANGUAGE, sourceCarePlan.getLanguage(), targetCarePlan.getLanguage(), operationOutcome,scenarioName);
		//extension
		ComparatorUtils.compareListOfExtension(ResourceNames.EXTENSION,sourceCarePlan.getExtension(), targetCarePlan.getExtension(), operationOutcome,scenarioName);
		//modifierExtension
		ComparatorUtils.compareListOfExtension(ResourceNames.MODIFIER_EXTENSION, sourceCarePlan.getModifierExtension(), targetCarePlan.getModifierExtension(), operationOutcome,scenarioName);
		//identifier
		ComparatorUtils.compareListOfIdentifier(ResourceNames.IDENTIFIER,sourceCarePlan.getIdentifier(),targetCarePlan.getIdentifier(),operationOutcome,scenarioName);
		// basedOn
		ComparatorUtils.compareListOfReference(ResourceNames.BASED_ON, sourceCarePlan.getBasedOn(),targetCarePlan.getBasedOn(), operationOutcome,scenarioName);
		//text
		ComparatorUtils.compareNarrative(ResourceNames.TEXT, sourceCarePlan.getText(),targetCarePlan.getText(), operationOutcome,scenarioName);
		// partOf
		ComparatorUtils.compareListOfReference(ResourceNames.PART_OF, sourceCarePlan.getPartOf(),targetCarePlan.getPartOf(), operationOutcome,scenarioName);
		// status
		String source = null;
		String target=null;
		if(sourceCarePlan.getStatus()!=null) {
			source = sourceCarePlan.getStatus().toString();	
		}
		if(targetCarePlan.getStatus()!=null) {
			target = targetCarePlan.getStatus().toString();	
		}
		ComparatorUtils.compareString(ResourceNames.STATUS, source,target, operationOutcome,scenarioName);		
		
		//replaces
		ComparatorUtils.compareListOfReference(ResourceNames.REPLACES, sourceCarePlan.getReplaces(),targetCarePlan.getReplaces(), operationOutcome,scenarioName);
		
		//intent
		String sourceintent = null;
		String targetintent=null;
		if(sourceCarePlan.getIntent()!=null) {
			sourceintent = sourceCarePlan.getIntent().toString();	
		}
		if(targetCarePlan.getIntent()!=null) {
			targetintent = targetCarePlan.getIntent().toString();	
		}
		ComparatorUtils.compareString(ResourceNames.INTENT, sourceintent,targetintent, operationOutcome,scenarioName);	
		
		//encounter
		ComparatorUtils.compareReference(ResourceNames.ENCOUNTER, sourceCarePlan.getEncounter(), targetCarePlan.getEncounter(), operationOutcome,scenarioName);
		//subject
		ComparatorUtils.compareReference(ResourceNames.SUBJECT, sourceCarePlan.getSubject(),targetCarePlan.getSubject(), operationOutcome,scenarioName);
		//title
		ComparatorUtils.compareString(ResourceNames.TITLE, sourceCarePlan.getTitle(),targetCarePlan.getTitle(), operationOutcome,scenarioName);
		//description
		ComparatorUtils.compareString(ResourceNames.DESCRIPTION, sourceCarePlan.getDescription(),targetCarePlan.getDescription(), operationOutcome,scenarioName);
		// period
		ComparatorUtils.comparePeriod(ResourceNames.PERIOD, sourceCarePlan.getPeriod(), targetCarePlan.getPeriod(),operationOutcome,scenarioName);
		//created
		ComparatorUtils.compareDate(ResourceNames.CREATED, sourceCarePlan.getCreated(), targetCarePlan.getCreated(),operationOutcome,scenarioName);
		//author
		ComparatorUtils.compareReference(ResourceNames.AUTHOR, sourceCarePlan.getAuthor(), targetCarePlan.getAuthor(),operationOutcome,scenarioName);
		//contributor
		ComparatorUtils.compareListOfReference(ResourceNames.CONTRIBUTOR, sourceCarePlan.getContributor(), targetCarePlan.getContributor(),operationOutcome,scenarioName);
		//careteam
		ComparatorUtils.compareListOfReference(ResourceNames.CARETEAM, sourceCarePlan.getCareTeam(), targetCarePlan.getCareTeam(),operationOutcome,scenarioName);
		//addresses
		ComparatorUtils.compareListOfReference(ResourceNames.ADDRESSES, sourceCarePlan.getAddresses(), targetCarePlan.getAddresses(),operationOutcome,scenarioName);
		//supportingInfo
		ComparatorUtils.compareListOfReference(ResourceNames.SUPPORTING_INFO, sourceCarePlan.getSupportingInfo(), targetCarePlan.getSupportingInfo(),operationOutcome,scenarioName);
		//goal
		ComparatorUtils.compareListOfReference(ResourceNames.GOAL, sourceCarePlan.getGoal(), targetCarePlan.getGoal(),operationOutcome,scenarioName);
		//activity 
		ComparatorUtils.compareListOfCarePlanActivityComponent(ResourceNames.ACTIVITY, sourceCarePlan.getActivity(), targetCarePlan.getActivity(),operationOutcome,scenarioName);
		//category
		ComparatorUtils.compareListOfCodeableConcept(ResourceNames.CATEGORY, sourceCarePlan.getCategory(), targetCarePlan.getCategory(),operationOutcome,scenarioName);

	
	}
}
