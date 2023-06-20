package com.interopx.fhir.auth.server.controller;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import java.io.File;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;

import com.interopx.fhir.auth.server.model.AuthorizationDetails;
import com.interopx.fhir.auth.server.model.Clients;
import com.interopx.fhir.auth.server.service.AuthorizationDetailsService;
import com.interopx.fhir.auth.server.service.ClientsService;

@SpringBootTest
class TokenEndPointTest {
	
	@InjectMocks
	TokenEndPoint tokenEndPoint;
	
	@Mock
	ClientsService clientService;
	
	@Mock
	AuthorizationDetailsService authTempService;
	

	@Test
	void getAuthorization() throws Exception {
		
		MockHttpServletRequest request = new MockHttpServletRequest();

		HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
		
		request.addHeader("Authorization", "KwI5dY8SX2xnJ-uJ4CZzfFl6ppqAp7yQcJbFyJD0mbIGHMvCV04BedAMei4yQseRwHFfBkPYesECR2njzffyB5QJsZ04MrpK57CldfVhhpGG51wawmKevIMI4w3EuzCnFgrkR2juKvWp3Z3BK1aVvLziEfpF4drkX9s6tulqmxyasNYeGjH7u-BH-nDI0BVg1IFTMT6R3NmMA9QiE1fHTOO9xalxxb2tIOf28TPBNvcTr3cCp2SYam-9Bx");
		request.setParameter("grant_type", "client_credentials");
		request.addParameter("refresh_token", "KwI5dY8SX2xnJ-uJ4CZzfFl6ppqAp7yQcJbFyJD0mbIGHMvCV04BedAMei4yQseRwHFfBkPYesECR2njzffyB5QJsZ04MrpK57CldfVhhpGG51wawmKevIMI4w3EuzCnFgrkR2juKvWp3Z3BK1aVvLziEfpF4drkX9s6tulqmxyasNYeGjH7u-BH-nDI0BVg1IFTMT6R3NmMA9QiE1fHTOO9xalxxb2tIOf28TPBNvcTr3cCp2SYam-9Bx");
		request.setParameter("client_id", "BuhZfFFzZPVMV4617Rd9OQsdIPw056");
		request.setParameter("client_secret", "YUE2TmFMWHBtdFh5LUx5ZVhUVHdRd0ZzVXRiTzB3V2Y1ZVVJazd3eEc3NnBpY1poNjU=");
		request.setParameter("scope", "user/Patient.read,user/AllergyIntolerance.read,user/CarePlan.read,user/CareTeam.read,user/Condition.read,user/Device.read,user/DiagnosticReport.read,user/DocumentReference.read,user/Encounter.read,user/Goal.read,user/Immunization.read,user/Location.read,user/MedicationRequest.read,user/Medication.read,user/Observation.read,user/Organization.read,user/Practitioner.read,user/PractitionerRole.read,user/Procedure.read,user/Provenance.read,user/RelatedPerson.read,patient/Medication.read,patient/Location.read,patient/Immunization.read,patiet/Goal.read,patient/Encounter.read,patient/DocumentReference.read,patient/DiagnosticReport.read,patient/Device.read,patient/Condition.read,patient/CareTeam.read,patient/CarePlan.read,patient/AllergyIntolerance.read,patient/Patient.read,patient/*.read,online_access,offline_access,fhirUser,openid,fhir_complete,launch/patient,launch,patient/MedicationRequest.read,patient/Observation.read,patient/Organization.read,patient/Practitioner.read,patient/PractitionerRole.read,patient/Procedure.read,patient/Provenance.read,patient/RelatedPerson.read");
		request.setParameter("client_assertion_type", "urn:ietf:params:oauth:client-assertion-type:jwt-bearer");
		request.setParameter("client_assertion", "eyJhbGciOiJSUzM4NCIsInR5cCI6IkpXVCJ9.eyJhdWQiOiJhcmNhZGlhIiwiaXNzIjoiLWtXN0F2RUxkOEFDZ05VaTZOSldGYmw4WSIsInN1YiI6Ii1rVzdBdkVMZDhBQ2dOVWk2TkpXRmJsOFkiLCJqdGkiOiIwNDIxNGYxNy04MmE2LTRkYTYtOGE1YS1mOGRjZjM5ZDJmOGYiLCJpYXQiOjE2NTYzMjM0MDYsImV4cCI6MTY1NjMzMzQwNn0.vD4wtrjN82_LFQBYQMF6WaTOKlNZiErLmQxLm90yiGwpbxYA6xBof2FH1bKm15sf4GTfWF5Dovp-Kic8dmv7yyQ0BwwJPCGsKl5J3DPE0DbDXYt4qAuu7vHVrQPs08LO_xTTcl2pbtWZw6MM8UzgXFCk2GMmF0hCybRf8b9sl6MUoQc8y5b3PDAAQ-hvaFF2hM1_ZOtn4BoLNwN2x88PwVCqzWRZtfo6UT5lf3o2roMlEHDf6cZ-s5oHtGBpnTsbdkGccGXeyL5zwMZGdPlcUaiJnXR3aMXr0-f7-N8cU-P3CXdD6lCJHQeE3pprsEu83J7BiHtxHD0RRhCYFpCbsQ");
				
		tokenEndPoint.getAuthorization(request, response);

	}
	
