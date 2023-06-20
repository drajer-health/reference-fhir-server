package com.interopx.fhir.auth.server.exception;

import java.util.Date;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonFormat;

public class ErrorResponse {
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
	private Date timestamp;

	private int code;

	private String status;

	private String message;

	public ErrorResponse() {
		timestamp = new Date();
	}

	public ErrorResponse(HttpStatus httpStatus, String message) {
		this();
		this.code = httpStatus.value();
		this.status = httpStatus.name();
		this.message = message;
	}

	public ErrorResponse(String message) {
		this();
		this.message = message;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
