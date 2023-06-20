package com.interopx.fhir.auth.server.service.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.interopx.fhir.auth.server.dao.ClientsDao;
import com.interopx.fhir.auth.server.model.Clients;
import com.interopx.fhir.auth.server.model.CurrentUserDetails;
import com.interopx.fhir.auth.server.model.Users;
import com.interopx.fhir.auth.server.service.UsersService;
import com.interopx.fhir.auth.server.util.AuthUtil.ApprovedStatus;

import net.minidev.json.JSONObject;

@SpringBootTest
@ActiveProfiles("test")
class ClientsServiceImplTest {

	@InjectMocks
	ClientsServiceImpl clientsServiceImpl;

	@Mock
	ClientsDao clientDao;

	@Mock
	RestTemplate restTemplate;

	@Mock
	UsersService userRegistrationService; 
	
	@Mock 
	CurrentUserDetails currentUserDetails;

	@Test
	void testRegisterClient() {
		Clients client = new Clients();
		String registerToken = "KwI5dY8SX2xnJ-uJ4CZzfFl6ppqAp7yQcJbFyJD0mbIGHMvCV04BedAMei4yQseRwHFfBkPYesECR2njzffyB5QJsZ04MrpK57CldfVhhpGG51wawmKevIMI4w3EuzCnFgrkR2juKvWp3Z3BK1aVvLziEfpF4drkX9s6tulqmxyasNYeGjH7u-BH-nDI0BVg1IFTMT6R3NmMA9QiE1fHTOO9xalxxb2tIOf28TPBNvcTr3cCp2SYam-9Bx";
		String clientSecret = "YUE2TmFMWHBtdFh5LUx5ZVhUVHdRd0ZzVXRiTzB3V2Y1ZVVJazd3eEc3NnBpY1poNjU=";
		String clientId = "BuhZfFFzZPVMV4617Rd9OQsdIPw056";
		String clientLaunchId = "https://inferno.healthit.gov/inferno/oauth2/static/launch";
		client.setRegisterToken(registerToken);
		client.setClientId(clientId);
		client.setConfidentialClient(true);
		client.setClientSecret(clientSecret);
		client.setApprovedStatus(ApprovedStatus.PENDING);
		client.setLaunchId(clientLaunchId);
		client.setApprovedStatus(ApprovedStatus.APPROVED);
		client.setDynamicClient(true);
		when(clientDao.registerClient(Mockito.any())).thenReturn(client);
		assertEquals(client, clientsServiceImpl.registerClient(client));
	}

	@Test
	void testUpdateClient() {
		Clients client = new Clients();
		String clientSecret = "YUE2TmFMWHBtdFh5LUx5ZVhUVHdRd0ZzVXRiTzB3V2Y1ZVVJazd3eEc3NnBpY1poNzhfvhgsd";
		client.setClientSecret(clientSecret);
		client.setConfidentialClient(true);
		when(clientDao.updateClient(Mockito.any())).thenReturn(client);
		assertEquals(client, clientsServiceImpl.updateClient(client));
	}

	@Test
	void testUpdateBackendClient() throws Exception{
		Clients client = new Clients();
		MockHttpServletRequest request = new MockHttpServletRequest();
		String registerToken = "KwI5dY8SX2xnJ-uJ4CZzfFl6ppqAp7yQcJbFyJD0mbIGHMvCV04BedAMei4yQseRwHFfBkPYesECR2njzffyB5QJsZ04MrpK57CldfVhhpGG51wawmKevIMI4w3EuzCnFgrkR2juKvWp3Z3BK1aVvLziEfpF4drkX9s6tulqmxyasNYeG";
		String clientSecret = "YUE2TmFMWHBtdFh5LUx5ZVhUVHdRd0ZzVXRiTzB3V2Yksdhfkdjfhjkd";
		String clientId = "vPVVKzscFYDBZ0uFDg1mJ-2ml";
		client.setRegisterToken(registerToken);
		client.setClientId(clientId);
		client.setConfidentialClient(true);
		client.setClientSecret(clientSecret);
		client.setUserId("1");
		client.setId(1);
		
//		CurrentUserDetails currentUserDetails = mock(CurrentUserDetails.class);
//		currentUserDetails.setUserId("1234");
		
		Mockito.when(currentUserDetails.getUserId()).thenReturn("1");
		Mockito.when(clientDao.getClient(Mockito.anyString())).thenReturn(client);
		Mockito.when(clientDao.verifyClientWithUser(any(), anyString(), any())).thenReturn(true);
		when(clientDao.updateClient(Mockito.any())).thenReturn(client);

		assertEquals(client, clientsServiceImpl.updateBackendClient(client, request));

	}
	
