package com.interopx.fhir.validator.resource.content.comparer;

import org.apache.commons.lang3.ObjectUtils;
import org.hl7.fhir.r4.model.AllergyIntolerance;
import org.hl7.fhir.r4.model.Goal;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.interopx.fhir.validator.util.ComparatorUtils;
import com.interopx.fhir.validator.util.ScenarioConstants.ResourceNames;
import com.interopx.fhir.validator.util.ScenarioConstants.ScenarioNameConstants;

public class GoalContentComparator {
public static final Logger logger=LoggerFactory.getLogger(GoalContentComparator.class);
	
	public static void compare(Resource sourceResource, Resource targetResource,String scenarioName,OperationOutcome operationOutcome) {
		
		logger.info("Entry - GoalContentComparator.compare");
		logger.info("Entry - GoalContentComparator.compare - sourceResource ::\n"+sourceResource);
		logger.info("Entry - GoalContentComparator.compare - targetResource ::\n"+targetResource);
		
		if (ObjectUtils.isNotEmpty(sourceResource) && ObjectUtils.isNotEmpty(targetResource)) {
			Goal sourceGoal = (Goal) sourceResource;
			Goal targetGoal = (Goal) targetResource;
			logger.info("sourceGoal :::::\n"+sourceGoal);
			if (scenarioName.equalsIgnoreCase(ScenarioNameConstants.USCDI_FULL_GOAL)) {
				logger.info("sourceGoal ::::: compareFullContent\n");
				compareFullContent(sourceGoal, targetGoal,operationOutcome,scenarioName);
				logger.info("sourceGoal ::::: compareFullContent\n");
			}else if (scenarioName.equalsIgnoreCase(ScenarioNameConstants.USCDI_ADDITIONAL_GOAL)) {
				compareAdditionalContent(sourceGoal, targetGoal,operationOutcome,scenarioName);
			}
		}
		logger.info("Exit - GoalContentComparator.compare");
	}

	private static void compareAdditionalContent(Goal sourceGoal, Goal targetGoal, OperationOutcome operationOutcome,String scenarioName) {

		//lifecycleStatus
   		ComparatorUtils.compareString(ResourceNames.LIFE_CYCLE_STATUS,sourceGoal.getLifecycleStatus().getDisplay(), targetGoal.getLifecycleStatus().getDisplay(), operationOutcome,scenarioName);
   		//description
   		ComparatorUtils.compareCodeableConcept(ResourceNames.DESCRIPTION, sourceGoal.getDescription(),targetGoal.getDescription(), operationOutcome,scenarioName);
   		//subject
   		ComparatorUtils.compareReference(ResourceNames.SUBJECT, sourceGoal.getSubject(),targetGoal.getSubject(), operationOutcome,scenarioName);
   		//target
   		ComparatorUtils.compareListOfGoalTargetComponent(ResourceNames.TARGET, sourceGoal.getTarget(),targetGoal.getTarget(), operationOutcome,scenarioName);

	}

