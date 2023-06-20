package com.interopx.fhir.auth.server.service.impl;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.interopx.fhir.auth.server.dao.UserRoleDao;
import com.interopx.fhir.auth.server.model.UserRole;

@SpringBootTest
class UserRoleServiceImplTest {
	
	@InjectMocks
	UserRoleServiceImpl userRoleServiceImpl;
	
	@Mock
	UserRoleDao sOFUserRoleDao;

	@Test
	void getAllRoles() {
		List<UserRole> list = new ArrayList<>();
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
		list.add(userRole);
		when(sOFUserRoleDao.getAllRoles()).thenReturn(list);
		
		assertNotNull(userRoleServiceImpl.getAllRoles());
	}
}
