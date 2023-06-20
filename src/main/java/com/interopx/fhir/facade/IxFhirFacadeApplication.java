/*******************************************************************************
 * Copyright (c) 2020-2022 InteropX LLC, www.interopx.com
 *
 * All Rights Reserved.
 *
 * The source code contained herein, and the packages generated from the source code, are and remain the property of InteropX, LLC. 
 * Access to this source code and the generated packages is granted to authorized customers of InteropX under an InteropX Software License Agreement (SLA).
 * Any use of the source code or the generated packages, not explicitly covered under the signed SLA, is prohibited.
 *******************************************************************************/
package com.interopx.fhir.facade;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication(exclude = HibernateJpaAutoConfiguration.class)
@ComponentScan(basePackages = {"com.interopx.fhir.facade","com.interopx.fhir.facade.service","com.interopx.fhir.facade.util","com.interopx.fhir.facade.service.impl","com.interopx.fhir.facade.dao","com.interopx.fhir.facade.dao.Impl","com.interopx.fhir.facade.providers"})
@EntityScan({"com.interopx.fhir.facade.model"})
@EnableAsync
public class IxFhirFacadeApplication {

	public static void main(String[] args) {
		SpringApplication.run(IxFhirFacadeApplication.class, args);
	}

}
