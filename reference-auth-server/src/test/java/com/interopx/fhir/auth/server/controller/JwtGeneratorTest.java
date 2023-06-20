package com.interopx.fhir.auth.server.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;

import com.interopx.fhir.auth.server.model.Jwks;
import com.interopx.fhir.auth.server.service.JwksService;
import com.interopx.fhir.auth.server.util.CommonUtil;
import com.nimbusds.jose.JOSEException;

@SpringBootTest
class JwtGeneratorTest {

	@InjectMocks
	JwtGenerator jwtGenerator;

	@Mock
	JwksService jwksService;
	
	/** @return This method will return current date time */
	
	@Test
	void generate() throws KeyStoreException, NoSuchAlgorithmException, InvalidKeySpecException, JOSEException,
			ParseException, IOException, ParserConfigurationException, JSONException {

		MockHttpServletRequest request = new MockHttpServletRequest();
		
		String timeStamp =
                new SimpleDateFormat("EE MMM dd HH:mm:ss z yyyy")
                    .format(new Timestamp(System.currentTimeMillis()));
            Date issueDate = CommonUtil.convertToDateFormat(timeStamp);
            Date expiryTime = new Date(issueDate.getTime() + 2 * (3600 * 1000));

		Map<String, Object> payloadData = new HashMap<>();
		payloadData.put("sub", "1234567890");
		payloadData.put("aud", "arcadia");
		payloadData.put("email", "arcadia@gmail.com");
		payloadData.put("issueDate", issueDate);
		payloadData.put("expiryTime", expiryTime);
		payloadData.put("userName", "Arcadia");
		
		Map<String, Object> jwkData = new HashMap<>();
		jwkData.put("p","wmhKSipECN1fWGeicAwhTkGHjI393eSr0Tgd90yxcitLxkiSeg8-dEreLScRvpPcgKdyDA6raHIrRBMqOv8TqMyPinhXAZ16BJOzanUwcSZIngGG6AOceVLp_1BXMprGqNH0uPhJZKuid23SryPVmfwUmE9f9cthUtz0y-41Tyc");
		jwkData.put("kty","EC");
		jwkData.put("q","teqsXf_va1rgAI-GvxBwYA5iAdRhzKfZEc8V6r10rSicb98bKa0mgyCRppqmt4TX05yRrjQpYJ52peK6pUlAlAEE3vWBn_x_umXkasILkMmwPx7GQhDrYYo9AKzlhvb0bOXrmKuorMu6rbVIPnseBsU-CIsa0b_gaQAnaSKTeAE");
		jwkData.put("d","QTnfnn0ACBWW9nY8o70CRaLJ0q0wPt5Casy_KXkNyOiqdMD8ZpltCuGrL17ZPJbAfVO5hOaNsAv78QBxWUU0jmf-71so1wHpiO-JkmfG9auhNmWYKis3wxeoFFPFADwD4P4yI6ACUUMV3eZbSumbQchkLG7Mt-xCNwTTvbRGgrL7kEeIwuHHx4L1Ht7K2sXjOkdFK4Yo6BjuScWxiNSeqWwdo0HgnFohwOkqCWVbIxsPVkazG5fAq3j9f4O3sinTyLAqdckCOFhojUROu8BCJ1C2H5DzrAfk6eC-K__hjuCkV3F2wvn-wcMTA7Hxw8cEmlXQ36uuYA-Svvt7S-kwAQ");
		jwkData.put("e","AQAB");
		jwkData.put("use","sig");
		jwkData.put("kid","2IsGMGIj8G");
		jwkData.put("qi","sBzjBkf-CHlTCbEtyLC1StyT7Z1sZRaclixSj1RqUErA7BSoIPj-1PjTd8er_MTAAHhYIm8c7jtbwsBx-AsXjipx2WAerB-rZCmT59U07IwxCGJv3VJCI6LHsy-GBXP0P5B51wjEaZ3U_TmzMNsPtSQpaHalguhHkoxTf5gFHUg");
		jwkData.put("dp","S8Rln74VQfc1H7lUP6Man-s-LvNwC7kBlcrvMuC7D6n8IE1MeTHTv3MmYCan7cSm3aVk93oIJ-7HGgL2JIQhX1pKX0dDcae0VHBULpt5w7-N8bbaozY1F7vO4uim81wLLOSXIjfyMsAJ084DnKq0pwzt4eadmagoSClBZvFx6WU");
		jwkData.put("alg","RS256");
		jwkData.put("dq","eOH5KflxAfiAEz9MfdgRpHLKQ4b6egErKD9gw_yi9JGg6mHp7dbGWaohvwDTOe3HcTGASK8Ws8J4-yBfgIfdprvKndGsEkC6K4WbtCk_gDXVrQvpGGufzdALW1CIsReXMmnSFUA0NKaoZNeqqwIq4LreztSIvMpw97UHe0BjmAE");
		jwkData.put("n","iiXyv8vVQK-EmKKlKuCi1wf-64gxrFhFG29_pnOrumRy6TgT2jkIcxZQRqM0JE2URBt9psPZkTwSeV9FCcI_MCWSX6xGq_U_zWBc8Qc73n-D2fdVkYvxIJEsL_vXXJg6rrL6FUe0OjB1KGI67spuk1gaDRRvrb0WPak8dQzqlqypr0LQD-FBlNb8QPMTFAEMKEVaFlH4Hwu2Gg6sHEkDzYwWz4XC5R0DwWdc-UEIXzHBdYwGU_bIUGAREG--qs9o3M1ZiSTFMBD33hutAhQa0eOt9mf646gnQebGYVgjjEWn3-rT4clLmb8GPApecPeNeRvaSmFZCSJeRdGTjLSXJw");
		
		String date = "2022-08-10 00:15:32";
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    Date date1 = sdf.parse(date);  
		
		Timestamp timestamp = new Timestamp(date1.getTime());
				
		JSONObject jsonObj = new JSONObject(jwkData.toString());
		
		Jwks jwks = new Jwks();

		jwks.setId(1);
		jwks.setLastUpdatedDatetime(timestamp);
		jwks.setJwk(jsonObj.toString());
				
		when(jwksService.getById(1)).thenReturn(jwks);

//		MockedStatic<CommonUtil> commonUtil = Mockito.mockStatic(CommonUtil.class);
		
//		commonUtil.when(() -> CommonUtil.convertTimestampToUnixTime(Mockito.any())).thenReturn(1163047913);		
		
		assertNotNull(jwtGenerator.generate(payloadData, request));

	}
	
