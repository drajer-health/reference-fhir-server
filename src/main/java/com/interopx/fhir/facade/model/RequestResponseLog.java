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

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="request_response")
public class RequestResponseLog {		
	@Id	
	@Column(name="request_id",nullable= false)
	private String requestId;	
	
	@Column(name="response_payload",columnDefinition = "text")	
	private String payload;
	
	@Column(name="status")	
	private String status;
	
	@Column(name="request_ts")
    @Temporal(TemporalType.TIMESTAMP)
    private Date requestTimestamp;
	
	@Column(name="response_ts")
    @Temporal(TemporalType.TIMESTAMP)
    private Date responseTimestamp;
	
	@Column(name="last_updated_ts")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;
	
	@Column(name="resource_type")	
	private String resourceType;
		
	@Column(name="practice_id")	
	private String practiceId;
	
	public String getRequestId() {
		return requestId;
	}
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	public String getPayload() {
		return payload;
	}
	public void setPayload(String payload) {
		this.payload = payload;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getRequestTimestamp() {
		return requestTimestamp;
	}
	public void setRequestTimestamp(Date requestTimestamp) {
		this.requestTimestamp = requestTimestamp;
	}
	public Date getResponseTimestamp() {
		return responseTimestamp;
	}
	public void setResponseTimestamp(Date responseTimestamp) {
		this.responseTimestamp = responseTimestamp;
	}
	public Date getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	public String getResourceType() {
		return resourceType;
	}
	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}
	public String getPracticeId() {
		return practiceId;
	}
	public void setPracticeId(String practiceId) {
		this.practiceId = practiceId;
	}	
}
