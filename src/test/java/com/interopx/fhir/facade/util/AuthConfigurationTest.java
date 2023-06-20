package com.interopx.fhir.facade.util;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;

import com.interopx.fhir.facade.model.AuthConfiguration;
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class AuthConfigurationTest {

	@Test
	void test() {
		AuthConfiguration authConfiguration = spy(AuthConfiguration.class);
		authConfiguration.setAuthConfigId(12);
		when(authConfiguration.getAuthConfigId()).thenReturn(12);
		
		assertEquals(authConfiguration.getAuthConfigId(), 12);
	}

}
