package com.interopx.fhir.auth.server.controller;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.oltu.oauth2.as.issuer.OAuthIssuer;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import com.interopx.fhir.auth.server.jwtservice.JWKSCache;
import com.interopx.fhir.auth.server.jwtservice.JWTStoredKeyService;
import com.interopx.fhir.auth.server.model.AuthorizationDetails;
import com.interopx.fhir.auth.server.model.Clients;
import com.interopx.fhir.auth.server.service.AuthorizationDetailsService;
import com.interopx.fhir.auth.server.service.ClientsService;
import com.interopx.fhir.auth.server.util.CommonUtil;
import com.interopx.fhir.auth.server.util.AuthUtil.ApprovedStatus;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@DirtiesContext
@ActiveProfiles("test")
class BackendTokenEndpointTest {

	@Mock
	ClientsService service;

	@Mock
	AuthorizationDetailsService dao;

	@Autowired
	BackendTokenEndpoint backendTokenEndpoint;

	@Mock
	CommonUtil commonUtil;

	@Mock
	JWKSCache validationServices;	
	
	@Value("${token.expiration-time}")
	private Integer tokenExpirationTime;


	@Test
	void getAuthorization() throws Exception {

		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();

	    request.addHeader("content-length", "1031"); 
		request.addHeader("putpostman-token", "09216da9-525d-4ca0-8821-33d723a7fa6c");
		request.addHeader("content-type", "application/x-www-form-urlencoded");
		request.addHeader("host", "localhost:8055");
		request.addHeader("connection", "keep-alive");
		request.addHeader("cache-control", "no-cache");
		request.addHeader("accept-encoding", "gzip,eflate, br");
		request.addHeader("accept", "application/json");
		request.addHeader("user-agent", "PostmanRuntime/7.29.2");
		
		String client_secret = "secret";
		String token = "d4a567fbbdde35478034161c6b28719a";
		String grant_type = "client_credentials";
		String scope = "system/Location.read,system/Organization.read,system/Practitioner.read,system/PractitionerRole.read,system/Device.read,system/RelatedPerson.read,system/HealthcareService.read,system/Endpoint.read,system/Claim.read,system/ClaimResponse.read,system/Coverage.read,system/ExplanationOfBenefit.read,system/InsurancePlan.read,system/Patient.read,system/AllergyIntolerance.read,system/CarePlan.read,system/CareTeam.read,system/Condition.read,system/DiagnosticReport.read,system/DocumentReference.read,system/Encounter.read,system/Goal.read,system/Immunization.read,system/Medication.read,system/MedicationRequest.read,system/Observation.read,system/Procedure.read,system/ServiceRequest.read,system/FamilyMemberHistory.read,system/MedicationStatement.read,system/Provenance.read,system/MedicationAdministration.read,system/Group.read,system/MedicationDispense.read,system/MedicationKnowledge.read,system/OrganizationAffiliation.read,system/*.read,system/*.*";
		String client_assertion_type = "urn:ietf:params:oauth:client-assertion-type:jwt-bearer";
		String client_id = "n-WW3PPCp-41DdYX2-i0Ksihe";
		String client_assertion = "eyJhbGciOiJSUzM4NCIsInR5cCI6IkpXVCJ9.eyJhdWQiOiJhcmNhZGlhIiwiaXNzIjoibi1XVzNQUENwLTQxRGRZWDItaTBLc2loZSIsInN1YiI6Im4tV1czUFBDcC00MURkWVgyLWkwS3NpaGUiLCJqdGkiOiJhMzVmNTNlNi1hZWY2LTRmM2YtODY4Ni0xMDEzNTg5YzFjODAiLCJpYXQiOjE2NjI5ODQ4NjYsImV4cCI6MTY2OTk4NDg2Nn0.fo3iC_JtjdE5za9LhKWhaRm-p3grA_96U1Y-J0J36Gxvn8Y2csSD8wEu02rr0lebnUQ3aopI41L7NC99NIvU9l_O1BoAGGY82m-w7h3MMzfTrKMVTztuK9VdQFAjuKcNNcB8x4H5eYMdyOfPxY23sg07ic3aXnm8Uz3aE0bFAK2tOgDIWBKJ9EGPn-AvF-C86PHQp9BXYpl5pyhsMC2dDQJ-me1B5ZqfAnoOQx7Z1FpSilx_PD3lKDe8juO33yYRWF-nT8NSGeDhnggm_1puZzx2KLA_vviGKvRknPzeMQ4qSGWFdynj0MthMyG0ysUEPAC_6T3anBied2usWiG5vg";

		request.setParameter("client_assertion_type", client_assertion_type);
//		request.setParameter("client_id", client_id);
		request.setParameter("token", token);
		request.setParameter("token", scope);
		request.setParameter("grant_type", grant_type);
		request.setParameter("client_secret", client_secret);
		request.setParameter("client_assertion", client_assertion);
		request.setParameter("scope", scope);

		Mockito.when(commonUtil.isValid(Mockito.any())).thenReturn(true);
		Clients clients = new Clients();
		clients.setScope(scope);			
		clients.setApprovedStatus(ApprovedStatus.APPROVED);
		when(service.getClient(Mockito.anyString())).thenReturn(clients);

		Mockito.when(commonUtil.isValidScopes(Mockito.any(), Mockito.any())).thenReturn(true);

		JWTStoredKeyService jWTStoredKeyService = mock(JWTStoredKeyService.class);
	
		when(validationServices.loadPublicKey(Mockito.anyString())).thenReturn(jWTStoredKeyService);
		Mockito.when(!jWTStoredKeyService.validateSignature(Mockito.any())).thenReturn(true);

		AuthorizationDetails authTemp = mock(AuthorizationDetails.class);
		OAuthIssuer oauthIssuerImpl = mock(OAuthIssuerImpl.class);
		String accessToken = "c2c4b3978850805c33692e1cb1345a97";
		when(oauthIssuerImpl.accessToken()).thenReturn(accessToken);

		authTemp.setClientId(client_id);
		authTemp.setScope(scope);
		authTemp.setAccessToken(accessToken);
		authTemp.setExpiry("2022-10-27 09:58:12");
		authTemp.setIssuedAt("2022-06-17 12:22:54");
		authTemp.setRefreshToken(authTemp.getRefreshToken());

		when(dao.saveOrUpdate(authTemp)).thenReturn(authTemp);

//		backendTokenEndpoint.getAuthorization(request, response);

	}
	
