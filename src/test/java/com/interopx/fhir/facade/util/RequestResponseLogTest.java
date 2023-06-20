package com.interopx.fhir.facade.util;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;

import com.interopx.fhir.facade.model.RequestResponseLog;
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class RequestResponseLogTest {

	//@Test
	void test() {
		RequestResponseLog response = spy(RequestResponseLog.class);
		response.setPayload("test");
		response.setRequestId("4bf0f3e7-7b0e-4ed1-b486-0d3bf4e47ac9");
		response.setStatus("Completed");
		response.setTimestamp(new Date());
		
		when(response.getPayload()).thenReturn("test");
		when(response.getRequestId()).thenReturn("4bf0f3e7-7b0e-4ed1-b486-0d3bf4e47ac9");
		when(response.getStatus()).thenReturn("test");
		when(response.getTimestamp()).thenReturn(new Date());
		
		assertEquals(response.getPayload(), "test");
	}

}
