package com.interopx.fhir.auth.server.jwtservice;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.text.ParseException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.google.common.base.Strings;
import com.interopx.fhir.auth.server.keystore.JWKSStore;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.util.JSONObjectUtils;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import net.minidev.json.parser.JSONParser;


@RunWith(SpringRunner.class)
@SpringBootTest
class JWTStoredKeyServiceImplTest {

	
	@InjectMocks
	JWTStoredKeyServiceImpl JWTStoredKeyserviceImpl;
	
	
	@Mock
	JWKSStore keyStore;
	@Mock
	JWKSet jwkSet;
	@Mock
	SignedJWT signedJwt;
	@Mock
	JWSSigner signer;
	@Mock
	JWK jwk;
	@Mock
	JSONParser JSONparser;
	
	@Mock
	JSONObjectUtils JSONobject;
	
	
	
	@Test
	void testJWTStoredKeyServiceImplMapOfStringJWK() throws NoSuchAlgorithmException, InvalidKeySpecException {
		
		 Map<String, JWK> keys= new HashMap<String, JWK>();
		 JWKSStore keyStore=new JWKSStore();
		 JWTStoredKeyServiceImpl JWTStoredKeyserviceImpl=new  JWTStoredKeyServiceImpl(keys);

	}

//	@Test
//	void testJWTStoredKeyServiceImplJWKSStore() {
//
//	}

	@Test
	void testGetDefaultSignerKeyId() {
		
		String str=JWTStoredKeyserviceImpl.getDefaultSignerKeyId();
	}

	@Test
	void testSetDefaultSignerKeyId() {
		
		String str="kjfsdfkj94789";
		JWTStoredKeyserviceImpl.setDefaultSignerKeyId(str);
		
	}

	@Test
	void testGetDefaultSigningAlgorithm() {
		
		String str="kjfsdfkj94789";
		JWTStoredKeyserviceImpl.getDefaultSigningAlgorithm();

	}

	@Test
	void testSetDefaultSigningAlgorithmName() {
		String algName="vtbx746";
		JWTStoredKeyserviceImpl.setDefaultSigningAlgorithmName(algName);
	}

	@Test
	void testGetDefaultSigningAlgorithmName() {
		
		JWSAlgorithm defaultAlgorithm=new JWSAlgorithm("value");
		JWTStoredKeyserviceImpl.getDefaultSigningAlgorithmName();
	}

	@Test
	void testSignJwtSignedJWT() {
		
		 String defaultSignerKeyId="kjfsdfkj94789";
		 JWTStoredKeyserviceImpl.setDefaultSignerKeyId(defaultSignerKeyId);
		 JWTStoredKeyserviceImpl.signJwt(signedJwt);
		
	}

	@Test
	void testSignJwtSignedJWT1() throws ParseException {
		
		JWSAlgorithm alg = null;
		 JWTStoredKeyserviceImpl.signJwt(signedJwt, alg);
		
	}

	@Test
	void testValidateSignature() {
		boolean value=	JWTStoredKeyserviceImpl.validateSignature(signedJwt);
	    assertThat(value).isNotNull();
	}

	@Test
	void testGetAllPublicKeys() {
		
		Map<String, JWK> keys = new HashMap<>();
		keys.put("hari", jwk);
		for (String keyId : keys.keySet()) {
		JWK key = keys.get(keyId);
		JWK pub = key.toPublicJWK();
		        if (pub != null) {
		          keys.put(keyId, pub);
		   }
		 }
		JWTStoredKeyserviceImpl.getAllPublicKeys();
		
	}

	@Test
	void testGetAllSigningAlgsSupported() {

        JWSAlgorithm HS256 = JWSAlgorithm.HS256;
        JWSAlgorithm jWSAlgorithm1 = JWSAlgorithm.RS256;
        JWSAlgorithm jWSAlgorithm2 = JWSAlgorithm.ES256;
        Set<JWSAlgorithm> algs = new HashSet<>();
       
        algs.add(HS256);
        algs.add(jWSAlgorithm1);
        algs.add(jWSAlgorithm2);
        assertEquals(algs.contains(jWSAlgorithm1), JWTStoredKeyserviceImpl.getAllSigningAlgsSupported().add(jWSAlgorithm1));
        	}

}