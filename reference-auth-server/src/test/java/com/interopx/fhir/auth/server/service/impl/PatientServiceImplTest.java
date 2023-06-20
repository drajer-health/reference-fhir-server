package com.interopx.fhir.auth.server.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

import com.interopx.fhir.auth.server.dao.PatientDao;
import com.interopx.fhir.auth.server.model.DafPatientJson;

@SpringBootTest
class PatientServiceImplTest {

	@Autowired
	PatientServiceImpl patientServiceImpl;

	@SpyBean
	PatientDao patientDao;

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

		when(this.patientDao.getAllPatient()).thenReturn(patientList);
		assertEquals(patientList, patientServiceImpl.getAllPatient());

	}

	@Test
	void testGetPatientById() {
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

		when(this.patientDao.getPatientById(1)).thenReturn(patient);
		assertEquals(patient, patientServiceImpl.getPatientById(1));

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

		when(this.patientDao.getPatientJsonForBulkData(integerList, start)).thenReturn(patientList);
		assertEquals(patientList, patientServiceImpl.getPatientJsonForBulkData(integerList, start));
	}

}
