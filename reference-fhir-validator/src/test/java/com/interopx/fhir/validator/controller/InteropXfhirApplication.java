package com.interopx.fhir.validator.controller;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import com.interopx.fhir.validator.InteropXFhirValidatorApplication;
import com.interopx.fhir.validator.component.Validator;
import com.interopx.fhir.validator.controller.ResourceValidationController;
import com.interopx.fhir.validator.service.ResourceValidationServiceImpl;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.fhir.validation.ValidationResult;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class InteropXfhirApplication {

	// @Autowired
	// @Qualifier("r4FhirContext")
	// private FhirContext r4Context;
	//
	 @Spy
	 @Qualifier("dstu3FhirContext")
	 private FhirContext dstu3Context;
	//
	// @Autowired
	// @Qualifier("dstu2FhirContext")
	// private FhirContext dstu2Context;
	//
	//
	// ResourceValidationController r= null;

	// @Autowired
	// @Qualifier("r5FhirContext")
	// private FhirContext r5Context;

	@InjectMocks
	ResourceValidationController resourceValidationController;
	
	@InjectMocks
	InteropXFhirValidatorApplication interopXFhirValidatorApplication;
	
	@Spy
	private Validator validator;
	
	@InjectMocks
	private ResourceValidationServiceImpl validationService;
	
	@Spy
	private ResourceLoader resourceLoader;

	@Spy
	@Qualifier("dstu2HL7FhirContext")
	private FhirContext dstu2HL7FhirContext;

	
	 public void initMocks() throws Exception {
		 Resource res = resourceLoader.getResource("./src/test/resources/igs/package");
//			Resource pdexRes = resourceLoader.getResource("classpath:pdexigs/package");
//			Resource pdexPlanNetRes = resourceLoader.getResource("classpath:pdexplannetigs/package");
//			Resource carinBBRes = resourceLoader.getResource("classpath:carinbbigs/package");
//			Resource hrexRes = resourceLoader.getResource("classpath:hrexigs/package");
//			Resource drugFormularyRes = resourceLoader.getResource("classpath:drugformularyigs/package");
//			Resource pasRes = resourceLoader.getResource("classpath:pasigs/package");
			List<String> resList = new ArrayList<>();
			resList.add(res.getURI().getPath());
//			resList.add(pdexRes.getURI().getPath());
//			resList.add(pdexPlanNetRes.getURI().getPath());
//			resList.add(carinBBRes.getURI().getPath());
//			resList.add(hrexRes.getURI().getPath());
//			resList.add(drugFormularyRes.getURI().getPath());
//			resList.add(pasRes.getURI().getPath());
//			return new Validator(resList);
		 
		 validator = new Validator(resList);
		 
//		validator = new Validator("./src/test/resources/igs/package");
	}

	@Test
	public void valiadteTheDSTU2() {
		FhirValidator validator = dstu2HL7FhirContext.newValidator();
		String bodyStr = "{\r\n" + 
				"  \"resourceType\": \"Organization\",\r\n" + 
				"  \"id\": \"f203\",\r\n" + 
				"  \"meta\" : {\r\n" + 
				"    \"profile\" : [\r\n" + 
				"      \"http://hl7.org/fhir/us/core/StructureDefinition/us-core-allergyintolerance\"\r\n" + 
				"    ]\r\n" + 
				"  },\r\n" + 
				"  \"text\": {\r\n" + 
				"    \"status\": \"generated\",\r\n" + 
				"    \"div\": \"<div><p><b>Generated Narrative with Details</b></p><p><b>id</b>: f203</p><p><b>identifier</b>: Zorginstelling naam = Blijdorp MC (OFFICIAL)</p><p><b>active</b>: true</p><p><b>type</b>: Academic Medical Center <span>(Details : {SNOMED CT code '405608006' = '405608006', given as 'Academic Medical Center'}; {http://hl7.org/fhir/organization-type code 'prov' = 'Healthcare Provider)</span></p><p><b>name</b>: Blijdorp Medisch Centrum (BUMC)</p><p><b>telecom</b>: ph: +31107040704(WORK)</p><p><b>address</b>: apenrots 230 Blijdorp 3056BE NLD (WORK)</p></div>\"\r\n" + 
				"  },\r\n" + 
				"  \"identifier\": [\r\n" + 
				"    {\r\n" + 
				"      \"use\": \"official\",\r\n" + 
				"      \"_use\": {\r\n" + 
				"        \"fhir_comments\": [\r\n" + 
				"          \"   Identifier for the BMC   \"\r\n" + 
				"        ]\r\n" + 
				"      },\r\n" + 
				"      \"type\": {\r\n" + 
				"        \"text\": \"Zorginstelling naam\"\r\n" + 
				"      },\r\n" + 
				"      \"system\": \"http://www.zorgkaartnederland.nl/\",\r\n" + 
				"      \"value\": \"Blijdorp MC\"\r\n" + 
				"    }\r\n" + 
				"  ],\r\n" + 
				"  \"active\": true,\r\n" + 
				"  \"_active\": {\r\n" + 
				"    \"fhir_comments\": [\r\n" + 
				"      \"      <accreditation>\\n      <!-\\\\-NIAZ Accreditatie-\\\\->\\n      <identifier>\\n         <!-\\\\-Identifier for the accreditation -\\\\->\\n         <system value=\\\"http://www.niaz.nl/\\\"/>\\n         <value value=\\\"NIAZ accreditation\\\"/>\\n      </identifier>\\n      <period>\\n         <start value=\\\"2008-11-07\\\"/>\\n         <end value=\\\"2013-11-07\\\"/>\\n      </period>\\n   </accreditation>   \",\r\n" + 
				"      \"   The BMC is in active use   \"\r\n" + 
				"    ]\r\n" + 
				"  },\r\n" + 
				"  \"type\": {\r\n" + 
				"    \"coding\": [\r\n" + 
				"      {\r\n" + 
				"        \"fhir_comments\": [\r\n" + 
				"          \"   BMC is an Academic Medical Center   \"\r\n" + 
				"        ],\r\n" + 
				"        \"system\": \"http://snomed.info/sct\",\r\n" + 
				"        \"code\": \"405608006\",\r\n" + 
				"        \"display\": \"Academic Medical Center\"\r\n" + 
				"      },\r\n" + 
				"      {\r\n" + 
				"        \"system\": \"http://hl7.org/fhir/organization-type\",\r\n" + 
				"        \"code\": \"prov\"\r\n" + 
				"      }\r\n" + 
				"    ]\r\n" + 
				"  },\r\n" + 
				"  \"name\": \"Blijdorp Medisch Centrum (BUMC)\",\r\n" + 
				"  \"telecom\": [\r\n" + 
				"    {\r\n" + 
				"      \"system\": \"phone\",\r\n" + 
				"      \"_system\": {\r\n" + 
				"        \"fhir_comments\": [\r\n" + 
				"          \"   BMC's contact detail   \"\r\n" + 
				"        ]\r\n" + 
				"      },\r\n" + 
				"      \"value\": \"+31107040704\",\r\n" + 
				"      \"use\": \"work\"\r\n" + 
				"    }\r\n" + 
				"  ],\r\n" + 
				"  \"address\": [\r\n" + 
				"    {\r\n" + 
				"      \"fhir_comments\": [\r\n" + 
				"        \"   ISO 3166 3 letter code   \"\r\n" + 
				"      ],\r\n" + 
				"      \"use\": \"work\",\r\n" + 
				"      \"_use\": {\r\n" + 
				"        \"fhir_comments\": [\r\n" + 
				"          \"   BMC's address   \"\r\n" + 
				"        ]\r\n" + 
				"      },\r\n" + 
				"      \"line\": [\r\n" + 
				"        \"apenrots 230\"\r\n" + 
				"      ],\r\n" + 
				"      \"city\": \"Blijdorp\",\r\n" + 
				"      \"postalCode\": \"3056BE\",\r\n" + 
				"      \"country\": \"NLD\"\r\n" + 
				"    }\r\n" + 
				"  ]\r\n" + 
				"}";

		ValidationResult results = validationService.validateDSTU2Resource(dstu2HL7FhirContext, validator, bodyStr);
		assertTrue(results.toString().contains("ValidationResult{messageCount=0, isSuccessful=true, description='No issues'}"));
	}
	
	@Test
	public void valiadteTheDSTU3() {
		FhirValidator validator = dstu3Context.newValidator();
		String bodyStr = "{\r\n" + 
				"  \"resourceType\" : \"Organization\",\r\n" + 
				"  \"id\" : \"acme-lab\",\r\n" + 
				"  \"meta\" : {\r\n" + 
				"    \"profile\" : [\r\n" + 
				"      \"http://hl7.org/fhir/us/core/StructureDefinition/us-core-organization\"\r\n" + 
				"    ]\r\n" + 
				"  },\r\n" + 
				"  \"text\" : {\r\n" + 
				"    \"status\" : \"generated\",\r\n" + 
				"    \"div\" : \"<div xmlns=\\\"http://www.w3.org/1999/xhtml\\\"><p><b>Generated Narrative with Details</b></p><p><b>id</b>: acme-lab</p><p><b>meta</b>: </p><p><b>identifier</b>: 1144221847, 12D4567890</p><p><b>active</b>: true</p><p><b>type</b>: Healthcare Provider <span style=\\\"background: LightGoldenRodYellow\\\">(Details : {http://terminology.hl7.org/CodeSystem/organization-type code 'prov' = 'Healthcare Provider', given as 'Healthcare Provider'})</span></p><p><b>name</b>: Acme Labs</p><p><b>telecom</b>: ph: (+1) 734-677-7777, hq@acme.org</p><p><b>address</b>: 3300 Washtenaw Avenue, Suite 227 Amherst MA 01002 USA </p></div>\"\r\n" + 
				"  },\r\n" + 
				"  \"identifier\" : [\r\n" + 
				"    {\r\n" + 
				"      \"system\" : \"http://hl7.org.fhir/sid/us-npi\",\r\n" + 
				"      \"value\" : \"1144221847\"\r\n" + 
				"    },\r\n" + 
				"    {\r\n" + 
				"      \"system\" : \"urn:oid:2.16.840.1.113883.4.7\",\r\n" + 
				"      \"value\" : \"12D4567890\"\r\n" + 
				"    }\r\n" + 
				"  ],\r\n" + 
				"  \"active\" : true,\r\n" + 
				"  \"type\" : [\r\n" + 
				"    {\r\n" + 
				"      \"coding\" : [\r\n" + 
				"        {\r\n" + 
				"          \"system\" : \"http://terminology.hl7.org/CodeSystem/organization-type\",\r\n" + 
				"          \"code\" : \"prov\",\r\n" + 
				"          \"display\" : \"Healthcare Provider\"\r\n" + 
				"        }\r\n" + 
				"      ]\r\n" + 
				"    }\r\n" + 
				"  ],\r\n" + 
				"  \"name\" : \"Acme Labs\",\r\n" + 
				"  \"telecom\" : [\r\n" + 
				"    {\r\n" + 
				"      \"system\" : \"phone\",\r\n" + 
				"      \"value\" : \"(+1) 734-677-7777\"\r\n" + 
				"    },\r\n" + 
				"    {\r\n" + 
				"      \"system\" : \"email\",\r\n" + 
				"      \"value\" : \"hq@acme.org\"\r\n" + 
				"    }\r\n" + 
				"  ],\r\n" + 
				"  \"address\" : [\r\n" + 
				"    {\r\n" + 
				"      \"line\" : [\r\n" + 
				"        \"3300 Washtenaw Avenue, Suite 227\"\r\n" + 
				"      ],\r\n" + 
				"      \"city\" : \"Amherst\",\r\n" + 
				"      \"state\" : \"MA\",\r\n" + 
				"      \"postalCode\" : \"01002\",\r\n" + 
				"      \"country\" : \"USA\"\r\n" + 
				"    }\r\n" + 
				"  ]\r\n" + 
				"}";
		ValidationResult results = validationService.validateSTU3Resource(dstu2HL7FhirContext, validator, bodyStr);
		assertTrue(results.toString().contains("ValidationResult{messageCount=0, isSuccessful=true, description='No issues'}"));
	}
	
}
