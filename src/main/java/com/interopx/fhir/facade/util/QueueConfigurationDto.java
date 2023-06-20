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

public class QueueConfigurationDto {
	
	private Integer queueConfigId;	
	private String inQueueName;	
	private String inQueueConnectionString;	
	private String outQueueName;	
	private String outQueueConnectionString;
	private Integer listenersSize;

	public String getInQueueName() {
		return inQueueName;
	}
	public void setInQueueName(String inQueueName) {
		this.inQueueName = inQueueName;
	}
	public String getInQueueConnectionString() {
		return inQueueConnectionString;
	}
	public void setInQueueConnectionString(String inQueueConnectionString) {
		this.inQueueConnectionString = inQueueConnectionString;
	}
	public String getOutQueueName() {
		return outQueueName;
	}
	public void setOutQueueName(String outQueueName) {
		this.outQueueName = outQueueName;
	}
	public String getOutQueueConnectionString() {
		return outQueueConnectionString;
	}
	public void setOutQueueConnectionString(String outQueueConnectionString) {
		this.outQueueConnectionString = outQueueConnectionString;
	}
	public Integer getQueueConfigId() {
		return queueConfigId;
	}
	public void setQueueConfigId(Integer queueConfigId) {
		this.queueConfigId = queueConfigId;
	}
	public Integer getListenersSize() {
		return listenersSize;
	}
	public void setListenersSize(Integer listenersSize) {
		this.listenersSize = listenersSize;
	}
	
}