	@Test
	void getAuthorization1() throws Exception {

		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();

	    request.addHeader("content-length", "1031"); 
		request.addHeader("putpostman-token", "09216da9-525d-4ca0-8821-33d723a7fa6c");
		request.addHeader("content-type", "application/x-www-form-urlencoded");
		request.addHeader("host", "localhost:8055");
		request.addHeader("connection", "keep-alive");
		request.addHeader("cache-control", "no-cache");
		request.addHeader("accept-encoding", "gzip,eflate, br");
		request.addHeader("accept", "application/json");
		request.addHeader("user-agent", "PostmanRuntime/7.29.2");
		request.addHeader("user-agent", "PostmanRuntime/7.29.2");
		
		String client_secret = "secret";
		String token = "d4a567fbbdde35478034161c6b28719a";
		String grant_type = "client_credentials";
		String scope = "system/Location.read,system/Organization.read,system/Practitioner.read,system/PractitionerRole.read,system/Device.read,system/RelatedPerson.read,system/HealthcareService.read,system/Endpoint.read,system/Claim.read,system/ClaimResponse.read,system/Coverage.read,system/ExplanationOfBenefit.read,system/InsurancePlan.read,system/Patient.read,system/AllergyIntolerance.read,system/CarePlan.read,system/CareTeam.read,system/Condition.read,system/DiagnosticReport.read,system/DocumentReference.read,system/Encounter.read,system/Goal.read,system/Immunization.read,system/Medication.read,system/MedicationRequest.read,system/Observation.read,system/Procedure.read,system/ServiceRequest.read,system/FamilyMemberHistory.read,system/MedicationStatement.read,system/Provenance.read,system/MedicationAdministration.read,system/Group.read,system/MedicationDispense.read,system/MedicationKnowledge.read,system/OrganizationAffiliation.read,system/*.read,system/*.*";
		String client_assertion_type = "urn:ietf:params:oauth:client-assertion-type:jwt-bearer";
		String client_id = "n-WW3PPCp-41DdYX2-i0Ksihe";
		String client_assertion = "eyJhbGciOiJSUzM4NCIsInR5cCI6IkpXVCJ9.eyJhdWQiOiJhcmNhZGlhIiwiaXNzIjoibi1XVzNQUENwLTQxRGRZWDItaTBLc2loZSIsInN1YiI6Im4tV1czUFBDcC00MURkWVgyLWkwS3NpaGUiLCJqdGkiOiJhMzVmNTNlNi1hZWY2LTRmM2YtODY4Ni0xMDEzNTg5YzFjODAiLCJpYXQiOjE2NjI5ODQ4NjYsImV4cCI6MTY2OTk4NDg2Nn0.fo3iC_JtjdE5za9LhKWhaRm-p3grA_96U1Y-J0J36Gxvn8Y2csSD8wEu02rr0lebnUQ3aopI41L7NC99NIvU9l_O1BoAGGY82m-w7h3MMzfTrKMVTztuK9VdQFAjuKcNNcB8x4H5eYMdyOfPxY23sg07ic3aXnm8Uz3aE0bFAK2tOgDIWBKJ9EGPn-AvF-C86PHQp9BXYpl5pyhsMC2dDQJ-me1B5ZqfAnoOQx7Z1FpSilx_PD3lKDe8juO33yYRWF-nT8NSGeDhnggm_1puZzx2KLA_vviGKvRknPzeMQ4qSGWFdynj0MthMyG0ysUEPAC_6T3anBied2usWiG5vg";

		request.setParameter("client_assertion_type", client_assertion_type);
//		request.setParameter("client_id", client_id);
		request.setParameter("token", token);
		request.setParameter("token", scope);
		request.setParameter("grant_type", grant_type);
		request.setParameter("client_secret", client_secret);
//		request.setParameter("client_assertion", client_assertion);
		request.setParameter("scope", scope);

//		backendTokenEndpoint.getAuthorization(request, response);

	}
	
