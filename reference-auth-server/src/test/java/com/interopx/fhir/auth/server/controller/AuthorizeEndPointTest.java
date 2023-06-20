package com.interopx.fhir.auth.server.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.oltu.oauth2.as.request.OAuthAuthzRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.interopx.fhir.auth.server.model.AuthorizationDetails;
import com.interopx.fhir.auth.server.model.Clients;
import com.interopx.fhir.auth.server.model.Users;
import com.interopx.fhir.auth.server.service.AuthorizationDetailsService;
import com.interopx.fhir.auth.server.service.ClientsService;
import com.interopx.fhir.auth.server.service.PatientService;
import com.interopx.fhir.auth.server.service.UsersService;
import com.interopx.fhir.auth.server.util.AuthUtil.ApprovedStatus;
import com.interopx.fhir.auth.server.util.CommonUtil;

@SpringBootTest
class AuthorizeEndPointTest {

	@InjectMocks
	AuthorizeEndPoint authorizeEndPoint;

	@Mock
	AuthorizationDetailsService authTempService;

	@Mock
	ClientsService service;
	
	@Mock
	PatientService patientService;

	@Mock
	UsersService userService;

	@Mock
	UsersService userRegistrationService;

	@Mock
	PasswordEncoder passwordEncoder;

//	@Mock
//	AuditMapper auditMapper;

	static Users userDetails() {
		Users userDetails = new Users();
		userDetails.setFirstName("nirusha");
		userDetails.setMobile_number("12334");
		userDetails.setUser_email("N@email.com");
		userDetails.setUser_name("nirusha");
		userDetails.setUser_password("123");
		userDetails.setLastName("S");

		return userDetails;
	}

	static Clients clientDetails() {
		Clients client = new Clients();
		client.setId(1);
		HttpServletRequest request = mock(HttpServletRequest.class);
		client.setUserId("1");
		client.setRegisterToken("token");
		client.setClientId("12");
		client.setClientSecret("secret");
		client.setApprovedStatus(ApprovedStatus.APPROVED);
		client.setScope("scope");
		client.setAppType("Arcadia");
		client.setName("nirusha");
		client.setDynamicClient(true);
		return client;
	}

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	static AuthorizationDetails createAuthorizationDetails() {
		AuthorizationDetails authorizationDetails = new AuthorizationDetails();
		// authorizationDetails.setAccess_token("token");
		authorizationDetails.setAud("j");
		authorizationDetails.setExpiry("2030-05-12 03:15:20");
		authorizationDetails.setAuthCode("jefjk");
		authorizationDetails.setClientId("n-WW3PPCp-41DdYX2-i0Ksihe");
		authorizationDetails.setScope("123");
		authorizationDetails.setTransactionId("123");
		authorizationDetails.setClientId("12");
		authorizationDetails.setAuthCode("12");
		authorizationDetails.setRedirectUri("A");
		authorizationDetails.setState("abc");
		authorizationDetails.setScope("scope");
		return authorizationDetails;
	}
	
//	@Test
//	void testAddAuditLog() {
//		Clients client = clientDetails();
//		Integer status = 1;
//		HttpServletRequest request = mock(HttpServletRequest.class);
//		AuditDto audit = new AuditDto();
//		audit.setId(CommonUtil.generateRandomString(30));
//		audit.setClientId(CommonUtil.generateRandomString(30));
//		audit.setServiceName("ix-auth-server");
//		audit.setRequestUrl(CommonUtil.getBaseUrl(request));
//		audit.setRequestMethod("GET");
//		audit.setClassName("AuthorizeEndPoint");
//		audit.setMethodName("getAuthorization");
//		audit.setAuditType("RESTAPI");
//		audit.setRequestMapping("/api/authorize");
//		audit.setRequestStatusCode(status);
//		auditMapper.mapDtoToEntity(audit);
//		authorizeEndPoint.addAuditLog(client, status, request);
//	}

	@Test
	void testAuthorizeAfterLaunchPatient() throws IOException, ServletException {
		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse response = mock(HttpServletResponse.class);
		AuthorizationDetails tempAuth = createAuthorizationDetails();
		String launchPatientId = "123";
		when(authTempService.getAuthenticationById(Mockito.anyString())).thenReturn(tempAuth);
		tempAuth.setLaunchPatientId(launchPatientId);
		authTempService.saveOrUpdate(tempAuth);
		String url = tempAuth.getRedirectUri().trim() + "?code=" + tempAuth.getAuthCode().trim() + "&state="
				+ tempAuth.getState().trim();
		assertEquals(url, authorizeEndPoint.authorizeAfterLaunchPatient(request, response, launchPatientId,
				tempAuth.getTransactionId()));

	}

	@Test
	void testValidateUser() throws IOException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		AuthorizationDetails tempAuth = createAuthorizationDetails();
		Clients client = clientDetails();
		Users user = userDetails();
		String userName = "N@email.com";
		String password = "123";
		String transactionId = tempAuth.getTransactionId();
		Mockito.lenient().doReturn(tempAuth).when(authTempService).getAuthenticationById(transactionId);

		//when(authTempService.getAuthenticationById(transactionId).thenReturn(tempAuth));
		when(service.getClient(tempAuth.getClientId())).thenReturn(client);
		when(userRegistrationService.getUserByEmail(userName, request)).thenReturn(user);
		
		String url = "http://localhost#/login?error=Invalid Username or Password.&transaction_id=123";
		 
