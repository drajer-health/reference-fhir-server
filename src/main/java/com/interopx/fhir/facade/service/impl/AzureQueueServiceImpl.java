/*******************************************************************************
 * Copyright (c) 2020-2022 InteropX LLC, www.interopx.com
 *
 * All Rights Reserved.
 *
 * The source code contained herein, and the packages generated from the source code, are and remain the property of InteropX, LLC. 
 * Access to this source code and the generated packages is granted to authorized customers of InteropX under an InteropX Software License Agreement (SLA).
 * Any use of the source code or the generated packages, not explicitly covered under the signed SLA, is prohibited.
 *******************************************************************************/
package com.interopx.fhir.facade.service.impl;

import java.io.IOException;
import java.time.Instant;
import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import javax.transaction.Transactional;

import org.hl7.fhir.r4.model.OperationOutcome;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.interopx.fhir.facade.dao.AzureQueueDao;
import com.interopx.fhir.facade.exception.MessagingException;
import com.interopx.fhir.facade.model.RequestResponseLog;
import com.interopx.fhir.facade.service.AzureQueueService;
import com.interopx.fhir.facade.util.AppConstants;
import com.interopx.fhir.facade.util.Status;

@Service
@Transactional
public class AzureQueueServiceImpl implements AzureQueueService {

	private static final Logger logger = LoggerFactory.getLogger(AzureQueueServiceImpl.class);

	@Value("${time.out.limit}")
	private long threadTimeOutTime;

	@Autowired
	AzureQueueDao azureQueueDao;

	/**
	 * Method to send the Request Payload to the Message Queue Using the Token
	 */
	@Override
	public void sendMessage(String requestPayload, String token) throws InterruptedException, IOException {
		logger.debug("Sending Message to Queue");
		try {
			if (token != null) {
				logger.debug("Token is not null: Sending message to Queue");
			}
		} catch (Exception e) {
			logger.error("Exception in sending messages to Message Queue {}", e);
			throw new MessagingException("500 Server Error: Could not process request", getOperationOutcome(500));
		}
	}

	@Override
	public void sendBulkMessage(String requestPayload, String practiceGuid, String token)
			throws InterruptedException, IOException {
		logger.debug("Sending Message to Queue");
		try {

			logger.debug("sendBulkMessage:::::{}", requestPayload);

		} catch (Exception e) {
			logger.error(" Exception in sending messages to Message Queue", e);
			throw new MessagingException("500 Server Error: Could not process request", getOperationOutcome(500));
		}

	}

	/**
	 * Method to save the Response Payload to database
	 */
	@Override
	public void saveResponse(RequestResponseLog responseLog) {
		azureQueueDao.updateResponse(responseLog);
	}

	/**
	 * Method to retrieve the Request against a requestId
	 */
	@Override
	public RequestResponseLog getRequestResponseLogById(String requestId) {
		return azureQueueDao.getRequestResponseLogById(requestId);
	}

	/**
	 * Method to save the Request to database
	 */
	@Override
	public void saveRequest(RequestResponseLog responseLog) {
		azureQueueDao.saveRequest(responseLog);
	}

	/**
	 * Method to asynchronously process the response
	 */
	@Override
	@Async("taskExecutor")
	public CompletableFuture<RequestResponseLog> getResponseFromQueue(String requestId) throws InterruptedException {
		logger.debug("{}{}{} Instantiating the new thread {} at {}", AppConstants.DEBUG_REQUEST_ID, requestId,
				AppConstants.DEBUG_FHIR_FACADE_EVENT, Thread.currentThread().getName(), Instant.now());
		boolean isReceived = false;
		RequestResponseLog requestResponseLog = null;
		Long deactiveTimeForThread = System.currentTimeMillis() + TimeUnit.MILLISECONDS.toMillis(threadTimeOutTime);
		logger.debug("deactiveTimeForThread=======>{}", deactiveTimeForThread);

		while (!isReceived) {
			logger.debug("----task2: {} ", Thread.currentThread().getName());
			Thread.sleep(2000);
			logger.debug("{}{}{} Checking the database for response at {}", AppConstants.DEBUG_REQUEST_ID, requestId,
					AppConstants.DEBUG_FHIR_FACADE_EVENT, Instant.now());
			requestResponseLog = azureQueueDao.getRequestResponseLogById(requestId);

			if (deactiveTimeForThread < System.currentTimeMillis()) {
				logger.debug("{}{}{} TRY TO TERMINATE ASYNC THREAD {} at {}", AppConstants.DEBUG_REQUEST_ID, requestId,
						AppConstants.DEBUG_FHIR_FACADE_EVENT, Thread.currentThread().getName(), Instant.now());

				if (Thread.currentThread().isAlive()) {
					logger.debug("{}{}{} THREAD GOT INTERRUPTED at {}", AppConstants.DEBUG_REQUEST_ID, requestId,
							AppConstants.DEBUG_FHIR_FACADE_EVENT, Instant.now());
					sleep(1000);
					Thread.currentThread().interrupt();
					if (Thread.currentThread().isInterrupted() == true) {
						logger.debug("{}{}{} THREAD GOT INTERRUPTED at {}", AppConstants.DEBUG_REQUEST_ID, requestId,
								AppConstants.DEBUG_FHIR_FACADE_EVENT, Instant.now());
						requestResponseLog.setStatus(Status.ABORTED.toString());
						requestResponseLog.setResponseTimestamp(new Date());
						azureQueueDao.updateStatus(requestId, Status.ABORTED.toString());
						break;
					}
				}
			}

			if (requestResponseLog.getStatus().equals(Status.COMPLETED.toString())) {
				logger.debug("{}{}{} Response received(COMPLETED). Stopped checking the database for response  at {}",
						AppConstants.DEBUG_REQUEST_ID, requestId, AppConstants.DEBUG_FHIR_FACADE_EVENT, Instant.now());

				isReceived = true;
				break;
			}
		}

		return CompletableFuture.completedFuture(requestResponseLog);
	}

	private void sleep(long ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			logger.debug(Thread.currentThread().getName(), "{}  is interrupted");
			logger.error("InterruptedException: ", e);
			Thread.currentThread().interrupt();
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
					.setDiagnostics("Server Error: Unable to send messages to Azure Messaging Queue");
		}
		return operationOutcome;
	}

	@Override
	public void updateStatus(String requestId, String status) {
		azureQueueDao.updateStatus(requestId, "Error");
	}

}
