package com.interopx.fhir.validator;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.interopx.fhir.validator.component.Validator;

import ca.uhn.fhir.rest.server.interceptor.LoggingInterceptor;

@SpringBootApplication
@EnableAutoConfiguration(exclude = HibernateJpaAutoConfiguration.class)
@ComponentScan(basePackages = {"com.interopx.fhir.validator",
							   "com.interopx.fhir.validator.entity",
							   "com.interopx.fhir.validator.repositories",
							   "com.interopx.platform.interceptor",
							   "com.interopx.platform.interceptor.dao",
							   "com.interopx.platform.interceptor.mapper",
							   "com.interopx.platform.interceptor.model",
							   "com.interopx.platform.interceptor.advice" })
@EntityScan(basePackages = {"com.interopx.platform.interceptor.entity",
							"com.interopx.fhir.InteropXFhirvalidator.entity"})
@EnableJpaRepositories(basePackages = {"com.interopx.fhir.validator.repositories"})
public class InteropXFhirValidatorApplication extends SpringBootServletInitializer {

	private final Logger log = LoggerFactory.getLogger(InteropXFhirValidatorApplication.class);

	@Autowired
	private ResourceLoader resourceLoader;

	public static void main(String[] args) {
		SpringApplication.run(InteropXFhirValidatorApplication.class, args);
		// initializeValidator();
	}

	@Bean
	public LoggingInterceptor loggingInterceptor() {
		return new LoggingInterceptor();
	}

	@PostConstruct
	public Validator initializeValidator() {
		log.info("inside the initializeValidator");
		try {
			Resource res = resourceLoader.getResource("classpath:igs/package");
			Resource pdexRes = resourceLoader.getResource("classpath:pdexigs/package");
			Resource pdexPlanNetRes = resourceLoader.getResource("classpath:pdexplannetigs/package");
			Resource carinBBRes = resourceLoader.getResource("classpath:carinbbigs/package");
			Resource hrexRes = resourceLoader.getResource("classpath:hrexigs/package");
			Resource drugFormularyRes = resourceLoader.getResource("classpath:drugformularyigs/package");
			Resource pasRes = resourceLoader.getResource("classpath:pasigs/package");
			List<String> resList = new ArrayList<>();
			resList.add(res.getURI().getPath());
			resList.add(pdexRes.getURI().getPath());
			resList.add(pdexPlanNetRes.getURI().getPath());
			resList.add(carinBBRes.getURI().getPath());
			resList.add(hrexRes.getURI().getPath());
			resList.add(drugFormularyRes.getURI().getPath());
			resList.add(pasRes.getURI().getPath());
			return new Validator(resList);
		} catch (Exception e) {
			log.error("There was an error initializing the validator:", e);
			System.exit(1);
			return null; // unreachable
		}
	}
}
