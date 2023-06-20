package com.interopx.fhir.facade.service;

import static org.mockito.Mockito.when;

import java.io.IOException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class AbstractDaoTest {
	@Mock
	SessionFactory sessionFactory;

	@Mock
	Session session;
	
	@Mock
	Query query;
	
	@BeforeEach
	private void setUp() throws IOException, JSONException {
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
	}}

	//@Test
	void test() {
		Object obj = Mockito.mock(Object.class);
		AbstractDao abstractDao = Mockito.mock(AbstractDao.class);
		when(abstractDao.getSession()).thenReturn(session);
		//abstractDao.delete(obj);
	}

}