	@Test
	void updateClient() {
		Clients client = new Clients();
		String registerToken = "KwI5dY8SX2xnJ-uJ4CZzfFl6ppqAp7yQcJbFyJD0mbIGHMvCV04BedAMei4yQseRwHFfBkPYesECR2njzffyB5QJsZ04MrpK57CldfVhhpGG51wawmKevIMI4w3EuzCnFgrkR2juKvWp3Z3BK1aVvLziEfpF4drkX9s6tulqmxyasNYeG";
		String clientId = "vPVVKzscFYDBZ0uFDg1mJ-2ml";
		client.setRegisterToken(registerToken);
		client.setClientId(clientId);
		client.setConfidentialClient(true);
		client.setUserId("1");
		client.setId(1);
		when(clientDao.updateClient(client)).thenReturn(client);
		assertEquals(client, clientsServiceImpl.updateClient(client));
	}
	
	@Test
	void updateClient1() {
		Clients client = new Clients();
		String registerToken = "KwI5dY8SX2xnJ-uJ4CZzfFl6ppqAp7yQcJbFyJD0mbIGHMvCV04BedAMei4yQseRwHFfBkPYesECR2njzffyB5QJsZ04MrpK57CldfVhhpGG51wawmKevIMI4w3EuzCnFgrkR2juKvWp3Z3BK1aVvLziEfpF4drkX9s6tulqmxyasNYeG";
		String clientId = "vPVVKzscFYDBZ0uFDg1mJ-2ml";
		client.setRegisterToken(registerToken);
		client.setClientId(clientId);
		client.setConfidentialClient(false);
		client.setClientSecret(null);
		client.setUserId("1");
		client.setId(1);
		when(clientDao.updateClient(client)).thenReturn(client);
		assertEquals(client, clientsServiceImpl.updateClient(client));
	}
	
	@Test
	void updateClientNew() throws ParseException {
		Clients client = new Clients();
		String registerToken = "KwI5dY8SX2xnJ-uJ4CZzfFl6ppqAp7yQcJbFyJD0mbIGHMvCV04BedAMei4yQseRwHFfBkPYesECR2njzffyB5QJsZ04MrpK57CldfVhhpGG51wawmKevIMI4w3EuzCnFgrkR2juKvWp3Z3BK1aVvLziEfpF4drkX9s6tulqmxyasNYeG";
		String clientSecret = "YUE2TmFMWHBtdFh5LUx5ZVhUVHdRd0ZzVXRiTzB3V2Yksdhfkdjfhjkd";
		String clientId = "vPVVKzscFYDBZ0uFDg1mJ-2ml";
		client.setRegisterToken(registerToken);
		client.setClientId(clientId);
		client.setConfidentialClient(true);
		client.setClientSecret(clientSecret);
		client.setUserId("1");
		client.setId(1);
		Mockito.when(currentUserDetails.getUserId()).thenReturn("1234");
		Mockito.when(clientDao.verifyClientWithUser(any(), anyString(), any())).thenReturn(true);
		when(clientDao.updateClient(Mockito.any())).thenReturn(client);

		assertEquals(client, clientsServiceImpl.updateClientNew(client));

	}
	
