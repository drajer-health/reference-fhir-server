package com.interopx.fhir.auth.server.exception;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TokenRefreshExceptionTest {

	@Test
	void testTokenRefreshException() {
		
		String str1="value";
		String str2="value222";
		TokenRefreshException tokenRefreshException=new TokenRefreshException(str1,str2);
	}

}
