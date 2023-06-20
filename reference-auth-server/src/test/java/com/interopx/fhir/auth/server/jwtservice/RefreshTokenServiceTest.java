package com.interopx.fhir.auth.server.jwtservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.interopx.fhir.auth.server.dao.RefreshTokenDao;
import com.interopx.fhir.auth.server.dao.UsersDao;
import com.interopx.fhir.auth.server.model.RefreshToken;
import com.interopx.fhir.auth.server.model.Users;
import com.interopx.fhir.auth.server.properties.DbConfigProperties;
import com.interopx.fhir.auth.server.properties.HibernateConfigProperties;
import com.interopx.fhir.auth.server.properties.SchedularProperties;

@SpringBootTest
class RefreshTokenServiceTest {

	@InjectMocks
	RefreshTokenService refreshTokenService;

	@Mock
	RefreshTokenDao refreshTokenRepository;
	
	@Autowired
	SchedularProperties schedularProperties; 
	
	@Autowired
	HibernateConfigProperties hibernateConfigProperties;
	
	@Autowired
	DbConfigProperties dbConfigProperties;

	@Mock
	UsersDao userRepository;

	long refreshTokenDurationMs = 31557014167219200L;

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

	static RefreshToken createRefreshToken() {
		Users user = userDetails();
		RefreshToken refreshToken = new RefreshToken();
		refreshToken.setId(1);
		long refreshTokenDurationMs = 31557014167219200L;
		refreshToken.setExpiryDate(Instant.now());
		refreshToken.setToken("1");
		refreshToken.setUser(user);

		return refreshToken;

	}

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	void testFindByToken() {
		RefreshToken refreshToken = createRefreshToken();
		String token = refreshToken.getToken();
		when(refreshTokenRepository.findByToken(token)).thenReturn(refreshToken);
		assertEquals(refreshToken, refreshTokenService.findByToken(token));
	}

	@Test
	void testCreateRefreshToken() {
		RefreshToken refreshToken = createRefreshToken();
		Users user = userDetails();
		String username = user.getUser_name();
		refreshToken.setUser(userRepository.getUserByName(username));
		refreshToken.setExpiryDate(refreshToken.getExpiryDate());
		refreshToken.setToken(refreshToken.getToken());
		String message = null;
		when(refreshTokenRepository.save(refreshToken)).thenReturn(message);
		assertEquals(message, refreshTokenService.createRefreshToken(username));
	}
	@Test
	void testCreateRefreshToken1() {
		RefreshToken refreshToken = new RefreshToken();
		Users user = userDetails();
		String message = null;
		String username = user.getUser_name();
		refreshToken.setUser(userRepository.getUserByName(username));
		refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
	      refreshToken.setToken(UUID.randomUUID().toString());
	      message = refreshTokenRepository.save(refreshToken);
		
		 message = refreshTokenRepository.save(refreshToken);
		when(refreshTokenRepository.save(refreshToken)).thenReturn(message);
		assertEquals(message, refreshTokenService.createRefreshToken(username));
	}

	@Test
	void testVerifyExpiration() {
		RefreshToken refreshToken = createRefreshToken();
		refreshTokenRepository.delete(refreshToken);
		refreshToken.getToken();
		assertEquals(refreshToken, refreshTokenService.verifyExpiration(refreshToken));

	}

	@Test
	void testDeleteByUserId() {
		RefreshToken refreshToken = createRefreshToken();
		Users user = userDetails();
		
		Mockito.when(userRepository.getUserById(Mockito.any())).thenReturn(user);
		int value = 0;

		assertEquals(value, refreshTokenService.deleteByUserId("1"));
	}
	
	@Test
	void schedularProperties() {
		schedularProperties.getTimePeriod();
		schedularProperties.setTimePeriod(null);
	}
	
	@Test
	void hibernateConfigProperties() {
		hibernateConfigProperties.setPackagesScan(null);
		hibernateConfigProperties.getPackagesScan();
		hibernateConfigProperties.getHbm2ddlAuto();
	}
	
	@Test
	void dbConfigProperties() {
		dbConfigProperties.setGraphPath(null);
		dbConfigProperties.getGraphPath();
	}

}