	@Test
	void testUpdateClientNew() {
		Clients client = new Clients();
		String registerToken = "KwI5dY8SX2xnJ-uJ4CZzfFl6ppqAp7yQcJbFyJD0mbIGHMvCV04BedAMei4yQseRwHFfBkPYesECR2njzffyB5QJsZ04MrpK57CldfVhhpGG51wawmKevIMI4w3EuzCnFgrkR2juKvWp3Z3BK1aVvLziEfpF4drkX9s6tulqmxyasNYeG";
		String clientId = "vPVVKzscFYDBZ0uFDg1mJ-2ml";
		client.setRegisterToken(registerToken);
		client.setClientId(clientId);
		client.setConfidentialClient(true);
		client.setUserId("1");
		client.setId(1);
		Mockito.when(currentUserDetails.getUserId()).thenReturn("1234");
		Mockito.when(clientDao.verifyClientWithUser(any(), anyString(), any())).thenReturn(true);
		when(clientDao.updateClient(Mockito.any())).thenReturn(client);
		assertEquals(client, clientsServiceImpl.updateClientNew(client));
	}
	
	@Test
	void testUpdateClientNew1() {
		Clients client = new Clients();
		String registerToken = "KwI5dY8SX2xnJ-uJ4CZzfFl6ppqAp7yQcJbFyJD0mbIGHMvCV04BedAMei4yQseRwHFfBkPYesECR2njzffyB5QJsZ04MrpK57CldfVhhpGG51wawmKevIMI4w3EuzCnFgrkR2juKvWp3Z3BK1aVvLziEfpF4drkX9s6tulqmxyasNYeG";
		String clientId = "vPVVKzscFYDBZ0uFDg1mJ-2ml";
		client.setRegisterToken(registerToken);
		client.setClientId(clientId);
		client.setConfidentialClient(false);
		client.setUserId("1");
		client.setId(1);
		Mockito.when(currentUserDetails.getUserId()).thenReturn("1234");
		Mockito.when(clientDao.verifyClientWithUser(any(), anyString(), any())).thenReturn(true);
		when(clientDao.updateClient(Mockito.any())).thenReturn(client);
		assertEquals(client, clientsServiceImpl.updateClientNew(client));
	}

	@Test
	void testGetClientByDetails() {
		Clients client = new Clients();
		String registerToken = "KwI5dY8SX2xnJ-uJ4CZzfFl6ppqAp7yQcJbFyJD0mbIGHMvCV04BedAMei4yQseRwHFfBkPYesECR2njzffyB5QJsZ04MrpK57CldfVhhpGG51wawmKevIMI4w3EuzCnFgrkR2juKvWp3Z3BK1aVvLziEfpF4drkX9s6tulqmxyasNYeGjH7u-BH-nDI0BVg1IFTMT6R3NmMA9QiE1fHTOO9xalxxb2tIOf28TPBNvcTr3cCp2SYam-9Bx";
		String clientId = "vPVVKzscFYDBZ0uFDg1mJ-2ml";
		String clientSecret = "YUE2TmFMWHBtdFh5LUx5ZVhUVHdRd0ZzVXRiTzB3V2Y1ZVVJazd3eEc3NnBpY1poNjU=";
		String clientLaunchId = "https://inferno.healthit.gov/inferno/oauth2/static/launch";
		client.setRegisterToken(registerToken);
		client.setClientId(clientId);
		client.setConfidentialClient(true);
		client.setClientSecret(clientSecret);
		client.setLaunchId(clientLaunchId);

		when(clientDao.getClientByDetails(clientId, registerToken)).thenReturn(client);
		assertEquals(client, clientsServiceImpl.getClientByDetails(clientId, registerToken));
	}

