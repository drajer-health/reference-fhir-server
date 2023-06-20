package com.interopx.fhir.facade;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;

import com.interopx.fhir.facade.configuration.RunAfterStartUp;
import com.interopx.fhir.facade.model.QueueConfiguration;
import com.interopx.fhir.facade.service.AuthConfigurationService;
import com.interopx.fhir.facade.service.QueueConfigurationService;
import com.interopx.fhir.facade.util.InteropxRequestQReceiver;
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class RunAfterStartUpTest {
	@InjectMocks
	RunAfterStartUp runAfterStartUp;
	@Mock
	QueueConfigurationService queueConfigurationService;
	@Mock
	ApplicationContext context;
	@Mock
	InteropxRequestQReceiver interopxRequestQReceiver;
	@Mock
	AuthConfigurationService authConfigurationService;
	@Test
	void test() {
		List<QueueConfiguration> queueConfigurations = new ArrayList<>();
		QueueConfiguration queueConfiguration = new QueueConfiguration();
		queueConfiguration.setListenersSize(1);
		queueConfiguration.setOutQueueConnectionString("test");
		queueConfiguration.setOutQueueName("testQueue");
		queueConfiguration.setQueueConfigId(123);
		queueConfigurations.add(queueConfiguration);
		when(queueConfigurationService.getAllQueueConfiguration()).thenReturn(queueConfigurations);
		when((InteropxRequestQReceiver) context.getBean("interopxRequestQReceiver")).thenReturn(interopxRequestQReceiver);
		doNothing().when(interopxRequestQReceiver).receiveMessages(any(String.class), any(String.class));
		runAfterStartUp.runAfterStartup();
	}
}
