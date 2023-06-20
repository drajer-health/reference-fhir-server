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

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;

/**
 * This is Swagger3 Configuration using annotaion(i.e, @OpenAPIDefinition), which is responsible to configuring 
 * and visualizing api's for 'InteropX FHIR-FACADE' module.
 * 
 * @author BirendraKumar
 * @date 08-02-2022
 */
@Configuration
@OpenAPIDefinition(info = @Info(title = "InteropX-FHIR-Facade API Documentation", 
                                version = "2.0", 
                                description = "Swagger Configuration to visualizing APIs for InteropX-FHIR-FACADE.",
                                termsOfService ="https://www.xyramsoft.com/",
                                contact=@Contact(name="Xyram Software Solutions",
                                                 url="https://www.xyramsoft.com/",
                                                 email="nagesh.bashyam@drajer.com"),
                                license =@License(name="Xyram InteropX API License Version 2.0",
                                                  url="https://www.xyramsoft.com/LICENSE-2.0")))
public class SwaggerConfig {

}
