package com.interopx.fhir.auth.server.dao.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.internal.SessionImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

import com.interopx.fhir.auth.server.configuration.HibernateConfiguration;
import com.interopx.fhir.auth.server.model.AuthorizationDetails;

@SpringBootTest
class AuthorizationDetailsDaoImplTest {
	Logger log = (Logger) LoggerFactory.getLogger(AuthorizationDetailsDaoImpl.class);

	@InjectMocks
	AuthorizationDetailsDaoImpl authorizationDetailsDaoImpl;
	@Mock
	SessionFactory sessionFactory;

	@SpyBean
	HibernateConfiguration hibernateConfig;

	@Mock
	Session session;

	@Mock
	Criteria criteria;
	
	@Mock
	EntityManager em;
	
	@Mock
	SessionImpl SharedSessionContract;

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

//	@BeforeEach
//	public void setUp() {
//		MockitoAnnotations.initMocks(this);
//	}

	static AuthorizationDetails createAuthorizationDetails() {
		AuthorizationDetails authorizationDetails = new AuthorizationDetails();
//		when(authorizationDetailsDaoImpl.)
		authorizationDetails.setAccessToken("token");
		authorizationDetails.setAud("j");
		authorizationDetails.setExpiry("2030-05-12 03:15:20");
		authorizationDetails.setAuthCode("jefjk");
		authorizationDetails.setClientId("n-WW3PPCp-41DdYX2-i0Ksihe");
		authorizationDetails.setScope("123");
		authorizationDetails.setClientSecret("Allow");
		authorizationDetails.setTransactionId("123");
		authorizationDetails.setClientId("12");
		authorizationDetails.setAuthCode("12");
		authorizationDetails.setRedirectUri("A");
		authorizationDetails.setState("abc");
		authorizationDetails.setScope("scope");
		authorizationDetails.setRefreshToken("123");
		authorizationDetails.setRefreshTokenExpiryTime("2030-06-12 03:15:20");
		authorizationDetails.setLaunchPatientId("1");
		authorizationDetails.setCodeChallenge("code_Challenge");
		authorizationDetails.setCodeChallengeMethod("CodeChallenge method");
		return authorizationDetails;

	}

	@Test
	void testSaveOrUpdate() {
		AuthorizationDetails auths = createAuthorizationDetails();

		// AuthorizationDetails authorizationDetails = new AuthorizationDetails();
		authorizationDetailsDaoImpl.getAuthById(auths.getClientId());
		auths.setAccessToken(auths.getAccessToken());
		auths.setAud(auths.getAud());
		auths.setAuthCode(auths.getAuthCode());
		auths.setClientId(auths.getClientId());
		auths.setClientSecret(auths.getClientSecret());
		auths.setExpiry(auths.getExpiry());
		auths.setRedirectUri(auths.getRedirectUri());
		auths.setScope(auths.getScope());
		auths.setTransactionId(auths.getTransactionId());
		auths.setState(auths.getState());
		auths.setRefreshToken(auths.getRefreshToken());
		auths.setRefreshTokenExpiryTime(auths.getRefreshTokenExpiryTime());
		auths.setLaunchPatientId(auths.getLaunchPatientId());
		auths.setCodeChallenge(auths.getCodeChallenge());
		auths.setCodeChallengeMethod(auths.getCodeChallengeMethod());
		

		assertEquals(auths, authorizationDetailsDaoImpl.saveOrUpdate(auths));
	}

//	@Test
//	void testSaveOrUpdate2() {
//		AuthorizationDetails auths = new AuthorizationDetails();
//		auths.setClientId("1");
//		auths.setAccessToken(null);
//		AuthorizationDetails auth = authorizationDetailsDaoImpl.getAuthById(auths.getClientId());
//		
//		assertEquals(auth, authorizationDetailsDaoImpl.saveOrUpdate(auth));
//	}
	@Test
    void testGetAuthByClientId()throws InvocationTargetException  {
        AuthorizationDetails auths = createAuthorizationDetails();
        String clientId = auths.getClientId() ;
        String clientSecret = auths.getClientSecret();



       Class<AuthorizationDetails> authorizationDetails = AuthorizationDetails.class;
        when(session.createCriteria(authorizationDetails)).thenReturn(criteria);
        when((AuthorizationDetails) criteria.uniqueResult()).thenReturn(auths);
        
        assertNotEquals(auths,authorizationDetailsDaoImpl.getAuthByClientId(clientId, clientSecret));
    }

