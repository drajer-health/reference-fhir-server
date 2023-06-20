package com.interopx.fhir.facade.util;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.hl7.fhir.r4.model.AllergyIntolerance;
import org.hl7.fhir.r4.model.CarePlan;
import org.hl7.fhir.r4.model.DiagnosticReport;
import org.hl7.fhir.r4.model.DocumentReference;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.Goal;
import org.hl7.fhir.r4.model.MedicationRequest;
import org.hl7.fhir.r4.model.PractitionerRole;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;

import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.rest.api.server.RequestDetails;
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
class ParameterResourceUtilTest {

	@Test
	void test1() {
		RequestDetails theRequestDetails = null;
		SearchParameterMap paramMap = new SearchParameterMap();
		ReferenceAndListParam theEncounter = new ReferenceAndListParam();
		ReferenceOrListParam rolp = new ReferenceOrListParam();
		ReferenceParam rpd = new ReferenceParam();
		rpd.setValue("123");
		rolp.add(rpd);
		theEncounter.addValue(rolp);
		paramMap.add("encounter", theEncounter);
		Map<String, String> pathMap = new HashMap<>();
		pathMap.put("resourceType", "encounter");
		pathMap.put("requestId", "124");
		pathMap.put("endPointUrl", "url");
		pathMap.put("practiceId", "tst123");
		assertNotNull(ParameterResourceUtil.generateParameterResource(paramMap, pathMap, theRequestDetails));

	}

	@Test
	void test2() {
		RequestDetails theRequestDetails = null;
		SearchParameterMap paramMap = new SearchParameterMap();
		ReferenceAndListParam theEncounter = new ReferenceAndListParam();
		ReferenceOrListParam rolp = new ReferenceOrListParam();
		ReferenceParam rpd = new ReferenceParam();
		rpd.setValue("123");
		rolp.add(rpd);
		theEncounter.addValue(rolp);
		paramMap.add("context", theEncounter);
		Map<String, String> pathMap = new HashMap<>();
		pathMap.put("resourceType", "encounter");
		pathMap.put("requestId", "124");
		pathMap.put("endPointUrl", "url");
		pathMap.put("practiceId", "tst123");
		assertNotNull(ParameterResourceUtil.generateParameterResource(paramMap, pathMap,theRequestDetails));

	}

	@Test
	void test3() throws ParseException {
		RequestDetails theRequestDetails = null;
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
		Map<String, String> pathMap = new HashMap<>();
		pathMap.put("resourceType", "encounter");
		pathMap.put("requestId", "124");
		pathMap.put("endPointUrl", "url");
		pathMap.put("practiceId", "tst123");
		assertNotNull(ParameterResourceUtil.generateParameterResource(paramMap, pathMap,theRequestDetails));
	}

	@Test
	void test4() throws ParseException {
		RequestDetails theRequestDetails = null;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		SearchParameterMap paramMap = new SearchParameterMap();
		DateRangeParam theDate = new DateRangeParam();
		DateParam dp = new DateParam();
		dp.setValue(formatter.parse("2022-07-02"));
		theDate.setUpperBound(dp);
		DateParam dpLb = new DateParam();
		dpLb.setValue(formatter.parse("2021-02-02"));
		theDate.setLowerBound(dpLb);
		paramMap.add("onset-date", theDate);
		Map<String, String> pathMap = new HashMap<>();
		pathMap.put("resourceType", "encounter");
		pathMap.put("requestId", "124");
		pathMap.put("endPointUrl", "url");
		pathMap.put("practiceId", "tst123");
		assertNotNull(ParameterResourceUtil.generateParameterResource(paramMap, pathMap,theRequestDetails));
	}

	@Test
	void test5() throws ParseException {
		RequestDetails theRequestDetails = null;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		SearchParameterMap paramMap = new SearchParameterMap();
		DateRangeParam theDate = new DateRangeParam();
		DateParam dp = new DateParam();
		dp.setValue(formatter.parse("2022-07-02"));
		theDate.setUpperBound(dp);
		DateParam dpLb = new DateParam();
		dpLb.setValue(formatter.parse("2021-02-02"));
		theDate.setLowerBound(dpLb);
		paramMap.add("period", theDate);
		Map<String, String> pathMap = new HashMap<>();
		pathMap.put("resourceType", "encounter");
		pathMap.put("requestId", "124");
		pathMap.put("endPointUrl", "url");
		pathMap.put("practiceId", "tst123");
		assertNotNull(ParameterResourceUtil.generateParameterResource(paramMap, pathMap, theRequestDetails));
	}

