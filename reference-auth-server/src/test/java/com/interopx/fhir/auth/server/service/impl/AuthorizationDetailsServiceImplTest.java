package com.interopx.fhir.auth.server.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import com.interopx.fhir.auth.server.dao.AuthorizationDetailsDao;
import com.interopx.fhir.auth.server.model.AuthorizationDetails;

@SpringBootTest
class AuthorizationDetailsServiceImplTest {

	@Mock
	AuthorizationDetailsDao authTempDao;

	@InjectMocks
	AuthorizationDetailsServiceImpl authorizationDetailsServiceImpl;

	@Test
	void saveOrUpdate() {
		AuthorizationDetails authorizationDetails = new AuthorizationDetails();
		authorizationDetails.setClientId("n-WW3PPCp-41DdYX2-i0Ksihe");
		authorizationDetails.setClientId("YUE2TmFMWHBtdFh5LUx5ZVhUVHdRd0ZzVXRiTzB3V2Y1ZVVJazd3eEc3NnBpY1poNjU=");
		authorizationDetails.setRedirectUri("http://interopx.com/");
		authorizationDetails.setScope(
				"user/Patient.read,user/AllergyIntolerance.read,user/CarePlan.read,user/CareTeam.read,user/Condition.read,user/Device.read,user/DiagnosticReport.read,user/DocumentReference.read,user/Encounter.read,user/Goal.read,user/Immunization.read,user/Location.read,user/MedicationRequest.read,user/Medication.read,user/Observation.read,user/Organization.read,user/Practitioner.read,user/PractitionerRole.read,user/Procedure.read,user/Provenance.read,user/RelatedPerson.read,patient/Medication.read,patient/Location.read,patient/Immunization.read,patiet/Goal.read,patient/Encounter.read,patient/DocumentReference.read,patient/DiagnosticReport.read,patient/Device.read,patient/Condition.read,patient/CareTeam.read,patient/CarePlan.read,patient/AllergyIntolerance.read,patient/Patient.read,patient/*.read,online_access,offline_access,fhirUser,openid,fhir_complete,launch/patient,launch,patient/MedicationRequest.read,patient/Observation.read,patient/Organization.read,patient/Practitioner.read,patient/PractitionerRole.read,patient/Procedure.read,patient/Provenance.read,patient/RelatedPerson.read");
		authorizationDetails.setAuthCode("40820c50b29fe8eba4f4d864c81a56c2");
		authorizationDetails.setState("state123");
		authorizationDetails.setTransactionId("O2VXV");
		authorizationDetails.setAud("aud");
		authorizationDetails.setLaunchPatientId("crado6.59662");
		authorizationDetails.setCodeChallenge("code");
		authorizationDetails.setCodeChallengeMethod("code challenge");
		when(this.authTempDao.saveOrUpdate(Mockito.any())).thenReturn(authorizationDetails);
		assertEquals(authorizationDetails, authorizationDetailsServiceImpl.saveOrUpdate(authorizationDetails));

	}

	@Test
	void getAuthByClientId() {
		AuthorizationDetails authorizationDetails = new AuthorizationDetails();
		String clientId = "n-WW3PPCp-41DdYX2-i0Ksihe";
		String clientSecret = "YUE2TmFMWHBtdFh5LUx5ZVhUVHdRd0ZzVXRiTzB3V2Y1ZVVJazd3eEc3NnBpY1poNjU=";
		authorizationDetails.setClientId(clientId);
		authorizationDetails.setClientId(clientSecret);
		when(this.authTempDao.getAuthByClientId(clientId, clientSecret)).thenReturn(authorizationDetails);
		assertEquals(authorizationDetails, authorizationDetailsServiceImpl.getAuthByClientId(clientId, clientSecret));
	}
	
	@Test
	void getList() {
		AuthorizationDetails authorizationDetails = new AuthorizationDetails();
		authorizationDetails.setClientId("n-WW3PPCp-41DdYX2-i0Ksihe");
		authorizationDetails.setClientId("YUE2TmFMWHBtdFh5LUx5ZVhUVHdRd0ZzVXRiTzB3V2Y1ZVVJazd3eEc3NnBpY1poNjU=");
		authorizationDetails.setRedirectUri("http://interopx.com/");
		authorizationDetails.setScope(
				"user/Patient.read,user/AllergyIntolerance.read,user/CarePlan.read,user/CareTeam.read,user/Condition.read,user/Device.read,user/DiagnosticReport.read,user/DocumentReference.read,user/Encounter.read,user/Goal.read,user/Immunization.read,user/Location.read,user/MedicationRequest.read,user/Medication.read,user/Observation.read,user/Organization.read,user/Practitioner.read,user/PractitionerRole.read,user/Procedure.read,user/Provenance.read,user/RelatedPerson.read,patient/Medication.read,patient/Location.read,patient/Immunization.read,patiet/Goal.read,patient/Encounter.read,patient/DocumentReference.read,patient/DiagnosticReport.read,patient/Device.read,patient/Condition.read,patient/CareTeam.read,patient/CarePlan.read,patient/AllergyIntolerance.read,patient/Patient.read,patient/*.read,online_access,offline_access,fhirUser,openid,fhir_complete,launch/patient,launch,patient/MedicationRequest.read,patient/Observation.read,patient/Organization.read,patient/Practitioner.read,patient/PractitionerRole.read,patient/Procedure.read,patient/Provenance.read,patient/RelatedPerson.read");
		authorizationDetails.setAuthCode("40820c50b29fe8eba4f4d864c81a56c2");
		authorizationDetails.setState("state123");
		authorizationDetails.setTransactionId("O2VXV");
		authorizationDetails.setAud("aud");
		authorizationDetails.setLaunchPatientId("crado6.59662");
		authorizationDetails.setCodeChallenge("code");
		authorizationDetails.setCodeChallengeMethod("code challenge");		
		List<AuthorizationDetails> authorizationDetailsList = new ArrayList<>();
		authorizationDetailsList.add(authorizationDetails);	
		
		when(this.authTempDao.getList()).thenReturn(authorizationDetailsList);
		assertEquals(authorizationDetailsList, authorizationDetailsServiceImpl.getList());	
	}
	
	@Test
	void validateAccessToken() {
		AuthorizationDetails authorizationDetails = new AuthorizationDetails();
		String accessToken = "b82edf643716d3a596489a700c793201";
		authorizationDetails.setAccessToken(accessToken);
		
		when(this.authTempDao.getAuthorizationByAccessToken(accessToken)).thenReturn(authorizationDetails);
		assertEquals(authorizationDetails, authorizationDetailsServiceImpl.getAuthorizationByAccessToken(accessToken));	
	}
	
	@Test
	void getAuthenticationById() {
		AuthorizationDetails authorizationDetails = new AuthorizationDetails();
		String transactionId = "O2VXV";
		authorizationDetails.setTransactionId(transactionId);
		
		when(this.authTempDao.getAuthenticationById(transactionId)).thenReturn(authorizationDetails);
		assertEquals(authorizationDetails, authorizationDetailsServiceImpl.getAuthenticationById(transactionId));	
	}
	
	@Test
	void getAuthById() {
		AuthorizationDetails authorizationDetails = new AuthorizationDetails();
		String clientId = "b82edf643716d3a596489a700c793201";
		authorizationDetails.setAccessToken(clientId);
		
		when(this.authTempDao.getAuthById(clientId)).thenReturn(authorizationDetails);
		assertEquals(authorizationDetails, authorizationDetailsServiceImpl.getAuthById(clientId));	
	}
		
}
