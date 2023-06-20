/*******************************************************************************
 * Copyright (c) 2020-2022 InteropX LLC, www.interopx.com
 *
 * All Rights Reserved.
 *
 * The source code contained herein, and the packages generated from the source code, are and remain the property of InteropX, LLC. 
 * Access to this source code and the generated packages is granted to authorized customers of InteropX under an InteropX Software License Agreement (SLA).
 * Any use of the source code or the generated packages, not explicitly covered under the signed SLA, is prohibited.
 *******************************************************************************/
package com.interopx.fhir.facade.util;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.StringType;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.interopx.fhir.facade.model.AuthConfiguration;
import com.interopx.fhir.facade.model.RequestResponseLog;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.TokenParam;

/**
 * CommonUtil.java
 *
 */
@Component
public class CommonUtil {
	private static final Logger logger = LoggerFactory.getLogger(CommonUtil.class);
	public static HashMap<String, String> authMap = new HashMap<>();
	public static Map<String, String> scopeToResourceMap = new HashMap<>();

	public CommonUtil() {
		scopeToResourceNameMap();
	}

	public static void scopeToResourceNameMap() {
		scopeToResourceMap.put("system/Goal.read", "Goal");
		scopeToResourceMap.put("system/Practitioner.read", "Practitioner");
		scopeToResourceMap.put("system/Condition.read", "Condition");
		scopeToResourceMap.put("system/Device.read", "Device");
		scopeToResourceMap.put("system/CarePlan.read", "CarePlan");
		scopeToResourceMap.put("system/CareTeam.read", "CareTeam");
		scopeToResourceMap.put("system/Immunization.read", "Immunization");
		scopeToResourceMap.put("system/MedicationRequest.read", "MedicationRequest");
		scopeToResourceMap.put("system/Medication.read", "Medication");
		scopeToResourceMap.put("system/Location.read", "Location");
		scopeToResourceMap.put("system/Provenance.read", "Provenance");
		scopeToResourceMap.put("system/Procedure.read", "Procedure");
		scopeToResourceMap.put("system/DiagnosticReport.read", "DiagnosticReport");
		scopeToResourceMap.put("system/AllergyIntolerance.read", "AllergyIntolerance");
		scopeToResourceMap.put("system/PractitionerRole.read", "PractitionerRole");
		scopeToResourceMap.put("system/Patient.read", "Patient");
		scopeToResourceMap.put("system/Organization.read", "Organization");
		scopeToResourceMap.put("system/DocumentReference.read", "DocumentReference");
		scopeToResourceMap.put("system/Binary.read", "Binary");
		scopeToResourceMap.put("system/Observation.read", "Observation");
		scopeToResourceMap.put("system/Encounter.read", "Encounter");
	}

	public String getResourceNameByScope(String scope) {
		return scopeToResourceMap.get(scope);
	}

	/**
	 * Returns boolean flag based on authorization verification results calls
	 * Introspect end point
	 * 
	 * @param name a <code>String</code> specifying the name of a accessToken
	 * @param name a <code>HttpServletRequest</code> specifying the name of a
	 *             request
	 * @return whether the access token is valid or not
	 *
	 */

	public static JSONObject validateAccessToken(String accessToken, List<AuthConfiguration> authConfigList,
			String groupId, String orgId) {
		JSONObject tokenResponse = new JSONObject();
		try {
			String isActive = null;

			if (authConfigList != null && !authConfigList.isEmpty()) {
				logger.debug("Using Introspection endpoint:::::{}",
						authConfigList.get(0).getIntrospectionEndpointUrl());
				logger.debug("Using Client Id:::::{}", authConfigList.get(0).getClientId());
				logger.debug("Using Client Secret:::::{}", authConfigList.get(0).getClientSecret());
				String url = authConfigList.get(0).getIntrospectionEndpointUrl() + "?token=" + accessToken + "&groupId="
						+ groupId + "&orgId=" + orgId;
				logger.debug("Invoking the URL:::::{}", url);

				HttpHeaders headers = new HttpHeaders();
				String authValues = authConfigList.get(0).getClientId() + ":" + authConfigList.get(0).getClientSecret();
				String base64encoded = Base64.getEncoder().encodeToString(authValues.getBytes(StandardCharsets.UTF_8));
				headers.add("Authorization", "Basic " + base64encoded);

				HttpEntity request = new HttpEntity(headers);

				RestTemplate restTemplate = new RestTemplate();
				ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

				if (response.getStatusCode() == HttpStatus.OK) {
					logger.debug("response is 200");
					logger.debug("Response:::::{}", response.getBody());
					tokenResponse = new JSONObject(response.getBody());
				}

				logger.debug("Is Token Active:::::{}", isActive);
				return tokenResponse;

			}

		} catch (Exception e) {
			logger.error("Exception in getAccessToken() of IntrospectInterceptor class ", e);
		}
		return tokenResponse;

	}

