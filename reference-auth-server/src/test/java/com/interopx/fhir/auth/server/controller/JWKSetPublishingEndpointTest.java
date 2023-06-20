package com.interopx.fhir.auth.server.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import net.minidev.json.JSONObject;

@SpringBootTest
class JWKSetPublishingEndpointTest {

	@InjectMocks
	JWKSetPublishingEndpoint jWKSetPublishingEndpoint;
	
	@Mock
	JwtGenerator jwtGenerator;
	
	@Test
	void getJwk() {
		Map<String, List<JSONObject>> keys = new HashMap();

		JSONObject obj=new JSONObject();
		obj.put("kty", "RSA");
		obj.put("e", "AQAB");
		obj.put("use", "sig");
		obj.put("kid", "2IsGMGIj8G");
		obj.put("alg", "RS256");
		obj.put("n",
				"iiXyv8vVQK-EmKKlKuCi1wf-64gxrFhFG29_pnOrumRy6TgT2jkIcxZQRqM0JE2URBt9psPZkTwSeV9FCcI_MCWSX6xGq_U_zWBc8Qc73n-D2fdVkYvxIJEsL_vXXJg6rrL6FUe0OjB1KGI67spuk1gaDRRvrb0WPak8dQzqlqypr0LQD-FBlNb8QPMTFAEMKEVaFlH4Hwu2Gg6sHEkDzYwWz4XC5R0DwWdc-UEIXzHBdYwGU_bIUGAREG--qs9o3M1ZiSTFMBD33hutAhQa0eOt9mf646gnQebGYVgjjEWn3-rT4clLmb8GPApecPeNeRvaSmFZCSJeRdGTjLSXJw");

		List<JSONObject> list = new ArrayList<>();
		list.add(obj);
		
		keys.put("key", list);
		System.out.println(keys);
		
		when(jwtGenerator.getAllPublicKeys()).thenReturn(keys);
		assertEquals(keys, jWKSetPublishingEndpoint.getJwk());

	}
	
	@Test
	void getJwkForException() {
		Map<String, List<JSONObject>> keys = new HashMap();
		
		doThrow(RuntimeException.class).when(jwtGenerator).getAllPublicKeys();
		keys.put("kty", null);
		assertNull(jWKSetPublishingEndpoint.getJwk());

	}
}
