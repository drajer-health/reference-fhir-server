package com.interopx.fhir.facade.providers;

import static com.interopx.fhir.facade.util.AppConstants.ACCEPT;
import static com.interopx.fhir.facade.util.AppConstants.CONTENT_LOCATION;
import static com.interopx.fhir.facade.util.AppConstants.CONTENT_TYPE;
import static com.interopx.fhir.facade.util.AppConstants.DATE_FORMAT;
import static com.interopx.fhir.facade.util.AppConstants.EXPIRES;
import static com.interopx.fhir.facade.util.AppConstants.EXPORT;
import static com.interopx.fhir.facade.util.AppConstants.PREFER;
import static com.interopx.fhir.facade.util.AppConstants.RESPOND_ASYNC;
import static com.interopx.fhir.facade.util.AppConstants.TIME_ZONE;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.CompletableFuture;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hl7.fhir.r4.model.Group;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.OperationOutcome.IssueSeverity;
import org.hl7.fhir.r4.model.Parameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.interopx.fhir.facade.model.BulkDataRequest;
import com.interopx.fhir.facade.service.AzureQueueService;
import com.interopx.fhir.facade.service.BulkDataRequestService;
import com.interopx.fhir.facade.util.CommonUtil;
import com.interopx.fhir.facade.util.AppConstants;
import com.interopx.fhir.facade.util.Parameter;
import com.interopx.fhir.facade.util.ParameterResourceUtil;
import com.interopx.fhir.facade.util.SearchParameterMap;
import com.interopx.fhir.facade.util.Status;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jaxrs.server.AbstractJaxRsResourceProvider;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.DateAndListParam;

/**
 * This class creates a parameter resource for bulk data and sends it across to
 * Fhir Server Returns the content polling export location in header
 * 
 * @author xyram
 *
 */
@Component
public class GroupResourceProvider extends AbstractJaxRsResourceProvider<Group> {

	private static final Logger logger = LoggerFactory.getLogger(GroupResourceProvider.class);
	public static final String VERSION_ID = "1";
	private static final String GROUP = "Group";

	@Autowired
	AzureQueueService azureQueueService;

	@Autowired
	FhirContext fhirContext;

	@Autowired
	BulkDataRequestService bdrService;
	
	@Autowired
	CommonUtil commonUtil;

	public GroupResourceProvider(FhirContext fhirContext) {
		super(fhirContext);
	}

	/**
	 * The getResourceType method comes from IResourceProvider, and must be
	 * overridden to indicate what type of resource this provider supplies.
	 */
	@Override
	public Class<Group> getResourceType() {
		return Group.class;
	}

