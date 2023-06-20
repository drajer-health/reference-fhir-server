package com.interopx.fhir.auth.server.controller;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ApplicationControllerTest {
	
	@InjectMocks
	ApplicationController applicationController;
	

	@Test
	void swaggerHomePage() {
		assertNotNull(applicationController.swaggerHomePage());
	}
	
	@Test
	void swaggerPage() {
		assertNotNull(applicationController.swaggerPage());
	}

}