	@Test
	void getAuthorization2() throws Exception {

		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();

	    request.addHeader("content-length", "1031"); 
		request.addHeader("putpostman-token", "09216da9-525d-4ca0-8821-33d723a7fa6c");
		request.addHeader("content-type", "application/x-www-form-urlencoded");
		request.addHeader("host", "localhost:8055");
		request.addHeader("connection", "keep-alive");
		request.addHeader("cache-control", "no-cache");
		request.addHeader("accept-encoding", "gzip,eflate, br");
		request.addHeader("accept", "application/json");
		request.addHeader("user-agent", "PostmanRuntime/7.29.2");
		request.addHeader("user-agent", "PostmanRuntime/7.29.2");
		
		String client_secret = "secret";
		String token = "d4a567fbbdde35478034161c6b28719a";
		String grant_type = "client_credentials";
		String scope = "system/Location.read,system/Organization.read,system/Practitioner.read,system/PractitionerRole.read,system/Device.read,system/RelatedPerson.read,system/HealthcareService.read,system/Endpoint.read,system/Claim.read,system/ClaimResponse.read,system/Coverage.read,system/ExplanationOfBenefit.read,system/InsurancePlan.read,system/Patient.read,system/AllergyIntolerance.read,system/CarePlan.read,system/CareTeam.read,system/Condition.read,system/DiagnosticReport.read,system/DocumentReference.read,system/Encounter.read,system/Goal.read,system/Immunization.read,system/Medication.read,system/MedicationRequest.read,system/Observation.read,system/Procedure.read,system/ServiceRequest.read,system/FamilyMemberHistory.read,system/MedicationStatement.read,system/Provenance.read,system/MedicationAdministration.read,system/Group.read,system/MedicationDispense.read,system/MedicationKnowledge.read,system/OrganizationAffiliation.read,system/*.read,system/*.*";
		String client_assertion_type = "urn:ietf:params:oauth:client-assertion-type:jwt-";
		String client_id = "n-WW3PPCp-41DdYX2-i0Ksihe";
		String client_assertion = "eyJhbGciOiJSUzM4NCIsInR5cCI6IkpXVCJ9.eyJhdWQiOiJhcmNhZGlhIiwiaXNzIjoibi1XVzNQUENwLTQxRGRZWDItaTBLc2loZSIsInN1YiI6Im4tV1czUFBDcC00MURkWVgyLWkwS3NpaGUiLCJqdGkiOiJhMzVmNTNlNi1hZWY2LTRmM2YtODY4Ni0xMDEzNTg5YzFjODAiLCJpYXQiOjE2NjI5ODQ4NjYsImV4cCI6MTY2OTk4NDg2Nn0.fo3iC_JtjdE5za9LhKWhaRm-p3grA_96U1Y-J0J36Gxvn8Y2csSD8wEu02rr0lebnUQ3aopI41L7NC99NIvU9l_O1BoAGGY82m-w7h3MMzfTrKMVTztuK9VdQFAjuKcNNcB8x4H5eYMdyOfPxY23sg07ic3aXnm8Uz3aE0bFAK2tOgDIWBKJ9EGPn-AvF-C86PHQp9BXYpl5pyhsMC2dDQJ-me1B5ZqfAnoOQx7Z1FpSilx_PD3lKDe8juO33yYRWF-nT8NSGeDhnggm_1puZzx2KLA_vviGKvRknPzeMQ4qSGWFdynj0MthMyG0ysUEPAC_6T3anBied2usWiG5vg";

		request.setParameter("client_assertion_type", client_assertion_type);
//		request.setParameter("client_id", client_id);
		request.setParameter("token", token);
		request.setParameter("token", scope);
		request.setParameter("grant_type", grant_type);
		request.setParameter("client_secret", client_secret);
		request.setParameter("client_assertion", client_assertion);
		request.setParameter("scope", scope);

//		backendTokenEndpoint.getAuthorization(request, response);

	}
	