	@Test
	void getAuthorization1() throws Exception {
		
		MockHttpServletRequest request = new MockHttpServletRequest();

		HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
		
		request.setParameter("grant_type", "client_credentials");
		request.addParameter("refresh_token", "KwI5dY8SX2xnJ-uJ4CZzfFl6ppqAp7yQcJbFyJD0mbIGHMvCV04BedAMei4yQseRwHFfBkPYesECR2njzffyB5QJsZ04MrpK57CldfVhhpGG51wawmKevIMI4w3EuzCnFgrkR2juKvWp3Z3BK1aVvLziEfpF4drkX9s6tulqmxyasNYeGjH7u-BH-nDI0BVg1IFTMT6R3NmMA9QiE1fHTOO9xalxxb2tIOf28TPBNvcTr3cCp2SYam-9Bx");
		request.setParameter("client_id", "BuhZfFFzZPVMV4617Rd9OQsdIPw056");
		request.setParameter("client_secret", "YUE2TmFMWHBtdFh5LUx5ZVhUVHdRd0ZzVXRiTzB3V2Y1ZVVJazd3eEc3NnBpY1poNjU=");
		request.setParameter("scope", "user/Patient.read,user/AllergyIntolerance.read,user/CarePlan.read,user/CareTeam.read,user/Condition.read,user/Device.read,user/DiagnosticReport.read,user/DocumentReference.read,user/Encounter.read,user/Goal.read,user/Immunization.read,user/Location.read,user/MedicationRequest.read,user/Medication.read,user/Observation.read,user/Organization.read,user/Practitioner.read,user/PractitionerRole.read,user/Procedure.read,user/Provenance.read,user/RelatedPerson.read,patient/Medication.read,patient/Location.read,patient/Immunization.read,patiet/Goal.read,patient/Encounter.read,patient/DocumentReference.read,patient/DiagnosticReport.read,patient/Device.read,patient/Condition.read,patient/CareTeam.read,patient/CarePlan.read,patient/AllergyIntolerance.read,patient/Patient.read,patient/*.read,online_access,offline_access,fhirUser,openid,fhir_complete,launch/patient,launch,patient/MedicationRequest.read,patient/Observation.read,patient/Organization.read,patient/Practitioner.read,patient/PractitionerRole.read,patient/Procedure.read,patient/Provenance.read,patient/RelatedPerson.read");
		request.setParameter("client_assertion_type", "urn:ietf:params:oauth:client-assertion-type:jwt-bearer");
		request.setParameter("client_assertion", "eyJhbGciOiJSUzM4NCIsInR5cCI6IkpXVCJ9.eyJhdWQiOiJhcmNhZGlhIiwiaXNzIjoiLWtXN0F2RUxkOEFDZ05VaTZOSldGYmw4WSIsInN1YiI6Ii1rVzdBdkVMZDhBQ2dOVWk2TkpXRmJsOFkiLCJqdGkiOiIwNDIxNGYxNy04MmE2LTRkYTYtOGE1YS1mOGRjZjM5ZDJmOGYiLCJpYXQiOjE2NTYzMjM0MDYsImV4cCI6MTY1NjMzMzQwNn0.vD4wtrjN82_LFQBYQMF6WaTOKlNZiErLmQxLm90yiGwpbxYA6xBof2FH1bKm15sf4GTfWF5Dovp-Kic8dmv7yyQ0BwwJPCGsKl5J3DPE0DbDXYt4qAuu7vHVrQPs08LO_xTTcl2pbtWZw6MM8UzgXFCk2GMmF0hCybRf8b9sl6MUoQc8y5b3PDAAQ-hvaFF2hM1_ZOtn4BoLNwN2x88PwVCqzWRZtfo6UT5lf3o2roMlEHDf6cZ-s5oHtGBpnTsbdkGccGXeyL5zwMZGdPlcUaiJnXR3aMXr0-f7-N8cU-P3CXdD6lCJHQeE3pprsEu83J7BiHtxHD0RRhCYFpCbsQ");
				
		Clients client = new Clients();
		client.setCenterId("152");
		
		when(clientService.getClient(Mockito.any())).thenReturn(client);
		
		tokenEndPoint.getAuthorization(request, response);

	}
	
