package com.interopx.fhir.auth.server.controller;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.interopx.fhir.auth.server.model.UserRole;
import com.interopx.fhir.auth.server.service.UserRoleService;

@SpringBootTest
@ActiveProfiles("test")
class SOFUserRoleControllerTest {

	@InjectMocks
	SOFUserRoleController sOFUserRoleController;

	@Mock
	UserRoleService sOFUserRoleService;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	void testGetAllSOFUserRole() {
		UserRole userRole = new UserRole();
		List<UserRole> list = new ArrayList<UserRole>();
		list.add(0, userRole);
		Mockito.lenient().doReturn(list).when(sOFUserRoleService).getAllRoles();
		assertEquals(list.size(), sOFUserRoleController.getAllSOFUserRole().size());

	}

}
