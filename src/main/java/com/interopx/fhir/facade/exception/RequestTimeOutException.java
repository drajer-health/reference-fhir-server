/*******************************************************************************
 * Copyright (c) 2020-2022 InteropX LLC, www.interopx.com
 *
 * All Rights Reserved.
 *
 * The source code contained herein, and the packages generated from the source code, are and remain the property of InteropX, LLC. 
 * Access to this source code and the generated packages is granted to authorized customers of InteropX under an InteropX Software License Agreement (SLA).
 * Any use of the source code or the generated packages, not explicitly covered under the signed SLA, is prohibited.
 *******************************************************************************/
package com.interopx.fhir.facade.exception;

import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.util.CoverageIgnore;
/**
 * Represents an <b>HTTP 408 Request Time Out</b> response.
 * This status indicates that the client's message was invalid (e.g. not a valid FHIR Resource
 * per the specifications), as opposed to the {@link UnprocessableEntityException} which indicates
 * that data does not pass business rule validation on the server.
 * 
 * <p>
 * Note that a complete list of RESTful exceptions is available in the
 * <a href="./package-summary.html">Package Summary</a>.
 * </p>
 * 
 * @see UnprocessableEntityException Which should be used for business level validation failures
 */
@CoverageIgnore
public class RequestTimeOutException extends BaseServerResponseException {

	public static final int STATUS_CODE = 408;

	private static final long serialVersionUID = 1L;

	public RequestTimeOutException() {
		super(STATUS_CODE, "Request Timeout");
	}

	public RequestTimeOutException(String theMessage) {
		super(STATUS_CODE, theMessage);
	}

	public RequestTimeOutException(String theMessage, Throwable theCause) {
		super(STATUS_CODE, theMessage, theCause);
	}
	
	

}