	@Test
	void test6() throws ParseException {
		RequestDetails theRequestDetails = null;
		TokenAndListParam tokenAndListParam = new TokenAndListParam();
		TokenOrListParam tolp = new TokenOrListParam();
		tolp.add("http://hospital.smarthealthit.org", "1032702");
		tokenAndListParam.addValue(tolp);
		SearchParameterMap paramMap = new SearchParameterMap();
		paramMap.add("class", tokenAndListParam);
		Map<String, String> pathMap = new HashMap<>();
		pathMap.put("resourceType", "encounter");
		pathMap.put("requestId", "124");
		pathMap.put("endPointUrl", "url");
		pathMap.put("practiceId", "tst123");
		assertNotNull(ParameterResourceUtil.generateParameterResource(paramMap, pathMap, theRequestDetails));
	}

	@Test
	void test7() throws ParseException {
		RequestDetails theRequestDetails = null;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		SearchParameterMap paramMap = new SearchParameterMap();
		DateRangeParam theDate = new DateRangeParam();
		DateParam dp = new DateParam();
		dp.setValue(formatter.parse("2022-07-02"));
		theDate.setUpperBound(dp);
		DateParam dpLb = new DateParam();
		dpLb.setValue(formatter.parse("2021-02-02"));
		theDate.setLowerBound(dpLb);
		paramMap.add("target-date", theDate);
		Map<String, String> pathMap = new HashMap<>();
		pathMap.put("resourceType", "encounter");
		pathMap.put("requestId", "124");
		pathMap.put("endPointUrl", "url");
		pathMap.put("practiceId", "tst123");
		assertNotNull(ParameterResourceUtil.generateParameterResource(paramMap, pathMap, theRequestDetails));
	}

	@Test
	void test8() throws ParseException {
		RequestDetails theRequestDetails = null;
		StringAndListParam salp = new StringAndListParam();
		StringOrListParam solp = new StringOrListParam();
		StringParam sp = new StringParam();
		sp.setValue("Kormangala");
		solp.add(sp);
		salp.addValue(solp);
		SearchParameterMap paramMap = new SearchParameterMap();
		paramMap.add("address", salp);
		Map<String, String> pathMap = new HashMap<>();
		pathMap.put("resourceType", "encounter");
		pathMap.put("requestId", "124");
		pathMap.put("endPointUrl", "url");
		pathMap.put("practiceId", "tst123");
		assertNotNull(ParameterResourceUtil.generateParameterResource(paramMap, pathMap, theRequestDetails));
	}

	@Test
	void test9() throws ParseException {
		RequestDetails theRequestDetails = null;
		StringAndListParam salp = new StringAndListParam();
		StringOrListParam solp = new StringOrListParam();
		StringParam sp = new StringParam();
		sp.setValue("Bangalore");
		solp.add(sp);
		salp.addValue(solp);
		SearchParameterMap paramMap = new SearchParameterMap();
		paramMap.add("address-city", salp);
		Map<String, String> pathMap = new HashMap<>();
		pathMap.put("resourceType", "encounter");
		pathMap.put("requestId", "124");
		pathMap.put("endPointUrl", "url");
		pathMap.put("practiceId", "tst123");
		assertNotNull(ParameterResourceUtil.generateParameterResource(paramMap, pathMap, theRequestDetails));
	}

	@Test
	void test10() throws ParseException {
		RequestDetails theRequestDetails = null;
		StringAndListParam salp = new StringAndListParam();
		StringOrListParam solp = new StringOrListParam();
		StringParam sp = new StringParam();
		sp.setValue("Maharastra");
		solp.add(sp);
		salp.addValue(solp);
		SearchParameterMap paramMap = new SearchParameterMap();
		paramMap.add("address-state", salp);
		Map<String, String> pathMap = new HashMap<>();
		pathMap.put("resourceType", "encounter");
		pathMap.put("requestId", "124");
		pathMap.put("endPointUrl", "url");
		pathMap.put("practiceId", "tst123");
		
		assertNotNull(ParameterResourceUtil.generateParameterResource(paramMap, pathMap,theRequestDetails));
	}

