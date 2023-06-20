package com.interopx.fhir.auth.server.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;

import com.interopx.fhir.auth.server.dao.UsersDao;
import com.interopx.fhir.auth.server.model.Users;

@SpringBootTest
class UsersServiceImplTest {

	@Mock
	UsersDao userDao;

	@InjectMocks
	UsersServiceImpl usersServiceImpl;

	@Test
	void registerUser() {
		Users user = mock(Users.class);
		user.setEmailActivated(true);
		user.setFirstName("Navaneetha");
		user.setLast_updated_ts(new Date());
		user.setLastName("gowda");
		user.setMobile_number("6360014886");
		user.setPatient_id("cradmo6.59662");
		user.setUser_email("navaneetha456@gmail.com");
		user.setUser_id("1");
		user.setUser_name("admin");
		user.setUser_password("test123");
		user.setUserActive(true);
		String message = "User Registerd Successfully";
		when(userDao.register(user)).thenReturn(message);
		assertEquals(message, usersServiceImpl.registerUser(user));
	}

	@Test
	void updateUser() {
		Users user = mock(Users.class);
		user.setEmailActivated(true);
		user.setFirstName("Nithya");
		user.setLast_updated_ts(new Date());
		user.setLastName("gowda");
		user.setMobile_number("6360014890");
		user.setPatient_id("cradmo6.59662");
		user.setUser_email("navaneetha456@gmail.com");
		user.setUser_id("1");
		user.setUser_name("admin");
		user.setUser_password("test623");
		user.setUserActive(true);
		String message = "User details updated";
		when(userDao.updateUser(user)).thenReturn(message);
		assertEquals(message, usersServiceImpl.updateUser(user));
	}

	@Test
	void getUserById() {
		Users user = mock(Users.class);
		user.setEmailActivated(true);
		user.setFirstName("Nithya");
		user.setLast_updated_ts(new Date());
		user.setLastName("gowda");
		user.setMobile_number("6360014890");
		user.setPatient_id("cradmo6.59662");
		user.setUser_email("navaneetha456@gmail.com");
		user.setUser_id("1");
		user.setUser_name("admin");
		user.setUser_password("test623");
		user.setUserActive(true);

		when(userDao.getUserById("1")).thenReturn(user);
		assertEquals(user, usersServiceImpl.getUserById("1"));
	}

	@Test
	void loadUserByName() {
		Users user = mock(Users.class);
		String userName = "admin";
		user.setEmailActivated(true);
		user.setFirstName("Nithya");
		user.setLast_updated_ts(new Date());
		user.setLastName("gowda");
		user.setMobile_number("6360014890");
		user.setPatient_id("cradmo6.59662");
		user.setUser_email("navaneetha456@gmail.com");
		user.setUser_id("1");
		user.setUser_name(userName);
		user.setUser_password("test623");
		user.setUserActive(true);

		when(userDao.getUserByName(userName)).thenReturn(user);
		assertEquals(user, usersServiceImpl.loadUserByName(userName));
	}

	@Test
	void getPropertyValue() {
		Users user = mock(Users.class);
		String key = "MukBCvvmUMx6FDsEVKtFrJTmcObNOr";
		user.setEmailActivated(true);
		user.setFirstName("Nithya");
		user.setLast_updated_ts(new Date());
		user.setLastName("gowda");
		user.setMobile_number("6360014890");
		user.setPatient_id("cradmo6.59662");
		user.setUser_email("navaneetha456@gmail.com");
		user.setUser_id("1");
		user.setUser_name("admin");
		user.setUser_password("test623");
		user.setUserActive(true);
		when(userDao.getPropertyValue(Mockito.anyString())).thenReturn(key);
		assertEquals(key, usersServiceImpl.getPropertyValue(key));
	}

	@Test
	void verifyToken() {
		Users user = mock(Users.class);
		String token = "MukBCvvmUMx6FDsEVKtFrJTmcObNOr";
		String userEmail = "navaneetha567@gmail.com";

		List<String> userRole = new ArrayList<>();
		userRole.add("3");
		user.setUserRole(userRole);
		user.setEmailActivated(true);
		user.setUser_email(userEmail);
		user.setUser_name("admin");
		user.setUserActive(true);
		when(userDao.verifyToken(token, userEmail)).thenReturn(user);
		when(userDao.updateUser(Mockito.any())).thenReturn(userEmail);
		assertEquals(user, usersServiceImpl.verifyToken(token, userEmail));
	}

	@Test
	void getAllUsers() {
		Users user = mock(Users.class);
		user.setEmailActivated(true);
		user.setFirstName("Nithya");
		user.setLast_updated_ts(new Date());
		user.setLastName("gowda");
		user.setMobile_number("6360014890");
		user.setPatient_id("cradmo6.59662");
		user.setUser_email("navaneetha456@gmail.com");
		user.setUser_id("1");
		user.setUser_name("admin");
		user.setUser_password("test623");
		user.setUserActive(true);

		List<Users> userList = new ArrayList<>();
		userList.add(user);
		when(userDao.getAllUsers()).thenReturn(userList);
		assertEquals(userList, usersServiceImpl.getAllUsers());
	}

	@Test
	void getUserByDetails() {
		Users user = mock(Users.class);
		String userName = "adminuser@gmail.com";
		String password = "test234";

		MockHttpServletRequest request = new MockHttpServletRequest();

		user.setEmailActivated(true);
		user.setFirstName("Nithya");
		user.setLast_updated_ts(new Date());
		user.setLastName("gowda");
		user.setMobile_number("6360014890");
		user.setPatient_id("cradmo6.59662");
		user.setUser_email(userName);
		user.setUser_id("1");
		user.setUser_name(userName);
		user.setUser_password(password);
		user.setUserActive(true);

		when(userDao.getUserByDetails(userName, password)).thenReturn(user);
		HashMap<String, Integer> sessionMap = new HashMap<String, Integer>();
		sessionMap.put("expiry", 1963221010);

		MockHttpSession session = new MockHttpSession();
		session.setAttribute("1", sessionMap);

		assertNotNull(usersServiceImpl.getUserByDetails(userName, password, request));
	}
	