	@Test
	void getAuthorization2() throws Exception {
		
		MockHttpServletRequest request = new MockHttpServletRequest();

		HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
				
		request.setParameter("grant_type", "client_credentials");
		request.addParameter("refresh_token", "KwI5dY8SX2xnJ-uJ4CZzfFl6ppqAp7yQcJbFyJD0mbIGHMvCV04BedAMei4yQseRwHFfBkPYesECR2njzffyB5QJsZ04MrpK57CldfVhhpGG51wawmKevIMI4w3EuzCnFgrkR2juKvWp3Z3BK1aVvLziEfpF4drkX9s6tulqmxyasNYeGjH7u-BH-nDI0BVg1IFTMT6R3NmMA9QiE1fHTOO9xalxxb2tIOf28TPBNvcTr3cCp2SYam-9Bx");
		request.setParameter("client_secret", "YUE2TmFMWHBtdFh5LUx5ZVhUVHdRd0ZzVXRiTzB3V2Y1ZVVJazd3eEc3NnBpY1poNjU=");
		request.setParameter("scope", "user/Patient.read,user/AllergyIntolerance.read,user/CarePlan.read,user/CareTeam.read,user/Condition.read,user/Device.read,user/DiagnosticReport.read,user/DocumentReference.read,user/Encounter.read,user/Goal.read,user/Immunization.read,user/Location.read,user/MedicationRequest.read,user/Medication.read,user/Observation.read,user/Organization.read,user/Practitioner.read,user/PractitionerRole.read,user/Procedure.read,user/Provenance.read,user/RelatedPerson.read,patient/Medication.read,patient/Location.read,patient/Immunization.read,patiet/Goal.read,patient/Encounter.read,patient/DocumentReference.read,patient/DiagnosticReport.read,patient/Device.read,patient/Condition.read,patient/CareTeam.read,patient/CarePlan.read,patient/AllergyIntolerance.read,patient/Patient.read,patient/*.read,online_access,offline_access,fhirUser,openid,fhir_complete,launch/patient,launch,patient/MedicationRequest.read,patient/Observation.read,patient/Organization.read,patient/Practitioner.read,patient/PractitionerRole.read,patient/Procedure.read,patient/Provenance.read,patient/RelatedPerson.read");
		request.setParameter("client_assertion_type", "urn:ietf:params:oauth:client-assertion-type:jwt-bearer");
		request.setParameter("client_assertion", "eyJhbGciOiJSUzM4NCIsInR5cCI6IkpXVCJ9.eyJhdWQiOiJhcmNhZGlhIiwiaXNzIjoiLWtXN0F2RUxkOEFDZ05VaTZOSldGYmw4WSIsInN1YiI6Ii1rVzdBdkVMZDhBQ2dOVWk2TkpXRmJsOFkiLCJqdGkiOiIwNDIxNGYxNy04MmE2LTRkYTYtOGE1YS1mOGRjZjM5ZDJmOGYiLCJpYXQiOjE2NTYzMjM0MDYsImV4cCI6MTY1NjMzMzQwNn0.vD4wtrjN82_LFQBYQMF6WaTOKlNZiErLmQxLm90yiGwpbxYA6xBof2FH1bKm15sf4GTfWF5Dovp-Kic8dmv7yyQ0BwwJPCGsKl5J3DPE0DbDXYt4qAuu7vHVrQPs08LO_xTTcl2pbtWZw6MM8UzgXFCk2GMmF0hCybRf8b9sl6MUoQc8y5b3PDAAQ-hvaFF2hM1_ZOtn4BoLNwN2x88PwVCqzWRZtfo6UT5lf3o2roMlEHDf6cZ-s5oHtGBpnTsbdkGccGXeyL5zwMZGdPlcUaiJnXR3aMXr0-f7-N8cU-P3CXdD6lCJHQeE3pprsEu83J7BiHtxHD0RRhCYFpCbsQ");
		
		tokenEndPoint.getAuthorization(request, response);

	}
	
