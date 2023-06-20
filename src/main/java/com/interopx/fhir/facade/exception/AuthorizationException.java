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

import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
/**
 * Represents an <b>HTTP 401 Unauthorized</b> response.
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
public class AuthorizationException extends BaseServerResponseException {

	public static final int UNAUTHORIZED_CODE = 401;
	public static final int FORBIDEN_STATUS_CODE = 403;
	private static final String DEFAULT_MESSAGE = "Invalid Authorization";
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 *
	 * @param theMessage          The message to add to the status line
	 * @param theOperationOutcome The {@link IBaseOperationOutcome} resource to
	 *                            return to the client
	 */
	public AuthorizationException(String theMessage, IBaseOperationOutcome theOperationOutcome) {
		super(UNAUTHORIZED_CODE, theMessage, theOperationOutcome);
	}

	/**
	 * Constructor
	 *
	 * @param theStatusCode           The HTTP status code corresponding to this
	 *                                problem
	 * @param theMessage              The message
	 * @param theBaseOperationOutcome An BaseOperationOutcome resource to return to
	 *                                the calling client (in a server) or the
	 *                                BaseOperationOutcome that was returned from
	 *                                the server (in a client)
	 */
	public AuthorizationException(int theStatusCode, String theMessage, IBaseOperationOutcome theBaseOperationOutcome) {
		super(FORBIDEN_STATUS_CODE, theMessage, theBaseOperationOutcome);
	}
}
