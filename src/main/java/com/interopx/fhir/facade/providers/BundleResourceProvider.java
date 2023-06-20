package com.interopx.fhir.facade.providers;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.r4.model.Bundle.BundleType;
import org.hl7.fhir.r4.model.Bundle.HTTPVerb;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.OperationOutcome.IssueSeverity;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Parameters.ParametersParameterComponent;
import org.hl7.fhir.r4.model.Resource;
import org.hl7.fhir.r4.model.StringType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.interopx.fhir.facade.client.FhirValidatorClient;
import com.interopx.fhir.facade.exception.InvalidRequestException;
import com.interopx.fhir.facade.exception.RequestTimeOutException;
import com.interopx.fhir.facade.model.RequestResponseLog;
import com.interopx.fhir.facade.service.AzureQueueService;
import com.interopx.fhir.facade.service.FacadeConfigurationService;
import com.interopx.fhir.facade.util.AppConstants;
import com.interopx.fhir.facade.util.CommonUtil;
import com.interopx.fhir.facade.util.ParameterResourceUtil;
import com.interopx.fhir.facade.util.ResourceTypes;
import com.interopx.fhir.facade.util.SearchParameterMap;
import com.interopx.fhir.facade.util.Status;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jaxrs.server.AbstractJaxRsResourceProvider;
import ca.uhn.fhir.rest.annotation.Transaction;
import ca.uhn.fhir.rest.annotation.TransactionParam;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;

@Component
public class BundleResourceProvider extends AbstractJaxRsResourceProvider<Bundle> {

	private static final Logger logger = LoggerFactory.getLogger(BundleResourceProvider.class);
	private static final String userLevel = "user/";
	private static final String WRITE = ".write";

	@Autowired
	private FhirValidatorClient fhirValidatorClient;
	@Autowired
	private FacadeConfigurationService facadeConfigurationService;
	@Autowired
	FhirContext fhirContext;

	@Autowired
	AzureQueueService service;

	public BundleResourceProvider(FhirContext fhirContext) {
		super(fhirContext);
	}

	@Override
	public Class<Bundle> getResourceType() {
		return Bundle.class;
	}

	/**
	 * The transaction action is among the most challenging parts of the FHIR
	 * specification to implement. It allows the user to submit a bundle containing
	 * a number of resources to be created/updated/deleted as a single atomic
	 * transaction. Invoke this method (this would be invoked using an HTTP POST,
	 * with the resource in the POST body): http://<server name>/<context>/fhir/
	 * 
	 * @param theBundle
	 * @return
	 */
	@Transaction
	public Bundle createBundle(@TransactionParam Bundle theBundle, HttpServletRequest request,
			RequestDetails theRequestDetails) throws Exception {
		logger.debug("Request received on FHIR FACADE with Bundle create operation at {}", Instant.now());
		Bundle retVal = null;
		String token = CommonUtil.getAccessTokenFronRequestHeader(request);
		if (request.getHeader("Content-Type") != null
				&& request.getHeader("Content-Type").equals("application/fhir+json")) {
			if (theBundle.hasType()) {
				if (theBundle.getType().toString().equalsIgnoreCase(BundleType.TRANSACTION.toString())) {
					String requestId = CommonUtil.generateId().toString();
					Map<String, String> pathParams = CommonUtil.getPathParams(request.getRequestURL());
					pathParams.put("queryParameters", "Bundle");
					pathParams.put("requestId", requestId);
					pathParams.put("resourceType", "Bundle");
					Parameters parameters = ParameterResourceUtil.generateParameterResource(new SearchParameterMap(),
							pathParams, theRequestDetails);
					Bundle filteredBundle = validateBundleResourceToCreate(theRequestDetails, theBundle, parameters);
					if(parameters != null && (parameters.hasParameter(ResourceTypes.IMMUNIZATION.toString())
							|| parameters.hasParameter(ResourceTypes.ALLERGYINTOLERANCE.toString()) 
							|| parameters.hasParameter(ResourceTypes.CONDITION.toString()))) {
						String parameterResource = fhirContext.newJsonParser().setPrettyPrint(true)
								.encodeResourceToString(parameters);
						logger.debug("Generated Parameter Resource: {}", parameterResource);
						logger.debug("{}{}{} Sending the message to OutQueue at {}", AppConstants.DEBUG_REQUEST_ID,
								requestId, AppConstants.DEBUG_FHIR_FACADE_EVENT, Instant.now());
						service.sendMessage(parameterResource, token);
						logger.debug("{}{}{} Message sent to OutQueue at {}", AppConstants.DEBUG_REQUEST_ID, requestId,
								AppConstants.DEBUG_FHIR_FACADE_EVENT, Instant.now());
						// Saving the request to database
						RequestResponseLog requestResponseLog = CommonUtil.generateRequestLog(requestId,
								pathParams.get("practiceId"), ResourceTypes.BUNDLE.toString());
						service.saveRequest(requestResponseLog);
						CompletableFuture<RequestResponseLog> requestResponseObj = service.getResponseFromQueue(requestId);
						RequestResponseLog responseLog = requestResponseObj.get();
						if (responseLog != null) {
							if (responseLog.getStatus().equals(Status.COMPLETED.toString())) {
								Bundle responseBundle = (Bundle) fhirContext.newJsonParser()
										.parseResource(responseLog.getPayload());
								if (responseBundle.hasEntry()) {
									Object obj = getBundleFromResponseAndFiltered(responseBundle, filteredBundle);
									if (obj instanceof OperationOutcome) {
										service.updateStatus(requestId, Status.ERROR.toString());
										throw new InvalidRequestException("400 Bad Request: Unable to process request",
												(OperationOutcome) obj);
									} else {
										retVal = (Bundle) obj;
									}

								}

							} else if (responseLog != null && responseLog.getStatus().equals(Status.ABORTED.toString())) {
								throw new RequestTimeOutException("Unable to Process the Request.Request Timeout");
							}
						}
					}
					else {
						//addResourceToBundle(filteredBundle, createOperationOutcome("The request bundle does not contain any validate resources"));
						retVal = filteredBundle;
					}
					logger.debug("{}{}{} Sending the response at {}", AppConstants.DEBUG_REQUEST_ID, requestId,
							AppConstants.DEBUG_FHIR_FACADE_EVENT, Instant.now());
				} else {
					throw new UnprocessableEntityException("Bundle.type should be transaction.");
				}
			} else {
				throw new UnprocessableEntityException("Bundle should have type transaction.");
			}
		} else {
			throw new UnprocessableEntityException("Invalid header values!");
		}
		return retVal;
	}

