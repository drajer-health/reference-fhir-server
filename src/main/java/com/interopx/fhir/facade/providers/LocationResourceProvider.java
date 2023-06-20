/*******************************************************************************
 * Copyright (c) 2020-2022 InteropX LLC, www.interopx.com
 *
 * All Rights Reserved.
 *
 * The source code contained herein, and the packages generated from the source code, are and remain the property of InteropX, LLC. 
 * Access to this source code and the generated packages is granted to authorized customers of InteropX under an InteropX Software License Agreement (SLA).
 * Any use of the source code or the generated packages, not explicitly covered under the signed SLA, is prohibited.
 *******************************************************************************/
package com.interopx.fhir.facade.providers;

import java.time.Instant;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Location;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.interopx.fhir.facade.exception.InvalidRequestException;
import com.interopx.fhir.facade.exception.RequestTimeOutException;
import com.interopx.fhir.facade.model.RequestResponseLog;
import com.interopx.fhir.facade.service.AzureQueueService;
import com.interopx.fhir.facade.util.AppConstants;
import com.interopx.fhir.facade.util.CommonUtil;
import com.interopx.fhir.facade.util.InteropxRequestQReceiver;
import com.interopx.fhir.facade.util.ParameterResourceUtil;
import com.interopx.fhir.facade.util.ResourceUtil;
import com.interopx.fhir.facade.util.ResourceProviderValidator;
import com.interopx.fhir.facade.util.ResourceTypes;
import com.interopx.fhir.facade.util.SearchParameterMap;
import com.interopx.fhir.facade.util.Status;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jaxrs.server.AbstractJaxRsResourceProvider;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.IncludeParam;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.StringAndListParam;
import ca.uhn.fhir.rest.param.TokenAndListParam;
/**
 * This class handles request for Location Resource. It generates a parameter resource based on read/search request types
 * send/receive messages to and from Fhir Server, processes and sends back a resource bundle to client
 * 
 * @author xyram
 *
 */
@Component
public class LocationResourceProvider extends AbstractJaxRsResourceProvider<Location> {
	private static final Logger logger = LoggerFactory.getLogger(LocationResourceProvider.class);

	@Autowired
	FhirContext fhirContext;

	@Autowired
	AzureQueueService service;

	@Autowired
	InteropxRequestQReceiver interopXReceiver;

	public LocationResourceProvider(FhirContext fhirContext) {
		super(fhirContext);
	}

	/**
	 * The getResourceType method comes from IResourceProvider, and must be
	 * overridden to indicate what type of resource this provider supplies.
	 */
	@Override
	public Class<Location> getResourceType() {
		return Location.class;
	}

