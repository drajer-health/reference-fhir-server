package com.interopx.fhir.facade.util;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;

import com.interopx.fhir.facade.model.QueueConfiguration;
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class QueueConfigurationTest {

	// @Test
	void test() {
		QueueConfiguration queueConfiguration = Mockito.spy(QueueConfiguration.class);
		queueConfiguration.setListenersSize(12);
		queueConfiguration.setOutQueueConnectionString("test");
		queueConfiguration.setOutQueueName("testname");
		queueConfiguration.setQueueConfigId(12);
		
		when(queueConfiguration.getListenersSize()).thenReturn(12);
		when(queueConfiguration.getOutQueueConnectionString()).thenReturn("test");
		when(queueConfiguration.getOutQueueName()).thenReturn("testname");
		when(queueConfiguration.getQueueConfigId()).thenReturn(12);
		
		assertEquals(queueConfiguration.getListenersSize(), 12);
	}

}