	@Test 
	void getAllPublicKeys() throws JSONException {
		
		Map<String, Object> jwkData = new HashMap<>();
		jwkData.put("p","wmhKSipECN1fWGeicAwhTkGHjI393eSr0Tgd90yxcitLxkiSeg8-dEreLScRvpPcgKdyDA6raHIrRBMqOv8TqMyPinhXAZ16BJOzanUwcSZIngGG6AOceVLp_1BXMprGqNH0uPhJZKuid23SryPVmfwUmE9f9cthUtz0y-41Tyc");
		jwkData.put("kty","RSA");
		jwkData.put("q","teqsXf_va1rgAI-GvxBwYA5iAdRhzKfZEc8V6r10rSicb98bKa0mgyCRppqmt4TX05yRrjQpYJ52peK6pUlAlAEE3vWBn_x_umXkasILkMmwPx7GQhDrYYo9AKzlhvb0bOXrmKuorMu6rbVIPnseBsU-CIsa0b_gaQAnaSKTeAE");
		jwkData.put("d","QTnfnn0ACBWW9nY8o70CRaLJ0q0wPt5Casy_KXkNyOiqdMD8ZpltCuGrL17ZPJbAfVO5hOaNsAv78QBxWUU0jmf-71so1wHpiO-JkmfG9auhNmWYKis3wxeoFFPFADwD4P4yI6ACUUMV3eZbSumbQchkLG7Mt-xCNwTTvbRGgrL7kEeIwuHHx4L1Ht7K2sXjOkdFK4Yo6BjuScWxiNSeqWwdo0HgnFohwOkqCWVbIxsPVkazG5fAq3j9f4O3sinTyLAqdckCOFhojUROu8BCJ1C2H5DzrAfk6eC-K__hjuCkV3F2wvn-wcMTA7Hxw8cEmlXQ36uuYA-Svvt7S-kwAQ");
		jwkData.put("e","AQAB");
		jwkData.put("use","sig");
		jwkData.put("kid","2IsGMGIj8G");
		jwkData.put("qi","sBzjBkf-CHlTCbEtyLC1StyT7Z1sZRaclixSj1RqUErA7BSoIPj-1PjTd8er_MTAAHhYIm8c7jtbwsBx-AsXjipx2WAerB-rZCmT59U07IwxCGJv3VJCI6LHsy-GBXP0P5B51wjEaZ3U_TmzMNsPtSQpaHalguhHkoxTf5gFHUg");
		jwkData.put("dp","S8Rln74VQfc1H7lUP6Man-s-LvNwC7kBlcrvMuC7D6n8IE1MeTHTv3MmYCan7cSm3aVk93oIJ-7HGgL2JIQhX1pKX0dDcae0VHBULpt5w7-N8bbaozY1F7vO4uim81wLLOSXIjfyMsAJ084DnKq0pwzt4eadmagoSClBZvFx6WU");
		jwkData.put("alg","RS256");
		jwkData.put("dq","eOH5KflxAfiAEz9MfdgRpHLKQ4b6egErKD9gw_yi9JGg6mHp7dbGWaohvwDTOe3HcTGASK8Ws8J4-yBfgIfdprvKndGsEkC6K4WbtCk_gDXVrQvpGGufzdALW1CIsReXMmnSFUA0NKaoZNeqqwIq4LreztSIvMpw97UHe0BjmAE");
		jwkData.put("n","iiXyv8vVQK-EmKKlKuCi1wf-64gxrFhFG29_pnOrumRy6TgT2jkIcxZQRqM0JE2URBt9psPZkTwSeV9FCcI_MCWSX6xGq_U_zWBc8Qc73n-D2fdVkYvxIJEsL_vXXJg6rrL6FUe0OjB1KGI67spuk1gaDRRvrb0WPak8dQzqlqypr0LQD-FBlNb8QPMTFAEMKEVaFlH4Hwu2Gg6sHEkDzYwWz4XC5R0DwWdc-UEIXzHBdYwGU_bIUGAREG--qs9o3M1ZiSTFMBD33hutAhQa0eOt9mf646gnQebGYVgjjEWn3-rT4clLmb8GPApecPeNeRvaSmFZCSJeRdGTjLSXJw");
		
		JSONObject jsonObj = new JSONObject(jwkData.toString());
		
		Jwks jwks = new Jwks();

		jwks.setId(1);
		jwks.setJwk(jsonObj.toString());
		
		when(jwksService.getById(1)).thenReturn(jwks);
		
	    List<JSONObject> publicKeyList = new ArrayList<>();
	    Map<String, List<JSONObject>> expectedKeys = new HashMap<>();
		
		Map<String, Object> data = new HashMap<>();
		data.put("kty","RSA");
		data.put("e","AQAB");
		data.put("use","sig");
		data.put("kid","2IsGMGIj8G");
		data.put("alg","RS256");
		data.put("n","iiXyv8vVQK-EmKKlKuCi1wf-64gxrFhFG29_pnOrumRy6TgT2jkIcxZQRqM0JE2URBt9psPZkTwSeV9FCcI_MCWSX6xGq_U_zWBc8Qc73n-D2fdVkYvxIJEsL_vXXJg6rrL6FUe0OjB1KGI67spuk1gaDRRvrb0WPak8dQzqlqypr0LQD-FBlNb8QPMTFAEMKEVaFlH4Hwu2Gg6sHEkDzYwWz4XC5R0DwWdc-UEIXzHBdYwGU_bIUGAREG--qs9o3M1ZiSTFMBD33hutAhQa0eOt9mf646gnQebGYVgjjEWn3-rT4clLmb8GPApecPeNeRvaSmFZCSJeRdGTjLSXJw");
		
		JSONObject jsonObject = new JSONObject(data.toString());
		publicKeyList.add(jsonObject);
		expectedKeys.put("keys", publicKeyList);
				
		assertEquals(expectedKeys.toString(), jwtGenerator.getAllPublicKeys().toString());
	}
	
