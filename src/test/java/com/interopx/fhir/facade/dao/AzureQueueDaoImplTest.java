package com.interopx.fhir.facade.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import com.interopx.fhir.facade.configuration.HibernateConfiguration;
import com.interopx.fhir.facade.exception.InternalErrorException;
import com.interopx.fhir.facade.model.RequestResponseLog;
import com.interopx.fhir.facade.provider.TestUtil;
import com.interopx.fhir.facade.util.HibSessionFactorySingleton;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
class AzureQueueDaoImplTest {

	RequestResponseLog response = null;
	@SpyBean
	AzureQueueDaoImpl azureQueueDao;

	@MockBean
	EntityManager entityManager;

	@BeforeEach
	void setUp() throws IOException {
		String jsonString = TestUtil.convertJsonToJsonString("bundle/immunization.json");
		response = new RequestResponseLog();
		response.setPayload(jsonString);
		response.setRequestId("4bf0f3e7-7b0e-4ed1-b486-0d3bf4e47ac9");
		response.setStatus("Completed");
		response.setTimestamp(new Date());
	}

	@Test
	void testUpdateResponse() throws SQLException {
		HibernateConfiguration hibernateConfiguration = Mockito.mock(HibernateConfiguration.class);
		Connection connection = Mockito.mock(Connection.class);
		when(hibernateConfiguration.getConnection()).thenReturn(connection);

		try (MockedStatic<HibSessionFactorySingleton> utilities = Mockito
				.mockStatic(HibSessionFactorySingleton.class)) {
			utilities.when(() -> HibSessionFactorySingleton.getSessionFactory(entityManager.getEntityManagerFactory()))
					.thenThrow(RuntimeException.class);
			Throwable exception = assertThrows(InternalErrorException.class,
					() -> azureQueueDao.updateResponse(response));
			assertEquals("500 Internal Server Error: Unable to process request", exception.getMessage());

		}

	}

	@Test
	void testSaveRequest() throws SQLException {
		HibernateConfiguration hibernateConfiguration = Mockito.mock(HibernateConfiguration.class);
		Connection connection = Mockito.mock(Connection.class);
		when(hibernateConfiguration.getConnection()).thenReturn(connection);

		try (MockedStatic<HibSessionFactorySingleton> utilities = Mockito
				.mockStatic(HibSessionFactorySingleton.class)) {
			utilities.when(() -> HibSessionFactorySingleton.getSessionFactory(entityManager.getEntityManagerFactory()))
					.thenThrow(RuntimeException.class);
			Throwable exception = assertThrows(InternalErrorException.class, () -> azureQueueDao.saveRequest(response));
			assertEquals("500 Internal Server Error: Unable to process request", exception.getMessage());

		}

	}

	@Test
	void testUpdateStatus() throws SQLException {
		HibernateConfiguration hibernateConfiguration = Mockito.mock(HibernateConfiguration.class);
		Connection connection = Mockito.mock(Connection.class);
		when(hibernateConfiguration.getConnection()).thenReturn(connection);

		try (MockedStatic<HibSessionFactorySingleton> utilities = Mockito
				.mockStatic(HibSessionFactorySingleton.class)) {
			utilities.when(() -> HibSessionFactorySingleton.getSessionFactory(entityManager.getEntityManagerFactory()))
					.thenThrow(RuntimeException.class);
			Throwable exception = assertThrows(InternalErrorException.class,
					() -> azureQueueDao.updateStatus("test", "test"));
			assertEquals("500 Internal Server Error: Unable to process request", exception.getMessage());

		}

	}

	@Test
	void testgetRequestResponseLogById() throws SQLException {
		HibernateConfiguration hibernateConfiguration = Mockito.mock(HibernateConfiguration.class);
		Connection connection = Mockito.mock(Connection.class);
		when(hibernateConfiguration.getConnection()).thenReturn(connection);

		try (MockedStatic<HibSessionFactorySingleton> utilities = Mockito
				.mockStatic(HibSessionFactorySingleton.class)) {
			utilities.when(() -> HibSessionFactorySingleton.getSessionFactory(entityManager.getEntityManagerFactory()))
					.thenThrow(RuntimeException.class);
			Throwable exception = assertThrows(InternalErrorException.class,
					() -> azureQueueDao.getRequestResponseLogById("test"));
			assertEquals("500 Internal Server Error: Unable to process request", exception.getMessage());

		}

	}

}
