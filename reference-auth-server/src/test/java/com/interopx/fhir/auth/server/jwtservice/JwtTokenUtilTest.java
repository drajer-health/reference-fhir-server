package com.interopx.fhir.auth.server.jwtservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.mail.internet.ParseException;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import io.jsonwebtoken.Jwts;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
@DirtiesContext
class JwtTokenUtilTest {

	@Autowired
	JwtTokenUtil jwtTokenUtil;

	@Value("${jwt.loginsecret}")
	private byte[] loginTokenSecret;

	@Test
	void generateJwtToken() {

		UserDetails user = mock(UserDetails.class);
		when(user.getUsername()).thenReturn("admin");
		when(user.getPassword()).thenReturn("test123");

		assertNotNull(jwtTokenUtil.generateJwtToken(user));
	}

	@Test
	void generateTokenForLogin() {
		String username = "xyram";
		assertNotNull(jwtTokenUtil.generateTokenForLogin(username));
	}

//	@Test
	void getUsernameFromToken() throws ParseException {
		String token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTY2Mzc1ODIyMCwiZXhwIjoxNjYzNzU4MjIwfQ.lukkxxiJ220O98RLwUejtnvo6kJOR4dkjmPtjvsVRCQO5ve2QANIyG4A_kVbBwP4xxcOuyE73bcAv1sG5hqt0g";

		jwtTokenUtil.getUsernameFromToken(token);
	}

//	@Test
	void getExpirationDateFromToken() {
		String token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTY2Mzc1ODIyMCwiZXhwIjoxNjYzNzU4MjIwfQ.lukkxxiJ220O98RLwUejtnvo6kJOR4dkjmPtjvsVRCQO5ve2QANIyG4A_kVbBwP4xxcOuyE73bcAv1sG5hqt0g";

		jwtTokenUtil.getExpirationDateFromToken(token);
	}
	
	@Test
	void generateToken() {
		UserDetails userDetails = mock(UserDetails.class);
		assertNotNull(jwtTokenUtil.generateToken(userDetails));
	}
	@Test
	void validateloginJwtToken() {
		
		String token="gfh23378 b9neygfh";
		jwtTokenUtil.validateloginJwtToken(token);
		
		String token1=null;
		jwtTokenUtil.validateloginJwtToken(token1);
	}
	
}
