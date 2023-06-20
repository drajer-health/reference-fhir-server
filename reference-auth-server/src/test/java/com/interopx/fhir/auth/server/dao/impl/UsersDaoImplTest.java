package com.interopx.fhir.auth.server.dao.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;

import com.interopx.fhir.auth.server.configuration.HibernateConfiguration;
import com.interopx.fhir.auth.server.model.Users;

@SpringBootTest
class UsersDaoImplTest {

	@InjectMocks
	UsersDaoImpl daoImpl;

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
	
//	@Test
	void testRegister() {
		Users users = mock(Users.class);
		String userName = "admin";
		users.setEmailActivated(true);
		users.setFirstName("Nithya");
		users.setLast_updated_ts(new Date());
		users.setLastName("gowda");
		users.setMobile_number("6360014890");
		users.setPatient_id("cradmo6.59662");
		users.setUser_email("navaneetha456@gmail.com");
		users.setUser_id("1");
		users.setUser_name("admin");
		users.setUser_password("test623");
		users.setUserActive(true);

		List<Users> userList = new ArrayList<>();
		userList.add(users);

		Class<Users> user = Users.class;
		Mockito.when(session.createCriteria(user)).thenReturn(criteria);
		when(users.getUser_name()).thenReturn(userName);
//		Restrictions restrictions = mock(Restrictions.class);
//		PowerMockito.mockStatic(Restrictions.class);
		criteria.add(Restrictions.eq("user_name", anyString()));
//		when(criteria.list()).thenReturn(userList);

		when(session.save(user)).thenReturn(234);
		assertNotNull(daoImpl.register(users));

	}
	
	@Test
	void testRegisterException() {
		Users users = mock(Users.class);
		doThrow(RuntimeException.class).when(users).getUser_name();
		users.setUser_name(null);
		assertNull(daoImpl.register(users));
		
	}

	@Test
	void testUpdateUser() {
		Users users = new Users();
		users.setEmailActivated(true);
		users.setFirstName("Nithya");
		users.setLast_updated_ts(new Date());
		users.setLastName("gowda");
		users.setMobile_number("6360014890");
		users.setPatient_id("cradmo6.59662");
		users.setUser_email("navaneetha456@gmail.com");
		users.setUser_id("1");
		users.setUser_name("admin");
		users.setUser_password("test623");
		users.setUserActive(true);
		String message = "User details updated.";
		assertEquals(message, daoImpl.updateUser(users));

	}

	@Test
	void testUpdateUserByException() {
		Users users = new Users();
		users.setUser_email(null);
		doThrow(RuntimeException.class).when(session).update(users);
		String message = "Failed to update User details. Please contact Admin.";
		assertEquals(message, daoImpl.updateUser(users));

	}

	@Test
	void testGetUserById() {
		Users users = new Users();
		users.setEmailActivated(true);
		users.setFirstName("Nithya");
		users.setLast_updated_ts(new Date());
		users.setLastName("gowda");
		users.setMobile_number("6360014890");
		users.setPatient_id("cradmo6.59662");
		users.setUser_email("navaneetha456@gmail.com");
		users.setUser_id("123");
		users.setUser_name("admin");
		users.setUser_password("test623");
		users.setUserActive(true);
		when(session.get(Users.class, "123")).thenReturn(users);
		assertEquals(users, daoImpl.getUserById("123"));
	}

	@Test
	void testGetUserByIdByException() {
		Users users = mock(Users.class);

		doThrow(RuntimeException.class).when(users).getUser_id();
		users.setUser_email(null);
		users.setUser_id("0");
		assertNull(daoImpl.getUserById("0"));
	}

//	@Test
	void testGetUserByDetails() {
		Users users = new Users();
		String userName = "admin";
		String password = "test123";
		users.setEmailActivated(true);
		users.setFirstName("Nithya");
		users.setLast_updated_ts(new Date());
		users.setLastName("gowda");
		users.setMobile_number("6360014890");
		users.setPatient_id("cradmo6.59662");
		users.setUser_email("navaneetha456@gmail.com");
		users.setUser_id("1");
		users.setUser_name(userName);
		users.setUser_password(password);
		users.setUserActive(true);

//		Class<Users> user = Users.class;
		when(session.createCriteria(Users.class)).thenReturn(criteria);
		criteria.add(Restrictions.eq("user_name", userName));
		criteria.add(Restrictions.eq("user_password", password));
		when(criteria.uniqueResult()).thenReturn(users);
		assertNotNull(daoImpl.getUserByDetails(userName, password));
	}

	@Test
	void testGetUserByDetailsByException() {
		Users users = mock(Users.class);		
		doThrow(RuntimeException.class).when(users).getUser_email();
		users.setUser_email(null);
		assertNull(daoImpl.getUserByDetails("admin", "test123"));

	}
	
	@Test
	void testGetUserByName() {

	}

	@Test
	void testGetUserByNameByException() {
		Users users = mock(Users.class);		
		doThrow(RuntimeException.class).when(users).getUser_name();
		users.setUser_name(null);
		assertNull(daoImpl.getUserByName("admin"));

	}
	@Test
	void testGetUserByEmail() {

	}

	@Test
	void testGetUserByEmailByException() {
		Users users = mock(Users.class);		
		doThrow(RuntimeException.class).when(users).getUser_email();
		users.setUser_email(null);
		assertNull(daoImpl.getUserByEmail("admin@gmail.com"));

	}
	@Test
	void testGetPropertyValue() {

	}
	
	@Test
	void testGetPropertyValueByException() {
		Users users = mock(Users.class);		
		doThrow(RuntimeException.class).when(users).getUser_email();
		users.setUser_email(null);
		assertNull(daoImpl.getPropertyValue("MukBCvvmUMx6FDsEVKtFrJTmcObNOr"));


	}

	@Test
	void testVerifyToken() {

	}
	
	@Test
	void testVerifyTokenByException() {
		Users users = mock(Users.class);		
		doThrow(RuntimeException.class).when(users).getUser_email();
		users.setUser_email(null);
		assertNull(daoImpl.verifyToken("4af2e5541bde53f8a4dcf87a0c38b937", "admin@gmail.com"));


	}

	@Test
	void testGetAllUsers() {
		Users users = new Users();
		users.setEmailActivated(true);
		users.setFirstName("Nithya");
		users.setLast_updated_ts(new Date());
		users.setLastName("gowda");
		users.setMobile_number("6360014890");
		users.setPatient_id("cradmo6.59662");
		users.setUser_email("navaneetha456@gmail.com");
		users.setUser_id("1");
		users.setUser_name("admin");
		users.setUser_password("test623");
		users.setUserActive(true);
		List<Users> usersList = new ArrayList<>();
		usersList.add(users);
		when(session.createCriteria(Users.class)).thenReturn(criteria);
		when(criteria.list()).thenReturn(usersList);
		assertEquals(usersList, daoImpl.getAllUsers());
	}

	@Test
	void testGetAllUsersByException() {
		Users users = mock(Users.class);
		users.setUser_email(null);
		List usersList = new ArrayList();
		doThrow(RuntimeException.class).when(users).getUser_email();
		assertEquals(usersList, daoImpl.getAllUsers());

	}
}