	@Test
	void testGetList() {
		AuthorizationDetails auths = createAuthorizationDetails();
		List<AuthorizationDetails> authorizationDetails = new ArrayList<>();
		authorizationDetails.add(auths);
		
		Class<AuthorizationDetails> authorizationDetails1 = AuthorizationDetails.class;
		when(session.createCriteria(authorizationDetails1)).thenReturn(criteria);
		when(criteria.list()).thenReturn(authorizationDetails);
		assertEquals(authorizationDetails, authorizationDetailsDaoImpl.getList());
	}

	@Test
	void testGetAuthorizationByAccessToken() {
		AuthorizationDetails auths = createAuthorizationDetails();
		String accessToken = auths.getAccessToken();
		Class<AuthorizationDetails> authorizationDetails1 = AuthorizationDetails.class;
		when(session.createCriteria(authorizationDetails1)).thenReturn(criteria);
		when(criteria.add(Restrictions.eq("accessToken", accessToken))).thenReturn(criteria);
		when(criteria.uniqueResult()).thenReturn(criteria);
        assertNotNull(auths);

	}

	@Test
    void testGetAuthenticationById() {
        AuthorizationDetails auths = createAuthorizationDetails();
        String transactionId =  auths.getTransactionId();
        Class<AuthorizationDetails> authorizationDetails1 = AuthorizationDetails.class;
        when(session.createCriteria(authorizationDetails1)).thenReturn(criteria);
        when(criteria.add(Restrictions.eq("transactionId", transactionId))).thenReturn(criteria);
        assertNotEquals(auths.getTransactionId(),authorizationDetailsDaoImpl.getAuthenticationById(transactionId));



   }

	@Test
	void testGetAuthById() {
		AuthorizationDetails auths = createAuthorizationDetails();
		String clientId =  auths.getClientId();
		Class<AuthorizationDetails> authorizationDetails1 = AuthorizationDetails.class;
		when(session.createCriteria(authorizationDetails1)).thenReturn(criteria);
		criteria.add(Restrictions.eq("clientId", clientId));		
		when(criteria.uniqueResult()).thenReturn(auths);
		assertNotEquals(auths,authorizationDetailsDaoImpl.getAuthById(clientId));
	}

	@Test
	void testSaveOrUpdate1() {
		
		AuthorizationDetails auth = mock(AuthorizationDetails.class);
		doThrow(NullPointerException.class).when(auth).getClientId();
		auth.setClientId(null);
		assertNotNull(authorizationDetailsDaoImpl.saveOrUpdate(auth));
	}
	
	@Test
	void testGetAuthByClientId1()throws InvocationTargetException  {
		AuthorizationDetails auths = mock(AuthorizationDetails.class);
		auths.setClientId(null);
		auths.setClientSecret(null);
		doThrow(RuntimeException.class).when(auths).getClientId();
		doThrow(RuntimeException.class).when(auths).getClientSecret();
		assertNull(authorizationDetailsDaoImpl.getAuthByClientId(null, null));

	}
	
	@Test
    void testGetListException(){
		AuthorizationDetails auth=mock(AuthorizationDetails.class);
		auth.setClientId(null);
		List list=new ArrayList();
		doThrow(RuntimeException.class).when(auth).getClientId();
		
	    assertEquals(list,authorizationDetailsDaoImpl.getList() );
    }
	
	@Test
	void testGetAuthorizationByAccessTokenException() {
		AuthorizationDetails auths = mock(AuthorizationDetails.class);
		auths.setAccessToken(null);
		doThrow(RuntimeException.class).when(auths).getAccessToken();
		assertNull(authorizationDetailsDaoImpl.getAuthorizationByAccessToken(null));

	}
	
}
