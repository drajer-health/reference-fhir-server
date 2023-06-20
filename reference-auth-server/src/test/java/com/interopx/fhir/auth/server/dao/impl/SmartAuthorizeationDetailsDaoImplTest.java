package com.interopx.fhir.auth.server.dao.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

import com.interopx.fhir.auth.server.configuration.HibernateConfiguration;
import com.interopx.fhir.auth.server.model.Jwks;
import com.interopx.fhir.auth.server.model.SmartAuthorizeationDetails;

@SpringBootTest
class SmartAuthorizeationDetailsDaoImplTest {

	@Mock
	SessionFactory sessionFactory;

	@InjectMocks
	SmartAuthorizeationDetailsDaoImpl smartAuthorizeationDetailsDaoImpl;

	@SpyBean
	HibernateConfiguration hibernateConfig;

	@Mock
	Criteria criteria;

	@Mock
	Session session;

	@BeforeEach
	void setUp() {
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
		}
	}

	@Test
	void testGetAuthorizationDetailsById() {

		SmartAuthorizeationDetails smartAuthorizeationDetails = new SmartAuthorizeationDetails();

		smartAuthorizeationDetails.setId(1);
		smartAuthorizeationDetails.setIsClaimsParamSupported(true);
		smartAuthorizeationDetails.setIsRequestParamSupported(true);
		smartAuthorizeationDetails.setIsRequestUriRegistration(true);
		List<String> algorithmsList = new ArrayList<>();
		algorithmsList.add("RS256");
		smartAuthorizeationDetails.setAlgorithmTypes(algorithmsList);
		List<String> subjectList = new ArrayList<>();
		subjectList.add("public");
		smartAuthorizeationDetails.setSubjectTypes(subjectList);
		List<String> claimsList = new ArrayList<>();
		String claims = "{sub,name,profile,email}";
		claimsList.add(claims);
		List<String> resourceTypesList = new ArrayList<>();
		when(session.get(SmartAuthorizeationDetails.class, 1)).thenReturn(smartAuthorizeationDetails);

		assertNotNull(smartAuthorizeationDetailsDaoImpl.getAuthorizationDetailsById(1));

	}
}
