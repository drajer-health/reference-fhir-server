package com.interopx.fhir.auth.server.jwtservice;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import com.interopx.fhir.auth.server.model.Clients;
import com.interopx.fhir.auth.server.service.ClientsService;
import com.interopx.fhir.auth.server.service.UsersService;
import com.interopx.fhir.auth.server.service.impl.ClientsServiceImpl;
import com.nimbusds.jose.jwk.JWKSet;

@RunWith(SpringRunner.class)
@SpringBootTest
class JWKSCacheTest {

	@InjectMocks
	JWKSCache JWKsCache;

	@Mock
	RestTemplate resttemplate;

	@Mock
	ClientsServiceImpl clientsServiceImpl;

	@Mock
	ClientsService clientRegistrationService;

	@Mock
	UsersService userRegistrationService;

	@Mock
	JWKSet jwkset;

	@Test
	void testGetValidator() {

		String str = "4fdstaysge6ry8dgu";
		JWTService JWTservice = JWKsCache.getValidator(str);

	}

	@Test
	void testloadStoredPublicKey() throws Exception {

		Clients client = new Clients();
		client.setContact_mail("ravi@gmail.com");
		client.setCenterId("544");

		Mockito.when(clientRegistrationService.getClient(Mockito.anyString())).thenReturn(client);

		assertThrows(ResponseStatusException.class, () -> {
			JWKsCache.loadPublicKey("123");
		});

	}

	@Test
	void testloadStoredPublicKeyException() throws Exception {
		assertNull(JWKsCache.loadPublicKey("123"));
	}
	
//	@Test
//	void testload() throws Exception {
//		assertNull(JWKsCache.lo);
//	}


}
