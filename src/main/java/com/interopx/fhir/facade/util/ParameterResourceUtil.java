package com.interopx.fhir.facade.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.InstantType;
import org.hl7.fhir.r4.model.IntegerType;
import org.hl7.fhir.r4.model.Location;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Parameters.ParametersParameterComponent;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.StringType;

import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.rest.api.server.RequestDetails;

public class ParameterResourceUtil {
	public static final String ENDPOINT_URL = "endpointUrl";
	private static final String ENCOUNTER = "encounter";

	public static Parameters generateParameterResource(SearchParameterMap paramMap, Map<String, String> pathParams, RequestDetails theRequestDetails) {

		Parameters retval = new Parameters();
		retval.setId(CommonUtil.generateId().toString());
		List<ParametersParameterComponent> parameters = new ArrayList<>();
		ParametersParameterComponent parameter = new ParametersParameterComponent();

		parameter.setName("resourceType");
		StringType resourceType = new StringType();
		resourceType.setValue(pathParams.get("resourceType"));
		parameter.setValue(resourceType);
		parameters.add(parameter);
		retval.setParameter(parameters);

		parameter = new ParametersParameterComponent();
		parameter.setName("requestId");
		StringType requestIdType = new StringType();
		requestIdType.setValue(pathParams.get("requestId"));
		parameter.setValue(requestIdType);
		parameters.add(parameter);
		retval.setParameter(parameters);

		parameter = new ParametersParameterComponent();
		parameter.setName(ENDPOINT_URL);
		StringType endpointUrlType = new StringType();
		endpointUrlType.setValue(pathParams.get("endPointUrl"));
		parameter.setValue(endpointUrlType);
		parameters.add(parameter);
		retval.setParameter(parameters);

		parameter = new ParametersParameterComponent();
		parameter.setName("practiceId");
		StringType practiceIdType = new StringType();
		practiceIdType.setValue(pathParams.get("practiceId"));
		parameter.setValue(practiceIdType);
		parameters.add(parameter);
		retval.setParameter(parameters);

		if (paramMap.get(Parameter.SP_PATIENT.toString()) != null) {
			parameter = new ParametersParameterComponent();
			parameter.setName(Parameter.SP_PATIENT.toString());
			Reference reference = new Reference();
			reference.setReference(ResourceTypes.PATIENT.toString() + "/"
					+ CommonUtil.getIdFromReferenceSearchParam(paramMap, Parameter.SP_PATIENT.toString()));
			parameter.setValue(reference);
			parameters.add(parameter);
			retval.setParameter(parameters);
		}
		if (paramMap.get(ENCOUNTER) != null) {
			parameter = new ParametersParameterComponent();
			parameter.setName(ENCOUNTER);
			Reference reference = new Reference();
			reference.setReference(ResourceTypes.ENCOUNTER.toString() + "/"
					+ CommonUtil.getIdFromReferenceSearchParam(paramMap, "encounter"));
			parameter.setValue(reference);
			parameters.add(parameter);
			retval.setParameter(parameters);
		}
		if (paramMap.get(Parameter.SP_CONTEXT.toString()) != null) {
			parameter = new ParametersParameterComponent();
			parameter.setName(Parameter.SP_CONTEXT.toString());
			Reference reference = new Reference();
			reference.setReference(ResourceTypes.ENCOUNTER.toString() + "/"
					+ CommonUtil.getIdFromReferenceSearchParam(paramMap, Parameter.SP_CONTEXT.toString()));
			parameter.setValue(reference);
			parameters.add(parameter);
			retval.setParameter(parameters);
		}

		if (paramMap.get(Parameter.SP_STATUS.toString()) != null) {
			List<String> statusList = CommonUtil.getListOfTokenParamSearchParameterValue(paramMap,
					Parameter.SP_STATUS.toString());
			for (String status : statusList) {
				parameter = new ParametersParameterComponent();
				parameter.setName(Parameter.SP_STATUS.toString());
				StringType stringType = new StringType();
				stringType.setValue(status);
				parameter.setValue(stringType);
				parameters.add(parameter);
				retval.setParameter(parameters);
			}
		}

		if (paramMap.get(Parameter.SP_DATE.toString()) != null) {
			List<String> dateString = CommonUtil.getDateParamsFromQuery(paramMap, Parameter.SP_DATE.toString());
			for (String date : dateString) {
				parameter = new ParametersParameterComponent();
				StringType stringType = new StringType();
				parameter.setName(Parameter.SP_DATE.toString());
				stringType.setValue(date);
				parameter.setValue(stringType);
				parameters.add(parameter);
				retval.setParameter(parameters);
			}
		}
		if (paramMap.get(Parameter.SP_CODE.toString()) != null) {
			Map<String, String> tokenValueMap = CommonUtil.getTokenParamSearchParameterValue(paramMap,
					Parameter.SP_CODE.toString());
			CodeableConcept codeableConcept = new CodeableConcept();
			List<Coding> codings = new ArrayList<>();
			parameter = new ParametersParameterComponent();
			parameter.setName(Parameter.SP_CODE.toString());

			for (Map.Entry<String, String> map : tokenValueMap.entrySet()) {
				Coding coding = new Coding();
				if (map.getKey().equalsIgnoreCase(map.getValue())) {
					coding.setCode(map.getKey());
					codings.add(coding);
					codeableConcept.setCoding(codings);
				} else {
					coding.setSystem(map.getValue());
					coding.setCode(map.getKey());
					codings.add(coding);
					codeableConcept.setCoding(codings);
				}

			}
			parameter.setValue(codeableConcept);
			parameters.add(parameter);
			retval.setParameter(parameters);

		}

		if (paramMap.get(Parameter.SP_CATEGORY.toString()) != null) {
			Map<String, String> tokenValueMap = CommonUtil.getTokenParamSearchParameterValue(paramMap,
					Parameter.SP_CATEGORY.toString());
			CodeableConcept codeableConcept = new CodeableConcept();
			List<Coding> codings = new ArrayList<>();
			parameter = new ParametersParameterComponent();
			parameter.setName(Parameter.SP_CATEGORY.toString());

			for (Map.Entry<String, String> map : tokenValueMap.entrySet()) {
				Coding coding = new Coding();
				if (map.getKey().equalsIgnoreCase(map.getValue())) {
					coding.setCode(map.getKey());
					codings.add(coding);
					codeableConcept.setCoding(codings);
				} else {
					coding.setSystem(map.getValue());
					coding.setCode(map.getKey());
					codings.add(coding);
					codeableConcept.setCoding(codings);
				}

			}
			parameter.setValue(codeableConcept);
			parameters.add(parameter);
			retval.setParameter(parameters);
		}

		if (paramMap.get(Parameter.SP_CLINICAL_STATUS.toString()) != null) {
			Map<String, String> tokenValueMap = CommonUtil.getTokenParamSearchParameterValue(paramMap,
					Parameter.SP_CLINICAL_STATUS.toString());
			CodeableConcept codeableConcept = new CodeableConcept();
			List<Coding> codings = new ArrayList<>();
			parameter = new ParametersParameterComponent();
			parameter.setName(Parameter.SP_CLINICAL_STATUS.toString());

			for (Map.Entry<String, String> map : tokenValueMap.entrySet()) {
				Coding coding = new Coding();
				if (map.getKey().equalsIgnoreCase(map.getValue())) {
					coding.setCode(map.getKey());
					codings.add(coding);
					codeableConcept.setCoding(codings);
				} else {
					coding.setSystem(map.getValue());
					coding.setCode(map.getKey());
					codings.add(coding);
					codeableConcept.setCoding(codings);
				}

			}
			parameter.setValue(codeableConcept);
			parameters.add(parameter);
			retval.setParameter(parameters);
		}
		if (paramMap.get(Parameter.SP_ONSET_DATE.toString()) != null) {
			List<String> dateString = CommonUtil.getDateParamsFromQuery(paramMap, Parameter.SP_ONSET_DATE.toString());
			for (String date : dateString) {
				parameter = new ParametersParameterComponent();
				StringType stringType = new StringType();
				parameter.setName(Parameter.SP_ONSET_DATE.toString());
				stringType.setValue(date);
				parameter.setValue(stringType);
				parameters.add(parameter);
				retval.setParameter(parameters);
			}
		}
		if (paramMap.get(Parameter.SP_PERIOD.toString()) != null) {
			List<String> dateString = CommonUtil.getDateParamsFromQuery(paramMap, Parameter.SP_PERIOD.toString());
			for (String date : dateString) {
				parameter = new ParametersParameterComponent();
				StringType stringType = new StringType();
				parameter.setName(Parameter.SP_PERIOD.toString());
				stringType.setValue(date);
				parameter.setValue(stringType);
				parameters.add(parameter);
				retval.setParameter(parameters);
			}
		}
		if (paramMap.get(Parameter.SP_TYPE.toString()) != null) {
			Map<String, String> tokenValueMap = CommonUtil.getTokenParamSearchParameterValue(paramMap,
					Parameter.SP_TYPE.toString());
			CodeableConcept codeableConcept = new CodeableConcept();
			List<Coding> codings = new ArrayList<>();
			parameter = new ParametersParameterComponent();
			parameter.setName(Parameter.SP_TYPE.toString());

			for (Map.Entry<String, String> map : tokenValueMap.entrySet()) {
				Coding coding = new Coding();
				if (map.getKey().equalsIgnoreCase(map.getValue())) {
					coding.setCode(map.getKey());
					codings.add(coding);
					codeableConcept.setCoding(codings);
				} else {
					coding.setSystem(map.getValue());
					coding.setCode(map.getKey());
					codings.add(coding);
					codeableConcept.setCoding(codings);
				}

			}
			parameter.setValue(codeableConcept);
			parameters.add(parameter);
			retval.setParameter(parameters);
		}
		if (paramMap.get(Parameter.SP_CLASS.toString()) != null) {
			Map<String, String> tokenValueMap = CommonUtil.getTokenParamSearchParameterValue(paramMap,
					Parameter.SP_CLASS.toString());

			for (Map.Entry<String, String> map : tokenValueMap.entrySet()) {
				Coding coding = new Coding();
				parameter = new ParametersParameterComponent();
				parameter.setName(Parameter.SP_CLASS.toString());
				if (map.getKey().equalsIgnoreCase(map.getValue())) {
					coding.setCode(map.getKey());
				} else {
					coding.setSystem(map.getValue());
					coding.setCode(map.getKey());

				}
				parameter.setValue(coding);
				parameters.add(parameter);

			}

			retval.setParameter(parameters);
		}

		if (paramMap.get(Parameter.SP_IDENTIFIER.toString()) != null) {
			Map<String, String> identifierValue = CommonUtil.getIdentifierSearchParameterValue(paramMap);
			for (Map.Entry<String, String> map : identifierValue.entrySet()) {
				parameter = new ParametersParameterComponent();
				parameter.setName(Parameter.SP_IDENTIFIER.toString());
				Identifier identifier = new Identifier();
				if (map.getKey().equalsIgnoreCase(map.getValue())) {
					identifier.setValue(map.getKey());
					parameter.setValue(identifier);
					parameters.add(parameter);

				} else {
					identifier.setSystem(map.getValue());
					identifier.setValue(map.getKey());
					parameter.setValue(identifier);
					parameters.add(parameter);

				}
			}
			retval.setParameter(parameters);
		}

		if (paramMap.get(Parameter.SP_LIFECYCLE_STATUS.toString()) != null) {
			List<String> statusList = CommonUtil.getListOfTokenParamSearchParameterValue(paramMap,
					Parameter.SP_LIFECYCLE_STATUS.toString());
			for (String status : statusList) {
				parameter = new ParametersParameterComponent();
				parameter.setName(Parameter.SP_LIFECYCLE_STATUS.toString());
				StringType stringType = new StringType();
				stringType.setValue(status);
				parameter.setValue(stringType);
				parameters.add(parameter);
				retval.setParameter(parameters);
			}
		}
		if (paramMap.get(Parameter.SP_TARGET_DATE.toString()) != null) {
			List<String> dateString = CommonUtil.getDateParamsFromQuery(paramMap, Parameter.SP_TARGET_DATE.toString());
			for (String date : dateString) {
				parameter = new ParametersParameterComponent();
				StringType stringType = new StringType();
				parameter.setName(Parameter.SP_TARGET_DATE.toString());
				stringType.setValue(date);
				parameter.setValue(stringType);
				parameters.add(parameter);
				retval.setParameter(parameters);
			}
		}
		if (paramMap.get(Parameter.SP_NAME.toString()) != null) {
			List<String> nameList = CommonUtil.getListOfStringParamSearchParameterValue(paramMap,
					Parameter.SP_NAME.toString());
			for (String name : nameList) {
				parameter = new ParametersParameterComponent();
				parameter.setName(Parameter.SP_NAME.toString());
				StringType stringType = new StringType();
				stringType.setValue(name);
				parameter.setValue(stringType);
				parameters.add(parameter);
				retval.setParameter(parameters);
			}
		}
		if (paramMap.get(Parameter.SP_ADDRESS.toString()) != null) {
			List<String> nameList = CommonUtil.getListOfStringParamSearchParameterValue(paramMap,
					Parameter.SP_ADDRESS.toString());
			for (String name : nameList) {
				parameter = new ParametersParameterComponent();
				parameter.setName(Parameter.SP_ADDRESS.toString());
				StringType stringType = new StringType();
				stringType.setValue(name);
				parameter.setValue(stringType);
				parameters.add(parameter);
				retval.setParameter(parameters);
			}
		}

		if (paramMap.get(Parameter.SP_ADDRESS_CITY.toString()) != null) {
			List<String> nameList = CommonUtil.getListOfStringParamSearchParameterValue(paramMap,
					Location.SP_ADDRESS_CITY);
			for (String name : nameList) {
				parameter = new ParametersParameterComponent();
				parameter.setName(Location.SP_ADDRESS_CITY);
				StringType stringType = new StringType();
				stringType.setValue(name);
				parameter.setValue(stringType);
				parameters.add(parameter);
				retval.setParameter(parameters);
			}
		}

		if (paramMap.get("_id") != null) {
			String id = CommonUtil.getIdFromSearchById(paramMap, "_id");
			parameter = new ParametersParameterComponent();
			parameter.setName("_id");
			StringType stringType = new StringType();
			stringType.setValue(id);
			parameter.setValue(stringType);
			parameters.add(parameter);
			retval.setParameter(parameters);
		}

		if (paramMap.get(Parameter.SP_ADDRESS_STATE.toString()) != null) {
			List<String> nameList = CommonUtil.getListOfStringParamSearchParameterValue(paramMap,
					Parameter.SP_ADDRESS_STATE.toString());
			for (String name : nameList) {
				parameter = new ParametersParameterComponent();
				parameter.setName(Parameter.SP_ADDRESS_STATE.toString());
				StringType stringType = new StringType();
				stringType.setValue(name);
				parameter.setValue(stringType);
				parameters.add(parameter);
				retval.setParameter(parameters);
			}
		}
		if (paramMap.get(Parameter.SP_ADDRESS_POSTALCODE.toString()) != null) {
			List<String> nameList = CommonUtil.getListOfStringParamSearchParameterValue(paramMap,
					Parameter.SP_ADDRESS_POSTALCODE.toString());
			for (String name : nameList) {
				parameter = new ParametersParameterComponent();
				parameter.setName(Parameter.SP_ADDRESS_POSTALCODE.toString());
				StringType stringType = new StringType();
				stringType.setValue(name);
				parameter.setValue(stringType);
				parameters.add(parameter);
				retval.setParameter(parameters);
			}
		}
		if (paramMap.get(Parameter.SP_INTENT.toString()) != null) {
			List<String> statusList = CommonUtil.getListOfTokenParamSearchParameterValue(paramMap,
					Parameter.SP_INTENT.toString());
			for (String status : statusList) {
				parameter = new ParametersParameterComponent();
				parameter.setName(Parameter.SP_INTENT.toString());
				StringType stringType = new StringType();
				stringType.setValue(status);
				parameter.setValue(stringType);
				parameters.add(parameter);
				retval.setParameter(parameters);
			}
		}
		if (paramMap.get(Parameter.SP_AUTHOREDON.toString()) != null) {
			List<String> dateString = CommonUtil.getDateParamsFromQuery(paramMap, Parameter.SP_AUTHOREDON.toString());
			for (String date : dateString) {
				parameter = new ParametersParameterComponent();
				StringType stringType = new StringType();
				parameter.setName(Parameter.SP_AUTHOREDON.toString());
				stringType.setValue(date);
				parameter.setValue(stringType);
				parameters.add(parameter);
				retval.setParameter(parameters);
			}
		}

		if (paramMap.get(Parameter.SP_FAMILY.toString()) != null) {
			List<String> nameList = CommonUtil.getListOfStringParamSearchParameterValue(paramMap,
					Parameter.SP_FAMILY.toString());
			for (String name : nameList) {
				parameter = new ParametersParameterComponent();
				parameter.setName(Parameter.SP_FAMILY.toString().toString());
				StringType stringType = new StringType();
				stringType.setValue(name);
				parameter.setValue(stringType);
				parameters.add(parameter);
				retval.setParameter(parameters);
			}
		}
		if (paramMap.get(Parameter.SP_BIRTHDATE.toString()) != null) {
			List<String> dateString = CommonUtil.getDateParamsFromQuery(paramMap, Parameter.SP_BIRTHDATE.toString());
			for (String date : dateString) {
				parameter = new ParametersParameterComponent();
				StringType stringType = new StringType();
				parameter.setName(Parameter.SP_BIRTHDATE.toString());
				stringType.setValue(date);
				parameter.setValue(stringType);
				parameters.add(parameter);
				retval.setParameter(parameters);
			}
		}
		if (paramMap.get(Parameter.SP_GENDER.toString()) != null) {
			List<String> statusList = CommonUtil.getListOfTokenParamSearchParameterValue(paramMap,
					Parameter.SP_GENDER.toString());
			for (String status : statusList) {
				parameter = new ParametersParameterComponent();
				parameter.setName(Parameter.SP_GENDER.toString());
				StringType stringType = new StringType();
				stringType.setValue(status);
				parameter.setValue(stringType);
				parameters.add(parameter);
				retval.setParameter(parameters);
			}
		}
		if (paramMap.get(Parameter.SP_SPECIALTY.toString()) != null) {
			Map<String, String> tokenValueMap = CommonUtil.getTokenParamSearchParameterValue(paramMap,
					Parameter.SP_SPECIALTY.toString());
			CodeableConcept codeableConcept = new CodeableConcept();
			List<Coding> codings = new ArrayList<>();
			parameter = new ParametersParameterComponent();
			parameter.setName(Parameter.SP_SPECIALTY.toString());

			for (Map.Entry<String, String> map : tokenValueMap.entrySet()) {
				Coding coding = new Coding();
				if (map.getKey().equalsIgnoreCase(map.getValue())) {
					coding.setCode(map.getKey());
					codings.add(coding);
					codeableConcept.setCoding(codings);
				} else {
					coding.setSystem(map.getValue());
					coding.setCode(map.getKey());
					codings.add(coding);
					codeableConcept.setCoding(codings);
				}

			}
			parameter.setValue(codeableConcept);
			parameters.add(parameter);
			retval.setParameter(parameters);
		}
		if (paramMap.get(AppConstants.PRACTITIONER_NAME) != null
				&& !paramMap.get(AppConstants.PRACTITIONER_NAME).isEmpty()) {
			List<String> nameList = CommonUtil.getListOfStringParamSearchParameterValue(paramMap,
					AppConstants.PRACTITIONER_NAME);
			if (ObjectUtils.isNotEmpty(nameList)) {
				for (String name : nameList) {
					parameter = new ParametersParameterComponent();
					parameter.setName(AppConstants.PRACTITIONER_NAME);
					StringType stringType = new StringType();
					stringType.setValue(name);
					parameter.setValue(stringType);
					parameters.add(parameter);
					retval.setParameter(parameters);
				}
			}
		}
		if (paramMap.get(AppConstants.PRACTITIONER_IDENTIFIER) != null
				&& !paramMap.get(AppConstants.PRACTITIONER_IDENTIFIER).isEmpty()) {
			Map<String, String> identifierValue = CommonUtil.getIdentifierSearchParameterValue(paramMap,
					AppConstants.PRACTITIONER_IDENTIFIER);
			for (Map.Entry<String, String> map : identifierValue.entrySet()) {
				parameter = new ParametersParameterComponent();
				parameter.setName(AppConstants.PRACTITIONER_IDENTIFIER);
				Identifier identifier = new Identifier();
				if (map.getKey().equalsIgnoreCase(map.getValue())) {
					identifier.setValue(map.getKey());
					parameter.setValue(identifier);
					parameters.add(parameter);

				} else {
					identifier.setSystem(map.getValue());
					identifier.setValue(map.getKey());
					parameter.setValue(identifier);
					parameters.add(parameter);

				}
			}
		}
		if (paramMap.getIncludes().contains(new Include(AppConstants.PRACTITIONERROLE_PRACTITIONER))) {
			parameter = new ParametersParameterComponent();
			parameter.setName(AppConstants.UNDERSCORE_INCLUDE);
			StringType includeType = new StringType();
			includeType.setValue(AppConstants.PRACTITIONERROLE_PRACTITIONER);
			parameter.setValue(includeType);
			parameters.add(parameter);
			retval.setParameter(parameters);
		}
		if (paramMap.getIncludes().contains(new Include(AppConstants.PRACTITIONERROLE_ORGANIZATION))) {
			parameter = new ParametersParameterComponent();
			parameter.setName(AppConstants.UNDERSCORE_INCLUDE);
			StringType includeType = new StringType();
			includeType.setValue(AppConstants.PRACTITIONERROLE_ORGANIZATION);
			parameter.setValue(includeType);
			parameters.add(parameter);
			retval.setParameter(parameters);
		}
		if (paramMap.getIncludes().contains(new Include(AppConstants.MEDICATIONREQUEST_MEDICATION))) {
			parameter = new ParametersParameterComponent();
			parameter.setName(AppConstants.UNDERSCORE_INCLUDE);
			StringType includeType = new StringType();
			includeType.setValue(AppConstants.MEDICATIONREQUEST_MEDICATION);
			parameter.setValue(includeType);
			parameters.add(parameter);
			retval.setParameter(parameters);
		}
		if (paramMap.getRevIncludes().contains(new Include(AppConstants.PROVENANCE_TARGET))) {
			parameter = new ParametersParameterComponent();
			parameter.setName(AppConstants.UNDERSCORE_REVINCLUDE);
			StringType includeType = new StringType();
			includeType.setValue(AppConstants.PROVENANCE_TARGET);
			parameter.setValue(includeType);
			parameters.add(parameter);
			retval.setParameter(parameters);
		}

		if (paramMap.getIncludes().contains(new Include(AppConstants.OBSERVATION_PERFORMER))) {
			parameter = new ParametersParameterComponent();
			parameter.setName(AppConstants.UNDERSCORE_INCLUDE);
			StringType includeType = new StringType();
			includeType.setValue(AppConstants.OBSERVATION_PERFORMER);
			parameter.setValue(includeType);
			parameters.add(parameter);
			retval.setParameter(parameters);
		}

		if (paramMap.getIncludes().contains(new Include(AppConstants.DIAGNOSTICREPORT_PERFORMER))) {
			parameter = new ParametersParameterComponent();
			parameter.setName(AppConstants.UNDERSCORE_INCLUDE);
			StringType includeType = new StringType();
			includeType.setValue(AppConstants.DIAGNOSTICREPORT_PERFORMER);
			parameter.setValue(includeType);
			parameters.add(parameter);
			retval.setParameter(parameters);
		}

		if (paramMap.getIncludes().contains(new Include(AppConstants.DOCUMENTREFERENCE_AUTHOR))) {
			parameter = new ParametersParameterComponent();
			parameter.setName(AppConstants.UNDERSCORE_INCLUDE);
			StringType includeType = new StringType();
			includeType.setValue(AppConstants.DOCUMENTREFERENCE_AUTHOR);
			parameter.setValue(includeType);
			parameters.add(parameter);
			retval.setParameter(parameters);
		}

		if (paramMap.getIncludes().contains(new Include(AppConstants.LOCATION_ORGANIZATION))) {
			parameter = new ParametersParameterComponent();
			parameter.setName(AppConstants.UNDERSCORE_INCLUDE);
			StringType includeType = new StringType();
			includeType.setValue(AppConstants.LOCATION_ORGANIZATION);
			parameter.setValue(includeType);
			parameters.add(parameter);
			retval.setParameter(parameters);
		}
		if (paramMap.getIncludes().contains(new Include(AppConstants.CARETEAM_PARTICIPANT))) {
			parameter = new ParametersParameterComponent();
			parameter.setName(AppConstants.UNDERSCORE_INCLUDE);
			StringType includeType = new StringType();
			includeType.setValue(AppConstants.CARETEAM_PARTICIPANT);
			parameter.setValue(includeType);
			parameters.add(parameter);
			retval.setParameter(parameters);
		}
		if (paramMap.getIncludes().contains(new Include(AppConstants.DOCUMENTREFERENCE_CUSTODIAN))) {
			parameter = new ParametersParameterComponent();
			parameter.setName(AppConstants.UNDERSCORE_INCLUDE);
			StringType includeType = new StringType();
			includeType.setValue(AppConstants.DOCUMENTREFERENCE_CUSTODIAN);
			parameter.setValue(includeType);
			parameters.add(parameter);
			retval.setParameter(parameters);
		}
		if(theRequestDetails != null) {
			Map<String, String[]> tokenParam = theRequestDetails.getParameters();
			//set patientGuid
			if(tokenParam.containsKey("patientGuid") && tokenParam.get("patientGuid") != null) {
				parameter = new ParametersParameterComponent();
				parameter.setName("patientGuid");
				StringType patientGuid = new StringType();
				patientGuid.setValue(tokenParam.get("patientGuid")[0]);
				parameter.setValue(patientGuid);
				parameters.add(parameter);
				retval.setParameter(parameters);
			}
			
			//set providerGuid
			if(tokenParam.containsKey("providerGuid") && tokenParam.get("providerGuid") != null) {
				parameter = new ParametersParameterComponent();
				parameter.setName("providerGuid");
				StringType providerGuid = new StringType();
				providerGuid.setValue(tokenParam.get("providerGuid")[0]);
				parameter.setValue(providerGuid);
				parameters.add(parameter);
				retval.setParameter(parameters);
			}
			
			//set vendorId
			if(tokenParam.containsKey("vendorId") && tokenParam.get("vendorId") != null) {
				parameter = new ParametersParameterComponent();
				parameter.setName("vendorId");
				StringType vendorId = new StringType();
				vendorId.setValue(tokenParam.get("vendorId")[0]);
				parameter.setValue(vendorId);
				parameters.add(parameter);
				retval.setParameter(parameters);
			}
			//set workflow
			if(tokenParam.containsKey("workflow") && tokenParam.get("workflow") != null) {
				parameter = new ParametersParameterComponent();
				parameter.setName("workflow");
				StringType workflow = new StringType();
				workflow.setValue(tokenParam.get("workflow")[0]);
				parameter.setValue(workflow);
				parameters.add(parameter);
				retval.setParameter(parameters);
			}
			
			//set queryParameters
			parameter = new ParametersParameterComponent();
			parameter.setName("queryParameters");
			StringType queryParameters = new StringType();
			queryParameters.setValue(pathParams.get("queryParameters"));
			parameter.setValue(queryParameters);
			parameters.add(parameter);
			retval.setParameter(parameters);

		}
		return retval;
	}

