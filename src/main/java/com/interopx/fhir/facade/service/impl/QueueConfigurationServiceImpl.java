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

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.interopx.fhir.facade.dao.QueueConfigurationDao;
import com.interopx.fhir.facade.model.QueueConfiguration;
import com.interopx.fhir.facade.service.QueueConfigurationException;
import com.interopx.fhir.facade.service.QueueConfigurationService;
import com.interopx.fhir.facade.util.QueueConfigurationDto;

@Service
@Transactional
public class QueueConfigurationServiceImpl implements  QueueConfigurationService{
	
	@Autowired
	QueueConfigurationDao queueConfigurationDao;

	@Override
	public void saveConfiguration(QueueConfigurationDto queueConfigurationDto) {
		QueueConfiguration queueConfiguration = new QueueConfiguration();
		BeanUtils.copyProperties(queueConfigurationDto, queueConfiguration);
		queueConfigurationDao.saveConfiguration(queueConfiguration);
		
	}

	@Override
	public void updateConfiguration(QueueConfigurationDto queueConfigurationDto, Integer queueConfigId) {
		QueueConfiguration queueConfiguration = queueConfigurationDao.getConfigurationById(queueConfigId);
		
		if(queueConfiguration == null) {
			throw new QueueConfigurationException("QueueConfiguration with practiceId: "+queueConfigId+" not found");
		}
		queueConfiguration.setOutQueueConnectionString(queueConfigurationDto.getOutQueueConnectionString());
		queueConfiguration.setOutQueueName(queueConfigurationDto.getOutQueueName());
		
		queueConfigurationDao.updateConfiguration(queueConfiguration);
		
		
	}

	@Override
	public QueueConfiguration getConfigurationById(Integer queueConfigId) {
		return queueConfigurationDao.getConfigurationById(queueConfigId);
		
	}

	@Override
	public void deleteConfiguration(Integer queueConfigId) {
		queueConfigurationDao.deleteConfiguration(queueConfigId);
		
	}

	@Override
	public List<QueueConfiguration> getAllQueueConfiguration() {		
		return queueConfigurationDao.getAllQueueConfiguration();
	}
	

	

}
