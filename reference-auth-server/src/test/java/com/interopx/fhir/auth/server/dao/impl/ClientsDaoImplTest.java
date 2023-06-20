package com.interopx.fhir.auth.server.dao.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.NativeQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;

import com.interopx.fhir.auth.server.configuration.HibernateConfiguration;
import com.interopx.fhir.auth.server.model.Clients;
import com.interopx.fhir.auth.server.model.Users;
import com.interopx.fhir.auth.server.util.AuthUtil.ApprovedStatus;

@SpringBootTest
class ClientsDaoImplTest {

	@InjectMocks
	ClientsDaoImpl clientsDaoImpl;

	@Mock
	SessionFactory sessionFactory;

	@MockBean
	Criteria criteria;

	@SpyBean
	HibernateConfiguration hibernateConfiguration;

	@Mock
	Session session;

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
	void testRegisterClient() {

	}

	@Test
	void testUpdateClient() {
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

		assertEquals(client, clientsDaoImpl.updateClient(client));

	}

	@Test
	void testUpdateClientException() {
		Clients client = mock(Clients.class);
		doThrow(RuntimeException.class).when(client).getLaunchId();
		client.setLaunchId(null);
		assertNotNull(clientsDaoImpl.updateClient(client));

	}

	@Test
	void testUpdateClient1() {
		Clients client = mock(Clients.class);
		String registerToken = "KwI5dY8SX2xnJ-uJ4CZzfFl6ppqAp7yQcJbFyJD0mbIGHMvCV04BedAMei4yQseRwHFfBkPYesECR2njzffyB5QJsZ04MrpK57CldfVhhpGG51wawmKevIMI4w3EuzCnFgrkR2juKvWp3Z3BK1aVvLziEfpF4drkX9s6tulqmxyasNYeGjH7u-BH-nDI0BVg1IFTMT6R3NmMA9QiE1fHTOO9xalxxb2tIOf28TPBNvcTr3cCp2SYam-9Bx";
		String clientSecret = "YUE2TmFMWHBtdFh5LUx5ZVhUVHdRd0ZzVXRiTzB3V2Y1ZVVJazd3eEc3NnBpY1poNjU=";
		String clientId = "BuhZfFFzZPVMV4617Rd9OQsdIPw056";
		client.setRegisterToken(registerToken);
		client.setClientId(clientId);
		client.setConfidentialClient(true);
		client.setClientSecret(clientSecret);
		client.setApprovedStatus(ApprovedStatus.PENDING);
		client.setApprovedStatus(ApprovedStatus.APPROVED);
		client.setDynamicClient(true);

		assertNotNull(clientsDaoImpl.updateClient(client));

	}

	@Test
	void testGetClientByDetails() {

	}

	@Test
	void testGetClientByDetailsByException() {
		Clients client = mock(Clients.class);
		doThrow(RuntimeException.class).when(client).getClientId();
		client.setClientId(null);
		assertNull(clientsDaoImpl.getClientByDetails("Ly73NWOpK2vIx1My6egbQMSgc", "4af2e5541bde53f8a4dcf87a0c38b937"));

	}

	@Test
	void testGetClientByCredentials() {

	}

	@Test
	void testGetClientByCredentialsByException() {
		Clients client = mock(Clients.class);
		doThrow(RuntimeException.class).when(client).getClientSecret();
		client.setClientSecret(null);
		assertNull(clientsDaoImpl.getClientByCredentials("Ly73NWOpK2vIx1My6egbQMSgc",
				"YUE2TmFMWHBtdFh5LUx5ZVhUVHdRd0ZzVXRiTzB3V2Y1ZVVJazd3eEc3NnBpY1poNjU="));

	}

	@Test
	void testGetClient() {

	}

	@Test
	void testGetClientByException() {
		Clients client = mock(Clients.class);
		doThrow(RuntimeException.class).when(client).getClientId();
		client.setClientId(null);
		assertNull(clientsDaoImpl.getClient("Ly73NWOpK2vIx1My6egbQMSgc"));
	}

	@Test
	void testGetClientsByUserId() {

	}

	@Test
	void testGetClientsByUserIdByException() {
		Clients client = mock(Clients.class);
		doThrow(RuntimeException.class).when(client).getUserId();
		client.setUserId(null);
		;
		assertNull(clientsDaoImpl.getClientsByUserId("28"));
	}

	@Test
	void testGetClientsByUserIdAndStatus() {

	}

	@Test
	void testGetClientsByUserIdAndStatusByException() {
		Clients client = mock(Clients.class);
		client.setUserId(null);
		List<Clients> clientsList = new ArrayList<>();
		clientsList.add(client);
		doThrow(RuntimeException.class).when(client).getUserId();

		assertNull(clientsDaoImpl.getClientsByUserIdAndStatus("28", ApprovedStatus.APPROVED));
	}