	/**
	 * Returns full url including query parameters
	 * 
	 * @param request
	 * @return
	 */
	public static String getFullURL(HttpServletRequest request) {
		String url = null;
		try {
			StringBuilder requestURL = new StringBuilder(request.getRequestURL().toString());
			String queryString = request.getQueryString();
			if (queryString != null) {
				requestURL.append('?').append(queryString);
			}
			url = requestURL.toString();
		} catch (Exception e) {
			logger.error("Exception in getFullURL() of CommonUtil class : ", e);
		}
		return url;
	}

	/**
	 * This method gets FhirFacadeServer url from data base
	 * @return
	 */
	public static String getFhirFacadeServerUrl() {
		try {
			  String baseUrl = "FhirFacadeServerUrl";
			  logger.debug("Using the Server URL from DB::::{}",baseUrl);
			  return baseUrl;
		}
		catch(Exception e) {
			logger.error("Exception in getFhirFacadeServerUrl() of CommonUtil class : ", e);
		}
		return null;
	}

	/**
	 * Returns request url including query parameters
	 * @param fullUrl
	 * @param uuid
	 * @param resourceType
	 * @return
	 */
	public static String getFullUrl(String fullUrl, String uuid, String resourceType) {
		String url = null;
		try {
			url = fullUrl;
			url = url.split("\\?", 2)[0];
			String resource = url.substring(url.lastIndexOf('-') + 1);
			if (!resourceType.equalsIgnoreCase(resource)) {
				url = url.replace(resource, resourceType);
			}
			url = url + "/" + uuid;
		} catch (Exception e) {
			logger.error("Exception in getFullUrl() of CommonUtil class : ", e);
		}
		return url;
	}