	@Test
	void testGetClientByCredentials() {
		Clients client = new Clients();
		String registerToken = "KwI5dY8SX2xnJ-uJ4CZzfFl6ppqAp7yQcJbFyJD0mbIGHMvCV04BedAMei4yQseRwHFfBkPYesECR2njzffyB5QJsZ04MrpK57CldfVhhpGG51wawmKevIMI4w3EuzCnFgrkR2juKvWp3Z3BK1aVvLziEfpF4drkX9s6tulqmxyasNYeGjH7u-BH-nDI0BVg1IFTMT6R3NmMA9QiE1fHTOO9xalxxb2tIOf28TPBNvcTr3cCp2SYam-9Bx";
		String clientSecret = "YUE2TmFMWHBtdFh5LUx5ZVhUVHdRd0ZzVXRiTzB3V2Y1ZVVJazd3eEc3NnBpY1poNjU=";
		String clientId = "BuhZfFFzZPVMV4617Rd9OQsdIPw056";
		String clientLaunchId = "https://inferno.healthit.gov/inferno/oauth2/static/launch";
		client.setRegisterToken(registerToken);
		client.setClientId(clientId);
		client.setConfidentialClient(true);
		client.setClientSecret(clientSecret);
		client.setLaunchId(clientLaunchId);

		when(clientDao.getClientByCredentials(clientId, clientSecret)).thenReturn(client);
		assertEquals(client, clientsServiceImpl.getClientByCredentials(clientId, clientSecret));
	}

	@Test
	void testGetClient() {
		Clients client = new Clients();
		String clientId = "BuhZfFFzZPVMV4617Rd9OQsdIPw056";
		String registerToken = "KwI5dY8SX2xnJ-uJ4CZzfFl6ppqAp7yQcJbFyJD0mbIGHMvCV04BedAMei4yQseRwHFfBkPYesECR2njzffyB5QJsZ04MrpK57CldfVhhpGG51wawmKevIMI4w3EuzCnFgrkR2juKvWp3Z3BK1aVvLziEfpF4drkX9s6tulqmxyasNYeGjH7u-BH-nDI0BVg1IFTMT6R3NmMA9QiE1fHTOO9xalxxb2tIOf28TPBNvcTr3cCp2SYam-9Bx";
		String clientSecret = "YUE2TmFMWHBtdFh5LUx5ZVhUVHdRd0ZzVXRiTzB3V2Y1ZVVJazd3eEc3NnBpY1poNjU=";
		String clientLaunchId = "https://inferno.healthit.gov/inferno/oauth2/static/launch";
		client.setRegisterToken(registerToken);
		client.setClientId(clientId);
		client.setConfidentialClient(true);
		client.setClientSecret(clientSecret);
		when(clientDao.getClient(clientId)).thenReturn(client);
		assertEquals(client, clientsServiceImpl.getClient(clientId));
	}

	@Test
	void testGetClientsByUserId() {
		Clients client = new Clients();
		String registerToken = "KwI5dY8SX2xnJ-uJ4CZzfFl6ppqAp7yQcJbFyJD0mbIGHMvCV04BedAMei4yQseRwHFfBkPYesECR2njzffyB5QJsZ04MrpK57CldfVhhpGG51wawmKevIMI4w3EuzCnFgrkR2juKvWp3Z3BK1aVvLziEfpF4drkX9s6tulqmxyasNYeGjH7u-BH-nDI0BVg1IFTMT6R3NmMA9QiE1fHTOO9xalxxb2tIOf28TPBNvcTr3cCp2SYam-9Bx";
		String clientSecret = "YUE2TmFMWHBtdFh5LUx5ZVhUVHdRd0ZzVXRiTzB3V2Y1ZVVJazd3eEc3NnBpY1poNjU=";
		String clientId = "BuhZfFFzZPVMV4617Rd9OQsdIPw056";
		String clientLaunchId = "https://inferno.healthit.gov/inferno/oauth2/static/launch";
		client.setRegisterToken(registerToken);
		client.setClientId(clientId);
		client.setConfidentialClient(true);
		client.setClientSecret(clientSecret);
		client.setLaunchId(clientLaunchId);
		client.setUserId("4");

		List<Clients> clientsList = new ArrayList<>();
		clientsList.add(client);
		Users dafUserRegister = new Users();
		dafUserRegister.setFirstName("Navaneetha");
		dafUserRegister.setUser_email("navaneetha23@gmail.com");
		dafUserRegister.setUser_id("2");
		when(userRegistrationService.getUserById(Mockito.any())).thenReturn(dafUserRegister);
		when(clientDao.getClientsByUserId("4")).thenReturn(clientsList);
		assertEquals(clientsList, clientsServiceImpl.getClientsByUserId("4"));
	}