	@Test
	void test11() throws ParseException {
		RequestDetails theRequestDetails = null;
		StringAndListParam salp = new StringAndListParam();
		StringOrListParam solp = new StringOrListParam();
		StringParam sp = new StringParam();
		sp.setValue("780983");
		solp.add(sp);
		salp.addValue(solp);
		SearchParameterMap paramMap = new SearchParameterMap();
		paramMap.add("address-postalcode", salp);
		Map<String, String> pathMap = new HashMap<>();
		pathMap.put("resourceType", "encounter");
		pathMap.put("requestId", "124");
		pathMap.put("endPointUrl", "url");
		pathMap.put("practiceId", "tst123");
		assertNotNull(ParameterResourceUtil.generateParameterResource(paramMap, pathMap, theRequestDetails));
	}

	@Test
	void test12() throws ParseException {
		RequestDetails theRequestDetails = null;
		TokenAndListParam theIdentifier = new TokenAndListParam();
		TokenOrListParam tolp = new TokenOrListParam();
		tolp.add("http://hospital.smarthealthit.org", "1032702");
		theIdentifier.addValue(tolp);
		SearchParameterMap paramMap = new SearchParameterMap();
		paramMap.add("_id", theIdentifier);
		Map<String, String> pathMap = new HashMap<>();
		pathMap.put("resourceType", "encounter");
		pathMap.put("requestId", "124");
		pathMap.put("endPointUrl", "url");
		pathMap.put("practiceId", "tst123");
		assertNotNull(ParameterResourceUtil.generateParameterResource(paramMap, pathMap, theRequestDetails));
	}

	@Test
	void test13() throws ParseException {
		RequestDetails theRequestDetails = null;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		SearchParameterMap paramMap = new SearchParameterMap();
		DateRangeParam theDate = new DateRangeParam();
		DateParam dp = new DateParam();
		dp.setValue(formatter.parse("2022-07-02"));
		theDate.setUpperBound(dp);
		DateParam dpLb = new DateParam();
		dpLb.setValue(formatter.parse("2021-02-02"));
		theDate.setLowerBound(dpLb);
		paramMap.add("authoredon", theDate);
		Map<String, String> pathMap = new HashMap<>();
		pathMap.put("resourceType", "encounter");
		pathMap.put("requestId", "124");
		pathMap.put("endPointUrl", "url");
		pathMap.put("practiceId", "tst123");
		assertNotNull(ParameterResourceUtil.generateParameterResource(paramMap, pathMap, theRequestDetails));
	}

	@Test
	void test14() throws ParseException {
		RequestDetails theRequestDetails = null;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		SearchParameterMap paramMap = new SearchParameterMap();
		DateRangeParam theDate = new DateRangeParam();
		DateParam dp = new DateParam();
		dp.setValue(formatter.parse("2022-07-02"));
		theDate.setUpperBound(dp);
		DateParam dpLb = new DateParam();
		dpLb.setValue(formatter.parse("2021-02-02"));
		theDate.setLowerBound(dpLb);
		paramMap.add("birthdate", theDate);
		Map<String, String> pathMap = new HashMap<>();
		pathMap.put("resourceType", "encounter");
		pathMap.put("requestId", "124");
		pathMap.put("endPointUrl", "url");
		pathMap.put("practiceId", "tst123");
		assertNotNull(ParameterResourceUtil.generateParameterResource(paramMap, pathMap, theRequestDetails));
	}

	@Test
	void test15() throws ParseException {
		RequestDetails theRequestDetails = null;
		Set<Include> theIncludes = new HashSet<>();
		Include o = new Include("PractitionerRole:practitioner");
		theIncludes.add(o);
		SearchParameterMap paramMap = new SearchParameterMap();
		paramMap.setIncludes(theIncludes);
		Map<String, String> pathMap = new HashMap<>();
		pathMap.put("resourceType", "encounter");
		pathMap.put("requestId", "124");
		pathMap.put("endPointUrl", "url");
		pathMap.put("practiceId", "tst123");
		assertNotNull(ParameterResourceUtil.generateParameterResource(paramMap, pathMap, theRequestDetails));
	}