	@Test
	void testGetClientsByApprovedStatus() {

	}

	@Test
	void testGetClientsByApprovedStatusByException() {
		Clients client = mock(Clients.class);
		client.setApprovedStatus(null);
		List<Clients> clientsList = new ArrayList<>();
		clientsList.add(client);
		doThrow(RuntimeException.class).when(client).getApprovedStatus();

		assertNull(clientsDaoImpl.getClientsByApprovedStatus(ApprovedStatus.APPROVED));
	}

	@Test
	void testGetAllClients() {
		Clients client = mock(Clients.class);
		String registerToken = "KwI5dY8SX2xnJ-uJ4CZzfFl6ppqAp7yQcJbFyJD0mbIGHMvCV04BedAMei4yQseRwHFfBkPYesECR2njzffyB5QJsZ04MrpK57CldfVhhpGG51wawmKevIMI4w3EuzCnFgrkR2juKvWp3Z3BK1aVvLziEfpF4drkX9s6tulqmxyasNYeGjH7u-BH-nDI0BVg1IFTMT6R3NmMA9QiE1fHTOO9xalxxb2tIOf28TPBNvcTr3cCp2SYam-9Bx";
		String clientSecret = "YUE2TmFMWHBtdFh5LUx5ZVhUVHdRd0ZzVXRiTzB3V2Y1ZVVJazd3eEc3NnBpY1poNjU=";
		String clientId = "BuhZfFFzZPVMV4617Rd9OQsdIPw056";
		client.setRegisterToken(registerToken);
		client.setClientId(clientId);
		client.setConfidentialClient(true);
		client.setClientSecret(clientSecret);
		client.setApprovedStatus(ApprovedStatus.PENDING);
		client.setApprovedStatus(ApprovedStatus.APPROVED);
		client.setDynamicClient(true);
		List<Clients> clientsList = new ArrayList<>();
		clientsList.add(client);

		when(session.createCriteria(Clients.class)).thenReturn(criteria);
		when(criteria.list()).thenReturn(clientsList);

		assertEquals(clientsList, clientsDaoImpl.getAllClients());
	}

	@Test
	void testGetAllClientsByException() {
		Clients client = mock(Clients.class);
		client.setClientId(null);
		List clientsList = new ArrayList();
		doThrow(RuntimeException.class).when(client).getClientId();

		assertEquals(clientsList, clientsDaoImpl.getAllClients());
	}

	@Test
	void testGetClientById() {

	}

	@Test
	void testGetClientByIdByException() {
		Clients client = mock(Clients.class);
		doThrow(RuntimeException.class).when(client).getId();
		client.setId(0);
		assertNull(clientsDaoImpl.getClientById(12));
	}

	@Test
	void testVerifyClientWithUser() {
		Clients client = mock(Clients.class);
		String clientId = "BuhZfFFzZPVMV4617Rd9OQsdIPw056";
		String id = "12";
		Integer userId = 28;
		client.setRegisterToken("KwI5dY8SX2xnJ-uJ4CZzfFl6ppqAp7yQcJbFyJD0mbIGHMvCV04BedAMei4yQseRwHFfBkPYesECR2njzffyB5QJsZ04MrpK57CldfVhhpGG51wawmKevIMI4w3EuzCnFgrkR2juKvWp3Z3BK1aVvLziEfpF4drkX9s6tulqmxyasNYeGjH7u-BH-nDI0BVg1IFTMT6R3NmMA9QiE1fHTOO9xalxxb2tIOf28TPBNvcTr3cCp2SYam-9Bx");
		client.setClientId(clientId);
		client.setConfidentialClient(true);
		client.setClientSecret("YUE2TmFMWHBtdFh5LUx5ZVhUVHdRd0ZzVXRiTzB3V2Y1ZVVJazd3eEc3NnBpY1poNjU=");
		client.setApprovedStatus(ApprovedStatus.PENDING);
		client.setApprovedStatus(ApprovedStatus.APPROVED);
		client.setDynamicClient(true);
		List clientsList = new ArrayList<>();
		clientsList.add(client);
		
		NativeQuery query = mock(NativeQuery.class);
		
		when(session.createNativeQuery(Mockito.anyString())).thenReturn(query);
		
		assertNotNull(clientsDaoImpl.verifyClientWithUser(null, clientId, null));
		
	}

	@Test
	void testVerifyClientWithUserByException() {
		Clients client = mock(Clients.class);
		doThrow(RuntimeException.class).when(client).getId();
		client.setId(0);
		assertEquals(false, clientsDaoImpl.verifyClientWithUser(12, "Ly73NWOpK2vIx1My6egbQMSgc", "28"));
	}

}
