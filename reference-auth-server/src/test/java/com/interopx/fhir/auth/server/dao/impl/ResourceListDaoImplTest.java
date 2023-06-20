package com.interopx.fhir.auth.server.dao.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.Session;
import javax.transaction.Transactional;

import org.hibernate.SessionFactory;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

import com.interopx.fhir.auth.server.configuration.HibernateConfiguration;
import com.jayway.jsonpath.Criteria;

@SpringBootTest
@Transactional
class ResourceListDaoImplTest {

	@SpyBean
	ResourceListDaoImpl resourceListDaoImpl;

	@SpyBean
	HibernateConfiguration hibernateConfig;

	@Mock
	Session session;
	
	@Mock
	SessionFactory sessionFactory;

	@Mock
	Criteria criteria;
	
	@BeforeEach
	  void setUp() {
	    when(sessionFactory.getCurrentSession()).thenReturn(session);
	    if (sessionFactory == null) {
	      System.out.println("Session Factory is null");
	    } else {
	      System.out.println("Session Factory is not null");
	      session = sessionFactory.getCurrentSession();

	     if (session != null) {
	        System.out.print("Session is not null");
	      } else {
	        System.out.println("Session is null");
	      }
	    }
	  }


	@Test
	void getResourcesByGroup() {

		String resources = "Patient, AllergyIntolerance, CarePlan, CareTeam, Condition, DiagnosticReport, DocumentReference, Encounter, Goal, Immunization, Medication, MedicationRequest, Observation, Procedure, ServiceRequest, FamilyMemberHistory, MedicationStatement, MedicationAdministration, MedicationDispense, MedicationKnowledge";
		List<String> resourceList = new ArrayList<>();
		resourceList.add(resources);
		Integer groupId = 3;
		
		List<String> actualList  = resourceListDaoImpl.getResourcesByGroup(groupId);
		assertNotNull(actualList);

	}
	
	@Test
	void getResourcesByGroup1() {

		String resources = "Location, Organization, Practitioner, PractitionerRole, Device, RelatedPerson, HealthcareService, Endpoint, Claim, ClaimResponse, Coverage, ExplanationOfBenefit, InsurancePlan, Patient, AllergyIntolerance, CarePlan, CareTeam, Condition, DiagnosticReport, DocumentReference, Encounter, Goal, Immunization, Medication, MedicationRequest, Observation, Procedure, ServiceRequest, FamilyMemberHistory, MedicationStatement, Provenance, MedicationAdministration, Group, MedicationDispense, MedicationKnowledge, OrganizationAffiliation";
		List<String> resourceList = new ArrayList<>();
		resourceList.add(resources);
		
		List<String> actualList  = resourceListDaoImpl.getResourcesByGroup(null);
     	assertNotNull(actualList);

	}
	
}
