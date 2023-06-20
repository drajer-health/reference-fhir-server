package com.interopx.fhir.auth.server.configuration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/** ValidatorSwaggerConfig */
@Configuration
@EnableSwagger2
public class AuthServerSwaggerConfig {
  /**
   * api
   *
   * @return Docket value
   */
  @Bean
  public Docket api() {
    ParameterBuilder aParameterBuilder = new ParameterBuilder();
    aParameterBuilder
        .name("X-AUTH-TOKEN") // name of header
        .modelRef(new ModelRef("string"))
        .parameterType("header") // type - header
        .defaultValue("") // based64 of - zone:mypassword
        .required(false) // for compulsory
        .build();
    List<Parameter> aParameters = new ArrayList<>();
    aParameters.add(aParameterBuilder.build());

    return new Docket(DocumentationType.SWAGGER_2)
        .select()
        .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
        .paths(PathSelectors.any())
        .build()
        .apiInfo(apiInfo())
        .pathMapping("")
        .globalOperationParameters(aParameters);
  }
  /**
   * apiInfo
   *
   * @return apiInfo value
   */
  private ApiInfo apiInfo() {
    ApiInfo apiInfo =
        new ApiInfo(
            "InteropX FHIR Auth Server Service",
            "FHIR-auth-server service",
            "1.0",
            "Terms of service",
            new Contact("InteropX", "http://interopx.com/", "info@interopx.com"),
            "Apache License Version 2.0",
            "https://www.apache.org/licenses/LICENSE-2.0",
            Collections.emptyList());
    return apiInfo;
  }
}