	/**
	 * This method adds the resource into the bundle
	 * 
	 * @param bundle
	 * @param resource
	 */
	public void addResourceToBundle(Bundle bundle, Resource resource) {
		try {
			BundleEntryComponent entryComponent = new BundleEntryComponent();
			entryComponent.setResource(resource);
			bundle.addEntry(entryComponent);
		} catch (Exception e) {
			logger.error("Exception for addResourceToBundle() in BundleResourceProvider class ", e);
		}
	}

	/**
	 * This method creates ParametersParameterComponent for name and resoure and
	 * adds it into Parameters resource
	 * 
	 * @param theParameters
	 * @param name
	 * @param resource
	 */
	public void addParameterComponentToParameters(Parameters theParameters, String name, Resource resource) {
		try {
			ParametersParameterComponent parameterComponent = new ParametersParameterComponent();
			parameterComponent.setName(name);
			parameterComponent.setResource(resource);
			theParameters.addParameter(parameterComponent);
		} catch (Exception e) {
			logger.error("Exception for addParameterComponentToParameters() in BundleResourceProvider class ", e);
		}
	}

	/**
	 * This method creates operation outcome for the provided message
	 * 
	 * @param messgae
	 * @param resourceName
	 * @return
	 */
	public Resource createOperationOutcome(String messgae) {
		try {
			if (StringUtils.isNotBlank(messgae)) {
				OperationOutcome outcome = new OperationOutcome();
				outcome.addIssue().setSeverity(IssueSeverity.ERROR).setDiagnostics(messgae);
				return outcome;
			}
		} catch (Exception e) {
			logger.error("Exception for createOperationOutcome() in BundleResourceProvider class ", e);
		}
		return null;
	}

