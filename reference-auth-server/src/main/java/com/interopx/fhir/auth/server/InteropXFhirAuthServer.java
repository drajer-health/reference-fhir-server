package com.interopx.fhir.auth.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

@SpringBootApplication(
    exclude = {SecurityAutoConfiguration.class},
    scanBasePackages = {"com.ix.audit.log", "com.ix.audit.log.service", "com.ix.audit.log.dao"})
@EnableAutoConfiguration(exclude = HibernateJpaAutoConfiguration.class)
@ComponentScan(
    basePackages = {
      "com.interopx.fhir.auth.server",
      "com.ix.audit.log",
      "com.ix.audit.log.service",
      "com.ix.audit.log.dao"
    })
@EntityScan(basePackages = {"com.ix.audit.log.entity"})
public class InteropXFhirAuthServer extends SpringBootServletInitializer {
  private static final Logger logger = LoggerFactory.getLogger(InteropXFhirAuthServer.class);

  public static void main(String[] args) {
    logger.info("starting InteropXFhirAuthServer");
    SpringApplication.run(InteropXFhirAuthServer.class, args);
    // initializeValidator();
  }

  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry
        .addResourceHandler("swagger-ui.html")
        .addResourceLocations("classpath:/META-INF/resources/");
    registry
        .addResourceHandler("/webjars/**")
        .addResourceLocations("classpath:/META-INF/resources/webjars/");
  }
}
