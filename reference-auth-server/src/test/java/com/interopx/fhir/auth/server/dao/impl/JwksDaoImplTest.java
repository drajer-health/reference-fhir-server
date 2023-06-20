package com.interopx.fhir.auth.server.dao.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.hibernate.Criteria;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.SharedSessionContract;
import org.hibernate.criterion.Restrictions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.interopx.fhir.auth.server.configuration.HibernateConfiguration;
import com.interopx.fhir.auth.server.model.Jwks;

@SpringBootTest
class JwksDaoImplTest {

	@InjectMocks
	JwksDaoImpl jwksDaoImpl;

	@Mock
	HibernateConfiguration hibernateConfig;

	@Mock
	SessionFactory sessionFactory;
	
	@Mock
	SharedSessionContract sharedSessionContract;

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
	void getById() {
		// when(sessionFactory.getCurrentSession()).thenReturn(session);
		Jwks jwks = new Jwks();

		jwks.setId(1);
		Class<Jwks> entityType = Jwks.class;
		when(session.get(entityType, 1)).thenReturn(jwks);

		assertEquals(jwks, jwksDaoImpl.getById(1));

	}

	@Test
	void saveOrUpdate() {

		Jwks jwks = new Jwks();
		Map<String, Object> jwkData = new HashMap();
		jwkData.put("p",
				"wmhKSipECN1fWGeicAwhTkGHjI393eSr0Tgd90yxcitLxkiSeg8-dEreLScRvpPcgKdyDA6raHIrRBMqOv8TqMyPinhXAZ16BJOzanUwcSZIngGG6AOceVLp_1BXMprGqNH0uPhJZKuid23SryPVmfwUmE9f9cthUtz0y-41Tyc");
		jwkData.put("kty", "RSA");
		jwkData.put("q",
				"teqsXf_va1rgAI-GvxBwYA5iAdRhzKfZEc8V6r10rSicb98bKa0mgyCRppqmt4TX05yRrjQpYJ52peK6pUlAlAEE3vWBn_x_umXkasILkMmwPx7GQhDrYYo9AKzlhvb0bOXrmKuorMu6rbVIPnseBsU-CIsa0b_gaQAnaSKTeAE");
		jwkData.put("d",
				"QTnfnn0ACBWW9nY8o70CRaLJ0q0wPt5Casy_KXkNyOiqdMD8ZpltCuGrL17ZPJbAfVO5hOaNsAv78QBxWUU0jmf-71so1wHpiO-JkmfG9auhNmWYKis3wxeoFFPFADwD4P4yI6ACUUMV3eZbSumbQchkLG7Mt-xCNwTTvbRGgrL7kEeIwuHHx4L1Ht7K2sXjOkdFK4Yo6BjuScWxiNSeqWwdo0HgnFohwOkqCWVbIxsPVkazG5fAq3j9f4O3sinTyLAqdckCOFhojUROu8BCJ1C2H5DzrAfk6eC-K__hjuCkV3F2wvn-wcMTA7Hxw8cEmlXQ36uuYA-Svvt7S-kwAQ");
		jwkData.put("e", "AQAB");
		jwkData.put("use", "sig");
		jwkData.put("kid", "2IsGMGIj8G");
		jwkData.put("qi",
				"sBzjBkf-CHlTCbEtyLC1StyT7Z1sZRaclixSj1RqUErA7BSoIPj-1PjTd8er_MTAAHhYIm8c7jtbwsBx-AsXjipx2WAerB-rZCmT59U07IwxCGJv3VJCI6LHsy-GBXP0P5B51wjEaZ3U_TmzMNsPtSQpaHalguhHkoxTf5gFHUg");
		jwkData.put("dp",
				"S8Rln74VQfc1H7lUP6Man-s-LvNwC7kBlcrvMuC7D6n8IE1MeTHTv3MmYCan7cSm3aVk93oIJ-7HGgL2JIQhX1pKX0dDcae0VHBULpt5w7-N8bbaozY1F7vO4uim81wLLOSXIjfyMsAJ084DnKq0pwzt4eadmagoSClBZvFx6WU");
		jwkData.put("alg", "RS256");
		jwkData.put("dq",
				"eOH5KflxAfiAEz9MfdgRpHLKQ4b6egErKD9gw_yi9JGg6mHp7dbGWaohvwDTOe3HcTGASK8Ws8J4-yBfgIfdprvKndGsEkC6K4WbtCk_gDXVrQvpGGufzdALW1CIsReXMmnSFUA0NKaoZNeqqwIq4LreztSIvMpw97UHe0BjmAE");
		jwkData.put("n",
				"iiXyv8vVQK-EmKKlKuCi1wf-64gxrFhFG29_pnOrumRy6TgT2jkIcxZQRqM0JE2URBt9psPZkTwSeV9FCcI_MCWSX6xGq_U_zWBc8Qc73n-D2fdVkYvxIJEsL_vXXJg6rrL6FUe0OjB1KGI67spuk1gaDRRvrb0WPak8dQzqlqypr0LQD-FBlNb8QPMTFAEMKEVaFlH4Hwu2Gg6sHEkDzYwWz4XC5R0DwWdc-UEIXzHBdYwGU_bIUGAREG--qs9o3M1ZiSTFMBD33hutAhQa0eOt9mf646gnQebGYVgjjEWn3-rT4clLmb8GPApecPeNeRvaSmFZCSJeRdGTjLSXJw");

		jwks.setId(1);
		jwks.setJwk(jwkData.toString());
		jwks.setLastUpdatedDatetime(new Date());
		Class<Jwks> jwk = Jwks.class;
		when(session.get(jwk, 1)).thenReturn(jwks);
		assertEquals(jwks, jwksDaoImpl.saveOrUpdate(jwks));

	}

