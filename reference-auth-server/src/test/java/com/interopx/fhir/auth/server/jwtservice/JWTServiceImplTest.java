package com.interopx.fhir.auth.server.jwtservice;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.text.ParseException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.Requirement;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.util.Base64URL;
import com.nimbusds.jwt.SignedJWT;



@RunWith(SpringRunner.class)
@SpringBootTest
class JWTServiceImplTest {

	
	@InjectMocks
	JWTServiceImpl JWTserviceImpl;
	@Mock
	SignedJWT  signedJWT;
	
	
	@Before(value = "true")
	public void setUp() {
	    MockitoAnnotations.initMocks(this);
	}
	
	@Test
	void testJWTServiceImplMapOfStringJWK() throws NoSuchAlgorithmException, InvalidKeySpecException {


		 Map<String, JWK> keys= new HashMap<String, JWK>();
		 JWTServiceImpl JwtServiceImpl=new JWTServiceImpl(keys);
		
	}

//	@Test
//	void testJWTServiceImplJWKSStore() {
//		fail("Not yet implemented");
//	}

	@Test
	void testGetDefaultSignerKeyId() {
		String str=JWTserviceImpl.getDefaultSignerKeyId();
	}

	@Test
	void testSetDefaultSignerKeyId() {

		String defaultSignerKeyId="kjfsdfkj94789";
		JWTserviceImpl.setDefaultSignerKeyId(defaultSignerKeyId);
	}

	@Test
	void testGetDefaultSigningAlgorithm() {
		
		JWSAlgorithm defaultAlgorithm=new JWSAlgorithm("kjfsdfkj94789");
		JWSAlgorithm defaultAlgorithm1=JWTserviceImpl.getDefaultSigningAlgorithm();
		
	}

	@Test
	void testSetDefaultSigningAlgorithmName() {
		
		String algName="SystemName";
		JWTserviceImpl.setDefaultSigningAlgorithmName(algName);
	}

	@Test
	void testGetDefaultSigningAlgorithmName() {
		
		JWSAlgorithm defaultAlgorithm=new JWSAlgorithm("kjfsdfkj94789");
		String string=JWTserviceImpl.getDefaultSigningAlgorithmName();
	}

	@Test
	void testSignJwtSignedJWT() throws ParseException {
	
		 
	   String defaultSignerKeyId="kjfsdfkj94789";
	   JWTserviceImpl.setDefaultSignerKeyId(defaultSignerKeyId);
	   JWTserviceImpl.signJwt(signedJWT);
	}

//	@Test
//	void testSignJwtSignedJWTJWSAlgorithm() {
//		fail("Not yet implemented");
//	}
//
	@Test
	void testSignJwtSignedJWT1() throws ParseException {
		
		JWSAlgorithm alg = null;
		JWTserviceImpl.signJwt(signedJWT, alg);
		
	}
	@Test
	void testValidateSignature() {
		boolean value=	JWTserviceImpl.validateSignature(signedJWT);
	    assertThat(value).isNotNull();
	}
//
	@Test
	void testGetAllPublicKeys() {
	    JWTserviceImpl.getAllPublicKeys();
	}
	@Test
	void testGetAllSigningAlgsSupported() {
		Set<JWSAlgorithm> algs = new HashSet<>();
		Collection<JWSAlgorithm> c=JWTserviceImpl.getAllSigningAlgsSupported();
		
	}
}