	@Test
	void testRegisterBackendClient() {
		HashMap<String, String> params = new HashMap<>();
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setServerPort(8082);
		params.put("jku", "https://dev.interopx.com/docs/RS384PublicKey.json");
		params.put("algorithmUsed", "RS384");
		params.put("status", "PENDING");
		params.put("backEndClient", "true");
		params.put("name", "arcadia auth server");
		params.put("org_name", "xyram software solutions");
		params.put("scopeType", "all");
		params.put("customerId", "cradmo6");
		params.put("centerId", "58");
		params.put("userId", "1");
		params.put("issuer", "http://localhost:8080/ix-auth-server");
		params.put("scope",
				"user/Patient.read,user/AllergyIntolerance.read,user/CarePlan.read,user/CareTeam.read,user/Condition.read,user/Device.read,user/DiagnosticReport.read,user/DocumentReference.read,user/Encounter.read,user/Goal.read,user/Immunization.read,user/Location.read,user/MedicationRequest.read,user/Medication.read,user/Observation.read,user/Organization.read,user/Practitioner.read,user/PractitionerRole.read,user/Procedure.read,user/Provenance.read,user/RelatedPerson.read,patient/Medication.read,patient/Location.read,patient/Immunization.read,patiet/Goal.read,patient/Encounter.read,patient/DocumentReference.read,patient/DiagnosticReport.read,patient/Device.read,patient/Condition.read,patient/CareTeam.read,patient/CarePlan.read,patient/AllergyIntolerance.read,patient/Patient.read,patient/*.read,online_access,offline_access,fhirUser,openid,fhir_complete,launch/patient,launch,patient/MedicationRequest.read,patient/Observation.read,patient/Organization.read,patient/Practitioner.read,patient/PractitionerRole.read,patient/Procedure.read,patient/Provenance.read,patient/RelatedPerson.read");
		params.put("tokenEndpoint", "http://localhost:8082/api/backendservice/token");

		Clients clients = new Clients();
//		clients.setAlgorithmUsed("RS384");
		clients.setApprovedStatus(ApprovedStatus.PENDING);
		clients.setJku("https://dev.interopx.com/docs/RS384PublicKey.json");
		clients.setIsBackendClient(true);
		clients.setName("arcadia auth server");
		clients.setOrgName("xyram software solutions");
		clients.setScopeType("all");
		clients.setCustomerId("cradmo6");
		clients.setCenterId("58");
		clients.setUserId("1");
		clients.setIssuer("http://localhost:8080/ix-auth-server");
		clients.setScope(
				"user/Patient.read,user/AllergyIntolerance.read,user/CarePlan.read,user/CareTeam.read,user/Condition.read,user/Device.read,user/DiagnosticReport.read,user/DocumentReference.read,user/Encounter.read,user/Goal.read,user/Immunization.read,user/Location.read,user/MedicationRequest.read,user/Medication.read,user/Observation.read,user/Organization.read,user/Practitioner.read,user/PractitionerRole.read,user/Procedure.read,user/Provenance.read,user/RelatedPerson.read,patient/Medication.read,patient/Location.read,patient/Immunization.read,patiet/Goal.read,patient/Encounter.read,patient/DocumentReference.read,patient/DiagnosticReport.read,patient/Device.read,patient/Condition.read,patient/CareTeam.read,patient/CarePlan.read,patient/AllergyIntolerance.read,patient/Patient.read,patient/*.read,online_access,offline_access,fhirUser,openid,fhir_complete,launch/patient,launch,patient/MedicationRequest.read,patient/Observation.read,patient/Organization.read,patient/Practitioner.read,patient/PractitionerRole.read,patient/Procedure.read,patient/Provenance.read,patient/RelatedPerson.read");
		clients.setTokenEndPoint("http://localhost:8082/api/backendservice/token");

		when(clientDao.registerClient(Mockito.any())).thenReturn(clients);
		assertEquals(clients,clientsServiceImpl.registerBackendClient(params, request));

	}
	
