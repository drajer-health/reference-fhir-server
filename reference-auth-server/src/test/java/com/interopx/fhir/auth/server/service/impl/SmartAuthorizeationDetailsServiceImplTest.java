package com.interopx.fhir.auth.server.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.interopx.fhir.auth.server.dao.SmartAuthorizeationDetailsDao;
import com.interopx.fhir.auth.server.model.SmartAuthorizeationDetails;

@SpringBootTest
class SmartAuthorizeationDetailsServiceImplTest {

	@InjectMocks
	SmartAuthorizeationDetailsServiceImpl smartAuthorizeationDetailsServiceImpl;
	
	@Mock
	SmartAuthorizeationDetailsDao smartAuthorizeationDetailsDao;
	
	@Test
	void getAuthorizationDetailsById() {

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
		when(smartAuthorizeationDetailsDao.getAuthorizationDetailsById(1)).thenReturn(smartAuthorizeationDetails);		
		assertEquals(smartAuthorizeationDetails, smartAuthorizeationDetailsServiceImpl.getAuthorizationDetailsById(1));
		
		
	
	}

}
