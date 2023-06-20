package com.interopx.fhir.auth.server.util;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Random;


import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.interopx.fhir.auth.server.util.AuthUtil.ApprovedStatus;


@RunWith(SpringRunner.class)
@SpringBootTest
class AuthUtilTest {


	
	@Test
	void testGenerateToken() {
		
		String str=AuthUtil.generateToken();
		//String token= "y7ZVFSW536cflQEMUONCez929GJsBo";
		 String TOKEN_CHARS =
			      "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
			   int TOKEN_LENGTH = 30;
			    StringBuilder token = new StringBuilder();
			    try {
			      Random rnd = new Random();
			      while (token.length() < TOKEN_LENGTH) {
			        int index = (int) (rnd.nextFloat() * TOKEN_CHARS.length());
			        token.append(TOKEN_CHARS.charAt(index));
			      }
			    } catch (Exception e) {	      
		assertEquals(token.toString(), str.toString());		
	      }	
	
	}

	@Test
	void testGetEnumValue() {
		
		String value="APPROVED";
		ApprovedStatus Approvedstatus11=AuthUtil.getEnumValue(value);
		assertEquals(value.toString(), Approvedstatus11.toString());
		String value1="PENDING";
		ApprovedStatus Approvedstatus12=AuthUtil.getEnumValue(value1);
		assertEquals(value1.toString(), Approvedstatus12.toString());
		String value2="REJECTED";
		ApprovedStatus Approvedstatus13=AuthUtil.getEnumValue(value2);
		assertEquals(value2.toString(), Approvedstatus13.toString());
		
//		ApprovedStatus Approvedstatus=EmailUtil.ApprovedStatus.APPROVED;
//		ApprovedStatus Approvedstatus1=EmailUtil.ApprovedStatus.PENDING;
//		ApprovedStatus Approvedstatus2=EmailUtil.ApprovedStatus.REJECTED;
		
		
	}

}
