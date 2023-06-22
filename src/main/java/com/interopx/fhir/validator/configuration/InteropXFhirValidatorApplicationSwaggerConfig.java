package com.interopx.fhir.validator.configuration;

import java.util.Collections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/** The Class InteropXCCDATransformationSwaggerConfig. */
@Configuration
@EnableSwagger2
public class InteropXFhirValidatorApplicationSwaggerConfig{

  /** The Constant LOGGER. */
  private static final Logger LOGGER =
      LoggerFactory.getLogger(InteropXFhirValidatorApplicationSwaggerConfig.class);
  /**
   * Api.
   *
   * @return the docket
   */
  @Bean
  public Docket api() {
    LOGGER.info("Entered - api Method in InteropXFhirValidatorApplicationSwaggerConfig ");
    return new Docket(DocumentationType.SWAGGER_2)
        .select()
        .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
        .paths(PathSelectors.any())
        .build()
        .apiInfo(apiInfo())
        .pathMapping("");
  }

  /**
   * Api info.
   *
   * @return the api info
   */
  private ApiInfo apiInfo() {
    LOGGER.info("Entered - apiInfo Method in InteropXFhirValidatorApplicationSwaggerConfig ");

    ApiInfo apiInfo =
        new ApiInfo(
            "InteropX Fhir Validator Service",
            "ix-fhir-validator",
            "1.0",
            "Terms of service",
            new Contact("InteropX", "http://interopx.com/", "info@interopx.com"),
            "Apache License Version 2.0",
            "https://www.apache.org/licenses/LICENSE-2.0",
            Collections.emptyList());
    LOGGER.info("Exit - apiInfo Method in InteropXFhirValidatorApplicationSwaggerConfig ");

    return apiInfo;
  }
}
