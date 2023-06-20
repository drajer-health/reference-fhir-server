package com.interopx.fhir.facade.model;

import java.util.List;

public class ResponseData {
	
	private String transactionTime;
	private String request;
	private String requiresAccessToken;
	private List<ResponseOutputData> output;
	public String getTransactionTime() {
		return transactionTime;
	}
	public void setTransactionTime(String transactionTime) {
		this.transactionTime = transactionTime;
	}
	public String getRequest() {
		return request;
	}
	public void setRequest(String request) {
		this.request = request;
	}
	public String getRequiresAccessToken() {
		return requiresAccessToken;
	}
	public void setRequiresAccessToken(String requiresAccessToken) {
		this.requiresAccessToken = requiresAccessToken;
	}
	public List<ResponseOutputData> getOutput() {
		return output;
	}
	public void setOutput(List<ResponseOutputData> output) {
		this.output = output;
	}
	
	
	
	

}
