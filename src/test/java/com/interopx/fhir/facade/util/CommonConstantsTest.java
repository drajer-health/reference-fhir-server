package com.interopx.fhir.facade.util;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;

import com.interopx.fhir.facade.util.ProfileConstants.CommonProfiles;
import com.interopx.fhir.facade.util.ProfileConstants.UsCoreProfiles;
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class CommonConstantsTest {

	@Test
	void testsd() {
		ProfileConstants pc = new ProfileConstants();
		UsCoreProfiles uc = new UsCoreProfiles();
		CommonProfiles ccp = new CommonProfiles();		
		Status.ABORTED.toString();		
	}
}
