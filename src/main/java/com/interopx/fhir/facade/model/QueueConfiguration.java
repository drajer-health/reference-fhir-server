/*******************************************************************************
 * Copyright (c) 2020-2022 InteropX LLC, www.interopx.com
 *
 * All Rights Reserved.
 *
 * The source code contained herein, and the packages generated from the source code, are and remain the property of InteropX, LLC. 
 * Access to this source code and the generated packages is granted to authorized customers of InteropX under an InteropX Software License Agreement (SLA).
 * Any use of the source code or the generated packages, not explicitly covered under the signed SLA, is prohibited.
 *******************************************************************************/
package com.interopx.fhir.facade.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="queue_configuration")
public class QueueConfiguration {
	@Id	
	@Column(name="queue_config_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer queueConfigId;
	@Column(name="outqueue_name", nullable = false)
	private String outQueueName;
	@Column(name="outqueue_connection_string", nullable = false)
	private String outQueueConnectionString;
	@Column(name="listeners_size")
	private Integer listenersSize;
	
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
	public Integer getListenersSize() {
		return listenersSize;
	}
	public void setListenersSize(Integer listenersSize) {
		this.listenersSize = listenersSize;
	}
	public Integer getQueueConfigId() {
		return queueConfigId;
	}
	public void setQueueConfigId(Integer queueConfigId) {
		this.queueConfigId = queueConfigId;
	}	
}
