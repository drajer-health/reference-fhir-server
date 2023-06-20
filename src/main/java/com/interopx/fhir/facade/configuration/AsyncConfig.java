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

import java.util.concurrent.Executor;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
/**
 * This class is used for configuration of async properties
 * @author xyram
 *
 */
@Configuration
@EnableAsync
public class AsyncConfig extends AsyncConfigurerSupport{
	@Autowired
	private FhirFacadeAsyncExceptionHandler asyncExceptionHandler;
	
	@Value("${threadpooltaskexcecutor.core.poolsize}")
	private Integer threadPoolTaskExecCorePoolSize;

	@Value("${threadpooltaskexcecutor.max.poolsize}")
	private Integer threadPoolTaskExecMaxPoolSize;

	@Value("${threadpooltaskexcecutor.queue.capacity}")
	private Integer threadPoolTaskExecQueueCapacity;
	
	@Bean(name="taskExecutor")
	public Executor taskExecutor() {		
		ThreadPoolTaskExecutor executor  = new ThreadPoolTaskExecutor();		
		executor.setCorePoolSize(threadPoolTaskExecCorePoolSize);
		executor.setMaxPoolSize(threadPoolTaskExecMaxPoolSize);
		executor.setQueueCapacity(threadPoolTaskExecQueueCapacity);
		executor.setThreadNamePrefix("fhir-facade-async-thread-");
		executor.initialize();
		executor.setAwaitTerminationSeconds(Integer.MAX_VALUE);
		executor.setWaitForTasksToCompleteOnShutdown(true);
		executor.setPrestartAllCoreThreads(true);
		return executor;
		
	}
	
	/**
	* @method getAsyncUncaughtExceptionHandler
	* @param none
	* @return asyncExceptionHandler
	*/
	@Override
	public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
	return asyncExceptionHandler;
	}
	
	

}
