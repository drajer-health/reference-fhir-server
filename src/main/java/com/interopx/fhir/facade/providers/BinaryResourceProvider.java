package com.interopx.fhir.facade.providers;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hl7.fhir.r4.model.Binary;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.IdType;
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
import com.interopx.fhir.facade.util.ResourceTypes;
import com.interopx.fhir.facade.util.Status;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jaxrs.server.AbstractJaxRsResourceProvider;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.api.server.RequestDetails;

/**
 * This class handles request for Binary Resource. It generates a parameter
 * resource based on read/search request types send/receive messages to and from
 * Fhir Server, processes and sends back a resource bundle to client
 * 
 * @author xyram
 *
 */
@Component
public class BinaryResourceProvider extends AbstractJaxRsResourceProvider<Binary> {

	private static final Logger logger = LoggerFactory.getLogger(BinaryResourceProvider.class);
	@Autowired
	FhirContext fhirContext;

	@Autowired
	AzureQueueService service;

	@Autowired
	InteropxRequestQReceiver interopXReceiver;

	public BinaryResourceProvider(FhirContext fhirContext) {
		super(fhirContext);
	}

	@Override
	public Class<Binary> getResourceType() {
		return Binary.class;
	}

	/**
	 * The "@Read" annotation indicates that this method supports the read
	 * operation. This operation retrieves a resource by ID. Example URL to invoke
	 * this method: http://<server name>/<context>/fhir/Binary/1
	 * 
	 * @param request
	 * @param response
	 * @param theRequestDetails
	 * @param theId
	 * @return
	 * @throws Exception
	 */
	@Read
	public Binary readOrVread(HttpServletRequest request, HttpServletResponse response,
			RequestDetails theRequestDetails, @IdParam IdType theId) throws Exception {
		logger.debug(" Request received on FHIR FACADE with Binary Read operation :{} at {}", theId.getIdPart(),
				Instant.now());
		Binary binary = null;
		try {
			String token = CommonUtil.getAccessTokenFronRequestHeader(request);
			String requestId = CommonUtil.generateId().toString();
			Map<String, String> pathParams = CommonUtil.getPathParams(request.getRequestURL());
			pathParams.put("requestId", requestId);
			pathParams.put("resourceType", ResourceTypes.BINARY.toString());
			logger.debug("{}{}{} Constructing the Parrameter resource at {}", AppConstants.DEBUG_REQUEST_ID, requestId,
					AppConstants.DEBUG_FHIR_FACADE_EVENT, Instant.now());
			Parameters parameter = ParameterResourceUtil.generateReadParameterResource(pathParams, theId.getIdPart(),
					theRequestDetails);
			logger.debug("{}{}{} Completed constructing the Parrameter resource at {}", AppConstants.DEBUG_REQUEST_ID,
					requestId, AppConstants.DEBUG_FHIR_FACADE_EVENT, Instant.now());
			String parameterResource = fhirContext.newJsonParser().setPrettyPrint(true)
					.encodeResourceToString(parameter);
			logger.debug("Generated Parameter Resource: {}", parameterResource);
			logger.debug("{}{}{} Sending the message to OutQueue at {}", AppConstants.DEBUG_REQUEST_ID, requestId,
					AppConstants.DEBUG_FHIR_FACADE_EVENT, Instant.now());
			service.sendMessage(parameterResource, token);
			logger.debug("{}{}{} Message sent to OutQueue at {}", AppConstants.DEBUG_REQUEST_ID, requestId,
					AppConstants.DEBUG_FHIR_FACADE_EVENT, Instant.now());
			// Saving the request to database
			RequestResponseLog requestResponseLog = CommonUtil.generateRequestLog(requestId,
					pathParams.get("practiceId"), ResourceTypes.BINARY.toString());
			service.saveRequest(requestResponseLog);
			CompletableFuture<RequestResponseLog> requestResponseObj = service.getResponseFromQueue(requestId);
			RequestResponseLog responseLog = requestResponseObj.get();
			if (responseLog != null) {
				if (responseLog.getStatus().equals(Status.COMPLETED.toString())) {
					Bundle bundle = (Bundle) fhirContext.newJsonParser().parseResource(responseLog.getPayload());
					Resource resource = ResourceUtil.getResourceFromBundleForRead(bundle,
							ResourceTypes.BINARY.toString());
					if (resource instanceof OperationOutcome) {
						service.updateStatus(requestId, Status.ERROR.toString());
						throw new InvalidRequestException("400 Bad Request: Unable to process request",
								(OperationOutcome) resource);
					} else {
						binary = (Binary) resource;
					}
				} else if (responseLog != null && responseLog.getStatus().equals(Status.ABORTED.toString())) {
					throw new RequestTimeOutException("Unable to Process the Request.Request Timeout");
				}
			}
			logger.debug("{}{}{} Sending the response at {}", AppConstants.DEBUG_REQUEST_ID, requestId,
					AppConstants.DEBUG_FHIR_FACADE_EVENT, Instant.now());
		} catch (Exception e) {
			logger.error("Exception in readOrVread() of BinaryResourceProvider class: ");
			throw e;
		}
		logger.debug("End readOrVread() in BinaryResourceProvider class ");
		return binary;
	}

}