	@Test
	void getAllPublicKeysException() throws JSONException {
		
		Map<String, Object> jwkData = new HashMap<>();
		jwkData.put("p","wmhKSipECN1fWGeicAwhTkGHjI393eSr0Tgd90yxcitLxkiSeg8-dEreLScRvpPcgKdyDA6raHIrRBMqOv8TqMyPinhXAZ16BJOzanUwcSZIngGG6AOceVLp_1BXMprGqNH0uPhJZKuid23SryPVmfwUmE9f9cthUtz0y-41Tyc");
		jwkData.put("kty","EC");
		jwkData.put("q","teqsXf_va1rgAI-GvxBwYA5iAdRhzKfZEc8V6r10rSicb98bKa0mgyCRppqmt4TX05yRrjQpYJ52peK6pUlAlAEE3vWBn_x_umXkasILkMmwPx7GQhDrYYo9AKzlhvb0bOXrmKuorMu6rbVIPnseBsU-CIsa0b_gaQAnaSKTeAE");
		jwkData.put("d","QTnfnn0ACBWW9nY8o70CRaLJ0q0wPt5Casy_KXkNyOiqdMD8ZpltCuGrL17ZPJbAfVO5hOaNsAv78QBxWUU0jmf-71so1wHpiO-JkmfG9auhNmWYKis3wxeoFFPFADwD4P4yI6ACUUMV3eZbSumbQchkLG7Mt-xCNwTTvbRGgrL7kEeIwuHHx4L1Ht7K2sXjOkdFK4Yo6BjuScWxiNSeqWwdo0HgnFohwOkqCWVbIxsPVkazG5fAq3j9f4O3sinTyLAqdckCOFhojUROu8BCJ1C2H5DzrAfk6eC-K__hjuCkV3F2wvn-wcMTA7Hxw8cEmlXQ36uuYA-Svvt7S-kwAQ");
		jwkData.put("e","AQAB");
		jwkData.put("use","sig");
		jwkData.put("kid","2IsGMGIj8G");
		jwkData.put("qi","sBzjBkf-CHlTCbEtyLC1StyT7Z1sZRaclixSj1RqUErA7BSoIPj-1PjTd8er_MTAAHhYIm8c7jtbwsBx-AsXjipx2WAerB-rZCmT59U07IwxCGJv3VJCI6LHsy-GBXP0P5B51wjEaZ3U_TmzMNsPtSQpaHalguhHkoxTf5gFHUg");
		jwkData.put("dp","S8Rln74VQfc1H7lUP6Man-s-LvNwC7kBlcrvMuC7D6n8IE1MeTHTv3MmYCan7cSm3aVk93oIJ-7HGgL2JIQhX1pKX0dDcae0VHBULpt5w7-N8bbaozY1F7vO4uim81wLLOSXIjfyMsAJ084DnKq0pwzt4eadmagoSClBZvFx6WU");
		jwkData.put("alg","RS256");
		jwkData.put("dq","eOH5KflxAfiAEz9MfdgRpHLKQ4b6egErKD9gw_yi9JGg6mHp7dbGWaohvwDTOe3HcTGASK8Ws8J4-yBfgIfdprvKndGsEkC6K4WbtCk_gDXVrQvpGGufzdALW1CIsReXMmnSFUA0NKaoZNeqqwIq4LreztSIvMpw97UHe0BjmAE");
		jwkData.put("n","iiXyv8vVQK-EmKKlKuCi1wf-64gxrFhFG29_pnOrumRy6TgT2jkIcxZQRqM0JE2URBt9psPZkTwSeV9FCcI_MCWSX6xGq_U_zWBc8Qc73n-D2fdVkYvxIJEsL_vXXJg6rrL6FUe0OjB1KGI67spuk1gaDRRvrb0WPak8dQzqlqypr0LQD-FBlNb8QPMTFAEMKEVaFlH4Hwu2Gg6sHEkDzYwWz4XC5R0DwWdc-UEIXzHBdYwGU_bIUGAREG--qs9o3M1ZiSTFMBD33hutAhQa0eOt9mf646gnQebGYVgjjEWn3-rT4clLmb8GPApecPeNeRvaSmFZCSJeRdGTjLSXJw");
		
		JSONObject jsonObj = new JSONObject(jwkData.toString());
		
		Map<String, Object> expectedData = new HashMap<>();
		
		Jwks jwks = new Jwks();

		jwks.setId(1);
		jwks.setJwk(jsonObj.toString());
		
		when(jwksService.getById(1)).thenReturn(jwks);
		
		assertEquals(expectedData.toString(), jwtGenerator.getAllPublicKeys().toString());
	}
	
}