	@Test
	void test16() throws ParseException {
		RequestDetails theRequestDetails = null;
		Set<Include> theIncludes = new HashSet<>();
		Include o = new Include("PractitionerRole:endpoint");
		theIncludes.add(o);
		SearchParameterMap paramMap = new SearchParameterMap();
		paramMap.setIncludes(theIncludes);
		Map<String, String> pathMap = new HashMap<>();
		pathMap.put("resourceType", "encounter");
		pathMap.put("requestId", "124");
		pathMap.put("endPointUrl", "url");
		pathMap.put("practiceId", "tst123");
		assertNotNull(ParameterResourceUtil.generateParameterResource(paramMap, pathMap, theRequestDetails));
	}

	@Test
	void test17() throws ParseException {
		RequestDetails theRequestDetails = null;
		Set<Include> theIncludes = new HashSet<>();
		Include o = new Include("MedicationRequest:medication");
		theIncludes.add(o);
		SearchParameterMap paramMap = new SearchParameterMap();
		paramMap.setIncludes(theIncludes);
		Map<String, String> pathMap = new HashMap<>();
		pathMap.put("resourceType", "encounter");
		pathMap.put("requestId", "124");
		pathMap.put("endPointUrl", "url");
		pathMap.put("practiceId", "tst123");
		assertNotNull(ParameterResourceUtil.generateParameterResource(paramMap, pathMap, theRequestDetails));
	}

	@Test
	void test18() throws ParseException {
		RequestDetails theRequestDetails = null;
		Set<Include> theIncludes = new HashSet<>();
		Include o = new Include("Provenance:target");
		theIncludes.add(o);
		SearchParameterMap paramMap = new SearchParameterMap();
		paramMap.setIncludes(theIncludes);
		Map<String, String> pathMap = new HashMap<>();
		pathMap.put("resourceType", "encounter");
		pathMap.put("requestId", "124");
		pathMap.put("endPointUrl", "url");
		pathMap.put("practiceId", "tst123");
		assertNotNull(ParameterResourceUtil.generateParameterResource(paramMap, pathMap, theRequestDetails));
	}

	@Test
	void test19() throws ParseException {
		RequestDetails theRequestDetails = null;
		TokenAndListParam theIdentifier = new TokenAndListParam();
		TokenOrListParam tolp = new TokenOrListParam();
		tolp.add("http://hospital.smarthealthit.org", "1032702");
		theIdentifier.addValue(tolp);
		SearchParameterMap paramMap = new SearchParameterMap();
		paramMap.add("practitioner.identifier", theIdentifier);
		Map<String, String> pathMap = new HashMap<>();
		pathMap.put("resourceType", "encounter");
		pathMap.put("requestId", "124");
		pathMap.put("endPointUrl", "url");
		pathMap.put("practiceId", "tst123");
		assertNotNull(ParameterResourceUtil.generateParameterResource(paramMap, pathMap, theRequestDetails));
	}

	@Test
	void test20() throws ParseException {
		RequestDetails theRequestDetails = null;
		TokenAndListParam toknAndListParam = new TokenAndListParam();
		TokenOrListParam top = new TokenOrListParam();
		top.add(DiagnosticReport.SP_CODE, "1445");
		toknAndListParam.addValue(top);
		SearchParameterMap paramMap = new SearchParameterMap();
		paramMap.add(DiagnosticReport.SP_CODE, toknAndListParam);
		Map<String, String> pathMap = new HashMap<>();
		pathMap.put("resourceType", "encounter");
		pathMap.put("requestId", "124");
		pathMap.put("endPointUrl", "url");
		pathMap.put("practiceId", "tst123");
		assertNotNull(ParameterResourceUtil.generateParameterResource(paramMap, pathMap, theRequestDetails));
	}

