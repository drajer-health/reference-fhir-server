/*******************************************************************************
 * Copyright (c) 2020-2022 InteropX LLC, www.interopx.com
 *
 * All Rights Reserved.
 *
 * The source code contained herein, and the packages generated from the source code, are and remain the property of InteropX, LLC. 
 * Access to this source code and the generated packages is granted to authorized customers of InteropX under an InteropX Software License Agreement (SLA).
 * Any use of the source code or the generated packages, not explicitly covered under the signed SLA, is prohibited.
 *******************************************************************************/
package com.interopx.fhir.facade.interceptor;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.HandlerInterceptor;

import com.interopx.fhir.facade.exception.AuthorizationException;
import com.interopx.fhir.facade.model.AuthConfiguration;
import com.interopx.fhir.facade.service.AuthConfigurationService;
import com.interopx.fhir.facade.util.AppConstants;
import com.interopx.fhir.facade.util.ResourceTypes;

import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.AuthenticationException;
import ca.uhn.fhir.rest.server.interceptor.auth.AuthorizationInterceptor;
import ca.uhn.fhir.rest.server.interceptor.auth.IAuthRule;
import ca.uhn.fhir.rest.server.interceptor.auth.RuleBuilder;

/**
 * Interceptor to introspect every in coming request with provided (SMART on
 * FHIR) authorization mechanism retrieve authToken from request, validate,
 * verify scopes then proceed with requested process
 * 
 * @author Swarna Nelaturi
 */
@Component
public class IntrospectInterceptor extends AuthorizationInterceptor implements HandlerInterceptor {

	private static String SERVICE_NAME;

	@Value("${application.name}")
	public void setApplicationName(String serviceName) {
		SERVICE_NAME = serviceName;
	}

	@Autowired
	AuthConfigurationService authConfigurationService;

	@Lazy
	@Autowired
	RestTemplate restTemplate;

	public static final String INTROSPECT_URL = "api/introspect?token=";
	public static final String SCOPES_URL = "api/scopes?token=";

	static final Logger logger = LoggerFactory.getLogger(IntrospectInterceptor.class);


	@Autowired(required = false)
	private HttpServletRequest request;

	@Autowired(required = false)
	private HttpServletResponse response;

