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

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.StringType;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.azure.messaging.servicebus.ServiceBusClientBuilder;
import com.azure.messaging.servicebus.ServiceBusReceivedMessage;
import com.azure.messaging.servicebus.ServiceBusReceiverAsyncClient;
import com.interopx.fhir.facade.dao.QueueConfigurationDao;
import com.interopx.fhir.facade.model.RequestResponseLog;
import com.interopx.fhir.facade.service.AzureQueueService;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import reactor.core.Disposable;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

/*
 * This file is Not Part of The JAR.
 * Receiving interopX Team should be writing this code.
 * Queue Name and it's credentials have been hard coded. Queue Name : "in_queue_global"
 * Every single Message received will be available in the "processMessage" API , where the code to further process the same can be invoked.
 */
@Component
@Scope("prototype")
public class InteropxRequestQReceiver {
	private final Logger logger = LoggerFactory.getLogger(InteropxRequestQReceiver.class);

	private static final Integer COUNTDOWN_LATCH_TIMEOUT = 20;

	@Autowired
	private AzureQueueService service;

	@Autowired
	QueueConfigurationDao queueConfigurationDao;

	@Autowired
	FhirContext fhirR4Context;

	@Autowired
	BulkDataProcessor queueProcessor;

	@Async("taskExecutor")
	public void receiveMessages(String inQueueName, String inQueueConnectionString) {
		ServiceBusReceiverAsyncClient receiver = null;
		Disposable subscription = null;
		try {
			logger.debug("FHIR FACADE|EventName==>>Started Listener thread - for InQueue {}. Thread id {} at {}", inQueueName, Thread.currentThread().getId(), Instant.now());
			AtomicBoolean sampleSuccessful = new AtomicBoolean(true);
			CountDownLatch countdownLatch = new CountDownLatch(1);
			receiver = new ServiceBusClientBuilder().connectionString(inQueueConnectionString).receiver()
					.disableAutoComplete().queueName(inQueueName).buildAsyncClient();

			Scheduler scheduler = Schedulers.boundedElastic();

			ServiceBusReceiverAsyncClient finalReceiver = receiver;
			subscription = receiver.receiveMessages().publishOn(scheduler).flatMap(message -> {
				boolean messageProcessed = processMessage(message);
				if (messageProcessed) {
					return finalReceiver.complete(message);
				} else {
					return finalReceiver.abandon(message);
				}
			}).subscribe(
					(ignore) -> logger.debug(
							"FHIR FACADE|EventName==>>Message processed Successfully at {}", Instant.now()),
					error -> {
						logger.error("FHIR FACADE|EventName==>>Error in Processing the Message at {} "
								, Instant.now());
						sampleSuccessful.set(false);
					});

			boolean await = countdownLatch.await(COUNTDOWN_LATCH_TIMEOUT, TimeUnit.SECONDS);
			logger.debug("Countdown latch await->{}", await);

		} catch (InterruptedException e) {
			logger.error(e.getMessage(), e);
			Thread.currentThread().interrupt();
		} catch (Exception e) {
			logger.error("Exception in receiveMessages() of InteropXReceiver class ", e);
		}

	}

	/*
	 * Every single Message received will be available in the below API , where the
	 * code to further process the same can be invoked.
	 */

	private boolean processMessage(ServiceBusReceivedMessage message) {
		try {
			logger.debug("Processing message# --> {}", message.getSequenceNumber());
			logger.debug("message Body# --> {}", message.getBody().toString());

			String practiceGuid = message.getSubject();
			logger.debug("Received PracticeGuid from Message::::{}", practiceGuid);
			String uncompressed = "";
			logger.debug("UnCompressed String:::::{}", uncompressed);

			saveResponseLog(uncompressed);

			logger.debug("Received message size: {}", uncompressed.length());
			logger.debug("Received message content : {}", uncompressed);
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu/MM/dd HH:mm:ss");
			LocalDateTime now = LocalDateTime.now();
			logger.debug("Current Time: {}", dtf.format(now));
			return true;
		} catch (Exception e) {
			logger.error("Exception in processMessage() of InteropXReceiver class ", e);
		}
		return false;
	}

	/**
	 * Method to store the response to database
	 * 
	 */
	// @Async("taskExecutor")
	private void saveResponseLog(String responsePayload) {
		try {
			if (responsePayload != null) {
				JSONObject obj = new JSONObject(responsePayload);
				String requestId = null;
				if (obj.has("resourceType")) {
					String nameOfResource = obj.getString("resourceType");
					if (nameOfResource.equalsIgnoreCase(ResourceTypes.BUNDLE.toString())) {
						IParser jsonParser = fhirR4Context.newJsonParser();
						Bundle bundle = jsonParser.parseResource(Bundle.class, responsePayload);
						if (bundle.hasEntry()) {
							for (BundleEntryComponent component : bundle.getEntry()) {
								if (component.getResource() instanceof Parameters) {
									Parameters bundleParameters = (Parameters) component.getResource();
									if (bundleParameters.hasParameter("requestId")) {
										StringType requestValue = (StringType) bundleParameters
												.getParameter("requestId");
										requestId = requestValue.getValue();
									}
									break;
								}
							}
						}
						// Save the response from Queue into database
						RequestResponseLog requestResponseLog = service.getRequestResponseLogById(requestId);
						if (requestResponseLog != null) {
							requestResponseLog.setStatus(Status.COMPLETED.toString());
							requestResponseLog.setPayload(responsePayload);
							requestResponseLog.setTimestamp(new Date());
							requestResponseLog.setResponseTimestamp(new Date());
							logger.debug("{}{}{} Saving the response from InQueue into database at {}",
									AppConstants.DEBUG_REQUEST_ID, requestId, AppConstants.DEBUG_FHIR_FACADE_EVENT,
									Instant.now());
							service.saveResponse(requestResponseLog);
							logger.debug("{}{}{} Completed Saving the response from InQueue into database at {}",
									AppConstants.DEBUG_REQUEST_ID, requestId, AppConstants.DEBUG_FHIR_FACADE_EVENT,
									Instant.now());
						}
					}
				} else {
					// Process the Response resource for bulk data response
					queueProcessor.processMessage(responsePayload);
				}
			}
		} catch (Exception e) {
			logger.error("Exception in updating the response status to Database: {} ", e);
		}
	}

	/**
	 * Method to generate the Operation Outcome
	 * 
	 * @param statusCode
	 * @return
	 */
	public static OperationOutcome getOperationOutcome(int statusCode) {
		OperationOutcome operationOutcome = new OperationOutcome();
		if (statusCode == 500) {
			operationOutcome.addIssue().setSeverity(OperationOutcome.IssueSeverity.ERROR)
					.setCode(OperationOutcome.IssueType.PROCESSING)
					.setDiagnostics("Server Error: Unable to receive messages from Azure Messaging Queue");
		}
		return operationOutcome;
	}
}