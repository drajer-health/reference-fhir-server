package com.interopx.fhir.facade.exception;

import java.util.List;

/**
 * Operation outcomes are sets of error, warning and information messages that provide detailed 
 * information about the outcome of an attempted system operation. They are provided as a direct system response, or 
 * component of one, and provide information about the outcome of the operation.
 * @author admin
 *
 */
public class OperationOutCome{
	
    public String resourceType;
    public List<Issue> issue;
	public String getResourceType() {
		return resourceType;
	}
	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}
	public List<Issue> getIssue() {
		return issue;
	}
	public void setIssue(List<Issue> issue) {
		this.issue = issue;
	}
}