	public static Parameters generateReadParameterResource(Map<String, String> pathParams, String id, RequestDetails theRequestDetails) {
		Parameters retval = new Parameters();
		retval.setId(CommonUtil.generateId().toString());
		List<ParametersParameterComponent> parameters = new ArrayList<>();
		ParametersParameterComponent parameter = new ParametersParameterComponent();
		parameter.setName("resourceType");
		StringType resourceType = new StringType();
		resourceType.setValue(pathParams.get("resourceType"));
		parameter.setValue(resourceType);
		parameters.add(parameter);
		retval.setParameter(parameters);
		parameter = new ParametersParameterComponent();
		parameter.setName("requestId");
		StringType requestIdType = new StringType();
		requestIdType.setValue(pathParams.get("requestId"));
		parameter.setValue(requestIdType);
		parameters.add(parameter);
		retval.setParameter(parameters);
		parameter = new ParametersParameterComponent();
		parameter.setName("practiceId");
		StringType practiceIdType = new StringType();
		practiceIdType.setValue(pathParams.get("practiceId"));
		parameter.setValue(practiceIdType);
		parameters.add(parameter);
		retval.setParameter(parameters);
		parameter = new ParametersParameterComponent();
		parameter.setName("readById");
		StringType nameType = new StringType();
		nameType.setValue(id);
		parameter.setValue(nameType);
		parameters.add(parameter);
		retval.setParameter(parameters);
		if(theRequestDetails != null) {
			Map<String, String[]> tokenParam = theRequestDetails.getParameters();
			//set patientGuid
			if(tokenParam.containsKey("patientGuid") && tokenParam.get("patientGuid") != null) {
				parameter = new ParametersParameterComponent();
				parameter.setName("patientGuid");
				StringType patientGuid = new StringType();
				patientGuid.setValue(tokenParam.get("patientGuid")[0]);
				parameter.setValue(patientGuid);
				parameters.add(parameter);
				retval.setParameter(parameters);
			}
			
			//set providerGuid
			if(tokenParam.containsKey("providerGuid") && tokenParam.get("providerGuid") != null) {
				parameter = new ParametersParameterComponent();
				parameter.setName("providerGuid");
				StringType providerGuid = new StringType();
				providerGuid.setValue(tokenParam.get("providerGuid")[0]);
				parameter.setValue(providerGuid);
				parameters.add(parameter);
				retval.setParameter(parameters);
			}
			
			//set vendorId
			if(tokenParam.containsKey("vendorId") && tokenParam.get("vendorId") != null) {
				parameter = new ParametersParameterComponent();
				parameter.setName("vendorId");
				StringType vendorId = new StringType();
				vendorId.setValue(tokenParam.get("vendorId")[0]);
				parameter.setValue(vendorId);
				parameters.add(parameter);
				retval.setParameter(parameters);
			}
			//set workflow
			if(tokenParam.containsKey("workflow") && tokenParam.get("workflow") != null) {
				parameter = new ParametersParameterComponent();
				parameter.setName("workflow");
				StringType workflow = new StringType();
				workflow.setValue(tokenParam.get("workflow")[0]);
				parameter.setValue(workflow);
				parameters.add(parameter);
				retval.setParameter(parameters);
			}
			
			//set queryParameters
			parameter = new ParametersParameterComponent();
			parameter.setName("queryParameters");
			StringType queryParameters = new StringType();
			queryParameters.setValue(pathParams.get("resourceType")+"/"+id);
			parameter.setValue(queryParameters);
			parameters.add(parameter);
			retval.setParameter(parameters);
		}
		return retval;
	}

