/*
*Copyright (c) 2020-2022 InteropX LLC, www.interopx.com
*All Rights Reserved.
*The source code contained herein, and the packages generated from the source code, are and remain the property of InteropX, LLC. Access to this source code and the generated packages is granted to authorized customers of InteropX under an InteropX Software License Agreement (SLA). Any use of the source code or the generated packages, not explicitly covered under the signed SLA, is prohibited.
*/
package com.interopx.fhir.facade.providers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Parameters;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.interopx.fhir.facade.exception.AuthorizationException;
import com.interopx.fhir.facade.exception.Issue;
import com.interopx.fhir.facade.exception.OperationOutCome;
import com.interopx.fhir.facade.model.AuthConfiguration;
import com.interopx.fhir.facade.model.BulkDataOutput;
import com.interopx.fhir.facade.model.BulkDataOutputInfo;
import com.interopx.fhir.facade.model.BulkDataRequest;
import com.interopx.fhir.facade.service.AuthConfigurationService;
import com.interopx.fhir.facade.service.BulkDataRequestService;
import com.interopx.fhir.facade.util.AppConstants;
import com.interopx.fhir.facade.util.CommonUtil;
import com.interopx.fhir.facade.util.ParameterResourceUtil;

import ca.uhn.fhir.context.FhirContext;

/**
 * This controller returns the available content location URLS for the provided
 * Job Id for bulk data
 * 
 * @author xyram
 *
 */
@RestController
public class BulkDataRequestProvider {
	private static final Logger logger = LoggerFactory.getLogger(BulkDataRequestProvider.class);
	@Autowired
	BulkDataRequestService bdrService;

	@Autowired
	FhirContext fhirContext;

	@Autowired
	AuthConfigurationService authConfigurationService;

