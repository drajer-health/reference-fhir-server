package com.interopx.fhir.auth.server.exception;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.http.HttpException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;

@ControllerAdvice
public class ApplicationExceptionHandler {

	@ExceptionHandler(Exception.class) // exception handled
	public ResponseEntity<ErrorResponse> handleExceptions(Exception e) {

		e.printStackTrace();

		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR; // 500

		return new ResponseEntity<>(new ErrorResponse(status, e.getMessage()), status);
	}

	@ExceptionHandler(InvalidRequestException.class) // exception handled
	public ResponseEntity<ErrorResponse> handleResponseStatusExceptions(Exception e) {

		InvalidRequestException invalidRequestException = (InvalidRequestException) e;

		e.printStackTrace();

		HttpStatus status = HttpStatus.BAD_REQUEST; // 400

		return new ResponseEntity<>(new ErrorResponse(status, e.getMessage()), status);
	}

	@ExceptionHandler(AuthServerException.class) // exception handled
	public ResponseEntity<ErrorResponse> handleAuthServerExceptions(Exception e) {

		AuthServerException authServerException = (AuthServerException) e;

		e.printStackTrace();

		HttpStatus status = HttpStatus.valueOf(authServerException.getStatus()); // 401

		return new ResponseEntity<>(new ErrorResponse(status, authServerException.getMessage()), status);
	}

}
