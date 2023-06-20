package com.interopx.fhir.auth.server.keystore;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;

@SpringBootTest
class JWKSStoreTest {

	
	
	@Mock
	JWK jwk;
	@Mock
	JWK JwkSet;
	
	@Mock
	JWKSet jwkSet;
	@Mock
	Resource location;
//	
//	@InjectMocks
//	JWKSStore jwksstore;

	
//	@Test
//	void testJWKSStore() {
//		
//		JWKSStore JWKSStore=new JWKSStore();
//		
//	}

	@Test
	void testJWKSStoreJWKSet() {
		
		JWKSStore JWKSStore=new JWKSStore(jwkSet);
	}

	@Test
	void testGetJwkSet() {
		
		JWKSStore JWKSStore=new JWKSStore();
		JWKSStore.getJwkSet();
	}

	@Test
	void testSetJwkSet() {
		
		 JWKSStore jwksstore=new JWKSStore();
		 jwksstore.setJwkSet(jwkSet);
		
	}

	@Test
	void testGetLocation() {

	     JWKSStore jwksstore=new JWKSStore();
		 Resource resource=jwksstore.getLocation();
	}
	
	
	

//	@Test
//	void testSetLocation() {
//		
//		JWKSStore jwksstore=new JWKSStore();
//		jwksstore.setLocation(location);
//	}

//	@Test
//	void testGetKeys() {
//		JWKSStore jwksstore=new JWKSStore();
////		List<JWK> publicKeyList = new LinkedList<>();
////		publicKeyList.
//		
////		List<JWK> publicKeyList = new LinkedList<>();
////		publicKeyList.size();
////		publicKeyList.add(JwkSet);
//		
////		JWKSet JwkSet=new JWKSet();
////		JwkSet.toPublicJWKSet();
//		jwksstore.getKeys();
//		
	//}
}
