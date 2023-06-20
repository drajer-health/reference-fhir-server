package com.interopx.fhir.facade.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;

import com.interopx.fhir.facade.dao.AzureQueueDao;
import com.interopx.fhir.facade.exception.MessagingException;
import com.interopx.fhir.facade.model.AuthConfiguration;
import com.interopx.fhir.facade.model.RequestResponseLog;
import com.interopx.fhir.facade.service.impl.AzureQueueServiceImpl;
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class AzureQueueServiceImplTest {
	@InjectMocks
	AzureQueueServiceImpl azureQueueServiceImpl; 
	@Mock
	AzureQueueDao azureQueueDao;
	RequestResponseLog response = new RequestResponseLog();	
	AuthConfiguration authConfiguration = null;
	@BeforeEach
	void setUp() {
		response = new RequestResponseLog();
		response.setPayload("test");
		response.setRequestId("4bf0f3e7-7b0e-4ed1-b486-0d3bf4e47ac9");
		response.setStatus("Completed");
		response.setTimestamp(new Date());
		
	}

	@Test
	void testGetRequestResponseLogById() throws Exception {
		System.out.println("--RAJ INSIDE testGetRequestResponseLogById()");
		when(azureQueueDao.getRequestResponseLogById(any(String.class))).thenReturn(response);
		assertEquals(response,azureQueueServiceImpl.getRequestResponseLogById("4bf0f3e7-7b0e-4ed1-b486-0d3bf4e47ac9"));	
		System.out.println("--RAJ END testGetRequestResponseLogById()");				
	}

}
