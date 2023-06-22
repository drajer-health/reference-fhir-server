package com.interopx.fhir.validator.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import com.interopx.platform.interceptor.InteropXPerformanceMetricsInterceptor;
import com.interopx.platform.interceptor.advice.ExecutionTimeTrackerAdvice;

@Component
public class InterceptorConfiguration implements WebMvcConfigurer {
	
	static final Logger logger = LoggerFactory.getLogger(InterceptorConfiguration.class);
	
	@Autowired
	ExecutionTimeTrackerAdvice advice;
	
	@Value("${interceptor.enable}")
	private boolean isInterceptorConfigurationEnabled;
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		if (isInterceptorConfigurationEnabled) {
			registry.addInterceptor(new InteropXPerformanceMetricsInterceptor());
			advice.setEnabled(true);
			logger.info("Registered Interceptor successfuly...");
		}else {
			advice.setEnabled(false);
			logger.info("Skipped Interceptor/Advice");
			
		}
		
	}

}
