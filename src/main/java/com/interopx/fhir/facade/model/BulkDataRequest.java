package com.interopx.fhir.facade.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "bulk_data_requests")
public class BulkDataRequest {

	@Id	
	@Column(name="bulk_data_request_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer bulkDataRequestId;
	
	@Column(name = "request_id")
	private String requestId;
	
	@Column(name = "job_id")
	private String jobId;
	
	@Column(name = "group_id")
	private String groupId;
	
	@Column(name = "org_id")
	private String orgId;

	@Column(name = "request_url")
	private String request;

	@Column(name = "job_status")
	private String jobStatus;

	@Column(name = "processed_flag")
	private boolean processedFlag;

	@Column(name = "status_message")
	private String statusMessage;
	
	@Column(name = "last_updated_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastUpdatedDate;
	

	public String getJobId() {
		return jobId;
	}

	public Integer getBulkDataRequestId() {
		return bulkDataRequestId;
	}

	public void setBulkDataRequestId(Integer bulkDataRequestId) {
		this.bulkDataRequestId = bulkDataRequestId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public String getRequest() {
		return request;
	}

	public void setRequest(String request) {
		this.request = request;
	}

	public String getJobStatus() {
		return jobStatus;
	}

	public void setJobStatus(String jobStatus) {
		this.jobStatus = jobStatus;
	}

	public boolean isProcessedFlag() {
		return processedFlag;
	}

	public void setProcessedFlag(boolean processedFlag) {
		this.processedFlag = processedFlag;
	}

	public String getStatusMessage() {
		return statusMessage;
	}

	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}

	public Date getLastUpdatedDate() {
		return lastUpdatedDate;
	}

	public void setLastUpdatedDate(Date lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}
	
	

	

}
