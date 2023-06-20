package com.interopx.fhir.auth.server.dao.impl;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

import com.interopx.fhir.auth.server.configuration.HibernateConfiguration;
import com.interopx.fhir.auth.server.model.DafPatientJson;
import com.interopx.fhir.auth.server.model.SmartAuthorizeationDetails;

@SpringBootTest
class PatientDaoImplTest {

	@InjectMocks
	PatientDaoImpl daoImpl;

	@Mock
	SessionFactory sessionFactory;

	@SpyBean
	HibernateConfiguration hibernateConfig;
	
	@Mock
	Session session;
	
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
	void testGetPatientById() {
		DafPatientJson dafPatientJson = new DafPatientJson();
		dafPatientJson.setActive(true);
		dafPatientJson.setId(1);
		dafPatientJson.setIdentifier("identifier");
		dafPatientJson.setUpdated(new Date());
		dafPatientJson.setAddressCity("Bangalore");
		dafPatientJson.setAddressCountry("India");
		dafPatientJson.setAddressState("Karnataka");
		dafPatientJson.setBirthPlace("Bangalore");
		dafPatientJson.setFullName("Navaneetha");
		dafPatientJson.setGender("Female");
		dafPatientJson.setGivenName("Navaneetha");
		
		Class<DafPatientJson> dafPatientJson1 = DafPatientJson.class;
		when(session.get(dafPatientJson1, 1)).thenReturn(dafPatientJson);

		assertEquals(dafPatientJson, daoImpl.getPatientById(1));

	}

	
	@Test
	void testGetAllPatient() {
		DafPatientJson patient = new DafPatientJson();
		patient.setActive(true);
		patient.setId(1);
		patient.setIdentifier("identifier");
		patient.setUpdated(new Date());
		patient.setAddressCity("Bangalore");
		patient.setAddressCountry("India");
		patient.setAddressState("Karnataka");
		patient.setBirthPlace("Bangalore");
		patient.setFullName("Navaneetha");
		patient.setGender("Female");
		patient.setGivenName("Navaneetha");

		List<DafPatientJson> patientList = new ArrayList<>();
		patientList.add(patient);
		
		Class<DafPatientJson> dafPatientJson1 = DafPatientJson.class;
		when(session.createCriteria(dafPatientJson1)).thenReturn(criteria);
		when(criteria.list()).thenReturn(patientList);
		assertEquals(patientList, daoImpl.getAllPatient());

	}
	
	@Test
	void testGetAllPatientByException() {
		DafPatientJson dafPatientJson = mock(DafPatientJson.class);
		dafPatientJson.setId(0);
		List patientList = new ArrayList();
		doThrow(RuntimeException.class).when(dafPatientJson).getId();			
		assertEquals(patientList, daoImpl.getAllPatient());		
	}

	@Test
	void testGetPatientJsonForBulkData() {
		DafPatientJson patient = new DafPatientJson();
		patient.setActive(true);
		patient.setId(1);
		patient.setIdentifier("identifier");
		patient.setUpdated(new Date());
		patient.setAddressCity("Bangalore");
		patient.setAddressCountry("India");
		patient.setAddressState("Karnataka");
		patient.setBirthPlace("Bangalore");
		patient.setFullName("Navaneetha");
		patient.setGender("Female");
		patient.setGivenName("Navaneetha");

		List<DafPatientJson> patientList = new ArrayList<>();
		patientList.add(patient);

		Date start = new Date();
		List<Integer> integerList = new ArrayList<>();
		integerList.add(1);
		integerList.add(2);
		integerList.add(3);

		Class<DafPatientJson> dafPatientJson1 = DafPatientJson.class;
		when(session.createCriteria(dafPatientJson1)).thenReturn(criteria);
		when(criteria.list()).thenReturn(patientList);
		assertEquals(patientList, daoImpl.getPatientJsonForBulkData(integerList, start));				
	}
	
	@Test
	void testGetPatientJsonForBulkDataByException() {
		Date start = new Date();
		List<Integer> integerList = new ArrayList<>();
		DafPatientJson dafPatientJson = mock(DafPatientJson.class);
		dafPatientJson.setId(0);
		List patientList = new ArrayList();
		doThrow(RuntimeException.class).when(dafPatientJson).getId();			
		assertNull(daoImpl.getPatientJsonForBulkData(integerList, start));		
	}

}
