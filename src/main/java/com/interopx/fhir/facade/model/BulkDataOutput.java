package com.interopx.fhir.facade.model;

import java.util.ArrayList;

public class BulkDataOutput {
	
	private String transactionTime;
	private String request;
	private Boolean requiresAccessToken;
	ArrayList<BulkDataOutputInfo> output;
	ArrayList<Object> error;
	
	public BulkDataOutput() {
		output = new ArrayList<>();
		error = new ArrayList<>();
	}

	public String getRequest() {
		return request;
	}

	public void setRequest(String request) {
		this.request = request;
	}

	public String getTransactionTime() {
		return transactionTime;
	}

	public void setTransactionTime(String transactionTime) {
		this.transactionTime = transactionTime;
	}

	public Boolean getRequiresAccessToken() {
		return requiresAccessToken;
	}

	public void setRequiresAccessToken(Boolean requiresAccessToken) {
		this.requiresAccessToken = requiresAccessToken;
	}

	public ArrayList<BulkDataOutputInfo> getOutput() {
		return output;
	}

	public void setOutput(ArrayList<BulkDataOutputInfo> output) {
		this.output = output;
	}
	
	public void add(BulkDataOutputInfo bdoi) {
		output.add(bdoi);
	}

	public ArrayList<Object> getError() {
		return error;
	}

	public void setError(ArrayList<Object> error) {
		this.error = error;
	}
	
	public void add(Object error) {
		((BulkDataOutput) error).add(error);
	}
	
	
}
