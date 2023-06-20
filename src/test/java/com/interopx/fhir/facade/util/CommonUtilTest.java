package com.interopx.fhir.facade.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.hl7.fhir.r4.model.AllergyIntolerance;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.Immunization;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.StringType;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;

import ca.uhn.fhir.rest.param.DateAndListParam;
import ca.uhn.fhir.rest.param.DateOrListParam;
import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.param.ReferenceAndListParam;
import ca.uhn.fhir.rest.param.ReferenceOrListParam;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.StringAndListParam;
import ca.uhn.fhir.rest.param.StringOrListParam;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.TokenAndListParam;
import ca.uhn.fhir.rest.param.TokenOrListParam;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class CommonUtilTest {

	@Test
	void testGetListOfStringParamSearchParameterValue() {

		StringAndListParam slp = new StringAndListParam();
		StringOrListParam slop = new StringOrListParam();
		StringParam sp = new StringParam();
		sp.setValue("true");
		slop.add(sp);
		slp.addValue(slop);

		SearchParameterMap paramMap = new SearchParameterMap();
		paramMap.add("de-dup", slp);

		assertNotNull(CommonUtil.getListOfStringParamSearchParameterValue(paramMap, "de-dup"));
	}

	@Test
	void testGetPathParams() {
		String URL = "http://localhost:8089/fhir/r4/1005/Encounter?patient=example1&date=ge2019-10-01";
		StringBuffer sb = new StringBuffer(URL);
		assertNotNull(CommonUtil.getPathParams(sb));
	}

	@Test
	void testGetOperationOutcome() {

		assertNotNull(CommonUtil.getOperationOutcome("test"));
	}

	@Test
	void testGenerateId() {

		assertNotNull(CommonUtil.generateId());
	}

	@Test
	void testGetIdentifierSearchParameterValue() {
		TokenAndListParam theIdentifier = new TokenAndListParam();
		TokenOrListParam tolp = new TokenOrListParam();
		tolp.add("http://hospital.smarthealthit.org", "1032702");
		theIdentifier.addValue(tolp);

		SearchParameterMap paramMap = new SearchParameterMap();
		paramMap.add(AllergyIntolerance.SP_IDENTIFIER, theIdentifier);

		assertNotNull(CommonUtil.getIdentifierSearchParameterValue(paramMap));
	}

	@Test
	void testGetIdentifierSearchParameterValue2() {
		TokenAndListParam theIdentifier = new TokenAndListParam();
		TokenOrListParam tolp = new TokenOrListParam();
		tolp.add("http://hospital.smarthealthit.org", "1032702");
		theIdentifier.addValue(tolp);

		SearchParameterMap paramMap = new SearchParameterMap();
		paramMap.add(AllergyIntolerance.SP_IDENTIFIER, theIdentifier);

		assertNotNull(CommonUtil.getIdentifierSearchParameterValue(paramMap, "dentifier"));
	}

	@Test
	void testGetIdFromSearchById() {
		TokenAndListParam theIdentifier = new TokenAndListParam();
		TokenOrListParam tolp = new TokenOrListParam();
		tolp.add("http://hospital.smarthealthit.org", "1032702");
		theIdentifier.addValue(tolp);

		SearchParameterMap paramMap = new SearchParameterMap();
		paramMap.add(AllergyIntolerance.SP_IDENTIFIER, theIdentifier);

		assertNotNull(CommonUtil.getIdFromSearchById(paramMap, "identifier"));
	}

	@Test
	void testGetListOfTokenParamSearchParameterValue() {
		TokenAndListParam theIdentifier = new TokenAndListParam();
		TokenOrListParam tolp = new TokenOrListParam();
		tolp.add("http://hospital.smarthealthit.org", "1032702");
		theIdentifier.addValue(tolp);

		SearchParameterMap paramMap = new SearchParameterMap();
		paramMap.add(AllergyIntolerance.SP_IDENTIFIER, theIdentifier);

		assertNotNull(CommonUtil.getListOfTokenParamSearchParameterValue(paramMap, "identifier"));
	}

	@Test
	void testGetTokenParamSearchParameterValue() {
		TokenAndListParam theIdentifier = new TokenAndListParam();
		TokenOrListParam tolp = new TokenOrListParam();
		tolp.add("http://hospital.smarthealthit.org", "1032702");
		theIdentifier.addValue(tolp);

		SearchParameterMap paramMap = new SearchParameterMap();
		paramMap.add(AllergyIntolerance.SP_IDENTIFIER, theIdentifier);

		assertNotNull(CommonUtil.getTokenParamSearchParameterValue(paramMap, "identifier"));
	}

	@Test
	void testGetIdFromReferenceSearchParam() {
		SearchParameterMap paramMap = new SearchParameterMap();
		ReferenceAndListParam thePatient = new ReferenceAndListParam();
		ReferenceOrListParam rolp = new ReferenceOrListParam();
		ReferenceParam rpd = new ReferenceParam();
		rpd.setValue("123");
		rolp.add(rpd);
		thePatient.addValue(rolp);
		paramMap.add(Immunization.SP_PATIENT, thePatient);
		assertNotNull(CommonUtil.getIdFromReferenceSearchParam(paramMap, "patient"));
	}

	@Test
	void testGetDateParamsFromQuery() throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

		SearchParameterMap paramMap = new SearchParameterMap();
		DateRangeParam theDate = new DateRangeParam();
		DateParam dp = new DateParam();
		dp.setValue(formatter.parse("2022-07-02"));
		theDate.setUpperBound(dp);
		DateParam dpLb = new DateParam();
		dpLb.setValue(formatter.parse("2021-02-02"));
		theDate.setLowerBound(dpLb);
		paramMap.add(Encounter.SP_DATE, theDate);
		assertNotNull(CommonUtil.getDateParamsFromQuery(paramMap, "date"));
	}

	@Test
	void testAccessTokenFronRequestHeader() {
		HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("bearer test");
		assertEquals("test", CommonUtil.getAccessTokenFronRequestHeader(request));
	}

	@Test
	void testGetFullURL() {
		HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
		StringBuffer sb = new StringBuffer("http://localhost:8090/CarePlan");
		when(request.getRequestURL()).thenReturn(sb);
		when(request.getQueryString()).thenReturn("patient=123");
		assertEquals("http://localhost:8090/CarePlan?patient=123", CommonUtil.getFullURL(request));

	}

	@Test
	void testGetFullUrl() {
		assertEquals("CarePlan/T5HydRhsy6s65w", CommonUtil.getFullUrl(
				"http://localhost:8089/fhir/r4/1005/CarePlan?patient=1137192", "T5HydRhsy6s65w", "CarePlan"));

	}
}