	@Override
	public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
		logger.debug("Entry IntrospectInterceptor - buildRuleList()");
		return introspectRequest(theRequestDetails);

	}

	/**
	 * Verifies/introspect incoming access token and scopes
	 * 
	 * @param theRequestDetails
	 * @return List<IAuthRule>
	 */

	private List<IAuthRule> introspectRequest(RequestDetails theRequestDetails) {

		logger.debug("FHIR FACADE EventName ==>> Introspecting the Request");

		try {
			String clientId = null;
			String authToken = theRequestDetails.getHeader("Authorization");
			if (authToken != null && "bearer".equalsIgnoreCase(authToken.split(" ", 2)[0].toString())
					&& !theRequestDetails.getCompleteUrl().contains("/metadata")) {
				authToken = authToken.substring(7);
				JSONObject tokenResponse = null;
				if(authToken.trim().isEmpty()) {
					throw new AuthorizationException(
							"401 Unauthorized: Authorization Token Should not be Empty",
							getOperationOutcome(401));
				}
				tokenResponse = validateAccessToken(authToken, theRequestDetails);
				logger.debug("Token Response::::::{}", tokenResponse);
				if (tokenResponse != null && !tokenResponse.isEmpty()) {
					String[] patientGuid = null;
					String[] providerGuid = null;
					String[] vendorId = null;
					String[] workflow = null;
					String[] practiceGuid = null;
					String[] scope = null;

					if (tokenResponse.has("client_id")) {
						clientId = tokenResponse.getString("client_id");
					}

					if (tokenResponse.has("patientGuid") && tokenResponse.get("patientGuid") != null) {
						patientGuid = new String[] { (String) tokenResponse.get("patientGuid") };
						theRequestDetails.addParameter("patientGuid", patientGuid);
					} else {
						patientGuid = new String[] { "" };
					}

					if (tokenResponse.has("providerGuid") && tokenResponse.get("providerGuid") != null) {
						providerGuid = new String[] { (String) tokenResponse.get("providerGuid") };
						theRequestDetails.addParameter("providerGuid", providerGuid);
					} else {
						providerGuid = new String[] { "" };
					}

					if (tokenResponse.has("practiceGuid") && tokenResponse.get("practiceGuid") != null) {
						practiceGuid = new String[] { (String) tokenResponse.get("practiceGuid") };
						theRequestDetails.addParameter("practiceGuid", practiceGuid);
					} else {
						practiceGuid = new String[] { "" };
					}

					if (tokenResponse.has("vendorId") && tokenResponse.get("vendorId") != null) {
						vendorId = new String[] { (String) tokenResponse.get("vendorId") };
						theRequestDetails.addParameter("vendorId", vendorId);
					} else {
						vendorId = new String[] { "" };
					}

					if (tokenResponse.has("workflow") && tokenResponse.get("workflow") != null) {
						workflow = new String[] { (String) tokenResponse.get("workflow") };
						theRequestDetails.addParameter("workflow", workflow);
					} else {
						workflow = new String[] { "" };
					}

					if (tokenResponse.has("scope") && tokenResponse.get("scope") != null) {
						scope = new String[] { (String) tokenResponse.get("scope") };
						theRequestDetails.addParameter("scope", scope);
					}
				}
				String isActive = String.valueOf(tokenResponse.getBoolean("active"));

				Map<String, String[]> requestParameters = theRequestDetails.getParameters();

				if (tokenResponse != null && !tokenResponse.isEmpty()) {
					logger.debug("Resource Name::::{}", theRequestDetails.getResourceName());
					logger.debug("OperationType::::{}", theRequestDetails.getRestOperationType().getCode());
					if (theRequestDetails.getRestOperationType().getCode().equalsIgnoreCase("read")
							&& theRequestDetails.getResourceName().equalsIgnoreCase(ResourceTypes.PATIENT.toString())) {
						logger.debug(AppConstants.DEBUG_REST_OPERATION_TYPE_FOR_RESOURCE,
								theRequestDetails.getRestOperationType().getCode(),
								theRequestDetails.getResourceName());
						if (tokenResponse.has("patientGuid")) {
							if (theRequestDetails.getRequestPath().contains(AppConstants.PATIENT_PATH)) {
								String patientId = StringUtils.substringAfter(theRequestDetails.getRequestPath(),
										AppConstants.PATIENT_PATH);
								logger.debug("Comparing Requested PatientId:{} with PatientId:{} from tokenResponse",
										patientId, tokenResponse.getString("patientGuid"));
								if(tokenResponse.has("workflow")) {
										String tokenPatientGuid = tokenResponse.getString("patientGuid");
										if(tokenResponse.getString("workflow").equalsIgnoreCase("standalone_provider")
												&& tokenPatientGuid != null && !tokenPatientGuid.isEmpty()
												&& !patientId.equals(tokenPatientGuid)) {
											logger.error("Error code 401 Unauthorized: Requested Patient Id is invalid.");
											throw new AuthorizationException(
													"401 Unauthorized: Requested Patient Id is invalid.",
													getOperationOutcome(401));
										}	
										else if(!tokenResponse.getString("workflow").equalsIgnoreCase("standalone_provider")
												&& !patientId.equals(tokenPatientGuid)) {
											logger.error("Error code 401 Unauthorized: Requested Patient Id is invalid.");
											throw new AuthorizationException(
													"401 Unauthorized: Requested Patient Id is invalid.",
													getOperationOutcome(401));
										}
								}
								else if (!patientId.equals(tokenResponse.getString("patientGuid"))) {
									logger.error("Error code 401 Unauthorized: Requested Patient Id is invalid.");
									throw new AuthorizationException(
											"401 Unauthorized: Requested Patient Id is invalid.",
											getOperationOutcome(401));
								}
							}
						}
					}
					if (theRequestDetails.getRestOperationType().getCode().equalsIgnoreCase("search-type")
							&& theRequestDetails.getResourceName().equalsIgnoreCase(ResourceTypes.PATIENT.toString())) {
						logger.debug(AppConstants.DEBUG_REST_OPERATION_TYPE_FOR_RESOURCE,
								theRequestDetails.getRestOperationType().getCode(),
								theRequestDetails.getResourceName());
						if (tokenResponse.has("patientGuid")) {
							if (requestParameters.containsKey("_id")) {
								String[] patientId = requestParameters.get("_id");
								if (patientId != null && patientId.length > 0) {
									logger.info("Comparing Requested PatientId:{} with PatientId:{} from tokenResponse",

											patientId, tokenResponse.getString("patientGuid"));
									if(tokenResponse.has("workflow")) {
										String tokenPatientGuid = tokenResponse.getString("patientGuid");
										if(tokenResponse.getString("workflow").equalsIgnoreCase("standalone_provider")
												&& tokenPatientGuid != null && !tokenPatientGuid.isEmpty()
												&& !patientId[0].equals(tokenPatientGuid)) {
											logger.error("Error code 401 Unauthorized: Requested Patient id is invalid.");
											throw new AuthorizationException(
													"401 Unauthorized: Requested Patient id is invalid.",
													getOperationOutcome(401));
										}	
										else if(!tokenResponse.getString("workflow").equalsIgnoreCase("standalone_provider")
												&& !patientId[0].equals(tokenPatientGuid)) {
											logger.error("Error code 401 Unauthorized: Requested Patient id is invalid.");
											throw new AuthorizationException(
													"401 Unauthorized: Requested Patient id is invalid.",
													getOperationOutcome(401));
										}
									}
									else if (!patientId[0].equals(tokenResponse.getString("patientGuid"))) {
										logger.error("Error code 401 Unauthorized: Requested Patient id is invalid.");
										throw new AuthorizationException(
												"401 Unauthorized: Requested Patient id is invalid.",
												getOperationOutcome(401));
									}
								}
							}
						}
					}

					if (theRequestDetails.getRestOperationType().getCode().equalsIgnoreCase("search-type")
							&& !theRequestDetails.getResourceName().equalsIgnoreCase(ResourceTypes.PATIENT.toString())
							&& tokenResponse.has("workflow")
							&& !tokenResponse.getString("workflow").equalsIgnoreCase("backend")) {
						logger.debug(AppConstants.DEBUG_REST_OPERATION_TYPE_FOR_RESOURCE,
								theRequestDetails.getRestOperationType().getCode(),
								theRequestDetails.getResourceName());
						if (tokenResponse.has("patientGuid")) {
							if (requestParameters.containsKey("patient")) {
								String[] patientId = requestParameters.get("patient");
								if (patientId != null && patientId.length > 0) {
									logger.info("Comparing Requested PatientId:{} with PatientId:{} from tokenResponse",
											patientId, tokenResponse.getString("patientGuid"));
									if (patientId[0].contains(AppConstants.PATIENT_PATH)) {
										String requestedPatientId = StringUtils.substringAfter(patientId[0],
												AppConstants.PATIENT_PATH);
										patientId[0] = requestedPatientId;
									}
									if(tokenResponse.has("workflow")) {
										String tokenPatientGuid = tokenResponse.getString("patientGuid");
										if(tokenResponse.getString("workflow").equalsIgnoreCase("standalone_provider")
												&& tokenPatientGuid != null && !tokenPatientGuid.isEmpty()
												&& !patientId[0].equals(tokenPatientGuid)) {
											logger.error("Error code 401 Unauthorized: Requested Patient id is invalid.");
											throw new AuthorizationException(
													"401 Unauthorized: Requested Patient id is invalid.",
													getOperationOutcome(401));
										}	
										else if(!tokenResponse.getString("workflow").equalsIgnoreCase("standalone_provider")
												&& !patientId[0].equals(tokenPatientGuid)) {
											logger.error("Error code 401 Unauthorized: Requested Patient id is invalid.");
											throw new AuthorizationException(
													"401 Unauthorized: Requested Patient id is invalid.",
													getOperationOutcome(401));
										}
									}
									else if (!patientId[0].equals(tokenResponse.getString("patientGuid"))) {
										logger.error("Error code 401 Unauthorized: Requested Patient id is invalid.");
										throw new AuthorizationException(
												"401 Unauthorized: Requested Patient id is invalid.",
												getOperationOutcome(401));
									}
								}
							}
						}
					}

					if (theRequestDetails.getRestOperationType().getCode()
							.equalsIgnoreCase("extended-operation-instance")
							&& theRequestDetails.getResourceName().equalsIgnoreCase(ResourceTypes.GROUP.toString())
							&& tokenResponse.has("workflow")
							&& tokenResponse.getString("workflow").equalsIgnoreCase("backend")) {
						logger.debug(AppConstants.DEBUG_REST_OPERATION_TYPE_FOR_RESOURCE,
								theRequestDetails.getRestOperationType().getCode(),
								theRequestDetails.getResourceName());
						// Need to write the code to validate PracticeGuid.
					}

					if (tokenResponse.has("aud")
							&& theRequestDetails.getResourceName() != null
							&& !theRequestDetails.getResourceName().equalsIgnoreCase(ResourceTypes.GROUP.toString())) {
						JSONArray audJsonArr = (JSONArray) tokenResponse.getJSONArray("aud");
						String[] practiceGuid = theRequestDetails.getRequestPath().split("/");
						if (practiceGuid.length > 0) {
							logger.debug("Comparing {}::::: with PracticeId::::{}", audJsonArr.toString(),
									practiceGuid[0]);
							if (!audJsonArr.toString().contains(practiceGuid[0])) {
								logger.error("Error code 401 Unauthorized: Requested Practice id is invalid.");
								throw new AuthorizationException("401 Unauthorized: Requested Practice id is invalid.",
										getOperationOutcome(401));
							}
						}
					}
				}

				if (isActive == null) {
					logger.error("Error code 401 Unauthorized: Authentication token is invalid.");
					throw new AuthorizationException("401 Unauthorized: Authentication token is invalid.",
							getOperationOutcome(401));
				} else if (isActive.equalsIgnoreCase("true")) {
					logger.debug("Authentication token is valid.");
					logger.debug("Received Scopes in Token Response:::::{}", tokenResponse.getString("scope"));
					List<String> scopes = Arrays.asList(tokenResponse.getString("scope").split(" "));

					logger.debug("Fullr URL contains fhir/r4:::::{}",
							theRequestDetails.getCompleteUrl().contains("/fhir/r4"));
					logger.debug("Fullr URL contains /Patient:::::{}",
							theRequestDetails.getCompleteUrl().contains("/Patient"));
					logger.debug("Scope Contains Patient:::::{}", scopes.contains("patient/Patient.read"));

					if ((scopes.contains("patient/Patient.read") || scopes.contains("user/Patient.read") || scopes.contains("system/Patient.read"))
							&& (theRequestDetails.getCompleteUrl().contains("/fhir/r4")
									&& theRequestDetails.getCompleteUrl().contains("/Patient"))) {
						logger.debug("FHIR FACADE EventName ==>> Introspection completed for Patient Resource");
						return new RuleBuilder().allowAll().build();
					} else if ((scopes.contains("patient/AllergyIntolerance.read")
							|| scopes.contains("user/AllergyIntolerance.read") || scopes.contains("system/AllergyIntolerance.read"))
							&& (theRequestDetails.getCompleteUrl().contains("/fhir/r4")
									&& theRequestDetails.getCompleteUrl().contains("/AllergyIntolerance"))) {
						logger.debug("FHIR FACADE EventName ==>> Introspection completed for AllergyIntolerance Resource");
						return new RuleBuilder().allowAll().build();
					} else if ((scopes.contains("patient/CarePlan.read") || scopes.contains("user/CarePlan.read") || scopes.contains("system/CarePlan.read"))
							&& (theRequestDetails.getCompleteUrl().contains("/fhir/r4")
									&& theRequestDetails.getCompleteUrl().contains("/CarePlan"))) {
						logger.debug("FHIR FACADE EventName ==>> Introspection completed for CarePlan Resource");
						return new RuleBuilder().allowAll().build();
					} else if ((scopes.contains("patient/CareTeam.read") || scopes.contains("user/CareTeam.read") || scopes.contains("system/CareTeam.read"))
							&& (theRequestDetails.getCompleteUrl().contains("/fhir/r4")
									&& theRequestDetails.getCompleteUrl().contains("/CareTeam"))) {
						logger.debug("FHIR FACADE EventName ==>> Introspection completed for CareTeam Resource");
						return new RuleBuilder().allowAll().build();
					} else if ((scopes.contains("patient/Condition.read") || scopes.contains("user/Condition.read") || scopes.contains("system/Condition.read"))
							&& (theRequestDetails.getCompleteUrl().contains("/fhir/r4")
									&& theRequestDetails.getCompleteUrl().contains("/Condition"))) {
						logger.debug("FHIR FACADE EventName ==>> Introspection completed for Condition Resource");
						return new RuleBuilder().allowAll().build();
					} else if ((scopes.contains("patient/Device.read") || scopes.contains("user/Device.read") || scopes.contains("system/Device.read"))
							&& (theRequestDetails.getCompleteUrl().contains("/fhir/r4")
									&& theRequestDetails.getCompleteUrl().contains("/Device"))) {
						logger.debug("FHIR FACADE EventName ==>> Introspection completed for Device Resource");
						return new RuleBuilder().allowAll().build();
					} else if ((scopes.contains("patient/DiagnosticReport.read")
							|| scopes.contains("user/DiagnosticReport.read") || scopes.contains("system/DiagnosticReport.read"))
							&& (theRequestDetails.getCompleteUrl().contains("/fhir/r4")
									&& theRequestDetails.getCompleteUrl().contains("/DiagnosticReport"))) {
						logger.debug("FHIR FACADE EventName ==>> Introspection completed for DiagnosticReport Resource");
						return new RuleBuilder().allowAll().build();
					} else if ((scopes.contains("patient/DocumentReference.read")
							|| scopes.contains("user/DocumentReference.read") || scopes.contains("system/DocumentReference.read"))
							&& (theRequestDetails.getCompleteUrl().contains("/fhir/r4")
									&& theRequestDetails.getCompleteUrl().contains("/DocumentReference"))) {
						logger.debug("FHIR FACADE EventName ==>> Introspection completed for DocumentReference Resource");
						return new RuleBuilder().allowAll().build();
					} else if ((scopes.contains("patient/Encounter.read") || scopes.contains("user/Encounter.read") || scopes.contains("system/Encounter.read"))
							&& (theRequestDetails.getCompleteUrl().contains("/fhir/r4")
									&& theRequestDetails.getCompleteUrl().contains("/Encounter"))) {
						logger.debug("FHIR FACADE EventName ==>> Introspection completed for Encounter Resource");
						return new RuleBuilder().allowAll().build();
					} else if ((scopes.contains("patient/Goal.read") || scopes.contains("user/Goal.read") || scopes.contains("system/Goal.read"))
							&& (theRequestDetails.getCompleteUrl().contains("/fhir/r4")
									&& theRequestDetails.getCompleteUrl().contains("/Goal"))) {
						logger.debug("FHIR FACADE EventName ==>> Introspection completed for Goal Resource");
						return new RuleBuilder().allowAll().build();
					} else if ((scopes.contains("patient/Immunization.read")
							|| scopes.contains("user/Immunization.read") || scopes.contains("system/Immunization.read"))
							&& (theRequestDetails.getCompleteUrl().contains("/fhir/r4")
									&& theRequestDetails.getCompleteUrl().contains("/Immunization"))) {
						logger.debug("FHIR FACADE EventName ==>> Introspection completed for Immunization Resource");
						return new RuleBuilder().allowAll().build();
					} else if ((scopes.contains("patient/Location.read") || scopes.contains("user/Location.read") || scopes.contains("system/Location.read"))
							&& (theRequestDetails.getCompleteUrl().contains("/fhir/r4")
									&& theRequestDetails.getCompleteUrl().contains("/Location"))) {
						logger.debug("FHIR FACADE EventName ==>> Introspection completed for Location Resource");
						return new RuleBuilder().allowAll().build();
					} else if ((scopes.contains("patient/Medication.read") || scopes.contains("user/Medication.read") || scopes.contains("system/Medication.read"))
							&& (theRequestDetails.getCompleteUrl().contains("/fhir/r4")
									&& theRequestDetails.getCompleteUrl().contains("/Medication"))) {
						logger.debug("FHIR FACADE EventName ==>> Introspection completed for Medication Resource");
						return new RuleBuilder().allowAll().build();
					} else if ((scopes.contains("patient/MedicationRequest.read")
							|| scopes.contains("user/MedicationRequest.read") || scopes.contains("system/MedicationRequest.read"))
							&& (theRequestDetails.getCompleteUrl().contains("/fhir/r4")
									&& theRequestDetails.getCompleteUrl().contains("/MedicationRequest"))) {
						logger.debug("FHIR FACADE EventName ==>> Introspection completed for MedicationRequest Resource");
						return new RuleBuilder().allowAll().build();
					} else if ((scopes.contains("patient/Observation.read") || scopes.contains("user/Observation.read") || scopes.contains("system/Observation.read"))
							&& (theRequestDetails.getCompleteUrl().contains("/fhir/r4")
									&& theRequestDetails.getCompleteUrl().contains("/Observation"))) {
						logger.debug("FHIR FACADE EventName ==>> Introspection completed for Observation Resource");
						return new RuleBuilder().allowAll().build();
					} else if ((scopes.contains("patient/Organization.read")
							|| scopes.contains("user/Organization.read") || scopes.contains("system/Organization.read"))
							&& (theRequestDetails.getCompleteUrl().contains("/fhir/r4")
									&& theRequestDetails.getCompleteUrl().contains("/Organization"))) {
						logger.debug("FHIR FACADE EventName ==>> Introspection completed for Organization Resource");
						return new RuleBuilder().allowAll().build();
					} else if ((scopes.contains("patient/Practitioner.read")
							|| scopes.contains("user/Practitioner.read") || scopes.contains("system/Practitioner.read"))
							&& (theRequestDetails.getCompleteUrl().contains("/fhir/r4")
									&& theRequestDetails.getCompleteUrl().contains("/Practitioner"))) {
						logger.debug("FHIR FACADE EventName ==>> Introspection completed for Practitioner Resource");
						return new RuleBuilder().allowAll().build();
					} else if ((scopes.contains("patient/PractitionerRole.read")
							|| scopes.contains("user/PractitionerRole.read") || scopes.contains("system/PractitionerRole.read"))
							&& (theRequestDetails.getCompleteUrl().contains("/fhir/r4")
									&& theRequestDetails.getCompleteUrl().contains("/PractitionerRole"))) {
						logger.debug("FHIR FACADE EventName ==>> Introspection completed for PractitionerRole Resource");
						return new RuleBuilder().allowAll().build();
					} else if ((scopes.contains("patient/Procedure.read") || scopes.contains("user/Procedure.read") || scopes.contains("system/Procedure.read"))
							&& (theRequestDetails.getCompleteUrl().contains("/fhir/r4")
									&& theRequestDetails.getCompleteUrl().contains("/Procedure"))) {
						logger.debug("FHIR FACADE EventName ==>> Introspection completed for Procedure Resource");
						return new RuleBuilder().allowAll().build();
					} else if ((scopes.contains("patient/Provenance.read") || scopes.contains("user/Provenance.read") || scopes.contains("system/Provenance.read"))
							&& (theRequestDetails.getCompleteUrl().contains("/fhir/r4")
									&& theRequestDetails.getCompleteUrl().contains("/Provenance"))) {
						logger.debug("FHIR FACADE EventName ==>> Introspection completed for Provenance Resource");
						return new RuleBuilder().allowAll().build();
					} else if ((scopes.contains("patient/RelatedPerson.read")
							|| scopes.contains("user/RelatedPerson.read"))
							&& (theRequestDetails.getCompleteUrl().contains("/fhir/r4")
									&& theRequestDetails.getCompleteUrl().contains("/RelatedPerson"))) {
						logger.debug("FHIR FACADE EventName ==>> Introspection completed for RelatedPerson Resource");
						return new RuleBuilder().allowAll().build();
					} else if ((scopes.contains("patient/ServiceRequest.read")
							|| scopes.contains("user/ServiceRequest.read"))
							&& (theRequestDetails.getCompleteUrl().contains("/fhir/r4")
									&& theRequestDetails.getCompleteUrl().contains("/ServiceRequest"))) {
						logger.debug("FHIR FACADE EventName ==>> Introspection completed for ServiceRequest Resource");
						return new RuleBuilder().allowAll().build();
					} else if ((scopes.contains("patient/Endpoint.read") || scopes.contains("user/Endpoint.read"))
							&& (theRequestDetails.getCompleteUrl().contains("/fhir/r4")
									&& theRequestDetails.getCompleteUrl().contains("/Endpoint"))) {
						logger.debug("FHIR FACADE EventName ==>> Introspection completed for Endpoint Resource");
						return new RuleBuilder().allowAll().build();
					} else if ((scopes.contains("patient/Binary.read") || scopes.contains("user/Binary.read"))
							&& (theRequestDetails.getCompleteUrl().contains("/fhir/r4")
									&& theRequestDetails.getCompleteUrl().contains("/Binary"))) {
						logger.debug("FHIR FACADE EventName ==>> Introspection completed for Binary Resource");
						return new RuleBuilder().allowAll().build();
					} else if (scopes.contains("system/Group.read")
							&& (theRequestDetails.getCompleteUrl().contains("/fhir/r4")
									&& theRequestDetails.getCompleteUrl().contains("/Group"))) {
						logger.debug("FHIR FACADE EventName ==>> Introspection completed for Group Resource");
						return new RuleBuilder().allowAll().build();
					} else if (scopes.contains("system/*.read") || scopes.contains("user/*.read")
							|| scopes.contains("patient/*.read")) {
						logger.debug("FHIR FACADE EventName ==>> Introspection completed for Group Resource");
						return new RuleBuilder().allowAll().build();
					} else if (theRequestDetails.getCompleteUrl().contains("/$export-poll-location")) {
						return new RuleBuilder().allowAll().build();
					} else if (theRequestDetails.getCompleteUrl().contains("/bulkdata/download")) {
						return new RuleBuilder().allowAll().build();
					} 
					else if ((scopes.contains("user/Immunization.write") || scopes.contains("user/Condition.write") 
							 || scopes.contains("user/AllergyIntolerance.write")) && theRequestDetails.getCompleteUrl().contains("/fhir/r4")) {
						 logger.debug("FHIR FACADE EventName ==>> Introspection completed for Bundle Resource");
						 	return new RuleBuilder().allowAll().build();
					 }else {
						logger.error(
								"Error code 403 Forbidden. Access denied: You are not Authorized to view this information.");
						return new RuleBuilder().denyAll().build();
					}

				} else if (isActive.equalsIgnoreCase("false")) {
					logger.error("Error code 401 Unauthorized: Authentication token is expired.");
					throw new AuthorizationException("401 Unauthorized: Authentication token is expired",
							getOperationOutcome(401));
				}
			} else if ((theRequestDetails.getCompleteUrl().contains("/metadata"))
					|| (theRequestDetails.getCompleteUrl().contains("/health/"))) {
				return new RuleBuilder().allowAll().build();
			} else if (theRequestDetails.getCompleteUrl().contains("/.well-known")) {
				return new RuleBuilder().allowAll().build();
			} else {
				logger.error("Error code 401 Unauthorized: Authentication token not found.");
				throw new AuthorizationException("401 Unauthorized: Authentication token is expired",
						getOperationOutcome(401));
			}

		} catch (AuthenticationException e) {

			logger.error("Error in introspecting request.");
		} finally {

		}

		return null;
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

	public JSONObject validateAccessToken(String accessToken, RequestDetails theRequestDetails) {
		JSONObject tokenResponse = new JSONObject();
		String url = null;
		String introspectionEndpointUrl = null;
		String clientId = null;
		String clientSecret = null;

		try {
			List<AuthConfiguration> authConfigList = authConfigurationService.getAuthConfiguration();
			if (authConfigList != null && !authConfigList.isEmpty()) {
				introspectionEndpointUrl = authConfigList.get(0).getIntrospectionEndpointUrl();
				clientId = authConfigList.get(0).getClientId();
				clientSecret = authConfigList.get(0).getClientSecret();
			}
			if (theRequestDetails.getRestOperationType().getCode().equalsIgnoreCase("extended-operation-instance")
					&& theRequestDetails.getResourceName().equalsIgnoreCase(ResourceTypes.GROUP.toString())) {
				if (authConfigList != null && !authConfigList.isEmpty()) {
					logger.debug("Using Introspection endpoint:::::{}", introspectionEndpointUrl);
					String groupId = StringUtils.substringBefore(
							StringUtils.substringAfter(theRequestDetails.getRequestPath(), "/Group/"), "/$export");
					String orgId = theRequestDetails.getTenantId();
					url = introspectionEndpointUrl + "?token=" + accessToken + "&groupId=" + groupId + "&orgId="
							+ orgId;
					logger.debug("Invoking the URL:::::{}", url);
				}
			} else {
				logger.debug("Using Introspection endpoint:::::{}", introspectionEndpointUrl);
				logger.debug("Using Client Id:::::{}", clientId);
				logger.debug("Using Client Secret:::::{}", clientSecret);
				url = introspectionEndpointUrl + "?token=" + accessToken;
				logger.debug("Invoking the URL:::::{}", url);
			}

			HttpHeaders headers = new HttpHeaders();
			String authValues = clientId + ":" + clientSecret;
			String base64encoded = Base64.getEncoder().encodeToString(authValues.getBytes(StandardCharsets.UTF_8));
			headers.add("Authorization", "Basic " + base64encoded);

			HttpEntity request = new HttpEntity(headers);

			ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

			if (response.getStatusCode() == HttpStatus.OK) {
				logger.debug("response is 200");
				logger.debug("Response:::::{}", response.getBody());
				tokenResponse = new JSONObject(response.getBody());
			}
			return tokenResponse;

		} catch (Exception e) {
			logger.error("Exception in getAccessToken() of IntrospectInterceptor class ", e);
		}
		return tokenResponse;

	}

	/**
	 * Returns scopes allowed for the client based on access token
	 * 
	 * @param accessToken
	 * @return scope
	 */
	public String getScopes(String accessToken) {
		try {
			String url = null;
			List<AuthConfiguration> authConfigList = authConfigurationService.getAuthConfiguration();
			if (authConfigList != null && !authConfigList.isEmpty()) {
				url = authConfigList.get(0).getAuthorizationEndpointUrl() + SCOPES_URL + accessToken;
			}
			HttpClient httpClient = HttpClientBuilder.create().build();
			HttpPost requestPost = new HttpPost(url);
			HttpResponse response = httpClient.execute(requestPost);
			StringBuffer result = new StringBuffer();
			if (response.getStatusLine().getStatusCode() == 200) {
				BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
				String line;
				while ((line = rd.readLine()) != null) {
					result.append(line);
				}
			}
			return result.toString();
		} catch (Exception e) {
			logger.error("Exception in getScopes() of IntrospectInterceptor class ", e);
		}
		return null;
	}

	/**
	 * Construct OperationOutcome object with required sets
	 * 
	 * @param statuscode
	 * @return operationOutcome with severity and diagnostics
	 */
	private OperationOutcome getOperationOutcome(int statusCode) {
		OperationOutcome oo = new OperationOutcome();
		if (statusCode == 403) {
			oo.addIssue().setSeverity(OperationOutcome.IssueSeverity.ERROR).setDiagnostics(
					"Forbidden: Authentication was provided, but the authenticated user is not permitted to perform the requested operation");
		} else {
			oo.addIssue().setSeverity(OperationOutcome.IssueSeverity.ERROR)
					.setDiagnostics("Unauthorized: No valid token found Authentication token is  invalid OR expired");
		}

		return oo;
	}
}
