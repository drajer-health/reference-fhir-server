/*******************************************************************************
 * Copyright (c) 2020-2022 InteropX LLC, www.interopx.com
 *
 * All Rights Reserved.
 *
 * The source code contained herein, and the packages generated from the source code, are and remain the property of InteropX, LLC. 
 * Access to this source code and the generated packages is granted to authorized customers of InteropX under an InteropX Software License Agreement (SLA).
 * Any use of the source code or the generated packages, not explicitly covered under the signed SLA, is prohibited.
 *******************************************************************************/
package com.interopx.fhir.facade.configuration;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.interopx.fhir.facade.model.AuthConfiguration;
import com.interopx.fhir.facade.model.QueueConfiguration;
import com.interopx.fhir.facade.service.AuthConfigurationService;
import com.interopx.fhir.facade.service.QueueConfigurationService;
import com.interopx.fhir.facade.util.CommonUtil;
import com.interopx.fhir.facade.util.InteropxRequestQReceiver;
/**
 * This class is responsible for running the Queue message receiver on start up
 * @author xyram
 *
 */
@Component
public class RunAfterStartUp {

	@Autowired
	private ApplicationContext context;
	@Autowired
	private QueueConfigurationService queueConfigurationService;
	@Autowired
	private AuthConfigurationService authConfigurationService;
	@EventListener(ApplicationReadyEvent.class)
	public void runAfterStartup() {		
		List<QueueConfiguration> queueConfigurations = queueConfigurationService.getAllQueueConfiguration();
		List<AuthConfiguration> authConfig = authConfigurationService.getAuthConfiguration();

		if (queueConfigurations != null) {
			for (QueueConfiguration queueConfiguration : queueConfigurations) {
				InteropxRequestQReceiver interopxRequestQReceiver = (InteropxRequestQReceiver) context
						.getBean("interopxRequestQReceiver");
				for(int a=0; a<queueConfiguration.getListenersSize(); a++) {
					interopxRequestQReceiver.receiveMessages(queueConfiguration.getOutQueueName(),queueConfiguration.getOutQueueConnectionString());	
				}
			}

		}
		
		 /**Pre populates the auth Map from db on startup**/
		 
		if (authConfig != null && !authConfig.isEmpty()) {
			CommonUtil.authMap.put("authorization_url", authConfig.get(0).getAuthorizationEndpointUrl());
			CommonUtil.authMap.put("token_url", authConfig.get(0).getTokenEndpointUrl());
		}

	}

}