	@Test
	void testGetClientsByUserIdAndStatus() {
		Clients client = new Clients();
		String registerToken = "KwI5dY8SX2xnJ-uJ4CZzfFl6ppqAp7yQcJbFyJD0mbIGHMvCV04BedAMei4yQseRwHFfBkPYesECR2njzffyB5QJsZ04MrpK57CldfVhhpGG51wawmKevIMI4w3EuzCnFgrkR2juKvWp3Z3BK1aVvLziEfpF4drkX9s6tulqmxyasNYeGjH7u-BH-nDI0BVg1IFTMT6R3NmMA9QiE1fHTOO9xalxxb2tIOf28TPBNvcTr3cCp2SYam-9Bx";
		String clientSecret = "YUE2TmFMWHBtdFh5LUx5ZVhUVHdRd0ZzVXRiTzB3V2Y1ZVVJazd3eEc3NnBpY1poNjU=";
		String clientId = "BuhZfFFzZPVMV4617Rd9OQsdIPw056";
		String clientLaunchId = "https://inferno.healthit.gov/inferno/oauth2/static/launch";

		client.setUserId("4");
		client.setRegisterToken(registerToken);
		client.setClientId(clientId);
		client.setConfidentialClient(true);
		client.setClientSecret(clientSecret);
		client.setLaunchId(clientLaunchId);
		client.setApprovedStatus(ApprovedStatus.PENDING);
		List<Clients> clientsList = new ArrayList<>();
		clientsList.add(client);
		Users dafUserRegister = new Users();
		dafUserRegister.setFirstName("Navaneetha");
		dafUserRegister.setUser_email("navaneetha23@gmail.com");
		dafUserRegister.setUser_id("2");
		when(userRegistrationService.getUserById(Mockito.any())).thenReturn(dafUserRegister);
		when(clientDao.getClientsByUserIdAndStatus("4", ApprovedStatus.APPROVED)).thenReturn(clientsList);
		assertEquals(clientsList, clientsServiceImpl.getClientsByUserIdAndStatus("4", ApprovedStatus.APPROVED));
	}

	@Test
	void testGetClientsByApprovedStatus() {
		Clients client = new Clients();
		String registerToken = "KwI5dY8SX2xnJ-uJ4CZzfFl6ppqAp7yQcJbFyJD0mbIGHMvCV04BedAMei4yQseRwHFfBkPYesECR2njzffyB5QJsZ04MrpK57CldfVhhpGG51wawmKevIMI4w3EuzCnFgrkR2juKvWp3Z3BK1aVvLziEfpF4drkX9s6tulqmxyasNYeGjH7u-BH-nDI0BVg1IFTMT6R3NmMA9QiE1fHTOO9xalxxb2tIOf28TPBNvcTr3cCp2SYam-9Bx";
		String clientSecret = "YUE2TmFMWHBtdFh5LUx5ZVhUVHdRd0ZzVXRiTzB3V2Y1ZVVJazd3eEc3NnBpY1poNjU=";
		String clientId = "BuhZfFFzZPVMV4617Rd9OQsdIPw056";
		String clientLaunchId = "https://inferno.healthit.gov/inferno/oauth2/static/launch";

		client.setUserId("4");
		client.setRegisterToken(registerToken);
		client.setClientId(clientId);
		client.setConfidentialClient(true);
		client.setClientSecret(clientSecret);
		client.setLaunchId(clientLaunchId);
		client.setApprovedStatus(ApprovedStatus.PENDING);
		List<Clients> clientsList = new ArrayList<>();
		clientsList.add(client);
		Users dafUserRegister = new Users();
		dafUserRegister.setFirstName("Navaneetha");
		dafUserRegister.setUser_email("navaneetha23@gmail.com");
		dafUserRegister.setUser_id("2");
		when(userRegistrationService.getUserById(Mockito.any())).thenReturn(dafUserRegister);
		when(clientDao.getClientsByApprovedStatus(Mockito.any())).thenReturn(clientsList);
		assertEquals(clientsList, clientsServiceImpl.getClientsByApprovedStatus(ApprovedStatus.APPROVED));
	}