	/**
	 * The "@Read" annotation indicates that this method supports the read
	 * operation. This operation retrieves a resource by ID. Example URL to invoke
	 * this method: http://<server name>/<context>/fhir/Location/1
	 * @param request
	 * @param response
	 * @param theRequestDetails
	 * @param theId
	 * @return
	 * @throws Exception
	 */
	@Read
	public Resource readOrVread(HttpServletRequest request, HttpServletResponse response, RequestDetails theRequestDetails, @IdParam IdType theId)
			throws Exception {
logger.debug("Request received on FHIR FACADE with Location Read operation :{} at{}",theId.getIdPart() ,Instant.now());
		Location theLocation = null;
		try {
			String token = CommonUtil.getAccessTokenFronRequestHeader(request);
			String requestId = CommonUtil.generateId().toString();
			Map<String, String> pathParams = CommonUtil.getPathParams(request.getRequestURL());
			pathParams.put("requestId",requestId);	
			pathParams.put("resourceType",ResourceTypes.LOCATION.toString());
logger.debug("{}{}{} Constructing the Parrameter resource at {}",AppConstants.DEBUG_REQUEST_ID,requestId,AppConstants.DEBUG_FHIR_FACADE_EVENT,Instant.now());
			Parameters parameter = ParameterResourceUtil.generateReadParameterResource(pathParams, theId.getIdPart(), theRequestDetails);		
logger.debug("{}{}{} Completed constructing the Parrameter resource at {}",AppConstants.DEBUG_REQUEST_ID,requestId,AppConstants.DEBUG_FHIR_FACADE_EVENT,Instant.now());
			String parameterResource = fhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(parameter);
			logger.debug("Generated Parameter Resource: {}", parameterResource);
logger.debug("{}{}{} Sending the message to OutQueue at {}",AppConstants.DEBUG_REQUEST_ID,requestId,AppConstants.DEBUG_FHIR_FACADE_EVENT,Instant.now());
			service.sendMessage(parameterResource,token);
logger.debug("{}{}{} Message sent to OutQueue at {}",AppConstants.DEBUG_REQUEST_ID,requestId,AppConstants.DEBUG_FHIR_FACADE_EVENT,Instant.now());
			// Saving the request to database
			RequestResponseLog requestResponseLog = CommonUtil.generateRequestLog(requestId, pathParams.get("practiceId"), ResourceTypes.LOCATION.toString());
			service.saveRequest(requestResponseLog);
			CompletableFuture<RequestResponseLog> requestResponseObj = service.getResponseFromQueue(requestId);
			RequestResponseLog responseLog = requestResponseObj.get();
			if (responseLog != null) {
				if (responseLog.getStatus().equals(Status.COMPLETED.toString())) {
					Bundle bundle = (Bundle) fhirContext.newJsonParser().parseResource(responseLog.getPayload());
					Resource obj = ResourceUtil.getResourceFromBundleForRead(bundle, ResourceTypes.LOCATION.toString());
					if (obj instanceof OperationOutcome) {
						service.updateStatus(requestId, Status.ERROR.toString());
						throw new InvalidRequestException("400 Bad Request: Unable to process request",
								(OperationOutcome) obj);
					}else {
						theLocation = (Location)obj;
					}
				} else if (responseLog != null && responseLog.getStatus().equals(Status.ABORTED.toString())) {
					throw new RequestTimeOutException("Unable to Process the Request.Request Timeout");
				}
			}
logger.debug("{}{}{} Sending the response at {}",AppConstants.DEBUG_REQUEST_ID,requestId,AppConstants.DEBUG_FHIR_FACADE_EVENT,Instant.now());
		} catch (Exception e) {
			logger.error("Exception in readOrVread() of LocationResourceProvider class : ");
			throw e;
		}
		logger.debug("End readOrVread() in LocationResourceProvider class ");
		return theLocation;
	}

