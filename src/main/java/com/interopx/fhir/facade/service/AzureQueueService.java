/*******************************************************************************
 * Copyright (c) 2020-2022 InteropX LLC, www.interopx.com
 *
 * All Rights Reserved.
 *
 * The source code contained herein, and the packages generated from the source code, are and remain the property of InteropX, LLC. 
 * Access to this source code and the generated packages is granted to authorized customers of InteropX under an InteropX Software License Agreement (SLA).
 * Any use of the source code or the generated packages, not explicitly covered under the signed SLA, is prohibited.
 *******************************************************************************/
package com.interopx.fhir.facade.service;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import com.interopx.fhir.facade.model.RequestResponseLog;

public interface AzureQueueService {
	
	void sendMessage(String requestPayload, String token) throws InterruptedException, IOException;	
	void sendBulkMessage(String requestPayload, String practiceGuid, String token) throws InterruptedException, IOException;	
	void saveRequest(RequestResponseLog responseLog);
	void saveResponse(RequestResponseLog responseLog);	
	RequestResponseLog getRequestResponseLogById(String requestId);
	CompletableFuture<RequestResponseLog> getResponseFromQueue(String requestId) throws InterruptedException;
	
	void  updateStatus(String requestId,String status);

}