	@Test
	void getAuthorization3() throws Exception {
		
		MockHttpServletRequest request = new MockHttpServletRequest();

		HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
		
		request.addHeader("Authorization", "KwI5dY8SX2xnJ-uJ4CZzfFl6ppqAp7yQcJbFyJD0mbIGHMvCV04BedAMei4yQseRwHFfBkPYesECR2njzffyB5QJsZ04MrpK57CldfVhhpGG51wawmKevIMI4w3EuzCnFgrkR2juKvWp3Z3BK1aVvLziEfpF4drkX9s6tulqmxyasNYeGjH7u-BH-nDI0BVg1IFTMT6R3NmMA9QiE1fHTOO9xalxxb2tIOf28TPBNvcTr3cCp2SYam-9Bx");
		request.setParameter("grant_type", "client_credentials");
		request.addParameter("refresh_token", "KwI5dY8SX2xnJ-uJ4CZzfFl6ppqAp7yQcJbFyJD0mbIGHMvCV04BedAMei4yQseRwHFfBkPYesECR2njzffyB5QJsZ04MrpK57CldfVhhpGG51wawmKevIMI4w3EuzCnFgrkR2juKvWp3Z3BK1aVvLziEfpF4drkX9s6tulqmxyasNYeGjH7u-BH-nDI0BVg1IFTMT6R3NmMA9QiE1fHTOO9xalxxb2tIOf28TPBNvcTr3cCp2SYam-9Bx");
		request.setParameter("client_id", "BuhZfFFzZPVMV4617Rd9OQsdIPw056");
		request.setParameter("client_secret", "YUE2TmFMWHBtdFh5LUx5ZVhUVHdRd0ZzVXRiTzB3V2Y1ZVVJazd3eEc3NnBpY1poNjU=");
		request.setParameter("scope", "user/Patient.read,user/AllergyIntolerance.read,user/CarePlan.read,user/CareTeam.read,user/Condition.read,user/Device.read,user/DiagnosticReport.read,user/DocumentReference.read,user/Encounter.read,user/Goal.read,user/Immunization.read,user/Location.read,user/MedicationRequest.read,user/Medication.read,user/Observation.read,user/Organization.read,user/Practitioner.read,user/PractitionerRole.read,user/Procedure.read,user/Provenance.read,user/RelatedPerson.read,patient/Medication.read,patient/Location.read,patient/Immunization.read,patiet/Goal.read,patient/Encounter.read,patient/DocumentReference.read,patient/DiagnosticReport.read,patient/Device.read,patient/Condition.read,patient/CareTeam.read,patient/CarePlan.read,patient/AllergyIntolerance.read,patient/Patient.read,patient/*.read,online_access,offline_access,fhirUser,openid,fhir_complete,launch/patient,launch,patient/MedicationRequest.read,patient/Observation.read,patient/Organization.read,patient/Practitioner.read,patient/PractitionerRole.read,patient/Procedure.read,patient/Provenance.read,patient/RelatedPerson.read");
		request.setParameter("client_assertion_type", "urn:ietf:params:oauth:client-assertion-type:jwt-bearer");
		
		tokenEndPoint.getAuthorization(request, response);

	}
	
	@Test
	void getAuthorization4() throws Exception {
		
		MockHttpServletRequest request = new MockHttpServletRequest();

		HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
		
		request.addHeader("Authorization", "KwI5dY8SX2xnJ-uJ4CZzfFl6ppqAp7yQcJbFyJD0mbIGHMvCV04BedAMei4yQseRwHFfBkPYesECR2njzffyB5QJsZ04MrpK57CldfVhhpGG51wawmKevIMI4w3EuzCnFgrkR2juKvWp3Z3BK1aVvLziEfpF4drkX9s6tulqmxyasNYeGjH7u-BH-nDI0BVg1IFTMT6R3NmMA9QiE1fHTOO9xalxxb2tIOf28TPBNvcTr3cCp2SYam-9Bx");
		request.setParameter("grant_type", "client_credentials");
		request.addParameter("refresh_token", "KwI5dY8SX2xnJ-uJ4CZzfFl6ppqAp7yQcJbFyJD0mbIGHMvCV04BedAMei4yQseRwHFfBkPYesECR2njzffyB5QJsZ04MrpK57CldfVhhpGG51wawmKevIMI4w3EuzCnFgrkR2juKvWp3Z3BK1aVvLziEfpF4drkX9s6tulqmxyasNYeGjH7u-BH-nDI0BVg1IFTMT6R3NmMA9QiE1fHTOO9xalxxb2tIOf28TPBNvcTr3cCp2SYam-9Bx");
		request.setParameter("client_id", "BuhZfFFzZPVMV4617Rd9OQsdIPw056");
		request.setParameter("client_secret", "YUE2TmFMWHBtdFh5LUx5ZVhUVHdRd0ZzVXRiTzB3V2Y1ZVVJazd3eEc3NnBpY1poNjU=");
		request.setParameter("scope", "user/Patient.read,user/AllergyIntolerance.read,user/CarePlan.read,user/CareTeam.read,user/Condition.read,user/Device.read,user/DiagnosticReport.read,user/DocumentReference.read,user/Encounter.read,user/Goal.read,user/Immunization.read,user/Location.read,user/MedicationRequest.read,user/Medication.read,user/Observation.read,user/Organization.read,user/Practitioner.read,user/PractitionerRole.read,user/Procedure.read,user/Provenance.read,user/RelatedPerson.read,patient/Medication.read,patient/Location.read,patient/Immunization.read,patiet/Goal.read,patient/Encounter.read,patient/DocumentReference.read,patient/DiagnosticReport.read,patient/Device.read,patient/Condition.read,patient/CareTeam.read,patient/CarePlan.read,patient/AllergyIntolerance.read,patient/Patient.read,patient/*.read,online_access,offline_access,fhirUser,openid,fhir_complete,launch/patient,launch,patient/MedicationRequest.read,patient/Observation.read,patient/Organization.read,patient/Practitioner.read,patient/PractitionerRole.read,patient/Procedure.read,patient/Provenance.read,patient/RelatedPerson.read");
		request.setParameter("client_assertion", "eyJhbGciOiJSUzM4NCIsInR5cCI6IkpXVCJ9.eyJhdWQiOiJhcmNhZGlhIiwiaXNzIjoiLWtXN0F2RUxkOEFDZ05VaTZOSldGYmw4WSIsInN1YiI6Ii1rVzdBdkVMZDhBQ2dOVWk2TkpXRmJsOFkiLCJqdGkiOiIwNDIxNGYxNy04MmE2LTRkYTYtOGE1YS1mOGRjZjM5ZDJmOGYiLCJpYXQiOjE2NTYzMjM0MDYsImV4cCI6MTY1NjMzMzQwNn0.vD4wtrjN82_LFQBYQMF6WaTOKlNZiErLmQxLm90yiGwpbxYA6xBof2FH1bKm15sf4GTfWF5Dovp-Kic8dmv7yyQ0BwwJPCGsKl5J3DPE0DbDXYt4qAuu7vHVrQPs08LO_xTTcl2pbtWZw6MM8UzgXFCk2GMmF0hCybRf8b9sl6MUoQc8y5b3PDAAQ-hvaFF2hM1_ZOtn4BoLNwN2x88PwVCqzWRZtfo6UT5lf3o2roMlEHDf6cZ-s5oHtGBpnTsbdkGccGXeyL5zwMZGdPlcUaiJnXR3aMXr0-f7-N8cU-P3CXdD6lCJHQeE3pprsEu83J7BiHtxHD0RRhCYFpCbsQ");
		
		tokenEndPoint.getAuthorization(request, response);

	}
	
