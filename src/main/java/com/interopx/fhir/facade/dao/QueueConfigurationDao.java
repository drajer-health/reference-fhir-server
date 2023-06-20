/*******************************************************************************
 * Copyright (c) 2020-2022 InteropX LLC, www.interopx.com
 *
 * All Rights Reserved.
 *
 * The source code contained herein, and the packages generated from the source code, are and remain the property of InteropX, LLC. 
 * Access to this source code and the generated packages is granted to authorized customers of InteropX under an InteropX Software License Agreement (SLA).
 * Any use of the source code or the generated packages, not explicitly covered under the signed SLA, is prohibited.
 *******************************************************************************/
package com.interopx.fhir.facade.dao;

import java.util.List;

import com.interopx.fhir.facade.model.QueueConfiguration;

public interface QueueConfigurationDao {
	
	public void saveConfiguration(QueueConfiguration queueConfiguration);
	public void updateConfiguration(QueueConfiguration queueConfiguration);
	public QueueConfiguration getConfigurationById(Integer queueConfigId);
	public void deleteConfiguration(Integer queueConfigId);
	public List<QueueConfiguration> getAllQueueConfiguration();

}
