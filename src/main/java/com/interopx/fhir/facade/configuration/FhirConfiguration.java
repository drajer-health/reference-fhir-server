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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import com.interopx.fhir.facade.interceptor.IntrospectInterceptor;
import com.interopx.fhir.facade.providers.CapabilityStatementResourceProvider;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.narrative.CustomThymeleafNarrativeGenerator;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.interceptor.LoggingInterceptor;
import ca.uhn.fhir.rest.server.interceptor.auth.AuthorizationInterceptor;
import ca.uhn.fhir.rest.server.tenant.UrlBaseTenantIdentificationStrategy;
import ca.uhn.fhir.spring.boot.autoconfigure.FhirRestfulServerCustomizer;
/**
 * This class contains the configurations for Fhir interceptors and rest template
 * @author xyram
 *
 */
@Configuration
public class FhirConfiguration implements IResourceProvider, FhirRestfulServerCustomizer{
	
	private static final Logger logger = LoggerFactory.getLogger(FhirConfiguration.class);
	
	@Autowired
	private IntrospectInterceptor introspectInterceptor;
	
	@Bean
    public LoggingInterceptor loggingInterceptor() {
        return new LoggingInterceptor();
    }
	
	@Bean
    public FhirContext getFhirContext() {
		FhirContext fhirContext =FhirContext.forR4();
		CustomThymeleafNarrativeGenerator customNarrative = new CustomThymeleafNarrativeGenerator("classpath:com/interopx/narrative/narratives.properties");
		fhirContext.setNarrativeGenerator(customNarrative);
        return fhirContext;
    }
	
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@SuppressWarnings("deprecation")
	@Override
	public void customize(RestfulServer server) {
		try {
			Collection<IResourceProvider> c = server.getResourceProviders();			
			List<IResourceProvider> l = c.stream().filter(p -> p != this).collect(Collectors.toList());
			server.setServerConformanceProvider(new CapabilityStatementResourceProvider());
			server.setResourceProviders(l);
			server.setTenantIdentificationStrategy(new UrlBaseTenantIdentificationStrategy());
			List<AuthorizationInterceptor> interceptorList = new ArrayList<>();
			interceptorList.add(introspectInterceptor);
			server.setInterceptors(interceptorList);
		} catch(Exception e){
			logger.error("Error in Initializing the FHIR Server");
		}finally {
			logger.debug("Initialized all the Resource Providers");
		}
	}

	@Override
	public Class<? extends IBaseResource> getResourceType() {
		return null;
	}	

}