	@Test
	void test21() throws ParseException {
		RequestDetails theRequestDetails = null;
		TokenAndListParam toknAndListParam = new TokenAndListParam();
		TokenOrListParam top = new TokenOrListParam();
		top.add(DiagnosticReport.SP_CODE, "code");
		toknAndListParam.addValue(top);
		SearchParameterMap paramMap = new SearchParameterMap();
		paramMap.add(DiagnosticReport.SP_CODE, toknAndListParam);
		Map<String, String> pathMap = new HashMap<>();
		pathMap.put("resourceType", "encounter");
		pathMap.put("requestId", "124");
		pathMap.put("endPointUrl", "url");
		pathMap.put("practiceId", "tst123");
		assertNotNull(ParameterResourceUtil.generateParameterResource(paramMap, pathMap, theRequestDetails));
	}

	@Test
	void test22() throws ParseException {
		RequestDetails theRequestDetails = null;
		TokenAndListParam tokenAndListParam = new TokenAndListParam();
		TokenOrListParam tolp = new TokenOrListParam();
		tolp.add(DiagnosticReport.SP_CATEGORY, "active");
		tokenAndListParam.addValue(tolp);
		SearchParameterMap paramMap = new SearchParameterMap();
		paramMap.add(DocumentReference.SP_CATEGORY, tokenAndListParam);
		Map<String, String> pathMap = new HashMap<>();
		pathMap.put("resourceType", "encounter");
		pathMap.put("requestId", "124");
		pathMap.put("endPointUrl", "url");
		pathMap.put("practiceId", "tst123");
		assertNotNull(ParameterResourceUtil.generateParameterResource(paramMap, pathMap, theRequestDetails));
	}
	@Test
	void test23() throws ParseException {
		RequestDetails theRequestDetails = null;
		TokenAndListParam tokenAndListParam = new TokenAndListParam();
		TokenOrListParam tolp = new TokenOrListParam();
		tolp.add(AllergyIntolerance.SP_CLINICAL_STATUS, "active");
		tokenAndListParam.addValue(tolp);
		SearchParameterMap paramMap = new SearchParameterMap();
		paramMap.add("clinical-status", tokenAndListParam);

		Map<String, String> pathMap = new HashMap<>();
		pathMap.put("resourceType", "encounter");
		pathMap.put("requestId", "124");
		pathMap.put("endPointUrl", "url");
		pathMap.put("practiceId", "tst123");
		assertNotNull(ParameterResourceUtil.generateParameterResource(paramMap, pathMap, theRequestDetails));
	}
	@Test
	void test24() throws ParseException {
		RequestDetails theRequestDetails = null;
		TokenAndListParam toknAndListParam = new TokenAndListParam();
		TokenOrListParam top = new TokenOrListParam();
		top.add(DocumentReference.SP_TYPE, "normal");
		toknAndListParam.addValue(top);
		SearchParameterMap paramMap = new SearchParameterMap();
		paramMap.add(DocumentReference.SP_TYPE, toknAndListParam);

		Map<String, String> pathMap = new HashMap<>();
		pathMap.put("resourceType", "encounter");
		pathMap.put("requestId", "124");
		pathMap.put("endPointUrl", "url");
		pathMap.put("practiceId", "tst123");
		assertNotNull(ParameterResourceUtil.generateParameterResource(paramMap, pathMap, theRequestDetails));
	}
	@Test
	void test25() throws ParseException {
		RequestDetails theRequestDetails = null;
		SearchParameterMap paramMap = new SearchParameterMap();
		TokenAndListParam tokenAndListParm = new TokenAndListParam();
		TokenOrListParam tol = new TokenOrListParam();
		tol.add("_id", "123");
		tokenAndListParm.addValue(tol);
		paramMap.add(Encounter.SP_IDENTIFIER, tokenAndListParm);
		Map<String, String> pathMap = new HashMap<>();
		pathMap.put("resourceType", "encounter");
		pathMap.put("requestId", "124");
		pathMap.put("endPointUrl", "url");
		pathMap.put("practiceId", "tst123");
		assertNotNull(ParameterResourceUtil.generateParameterResource(paramMap, pathMap, theRequestDetails));
	}
	@Test
	void test26() throws ParseException {
		RequestDetails theRequestDetails = null;
		SearchParameterMap paramMap = new SearchParameterMap();
		TokenAndListParam toknAndListParam = new TokenAndListParam();
		TokenOrListParam top = new TokenOrListParam();
		top.add(PractitionerRole.SP_SPECIALTY, "normal");
		toknAndListParam.addValue(top);
		paramMap.add(PractitionerRole.SP_SPECIALTY, toknAndListParam);
		Map<String, String> pathMap = new HashMap<>();
		pathMap.put("resourceType", "encounter");
		pathMap.put("requestId", "124");
		pathMap.put("endPointUrl", "url");
		pathMap.put("practiceId", "tst123");
		assertNotNull(ParameterResourceUtil.generateParameterResource(paramMap, pathMap, theRequestDetails));
	}
	@Test
	void test27() throws ParseException {
		RequestDetails theRequestDetails = null;
		SearchParameterMap paramMap = new SearchParameterMap();
		Set<Include> theIncludes = new HashSet<>();
		Include o = new Include("PractitionerRole:organization");
		theIncludes.add(o);
		paramMap.setIncludes(theIncludes);
		Map<String, String> pathMap = new HashMap<>();
		pathMap.put("resourceType", "encounter");
		pathMap.put("requestId", "124");
		pathMap.put("endPointUrl", "url");
		pathMap.put("practiceId", "tst123");
		assertNotNull(ParameterResourceUtil.generateParameterResource(paramMap, pathMap, theRequestDetails));
	}
	@Test
	void test28() throws ParseException {
		RequestDetails theRequestDetails = null;
		SearchParameterMap paramMap = new SearchParameterMap();
		Set<Include> theIncludes = new HashSet<>();
		Include o = new Include("Observation:performer");
		theIncludes.add(o);
		paramMap.setIncludes(theIncludes);
		Map<String, String> pathMap = new HashMap<>();
		pathMap.put("resourceType", "encounter");
		pathMap.put("requestId", "124");
		pathMap.put("endPointUrl", "url");
		pathMap.put("practiceId", "tst123");
		assertNotNull(ParameterResourceUtil.generateParameterResource(paramMap, pathMap, theRequestDetails));
	}
	@Test
	void test29() throws ParseException {
		RequestDetails theRequestDetails = null;
		SearchParameterMap paramMap = new SearchParameterMap();
		Set<Include> theIncludes = new HashSet<>();
		Include o = new Include("DiagnosticReport:performer");
		theIncludes.add(o);
		paramMap.setIncludes(theIncludes);
		Map<String, String> pathMap = new HashMap<>();
		pathMap.put("resourceType", "encounter");
		pathMap.put("requestId", "124");
		pathMap.put("endPointUrl", "url");
		pathMap.put("practiceId", "tst123");
		assertNotNull(ParameterResourceUtil.generateParameterResource(paramMap, pathMap, theRequestDetails));
	}
	@Test
	void test30() throws ParseException {
		RequestDetails theRequestDetails = null;
		SearchParameterMap paramMap = new SearchParameterMap();
		Set<Include> theIncludes = new HashSet<>();
		Include o = new Include("DocumentReference:author");
		theIncludes.add(o);
		paramMap.setIncludes(theIncludes);
		Map<String, String> pathMap = new HashMap<>();
		pathMap.put("resourceType", "encounter");
		pathMap.put("requestId", "124");
		pathMap.put("endPointUrl", "url");
		pathMap.put("practiceId", "tst123");
		assertNotNull(ParameterResourceUtil.generateParameterResource(paramMap, pathMap, theRequestDetails));
	}
	@Test
	void test31() throws ParseException {
		RequestDetails theRequestDetails = null;
		SearchParameterMap paramMap = new SearchParameterMap();
		Set<Include> theIncludes = new HashSet<>();
		Include o = new Include("Location:organization");
		theIncludes.add(o);
		paramMap.setIncludes(theIncludes);
		Map<String, String> pathMap = new HashMap<>();
		pathMap.put("resourceType", "encounter");
		pathMap.put("requestId", "124");
		pathMap.put("endPointUrl", "url");
		pathMap.put("practiceId", "tst123");
		assertNotNull(ParameterResourceUtil.generateParameterResource(paramMap, pathMap, theRequestDetails));
	}
	@Test
	void test32() throws ParseException {
		RequestDetails theRequestDetails = null;
		SearchParameterMap paramMap = new SearchParameterMap();
		Set<Include> theIncludes = new HashSet<>();
		Include o = new Include("CareTeam:participant");
		theIncludes.add(o);
		paramMap.setIncludes(theIncludes);
		Map<String, String> pathMap = new HashMap<>();
		pathMap.put("resourceType", "encounter");
		pathMap.put("requestId", "124");
		pathMap.put("endPointUrl", "url");
		pathMap.put("practiceId", "tst123");
		assertNotNull(ParameterResourceUtil.generateParameterResource(paramMap, pathMap, theRequestDetails));
	}
	@Test
	void test33() throws ParseException {
		RequestDetails theRequestDetails = null;
		SearchParameterMap paramMap = new SearchParameterMap();
		Set<Include> theIncludes = new HashSet<>();
		Include o = new Include("DocumentReference:custodian");
		theIncludes.add(o);
		paramMap.setIncludes(theIncludes);
		Map<String, String> pathMap = new HashMap<>();
		pathMap.put("resourceType", "encounter");
		pathMap.put("requestId", "124");
		pathMap.put("endPointUrl", "url");
		pathMap.put("practiceId", "tst123");
		assertNotNull(ParameterResourceUtil.generateParameterResource(paramMap, pathMap, theRequestDetails));
	}
	@Test
	void test34() throws ParseException {
		RequestDetails theRequestDetails = null;
		SearchParameterMap paramMap = new SearchParameterMap();
		ReferenceAndListParam patient = new ReferenceAndListParam();
		ReferenceOrListParam rolp = new ReferenceOrListParam();
		ReferenceParam rpd = new ReferenceParam();
		rpd.setValue("123");
		rolp.add(rpd);
		patient.addValue(rolp);
		paramMap.add(MedicationRequest.SP_PATIENT, patient);
		Map<String, String> pathMap = new HashMap<>();
		pathMap.put("resourceType", "encounter");
		pathMap.put("requestId", "124");
		pathMap.put("endPointUrl", "url");
		pathMap.put("practiceId", "tst123");
		assertNotNull(ParameterResourceUtil.generateParameterResource(paramMap, pathMap, theRequestDetails));
	}
	@Test
	void test35() throws ParseException {
		RequestDetails theRequestDetails = null;
		SearchParameterMap paramMap = new SearchParameterMap();
		TokenAndListParam tokenAndListPam = new TokenAndListParam();
		TokenOrListParam to = new TokenOrListParam();
		to.add(CarePlan.SP_STATUS, "active");
		tokenAndListPam.addValue(to);
		paramMap.add(CarePlan.SP_STATUS, tokenAndListPam);
		Map<String, String> pathMap = new HashMap<>();
		pathMap.put("resourceType", "encounter");
		pathMap.put("requestId", "124");
		pathMap.put("endPointUrl", "url");
		pathMap.put("practiceId", "tst123");
		assertNotNull(ParameterResourceUtil.generateParameterResource(paramMap, pathMap, theRequestDetails));
	}
	