	@Test
	void testGetAllClients() {
		Clients client = new Clients();
		String registerToken = "KwI5dY8SX2xnJ-uJ4CZzfFl6ppqAp7yQcJbFyJD0mbIGHMvCV04BedAMei4yQseRwHFfBkPYesECR2njzffyB5QJsZ04MrpK57CldfVhhpGG51wawmKevIMI4w3EuzCnFgrkR2juKvWp3Z3BK1aVvLziEfpF4drkX9s6tulqmxyasNYeGjH7u-BH-nDI0BVg1IFTMT6R3NmMA9QiE1fHTOO9xalxxb2tIOf28TPBNvcTr3cCp2SYam-9Bx";
		String clientSecret = "YUE2TmFMWHBtdFh5LUx5ZVhUVHdRd0ZzVXRiTzB3V2Y1ZVVJazd3eEc3NnBpY1poNjU=";
		String clientId = "BuhZfFFzZPVMV4617Rd9OQsdIPw056";
		String clientLaunchId = "https://inferno.healthit.gov/inferno/oauth2/static/launch";

		client.setUserId("4");
		client.setRegisterToken(registerToken);
		client.setUserName("adminuser@gmail.com");
		client.setUserEmail("adminuser@gmail.com");
		client.setClientId(clientId);
		client.setConfidentialClient(true);
		client.setClientSecret(clientSecret);
		client.setLaunchId(clientLaunchId);
		client.setApprovedStatus(ApprovedStatus.PENDING);
		List<Clients> clientsList = new ArrayList<>();
		clientsList.add(client);

		Users dafUserRegister = new Users();
		dafUserRegister.setFirstName("Navaneetha");
		dafUserRegister.setUser_email("navaneetha23@gmail.com");
		dafUserRegister.setUser_id("2");
		when(userRegistrationService.getUserById(Mockito.any())).thenReturn(dafUserRegister);
		when(clientDao.getAllClients()).thenReturn(clientsList);

		assertEquals(clientsList, clientsServiceImpl.getAllClients());

	}

	@Test
	void testSetUserDetails() {
	}

	@Test
	void testGetClientById() {
		Clients client = new Clients();
		String registerToken = "KwI5dY8SX2xnJ-uJ4CZzfFl6ppqAp7yQcJbFyJD0mbIGHMvCV04BedAMei4yQseRwHFfBkPYesECR2njzffyB5QJsZ04MrpK57CldfVhhpGG51wawmKevIMI4w3EuzCnFgrkR2juKvWp3Z3BK1aVvLziEfpF4drkX9s6tulqmxyasNYeGjH7u-BH-nDI0BVg1IFTMT6R3NmMA9QiE1fHTOO9xalxxb2tIOf28TPBNvcTr3cCp2SYam-9Bx";
		String clientSecret = "YUE2TmFMWHBtdFh5LUx5ZVhUVHdRd0ZzVXRiTzB3V2Y1ZVVJazd3eEc3NnBpY1poNjU=";
		String clientId = "BuhZfFFzZPVMV4617Rd9OQsdIPw056";

		client.setRegisterToken(registerToken);
		client.setClientId(clientId);
		client.setConfidentialClient(true);
		client.setClientSecret(clientSecret);
		client.setApprovedStatus(ApprovedStatus.PENDING);

		when(clientDao.getClientById(1)).thenReturn(client);
		assertEquals(client, clientsServiceImpl.getClientById(1));
	}

}