	@Test
	void getAuthorization5() throws Exception {
		
		MockHttpServletRequest request = new MockHttpServletRequest();

		HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
		
		request.addHeader("Authorization", "KwI5dY8SX2xnJ-uJ4CZzfFl6ppqAp7yQcJbFyJD0mbIGHMvCV04BedAMei4yQseRwHFfBkPYesECR2njzffyB5QJsZ04MrpK57CldfVhhpGG51wawmKevIMI4w3EuzCnFgrkR2juKvWp3Z3BK1aVvLziEfpF4drkX9s6tulqmxyasNYeGjH7u-BH-nDI0BVg1IFTMT6R3NmMA9QiE1fHTOO9xalxxb2tIOf28TPBNvcTr3cCp2SYam-9Bx");
		request.setParameter("grant_type", "client_credentials");
		request.addParameter("refresh_token", "KwI5dY8SX2xnJ-uJ4CZzfFl6ppqAp7yQcJbFyJD0mbIGHMvCV04BedAMei4yQseRwHFfBkPYesECR2njzffyB5QJsZ04MrpK57CldfVhhpGG51wawmKevIMI4w3EuzCnFgrkR2juKvWp3Z3BK1aVvLziEfpF4drkX9s6tulqmxyasNYeGjH7u-BH-nDI0BVg1IFTMT6R3NmMA9QiE1fHTOO9xalxxb2tIOf28TPBNvcTr3cCp2SYam-9Bx");
		request.setParameter("client_id", "BuhZfFFzZPVMV4617Rd9OQsdIPw056");
		request.setParameter("client_secret", "YUE2TmFMWHBtdFh5LUx5ZVhUVHdRd0ZzVXRiTzB3V2Y1ZVVJazd3eEc3NnBpY1poNjU=");
		request.setParameter("client_assertion_type", "urn:ietf:params:oauth:client-assertion-type:jwt-bearer");
		request.setParameter("client_assertion", "eyJhbGciOiJSUzM4NCIsInR5cCI6IkpXVCJ9.eyJhdWQiOiJhcmNhZGlhIiwiaXNzIjoiLWtXN0F2RUxkOEFDZ05VaTZOSldGYmw4WSIsInN1YiI6Ii1rVzdBdkVMZDhBQ2dOVWk2TkpXRmJsOFkiLCJqdGkiOiIwNDIxNGYxNy04MmE2LTRkYTYtOGE1YS1mOGRjZjM5ZDJmOGYiLCJpYXQiOjE2NTYzMjM0MDYsImV4cCI6MTY1NjMzMzQwNn0.vD4wtrjN82_LFQBYQMF6WaTOKlNZiErLmQxLm90yiGwpbxYA6xBof2FH1bKm15sf4GTfWF5Dovp-Kic8dmv7yyQ0BwwJPCGsKl5J3DPE0DbDXYt4qAuu7vHVrQPs08LO_xTTcl2pbtWZw6MM8UzgXFCk2GMmF0hCybRf8b9sl6MUoQc8y5b3PDAAQ-hvaFF2hM1_ZOtn4BoLNwN2x88PwVCqzWRZtfo6UT5lf3o2roMlEHDf6cZ-s5oHtGBpnTsbdkGccGXeyL5zwMZGdPlcUaiJnXR3aMXr0-f7-N8cU-P3CXdD6lCJHQeE3pprsEu83J7BiHtxHD0RRhCYFpCbsQ");
		
		tokenEndPoint.getAuthorization(request, response);

	}
	
//	@Test
	void getAuthorization6() throws Exception {
		
		MockHttpServletRequest request = new MockHttpServletRequest();
        
		HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
		
		request.setParameter("grant_type", "client_credentials");
		request.addParameter("refresh_token", "KwI5dY8SX2xnJ-uJ4CZzfFl6ppqAp7yQcJbFyJD0mbIGHMvCV04BedAMei4yQseRwHFfBkPYesECR2njzffyB5QJsZ04MrpK57CldfVhhpGG51wawmKevIMI4w3EuzCnFgrkR2juKvWp3Z3BK1aVvLziEfpF4drkX9s6tulqmxyasNYeGjH7u-BH-nDI0BVg1IFTMT6R3NmMA9QiE1fHTOO9xalxxb2tIOf28TPBNvcTr3cCp2SYam-9Bx");
		request.setParameter("client_id", "BuhZfFFzZPVMV4617Rd9OQsdIPw056");
		request.setParameter("client_secret", "YUE2TmFMWHBtdFh5LUx5ZVhUVHdRd0ZzVXRiTzB3V2Y1ZVVJazd3eEc3NnBpY1poNjU=");
		request.setParameter("scope", "user/Patient.read,user/AllergyIntolerance.read,user/CarePlan.read,user/CareTeam.read,user/Condition.read,user/Device.read,user/DiagnosticReport.read,user/DocumentReference.read,user/Encounter.read,user/Goal.read,user/Immunization.read,user/Location.read,user/MedicationRequest.read,user/Medication.read,user/Observation.read,user/Organization.read,user/Practitioner.read,user/PractitionerRole.read,user/Procedure.read,user/Provenance.read,user/RelatedPerson.read,patient/Medication.read,patient/Location.read,patient/Immunization.read,patiet/Goal.read,patient/Encounter.read,patient/DocumentReference.read,patient/DiagnosticReport.read,patient/Device.read,patient/Condition.read,patient/CareTeam.read,patient/CarePlan.read,patient/AllergyIntolerance.read,patient/Patient.read,patient/*.read,online_access,offline_access,fhirUser,openid,fhir_complete,launch/patient,launch,patient/MedicationRequest.read,patient/Observation.read,patient/Organization.read,patient/Practitioner.read,patient/PractitionerRole.read,patient/Procedure.read,patient/Provenance.read,patient/RelatedPerson.read");
		request.setParameter("client_assertion_type", "urn:ietf:params:oauth:client-assertion-type:jwt-bearer");
		request.setParameter("client_assertion", "eyJhbGciOiJSUzM4NCIsInR5cCI6IkpXVCJ9.eyJhdWQiOiJhcmNhZGlhIiwiaXNzIjoiLWtXN0F2RUxkOEFDZ05VaTZOSldGYmw4WSIsInN1YiI6Ii1rVzdBdkVMZDhBQ2dOVWk2TkpXRmJsOFkiLCJqdGkiOiIwNDIxNGYxNy04MmE2LTRkYTYtOGE1YS1mOGRjZjM5ZDJmOGYiLCJpYXQiOjE2NTYzMjM0MDYsImV4cCI6MTY1NjMzMzQwNn0.vD4wtrjN82_LFQBYQMF6WaTOKlNZiErLmQxLm90yiGwpbxYA6xBof2FH1bKm15sf4GTfWF5Dovp-Kic8dmv7yyQ0BwwJPCGsKl5J3DPE0DbDXYt4qAuu7vHVrQPs08LO_xTTcl2pbtWZw6MM8UzgXFCk2GMmF0hCybRf8b9sl6MUoQc8y5b3PDAAQ-hvaFF2hM1_ZOtn4BoLNwN2x88PwVCqzWRZtfo6UT5lf3o2roMlEHDf6cZ-s5oHtGBpnTsbdkGccGXeyL5zwMZGdPlcUaiJnXR3aMXr0-f7-N8cU-P3CXdD6lCJHQeE3pprsEu83J7BiHtxHD0RRhCYFpCbsQ");
				
		Clients client = new Clients();
		client.setCenterId("152");
		client.setPublicKey("123456789");
		client.setFiles("test");
		
		
		when(clientService.getClient(Mockito.any())).thenReturn(client);
		
		Map<String, Object> body = new HashMap<>();
		body.put("test","123");
		
		 String contextPath = System.getProperty("catalina.base");
         String mainDirPath = client.getPublicKey(); 
         String fileName = client.getFiles();
         
		File publicFile = new File(contextPath + mainDirPath + fileName);
		
//		Mockito.when(publicFile.exists()).thenReturn(true);
        
//		JSONObject jwtBody = Mockito.mock(JSONObject.class); 
		
//		JSONObject jwtBody = PowerMockito.mock(JSONObject.class);
		
//		Mockito.when(jwtBody.getString(Mockito.anyString())).thenReturn(null);
		
//		Mockito.lenient().doReturn(null).when(jwtBody.getString(Mockito.any()));
		
		tokenEndPoint.getAuthorization(request, response);

	}
	