	@Test
	void test36() throws ParseException {
		RequestDetails theRequestDetails = null;
		TokenAndListParam tokenAndListParam = new TokenAndListParam();
		TokenOrListParam tolp = new TokenOrListParam();
		tolp.add(DiagnosticReport.SP_CATEGORY, "category");
		tokenAndListParam.addValue(tolp);
		SearchParameterMap paramMap = new SearchParameterMap();
		paramMap.add(DocumentReference.SP_CATEGORY, tokenAndListParam);
		Map<String, String> pathMap = new HashMap<>();
		pathMap.put("resourceType", "encounter");
		pathMap.put("requestId", "124");
		pathMap.put("endPointUrl", "url");
		pathMap.put("practiceId", "tst123");
		assertNotNull(ParameterResourceUtil.generateParameterResource(paramMap, pathMap, theRequestDetails));
	}
	
	@Test
	void test37() throws ParseException {
		RequestDetails theRequestDetails = null;
		TokenAndListParam tokenAndListParam = new TokenAndListParam();
		TokenOrListParam tolp = new TokenOrListParam();
		tolp.add(AllergyIntolerance.SP_CLINICAL_STATUS, "clinical-status");
		tokenAndListParam.addValue(tolp);
		SearchParameterMap paramMap = new SearchParameterMap();
		paramMap.add("clinical-status", tokenAndListParam);

		Map<String, String> pathMap = new HashMap<>();
		pathMap.put("resourceType", "encounter");
		pathMap.put("requestId", "124");
		pathMap.put("endPointUrl", "url");
		pathMap.put("practiceId", "tst123");
		assertNotNull(ParameterResourceUtil.generateParameterResource(paramMap, pathMap, theRequestDetails));
	}
	@Test
	void test38() throws ParseException {
		RequestDetails theRequestDetails = null;
		TokenAndListParam toknAndListParam = new TokenAndListParam();
		TokenOrListParam top = new TokenOrListParam();
		top.add(DocumentReference.SP_TYPE, "type");
		toknAndListParam.addValue(top);
		SearchParameterMap paramMap = new SearchParameterMap();
		paramMap.add(DocumentReference.SP_TYPE, toknAndListParam);

		Map<String, String> pathMap = new HashMap<>();
		pathMap.put("resourceType", "encounter");
		pathMap.put("requestId", "124");
		pathMap.put("endPointUrl", "url");
		pathMap.put("practiceId", "tst123");
		assertNotNull(ParameterResourceUtil.generateParameterResource(paramMap, pathMap, theRequestDetails));
	}
	@Test
	void test39() throws ParseException {
		RequestDetails theRequestDetails = null;
		TokenAndListParam toknAndListParam = new TokenAndListParam();
		TokenOrListParam top = new TokenOrListParam();
		top.add(Encounter.SP_CLASS, "class");
		toknAndListParam.addValue(top);
		SearchParameterMap paramMap = new SearchParameterMap();
		paramMap.add(Encounter.SP_CLASS, toknAndListParam);

		Map<String, String> pathMap = new HashMap<>();
		pathMap.put("resourceType", "encounter");
		pathMap.put("requestId", "124");
		pathMap.put("endPointUrl", "url");
		pathMap.put("practiceId", "tst123");
		assertNotNull(ParameterResourceUtil.generateParameterResource(paramMap, pathMap, theRequestDetails));
	}
	