	@Test
	void getUserByDetailsByexception() {
		Users user = mock(Users.class);
		MockHttpServletRequest request = new MockHttpServletRequest();
		doThrow(RuntimeException.class).when(userDao).getUserByDetails(anyString(), anyString());
		user.setUser_name(null);
		user.setUser_password(null);
		assertNotNull(usersServiceImpl.getUserByDetails("admmin", "test123", request));
	}

	@Test
	void getUserByName() {
		Users user = mock(Users.class);
		String userName = "adminuser@gmail.com";

		MockHttpServletRequest request = new MockHttpServletRequest();

		user.setEmailActivated(true);
		user.setFirstName("Nithya");
		user.setLast_updated_ts(new Date());
		user.setLastName("gowda");
		user.setMobile_number("6360014890");
		user.setPatient_id("cradmo6.59662");
		user.setUser_email("adminuser@gmail.com");
		user.setUser_id("1");
		user.setUser_name(userName);
		user.setUserActive(true);

		when(userDao.getUserByName(userName)).thenReturn(user);
		HashMap<String, Integer> sessionMap = new HashMap<String, Integer>();
		sessionMap.put("expiry", 1963221010);

		MockHttpSession session = new MockHttpSession();
		session.setAttribute("1", sessionMap);
		assertNotNull(usersServiceImpl.getUserByName(userName, request));
	}

	@Test
	void getUserByNameByexception() {
		Users user = mock(Users.class);
		MockHttpServletRequest request = new MockHttpServletRequest();
		doThrow(RuntimeException.class).when(userDao).getUserByName(anyString());
		user.setUser_name(null);
		assertNotNull(usersServiceImpl.getUserByName("admmin",request));
	}
	
	@Test
	void getUserByEmailByexception() {
		Users user = mock(Users.class);
		MockHttpServletRequest request = new MockHttpServletRequest();
		doThrow(RuntimeException.class).when(userDao).getUserByEmail(anyString());
		user.setUser_email(null);
		assertNotNull(usersServiceImpl.getUserByEmail("admmin",request));
	}
	
	@Test
	void getUserByEmail() {
		Users user = mock(Users.class);
		String email = "adminuser@gmail.com";

		MockHttpServletRequest request = new MockHttpServletRequest();

		user.setEmailActivated(true);
		user.setFirstName("Nithya");
		user.setLast_updated_ts(new Date());
		user.setLastName("gowda");
		user.setMobile_number("6360014890");
		user.setPatient_id("cradmo6.59662");
		user.setUser_email(email);
		user.setUser_id("1");
		user.setUser_name("admin");
		user.setUserActive(true);

		when(userDao.getUserByEmail(email)).thenReturn(user);
		HashMap<String, Integer> sessionMap = new HashMap<String, Integer>();
		sessionMap.put("expiry", 1963221010);

		MockHttpSession session = new MockHttpSession();
		session.setAttribute("1", sessionMap);
		assertNotNull(usersServiceImpl.getUserByEmail(email, request));
	}

	@Test
	void getUserByDetailsByException() {
		Users user = new Users();
		HttpServletRequest request = mock(HttpServletRequest.class);
		HashMap<String, Integer> sessionMap = new HashMap<String, Integer>();
		doThrow(RuntimeException.class).when(userDao).getUserByDetails(Mockito.anyString(), Mockito.anyString());
		sessionMap.put("username", null);
		assertNull(usersServiceImpl.getUserByDetails(null, null, null));
	}

	@Test
	void getUserByNameByException() {
		Users user = new Users();
		HttpServletRequest request = mock(HttpServletRequest.class);
		HashMap<String, Integer> sessionMap = new HashMap<String, Integer>();
		doThrow(RuntimeException.class).when(userDao).getUserByName(Mockito.anyString());
		sessionMap.put("username", null);
		assertNull(usersServiceImpl.getUserByName(null, null));
	}

	@Test
	void getUserByEmailByException() {
		Users user = new Users();
		HttpServletRequest request = mock(HttpServletRequest.class);
		HashMap<String, Integer> sessionMap = new HashMap<String, Integer>();
		doThrow(RuntimeException.class).when(userDao).getUserByEmail(Mockito.anyString());
		sessionMap.put("email", null);
		assertNull(usersServiceImpl.getUserByEmail(null, null));
	}

	@Test
	void loadUserByNameException() {
		Users user = new Users();
		doThrow(RuntimeException.class).when(userDao).getUserByName(Mockito.anyString());
		user.setUser_name(null);
		assertNull(usersServiceImpl.loadUserByName(null));

	}

	@Test
	void verifyTokenByException() {
		Users user = new Users();
		doThrow(RuntimeException.class).when(userDao).verifyToken(Mockito.anyString(), Mockito.anyString());
		user.setUser_id("0");
		assertNull(usersServiceImpl.verifyToken("MukBCvvmUMx6FDsEVKtFrJTmcObNOr", "adminuser@gmail.com"));
	}

	@Test
	void getAllUsersByException() {
		Users user = new Users();
	
	    List<Users> userList = new ArrayList<>();
		doThrow(RuntimeException.class).when(userDao).getAllUsers();
		userList.add(null);
		assertNull(usersServiceImpl.getAllUsers());	
	}
}