	@Test
	void introspectToken() throws ParseException {
		
		MockHttpServletRequest request = new MockHttpServletRequest();

		HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
		
		request.addHeader("Authorization", "KwI5dY8SX2xnJ-uJ4CZzfFl6ppqAp7yQcJbFyJD0mbIGHMvCV04BedAMei4yQseRwHFfBkPYesECR2njzffyB5QJsZ04MrpK57CldfVhhpGG51wawmKevIMI4w3EuzCnFgrkR2juKvWp3Z3BK1aVvLziEfpF4drkX9s6tulqmxyasNYeGjH7u-BH-nDI0BVg1IFTMT6R3NmMA9QiE1fHTOO9xalxxb2tIOf28TPBNvcTr3cCp2SYam-9Bx");
		request.setParameter("client_id", "BuhZfFFzZPVMV4617Rd9OQsdIPw056");
		request.setParameter("token", "290c002337428c4f00b3ec4ddf962a16");
		String clientId = "BuhZfFFzZPVMV4617Rd9OQsdIPw056";
		String token = "290c002337428c4f00b3ec4ddf962a16";
		
		AuthorizationDetails authDetails = new AuthorizationDetails();
		authDetails.setClientId(clientId);
		authDetails.setAccessToken(clientId);
		
		String date = "2024-02-12 00:15:32";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    Date date1 = sdf.parse(date);  
		
		Timestamp timestamp = new Timestamp(date1.getTime());
            
		authDetails.setExpiry(timestamp.toString());
		
		Mockito.when(authTempService.getAuthById(Mockito.any())).thenReturn(authDetails);
		
		assertNotNull(tokenEndPoint.introspectToken(request, response));
		
	}
	
