package com.interopx.fhir.auth.server.controller;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.mock.web.MockHttpServletRequest;

import com.interopx.fhir.auth.server.model.SmartAuthorizeationDetails;
import com.interopx.fhir.auth.server.service.SmartAuthorizeationDetailsService;

@SpringBootTest
class SMARTControllerTest {

	@InjectMocks
	SMARTController smartController;

	@Mock
	SmartAuthorizeationDetailsService smartAuthorizeationDetailsService;

	@Test
	void getAuthorization() throws Exception {

		MockHttpServletRequest request = new MockHttpServletRequest();

		HttpServletResponse response = Mockito.mock(HttpServletResponse.class);

		SmartAuthorizeationDetails smartAuthorizeationDetails = new SmartAuthorizeationDetails();

		List<String> algList = new ArrayList();
		algList.add("{RS256}");

		List<String> claimsList = new ArrayList();
		claimsList.add("{sub,name,profile,email}");

		List<String> responseList = new ArrayList();
		responseList.add("{code}");

		List<String> subjectList = new ArrayList();
		subjectList.add("{public}");

		smartAuthorizeationDetails.setId(1);
		smartAuthorizeationDetails.setIsClaimsParamSupported(false);
		smartAuthorizeationDetails.setIsRequestParamSupported(false);
		smartAuthorizeationDetails.setIsRequestUriParamSupported(true);
		smartAuthorizeationDetails.setIsRequestUriRegistration(true);
		smartAuthorizeationDetails.setAlgorithmTypes(algList);
		smartAuthorizeationDetails.setClaims(claimsList);
		smartAuthorizeationDetails.setResponseTypes(responseList);
		smartAuthorizeationDetails.setSubjectTypes(subjectList);

		when(smartAuthorizeationDetailsService.getAuthorizationDetailsById(Mockito.anyInt())).thenReturn(smartAuthorizeationDetails);

		assertNotNull(smartController.getAuthorization(request, response));
	}

}
