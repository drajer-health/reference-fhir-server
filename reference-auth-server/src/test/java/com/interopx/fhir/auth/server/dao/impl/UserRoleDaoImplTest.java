package com.interopx.fhir.auth.server.dao.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

import com.interopx.fhir.auth.server.configuration.HibernateConfiguration;
import com.interopx.fhir.auth.server.model.DafPatientJson;
import com.interopx.fhir.auth.server.model.UserRole;

@SpringBootTest
class UserRoleDaoImplTest {

	@InjectMocks
	UserRoleDaoImpl daoImpl;

	@Mock
	SessionFactory sessionFactory;

	@SpyBean
	HibernateConfiguration hibernateConfig;

	@Mock
	Criteria criteria;

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
	void testGetRoleById() {
		UserRole userRole = new UserRole();
		userRole.setCreated(new Date());
		userRole.setCreatedby("System");
		userRole.setLastUpdated(new Date());
		userRole.setRoleDescription("Given Role is Admin");
		List<String> permission = new ArrayList<>();
		permission.add("Admin");
		userRole.setPermissionNames(permission);
		userRole.setRoleId(1);
		userRole.setRoleName("Admin");
		userRole.setRolePredefined(true);
		userRole.setStatus(true);

		when(session.get(UserRole.class, 1)).thenReturn(userRole);

		assertEquals(userRole, daoImpl.getRoleById("1"));

	}

	@Test
	void testGetAllRoles() {
		UserRole userRole = new UserRole();
		userRole.setCreated(new Date());
		userRole.setCreatedby("System");
		userRole.setLastUpdated(new Date());
		userRole.setRoleDescription("Given Role is Admin");
		List<String> permission = new ArrayList<>();
		permission.add("Admin");
		userRole.setPermissionNames(permission);
		userRole.setRoleId(1);
		userRole.setRoleName("Admin");
		userRole.setRolePredefined(true);
		userRole.setStatus(true);

		when(session.createCriteria(UserRole.class)).thenReturn(criteria);

		List<UserRole> list = new ArrayList<>();
		list.add(userRole);

		when(criteria.list()).thenReturn(list);
		assertEquals(list, daoImpl.getAllRoles());
	}

	@Test
	void testGetAllRolesByException() {
		UserRole userRole = mock(UserRole.class);
		userRole.setRoleId(0);
		List userRoleList = new ArrayList<>();
		doThrow(RuntimeException.class).when(userRole).getRoleId();
		assertEquals(userRoleList,daoImpl.getAllRoles());

	}
}