	/**
	 * This method validates each entry resource against scopes and us core 3.1.1
	 * compliance
	 * 
	 * @param theBundle
	 * @return
	 */
	public Bundle validateBundleResourceToCreate(RequestDetails theRequestDetails, Bundle theBundle,
			Parameters parameters) {
		Bundle filteredBundle = new Bundle();
		try {
			if (theBundle != null && !theBundle.isEmpty()) {
				if (theBundle.hasEntry()) {
					Map<String, String[]> tokenParam = theRequestDetails.getParameters();
					if (tokenParam.containsKey("scope") && tokenParam.get("scope") != null) {
						String scopes = tokenParam.get("scope")[0];
						ParametersParameterComponent parameterComponent = new ParametersParameterComponent();
						parameterComponent.setName("Requested_Operation");
						parameterComponent.setValue(new StringType("Create"));
						parameters.addParameter(parameterComponent);
						for (BundleEntryComponent component : theBundle.getEntry()) {
							if (component.hasRequest() && component.getRequest().hasMethod()) {
								if (component.getRequest().getMethod().equals(HTTPVerb.POST)) {
									if (component.hasResource()) {
										String resourceName = component.getResource().getResourceType().name();
										if ((resourceName.equalsIgnoreCase(ResourceTypes.IMMUNIZATION.toString())
												&& scopes.contains(
														userLevel + ResourceTypes.IMMUNIZATION.toString() + WRITE))
												|| (resourceName.equalsIgnoreCase(ResourceTypes.CONDITION.toString())
														&& scopes.contains(
																userLevel + ResourceTypes.CONDITION.toString() + WRITE))
												|| (resourceName
														.equalsIgnoreCase(ResourceTypes.ALLERGYINTOLERANCE.toString())
														&& scopes.contains(
																userLevel + ResourceTypes.ALLERGYINTOLERANCE.toString()
																		+ WRITE))) {
											Resource theResource = component.getResource();
											if(facadeConfigurationService.getFhirValidationStatus()) {
												logger.debug("fhir validation is enabled");
												if (fhirValidatorClient.validateResource(theResource)) {
													addParameterComponentToParameters(parameters, resourceName,
															theResource);
												} else {
													String message = new StringBuilder().append("Requested Resource ")
															.append(theResource.getId())
															.append(" does not match with US Core 3.1.1 compliance")
															.toString();
													logger.error("Error code 400 : {}", message);
													addResourceToBundle(filteredBundle, createOperationOutcome(message));
												}
											}
											else {
												logger.debug("fhir validation is disabled");
												addParameterComponentToParameters(parameters, resourceName,
														theResource);
											}

										} else {
											String message = new StringBuilder().append(
													"Authentication was provided, but the authenticated user is not permitted to perform the requested operation for ")
													.append(resourceName).toString();
											logger.error("Error code 401 Forbidden: {} ", message);
											addResourceToBundle(filteredBundle, createOperationOutcome(message));
										}

									} else {
										logger.info(
												"BundleEntryComponent doesn't have resource. Hence skipping the BundleEntryComponent.");
									}
								} else {
									logger.info(
											"BundleEntryComponent request.method is not POST. Hence skipping the BundleEntryComponent.");
								}
							}
						}
					}
				} else {
					throw new UnprocessableEntityException("Bundle doesn't contain any entries to process");
				}
			}
		} catch (Exception e) {
			logger.error("Exception in createBundle of BundleServiceProvider ", e);
		}
		return filteredBundle;
	}

	/**
	 * This method removes parameters resource from responseBundle and returns
	 * OperationOutcome from responseBundle if any and adds filteredBundle entries
	 * to responseBundle
	 * 
	 * @param responseBundle
	 * @param filteredBundle
	 * @return
	 */
	public Object getBundleFromResponseAndFiltered(Bundle responseBundle, Bundle filteredBundle) {
		if (responseBundle != null && !responseBundle.isEmpty()) {
			if (responseBundle.hasEntry()) {
				try {
					for (BundleEntryComponent component : responseBundle.getEntry()) {
						try {
							if (component.getResource() instanceof Parameters) {
								component.setResource(null);
							}
							if (component.getResource() instanceof OperationOutcome) {
								return (OperationOutcome) component.getResource();
							}
						} catch (Exception e) {
							logger.error(
									"Exception for getBundleFromResponseAndFiltered() in  BundleResourceProvider class while iterating bundle entries {}",
									e);
						}
					}
					if (filteredBundle != null && !filteredBundle.isEmpty()) {
						for (BundleEntryComponent component : filteredBundle.getEntry()) {
							try {
								responseBundle.addEntry(component);
							} catch (Exception e) {
								logger.error(
										"Exception for getBundleFromResponseAndFiltered() in  BundleResourceProvider class while iterating bundle entries {}",
										e);
							}
						}
					}
				} catch (Exception e) {
					logger.error(
							"Exception for getBundleFromResponseAndFiltered() in  BundleResourceProvider class {} ", e);
				}
			}
		}
		return responseBundle;
	}
}
