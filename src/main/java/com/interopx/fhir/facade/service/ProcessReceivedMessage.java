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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.azure.messaging.servicebus.ServiceBusReceivedMessageContext;

public class ProcessReceivedMessage {
	
	private static final Logger logger = LoggerFactory.getLogger(ProcessReceivedMessage.class);
	
	public static String processMessage(ServiceBusReceivedMessageContext context) {
		logger.debug("This is received Message::::: {}",context.getMessage().getBody().toString());
		
		return null;
		
	}
	

}