	/**
	 * The "@Search" annotation indicates that this method supports the search
	 * operation. You may have many different method annotated with this annotation,
	 * to support many different search criteria. The search operation returns a
	 * bundle with zero-to-many resources of a given type, matching a given set of
	 * parameters.
	 * @param request
	 * @param response
	 * @param theRequestDetails
	 * @param theId
	 * @param theName
	 * @param theAddress
	 * @param theStatus
	 * @param theAddressState
	 * @param theAddressPostalCode
	 * @param theAddressCity
	 * @param theIncludes
	 * @param theRevIncludes
	 * @return
	 * @throws Exception
	 */
	@Search()
	public Bundle search(HttpServletRequest request, HttpServletResponse response, RequestDetails theRequestDetails,
			
			  @Description(shortDefinition="The ID of the resource")
    @OptionalParam(name=Location.SP_RES_ID)
    TokenAndListParam theId,
			@Description(shortDefinition = "A portion of the location's name or alias") @OptionalParam(name = Location.SP_NAME) StringAndListParam theName,

			@Description(shortDefinition = "A (part of the) address of the location") @OptionalParam(name = Location.SP_ADDRESS) StringAndListParam theAddress,
			@Description(shortDefinition="Searches for locations with a specific kind of status") @OptionalParam(name=Location.SP_STATUS) TokenAndListParam theStatus,
			@Description(shortDefinition = "A state specified in an address") @OptionalParam(name = Location.SP_ADDRESS_STATE) StringAndListParam theAddressState,

			@Description(shortDefinition = "A postal code specified in an address") @OptionalParam(name = Location.SP_ADDRESS_POSTALCODE) StringAndListParam theAddressPostalCode,

			@Description(shortDefinition = "A city specified in an address") @OptionalParam(name = Location.SP_ADDRESS_CITY) StringAndListParam theAddressCity,

			@IncludeParam(allow = {"Location:organization"}) Set<Include> theIncludes,
			@IncludeParam(reverse = true, allow = { "Provenance:target" }) Set<Include> theRevIncludes)
			throws Exception {
		if (StringUtils.isNotBlank(request.getQueryString())) {
			logger.debug(
					"Request received on FHIR FACADE with Location search with parameters :{} at {}" , request.getQueryString() ,Instant.now());
		} else {
			logger.debug("Request received for all Location search");
		}
		SearchParameterMap searchParameterMap = new SearchParameterMap();
		searchParameterMap.add(Location.SP_RES_ID,theId );
		searchParameterMap.add(Location.SP_NAME, theName);
		searchParameterMap.add(Location.SP_ADDRESS, theAddress);
		searchParameterMap.add(Location.SP_ADDRESS_CITY, theAddressCity);
		searchParameterMap.add(Location.SP_ADDRESS_POSTALCODE, theAddressPostalCode);
		searchParameterMap.add(Location.SP_ADDRESS_STATE, theAddressState);
		searchParameterMap.setIncludes(theIncludes);
		searchParameterMap.setRevIncludes(theRevIncludes);

		Bundle retVal = new Bundle();
		try {
			String token = CommonUtil.getAccessTokenFronRequestHeader(request);
			boolean isValid = ResourceProviderValidator.validateRequest(searchParameterMap,
					ResourceTypes.LOCATION.toString());

			if (!isValid) {
				String paramList = CommonUtil.getRequestedParameters(request);
				throw new InvalidRequestException("400 Bad Request: Unable to process request",
						CommonUtil.getOperationOutcome(paramList));
			}
			String requestId = CommonUtil.generateId().toString();
			Map<String, String> pathParams = CommonUtil.getPathParams(request.getRequestURL());
			pathParams.put("queryParameters", request.getQueryString());
			pathParams.put("requestId",requestId);
			pathParams.put("resourceType",ResourceTypes.LOCATION.toString());	
logger.debug("{}{}{} Constructing the Parrameter resource at {}",AppConstants.DEBUG_REQUEST_ID,requestId,AppConstants.DEBUG_FHIR_FACADE_EVENT,Instant.now());
			Parameters parameter = ParameterResourceUtil.generateParameterResource(searchParameterMap,pathParams,theRequestDetails);	
logger.debug("{}{}{} Completed constructing the Parrameter resource at {}",AppConstants.DEBUG_REQUEST_ID,requestId,AppConstants.DEBUG_FHIR_FACADE_EVENT,Instant.now());
			String parameterResource = fhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(parameter);
			logger.debug("Generated Parameter Resource: {}", parameterResource);
logger.debug("{}{}{} Sending the message to OutQueue at {}",AppConstants.DEBUG_REQUEST_ID,requestId,AppConstants.DEBUG_FHIR_FACADE_EVENT,Instant.now());
			service.sendMessage(parameterResource,token);
logger.debug("{}{}{} Message sent to OutQueue at {}",AppConstants.DEBUG_REQUEST_ID,requestId,AppConstants.DEBUG_FHIR_FACADE_EVENT,Instant.now());
			// Saving the request to database
			RequestResponseLog requestResponseLog = CommonUtil.generateRequestLog(requestId, pathParams.get("practiceId"), ResourceTypes.LOCATION.toString());
			service.saveRequest(requestResponseLog);
			CompletableFuture<RequestResponseLog> requestResponseObj = service.getResponseFromQueue(requestId);
			RequestResponseLog responseLog = requestResponseObj.get();
			if (responseLog != null) {
				if (responseLog.getStatus().equals(Status.COMPLETED.toString())) {
					Bundle bundle = (Bundle) fhirContext.newJsonParser().parseResource(responseLog.getPayload());
					if (bundle.hasEntry()) {
						Object obj = ResourceUtil.getResourceFromBundleForSearch(bundle, request);
						if (obj instanceof OperationOutcome) {
							service.updateStatus(requestId, Status.ERROR.toString());
							throw new InvalidRequestException("400 Bad Request: Unable to process request",
									(OperationOutcome) obj);
						}else {
							retVal = (Bundle) obj;
						}
							
					}
				} else if (responseLog != null && responseLog.getStatus().equals(Status.ABORTED.toString())) {
					throw new RequestTimeOutException("Unable to Process the Request.Request Timeout");
				}
			}
logger.debug("{}{}{} Sending the response at {}",AppConstants.DEBUG_REQUEST_ID,requestId,AppConstants.DEBUG_FHIR_FACADE_EVENT,Instant.now());
		} catch (Exception e) {
			logger.error("Exception in search() of LocationResourceProvider class : ");
			throw e;
		}
		logger.debug("End search() in LocationResourceProvider class ");
		return retVal;
	}

}