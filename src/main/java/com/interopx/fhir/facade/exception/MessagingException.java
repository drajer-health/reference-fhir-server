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

import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;

import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.util.CoverageIgnore;

/**
 * Represents an <b>HTTP 500 Internal Server Error</b> response.
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
public class MessagingException extends BaseServerResponseException {

	public static final int STATUS_CODE = Constants.STATUS_HTTP_500_INTERNAL_ERROR;
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 */
	public MessagingException(String theMessage) {
		super(STATUS_CODE, theMessage);
	}

	/**
	 * Constructor
	 */
	public MessagingException(String theMessage, Throwable theCause) {
		super(STATUS_CODE, theMessage, theCause);
	}

	/**
	 * Constructor
	 */
	public MessagingException(Throwable theCause) {
		super(STATUS_CODE, theCause);
	}
	
	/**
	 * Constructor
	 * 
	 * @param theMessage
	 *            The message
	 *  @param theOperationOutcome The OperationOutcome resource to return to the client
	 */
	public MessagingException(String theMessage, IBaseOperationOutcome theOperationOutcome) {
		super(STATUS_CODE, theMessage, theOperationOutcome);
	}


}