	@Test
	void updateById() {

		Jwks jwks = new Jwks();
		Map<String, Object> jwkData = new HashMap();
		jwkData.put("p",
				"wmhKSipECN1fWGeicAwhTkGHjI393eSr0Tgd90yxcitLxkiSeg8-dEreLScRvpPcgKdyDA6raHIrRBMqOv8TqMyPinhXAZ16BJOzanUwcSZIngGG6AOceVLp_1BXMprGqNH0uPhJZKuid23SryPVmfwUmE9f9cthUtz0y-41Tyc");
		jwkData.put("kty", "RSA");
		jwkData.put("q",
				"teqsXf_va1rgAI-GvxBwYA5iAdRhzKfZEc8V6r10rSicb98bKa0mgyCRppqmt4TX05yRrjQpYJ52peK6pUlAlAEE3vWBn_x_umXkasILkMmwPx7GQhDrYYo9AKzlhvb0bOXrmKuorMu6rbVIPnseBsU-CIsa0b_gaQAnaSKTeAE");
		jwkData.put("d",
				"QTnfnn0ACBWW9nY8o70CRaLJ0q0wPt5Casy_KXkNyOiqdMD8ZpltCuGrL17ZPJbAfVO5hOaNsAv78QBxWUU0jmf-71so1wHpiO-JkmfG9auhNmWYKis3wxeoFFPFADwD4P4yI6ACUUMV3eZbSumbQchkLG7Mt-xCNwTTvbRGgrL7kEeIwuHHx4L1Ht7K2sXjOkdFK4Yo6BjuScWxiNSeqWwdo0HgnFohwOkqCWVbIxsPVkazG5fAq3j9f4O3sinTyLAqdckCOFhojUROu8BCJ1C2H5DzrAfk6eC-K__hjuCkV3F2wvn-wcMTA7Hxw8cEmlXQ36uuYA-Svvt7S-kwAQ");
		jwkData.put("e", "AQAB");
		jwkData.put("use", "sig");
		jwkData.put("kid", "2IsGMGIj8G");
		jwkData.put("qi",
				"sBzjBkf-CHlTCbEtyLC1StyT7Z1sZRaclixSj1RqUErA7BSoIPj-1PjTd8er_MTAAHhYIm8c7jtbwsBx-AsXjipx2WAerB-rZCmT59U07IwxCGJv3VJCI6LHsy-GBXP0P5B51wjEaZ3U_TmzMNsPtSQpaHalguhHkoxTf5gFHUg");
		jwkData.put("dp",
				"S8Rln74VQfc1H7lUP6Man-s-LvNwC7kBlcrvMuC7D6n8IE1MeTHTv3MmYCan7cSm3aVk93oIJ-7HGgL2JIQhX1pKX0dDcae0VHBULpt5w7-N8bbaozY1F7vO4uim81wLLOSXIjfyMsAJ084DnKq0pwzt4eadmagoSClBZvFx6WU");
		jwkData.put("alg", "RS256");
		jwkData.put("dq",
				"eOH5KflxAfiAEz9MfdgRpHLKQ4b6egErKD9gw_yi9JGg6mHp7dbGWaohvwDTOe3HcTGASK8Ws8J4-yBfgIfdprvKndGsEkC6K4WbtCk_gDXVrQvpGGufzdALW1CIsReXMmnSFUA0NKaoZNeqqwIq4LreztSIvMpw97UHe0BjmAE");
		jwkData.put("n",
				"iiXyv8vVQK-EmKKlKuCi1wf-64gxrFhFG29_pnOrumRy6TgT2jkIcxZQRqM0JE2URBt9psPZkTwSeV9FCcI_MCWSX6xGq_U_zWBc8Qc73n-D2fdVkYvxIJEsL_vXXJg6rrL6FUe0OjB1KGI67spuk1gaDRRvrb0WPak8dQzqlqypr0LQD-FBlNb8QPMTFAEMKEVaFlH4Hwu2Gg6sHEkDzYwWz4XC5R0DwWdc-UEIXzHBdYwGU_bIUGAREG--qs9o3M1ZiSTFMBD33hutAhQa0eOt9mf646gnQebGYVgjjEWn3-rT4clLmb8GPApecPeNeRvaSmFZCSJeRdGTjLSXJw");

		jwks.setId(1);
		jwks.setJwk(jwkData.toString());
		jwks.setLastUpdatedDatetime(new Date());
	
		when(sharedSessionContract.createCriteria(Jwks.class)).thenReturn(criteria);
		criteria.add(Restrictions.eq("id", Mockito.anyInt()));
		jwksDaoImpl.updateById(1, jwkData.toString());

	}
	
	@Test
	void saveOrUpdateByException() {
		
		Jwks jwks = mock(Jwks.class);
		doThrow(RuntimeException.class).when(jwks).getId();
		jwks.setId(0);
		assertNull(jwksDaoImpl.saveOrUpdate(jwks));
	}
	
	@Test
	void getByIdByException() {
		
		Jwks jwks = mock(Jwks.class);
		doThrow(RuntimeException.class).when(jwks).getId();
		jwks.setId(0);
		assertNull(jwksDaoImpl.getById(0));
	}
}
