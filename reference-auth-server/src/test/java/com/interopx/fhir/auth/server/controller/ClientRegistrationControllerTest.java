package com.interopx.fhir.auth.server.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import com.interopx.fhir.auth.server.dao.ClientsDao;
import com.interopx.fhir.auth.server.dao.impl.ClientsDaoImpl;
import com.interopx.fhir.auth.server.model.Clients;
import com.interopx.fhir.auth.server.model.CurrentUserDetails;
import com.interopx.fhir.auth.server.service.ClientsService;
import com.interopx.fhir.auth.server.util.AuthUtil.ApprovedStatus;

@SpringBootTest
class ClientRegistrationControllerTest {

	@InjectMocks
	ClientRegistrationController clientRegistrationController;

	@Mock
	ClientsService clientsService;

	@Mock
	ClientsDaoImpl clientsDaoImpl;

	@Mock
	ClientsDao clientDao;
	
	@Mock
	CurrentUserDetails currentUserDetails;
	

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.initMocks(this);
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
		return client;
	}

	static List<Clients> listOfClientDetails() {
		Clients client = new Clients();
		client.setId(1);
		HttpServletRequest request = mock(HttpServletRequest.class);
		client.setUserId("1");
		client.setRegisterToken("token");
		client.setClientId("1");
		client.setClientSecret("secret");
		client.setApprovedStatus(ApprovedStatus.APPROVED);

		List<Clients> listOfClients = new ArrayList<>();
		listOfClients.add(0, client);
		return listOfClients;

	}

	static HashMap<String, String> backEndRegDetails() {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("jku", "Jku");
		params.put("algorithmUsed", "algorithmUsed");
		params.put("jku", "jku");
		params.put("name", "Arcadia");
		params.put("org_name", "Arcadia");
		params.put("scopeType", "RS");
		params.put("centerId", "123");
		return params;
	}

	@Test
	void testRegisterClient() {
		Mockito.when(currentUserDetails.getUserId()).thenReturn("1234");
		Clients client = clientDetails();
		HttpServletRequest request = mock(HttpServletRequest.class);
		
		Mockito.lenient().doReturn(client).when(clientsService).registerClient(client);

		assertEquals(client, clientRegistrationController.registerClient(client, request));
	}

	@Test
	void testUpdateClient() {
		Clients client = clientDetails();
		HttpServletRequest request = mock(HttpServletRequest.class);
		client.setClientSecret("123");
		client.setClientSecret(client.getClientSecret());
		Mockito.lenient().doReturn(client).when(clientsService).updateClient(client);
		when(clientsService.updateClientNew(client)).thenReturn(client);

		assertEquals(clientRegistrationController.updateClient(client).getClientSecret(), "123");
	}

	@Test
	void testGetClientByDetails() throws IOException {
		Clients client1 = clientDetails();
		HttpServletResponse response = mock(HttpServletResponse.class);

		Mockito.lenient().doReturn(client1).when(clientsService).getClientByDetails(clientDetails().getCenterId(),
				clientDetails().getRegisterToken());

		assertEquals(client1, clientRegistrationController.getClientByDetails(clientDetails().getCenterId(),
				clientDetails().getRegisterToken(), response));
	}
	
	@Test
	void testGetClientByDetails1() throws IOException {
		Clients client1 = null;
		HttpServletResponse response = mock(HttpServletResponse.class);

		Mockito.lenient().doReturn(client1).when(clientsService).getClientByDetails(clientDetails().getCenterId(),
				clientDetails().getRegisterToken());

		assertEquals(client1, clientRegistrationController.getClientByDetails(clientDetails().getCenterId(),
				clientDetails().getRegisterToken(), response));
	}

	@Test
	void testGetClientByCredentials() {
		Clients client = clientDetails();
		Mockito.lenient().doReturn(client).when(clientsService).getClientByCredentials(client.getClientId(),
				client.getClientSecret());
		assertEquals(client,
				clientRegistrationController.getClientByCredentials(client.getClientId(), client.getClientSecret()));
	}

	@Test
	void testGetClientsByUserId() {
		List<Clients> listOfClients = listOfClientDetails();
		Mockito.when(currentUserDetails.getUserId()).thenReturn("1");
		Mockito.lenient().doReturn(listOfClients).when(clientsService).getClientsByUserIdAndStatus(
				listOfClients.get(0).getUserId(), listOfClients.get(0).getApprovedStatus());

		assertEquals(listOfClients.size(), clientRegistrationController
				.getClientsByUserId(listOfClients.get(0).getUserId(), listOfClients.get(0).getApprovedStatus()).size());
	}
	
	@Test
	void testGetClientsByUserId1() {
		List<Clients> listOfClients = listOfClientDetails();
		Mockito.when(currentUserDetails.getUserId()).thenReturn("1");
		Mockito.lenient().doReturn(listOfClients).when(clientsService).getClientsByUserIdAndStatus(
				listOfClients.get(0).getUserId(), listOfClients.get(0).getApprovedStatus());
		
		List expected = new ArrayList();

		assertEquals(expected, clientRegistrationController
				.getClientsByUserId(listOfClients.get(0).getUserId(), null));
	}

	@Test
	void testGetClientsByApprovedStatus() {
		
//		Map map  = new HashMap();
//		map.put("roleName","Admin");
		List<String> roles = new ArrayList();
		roles.add("Admin");
		Mockito.when(currentUserDetails.getUserRole()).thenReturn(roles);
		List<Clients> listOfClients = listOfClientDetails();
		Mockito.lenient().doReturn(listOfClients).when(clientsService)
				.getClientsByApprovedStatus(listOfClients.get(0).getApprovedStatus());

		assertEquals(listOfClients.size(), (clientRegistrationController)
				.getClientsByApprovedStatus(listOfClients.get(0).getApprovedStatus()).size());

	}

	@Test
	void testGetAllClients() {
		
//		Map map  = new HashMap();
//		map.put("roleName","Admin");
		List<String> roles = new ArrayList();
		roles.add("Admin");
		Mockito.when(currentUserDetails.getUserRole()).thenReturn(roles);
		List<Clients> listOfClients = listOfClientDetails();
		Mockito.lenient().doReturn(listOfClients).when(clientsService).getAllClients();

		assertEquals(listOfClients.size(), (clientRegistrationController).getAllClients().size());

	}

	@Test
	void testRegisterBackendClient() {

		HashMap<String, String> details = backEndRegDetails();
		Clients client = new Clients();
		client.setJku(details.get("jku"));
//		client.setAlgorithmUsed(details.get("algorithmUsed"));

		client.setName(details.get("name"));
		client.setOrgName(details.get("org_name"));
		client.setScopeType(details.get("scopeType"));
		client.setCenterId(details.get("centerId"));

		HttpServletRequest request = mock(HttpServletRequest.class);

		Mockito.lenient().doReturn(client).when(clientsService).registerBackendClient(details, request);

		assertEquals(details.get("org_name"),
				clientRegistrationController.registerBackendClient(details, request).getOrgName());
	}

	@Test
	void testUpdateBackendClient() {
		HashMap<String, String> details = backEndRegDetails();
		Clients client = new Clients();
		client.setJku(details.get("jku"));
//		client.setAlgorithmUsed(details.get("algorithmUsed"));

		client.setName(details.get("name"));
		client.setOrgName(details.get("org_name"));
		client.setScopeType(details.get("scopeType"));
		client.setCenterId(details.get("centerId"));

		HttpServletRequest request = mock(HttpServletRequest.class);

		Mockito.lenient().doReturn(client).when(clientsService).updateBackendClient(client, request);

		assertEquals(details.get("org_name"),
				clientRegistrationController.updateBackendClient(client, request).getOrgName());

	}

	@Test
	void testGetModel() {
		Clients client = new Clients();

		assertThat(client).usingRecursiveComparison().isEqualTo(clientRegistrationController.getModel());
	}

	@Test
	void testAddPatientIdToClient() {
		Clients client = clientDetails();
		when(clientsService.getClientById(Mockito.anyInt())).thenReturn(client);
		client.setPatientId("12");
		ResponseEntity<?> response = new ResponseEntity<Clients>(client, HttpStatus.OK);

		Mockito.lenient().doReturn(client).when(clientsService).updateClient(client);

		assertEquals(response,
				clientRegistrationController.addPatientIdToClient(client.getId(), client.getPatientId()));
	}
	
	@Test
	void testAddPatientIdToClient1() {
		Clients client = clientDetails();
				
		ResponseEntity<?> response = new ResponseEntity<String>("Failed to update patient id", HttpStatus.BAD_REQUEST);
		
		when(clientsService.getClientById(Mockito.any())).thenReturn(null);

		assertEquals(response,
				clientRegistrationController.addPatientIdToClient(client.getId(), client.getPatientId()));
	}

	@Test
	void testUpdateApproveStatus() {
		Clients client = clientDetails();
//		Map map  = new HashMap();
//		map.put("roleName","Admin");
		List<String> roles = new ArrayList();
		roles.add("Admin");
		Mockito.when(currentUserDetails.getUserRole()).thenReturn(roles);
		when(clientsService.getClientById(Mockito.anyInt())).thenReturn(client);
		when(clientsService.updateClient(Mockito.any())).thenReturn(client);
		ResponseEntity<?> response = new ResponseEntity<Clients>(client, HttpStatus.OK);
		Mockito.lenient().doReturn(client).when(clientsService).updateClient(client);

		assertEquals(response, clientRegistrationController.updateApproveStatus(client));

	}
	
	@Test
	void testUpdateApproveStatus1() {
		Clients client = clientDetails();
		Map map  = new HashMap();
		map.put("roleName","Admin");
		List<String> roles = new ArrayList();
		roles.add("Admin");
		Mockito.when(currentUserDetails.getUserRole()).thenReturn(roles);
		when(clientsService.getClientById(Mockito.anyInt())).thenReturn(null);
		ResponseEntity<?> response = new ResponseEntity<String>("Failed to update status", HttpStatus.BAD_REQUEST);

		assertEquals(response, clientRegistrationController.updateApproveStatus(client));

	}
	
}