	@Test
	void introspectToken1() throws ParseException {
		
		MockHttpServletRequest request = new MockHttpServletRequest();

		HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
		
		request.addHeader("Authorization", "KwI5dY8SX2xnJ-uJ4CZzfFl6ppqAp7yQcJbFyJD0mbIGHMvCV04BedAMei4yQseRwHFfBkPYesECR2njzffyB5QJsZ04MrpK57CldfVhhpGG51wawmKevIMI4w3EuzCnFgrkR2juKvWp3Z3BK1aVvLziEfpF4drkX9s6tulqmxyasNYeGjH7u-BH-nDI0BVg1IFTMT6R3NmMA9QiE1fHTOO9xalxxb2tIOf28TPBNvcTr3cCp2SYam-9Bx");
		request.setParameter("client_id", "BuhZfFFzZPVMV4617Rd9OQsdIPw056");
		request.setParameter("token", "290c002337428c4f00b3ec4ddf962a16");
		String clientId = "BuhZfFFzZPVMV4617Rd9OQsdIPw056";
		String token = "290c002337428c4f00b3ec4ddf962a16";
		
		AuthorizationDetails authDetails = new AuthorizationDetails();
		authDetails.setClientId(clientId);
		authDetails.setAccessToken(token);
		
		String date = "2024-02-12 00:15:32";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    Date date1 = sdf.parse(date);  
		
		Timestamp timestamp = new Timestamp(date1.getTime());
            
		authDetails.setExpiry(timestamp.toString());
		
		Mockito.when(authTempService.getAuthById(Mockito.any())).thenReturn(authDetails);
		
		assertNotNull(tokenEndPoint.introspectToken(request, response));
		
	}
	
	@Test
	void introspectToken2() throws ParseException {
		
		MockHttpServletRequest request = new MockHttpServletRequest();

		HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
		
		request.addHeader("Authorization", "KwI5dY8SX2xnJ-uJ4CZzfFl6ppqAp7yQcJbFyJD0mbIGHMvCV04BedAMei4yQseRwHFfBkPYesECR2njzffyB5QJsZ04MrpK57CldfVhhpGG51wawmKevIMI4w3EuzCnFgrkR2juKvWp3Z3BK1aVvLziEfpF4drkX9s6tulqmxyasNYeGjH7u-BH-nDI0BVg1IFTMT6R3NmMA9QiE1fHTOO9xalxxb2tIOf28TPBNvcTr3cCp2SYam-9Bx");
		request.setParameter("client_id", "BuhZfFFzZPVMV4617Rd9OQsdIPw056");
		request.setParameter("token", "290c002337428c4f00b3ec4ddf962a16");
		
		Mockito.when(authTempService.getAuthById(Mockito.any())).thenReturn(null);
		
		assertNotNull(tokenEndPoint.introspectToken(request, response));
		
	}
	