	/**
	 * This method receives the groupId and generates a parameter resource and send
	 * it across to fhir server Sends a export polling location as a response
	 * 
	 * @param grpId
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@Operation(name = EXPORT, idempotent = true, manualResponse = true, global = true)
	public void patientTypeOperation(@IdParam IdType grpId, @OperationParam(name = "_type") String type,
			@OperationParam(name = "_since") DateAndListParam theDate, HttpServletRequest request,
			HttpServletResponse response,  RequestDetails theRequestDetails) throws Exception {
		String paramsMessage = "";
logger.debug("Request received for Group $export by id :{} {}" , grpId , paramsMessage);
		logger.debug("Received request to export with _type:::::{}",type);
		FhirContext ctx = FhirContext.forR4();
		Map<String, String> map = new HashMap<>();
		Map<String,String[]> requestParams = theRequestDetails.getParameters();
		try {
			String token = CommonUtil.getAccessTokenFronRequestHeader(request);
			logger.debug("Received Token::::{}",token);
			Enumeration<String> headerNames = request.getHeaderNames();
			String practiceId = CommonUtil.getPracticeId(request.getRequestURL().toString());
			String resourcesPerFile = "100";
			String typeValue =null;
			while (headerNames.hasMoreElements()) {
				String key = (String) headerNames.nextElement();
				String value = request.getHeader(key);
				map.put(key, value);
			}
			if (map.get(PREFER.toLowerCase()) != null && map.get(ACCEPT.toLowerCase()) != null) {
				if (map.get(PREFER.toLowerCase()).equals(RESPOND_ASYNC.toLowerCase())
						&& map.get(ACCEPT.toLowerCase()).equals(CONTENT_TYPE)) {
					String groupId = grpId.getIdPart();
					String requestId = CommonUtil.generateId().toString();
					
					logger.debug("RequestParameter contains the Scope::::{}",requestParams.containsKey("scope"));
					if(requestParams.containsKey("scope") && type == null) {
						logger.debug("_type is null. So adding all the resources from Scopes received");
						String scope = requestParams.get("scope")[0];
						List<String> scopes = Arrays.asList(scope.split(" "));
						List<String> typeList = new ArrayList<>();
						for(String scopeList: scopes) {
							if(commonUtil.getResourceNameByScope(scopeList) != null && !scopeList.equals("system/*.read")) {
								typeList.add(commonUtil.getResourceNameByScope(scopeList));	
							} else if(scopeList.equals("system/*.read")) {
								Map<String,String> scopesMap = CommonUtil.scopeToResourceMap;
								scopesMap.forEach((k,v)->{
									typeList.add(v);
								});
							}
						}
						typeValue = String.join(",",typeList);
					} else if(requestParams.containsKey("scope") && type!=null){
						logger.debug("_type is not null. So we use the same value received in request");
						String[] requestedResources = type.split(",");
						for(String resourceName:requestedResources) {
							String scope = requestParams.get("scope")[0];
							if(!scope.contains("system/"+resourceName+".read")) {
								logger.error("Error code 401 : Requested Resources are not Authorized");
								response.setContentType(Constants.CT_JSON);
								response.setStatus(401);
								org.hl7.fhir.r4.model.OperationOutcome outcome = new org.hl7.fhir.r4.model.OperationOutcome();
								outcome.addIssue().setSeverity(IssueSeverity.ERROR)
										.setDiagnostics("Requested Resources are not Authorized");
								String results = ctx.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome);
								response.getWriter().println(results);
								return;
							}
						}
						typeValue = type;
					}
					
					Map<String, Object> requestMap = new HashMap<>();
					requestMap.put(Parameter.GROUP_ID.toString(), groupId);
					requestMap.put(Parameter.REQUESTED_OPERATION.toString(), "export");
					requestMap.put(Parameter.REQUEST_URL.toString(), request.getRequestURL().toString());
					requestMap.put(Parameter.REQUEST_ID.toString(), requestId);
					requestMap.put(Parameter.RESOURCETYPE.toString(), GROUP);
					requestMap.put(Parameter.RESOURCES_PER_FILE.toString(), resourcesPerFile);
					requestMap.put(Parameter.TYPE.toString(), typeValue);
					if(requestParams.containsKey("vendorId") && requestParams.get("vendorId") != null) {
						logger.debug("Adding vendorId:{} to Parameters List",requestParams.get("vendorId")[0]);
						requestMap.put("vendorId", requestParams.get("vendorId")[0]);	
					} 
					requestMap.put("workflow", "backend");
					SearchParameterMap paramMap = new SearchParameterMap();
					paramMap.add(Parameter.SP_DATE.toString(), theDate);
					requestMap.put(Parameter.SINCE.toString(), paramMap);

					if (groupId != null) {
						Parameters parameter = ParameterResourceUtil.generateGroupParameterResource(requestMap);
						fhirContext = FhirContext.forR4();
						String parameterResource = fhirContext.newJsonParser().setPrettyPrint(true)
								.encodeResourceToString(parameter);
						logger.debug("Generated Parameter Resource: {}", parameterResource);
						if(requestParams.containsKey("practiceGuid") && requestParams.get("practiceGuid") != null) {
							String practiceGuid = requestParams.get("practiceGuid")[0];
							logger.debug("Using PracticeGuid:::::{} to send the message",practiceGuid);
							azureQueueService.sendBulkMessage(parameterResource, practiceGuid,token);
						} 

						BulkDataRequest bdr = new BulkDataRequest();
						bdr.setJobStatus("In-Progress");
						bdr.setLastUpdatedDate(new Date());
						bdr.setProcessedFlag(false);
						bdr.setRequest(request.getRequestURL().toString());
						bdr.setRequestId(requestId);
						bdr.setGroupId(groupId);
						bdr.setOrgId(theRequestDetails.getTenantId());
						bdr.setStatusMessage("Extraction Started");

						bdrService.saveBulkDataRequest(bdr);
						// Get the response from database
						CompletableFuture<BulkDataRequest> responseObj = bdrService.getResponseFromQueue(requestId);
						BulkDataRequest bulkDataRequest = responseObj.get();
						if (bulkDataRequest != null && bulkDataRequest.getJobId() != null) {
							StringBuilder stringBuffer = new StringBuilder();
							String uri = "";
							logger.debug("Reading Server URL from DB::::{}",uri);

							response.setStatus(Constants.STATUS_HTTP_202_ACCEPTED);
							GregorianCalendar cal = new GregorianCalendar();
							cal.setTime(new Date());
							cal.setTimeZone(TimeZone.getTimeZone(TIME_ZONE));
							cal.add(Calendar.DATE, 10);
							String o = new SimpleDateFormat(DATE_FORMAT).format(cal.getTime());
							response.setContentType(Constants.CT_JSON);
							response.setHeader(EXPIRES, o);
							response.setHeader(AppConstants.RETRYAFTER, AppConstants.ONETWENTY);

							response.setHeader(CONTENT_LOCATION,
									uri + AppConstants.SINGLEFORWARDSLASH + "r4"
											+ AppConstants.SINGLEFORWARDSLASH + practiceId + AppConstants.SINGLEFORWARDSLASH
											+ "$export-poll-location?job_id=" + bulkDataRequest.getJobId());

							response.setContentType(Constants.CT_JSON);
						} else if (bulkDataRequest != null && bulkDataRequest.getJobStatus().equals(Status.ABORTED.toString())) {
							logger.error("Error code 408 : Request Time Out! ");
							response.setContentType(Constants.CT_JSON);
							response.setStatus(408);
							org.hl7.fhir.r4.model.OperationOutcome outcome = new org.hl7.fhir.r4.model.OperationOutcome();
							outcome.addIssue().setSeverity(IssueSeverity.ERROR)
									.setDiagnostics("Unable to Process the Request.Request Timeout");
							String results = ctx.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome);
							response.getWriter().println(results);
						}

					}
				} else {
					logger.error("Error code 400 : Invalid header values! ");
					response.setContentType(Constants.CT_JSON);
					response.setStatus(Constants.STATUS_HTTP_400_BAD_REQUEST);
					org.hl7.fhir.r4.model.OperationOutcome outcome = new org.hl7.fhir.r4.model.OperationOutcome();
					outcome.addIssue().setSeverity(IssueSeverity.ERROR).setDiagnostics("Invalid header values!");
					String results = ctx.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome);
					response.getWriter().println(results);
				}

			} else {
				logger.error("Error code 422 : Prefer or Accept Header is missing! ");
				response.setContentType(Constants.CT_JSON);
				response.setStatus(Constants.STATUS_HTTP_422_UNPROCESSABLE_ENTITY);
				org.hl7.fhir.r4.model.OperationOutcome outcome = new org.hl7.fhir.r4.model.OperationOutcome();
				outcome.addIssue().setSeverity(IssueSeverity.ERROR)
						.setDiagnostics("Prefer or Accept Header is missing!");
				String results = ctx.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome);
				response.getWriter().println(results);
			}
		} catch(InterruptedException ie) {
			logger.error("Thread interrupted ",ie);
		}catch (Exception e) {
			logger.error("Error in processing request ", e);
		}

	}

}