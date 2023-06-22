package com.interopx.fhir.validator.service;

import java.util.Map;

import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r5.model.CodeableConcept;
import org.hl7.fhir.r5.model.StringType;
import org.hl7.fhir.r5.utils.ToolingExtensions;
import org.hl7.fhir.r5.model.OperationOutcome;
import org.hl7.fhir.r5.model.OperationOutcome.OperationOutcomeIssueComponent;
import org.hl7.fhir.utilities.validation.ValidationMessage;
import org.hl7.fhir.utilities.validation.ValidationMessage.IssueSeverity;
import org.hl7.fhir.utilities.validation.ValidationMessage.IssueType;
import org.hl7.fhir.utilities.validation.ValidationMessage.Source;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.interopx.fhir.validator.component.Validator;
import com.interopx.fhir.validator.util.JSONUtil;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.StrictErrorHandler;
import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.fhir.validation.ValidationResult;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory; 

@Service
public class ResourceValidationServiceImpl implements ResourceValidationService {

	private static final Logger logger = LoggerFactory.getLogger(ResourceValidationServiceImpl.class);
	
	@Autowired
	private Validator validator;
	/**
	 * Validates DSTU2 resource
	 * 
	 * @param fhirContext
	 * @param val
	 * @param bodyStr
	 * @return
	 */
	@Override
	public ValidationResult validateDSTU2Resource(FhirContext fhirContext, FhirValidator val, String bodyStr) {
		IBaseResource resource = null;
			// Parse that JSON string encoding)
			resource = fhirContext.newJsonParser().setParserErrorHandler(new StrictErrorHandler())
					.parseResource(bodyStr);
			ValidationResult result = val.validateWithResult(resource);
		return result;
	}

	/**
	 * Validates STU3 resource
	 * 
	 * @param fhirContext
	 * @param val
	 * @param bodyStr
	 * @return
	 */
	@Override
	public ValidationResult validateSTU3Resource(FhirContext fhirContext, FhirValidator val, String bodyStr) {
		IBaseResource resource = null;
		// Parse that JSON string encoding)
		resource = fhirContext.newJsonParser().setParserErrorHandler(new StrictErrorHandler()).parseResource(bodyStr);
		ValidationResult result = val.validateWithResult(resource);
		return result;
	}

	/**
	 * Validates r4 resources
	 * 
	 * @param fhirContext
	 * @param val
	 * @param bodyStr
	 * @return
	 */
	public ValidationResult validateR4Resource(FhirContext fhirContext, FhirValidator val, String bodyStr) {
		IBaseResource resource = null;
		resource = fhirContext.newJsonParser().setParserErrorHandler(new StrictErrorHandler()).parseResource(bodyStr);
		ValidationResult result = val.validateWithResult(resource);
		return result;
	}

	/**
	 * Validates r4 resources based on us-core profile
	 */
	@Override
	public OperationOutcome validate(byte[] resource, String profile) throws Exception{
		return validator.validate(resource, profile);
	}
	
	public OperationOutcome validateID(String resource,String profile,int resourceIndex) throws Exception{
		OperationOutcome operationOutcome= new OperationOutcome();
		String fieldName="Id";
		JSONParser parser = new JSONParser();
		JSONObject json = (JSONObject) parser.parse(resource);
		Map resourceMap =JSONUtil.jsonToMap(resource);
		String res= (String)resourceMap.get("id");
		if(!(res.matches("[a-zA-Z0-9.-]+"))){
		String errorMessage ="Note that strings SHALL NOT exceed 1MB (1024*1024 characters) in size. Strings SHOULD not contain Unicode character points below 32, except for u0009 (horizontal tab), u0010 (carriage return) and u0013 (line feed). Leading and Trailing whitespace is allowed, but SHOULD be removed when using the XML format. ";
		org.hl7.fhir.r5.model.OperationOutcome.OperationOutcomeIssueComponent issues = createScenarioResults(IssueType.INVALID, fieldName,errorMessage,resourceIndex);
		operationOutcome.addIssue(issues);
			}
		
		return operationOutcome;
		
	}

			public static OperationOutcomeIssueComponent createScenarioResults(IssueType type, String path, String message,int resourceIndex) {
		try {

			OperationOutcome operationOutcome = new OperationOutcome();
			ValidationMessage validationMessage = new ValidationMessage(Source.ProfileValidator, type, path, message,
					IssueSeverity.ERROR);
			OperationOutcomeIssueComponent operationOutcomeIssueComponent = convertToIssue(validationMessage,
					operationOutcome,resourceIndex);
			return operationOutcomeIssueComponent;
		} catch (Exception ex) {
			logger.error("\n Exception in createScenarioResults of ComparatorUtils :: ", ex.getMessage());
		}
		return null;

	}
	
	private static OperationOutcomeIssueComponent convertToIssue(ValidationMessage message, OperationOutcome op,int resourceIndex) {
		try {
			OperationOutcomeIssueComponent issue = new OperationOutcome.OperationOutcomeIssueComponent();
			issue.addExpression("Bundle.entry["+resourceIndex+"].resource.id");
			issue.setCode(convert(message.getType()));
			issue.setSeverity(convert(message.getLevel()));
			if (message.getLocation() != null) {
				StringType s = new StringType();
				s.setValue(message.getLocation() + (message.getLine() >= 0 && message.getCol() >= 0 ? " (line "
						+ Integer.toString(message.getLine()) + ", col" + Integer.toString(message.getCol()) + ")"
						: ""));
				issue.getLocation();
			}
			CodeableConcept c = new CodeableConcept();
			c.setText(message.getMessage());
			issue.setDetails(c);
			if (message.getSource() != null) {
				issue.getExtension().add(ToolingExtensions.makeIssueSource(message.getSource()));
			}
			return issue;
		} catch (Exception ex) {
			logger.error("\n Exception in convertToIssue of ComparatorUtils ::"+ex);
		}
		return null;

	}
	
	private static org.hl7.fhir.r5.model.OperationOutcome.IssueType convert(IssueType type) {
		return OperationOutcome.IssueType.valueOf(type.name());
	}

	private static OperationOutcome.IssueSeverity convert(IssueSeverity issueSeverity) {
		return OperationOutcome.IssueSeverity.valueOf(issueSeverity.name());

	}
}