	@Test
	void introspectToken3() {
		
		MockHttpServletRequest request = new MockHttpServletRequest();

		HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
		
		assertNotNull(tokenEndPoint.introspectToken(request, response));
	}
	
	@Test
	void revokeToken() throws ParseException {
		
		MockHttpServletRequest request = new MockHttpServletRequest();

		HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
		
		request.addHeader("Authorization", "KwI5dY8SX2xnJ-uJ4CZzfFl6ppqAp7yQcJbFyJD0mbIGHMvCV04BedAMei4yQseRwHFfBkPYesECR2njzffyB5QJsZ04MrpK57CldfVhhpGG51wawmKevIMI4w3EuzCnFgrkR2juKvWp3Z3BK1aVvLziEfpF4drkX9s6tulqmxyasNYeGjH7u-BH-nDI0BVg1IFTMT6R3NmMA9QiE1fHTOO9xalxxb2tIOf28TPBNvcTr3cCp2SYam-9Bx");
		request.setParameter("client_id", "BuhZfFFzZPVMV4617Rd9OQsdIPw056");
		request.setParameter("token", "290c002337428c4f00b3ec4ddf962a16");
		request.setParameter("token_type", "access_token");
		
		String clientId = "BuhZfFFzZPVMV4617Rd9OQsdIPw056";
		String token = "290c002337428c4f00b3ec4ddf962a16";
		
		AuthorizationDetails authDetails = new AuthorizationDetails();
		authDetails.setClientId(clientId);
		authDetails.setAccessToken(token);
		
		Mockito.when(authTempService.getAuthById(Mockito.any())).thenReturn(authDetails);
		
		assertNotNull(tokenEndPoint.revokeToken(request, response));
	}
	
	@Test
	void revokeToken1() throws ParseException {
		
		MockHttpServletRequest request = new MockHttpServletRequest();

		HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
		
		request.addHeader("Authorization", "KwI5dY8SX2xnJ-uJ4CZzfFl6ppqAp7yQcJbFyJD0mbIGHMvCV04BedAMei4yQseRwHFfBkPYesECR2njzffyB5QJsZ04MrpK57CldfVhhpGG51wawmKevIMI4w3EuzCnFgrkR2juKvWp3Z3BK1aVvLziEfpF4drkX9s6tulqmxyasNYeGjH7u-BH-nDI0BVg1IFTMT6R3NmMA9QiE1fHTOO9xalxxb2tIOf28TPBNvcTr3cCp2SYam-9Bx");
		request.setParameter("client_id", "BuhZfFFzZPVMV4617Rd9OQsdIPw056");
		request.setParameter("token", "290c002337428c4f00b3ec4ddf962a16");
		request.setParameter("token_type", "refresh_token");
		
		String clientId = "BuhZfFFzZPVMV4617Rd9OQsdIPw056";
		String token = "290c002337428c4f00b3ec4ddf962a16";
		
		AuthorizationDetails authDetails = new AuthorizationDetails();
		authDetails.setClientId(clientId);
		authDetails.setRefreshToken(token);
		
		Mockito.when(authTempService.getAuthById(Mockito.any())).thenReturn(authDetails);
		
		assertNotNull(tokenEndPoint.revokeToken(request, response));
	}
	
	@Test
	void revokeToken2() throws ParseException {
		
		MockHttpServletRequest request = new MockHttpServletRequest();

		HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
		
		request.addHeader("Authorization", "KwI5dY8SX2xnJ-uJ4CZzfFl6ppqAp7yQcJbFyJD0mbIGHMvCV04BedAMei4yQseRwHFfBkPYesECR2njzffyB5QJsZ04MrpK57CldfVhhpGG51wawmKevIMI4w3EuzCnFgrkR2juKvWp3Z3BK1aVvLziEfpF4drkX9s6tulqmxyasNYeGjH7u-BH-nDI0BVg1IFTMT6R3NmMA9QiE1fHTOO9xalxxb2tIOf28TPBNvcTr3cCp2SYam-9Bx");
		request.setParameter("client_id", "BuhZfFFzZPVMV4617Rd9OQsdIPw056");
		request.setParameter("token", "290c002337428c4f00b3ec4ddf962a16");
		request.setParameter("token_type", "refresh_token");
		
		Mockito.when(authTempService.getAuthById(Mockito.any())).thenReturn(null);
		
		assertNotNull(tokenEndPoint.revokeToken(request, response));
	}
	
	@Test
	void revokeToken3() throws ParseException {
		
		MockHttpServletRequest request = new MockHttpServletRequest();

		HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
				
		assertNotNull(tokenEndPoint.revokeToken(request, response));
	}
	
	
	

}