	@Test
	void getAuthorization3() throws Exception {

		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();

	    request.addHeader("content-length", "1031"); 
		request.addHeader("putpostman-token", "09216da9-525d-4ca0-8821-33d723a7fa6c");
		request.addHeader("content-type", "application/x-www-form-urlencoded");
		request.addHeader("host", "localhost:8055");
		request.addHeader("connection", "keep-alive");
		request.addHeader("cache-control", "no-cache");
		request.addHeader("accept-encoding", "gzip,eflate, br");
		request.addHeader("accept", "application/json");
		request.addHeader("user-agent", "PostmanRuntime/7.29.2");
		
		String client_secret = "secret";
		String token = "d4a567fbbdde35478034161c6b28719a";
		String grant_type = "client_credentials";
		String scope = "system/Location.read,system/Organization.read,system/Practitioner.read,system/PractitionerRole.read,system/Device.read,system/RelatedPerson.read,system/HealthcareService.read,system/Endpoint.read,system/Claim.read,system/ClaimResponse.read,system/Coverage.read,system/ExplanationOfBenefit.read,system/InsurancePlan.read,system/Patient.read,system/AllergyIntolerance.read,system/CarePlan.read,system/CareTeam.read,system/Condition.read,system/DiagnosticReport.read,system/DocumentReference.read,system/Encounter.read,system/Goal.read,system/Immunization.read,system/Medication.read,system/MedicationRequest.read,system/Observation.read,system/Procedure.read,system/ServiceRequest.read,system/FamilyMemberHistory.read,system/MedicationStatement.read,system/Provenance.read,system/MedicationAdministration.read,system/Group.read,system/MedicationDispense.read,system/MedicationKnowledge.read,system/OrganizationAffiliation.read,system/*.read,system/*.*";
		String client_assertion_type = "urn:ietf:params:oauth:client-assertion-type:jwt-bearer";
		String client_id = "n-WW3PPCp-41DdYX2-i0Ksihe";
		String client_assertion = "eyJhbGciOiJSUzM4NCIsInR5cCI6IkpXVCJ9.eyJhdWQiOiJhcmNhZGlhIiwiaXNzIjoibi1XVzNQUENwLTQxRGRZWDItaTBLc2loZSIsInN1YiI6Im4tV1czUFBDcC00MURkWVgyLWkwS3NpaGUiLCJqdGkiOiJhMzVmNTNlNi1hZWY2LTRmM2YtODY4Ni0xMDEzNTg5YzFjODAiLCJpYXQiOjE2NjI5ODQ4NjYsImV4cCI6MTY2OTk4NDg2Nn0.fo3iC_JtjdE5za9LhKWhaRm-p3grA_96U1Y-J0J36Gxvn8Y2csSD8wEu02rr0lebnUQ3aopI41L7NC99NIvU9l_O1BoAGGY82m-w7h3MMzfTrKMVTztuK9VdQFAjuKcNNcB8x4H5eYMdyOfPxY23sg07ic3aXnm8Uz3aE0bFAK2tOgDIWBKJ9EGPn-AvF-C86PHQp9BXYpl5pyhsMC2dDQJ-me1B5ZqfAnoOQx7Z1FpSilx_PD3lKDe8juO33yYRWF-nT8NSGeDhnggm_1puZzx2KLA_vviGKvRknPzeMQ4qSGWFdynj0MthMyG0ysUEPAC_6T3anBied2usWiG5vg";

		request.setParameter("client_assertion_type", client_assertion_type);
//		request.setParameter("client_id", client_id);
		request.setParameter("token", token);
		request.setParameter("token", scope);
		request.setParameter("grant_type", grant_type);
		request.setParameter("client_secret", client_secret);
		request.setParameter("client_assertion", client_assertion);
//		request.setParameter("scope", scope);


//		backendTokenEndpoint.getAuthorization(request, response);

	}
	