			assertEquals(url, authorizeEndPoint.validateUser(request, response, userName,
				password, tempAuth.getTransactionId()));
	}
	
	@Test
	void testValidateUser1() throws IOException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		AuthorizationDetails tempAuth = createAuthorizationDetails();
		Clients client = clientDetails();
		Users user = userDetails();
		String userName = "N@email.com";
		String password = "123";
		String transactionId = tempAuth.getTransactionId();
		Mockito.lenient().doReturn(tempAuth).when(authTempService).getAuthenticationById(transactionId);

		//when(authTempService.getAuthenticationById(transactionId).thenReturn(tempAuth));
		when(service.getClient(tempAuth.getClientId())).thenReturn(client);
		when(userRegistrationService.getUserByEmail(userName, request)).thenReturn(user);
		when(passwordEncoder.matches(Mockito.anyString(),Mockito.any())).thenReturn(true);

		 String url =
		          CommonUtil.getBaseUrl(request)
		              + "#/authentication?client_id="
		              + tempAuth.getClientId()
		              + "&redirect_uri="
		              + tempAuth.getRedirectUri().trim()
		              + "&scope="
		              + tempAuth.getScope()
		              + "&state="
		              + tempAuth.getState()
		              + "&transaction_id="
		              + tempAuth.getTransactionId()
		              + "&name="
		              + user.getFirstName().trim()
		              + " "
		              + user.getLastName().trim()
		              + "&cName="
		              + client.getName().trim()
		              + "&appType="
		              + client.getAppType().trim();
				 
			assertEquals(url, authorizeEndPoint.validateUser(request, response, userName,
				password, tempAuth.getTransactionId()));
	}
	
	
	
	
	@Test
	void getAuthorization() throws IOException {
	
	MockHttpServletRequest request = new MockHttpServletRequest();

	HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
	
	request.setParameter("response_type", "client_credentials");
	request.setParameter("redirect_uri", "http://interopx.com/");   
	request.setParameter("state", "state123"); 
	request.setParameter("aud", "interopx.com"); 
	request.setParameter("launch", "http://interopx.com/"); 
	request.setParameter("code_challenge", "test123"); 
	request.setParameter("code_challenge_method", "test123"); 
	request.addParameter("refresh_token", "KwI5dY8SX2xnJ-uJ4CZzfFl6ppqAp7yQcJbFyJD0mbIGHMvCV04BedAMei4yQseRwHFfBkPYesECR2njzffyB5QJsZ04MrpK57CldfVhhpGG51wawmKevIMI4w3EuzCnFgrkR2juKvWp3Z3BK1aVvLziEfpF4drkX9s6tulqmxyasNYeGjH7u-BH-nDI0BVg1IFTMT6R3NmMA9QiE1fHTOO9xalxxb2tIOf28TPBNvcTr3cCp2SYam-9Bx");
//	request.setParameter("client_id", "BuhZfFFzZPVMV4617Rd9OQsdIPw056");
	request.setParameter("client_secret", "YUE2TmFMWHBtdFh5LUx5ZVhUVHdRd0ZzVXRiTzB3V2Y1ZVVJazd3eEc3NnBpY1poNjU=");
	request.setParameter("scope", "user/Patient.read,user/AllergyIntolerance.read,user/CarePlan.read,user/CareTeam.read,user/Condition.read,user/Device.read,user/DiagnosticReport.read,user/DocumentReference.read,user/Encounter.read,user/Goal.read,user/Immunization.read,user/Location.read,user/MedicationRequest.read,user/Medication.read,user/Observation.read,user/Organization.read,user/Practitioner.read,user/PractitionerRole.read,user/Procedure.read,user/Provenance.read,user/RelatedPerson.read,patient/Medication.read,patient/Location.read,patient/Immunization.read,patiet/Goal.read,patient/Encounter.read,patient/DocumentReference.read,patient/DiagnosticReport.read,patient/Device.read,patient/Condition.read,patient/CareTeam.read,patient/CarePlan.read,patient/AllergyIntolerance.read,patient/Patient.read,patient/*.read,online_access,offline_access,fhirUser,openid,fhir_complete,launch/patient,launch,patient/MedicationRequest.read,patient/Observation.read,patient/Organization.read,patient/Practitioner.read,patient/PractitionerRole.read,patient/Procedure.read,patient/Provenance.read,patient/RelatedPerson.read");
	request.setParameter("client_assertion_type", "urn:ietf:params:oauth:client-assertion-type:jwt-bearer");
	request.setParameter("client_assertion", "eyJhbGciOiJSUzM4NCIsInR5cCI6IkpXVCJ9.eyJhdWQiOiJhcmNhZGlhIiwiaXNzIjoiLWtXN0F2RUxkOEFDZ05VaTZOSldGYmw4WSIsInN1YiI6Ii1rVzdBdkVMZDhBQ2dOVWk2TkpXRmJsOFkiLCJqdGkiOiIwNDIxNGYxNy04MmE2LTRkYTYtOGE1YS1mOGRjZjM5ZDJmOGYiLCJpYXQiOjE2NTYzMjM0MDYsImV4cCI6MTY1NjMzMzQwNn0.vD4wtrjN82_LFQBYQMF6WaTOKlNZiErLmQxLm90yiGwpbxYA6xBof2FH1bKm15sf4GTfWF5Dovp-Kic8dmv7yyQ0BwwJPCGsKl5J3DPE0DbDXYt4qAuu7vHVrQPs08LO_xTTcl2pbtWZw6MM8UzgXFCk2GMmF0hCybRf8b9sl6MUoQc8y5b3PDAAQ-hvaFF2hM1_ZOtn4BoLNwN2x88PwVCqzWRZtfo6UT5lf3o2roMlEHDf6cZ-s5oHtGBpnTsbdkGccGXeyL5zwMZGdPlcUaiJnXR3aMXr0-f7-N8cU-P3CXdD6lCJHQeE3pprsEu83J7BiHtxHD0RRhCYFpCbsQ");

	authorizeEndPoint.getAuthorization(request, response);
	
	}
	
	@Test
	void getAuthorization1() throws IOException {
	
	MockHttpServletRequest request = new MockHttpServletRequest();

	HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
	
	request.setParameter("response_type", "client_credentials");
	request.setParameter("redirect_uri", "http://interopx.com/");   
	request.setParameter("state", "state123"); 
	request.setParameter("aud", "interopx.com"); 
	request.setParameter("launch", "http://interopx.com/"); 
	request.setParameter("code_challenge", "test123"); 
	request.setParameter("code_challenge_method", "test123"); 
	request.addParameter("refresh_token", "KwI5dY8SX2xnJ-uJ4CZzfFl6ppqAp7yQcJbFyJD0mbIGHMvCV04BedAMei4yQseRwHFfBkPYesECR2njzffyB5QJsZ04MrpK57CldfVhhpGG51wawmKevIMI4w3EuzCnFgrkR2juKvWp3Z3BK1aVvLziEfpF4drkX9s6tulqmxyasNYeGjH7u-BH-nDI0BVg1IFTMT6R3NmMA9QiE1fHTOO9xalxxb2tIOf28TPBNvcTr3cCp2SYam-9Bx");
	request.setParameter("client_id", "BuhZfFFzZPVMV4617Rd9OQsdIPw056");
	request.setParameter("client_secret", "YUE2TmFMWHBtdFh5LUx5ZVhUVHdRd0ZzVXRiTzB3V2Y1ZVVJazd3eEc3NnBpY1poNjU=");
//	request.setParameter("scope", "user/Patient.read,user/AllergyIntolerance.read,user/CarePlan.read,user/CareTeam.read,user/Condition.read,user/Device.read,user/DiagnosticReport.read,user/DocumentReference.read,user/Encounter.read,user/Goal.read,user/Immunization.read,user/Location.read,user/MedicationRequest.read,user/Medication.read,user/Observation.read,user/Organization.read,user/Practitioner.read,user/PractitionerRole.read,user/Procedure.read,user/Provenance.read,user/RelatedPerson.read,patient/Medication.read,patient/Location.read,patient/Immunization.read,patiet/Goal.read,patient/Encounter.read,patient/DocumentReference.read,patient/DiagnosticReport.read,patient/Device.read,patient/Condition.read,patient/CareTeam.read,patient/CarePlan.read,patient/AllergyIntolerance.read,patient/Patient.read,patient/*.read,online_access,offline_access,fhirUser,openid,fhir_complete,launch/patient,launch,patient/MedicationRequest.read,patient/Observation.read,patient/Organization.read,patient/Practitioner.read,patient/PractitionerRole.read,patient/Procedure.read,patient/Provenance.read,patient/RelatedPerson.read");
	request.setParameter("client_assertion_type", "urn:ietf:params:oauth:client-assertion-type:jwt-bearer");
	request.setParameter("client_assertion", "eyJhbGciOiJSUzM4NCIsInR5cCI6IkpXVCJ9.eyJhdWQiOiJhcmNhZGlhIiwiaXNzIjoiLWtXN0F2RUxkOEFDZ05VaTZOSldGYmw4WSIsInN1YiI6Ii1rVzdBdkVMZDhBQ2dOVWk2TkpXRmJsOFkiLCJqdGkiOiIwNDIxNGYxNy04MmE2LTRkYTYtOGE1YS1mOGRjZjM5ZDJmOGYiLCJpYXQiOjE2NTYzMjM0MDYsImV4cCI6MTY1NjMzMzQwNn0.vD4wtrjN82_LFQBYQMF6WaTOKlNZiErLmQxLm90yiGwpbxYA6xBof2FH1bKm15sf4GTfWF5Dovp-Kic8dmv7yyQ0BwwJPCGsKl5J3DPE0DbDXYt4qAuu7vHVrQPs08LO_xTTcl2pbtWZw6MM8UzgXFCk2GMmF0hCybRf8b9sl6MUoQc8y5b3PDAAQ-hvaFF2hM1_ZOtn4BoLNwN2x88PwVCqzWRZtfo6UT5lf3o2roMlEHDf6cZ-s5oHtGBpnTsbdkGccGXeyL5zwMZGdPlcUaiJnXR3aMXr0-f7-N8cU-P3CXdD6lCJHQeE3pprsEu83J7BiHtxHD0RRhCYFpCbsQ");

	Clients client = new Clients();
	client.setId(1);
	Mockito.when(service.getClient(Mockito.any())).thenReturn(client);
	authorizeEndPoint.getAuthorization(request, response);
	
	}
	
	@Test
	void getAuthorization2() throws IOException {
	
	MockHttpServletRequest request = new MockHttpServletRequest();

	HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
	
	request.setParameter("response_type", "client_credentials");
	request.setParameter("redirect_uri", "http://interopx.com/");   
//	request.setParameter("state", "state123"); 
	request.setParameter("aud", "interopx.com"); 
	request.setParameter("launch", "http://interopx.com/"); 
	request.setParameter("code_challenge", "test123"); 
	request.setParameter("code_challenge_method", "test123"); 
	request.addParameter("refresh_token", "KwI5dY8SX2xnJ-uJ4CZzfFl6ppqAp7yQcJbFyJD0mbIGHMvCV04BedAMei4yQseRwHFfBkPYesECR2njzffyB5QJsZ04MrpK57CldfVhhpGG51wawmKevIMI4w3EuzCnFgrkR2juKvWp3Z3BK1aVvLziEfpF4drkX9s6tulqmxyasNYeGjH7u-BH-nDI0BVg1IFTMT6R3NmMA9QiE1fHTOO9xalxxb2tIOf28TPBNvcTr3cCp2SYam-9Bx");
	request.setParameter("client_id", "BuhZfFFzZPVMV4617Rd9OQsdIPw056");
	request.setParameter("client_secret", "YUE2TmFMWHBtdFh5LUx5ZVhUVHdRd0ZzVXRiTzB3V2Y1ZVVJazd3eEc3NnBpY1poNjU=");
	request.setParameter("scope", "user/Patient.read,user/AllergyIntolerance.read,user/CarePlan.read,user/CareTeam.read,user/Condition.read,user/Device.read,user/DiagnosticReport.read,user/DocumentReference.read,user/Encounter.read,user/Goal.read,user/Immunization.read,user/Location.read,user/MedicationRequest.read,user/Medication.read,user/Observation.read,user/Organization.read,user/Practitioner.read,user/PractitionerRole.read,user/Procedure.read,user/Provenance.read,user/RelatedPerson.read,patient/Medication.read,patient/Location.read,patient/Immunization.read,patiet/Goal.read,patient/Encounter.read,patient/DocumentReference.read,patient/DiagnosticReport.read,patient/Device.read,patient/Condition.read,patient/CareTeam.read,patient/CarePlan.read,patient/AllergyIntolerance.read,patient/Patient.read,patient/*.read,online_access,offline_access,fhirUser,openid,fhir_complete,launch/patient,launch,patient/MedicationRequest.read,patient/Observation.read,patient/Organization.read,patient/Practitioner.read,patient/PractitionerRole.read,patient/Procedure.read,patient/Provenance.read,patient/RelatedPerson.read");
	request.setParameter("client_assertion_type", "urn:ietf:params:oauth:client-assertion-type:jwt-bearer");
	request.setParameter("client_assertion", "eyJhbGciOiJSUzM4NCIsInR5cCI6IkpXVCJ9.eyJhdWQiOiJhcmNhZGlhIiwiaXNzIjoiLWtXN0F2RUxkOEFDZ05VaTZOSldGYmw4WSIsInN1YiI6Ii1rVzdBdkVMZDhBQ2dOVWk2TkpXRmJsOFkiLCJqdGkiOiIwNDIxNGYxNy04MmE2LTRkYTYtOGE1YS1mOGRjZjM5ZDJmOGYiLCJpYXQiOjE2NTYzMjM0MDYsImV4cCI6MTY1NjMzMzQwNn0.vD4wtrjN82_LFQBYQMF6WaTOKlNZiErLmQxLm90yiGwpbxYA6xBof2FH1bKm15sf4GTfWF5Dovp-Kic8dmv7yyQ0BwwJPCGsKl5J3DPE0DbDXYt4qAuu7vHVrQPs08LO_xTTcl2pbtWZw6MM8UzgXFCk2GMmF0hCybRf8b9sl6MUoQc8y5b3PDAAQ-hvaFF2hM1_ZOtn4BoLNwN2x88PwVCqzWRZtfo6UT5lf3o2roMlEHDf6cZ-s5oHtGBpnTsbdkGccGXeyL5zwMZGdPlcUaiJnXR3aMXr0-f7-N8cU-P3CXdD6lCJHQeE3pprsEu83J7BiHtxHD0RRhCYFpCbsQ");

	Clients client = new Clients();
	client.setId(1);
	Mockito.when(service.getClient(Mockito.any())).thenReturn(client);
	authorizeEndPoint.getAuthorization(request, response);
	
	}
	
	@Test
	void getAuthorization3() throws IOException {
	
	MockHttpServletRequest request = new MockHttpServletRequest();

	HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
	
	request.setParameter("response_type", "client_credentials");
//	request.setParameter("redirect_uri", "http://interopx.com/");   
	request.setParameter("state", "state123"); 
	request.setParameter("aud", "interopx.com"); 
	request.setParameter("launch", "http://interopx.com/"); 
	request.setParameter("code_challenge", "test123"); 
	request.setParameter("code_challenge_method", "test123"); 
	request.addParameter("refresh_token", "KwI5dY8SX2xnJ-uJ4CZzfFl6ppqAp7yQcJbFyJD0mbIGHMvCV04BedAMei4yQseRwHFfBkPYesECR2njzffyB5QJsZ04MrpK57CldfVhhpGG51wawmKevIMI4w3EuzCnFgrkR2juKvWp3Z3BK1aVvLziEfpF4drkX9s6tulqmxyasNYeGjH7u-BH-nDI0BVg1IFTMT6R3NmMA9QiE1fHTOO9xalxxb2tIOf28TPBNvcTr3cCp2SYam-9Bx");
	request.setParameter("client_id", "BuhZfFFzZPVMV4617Rd9OQsdIPw056");
	request.setParameter("client_secret", "YUE2TmFMWHBtdFh5LUx5ZVhUVHdRd0ZzVXRiTzB3V2Y1ZVVJazd3eEc3NnBpY1poNjU=");
	request.setParameter("scope", "user/Patient.read,user/AllergyIntolerance.read,user/CarePlan.read,user/CareTeam.read,user/Condition.read,user/Device.read,user/DiagnosticReport.read,user/DocumentReference.read,user/Encounter.read,user/Goal.read,user/Immunization.read,user/Location.read,user/MedicationRequest.read,user/Medication.read,user/Observation.read,user/Organization.read,user/Practitioner.read,user/PractitionerRole.read,user/Procedure.read,user/Provenance.read,user/RelatedPerson.read,patient/Medication.read,patient/Location.read,patient/Immunization.read,patiet/Goal.read,patient/Encounter.read,patient/DocumentReference.read,patient/DiagnosticReport.read,patient/Device.read,patient/Condition.read,patient/CareTeam.read,patient/CarePlan.read,patient/AllergyIntolerance.read,patient/Patient.read,patient/*.read,online_access,offline_access,fhirUser,openid,fhir_complete,launch/patient,launch,patient/MedicationRequest.read,patient/Observation.read,patient/Organization.read,patient/Practitioner.read,patient/PractitionerRole.read,patient/Procedure.read,patient/Provenance.read,patient/RelatedPerson.read");
	request.setParameter("client_assertion_type", "urn:ietf:params:oauth:client-assertion-type:jwt-bearer");
	request.setParameter("client_assertion", "eyJhbGciOiJSUzM4NCIsInR5cCI6IkpXVCJ9.eyJhdWQiOiJhcmNhZGlhIiwiaXNzIjoiLWtXN0F2RUxkOEFDZ05VaTZOSldGYmw4WSIsInN1YiI6Ii1rVzdBdkVMZDhBQ2dOVWk2TkpXRmJsOFkiLCJqdGkiOiIwNDIxNGYxNy04MmE2LTRkYTYtOGE1YS1mOGRjZjM5ZDJmOGYiLCJpYXQiOjE2NTYzMjM0MDYsImV4cCI6MTY1NjMzMzQwNn0.vD4wtrjN82_LFQBYQMF6WaTOKlNZiErLmQxLm90yiGwpbxYA6xBof2FH1bKm15sf4GTfWF5Dovp-Kic8dmv7yyQ0BwwJPCGsKl5J3DPE0DbDXYt4qAuu7vHVrQPs08LO_xTTcl2pbtWZw6MM8UzgXFCk2GMmF0hCybRf8b9sl6MUoQc8y5b3PDAAQ-hvaFF2hM1_ZOtn4BoLNwN2x88PwVCqzWRZtfo6UT5lf3o2roMlEHDf6cZ-s5oHtGBpnTsbdkGccGXeyL5zwMZGdPlcUaiJnXR3aMXr0-f7-N8cU-P3CXdD6lCJHQeE3pprsEu83J7BiHtxHD0RRhCYFpCbsQ");

	Clients client = new Clients();
	client.setId(1);
	Mockito.when(service.getClient(Mockito.any())).thenReturn(client);
	authorizeEndPoint.getAuthorization(request, response);
	
	}
	
	@Test
	void getAuthorization4() throws IOException {
	
	MockHttpServletRequest request = new MockHttpServletRequest();

	HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
	
	request.setParameter("response_type", "client_credentials");
	request.setParameter("redirect_uri", "http://interopx.com/");   
	request.setParameter("state", "state123"); 
	request.setParameter("aud", "interopx.com"); 
	request.setParameter("launch", "http://interopx.com/"); 
	request.setParameter("code_challenge", "test123"); 
	request.setParameter("code_challenge_method", "test123"); 
	request.addParameter("refresh_token", "KwI5dY8SX2xnJ-uJ4CZzfFl6ppqAp7yQcJbFyJD0mbIGHMvCV04BedAMei4yQseRwHFfBkPYesECR2njzffyB5QJsZ04MrpK57CldfVhhpGG51wawmKevIMI4w3EuzCnFgrkR2juKvWp3Z3BK1aVvLziEfpF4drkX9s6tulqmxyasNYeGjH7u-BH-nDI0BVg1IFTMT6R3NmMA9QiE1fHTOO9xalxxb2tIOf28TPBNvcTr3cCp2SYam-9Bx");
	request.setParameter("client_id", "BuhZfFFzZPVMV4617Rd9OQsdIPw056");
	request.setParameter("client_secret", "YUE2TmFMWHBtdFh5LUx5ZVhUVHdRd0ZzVXRiTzB3V2Y1ZVVJazd3eEc3NnBpY1poNjU=");
	request.setParameter("scope", "user/Patient.read,user/AllergyIntolerance.read,user/CarePlan.read,user/CareTeam.read,user/Condition.read,user/Device.read,user/DiagnosticReport.read,user/DocumentReference.read,user/Encounter.read,user/Goal.read,user/Immunization.read,user/Location.read,user/MedicationRequest.read,user/Medication.read,user/Observation.read,user/Organization.read,user/Practitioner.read,user/PractitionerRole.read,user/Procedure.read,user/Provenance.read,user/RelatedPerson.read,patient/Medication.read,patient/Location.read,patient/Immunization.read,patiet/Goal.read,patient/Encounter.read,patient/DocumentReference.read,patient/DiagnosticReport.read,patient/Device.read,patient/Condition.read,patient/CareTeam.read,patient/CarePlan.read,patient/AllergyIntolerance.read,patient/Patient.read,patient/*.read,online_access,offline_access,fhirUser,openid,fhir_complete,launch/patient,launch,patient/MedicationRequest.read,patient/Observation.read,patient/Organization.read,patient/Practitioner.read,patient/PractitionerRole.read,patient/Procedure.read,patient/Provenance.read,patient/RelatedPerson.read");
	request.setParameter("client_assertion_type", "urn:ietf:params:oauth:client-assertion-type:jwt-bearer");
	request.setParameter("client_assertion", "eyJhbGciOiJSUzM4NCIsInR5cCI6IkpXVCJ9.eyJhdWQiOiJhcmNhZGlhIiwiaXNzIjoiLWtXN0F2RUxkOEFDZ05VaTZOSldGYmw4WSIsInN1YiI6Ii1rVzdBdkVMZDhBQ2dOVWk2TkpXRmJsOFkiLCJqdGkiOiIwNDIxNGYxNy04MmE2LTRkYTYtOGE1YS1mOGRjZjM5ZDJmOGYiLCJpYXQiOjE2NTYzMjM0MDYsImV4cCI6MTY1NjMzMzQwNn0.vD4wtrjN82_LFQBYQMF6WaTOKlNZiErLmQxLm90yiGwpbxYA6xBof2FH1bKm15sf4GTfWF5Dovp-Kic8dmv7yyQ0BwwJPCGsKl5J3DPE0DbDXYt4qAuu7vHVrQPs08LO_xTTcl2pbtWZw6MM8UzgXFCk2GMmF0hCybRf8b9sl6MUoQc8y5b3PDAAQ-hvaFF2hM1_ZOtn4BoLNwN2x88PwVCqzWRZtfo6UT5lf3o2roMlEHDf6cZ-s5oHtGBpnTsbdkGccGXeyL5zwMZGdPlcUaiJnXR3aMXr0-f7-N8cU-P3CXdD6lCJHQeE3pprsEu83J7BiHtxHD0RRhCYFpCbsQ");

	Clients client = new Clients();
	client.setApprovedStatus(ApprovedStatus.PENDING);
	Mockito.when(service.getClient(Mockito.any())).thenReturn(client);
	authorizeEndPoint.getAuthorization(request, response);
	
	}
	
	@Test
	void getAuthorization5() throws IOException {
	
	MockHttpServletRequest request = new MockHttpServletRequest();

	HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
	
//	request.setParameter("response_type", "client_credentials");
	request.setParameter("redirect_uri", "http://interopx.com/");   
	request.setParameter("state", "state123"); 
	request.setParameter("aud", "interopx.com"); 
	request.setParameter("launch", "http://interopx.com/"); 
	request.setParameter("code_challenge", "test123"); 
	request.setParameter("code_challenge_method", "test123"); 
	request.addParameter("refresh_token", "KwI5dY8SX2xnJ-uJ4CZzfFl6ppqAp7yQcJbFyJD0mbIGHMvCV04BedAMei4yQseRwHFfBkPYesECR2njzffyB5QJsZ04MrpK57CldfVhhpGG51wawmKevIMI4w3EuzCnFgrkR2juKvWp3Z3BK1aVvLziEfpF4drkX9s6tulqmxyasNYeGjH7u-BH-nDI0BVg1IFTMT6R3NmMA9QiE1fHTOO9xalxxb2tIOf28TPBNvcTr3cCp2SYam-9Bx");
	request.setParameter("client_id", "BuhZfFFzZPVMV4617Rd9OQsdIPw056");
	request.setParameter("client_secret", "YUE2TmFMWHBtdFh5LUx5ZVhUVHdRd0ZzVXRiTzB3V2Y1ZVVJazd3eEc3NnBpY1poNjU=");
	request.setParameter("scope", "user/Patient.read,user/AllergyIntolerance.read,user/CarePlan.read,user/CareTeam.read,user/Condition.read,user/Device.read,user/DiagnosticReport.read,user/DocumentReference.read,user/Encounter.read,user/Goal.read,user/Immunization.read,user/Location.read,user/MedicationRequest.read,user/Medication.read,user/Observation.read,user/Organization.read,user/Practitioner.read,user/PractitionerRole.read,user/Procedure.read,user/Provenance.read,user/RelatedPerson.read,patient/Medication.read,patient/Location.read,patient/Immunization.read,patiet/Goal.read,patient/Encounter.read,patient/DocumentReference.read,patient/DiagnosticReport.read,patient/Device.read,patient/Condition.read,patient/CareTeam.read,patient/CarePlan.read,patient/AllergyIntolerance.read,patient/Patient.read,patient/*.read,online_access,offline_access,fhirUser,openid,fhir_complete,launch/patient,launch,patient/MedicationRequest.read,patient/Observation.read,patient/Organization.read,patient/Practitioner.read,patient/PractitionerRole.read,patient/Procedure.read,patient/Provenance.read,patient/RelatedPerson.read");
	request.setParameter("client_assertion_type", "urn:ietf:params:oauth:client-assertion-type:jwt-bearer");
	request.setParameter("client_assertion", "eyJhbGciOiJSUzM4NCIsInR5cCI6IkpXVCJ9.eyJhdWQiOiJhcmNhZGlhIiwiaXNzIjoiLWtXN0F2RUxkOEFDZ05VaTZOSldGYmw4WSIsInN1YiI6Ii1rVzdBdkVMZDhBQ2dOVWk2TkpXRmJsOFkiLCJqdGkiOiIwNDIxNGYxNy04MmE2LTRkYTYtOGE1YS1mOGRjZjM5ZDJmOGYiLCJpYXQiOjE2NTYzMjM0MDYsImV4cCI6MTY1NjMzMzQwNn0.vD4wtrjN82_LFQBYQMF6WaTOKlNZiErLmQxLm90yiGwpbxYA6xBof2FH1bKm15sf4GTfWF5Dovp-Kic8dmv7yyQ0BwwJPCGsKl5J3DPE0DbDXYt4qAuu7vHVrQPs08LO_xTTcl2pbtWZw6MM8UzgXFCk2GMmF0hCybRf8b9sl6MUoQc8y5b3PDAAQ-hvaFF2hM1_ZOtn4BoLNwN2x88PwVCqzWRZtfo6UT5lf3o2roMlEHDf6cZ-s5oHtGBpnTsbdkGccGXeyL5zwMZGdPlcUaiJnXR3aMXr0-f7-N8cU-P3CXdD6lCJHQeE3pprsEu83J7BiHtxHD0RRhCYFpCbsQ");

	Clients client = new Clients();
	client.setApprovedStatus(ApprovedStatus.APPROVED);
	Mockito.when(service.getClient(Mockito.any())).thenReturn(client);
	authorizeEndPoint.getAuthorization(request, response);
	
	}
	
	@Test
	void getAuthorization6() throws IOException {
	
	MockHttpServletRequest request = new MockHttpServletRequest();

	HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
	
	request.setParameter("response_type", "client_credentials");
	request.setParameter("redirect_uri", "http://interopx.com/");   
	request.setParameter("state", "state123"); 
//	request.setParameter("aud", "interopx.com"); 
	request.setParameter("launch", "http://interopx.com/"); 
	request.setParameter("code_challenge", "test123"); 
	request.setParameter("code_challenge_method", "test123"); 
	request.addParameter("refresh_token", "KwI5dY8SX2xnJ-uJ4CZzfFl6ppqAp7yQcJbFyJD0mbIGHMvCV04BedAMei4yQseRwHFfBkPYesECR2njzffyB5QJsZ04MrpK57CldfVhhpGG51wawmKevIMI4w3EuzCnFgrkR2juKvWp3Z3BK1aVvLziEfpF4drkX9s6tulqmxyasNYeGjH7u-BH-nDI0BVg1IFTMT6R3NmMA9QiE1fHTOO9xalxxb2tIOf28TPBNvcTr3cCp2SYam-9Bx");
	request.setParameter("client_id", "BuhZfFFzZPVMV4617Rd9OQsdIPw056");
	request.setParameter("client_secret", "YUE2TmFMWHBtdFh5LUx5ZVhUVHdRd0ZzVXRiTzB3V2Y1ZVVJazd3eEc3NnBpY1poNjU=");
	request.setParameter("scope", "user/Patient.read,user/AllergyIntolerance.read,user/CarePlan.read,user/CareTeam.read,user/Condition.read,user/Device.read,user/DiagnosticReport.read,user/DocumentReference.read,user/Encounter.read,user/Goal.read,user/Immunization.read,user/Location.read,user/MedicationRequest.read,user/Medication.read,user/Observation.read,user/Organization.read,user/Practitioner.read,user/PractitionerRole.read,user/Procedure.read,user/Provenance.read,user/RelatedPerson.read,patient/Medication.read,patient/Location.read,patient/Immunization.read,patiet/Goal.read,patient/Encounter.read,patient/DocumentReference.read,patient/DiagnosticReport.read,patient/Device.read,patient/Condition.read,patient/CareTeam.read,patient/CarePlan.read,patient/AllergyIntolerance.read,patient/Patient.read,patient/*.read,online_access,offline_access,fhirUser,openid,fhir_complete,launch/patient,launch,patient/MedicationRequest.read,patient/Observation.read,patient/Organization.read,patient/Practitioner.read,patient/PractitionerRole.read,patient/Procedure.read,patient/Provenance.read,patient/RelatedPerson.read");
	request.setParameter("client_assertion_type", "urn:ietf:params:oauth:client-assertion-type:jwt-bearer");
	request.setParameter("client_assertion", "eyJhbGciOiJSUzM4NCIsInR5cCI6IkpXVCJ9.eyJhdWQiOiJhcmNhZGlhIiwiaXNzIjoiLWtXN0F2RUxkOEFDZ05VaTZOSldGYmw4WSIsInN1YiI6Ii1rVzdBdkVMZDhBQ2dOVWk2TkpXRmJsOFkiLCJqdGkiOiIwNDIxNGYxNy04MmE2LTRkYTYtOGE1YS1mOGRjZjM5ZDJmOGYiLCJpYXQiOjE2NTYzMjM0MDYsImV4cCI6MTY1NjMzMzQwNn0.vD4wtrjN82_LFQBYQMF6WaTOKlNZiErLmQxLm90yiGwpbxYA6xBof2FH1bKm15sf4GTfWF5Dovp-Kic8dmv7yyQ0BwwJPCGsKl5J3DPE0DbDXYt4qAuu7vHVrQPs08LO_xTTcl2pbtWZw6MM8UzgXFCk2GMmF0hCybRf8b9sl6MUoQc8y5b3PDAAQ-hvaFF2hM1_ZOtn4BoLNwN2x88PwVCqzWRZtfo6UT5lf3o2roMlEHDf6cZ-s5oHtGBpnTsbdkGccGXeyL5zwMZGdPlcUaiJnXR3aMXr0-f7-N8cU-P3CXdD6lCJHQeE3pprsEu83J7BiHtxHD0RRhCYFpCbsQ");

	Clients client = new Clients();
	client.setApprovedStatus(ApprovedStatus.APPROVED);
	Mockito.when(service.getClient(Mockito.any())).thenReturn(client);
	authorizeEndPoint.getAuthorization(request, response);
	
	}
	
	@Test
	void getAuthorization7() throws IOException {
	
	MockHttpServletRequest request = new MockHttpServletRequest();

	HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
	
	request.setParameter("response_type", "client_credentials");
	request.setParameter("redirect_uri", "http://interopx.com/");   
	request.setParameter("state", "state123"); 
	request.setParameter("aud", "interopx.com"); 
	request.setParameter("launch", "http://interopx.com/"); 
	request.setParameter("code_challenge", "test123"); 
	request.setParameter("code_challenge_method", "test123"); 
	request.addParameter("refresh_token", "KwI5dY8SX2xnJ-uJ4CZzfFl6ppqAp7yQcJbFyJD0mbIGHMvCV04BedAMei4yQseRwHFfBkPYesECR2njzffyB5QJsZ04MrpK57CldfVhhpGG51wawmKevIMI4w3EuzCnFgrkR2juKvWp3Z3BK1aVvLziEfpF4drkX9s6tulqmxyasNYeGjH7u-BH-nDI0BVg1IFTMT6R3NmMA9QiE1fHTOO9xalxxb2tIOf28TPBNvcTr3cCp2SYam-9Bx");
	request.setParameter("client_id", "BuhZfFFzZPVMV4617Rd9OQsdIPw056");
	request.setParameter("client_secret", "YUE2TmFMWHBtdFh5LUx5ZVhUVHdRd0ZzVXRiTzB3V2Y1ZVVJazd3eEc3NnBpY1poNjU=");
	request.setParameter("scope", "user/Patient.read,user/AllergyIntolerance.read,user/CarePlan.read,user/CareTeam.read,user/Condition.read,user/Device.read,user/DiagnosticReport.read,user/DocumentReference.read,user/Encounter.read,user/Goal.read,user/Immunization.read,user/Location.read,user/MedicationRequest.read,user/Medication.read,user/Observation.read,user/Organization.read,user/Practitioner.read,user/PractitionerRole.read,user/Procedure.read,user/Provenance.read,user/RelatedPerson.read,patient/Medication.read,patient/Location.read,patient/Immunization.read,patiet/Goal.read,patient/Encounter.read,patient/DocumentReference.read,patient/DiagnosticReport.read,patient/Device.read,patient/Condition.read,patient/CareTeam.read,patient/CarePlan.read,patient/AllergyIntolerance.read,patient/Patient.read,patient/*.read,online_access,offline_access,fhirUser,openid,fhir_complete,launch/patient,launch,patient/MedicationRequest.read,patient/Observation.read,patient/Organization.read,patient/Practitioner.read,patient/PractitionerRole.read,patient/Procedure.read,patient/Provenance.read,patient/RelatedPerson.read");
	request.setParameter("client_assertion_type", "urn:ietf:params:oauth:client-assertion-type:jwt-bearer");
	request.setParameter("client_assertion", "eyJhbGciOiJSUzM4NCIsInR5cCI6IkpXVCJ9.eyJhdWQiOiJhcmNhZGlhIiwiaXNzIjoiLWtXN0F2RUxkOEFDZ05VaTZOSldGYmw4WSIsInN1YiI6Ii1rVzdBdkVMZDhBQ2dOVWk2TkpXRmJsOFkiLCJqdGkiOiIwNDIxNGYxNy04MmE2LTRkYTYtOGE1YS1mOGRjZjM5ZDJmOGYiLCJpYXQiOjE2NTYzMjM0MDYsImV4cCI6MTY1NjMzMzQwNn0.vD4wtrjN82_LFQBYQMF6WaTOKlNZiErLmQxLm90yiGwpbxYA6xBof2FH1bKm15sf4GTfWF5Dovp-Kic8dmv7yyQ0BwwJPCGsKl5J3DPE0DbDXYt4qAuu7vHVrQPs08LO_xTTcl2pbtWZw6MM8UzgXFCk2GMmF0hCybRf8b9sl6MUoQc8y5b3PDAAQ-hvaFF2hM1_ZOtn4BoLNwN2x88PwVCqzWRZtfo6UT5lf3o2roMlEHDf6cZ-s5oHtGBpnTsbdkGccGXeyL5zwMZGdPlcUaiJnXR3aMXr0-f7-N8cU-P3CXdD6lCJHQeE3pprsEu83J7BiHtxHD0RRhCYFpCbsQ");

	Clients client = new Clients();
	client.setApprovedStatus(ApprovedStatus.APPROVED);
	Mockito.when(service.getClient(Mockito.any())).thenReturn(client);
	authorizeEndPoint.getAuthorization(request, response);
	
	}
	
	@Test
	void getAuthorization8() throws IOException {
	
	MockHttpServletRequest request = new MockHttpServletRequest();

	HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
	
	request.setParameter("response_type", "client_credentials");
	request.setParameter("redirect_uri", "http://interopx.com/");   
	request.setParameter("state", "state123"); 
	request.setParameter("aud", "http://localhost/InteropXFHIR/fhir"); 
	request.setParameter("launch", "http://interopx.com/"); 
	request.setParameter("code_challenge", "test123"); 
	request.setParameter("code_challenge_method", "test123"); 
	request.addParameter("refresh_token", "KwI5dY8SX2xnJ-uJ4CZzfFl6ppqAp7yQcJbFyJD0mbIGHMvCV04BedAMei4yQseRwHFfBkPYesECR2njzffyB5QJsZ04MrpK57CldfVhhpGG51wawmKevIMI4w3EuzCnFgrkR2juKvWp3Z3BK1aVvLziEfpF4drkX9s6tulqmxyasNYeGjH7u-BH-nDI0BVg1IFTMT6R3NmMA9QiE1fHTOO9xalxxb2tIOf28TPBNvcTr3cCp2SYam-9Bx");
	request.setParameter("client_id", "BuhZfFFzZPVMV4617Rd9OQsdIPw056");
	request.setParameter("client_secret", "YUE2TmFMWHBtdFh5LUx5ZVhUVHdRd0ZzVXRiTzB3V2Y1ZVVJazd3eEc3NnBpY1poNjU=");
	request.setParameter("scope", "user/Patient.read,user/AllergyIntolerance.read,user/CarePlan.read,user/CareTeam.read,user/Condition.read,user/Device.read,user/DiagnosticReport.read,user/DocumentReference.read,user/Encounter.read,user/Goal.read,user/Immunization.read,user/Location.read,user/MedicationRequest.read,user/Medication.read,user/Observation.read,user/Organization.read,user/Practitioner.read,user/PractitionerRole.read,user/Procedure.read,user/Provenance.read,user/RelatedPerson.read,patient/Medication.read,patient/Location.read,patient/Immunization.read,patiet/Goal.read,patient/Encounter.read,patient/DocumentReference.read,patient/DiagnosticReport.read,patient/Device.read,patient/Condition.read,patient/CareTeam.read,patient/CarePlan.read,patient/AllergyIntolerance.read,patient/Patient.read,patient/*.read,online_access,offline_access,fhirUser,openid,fhir_complete,launch/patient,launch,patient/MedicationRequest.read,patient/Observation.read,patient/Organization.read,patient/Practitioner.read,patient/PractitionerRole.read,patient/Procedure.read,patient/Provenance.read,patient/RelatedPerson.read");
	request.setParameter("client_assertion_type", "urn:ietf:params:oauth:client-assertion-type:jwt-bearer");
	request.setParameter("client_assertion", "eyJhbGciOiJSUzM4NCIsInR5cCI6IkpXVCJ9.eyJhdWQiOiJhcmNhZGlhIiwiaXNzIjoiLWtXN0F2RUxkOEFDZ05VaTZOSldGYmw4WSIsInN1YiI6Ii1rVzdBdkVMZDhBQ2dOVWk2TkpXRmJsOFkiLCJqdGkiOiIwNDIxNGYxNy04MmE2LTRkYTYtOGE1YS1mOGRjZjM5ZDJmOGYiLCJpYXQiOjE2NTYzMjM0MDYsImV4cCI6MTY1NjMzMzQwNn0.vD4wtrjN82_LFQBYQMF6WaTOKlNZiErLmQxLm90yiGwpbxYA6xBof2FH1bKm15sf4GTfWF5Dovp-Kic8dmv7yyQ0BwwJPCGsKl5J3DPE0DbDXYt4qAuu7vHVrQPs08LO_xTTcl2pbtWZw6MM8UzgXFCk2GMmF0hCybRf8b9sl6MUoQc8y5b3PDAAQ-hvaFF2hM1_ZOtn4BoLNwN2x88PwVCqzWRZtfo6UT5lf3o2roMlEHDf6cZ-s5oHtGBpnTsbdkGccGXeyL5zwMZGdPlcUaiJnXR3aMXr0-f7-N8cU-P3CXdD6lCJHQeE3pprsEu83J7BiHtxHD0RRhCYFpCbsQ");

	Clients client = new Clients();
	client.setApprovedStatus(ApprovedStatus.APPROVED);
	client.setLaunchId("1234");
	Mockito.when(service.getClient(Mockito.any())).thenReturn(client);
	authorizeEndPoint.getAuthorization(request, response);
	
	}
	
	@Test
	void getAuthorization9() throws IOException {
	
	MockHttpServletRequest request = new MockHttpServletRequest();

	HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
	
	request.setParameter("response_type", "client_credentials");
	request.setParameter("redirect_uri", "http://interopx.com/");   
	request.setParameter("state", "state123"); 
	request.setParameter("aud", "http://localhost/InteropXFHIR/fhir"); 
//	request.setParameter("launch", "http://interopx.com/"); 
	request.setParameter("code_challenge", "test123"); 
	request.setParameter("code_challenge_method", "test123"); 
	request.addParameter("refresh_token", "KwI5dY8SX2xnJ-uJ4CZzfFl6ppqAp7yQcJbFyJD0mbIGHMvCV04BedAMei4yQseRwHFfBkPYesECR2njzffyB5QJsZ04MrpK57CldfVhhpGG51wawmKevIMI4w3EuzCnFgrkR2juKvWp3Z3BK1aVvLziEfpF4drkX9s6tulqmxyasNYeGjH7u-BH-nDI0BVg1IFTMT6R3NmMA9QiE1fHTOO9xalxxb2tIOf28TPBNvcTr3cCp2SYam-9Bx");
	request.setParameter("client_id", "BuhZfFFzZPVMV4617Rd9OQsdIPw056");
	request.setParameter("client_secret", "YUE2TmFMWHBtdFh5LUx5ZVhUVHdRd0ZzVXRiTzB3V2Y1ZVVJazd3eEc3NnBpY1poNjU=");
	request.setParameter("scope", "user/Patient.read,user/AllergyIntolerance.read,user/CarePlan.read,user/CareTeam.read,user/Condition.read,user/Device.read,user/DiagnosticReport.read,user/DocumentReference.read,user/Encounter.read,user/Goal.read,user/Immunization.read,user/Location.read,user/MedicationRequest.read,user/Medication.read,user/Observation.read,user/Organization.read,user/Practitioner.read,user/PractitionerRole.read,user/Procedure.read,user/Provenance.read,user/RelatedPerson.read,patient/Medication.read,patient/Location.read,patient/Immunization.read,patiet/Goal.read,patient/Encounter.read,patient/DocumentReference.read,patient/DiagnosticReport.read,patient/Device.read,patient/Condition.read,patient/CareTeam.read,patient/CarePlan.read,patient/AllergyIntolerance.read,patient/Patient.read,patient/*.read,online_access,offline_access,fhirUser,openid,fhir_complete,launch/patient,launch,patient/MedicationRequest.read,patient/Observation.read,patient/Organization.read,patient/Practitioner.read,patient/PractitionerRole.read,patient/Procedure.read,patient/Provenance.read,patient/RelatedPerson.read");
	request.setParameter("client_assertion_type", "urn:ietf:params:oauth:client-assertion-type:jwt-bearer");
	request.setParameter("client_assertion", "eyJhbGciOiJSUzM4NCIsInR5cCI6IkpXVCJ9.eyJhdWQiOiJhcmNhZGlhIiwiaXNzIjoiLWtXN0F2RUxkOEFDZ05VaTZOSldGYmw4WSIsInN1YiI6Ii1rVzdBdkVMZDhBQ2dOVWk2TkpXRmJsOFkiLCJqdGkiOiIwNDIxNGYxNy04MmE2LTRkYTYtOGE1YS1mOGRjZjM5ZDJmOGYiLCJpYXQiOjE2NTYzMjM0MDYsImV4cCI6MTY1NjMzMzQwNn0.vD4wtrjN82_LFQBYQMF6WaTOKlNZiErLmQxLm90yiGwpbxYA6xBof2FH1bKm15sf4GTfWF5Dovp-Kic8dmv7yyQ0BwwJPCGsKl5J3DPE0DbDXYt4qAuu7vHVrQPs08LO_xTTcl2pbtWZw6MM8UzgXFCk2GMmF0hCybRf8b9sl6MUoQc8y5b3PDAAQ-hvaFF2hM1_ZOtn4BoLNwN2x88PwVCqzWRZtfo6UT5lf3o2roMlEHDf6cZ-s5oHtGBpnTsbdkGccGXeyL5zwMZGdPlcUaiJnXR3aMXr0-f7-N8cU-P3CXdD6lCJHQeE3pprsEu83J7BiHtxHD0RRhCYFpCbsQ");

	Clients client = new Clients();
	client.setApprovedStatus(ApprovedStatus.APPROVED);
	client.setLaunchId("1234");
	client.setScope("user/Patient.read,user/AllergyIntolerance.read,user/CarePlan.read,user/CareTeam.read,user/Condition.read,user/Device.read,user/DiagnosticReport.read,user/DocumentReference.read,user/Encounter.read,user/Goal.read,user/Immunization.read,user/Location.read,user/MedicationRequest.read,user/Medication.read,user/Observation.read,user/Organization.read,user/Practitioner.read,user/PractitionerRole.read,user/Procedure.read,user/Provenance.read,user/RelatedPerson.read,patient/Medication.read,patient/Location.read,patient/Immunization.read,patiet/Goal.read,patient/Encounter.read,patient/DocumentReference.read,patient/DiagnosticReport.read,patient/Device.read,patient/Condition.read,patient/CareTeam.read,patient/CarePlan.read,patient/AllergyIntolerance.read,patient/Patient.read,patient/*.read,online_access,offline_access,fhirUser,openid,fhir_complete,launch/patient,launch,patient/MedicationRequest.read,patient/Observation.read,patient/Organization.read,patient/Practitioner.read,patient/PractitionerRole.read,patient/Procedure.read,patient/Provenance.read,patient/RelatedPerson.read");
	
	Mockito.when(service.getClient(Mockito.any())).thenReturn(client);
	
	Users user = new Users();
	user.setFirstName("Ram");
	user.setMobile_number("9837373388");
	user.setUser_email("ram@gmail.com");
	user.setUser_name("xyram");
	user.setUser_password("1234");
	user.setLastName("Kumar");
	
	Mockito.when(userService.getUserById(Mockito.any())).thenReturn(user);
	
	authorizeEndPoint.getAuthorization(request, response);
	
	}
	
	@Test
	void getAuthorization10() throws IOException {
	
	MockHttpServletRequest request = new MockHttpServletRequest();

	HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
	
	request.setParameter("response_type", "client_credentials");
	request.setParameter("redirect_uri", "http://interopx.com/");   
	request.setParameter("state", "state123"); 
	request.setParameter("aud", "http://localhost/InteropXFHIR/fhir"); 
//	request.setParameter("launch", "http://interopx.com/"); 
	request.setParameter("code_challenge", "test123"); 
	request.setParameter("code_challenge_method", "test123"); 
	request.addParameter("refresh_token", "KwI5dY8SX2xnJ-uJ4CZzfFl6ppqAp7yQcJbFyJD0mbIGHMvCV04BedAMei4yQseRwHFfBkPYesECR2njzffyB5QJsZ04MrpK57CldfVhhpGG51wawmKevIMI4w3EuzCnFgrkR2juKvWp3Z3BK1aVvLziEfpF4drkX9s6tulqmxyasNYeGjH7u-BH-nDI0BVg1IFTMT6R3NmMA9QiE1fHTOO9xalxxb2tIOf28TPBNvcTr3cCp2SYam-9Bx");
	request.setParameter("client_id", "BuhZfFFzZPVMV4617Rd9OQsdIPw056");
	request.setParameter("client_secret", "YUE2TmFMWHBtdFh5LUx5ZVhUVHdRd0ZzVXRiTzB3V2Y1ZVVJazd3eEc3NnBpY1poNjU=");
	request.setParameter("scope", "user/Patient.read,user/AllergyIntolerance.read,user/CarePlan.read,user/CareTeam.read,user/Condition.read,user/Device.read,user/DiagnosticReport.read,user/DocumentReference.read,user/Encounter.read,user/Goal.read,user/Immunization.read,user/Location.read,user/MedicationRequest.read,user/Medication.read,user/Observation.read,user/Organization.read,user/Practitioner.read,user/PractitionerRole.read,user/Procedure.read,user/Provenance.read,user/RelatedPerson.read,patient/Medication.read,patient/Location.read,patient/Immunization.read,patiet/Goal.read,patient/Encounter.read,patient/DocumentReference.read,patient/DiagnosticReport.read,patient/Device.read,patient/Condition.read,patient/CareTeam.read,patient/CarePlan.read,patient/AllergyIntolerance.read,patient/Patient.read,patient/*.read,online_access,offline_access,fhirUser,openid,fhir_complete,launch/patient,launch,patient/MedicationRequest.read,patient/Observation.read,patient/Organization.read,patient/Practitioner.read,patient/PractitionerRole.read,patient/Procedure.read,patient/Provenance.read,patient/RelatedPerson.read");
	request.setParameter("client_assertion_type", "urn:ietf:params:oauth:client-assertion-type:jwt-bearer");
	request.setParameter("client_assertion", "eyJhbGciOiJSUzM4NCIsInR5cCI6IkpXVCJ9.eyJhdWQiOiJhcmNhZGlhIiwiaXNzIjoiLWtXN0F2RUxkOEFDZ05VaTZOSldGYmw4WSIsInN1YiI6Ii1rVzdBdkVMZDhBQ2dOVWk2TkpXRmJsOFkiLCJqdGkiOiIwNDIxNGYxNy04MmE2LTRkYTYtOGE1YS1mOGRjZjM5ZDJmOGYiLCJpYXQiOjE2NTYzMjM0MDYsImV4cCI6MTY1NjMzMzQwNn0.vD4wtrjN82_LFQBYQMF6WaTOKlNZiErLmQxLm90yiGwpbxYA6xBof2FH1bKm15sf4GTfWF5Dovp-Kic8dmv7yyQ0BwwJPCGsKl5J3DPE0DbDXYt4qAuu7vHVrQPs08LO_xTTcl2pbtWZw6MM8UzgXFCk2GMmF0hCybRf8b9sl6MUoQc8y5b3PDAAQ-hvaFF2hM1_ZOtn4BoLNwN2x88PwVCqzWRZtfo6UT5lf3o2roMlEHDf6cZ-s5oHtGBpnTsbdkGccGXeyL5zwMZGdPlcUaiJnXR3aMXr0-f7-N8cU-P3CXdD6lCJHQeE3pprsEu83J7BiHtxHD0RRhCYFpCbsQ");

	Clients client = new Clients();
	client.setApprovedStatus(ApprovedStatus.APPROVED);
	client.setLaunchId("1234");
	client.setScope("user/Patient.read");
	
	Mockito.when(service.getClient(Mockito.any())).thenReturn(client);
	
	Users user = new Users();
	user.setFirstName("Ram");
	user.setMobile_number("9837373388");
	user.setUser_email("ram@gmail.com");
	user.setUser_name("xyram");
	user.setUser_password("1234");
	user.setLastName("Kumar");
	
	Mockito.when(userService.getUserById(Mockito.any())).thenReturn(user);
		
	authorizeEndPoint.getAuthorization(request, response);
	
	}
	
	@Test
	void getAuthorization11() throws IOException {
	
	MockHttpServletRequest request = new MockHttpServletRequest();

	HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
	
	request.setParameter("response_type", "client_credentials");
	request.setParameter("redirect_uri", "http://interopx.com/");   
	request.setParameter("state", "state123"); 
	request.setParameter("aud", "http://localhost/InteropXFHIR/fhir"); 
//	request.setParameter("launch", "http://interopx.com/"); 
	request.setParameter("code_challenge", "test123"); 
	request.setParameter("code_challenge_method", "test123"); 
	request.addParameter("refresh_token", "KwI5dY8SX2xnJ-uJ4CZzfFl6ppqAp7yQcJbFyJD0mbIGHMvCV04BedAMei4yQseRwHFfBkPYesECR2njzffyB5QJsZ04MrpK57CldfVhhpGG51wawmKevIMI4w3EuzCnFgrkR2juKvWp3Z3BK1aVvLziEfpF4drkX9s6tulqmxyasNYeGjH7u-BH-nDI0BVg1IFTMT6R3NmMA9QiE1fHTOO9xalxxb2tIOf28TPBNvcTr3cCp2SYam-9Bx");
	request.setParameter("client_id", "BuhZfFFzZPVMV4617Rd9OQsdIPw056");
	request.setParameter("client_secret", "YUE2TmFMWHBtdFh5LUx5ZVhUVHdRd0ZzVXRiTzB3V2Y1ZVVJazd3eEc3NnBpY1poNjU=");
	request.setParameter("scope", "user/Patient.read,user/AllergyIntolerance.read,user/CarePlan.read,user/CareTeam.read,user/Condition.read,user/Device.read,user/DiagnosticReport.read,user/DocumentReference.read,user/Encounter.read,user/Goal.read,user/Immunization.read,user/Location.read,user/MedicationRequest.read,user/Medication.read,user/Observation.read,user/Organization.read,user/Practitioner.read,user/PractitionerRole.read,user/Procedure.read,user/Provenance.read,user/RelatedPerson.read,patient/Medication.read,patient/Location.read,patient/Immunization.read,patiet/Goal.read,patient/Encounter.read,patient/DocumentReference.read,patient/DiagnosticReport.read,patient/Device.read,patient/Condition.read,patient/CareTeam.read,patient/CarePlan.read,patient/AllergyIntolerance.read,patient/Patient.read,patient/*.read,online_access,offline_access,fhirUser,openid,fhir_complete,launch/patient,launch,patient/MedicationRequest.read,patient/Observation.read,patient/Organization.read,patient/Practitioner.read,patient/PractitionerRole.read,patient/Procedure.read,patient/Provenance.read,patient/RelatedPerson.read");
	request.setParameter("client_assertion_type", "urn:ietf:params:oauth:client-assertion-type:jwt-bearer");
	request.setParameter("client_assertion", "eyJhbGciOiJSUzM4NCIsInR5cCI6IkpXVCJ9.eyJhdWQiOiJhcmNhZGlhIiwiaXNzIjoiLWtXN0F2RUxkOEFDZ05VaTZOSldGYmw4WSIsInN1YiI6Ii1rVzdBdkVMZDhBQ2dOVWk2TkpXRmJsOFkiLCJqdGkiOiIwNDIxNGYxNy04MmE2LTRkYTYtOGE1YS1mOGRjZjM5ZDJmOGYiLCJpYXQiOjE2NTYzMjM0MDYsImV4cCI6MTY1NjMzMzQwNn0.vD4wtrjN82_LFQBYQMF6WaTOKlNZiErLmQxLm90yiGwpbxYA6xBof2FH1bKm15sf4GTfWF5Dovp-Kic8dmv7yyQ0BwwJPCGsKl5J3DPE0DbDXYt4qAuu7vHVrQPs08LO_xTTcl2pbtWZw6MM8UzgXFCk2GMmF0hCybRf8b9sl6MUoQc8y5b3PDAAQ-hvaFF2hM1_ZOtn4BoLNwN2x88PwVCqzWRZtfo6UT5lf3o2roMlEHDf6cZ-s5oHtGBpnTsbdkGccGXeyL5zwMZGdPlcUaiJnXR3aMXr0-f7-N8cU-P3CXdD6lCJHQeE3pprsEu83J7BiHtxHD0RRhCYFpCbsQ");

	Clients client = new Clients();
	client.setApprovedStatus(ApprovedStatus.APPROVED);
	client.setLaunchId("1234");
	client.setScope("user/Patient.read,user/AllergyIntolerance.read,user/CarePlan.read,user/CareTeam.read,user/Condition.read,user/Device.read,user/DiagnosticReport.read,user/DocumentReference.read,user/Encounter.read,user/Goal.read,user/Immunization.read,user/Location.read,user/MedicationRequest.read,user/Medication.read,user/Observation.read,user/Organization.read,user/Practitioner.read,user/PractitionerRole.read,user/Procedure.read,user/Provenance.read,user/RelatedPerson.read,patient/Medication.read,patient/Location.read,patient/Immunization.read,patiet/Goal.read,patient/Encounter.read,patient/DocumentReference.read,patient/DiagnosticReport.read,patient/Device.read,patient/Condition.read,patient/CareTeam.read,patient/CarePlan.read,patient/AllergyIntolerance.read,patient/Patient.read,patient/*.read,online_access,offline_access,fhirUser,openid,fhir_complete,launch/patient,launch,patient/MedicationRequest.read,patient/Observation.read,patient/Organization.read,patient/Practitioner.read,patient/PractitionerRole.read,patient/Procedure.read,patient/Provenance.read,patient/RelatedPerson.read");
	
	Mockito.when(service.getClient(Mockito.any())).thenReturn(client);
	
	Users user = new Users();
	user.setFirstName("Ram");
	user.setMobile_number("9837373388");
	user.setUser_email("ram@gmail.com");
	user.setUser_name("xyram");
	user.setUser_password("1234");
	user.setLastName("Kumar");
	
	Mockito.when(userService.getUserById(Mockito.any())).thenReturn(user);
	
	OAuthAuthzRequest authzRequest = mock(OAuthAuthzRequest.class);
	
	authorizeEndPoint.getAuthorization(request, response);
	
	}
	
	@Test
	void testGetAuthentication1() throws IOException {
		String requestType = "Allow";
		String scopes = "Scope";
		String transactionId = "1";
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		String url = CommonUtil.getBaseUrl(request) + "?Invalid transaction_id";
		assertEquals(url, authorizeEndPoint.getAuthentication(request, response, scopes, requestType, transactionId));

	}
	
	@Test
	void testGetAuthentication2() throws IOException {
		String requestType = "Allow";
		String scopes = "Scope";
		String transactionId = "1";
		Clients client = clientDetails();
		AuthorizationDetails tempAuth = createAuthorizationDetails();
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		Mockito.when(authTempService.getAuthenticationById(Mockito.anyString())).thenReturn(tempAuth);
		
		Mockito.when(service.getClient(Mockito.any())).thenReturn(client);
		
		String url = tempAuth.getRedirectUri().trim() + "?code=" + tempAuth.getAuthCode().trim() + "&state="
				+ tempAuth.getState().trim();
		assertEquals(url, authorizeEndPoint.getAuthentication(request, response, scopes, requestType, transactionId));

	}
	 
	
	@Test
	void testGetAuthentication3() throws IOException {
		String requestType = "type";
		String scopes = "launch";
		String transactionId = "1";
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		AuthorizationDetails tempAuth = createAuthorizationDetails();
		when(authTempService.getAuthenticationById(Mockito.anyString())).thenReturn(tempAuth);

		Clients clientDetails = clientDetails();
		Mockito.when(service.getClient(Mockito.any())).thenReturn(clientDetails);

		tempAuth.setScope(scopes);
		authTempService.saveOrUpdate(tempAuth);
		Clients dafClientRegister = clientDetails();
		Mockito.when(service.getClient(Mockito.any())).thenReturn(dafClientRegister);
		
		Users user = userDetails(); 
		
		Mockito.when(userService.getUserById(Mockito.any())).thenReturn(user);

		String url = tempAuth.getRedirectUri().trim() + "?error=Access Denied&error_description=Access Denied&state="
				+ tempAuth.getState().trim();
		assertEquals(url, authorizeEndPoint.getAuthentication(request, response, scopes, requestType, transactionId));
	}

	@Test
	void testGetAuthentication4() throws IOException {
		AuthorizationDetails tempAuth = createAuthorizationDetails();

		Clients clientDetails = clientDetails();
		Clients dafClientRegister = clientDetails();
		Users dafUserRegister = userDetails();

		String requestType = "Allow";
		String scopes = "launch/patient";
		String transactionId = tempAuth.getTransactionId();
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		when(authTempService.getAuthenticationById(Mockito.anyString())).thenReturn(tempAuth);
		Mockito.when(service.getClient(Mockito.anyString())).thenReturn(clientDetails);

		List<String> scopeList = new ArrayList<>();
		scopeList.add(scopes);
		when(authTempService.saveOrUpdate(tempAuth)).thenReturn(tempAuth);
		when(service.getClient(Mockito.anyString())).thenReturn(dafClientRegister);

		userService.getUserById(dafClientRegister.getUserId());
		when(userService.getUserById(Mockito.any())).thenReturn(dafUserRegister);
		String url = "http://localhost/#/patientlist?transaction_id=123";

		assertEquals(url, authorizeEndPoint.getAuthentication(request, response, tempAuth.getTransactionId(), requestType, scopes));

	}

}