	/**
	 * Method for returning the appropriate value based on the parameter
	 * 
	 * @param theMap
	 * @param parameter
	 * @return
	 */
	public static Map<String, String> getIdentifierSearchParameterValue(SearchParameterMap theMap) {
		Map<String, String> valueMap = new HashMap<>();
		String key = "";
		String value = "";
		try {
			List<List<? extends IQueryParameterType>> list = theMap.get("identifier");
			if (list != null) {
				for (List<? extends IQueryParameterType> values : list) {
					for (IQueryParameterType params : values) {
						TokenParam identifier = (TokenParam) params;
						if (identifier.getValue() != null) {
							key = identifier.getValue();
							if (identifier.getSystem() != null) {
								value = identifier.getSystem();

							}
						}
						if (value.isEmpty()) {
							valueMap.put(key, key);
						} else {
							valueMap.put(key, value);
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("Exception in getIdentifierSearchParameterValue() of CommonUtil class : ", e);
		}
		return valueMap;
	}

	/**
	 * Method for returning the appropriate value based on the parameter
	 *
	 * @param theMap
	 * @param parameter
	 * @return
	 */
	public static Map<String, String> getTokenParamSearchParameterValue(SearchParameterMap theMap, String parameter) {
		Map<String, String> valueMap = new HashMap<>();
		String key = "";
		String value = "";
		try {
			List<List<? extends IQueryParameterType>> list = theMap.get(parameter);
			if (list != null) {
				for (List<? extends IQueryParameterType> values : list) {
					for (IQueryParameterType params : values) {
						TokenParam data = (TokenParam) params;
						if (data.getValue() != null) {
							key = data.getValue();

							if (data.getSystem() != null) {
								value = data.getSystem();

							}
						}
						if (value.isEmpty()) {
							valueMap.put(key, key);
						} else {
							valueMap.put(key, value);
						}

					}

				}
			}
		} catch (Exception e) {
			logger.error("Exception in getTokenParamSearchParameterValue() of CommonUtil class : ", e);
		}
		return valueMap;
	}

	/**
	 * This method returns id part from ReferenceParam
	 * @param theMap
	 * @param parameterName
	 * @return
	 */
	public static String getIdFromReferenceSearchParam(SearchParameterMap theMap, String parameterName) {
		String id = null;
		try {
			List<List<? extends IQueryParameterType>> list = theMap.get(parameterName);
			if (list != null) {
				for (List<? extends IQueryParameterType> values : list) {
					for (IQueryParameterType params : values) {
						ReferenceParam valueReference = (ReferenceParam) params;
						id = valueReference.getIdPart();
					}
				}
			}
		} catch (Exception e) {
			logger.error("Exception in getIdFromReferenceSearchParam() of CommonUtil class : ", e);
		}
		return id;
	}

	/**
	 * This method finds prefix and date from pamaters
	 * @param theMap
	 * @param parameterName
	 * @return
	 */
	public static List<String> getDateParamsFromQuery(SearchParameterMap theMap, String parameterName) {
		List<String> dateList = new ArrayList<>();
		try {
			List<List<? extends IQueryParameterType>> list = theMap.get(parameterName);
			if (list != null) {
				for (List<? extends IQueryParameterType> values : list) {
					String orValue = "";
					for (IQueryParameterType params : values) {
						DateParam birthDate = (DateParam) params;
						if (birthDate.getPrefix() != null) {
							if (birthDate.getPrefix().getValue().equals("gt")) {
								orValue += "gt|";
							} else if (birthDate.getPrefix().getValue().equals("lt")) {
								orValue += "lt|";
							} else if (birthDate.getPrefix().getValue().equals("ge")) {
								orValue += "ge|";

							} else if (birthDate.getPrefix().getValue().equals("le")) {
								orValue += "le|";

							} else if (birthDate.getPrefix().getValue().equals("eq")) {
								orValue += "eq|";

							}
						}
						orValue += birthDate.getValueAsString() + ",";
					}
					if (StringUtils.isNotBlank(orValue)) {
						if (orValue.endsWith(",")) {
							orValue = orValue.substring(0, orValue.length() - 1);
						}
						dateList.add(orValue);
					}
				}
			}
		} catch (Exception e) {
			logger.error("Exception in getDateParamsFromQuery() of CommonUtil class : ", e);
		}
		return dateList;
	}

	/**
	 * Method for returning the appropriate value based on the parameter
	 *
	 * @param theMap
	 * @param parameter
	 * @return
	 */
	public static List<String> getListOfTokenParamSearchParameterValue(SearchParameterMap theMap, String parameter) {
		List<String> value = new ArrayList<>();
		try {
			List<List<? extends IQueryParameterType>> list = theMap.get(parameter);
			if (list != null) {
				for (List<? extends IQueryParameterType> values : list) {
					for (IQueryParameterType params : values) {
						TokenParam data = (TokenParam) params;
						if (data.getValue() != null) {
							value.add(data.getValue());
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("Exception in getSearchParameterValue() of CommonUtil class : ", e);
		}
		return value;
	}

	/**
	 * This method returns list of strings from StringParam
	 * @param theMap
	 * @param parameter
	 * @return
	 */
	public static List<String> getListOfStringParamSearchParameterValue(SearchParameterMap theMap, String parameter) {
		List<String> value = new ArrayList<>();
		try {
			List<List<? extends IQueryParameterType>> list = theMap.get(parameter);
			if (list != null) {
				for (List<? extends IQueryParameterType> values : list) {
					for (IQueryParameterType params : values) {
						StringParam data = (StringParam) params;
						if (data.getValue() != null) {
							value.add(data.getValue());
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("Exception in getListOfStringParamSearchParameterValue() of CommonUtil class : ", e);
		}
		return value;
	}

	/**
	 * Method to return the PracticeId and fhirVersion from the URL
	 * @param url
	 * @return
	 */
	public static Map<String, String> getPathParams(StringBuffer url) {
		String pathParams = url.substring(url.lastIndexOf("/fhir/"), url.length());
		String baseUrl = url.substring(0, url.lastIndexOf("/"));
		String[] params = pathParams.split("\\/");
		Map<String, String> map = new HashMap<>();
		map.put("fhirVersion", params[2]);
		map.put("practiceId", params[3]);
		map.put("endPointUrl", baseUrl);
		return map;
	}

	/**
	 * Method to generate Operation Outcomes for Invalid Request
	 * 
	 * @param params
	 * @return
	 */
	public static OperationOutcome getOperationOutcome(String params) {
		OperationOutcome operationOutcome = new OperationOutcome();
		operationOutcome.addIssue().setSeverity(OperationOutcome.IssueSeverity.ERROR)
				.setCode(OperationOutcome.IssueType.PROCESSING).setDiagnostics(String.format(
						"Invalid request: Does not know how to handle get operation with parameter [%s]", params));

		return operationOutcome;
	}

	/**
	 * Generates random uuid id.
	 *
	 * @return UUID
	 */
	public static UUID generateId() {
		return UUID.randomUUID();
	}

	/**
	 * Method to return the PracticeId from the URL
	 * 
	 * @param url
	 * @return
	 */
	public static String getPracticeId(String url) {
		String pathParams = url.substring(url.lastIndexOf("/fhir/"), url.length());
		String[] params = pathParams.split("\\/");
		String practiceId = params[3];
		return practiceId;
	}

	/**
	 * Method for returning the appropriate value based on the parameter
	 *
	 * @param theMap
	 * @param parameter
	 * @return
	 */
	public static Map<String, String> getIdentifierSearchParameterValue(SearchParameterMap theMap, String parameter) {
		Map<String, String> valueMap = new HashMap<>();
		String key = "";
		String value = "";
		try {
			List<List<? extends IQueryParameterType>> list = theMap.get(parameter);
			if (list != null) {
				for (List<? extends IQueryParameterType> values : list) {
					for (IQueryParameterType params : values) {
						TokenParam identifier = (TokenParam) params;
						if (identifier.getValue() != null) {
							key = identifier.getValue();
							if (identifier.getSystem() != null) {
								value = identifier.getSystem();

							}
						}
						if (value.isEmpty()) {
							valueMap.put(key, key);
						} else {
							valueMap.put(key, value);
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("Exception in getIdentifierSearchParameterValue() of CommonUtil class : ", e);
		}
		return valueMap;
	}

	/**
	 * This method takes extract id from search parameter map
	 * 
	 * @param theMap
	 * @param parameter
	 * @return
	 */
	public static String getIdFromSearchById(SearchParameterMap theMap, String parameter) {
		try {
			List<List<? extends IQueryParameterType>> list = theMap.get(parameter);
			if (list != null) {
				for (List<? extends IQueryParameterType> values : list) {
					for (IQueryParameterType params : values) {
						TokenParam id = (TokenParam) params;
						if (StringUtils.isNoneEmpty(id.getValue())) {
							return id.getValue();
						}
					}
				}
			}
		} catch (Exception sqlex) {
			logger.error("Exception in getIdFromSearchById() of CommonUtil class : ", sqlex);
		}
		return null;
	}

	/**
	 * This method takes request as input and return bearer token
	 * 
	 * @param request
	 * @return
	 */
	public static String getAccessTokenFronRequestHeader(HttpServletRequest request) {
		try {
			if (request != null) {
				String token = request.getHeader("Authorization");
				if (token != null && "bearer".equalsIgnoreCase(token.split(" ", 2)[0].toString())) {
					token = token.substring(7);
				}
				return token;
			}
		} catch (Exception e) {
			logger.error("Exception in getAccessTokenFronRequestHeader() of CommonUtil class : ", e);
		}
		return null;
	}

	/**
	 * Method to return the practitioner id
	 * @param url
	 * @return
	 */
	public static String getPractionerId(RequestDetails theRequestDetails) {
		String practionerId = null;
		try {
			String[] patientIdArray = theRequestDetails.getParameters().get("practitioner");
			if (patientIdArray != null) {
				practionerId = patientIdArray[0];
			}

			if (practionerId == null) {
				String requestURL = theRequestDetails.getRequestPath();
				if (requestURL.contains("Practitioner/")) {
					practionerId = StringUtils.substringAfter(requestURL, "Practitioner/");
				}
			}
		} catch (Exception e) {
			logger.error("Exception in getPractioner() of CommonUtil class : ", e);
		}
		return practionerId;
	}
	/**
	 * This method returns value for given key from AzureConfigurationService
	 * @param key
	 * @return
	 */
	public String getAzureConfigurationServiceFhirServerUrl(String key) {
		try {
			String url = "";
		     return url;
		}
		catch(Exception e) {
			logger.error("Exception in getAzureConfigurationServiceFhirServerUrl() of CommonUtil class : ", e);
		}
		return null;
    }
	
	/**
	 * This method creates RequestResponseLog object
	 * @param requestId
	 * @param practiceId
	 * @param resourceType
	 * @return
	 */
	public static RequestResponseLog generateRequestLog(String requestId, String practiceId, String resourceType) {
		RequestResponseLog requestResponseLog = new RequestResponseLog();
		requestResponseLog.setRequestId(requestId);
		requestResponseLog.setStatus(Status.IN_PROGRESS.toString());
		requestResponseLog.setRequestTimestamp(new Date());
		requestResponseLog.setTimestamp(new Date());
		requestResponseLog.setPracticeId(practiceId);
		requestResponseLog.setResourceType(resourceType);
		return requestResponseLog;
	}
	
	/**
	 * This method returns requested parameters
	 * @param request
	 */
	public static String getRequestedParameters(HttpServletRequest request) {
		String paramList = "";
		try {
			Map<String, String[]> hmap = request.getParameterMap();
			for (String param : hmap.keySet()) {
				paramList += param + ",";
			}
			paramList = paramList.substring(0, paramList.length() - 1);
		}
		catch(Exception e) {
			logger.error("Exception in getRequestedParameters() of CommonUtil class : ", e);
		}
		return paramList;
	}

	/**
	 * This method parse the message and extracts the Parameter Resource
	 * 
	 * @param <T>
	 * @param theResourceType
	 * @param theResourceData
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T extends IBaseResource> T resourceParser(Class<T> theResourceType, String theResourceData) {
		IBaseResource iBaseResource = null;
		try {
			FhirContext fhirContext = FhirContext.forR4();
			if (theResourceType != null && (theResourceData != null && !theResourceData.isEmpty())) {
				IParser jsonParser = fhirContext.newJsonParser();
				iBaseResource = jsonParser.parseResource(theResourceType, theResourceData);
			}
		} catch (Exception e) {
			logger.error("Exception in resourceParser() of CommonUtil class : ", e);
		}
		return (T) iBaseResource;
	}
	
	/**
	 * This method returns requested parameters
	 * @param request
	 */
	public static String getRequestIdFromParametersResource(Parameters parameter) {
		try {
			if(parameter != null && !parameter.isEmpty()) {
				if (parameter.hasParameter("requestId")) {
					StringType requestIdType = (StringType) parameter.getParameter("requestId");
					if(requestIdType != null) {
						return requestIdType.getValue(); 
					}
				}
			}
		}
		catch(Exception e) {
			logger.error("Exception in getRequestIdFromParametersResource() in CommonUtil class {} ", e);
		}
		return null;
	}
	
	/**
	 * This method takes string parameters resource as input and gets requestId value
	 * and returns request id
	 * @return
	 */
	public static String getRequestIdFromStringFormatParametersResource(String stringParameter) {
		try {
			Parameters parameter = resourceParser(Parameters.class, stringParameter);
			if(parameter != null && !parameter.isEmpty()) {
				if (parameter.hasParameter("requestId")) {
					StringType requestIdType = (StringType) parameter.getParameter("requestId");
					if(requestIdType != null) {
						return requestIdType.getValue(); 
					}
				}
			}
		}
		catch(Exception e) {
			logger.error("Exception in getRequestIdFromStringFormatParametersResource() in CommonUtil class {} ", e);
		}
		return null;
	}
}