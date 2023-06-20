/*******************************************************************************
 * Copyright (c) 2020-2022 InteropX LLC, www.interopx.com
 *
 * All Rights Reserved.
 *
 * The source code contained herein, and the packages generated from the source code, are and remain the property of InteropX, LLC. 
 * Access to this source code and the generated packages is granted to authorized customers of InteropX under an InteropX Software License Agreement (SLA).
 * Any use of the source code or the generated packages, not explicitly covered under the signed SLA, is prohibited.
 *******************************************************************************/
package com.interopx.fhir.facade.interceptor;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
/**
 * This class is responsible for rediecting urls
 * @author xyram
 *
 */
@Component
public class RedirectionFilter extends OncePerRequestFilter {
	private final Logger logger = LoggerFactory.getLogger(RedirectionFilter.class);
	
	@Override
	protected void doFilterInternal(HttpServletRequest servletRequest, HttpServletResponse servletResponse, FilterChain chain)
			throws IOException, ServletException {

		final HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
		try {		
			if (httpRequest.getRequestURI().contains(".well-known/openid-configuration")) {			   
		       getServletContext().getRequestDispatcher("/.well-known/openid-configuration").forward(servletRequest, servletResponse);
				
			}else if(httpRequest.getRequestURI().contains(".well-known/smart-configuration")) {		      
			   getServletContext().getRequestDispatcher("/.well-known/smart-configuration").forward(servletRequest, servletResponse);
				
			}else if(httpRequest.getRequestURI().contains("$export-poll-location")) {
				 getServletContext().getRequestDispatcher("/$export-poll-location").forward(servletRequest, servletResponse);
			}else{			
				chain.doFilter(servletRequest, servletResponse);			
			}
		} catch (Exception e) {
			logger.error("Exception in doFilter() of RedirectionFilter class ", e);
		}
	}

	
}