	@Test
	void getAuthorization4() throws Exception {

		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();

	    request.addHeader("content-length", "1031"); 
		request.addHeader("putpostman-token", "09216da9-525d-4ca0-8821-33d723a7fa6c");
		request.addHeader("content-type", "application/x-www-form-urlencoded");
		request.addHeader("host", "localhost:8055");
		request.addHeader("connection", "keep-alive");
		request.addHeader("cache-control", "no-cache");
		request.addHeader("accept-encoding", "gzip,eflate, br");
//		request.addHeader("accept", "application/");
		request.addHeader("user-agent", "PostmanRuntime/7.29.2");

//		backendTokenEndpoint.getAuthorization(request, response);

	}
	
	@Test
	void getAuthorization5() throws Exception {

		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();

	    request.addHeader("content-length", "1031"); 
		request.addHeader("putpostman-token", "09216da9-525d-4ca0-8821-33d723a7fa6c");
		request.addHeader("content-type", "application/x-www-form-urlencoded");
		request.addHeader("host", "localhost:8055");
		request.addHeader("connection", "keep-alive");
		request.addHeader("cache-control", "no-cache");
		request.addHeader("accept-encoding", "gzip,eflate, br");
		request.addHeader("accept", "application/");
		request.addHeader("user-agent", "PostmanRuntime/7.29.2");
//		backendTokenEndpoint.getAuthorization(request, response);

	}

	
	@Test
	void getAuthorization6() throws Exception {

		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();

	    request.addHeader("content-length", "1031"); 
		request.addHeader("putpostman-token", "09216da9-525d-4ca0-8821-33d723a7fa6c");
		request.addHeader("content-type", "application/x-www-form-urlencoded");
		request.addHeader("host", "localhost:8055");
		request.addHeader("connection", "keep-alive");
		request.addHeader("cache-control", "no-cache");
		request.addHeader("accept-encoding", "gzip,eflate, br");
		request.addHeader("accept", "application/json");
		request.addHeader("user-agent", "PostmanRuntime/7.29.2");
		
		String client_secret = "secret";
		String token = "d4a567fbbdde35478034161c6b28719a";
		String grant_type = "client_credentials";
		String scope = "system/Location.read,system/Organization.read,system/Practitioner.read,system/PractitionerRole.read,system/Device.read,system/RelatedPerson.read,system/HealthcareService.read,system/Endpoint.read,system/Claim.read,system/ClaimResponse.read,system/Coverage.read,system/ExplanationOfBenefit.read,system/InsurancePlan.read,system/Patient.read,system/AllergyIntolerance.read,system/CarePlan.read,system/CareTeam.read,system/Condition.read,system/DiagnosticReport.read,system/DocumentReference.read,system/Encounter.read,system/Goal.read,system/Immunization.read,system/Medication.read,system/MedicationRequest.read,system/Observation.read,system/Procedure.read,system/ServiceRequest.read,system/FamilyMemberHistory.read,system/MedicationStatement.read,system/Provenance.read,system/MedicationAdministration.read,system/Group.read,system/MedicationDispense.read,system/MedicationKnowledge.read,system/OrganizationAffiliation.read,system/*.read,system/*.*";
		String client_assertion_type = "urn:ietf:params:oauth:client-assertion-type:jwt-bearer";
		String client_id = "n-WW3PPCp-41DdYX2-i0Ksihe";
		String client_assertion = "eyJhbGciOiJSUzM4NCIsInR5cCI6IkpXVCJ9.eyJhdWQiOiJhcmNhZGlhIiwiaXNzIjoibi1XVzNQUENwLTQxRGRZWDItaTBLc2loZSIsInN1YiI6Im4tV1czUFBDcC00MURkWVgyLWkwS3NpaGUiLCJqdGkiOiJhMzVmNTNlNi1hZWY2LTRmM2YtODY4Ni0xMDEzNTg5YzFjODAiLCJpYXQiOjE2NjI5ODQ4NjYsImV4cCI6MTY2OTk4NDg2Nn0.fo3iC_JtjdE5za9LhKWhaRm-p3grA_96U1Y-J0J36Gxvn8Y2csSD8wEu02rr0lebnUQ3aopI41L7NC99NIvU9l_O1BoAGGY82m-w7h3MMzfTrKMVTztuK9VdQFAjuKcNNcB8x4H5eYMdyOfPxY23sg07ic3aXnm8Uz3aE0bFAK2tOgDIWBKJ9EGPn-AvF-C86PHQp9BXYpl5pyhsMC2dDQJ-me1B5ZqfAnoOQx7Z1FpSilx_PD3lKDe8juO33yYRWF-nT8NSGeDhnggm_1puZzx2KLA_vviGKvRknPzeMQ4qSGWFdynj0MthMyG0ysUEPAC_6T3anBied2usWiG5vg";

		request.setParameter("client_assertion_type", client_assertion_type);
//		request.setParameter("client_id", client_id);
		request.setParameter("token", token);
		request.setParameter("token", scope);
		request.setParameter("grant_type", grant_type);
		request.setParameter("client_secret", client_secret);
		request.setParameter("client_assertion", client_assertion);
		request.setParameter("scope", scope);

		Mockito.when(commonUtil.isValid(Mockito.any())).thenReturn(false);
		Clients clients = new Clients();
//		clients.setScope(scope);			
//		clients.setApprovedStatus(ApprovedStatus.PENDING);
		when(service.getClient(Mockito.anyString())).thenReturn(null);

//		Mockito.when(commonutil.isValidScopes(Mockito.any(), Mockito.any())).thenReturn(false);

		JWTStoredKeyService jWTStoredKeyService = mock(JWTStoredKeyService.class);
		
		Mockito.when(validationServices.loadPublicKey(Mockito.any())).thenReturn(jWTStoredKeyService);
		
//		when(clients.getApprovedStatus()).thenReturn(ApprovedStatus.PENDING);
	
		Mockito.when(!jWTStoredKeyService.validateSignature(Mockito.any())).thenReturn(true);

//		backendTokenEndpoint.getAuthorization(request, response);

	}
	
}