	/**
	 * This method returns the export location URLs for the provided JobId
	 * 
	 * @param jobId
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/$export-poll-location", method = RequestMethod.GET, produces = { "application/json" })
	@ResponseBody
	public Object getContentLocationResponse(@RequestParam("job_id") String jobId, HttpServletRequest request,
			HttpServletResponse response) {
		logger.debug("Request recevied to get content location response. Job id is {}", jobId);
		String body = "";
		StringBuilder stringBuffer = new StringBuilder();
		String authToken = request.getHeader("Authorization");
		try {
			String uri = "";
			String retryAfter = "120";
			BulkDataRequest bdr = bdrService.getBulkDataRequestByJobId(jobId);
			if (bdr != null) {
				JSONObject practiceDetails = getPracticeDetailsFromAccessToken(authToken, bdr);
				if(practiceDetails != null) {
					// Write the logic to get the message with job Id using client jar
					logger.debug("JobId::::::{}", bdr.getJobId());
	
					logger.debug("Received PracticeGuid::::{}", practiceDetails.getString("practiceGuid"));
					String metadata = null;
					logger.debug("Received Metadata:::::{}", metadata);
					JSONObject obj = new JSONObject(metadata);
					if (obj.has("status")) {
						String status = obj.getString("status");
						bdr.setJobStatus(status);
						bdrService.saveBulkDataRequest(bdr);
						if (status.equals("IN_PROGRESS")) {
							response.setStatus(202);
							response.setHeader("X-Progress", obj.getInt("percent_completion") + "% Completed");
							response.setHeader("Retry-After", retryAfter);
						} else if (status.equals("NOT_STARTED")) {
							response.setStatus(202);
							response.setHeader("X-Progress", "0% Completed");
							response.setHeader("Retry-After", retryAfter);
						} else if (status.equals("COMPLETED")) {
							response.setStatus(200);
							response.setHeader("X-Progress", "100% Completed");
							BulkDataOutput bdo = new BulkDataOutput();
							bdo.setRequest(bdr.getRequest());
							bdo.setRequiresAccessToken(true);
							bdo.setTransactionTime(obj.getString("reference_time"));
	
							ArrayList<BulkDataOutputInfo> bdoInfos = new ArrayList<>();
							JSONArray outputFiles = (JSONArray) obj.get("files_list");
							for (int i = 0; i < outputFiles.length(); i++) {
								BulkDataOutputInfo bdoInfo = new BulkDataOutputInfo();
								JSONObject jsonObj = (JSONObject) outputFiles.get(i);
								bdoInfo.setType(jsonObj.getString("type"));
								bdoInfo.setUrl(uri + "/bulkdata/download?job_id=" + jobId + "&file_name="
										+ jsonObj.getString("file_name"));
								bdoInfos.add(bdoInfo);
							}
	
							bdo.setOutput(bdoInfos);
							return new ResponseEntity<Object>(bdo, HttpStatus.OK);
						} else if (status.equals("CANCELLED")) {
							response.setStatus(202);
							response.setHeader("X-Progress", "CANCELLED");
						}
					}
				}
			} else {
				logger.error("Error code 404: The requested Content-Location was not found for the Job Id.");
				response.setStatus(404);
				return new ResponseEntity<Object>(getOperationOutCome(), HttpStatus.NOT_FOUND);
			}

		} catch (Exception e) {
			logger.error(AppConstants.ERROR_PROCESSING_REQUEST, e);
			if(e instanceof AuthorizationException) {
                response.setStatus(401);
                String operationOutcomeResponse = fhirContext.newJsonParser().setPrettyPrint(true)
                        .encodeResourceToString(getOperationOutcome(401));
                return new ResponseEntity<Object>(operationOutcomeResponse, HttpStatus.UNAUTHORIZED);            }
		}
		logger.debug("End getContentLocationResponse() in  BulkDataRequestProvider class ");
		return body;
	}

	@RequestMapping(value = "/bulkdata/download", method = RequestMethod.GET, produces= {"application/fhir+ndjson"})
	@ResponseBody
	public void downloadFile(@RequestParam("job_id") String jobId, @RequestParam("file_name") String fileName,
			HttpServletRequest theRequestDetails, HttpServletResponse response) {
		logger.debug("Request recevied to download the file. Job id is {}", jobId);
		logger.debug("Request recevied to download the file. FileName is {}", fileName);
		logger.debug("Request URL::::{}", theRequestDetails.getHeader("Authorization"));
		String authToken = theRequestDetails.getHeader("Authorization");
		try {
			BulkDataRequest bdr = bdrService.getBulkDataRequestByJobId(jobId);
			JSONObject practiceDetails = getPracticeDetailsFromAccessToken(authToken, bdr);
			if(practiceDetails != null && practiceDetails.has("practiceGuid")) {
				logger.debug("Received PracticeGuid from Token Response:::::{}", practiceDetails.getString("practiceGuid"));
			} else {
				logger.debug("Token is either invalid or expired");
				response.setStatus(401);
			}
		} catch (Exception e) {
			logger.error(AppConstants.ERROR_PROCESSING_REQUEST, e);
			if(e instanceof AuthorizationException) {
				response.setStatus(401);
			} else {
				response.setStatus(500);
			}
		}
		logger.debug("End getContentLocationResponse() in  BulkDataRequestProvider class ");
	}

	@RequestMapping(value = "/$export-poll-location", method = RequestMethod.DELETE, produces = { "application/json" })
	@ResponseBody
	public Object deleteRequest(@RequestParam("job_id") String jobId, HttpServletRequest request,
			HttpServletResponse response) {
		logger.debug(" Request recevied to delete the Request. Job id is {} " , jobId);
		String authToken = request.getHeader("Authorization");
		String body = "";
		try {
			BulkDataRequest bdr = bdrService.getBulkDataRequestByJobId(jobId);
			JSONObject practiceDetails = getPracticeDetailsFromAccessToken(authToken, bdr);

			if (bdr != null && practiceDetails != null) {
				logger.debug("Job ID is Valid");
				Map<String, Object> requestMap = new HashMap<>();
				requestMap.put("requestId", bdr.getRequestId());
				requestMap.put("jobId", jobId);
				requestMap.put("requestedOperation", "delete");
				requestMap.put("vendorId", practiceDetails.getString("vendorId"));
				requestMap.put("workflow", "backend");

				Parameters parameters = ParameterResourceUtil.deleteParameterResource(requestMap);

				String requestPayload = fhirContext.newJsonParser().encodeResourceToString(parameters);

				logger.debug("Parameters Resource::::{}", requestPayload);

				if (authToken != null && "bearer".equalsIgnoreCase(authToken.split(" ", 2)[0].toString())) {
					authToken = authToken.substring(7);
				}
				
			}

		} catch (Exception e) {
			logger.error(AppConstants.ERROR_PROCESSING_REQUEST, e);
			if(e instanceof AuthorizationException) {
                response.setStatus(401);
                String operationOutcomeResponse = fhirContext.newJsonParser().setPrettyPrint(true)
                        .encodeResourceToString(getOperationOutcome(401));
                return new ResponseEntity<Object>(operationOutcomeResponse, HttpStatus.UNAUTHORIZED);            }
		}
		logger.debug("End getContentLocationResponse() in  BulkDataRequestProvider class ");
		return body;
	}
	
	/**
	 * Gets the practice guid from access token.
	 *
	 * @param authToken the auth token
	 * @param bdr       the bdr
	 * @return the practice guid from access token
	 */
	private JSONObject getPracticeDetailsFromAccessToken(String authToken, BulkDataRequest bdr) {
		JSONObject tokenResponse = null;
		if (authToken != null && "bearer".equalsIgnoreCase(authToken.split(" ", 2)[0].toString())) {
			authToken = authToken.substring(7);
			List<AuthConfiguration> authConfigList = authConfigurationService.getAuthConfiguration();
			tokenResponse = CommonUtil.validateAccessToken(authToken, authConfigList, bdr.getGroupId(),
					bdr.getOrgId());
			if(tokenResponse != null && !tokenResponse.isEmpty()) {
                if(tokenResponse.has("active")) {
                    String isActive = String.valueOf(tokenResponse.getBoolean("active"));
                    if(isActive.equalsIgnoreCase("false")) {
                        throw new AuthorizationException("401 Unauthorized: Authentication token is expired",
                                getOperationOutcome(401));
                    }
                }
			}
		} else {
			throw new AuthorizationException("401 Unauthorized: Authentication token is expired",
					getOperationOutcome(401));
		}
		return tokenResponse;
	}

	private OperationOutCome getOperationOutCome() {

		OperationOutCome operationOutCome = new OperationOutCome();
		operationOutCome.setResourceType("OperationOutcome");
		List<Issue> issues = Arrays.asList(new Issue("error", "processing",
				"The requested Content-Location was not found with the provided Job Id."));
		operationOutCome.setIssue(issues);

		return operationOutCome;

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
					.setDiagnostics("Unauthorized: Invalid or expired access token OR Invalid or unauthorized group id");
		}

		return oo;
	}

}
