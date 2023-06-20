package com.interopx.fhir.auth.server.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import com.interopx.fhir.auth.server.dao.impl.ResourceListDaoImpl;
import com.interopx.fhir.auth.server.service.ResourceListService;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")

class ResourceListControllerTest {

	@Autowired
	ResourceListController resourceListController;

	@MockBean
	ResourceListService resourceListService;

	@Test
	void registerClient() {
		HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
		List<String> clinicalTypeList = new ArrayList<String>();
		List<String> finalcialTypeList = new ArrayList<String>();
		List<String> securityTypeList = new ArrayList<String>();
		List<String> administrativeTypeList = new ArrayList<String>();
		List<String> allTypeList = new ArrayList<String>();
		
		String clinical = "{Patient, CareTeam, Medication, MedicationRequest,Observation, Procedure, AlleryIntolerance, CareTeam,Condition,"
				+ "DiagnosticReport,DocumentReference, Encounter, Goal, Immunization, ServiceRequest,FamilyMemberHistory,MedicationStatement,MedicationAdministration,MedicationDispense,MedicationKnowledge}";
		String financial = "{Claim, ClaimResponse, Coverage, Coverage, InsurancePlan}";
		String security = "{Provenance}";
		String administrative = "{Location,Organization,Practitioner,PractitionerRole,Device,RelatedPerson,HealthcareService,Endpoint,Group,OrganizationAffiliation}";
		String all = "{Patient, CareTeam, Medication, MedicationRequest,Observation, Procedure, AlleryIntolerance, CareTeam,Condition,"
				+ "DiagnosticReport,DocumentReference, Encounter, Goal, Immunization, ServiceRequest,FamilyMemberHistory,MedicationStatement,MedicationAdministration,MedicationDispense,MedicationKnowledge,Claim, ClaimResponse, Coverage, Coverage, InsurancePlan, Provenance, Location,Organization,Practitioner,PractitionerRole,Device,RelatedPerson,HealthcareService,Endpoint,Group,OrganizationAffiliation}";
		clinicalTypeList.add(clinical);
		finalcialTypeList.add(financial);
		securityTypeList.add(security);
		administrativeTypeList.add(administrative);
 
		when(resourceListService.getResourcesByGroup(3)).thenReturn(clinicalTypeList);
		when(resourceListService.getResourcesByGroup(2)).thenReturn(finalcialTypeList);
		when(resourceListService.getResourcesByGroup(1)).thenReturn(administrativeTypeList);
		when(resourceListService.getResourcesByGroup(4)).thenReturn(securityTypeList);
		when(resourceListService.getResourcesByGroup(null)).thenReturn(allTypeList);

		assertEquals(clinicalTypeList, resourceListController.registerClient("clinical", request));
		assertEquals(finalcialTypeList, resourceListController.registerClient("financial", request));
		assertEquals(securityTypeList, resourceListController.registerClient("security", request));
		assertEquals(administrativeTypeList, resourceListController.registerClient("administrative", request));
		assertEquals(allTypeList, resourceListController.registerClient("all", request));
	}

}
