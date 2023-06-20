package com.interopx.fhir.facade.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doReturn;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;

import com.interopx.fhir.facade.dao.QueueConfigurationDao;
import com.interopx.fhir.facade.model.QueueConfiguration;
import com.interopx.fhir.facade.service.impl.QueueConfigurationServiceImpl;
import com.interopx.fhir.facade.util.QueueConfigurationDto;
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class QueueConfigurationServiceImplTest {
	@InjectMocks
	QueueConfigurationServiceImpl queueConfigurationServiceImpl;
	@Mock
	QueueConfigurationDao queueConfigurationDao;
	QueueConfiguration queueConfiguration =  null;
	
	@BeforeEach
	private void setUp() {
		 queueConfiguration = new QueueConfiguration();
		 queueConfiguration.setListenersSize(1);
		 queueConfiguration.setOutQueueConnectionString("Connection String");
		 queueConfiguration.setOutQueueName("Out Queue name");
		 queueConfiguration.setQueueConfigId(123);
	}

	@Test
	void updateConfigurationTest() {
		QueueConfigurationDto dto = new QueueConfigurationDto();
		dto.setInQueueConnectionString("dd");
		dto.setQueueConfigId(1);
		dto.setInQueueName("test");
		dto.setListenersSize(1);
		dto.setOutQueueConnectionString("test");
		dto.setOutQueueName("test");
		dto.setQueueConfigId(1);
		doReturn(null).when(queueConfigurationDao).getConfigurationById(anyInt());
		assertThrows(QueueConfigurationException.class, () -> queueConfigurationServiceImpl.updateConfiguration(dto,12));
		
		
	}

}