	private static void compareFullContent(Goal sourceGoal, Goal targetGoal, OperationOutcome operationOutcome,String scenarioName) {
		//Id
        ComparatorUtils.compareString(ResourceNames.ID, sourceGoal.getId(), targetGoal.getId(), operationOutcome,scenarioName);	
        //ImplicitRules
        ComparatorUtils.compareString(ResourceNames.IMPLICIT_RULES, sourceGoal.getImplicitRules(), targetGoal.getImplicitRules(), operationOutcome,scenarioName);
        //Language
      	ComparatorUtils.compareLanguage(ResourceNames.LANGUAGE, sourceGoal.getLanguage(), targetGoal.getLanguage(), operationOutcome,scenarioName);
        //text
     	ComparatorUtils.compareNarrative(ResourceNames.TEXT, sourceGoal.getText(),targetGoal.getText(), operationOutcome,scenarioName);
     	//Contained
     	ComparatorUtils.compareListOfResource(ResourceNames.CONTAINED, sourceGoal.getContained(), targetGoal.getContained(), operationOutcome,scenarioName);
   		//ModifierExtension
   		ComparatorUtils.compareListOfExtension(ResourceNames.MODIFIER_EXTENSION, sourceGoal.getModifierExtension(), targetGoal.getModifierExtension(), operationOutcome,scenarioName);
   		//Identifier
   		ComparatorUtils.compareListOfIdentifier(ResourceNames.IDENTIFIER,sourceGoal.getIdentifier(),targetGoal.getIdentifier(),operationOutcome,scenarioName);
   		//Extension
   		ComparatorUtils.compareListOfExtension(ResourceNames.EXTENSION,sourceGoal.getExtension(), targetGoal.getExtension(), operationOutcome,scenarioName);
   		
   		//lifecycleStatus
   		String source = null;
		String target=null;
		if(sourceGoal.getLifecycleStatus()!=null) {
			source = sourceGoal.getLifecycleStatus().toString();	
		}
		if(targetGoal.getLifecycleStatus()!=null) {
			target = targetGoal.getLifecycleStatus().toString();	
		}
		ComparatorUtils.compareString(ResourceNames.LIFE_CYCLE_STATUS,source,target,operationOutcome,scenarioName);
   		//achievementStatus
   		ComparatorUtils.compareListOfExtension(ResourceNames.ACHIEVEMENT_STATUS,sourceGoal.getExtension(), targetGoal.getExtension(), operationOutcome,scenarioName);
   		//priority
   		ComparatorUtils.compareCodeableConcept(ResourceNames.PRIORITY,sourceGoal.getPriority(), targetGoal.getPriority(), operationOutcome,scenarioName);
   		//category
   		ComparatorUtils.compareListOfCodeableConcept(ResourceNames.CATEGORY, sourceGoal.getCategory(), targetGoal.getCategory(), operationOutcome,scenarioName);
   		//subject
   		ComparatorUtils.compareReference(ResourceNames.SUBJECT, sourceGoal.getSubject(),targetGoal.getSubject(), operationOutcome,scenarioName);
   		//description
   		ComparatorUtils.compareCodeableConcept(ResourceNames.DESCRIPTION, sourceGoal.getDescription(),targetGoal.getDescription(), operationOutcome,scenarioName);
   		//note
   		ComparatorUtils.compareListOfAnnotation(ResourceNames.NOTE, sourceGoal.getNote(), targetGoal.getNote(), operationOutcome,scenarioName);
   		//outcomeCode
   		ComparatorUtils.compareListOfCodeableConcept(ResourceNames.OUTCOME_CODE, sourceGoal.getOutcomeCode(),targetGoal.getOutcomeCode(), operationOutcome,scenarioName);
   		//statusDate
   		ComparatorUtils.compareDate(ResourceNames.STATUS_DATE, sourceGoal.getStatusDate(),targetGoal.getStatusDate(), operationOutcome,scenarioName);
   		//statusReason
   		ComparatorUtils.compareString(ResourceNames.STATUS_REASON, sourceGoal.getStatusReason(),targetGoal.getStatusReason(), operationOutcome,scenarioName);
   		//expressedBy
   		ComparatorUtils.compareReference(ResourceNames.EXPRESSED_BY, sourceGoal.getExpressedBy(),targetGoal.getExpressedBy(), operationOutcome,scenarioName);
   		//addresses
   		ComparatorUtils.compareListOfReference(ResourceNames.ADDRESSES, sourceGoal.getAddresses(),targetGoal.getAddresses(), operationOutcome,scenarioName);
   		//outcomeReference
   		ComparatorUtils.compareListOfReference(ResourceNames.OUTCOME_REFERENCE, sourceGoal.getOutcomeReference(),targetGoal.getOutcomeReference(), operationOutcome,scenarioName);
   		//target
   		ComparatorUtils.compareListOfGoalTargetComponent(ResourceNames.TARGET, sourceGoal.getTarget(),targetGoal.getTarget(), operationOutcome,scenarioName);
	
	}
}
