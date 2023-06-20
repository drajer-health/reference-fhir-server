package com.interopx.fhir.auth.server.exception;

public class AuthServerException extends RuntimeException {

	private static final long serialVersionUID = -746396641587016196L;

	private int status;

	public AuthServerException(String message) {
		super(message);
	}

	public AuthServerException(int status, String message) {
		super(message);
		this.status = status;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