	@Test
	void test40() throws ParseException {
		RequestDetails theRequestDetails = null;
		SearchParameterMap paramMap = new SearchParameterMap();
		TokenAndListParam tokenAndListParm = new TokenAndListParam();
		TokenOrListParam tol = new TokenOrListParam();
		tol.add("_id", "identifier");
		tokenAndListParm.addValue(tol);
		paramMap.add(Encounter.SP_IDENTIFIER, tokenAndListParm);
		Map<String, String> pathMap = new HashMap<>();
		pathMap.put("resourceType", "encounter");
		pathMap.put("requestId", "124");
		pathMap.put("endPointUrl", "url");
		pathMap.put("practiceId", "tst123");
		assertNotNull(ParameterResourceUtil.generateParameterResource(paramMap, pathMap, theRequestDetails));
	}
	@Test
	void test41() throws ParseException {
		RequestDetails theRequestDetails = null;
		ParameterResourceUtil pru = new ParameterResourceUtil ();
		SearchParameterMap paramMap = new SearchParameterMap();
		TokenAndListParam toknAndListParam = new TokenAndListParam();
		TokenOrListParam top = new TokenOrListParam();
		top.add(Goal.SP_LIFECYCLE_STATUS, "normal");
		toknAndListParam.addValue(top);
		paramMap.add(Goal.SP_LIFECYCLE_STATUS, toknAndListParam);
		Map<String, String> pathMap = new HashMap<>();
		pathMap.put("resourceType", "encounter");
		pathMap.put("requestId", "124");
		pathMap.put("endPointUrl", "url");
		pathMap.put("practiceId", "tst123");
		assertNotNull(ParameterResourceUtil.generateParameterResource(paramMap, pathMap, theRequestDetails));
	}
}