	/**
	 * This method generates the Group Parameter Resource
	 * 
	 * @param pathParams
	 * @param id
	 * @return
	 */
	public static Parameters generateGroupParameterResource(Map<String, Object> requestMap) {
		Parameters retval = new Parameters();
		retval.setId(CommonUtil.generateId().toString());
		List<ParametersParameterComponent> parameters = new ArrayList<>();
		ParametersParameterComponent parameter = new ParametersParameterComponent();
		parameter.setName(Parameter.RESOURCETYPE.toString());
		StringType resourceType = new StringType();
		resourceType.setValue((String) requestMap.get("resourceType"));
		parameter.setValue(resourceType);
		parameters.add(parameter);
		retval.setParameter(parameters);

		parameter = new ParametersParameterComponent();
		parameter.setName(Parameter.REQUEST_ID.toString());
		StringType requestIdType = new StringType();
		requestIdType.setValue((String) requestMap.get(Parameter.REQUEST_ID.toString()));
		parameter.setValue(requestIdType);
		parameters.add(parameter);
		retval.setParameter(parameters);

		parameter = new ParametersParameterComponent();
		parameter.setName(Parameter.GROUP_ID.toString());
		StringType groupIdType = new StringType();
		groupIdType.setValue((String) requestMap.get(Parameter.GROUP_ID.toString()));
		parameter.setValue(groupIdType);
		parameters.add(parameter);
		retval.setParameter(parameters);

		parameter = new ParametersParameterComponent();
		parameter.setName(Parameter.REQUESTED_OPERATION.toString());
		StringType operationType = new StringType();
		operationType.setValue((String) requestMap.get(Parameter.REQUESTED_OPERATION.toString()));
		parameter.setValue(operationType);
		parameters.add(parameter);
		retval.setParameter(parameters);

		parameter = new ParametersParameterComponent();
		parameter.setName(Parameter.REQUEST_URL.toString());
		StringType urlType = new StringType();
		urlType.setValue((String) requestMap.get(Parameter.REQUEST_URL.toString()));
		parameter.setValue(urlType);
		parameters.add(parameter);
		retval.setParameter(parameters);

		parameter = new ParametersParameterComponent();
		parameter.setName(Parameter.RESOURCES_PER_FILE.toString());
		IntegerType fileType = new IntegerType();
		fileType.setValue(Integer.parseInt((String) requestMap.get(Parameter.RESOURCES_PER_FILE.toString())));
		parameter.setValue(fileType);
		parameters.add(parameter);
		retval.setParameter(parameters);
		
		parameter = new ParametersParameterComponent();
		parameter.setName("vendorId");
		StringType vendorType = new StringType();
		vendorType.setValue((String) requestMap.get("vendorId"));
		parameter.setValue(vendorType);
		parameters.add(parameter);
		retval.setParameter(parameters);

		parameter = new ParametersParameterComponent();
		parameter.setName("workflow");
		StringType workflowType = new StringType();
		workflowType.setValue("backend");
		parameter.setValue(workflowType);
		parameters.add(parameter);
		retval.setParameter(parameters);
		
		if (requestMap.get(Parameter.TYPE.toString()) != null) {
			parameter = new ParametersParameterComponent();
			parameter.setName(Parameter.TYPE.toString());
			StringType type = new StringType();
			type.setValue((String) requestMap.get(Parameter.TYPE.toString()));
			parameter.setValue(type);
			parameters.add(parameter);
			retval.setParameter(parameters);
		}

		if (requestMap.get(Parameter.SINCE.toString()) != null) {
			SearchParameterMap paramMap = (SearchParameterMap) requestMap.get(Parameter.SINCE.toString());
			List<String> dateString = CommonUtil.getDateParamsFromQuery(paramMap, Parameter.SP_DATE.toString());
			for (String date : dateString) {
				parameter = new ParametersParameterComponent();
				InstantType stringType = new InstantType();
				parameter.setName(Parameter.SINCE.toString());
				stringType.setValueAsString(date);
				parameter.setValue(stringType);
				parameters.add(parameter);
				retval.setParameter(parameters);
			}

		}

		return retval;
	}
	
	
	public static Parameters deleteParameterResource(Map<String, Object> requestMap) {
		Parameters retval = new Parameters();
		retval.setId(CommonUtil.generateId().toString());
		List<ParametersParameterComponent> parameters = new ArrayList<>();
		ParametersParameterComponent parameter = new ParametersParameterComponent();
		parameter = new ParametersParameterComponent();
		parameter.setName(Parameter.REQUEST_ID.toString());
		StringType requestIdType = new StringType();
		requestIdType.setValue((String) requestMap.get(Parameter.REQUEST_ID.toString()));
		parameter.setValue(requestIdType);
		parameters.add(parameter);
		retval.setParameter(parameters);

		parameter = new ParametersParameterComponent();
		parameter.setName("jobId");
		StringType groupIdType = new StringType();
		groupIdType.setValue((String) requestMap.get("jobId"));
		parameter.setValue(groupIdType);
		parameters.add(parameter);
		retval.setParameter(parameters);

		parameter = new ParametersParameterComponent();
		parameter.setName(Parameter.REQUESTED_OPERATION.toString());
		StringType operationType = new StringType();
		operationType.setValue((String) requestMap.get(Parameter.REQUESTED_OPERATION.toString()));
		parameter.setValue(operationType);
		parameters.add(parameter);
		retval.setParameter(parameters);

		parameter = new ParametersParameterComponent();
		parameter.setName("vendorId");
		StringType urlType = new StringType();
		urlType.setValue((String) requestMap.get("vendorId"));
		parameter.setValue(urlType);
		parameters.add(parameter);
		retval.setParameter(parameters);

		parameter = new ParametersParameterComponent();
		parameter.setName("workflow");
		StringType fileType = new StringType();
		fileType.setValue("backend");
		parameter.setValue(fileType);
		parameters.add(parameter);
		retval.setParameter(parameters);
		
		return retval;
	}

}
