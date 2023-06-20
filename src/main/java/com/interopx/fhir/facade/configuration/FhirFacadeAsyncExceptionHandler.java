/*******************************************************************************
 * Copyright (c) 2020-2022 InteropX LLC, www.interopx.com
 *
 * All Rights Reserved.
 *
 * The source code contained herein, and the packages generated from the source code, are and remain the property of InteropX, LLC. 
 * Access to this source code and the generated packages is granted to authorized customers of InteropX under an InteropX Software License Agreement (SLA).
 * Any use of the source code or the generated packages, not explicitly covered under the signed SLA, is prohibited.
 *******************************************************************************/
package com.interopx.fhir.facade.configuration;

import java.lang.reflect.Method;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.stereotype.Component;

/**
 * @class AsyncExceptionHandler This class is responsible to handle exception
 *        for Asynchronous communication through out the ix-fhir-parser
 *        application
 * @author BirendraKumar
 * @date FEB 03,2022
 */
@Component
public class FhirFacadeAsyncExceptionHandler implements AsyncUncaughtExceptionHandler {

	/**
	 * This is the logger object (i.e, Singleton Design Factory object) which
	 * provides the ability to trace out the errors/success Async Task Executor.
	 *
	 * @see Logger interface
	 */
	private static final Logger logger = LoggerFactory.getLogger(FhirFacadeAsyncExceptionHandler.class);

	/**
	 * @method handleUncaughtException
	 *
	 * @param ex     of type Throwable object
	 * @param method of type Method
	 * @param args   of type Object-Array
	 */
	@Override
	public void handleUncaughtException(Throwable ex, Method method, Object... args) {

		logger.debug("----------------------------------------------------------------------------------------");
		logger.error("Method name :{0}", method.getName());
		logger.error("Arguments :{0}", Arrays.toString(args));
		logger.error("Error message :{0}", ex.getMessage());
		logger.debug("----------------------------------------------------------------------------------------");

	}

}