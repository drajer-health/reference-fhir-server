package com.interopx.fhir.auth.server.jwtservice;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.interopx.fhir.auth.server.dao.UsersDao;
import com.interopx.fhir.auth.server.model.Users;

@SpringBootTest
class JwtUserDetailsServiceTest {

	@InjectMocks
	JwtUserDetailsService jwtUserDetailsService;
	@Mock
	UsersDao userDao;

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

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	void testLoadUserByUsername() {
		Users user = userDetails();
		when(userDao.getUserByName(user.getUser_name())).thenReturn(user);
		UserDetails userDetails = new org.springframework.security.core.userdetails.User(user.getUser_name(),
				user.getUser_password(), new ArrayList<>());

		assertEquals(userDetails, jwtUserDetailsService.loadUserByUsername(user.getUser_name()));
	}

	@Test
	void testLoadUserByUsernameException() {
		Users user = null;
		String username = "abd";
		when(userDao.getUserByName(username)).thenReturn(user);
		assertThrows(UsernameNotFoundException.class, () -> {
			jwtUserDetailsService.loadUserByUsername(username);
		});
	}

}
