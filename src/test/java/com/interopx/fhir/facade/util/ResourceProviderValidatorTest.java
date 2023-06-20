package com.interopx.fhir.facade.util;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.hl7.fhir.r4.model.AllergyIntolerance;
import org.hl7.fhir.r4.model.CarePlan;
import org.hl7.fhir.r4.model.CareTeam;
import org.hl7.fhir.r4.model.Condition;
import org.hl7.fhir.r4.model.Device;
import org.hl7.fhir.r4.model.DiagnosticReport;
import org.hl7.fhir.r4.model.DocumentReference;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.Goal;
import org.hl7.fhir.r4.model.Immunization;
import org.hl7.fhir.r4.model.Location;
import org.hl7.fhir.r4.model.MedicationAdministration;
import org.hl7.fhir.r4.model.MedicationRequest;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Practitioner;
import org.hl7.fhir.r4.model.PractitionerRole;
import org.hl7.fhir.r4.model.Procedure;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;

import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.rest.param.DateAndListParam;
import ca.uhn.fhir.rest.param.DateOrListParam;
import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.rest.param.ReferenceAndListParam;
import ca.uhn.fhir.rest.param.ReferenceOrListParam;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.StringAndListParam;
import ca.uhn.fhir.rest.param.StringOrListParam;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.TokenAndListParam;
import ca.uhn.fhir.rest.param.TokenOrListParam;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class ResourceProviderValidatorTest {
	@Test
	void testAllergy_id() {
		ResourceProviderValidator rpv = new ResourceProviderValidator();
		SearchParameterMap paramMap = new SearchParameterMap();
		paramMap = new SearchParameterMap();
		TokenAndListParam tokenAndListParm = new TokenAndListParam();
		TokenOrListParam tol = new TokenOrListParam();
		tol.add("_id", "123");
		tokenAndListParm.addValue(tol);
		paramMap.add(AllergyIntolerance.SP_RES_ID, tokenAndListParm);
		assertTrue(ResourceProviderValidator.validateRequest(paramMap, ResourceTypes.ALLERGYINTOLERANCE.toString()));
	}

	@Test
	void testAllergyPatientAndCinicalStatus() {
		SearchParameterMap paramMap = new SearchParameterMap();
		ReferenceAndListParam patient = new ReferenceAndListParam();
		ReferenceOrListParam rolp = new ReferenceOrListParam();
		ReferenceParam rpd = new ReferenceParam();
		rpd.setValue("123");
		rolp.add(rpd);
		patient.addValue(rolp);
		paramMap.add(AllergyIntolerance.SP_PATIENT, patient);

		TokenAndListParam tokenAndListParam = new TokenAndListParam();
		TokenOrListParam tolp = new TokenOrListParam();
		tolp.add(AllergyIntolerance.SP_CLINICAL_STATUS, "active");
		tokenAndListParam.addValue(tolp);
		paramMap.add("clinical-status", tokenAndListParam);

		assertTrue(ResourceProviderValidator.validateRequest(paramMap, ResourceTypes.ALLERGYINTOLERANCE.toString()));
	}

	@Test
	void testAllergyPatientAndEncounter() {
		SearchParameterMap paramMap = new SearchParameterMap();
		ReferenceAndListParam patient = new ReferenceAndListParam();
		ReferenceOrListParam rolp = new ReferenceOrListParam();
		ReferenceParam rpd = new ReferenceParam();
		rpd.setValue("123");
		rolp.add(rpd);
		patient.addValue(rolp);
		paramMap.add(AllergyIntolerance.SP_PATIENT, patient);

		ReferenceAndListParam encounter = new ReferenceAndListParam();
		ReferenceOrListParam rolpe = new ReferenceOrListParam();
		ReferenceParam rpde = new ReferenceParam();
		rpde.setValue("1234");
		rolpe.add(rpd);
		encounter.addValue(rolpe);
		paramMap.add("encounter", encounter);

		assertTrue(ResourceProviderValidator.validateRequest(paramMap, ResourceTypes.ALLERGYINTOLERANCE.toString()));
	}

	@Test
	void testCarePlanidTest() {
		SearchParameterMap paramMap = new SearchParameterMap();
		paramMap = new SearchParameterMap();
		TokenAndListParam tokenAndListParm = new TokenAndListParam();
		TokenOrListParam tol = new TokenOrListParam();
		tol.add("_id", "123");
		tokenAndListParm.addValue(tol);
		paramMap.add(CarePlan.SP_RES_ID, tokenAndListParm);
		assertTrue(ResourceProviderValidator.validateRequest(paramMap, ResourceTypes.CAREPLAN.toString()));
	}

	@Test
	void testCarePlanPatientCategoryStatusDateTest() {
		SearchParameterMap paramMap = new SearchParameterMap();
		ReferenceAndListParam patient = new ReferenceAndListParam();
		ReferenceOrListParam rolp = new ReferenceOrListParam();
		ReferenceParam rpd = new ReferenceParam();
		rpd.setValue("123");
		rolp.add(rpd);
		patient.addValue(rolp);
		paramMap.add(CarePlan.SP_PATIENT, patient);

		TokenAndListParam tokenAndListParam = new TokenAndListParam();
		TokenOrListParam tolp = new TokenOrListParam();
		tolp.add(CarePlan.SP_CATEGORY, "active");
		tokenAndListParam.addValue(tolp);
		paramMap.add(CarePlan.SP_CATEGORY, tokenAndListParam);

		TokenAndListParam tokenAndListPam = new TokenAndListParam();
		TokenOrListParam to = new TokenOrListParam();
		to.add(CarePlan.SP_STATUS, "active");
		tokenAndListPam.addValue(to);
		paramMap.add(CarePlan.SP_STATUS, tokenAndListParam);

		DateAndListParam dateAndListParam = new DateAndListParam();
		DateOrListParam dolp = new DateOrListParam();
		DateParam dp = new DateParam();
		dp.setValue(new Date());
		dolp.add(dp);
		dateAndListParam.addValue(dolp);
		paramMap.add(CarePlan.SP_DATE, dateAndListParam);

		assertTrue(ResourceProviderValidator.validateRequest(paramMap, ResourceTypes.CAREPLAN.toString()));
	}

	@Test
	void testAllergyPatientCategoryStatus() {
		SearchParameterMap paramMap = new SearchParameterMap();
		ReferenceAndListParam patient = new ReferenceAndListParam();
		ReferenceOrListParam rolp = new ReferenceOrListParam();
		ReferenceParam rpd = new ReferenceParam();
		rpd.setValue("123");
		rolp.add(rpd);
		patient.addValue(rolp);
		paramMap.add(CarePlan.SP_PATIENT, patient);

		TokenAndListParam tokenAndListParam = new TokenAndListParam();
		TokenOrListParam tolp = new TokenOrListParam();
		tolp.add(CarePlan.SP_CATEGORY, "active");
		tokenAndListParam.addValue(tolp);
		paramMap.add(CarePlan.SP_CATEGORY, tokenAndListParam);

		TokenAndListParam tokenAndListPam = new TokenAndListParam();
		TokenOrListParam to = new TokenOrListParam();
		to.add(CarePlan.SP_STATUS, "active");
		tokenAndListPam.addValue(to);
		paramMap.add(CarePlan.SP_STATUS, tokenAndListParam);

		assertTrue(ResourceProviderValidator.validateRequest(paramMap, ResourceTypes.CAREPLAN.toString()));
	}

	@Test
	void testCarePlanPatientCategoryDate() {
		SearchParameterMap paramMap = new SearchParameterMap();
		ReferenceAndListParam patient = new ReferenceAndListParam();
		ReferenceOrListParam rolp = new ReferenceOrListParam();
		ReferenceParam rpd = new ReferenceParam();
		rpd.setValue("123");
		rolp.add(rpd);
		patient.addValue(rolp);
		paramMap.add(CarePlan.SP_PATIENT, patient);

		TokenAndListParam tokenAndListParam = new TokenAndListParam();
		TokenOrListParam tolp = new TokenOrListParam();
		tolp.add(CarePlan.SP_CATEGORY, "active");
		tokenAndListParam.addValue(tolp);
		paramMap.add(CarePlan.SP_CATEGORY, tokenAndListParam);

		DateAndListParam dateAndListParam = new DateAndListParam();
		DateOrListParam dolp = new DateOrListParam();
		DateParam dp = new DateParam();
		dp.setValue(new Date());
		dolp.add(dp);
		dateAndListParam.addValue(dolp);
		paramMap.add(CarePlan.SP_DATE, dateAndListParam);

		assertTrue(ResourceProviderValidator.validateRequest(paramMap, ResourceTypes.CAREPLAN.toString()));
	}

	@Test
	void testCarePlanPatientCategory() {
		SearchParameterMap paramMap = new SearchParameterMap();
		ReferenceAndListParam patient = new ReferenceAndListParam();
		ReferenceOrListParam rolp = new ReferenceOrListParam();
		ReferenceParam rpd = new ReferenceParam();
		rpd.setValue("123");
		rolp.add(rpd);
		patient.addValue(rolp);
		paramMap.add(CarePlan.SP_PATIENT, patient);

		TokenAndListParam tokenAndListParam = new TokenAndListParam();
		TokenOrListParam tolp = new TokenOrListParam();
		tolp.add(CarePlan.SP_CATEGORY, "active");
		tokenAndListParam.addValue(tolp);
		paramMap.add(CarePlan.SP_CATEGORY, tokenAndListParam);

		assertTrue(ResourceProviderValidator.validateRequest(paramMap, ResourceTypes.CAREPLAN.toString()));
	}

	@Test
	void testCarePlanPatientEncounter() {
		SearchParameterMap paramMap = new SearchParameterMap();
		ReferenceAndListParam patient = new ReferenceAndListParam();
		ReferenceOrListParam rolp = new ReferenceOrListParam();
		ReferenceParam rpd = new ReferenceParam();
		rpd.setValue("123");
		rolp.add(rpd);
		patient.addValue(rolp);
		paramMap.add(CarePlan.SP_PATIENT, patient);

		ReferenceAndListParam encounter = new ReferenceAndListParam();
		ReferenceOrListParam rolpe = new ReferenceOrListParam();
		ReferenceParam rpde = new ReferenceParam();
		rpde.setValue("1234");
		rolpe.add(rpd);
		encounter.addValue(rolpe);
		paramMap.add("encounter", encounter);

		assertTrue(ResourceProviderValidator.validateRequest(paramMap, ResourceTypes.CAREPLAN.toString()));
	}

	@Test
	void testCareTeamidTest() {
		SearchParameterMap paramMap = new SearchParameterMap();
		paramMap = new SearchParameterMap();
		TokenAndListParam tokenAndListParm = new TokenAndListParam();
		TokenOrListParam tol = new TokenOrListParam();
		tol.add("_id", "123");
		tokenAndListParm.addValue(tol);
		paramMap.add(CareTeam.SP_RES_ID, tokenAndListParm);
		assertTrue(ResourceProviderValidator.validateRequest(paramMap, ResourceTypes.CARETEAM.toString()));
	}

	@Test
	void testCareTeamPatientStatus() {
		SearchParameterMap paramMap = new SearchParameterMap();
		ReferenceAndListParam patient = new ReferenceAndListParam();
		ReferenceOrListParam rolp = new ReferenceOrListParam();
		ReferenceParam rpd = new ReferenceParam();
		rpd.setValue("123");
		rolp.add(rpd);
		patient.addValue(rolp);
		paramMap.add(CareTeam.SP_PATIENT, patient);

		TokenAndListParam tokenAndListPam = new TokenAndListParam();
		TokenOrListParam to = new TokenOrListParam();
		to.add(CareTeam.SP_STATUS, "active");
		tokenAndListPam.addValue(to);
		paramMap.add(CareTeam.SP_STATUS, tokenAndListPam);

		assertTrue(ResourceProviderValidator.validateRequest(paramMap, ResourceTypes.CARETEAM.toString()));
	}

	@Test
	void testConditionidTest() {
		SearchParameterMap paramMap = new SearchParameterMap();
		paramMap = new SearchParameterMap();
		TokenAndListParam tokenAndListParm = new TokenAndListParam();
		TokenOrListParam tol = new TokenOrListParam();
		tol.add("_id", "123");
		tokenAndListParm.addValue(tol);
		paramMap.add(Condition.SP_RES_ID, tokenAndListParm);
		assertTrue(ResourceProviderValidator.validateRequest(paramMap, ResourceTypes.CONDITION.toString()));
	}

	@Test
	void testConditionPatientDate() {
		SearchParameterMap paramMap = new SearchParameterMap();
		ReferenceAndListParam patient = new ReferenceAndListParam();
		ReferenceOrListParam rolp = new ReferenceOrListParam();
		ReferenceParam rpd = new ReferenceParam();
		rpd.setValue("123");
		rolp.add(rpd);
		patient.addValue(rolp);
		paramMap.add(Condition.SP_PATIENT, patient);

		DateAndListParam dateAndListParam = new DateAndListParam();
		DateOrListParam dolp = new DateOrListParam();
		DateParam dp = new DateParam();
		dp.setValue(new Date());
		dolp.add(dp);
		dateAndListParam.addValue(dolp);
		paramMap.add(Condition.SP_ONSET_DATE, dateAndListParam);

		assertTrue(ResourceProviderValidator.validateRequest(paramMap, ResourceTypes.CONDITION.toString()));
	}

	@Test
	void testConditionPatientCategoryEncounter() {
		SearchParameterMap paramMap = new SearchParameterMap();
		ReferenceAndListParam patient = new ReferenceAndListParam();
		ReferenceOrListParam rolp = new ReferenceOrListParam();
		ReferenceParam rpd = new ReferenceParam();
		rpd.setValue("123");
		rolp.add(rpd);
		patient.addValue(rolp);
		paramMap.add(Condition.SP_PATIENT, patient);

		TokenAndListParam talp = new TokenAndListParam();
		TokenOrListParam tp = new TokenOrListParam();
		tp.add(Condition.SP_CLINICAL_STATUS, "active");
		talp.addValue(tp);
		TokenAndListParam tokenAndListParam = new TokenAndListParam();
		TokenOrListParam tolp = new TokenOrListParam();
		tolp.add(Condition.SP_CATEGORY, "active");
		tokenAndListParam.addValue(tolp);
		paramMap.add(CarePlan.SP_CATEGORY, tokenAndListParam);

		ReferenceAndListParam encounter = new ReferenceAndListParam();
		ReferenceOrListParam rolpe = new ReferenceOrListParam();
		ReferenceParam rpde = new ReferenceParam();
		rpde.setValue("1234");
		rolpe.add(rpd);
		encounter.addValue(rolpe);
		paramMap.add("encounter", encounter);

		assertTrue(ResourceProviderValidator.validateRequest(paramMap, ResourceTypes.CONDITION.toString()));
	}

	@Test
	void testConditionPatientCategory() {
		SearchParameterMap paramMap = new SearchParameterMap();
		ReferenceAndListParam patient = new ReferenceAndListParam();
		ReferenceOrListParam rolp = new ReferenceOrListParam();
		ReferenceParam rpd = new ReferenceParam();
		rpd.setValue("123");
		rolp.add(rpd);
		patient.addValue(rolp);
		paramMap.add(Condition.SP_PATIENT, patient);

		TokenAndListParam tokenAndListParam = new TokenAndListParam();
		TokenOrListParam tolp = new TokenOrListParam();
		tolp.add(Condition.SP_CATEGORY, "active");
		tokenAndListParam.addValue(tolp);
		paramMap.add(CarePlan.SP_CATEGORY, tokenAndListParam);

		assertTrue(ResourceProviderValidator.validateRequest(paramMap, ResourceTypes.CONDITION.toString()));
	}

	@Test
	void testConditionPatient() {
		SearchParameterMap paramMap = new SearchParameterMap();
		ReferenceAndListParam patient = new ReferenceAndListParam();
		ReferenceOrListParam rolp = new ReferenceOrListParam();
		ReferenceParam rpd = new ReferenceParam();
		rpd.setValue("123");
		rolp.add(rpd);
		patient.addValue(rolp);
		paramMap.add(Condition.SP_PATIENT, patient);

		assertTrue(ResourceProviderValidator.validateRequest(paramMap, ResourceTypes.CONDITION.toString()));
	}

	@Test
	void testDeviceidTest() {
		SearchParameterMap paramMap = new SearchParameterMap();
		paramMap = new SearchParameterMap();
		TokenAndListParam tokenAndListParm = new TokenAndListParam();
		TokenOrListParam tol = new TokenOrListParam();
		tol.add("_id", "123");
		tokenAndListParm.addValue(tol);
		paramMap.add(Device.SP_RES_ID, tokenAndListParm);
		assertTrue(ResourceProviderValidator.validateRequest(paramMap, ResourceTypes.DEVICE.toString()));
	}

	@Test
	void testDevicePatientTypeTest() {
		SearchParameterMap paramMap = new SearchParameterMap();
		ReferenceAndListParam patient = new ReferenceAndListParam();
		ReferenceOrListParam rolp = new ReferenceOrListParam();
		ReferenceParam rpd = new ReferenceParam();
		rpd.setValue("123");
		rolp.add(rpd);
		patient.addValue(rolp);
		paramMap.add(Device.SP_PATIENT, patient);

		TokenAndListParam toknAndListParam = new TokenAndListParam();
		TokenOrListParam top = new TokenOrListParam();
		top.add(Device.SP_TYPE, "normal");
		toknAndListParam.addValue(top);
		assertTrue(ResourceProviderValidator.validateRequest(paramMap, ResourceTypes.DEVICE.toString()));
	}

	@Test
	void testDevicePatientEncounterTest() {
		SearchParameterMap paramMap = new SearchParameterMap();
		ReferenceAndListParam patient = new ReferenceAndListParam();
		ReferenceOrListParam rolp = new ReferenceOrListParam();
		ReferenceParam rpd = new ReferenceParam();
		rpd.setValue("123");
		rolp.add(rpd);
		patient.addValue(rolp);
		paramMap.add(Device.SP_PATIENT, patient);
		ReferenceAndListParam encounter = new ReferenceAndListParam();
		ReferenceOrListParam rolpe = new ReferenceOrListParam();
		ReferenceParam rpde = new ReferenceParam();
		rpde.setValue("1234");
		rolpe.add(rpd);
		encounter.addValue(rolpe);
		paramMap.add("encounter", encounter);
		assertTrue(ResourceProviderValidator.validateRequest(paramMap, ResourceTypes.DEVICE.toString()));
	}

	@Test
	void testDevicePatientTest() {
		SearchParameterMap paramMap = new SearchParameterMap();
		ReferenceAndListParam patient = new ReferenceAndListParam();
		ReferenceOrListParam rolp = new ReferenceOrListParam();
		ReferenceParam rpd = new ReferenceParam();
		rpd.setValue("123");
		rolp.add(rpd);
		patient.addValue(rolp);
		paramMap.add(Device.SP_PATIENT, patient);
		assertTrue(ResourceProviderValidator.validateRequest(paramMap, ResourceTypes.DEVICE.toString()));
	}

	@Test
	void testDiagnosticReportReferenceTest() {
		SearchParameterMap paramMap = new SearchParameterMap();
		paramMap = new SearchParameterMap();
		TokenAndListParam tokenAndListParm = new TokenAndListParam();
		TokenOrListParam tol = new TokenOrListParam();
		tol.add("_id", "123");
		tokenAndListParm.addValue(tol);
		paramMap.add(DiagnosticReport.SP_RES_ID, tokenAndListParm);
		assertTrue(ResourceProviderValidator.validateRequest(paramMap, ResourceTypes.DIAGNOSTICREPORT.toString()));
	}

	@Test
	void testDiagnosticReportPatientCategoryDateTest() {
		SearchParameterMap paramMap = new SearchParameterMap();
		ReferenceAndListParam patient = new ReferenceAndListParam();
		ReferenceOrListParam rolp = new ReferenceOrListParam();
		ReferenceParam rpd = new ReferenceParam();
		rpd.setValue("123");
		rolp.add(rpd);
		patient.addValue(rolp);
		paramMap.add(DiagnosticReport.SP_PATIENT, patient);
		TokenAndListParam tokenAndListParam = new TokenAndListParam();

		TokenOrListParam tolp = new TokenOrListParam();
		tolp.add(DiagnosticReport.SP_CATEGORY, "active");
		tokenAndListParam.addValue(tolp);
		paramMap.add(DocumentReference.SP_CATEGORY, tokenAndListParam);

		DateAndListParam dateAndListParam = new DateAndListParam();
		DateOrListParam dolp = new DateOrListParam();
		DateParam dp = new DateParam();
		dp.setValue(new Date());
		dolp.add(dp);
		dateAndListParam.addValue(dolp);
		paramMap.add(DiagnosticReport.SP_DATE, dateAndListParam);
		assertTrue(ResourceProviderValidator.validateRequest(paramMap, ResourceTypes.DIAGNOSTICREPORT.toString()));
	}

	@Test
	void testDiagnosticReportPatientCodeDateTest() {
		SearchParameterMap paramMap = new SearchParameterMap();
		ReferenceAndListParam patient = new ReferenceAndListParam();
		ReferenceOrListParam rolp = new ReferenceOrListParam();
		ReferenceParam rpd = new ReferenceParam();
		rpd.setValue("123");
		rolp.add(rpd);
		patient.addValue(rolp);
		paramMap.add(Device.SP_PATIENT, patient);

		TokenAndListParam toknAndListParam = new TokenAndListParam();
		TokenOrListParam top = new TokenOrListParam();
		top.add(DiagnosticReport.SP_CODE, "1445");
		toknAndListParam.addValue(top);
		paramMap.add(DiagnosticReport.SP_CODE, toknAndListParam);

		DateAndListParam dateAndListParam = new DateAndListParam();
		DateOrListParam dolp = new DateOrListParam();
		DateParam dp = new DateParam();
		dp.setValue(new Date());
		dolp.add(dp);
		dateAndListParam.addValue(dolp);
		assertTrue(ResourceProviderValidator.validateRequest(paramMap, ResourceTypes.DIAGNOSTICREPORT.toString()));
	}

	@Test
	void testDiagnosticReportPatientCategoryTest() {
		SearchParameterMap paramMap = new SearchParameterMap();
		ReferenceAndListParam patient = new ReferenceAndListParam();
		ReferenceOrListParam rolp = new ReferenceOrListParam();
		ReferenceParam rpd = new ReferenceParam();
		rpd.setValue("123");
		rolp.add(rpd);
		patient.addValue(rolp);
		paramMap.add(DiagnosticReport.SP_PATIENT, patient);
		TokenAndListParam tokenAndListParam = new TokenAndListParam();

		TokenOrListParam tolp = new TokenOrListParam();
		tolp.add(DiagnosticReport.SP_CATEGORY, "active");
		tokenAndListParam.addValue(tolp);
		paramMap.add(DocumentReference.SP_CATEGORY, tokenAndListParam);

		assertTrue(ResourceProviderValidator.validateRequest(paramMap, ResourceTypes.DIAGNOSTICREPORT.toString()));
	}

	@Test
	void testDiagnosticReportPatientCodeTest() {
		SearchParameterMap paramMap = new SearchParameterMap();
		ReferenceAndListParam patient = new ReferenceAndListParam();
		ReferenceOrListParam rolp = new ReferenceOrListParam();
		ReferenceParam rpd = new ReferenceParam();
		rpd.setValue("123");
		rolp.add(rpd);
		patient.addValue(rolp);
		paramMap.add(DiagnosticReport.SP_PATIENT, patient);
		TokenAndListParam tokenAndListParam = new TokenAndListParam();

		TokenAndListParam toknAndListParam = new TokenAndListParam();
		TokenOrListParam top = new TokenOrListParam();
		top.add(DiagnosticReport.SP_CODE, "1445");
		toknAndListParam.addValue(top);
		paramMap.add(DiagnosticReport.SP_CODE, tokenAndListParam);

		assertTrue(ResourceProviderValidator.validateRequest(paramMap, ResourceTypes.DIAGNOSTICREPORT.toString()));
	}

	@Test
	void testDiagnosticReportPatientStatusTest() {
		SearchParameterMap paramMap = new SearchParameterMap();
		ReferenceAndListParam patient = new ReferenceAndListParam();
		ReferenceOrListParam rolp = new ReferenceOrListParam();
		ReferenceParam rpd = new ReferenceParam();
		rpd.setValue("123");
		rolp.add(rpd);
		patient.addValue(rolp);
		paramMap.add(DiagnosticReport.SP_PATIENT, patient);
		TokenAndListParam tokenAndListParam = new TokenAndListParam();
		TokenOrListParam tp = new TokenOrListParam();
		tp.add(DiagnosticReport.SP_STATUS, "active");
		tokenAndListParam.addValue(tp);
		paramMap.add(DiagnosticReport.SP_STATUS, tokenAndListParam);

		assertTrue(ResourceProviderValidator.validateRequest(paramMap, ResourceTypes.DIAGNOSTICREPORT.toString()));
	}

	@Test
	void testDiagnosticReportPatientEncounterTest() {
		SearchParameterMap paramMap = new SearchParameterMap();
		ReferenceAndListParam patient = new ReferenceAndListParam();
		ReferenceOrListParam rolp = new ReferenceOrListParam();
		ReferenceParam rpd = new ReferenceParam();
		rpd.setValue("123");
		rolp.add(rpd);
		patient.addValue(rolp);
		paramMap.add(DiagnosticReport.SP_PATIENT, patient);
		ReferenceAndListParam encounter = new ReferenceAndListParam();
		ReferenceOrListParam rolpe = new ReferenceOrListParam();
		ReferenceParam rpde = new ReferenceParam();
		rpde.setValue("1234");
		rolpe.add(rpd);
		encounter.addValue(rolpe);
		paramMap.add("encounter", encounter);

		assertTrue(ResourceProviderValidator.validateRequest(paramMap, ResourceTypes.DIAGNOSTICREPORT.toString()));
	}

	@Test
	void testDocumentReferenceIdTest() {
		SearchParameterMap paramMap = new SearchParameterMap();
		paramMap = new SearchParameterMap();
		TokenAndListParam tokenAndListParm = new TokenAndListParam();
		TokenOrListParam tol = new TokenOrListParam();
		tol.add("_id", "123");
		tokenAndListParm.addValue(tol);
		paramMap.add(DocumentReference.SP_RES_ID, tokenAndListParm);
		assertTrue(ResourceProviderValidator.validateRequest(paramMap, ResourceTypes.DOCUMENTREFERENCE.toString()));
	}

	@Test
	void testDocumentReferencePatientCategoryDateTest() {
		SearchParameterMap paramMap = new SearchParameterMap();
		ReferenceAndListParam patient = new ReferenceAndListParam();
		ReferenceOrListParam rolp = new ReferenceOrListParam();
		ReferenceParam rpd = new ReferenceParam();
		rpd.setValue("123");
		rolp.add(rpd);
		patient.addValue(rolp);
		paramMap.add(DocumentReference.SP_PATIENT, patient);

		TokenAndListParam tokenAndListParam = new TokenAndListParam();
		TokenOrListParam tolp = new TokenOrListParam();
		tolp.add(DocumentReference.SP_CATEGORY, "active");
		tokenAndListParam.addValue(tolp);
		paramMap.add(DocumentReference.SP_CATEGORY, tokenAndListParam);

		DateAndListParam dateAndListParam = new DateAndListParam();
		DateOrListParam dolp = new DateOrListParam();
		DateParam dp = new DateParam();
		dp.setValue(new Date());
		dolp.add(dp);
		dateAndListParam.addValue(dolp);
		paramMap.add(DocumentReference.SP_DATE, dateAndListParam);
		assertTrue(ResourceProviderValidator.validateRequest(paramMap, ResourceTypes.DOCUMENTREFERENCE.toString()));
	}

	@Test
	void testDocumentReferencePatientTypeDateTest() {
		SearchParameterMap paramMap = new SearchParameterMap();
		ReferenceAndListParam patient = new ReferenceAndListParam();
		ReferenceOrListParam rolp = new ReferenceOrListParam();
		ReferenceParam rpd = new ReferenceParam();
		rpd.setValue("123");
		rolp.add(rpd);
		patient.addValue(rolp);
		paramMap.add(DocumentReference.SP_PATIENT, patient);

		TokenAndListParam toknAndListParam = new TokenAndListParam();
		TokenOrListParam top = new TokenOrListParam();
		top.add(DocumentReference.SP_TYPE, "normal");
		toknAndListParam.addValue(top);
		paramMap.add(DocumentReference.SP_TYPE, toknAndListParam);

		DateAndListParam dateAndListParam = new DateAndListParam();
		DateOrListParam dolp = new DateOrListParam();
		DateParam dp = new DateParam();
		dp.setValue(new Date());
		dolp.add(dp);
		dateAndListParam.addValue(dolp);
		paramMap.add(DocumentReference.SP_DATE, dateAndListParam);
		assertTrue(ResourceProviderValidator.validateRequest(paramMap, ResourceTypes.DOCUMENTREFERENCE.toString()));
	}

	@Test
	void testDocumentReferencePatientCategoryTest() {
		SearchParameterMap paramMap = new SearchParameterMap();
		ReferenceAndListParam patient = new ReferenceAndListParam();
		ReferenceOrListParam rolp = new ReferenceOrListParam();
		ReferenceParam rpd = new ReferenceParam();
		rpd.setValue("123");
		rolp.add(rpd);
		patient.addValue(rolp);
		paramMap.add(DocumentReference.SP_PATIENT, patient);

		TokenAndListParam tokenAndListParam = new TokenAndListParam();
		TokenOrListParam tolp = new TokenOrListParam();
		tolp.add(DocumentReference.SP_CATEGORY, "active");
		tokenAndListParam.addValue(tolp);
		paramMap.add(DocumentReference.SP_CATEGORY, tokenAndListParam);
		assertTrue(ResourceProviderValidator.validateRequest(paramMap, ResourceTypes.DOCUMENTREFERENCE.toString()));
	}

	@Test
	void testDocumentReferencePatientTypeTest() {
		SearchParameterMap paramMap = new SearchParameterMap();
		ReferenceAndListParam patient = new ReferenceAndListParam();
		ReferenceOrListParam rolp = new ReferenceOrListParam();
		ReferenceParam rpd = new ReferenceParam();
		rpd.setValue("123");
		rolp.add(rpd);
		patient.addValue(rolp);
		paramMap.add(DocumentReference.SP_PATIENT, patient);

		TokenAndListParam toknAndListParam = new TokenAndListParam();
		TokenOrListParam top = new TokenOrListParam();
		top.add(DocumentReference.SP_TYPE, "normal");
		toknAndListParam.addValue(top);

		paramMap.add(DocumentReference.SP_TYPE, toknAndListParam);
		assertTrue(ResourceProviderValidator.validateRequest(paramMap, ResourceTypes.DOCUMENTREFERENCE.toString()));
	}

	@Test
	void testDocumentReferencePatientStatusTest() {
		SearchParameterMap paramMap = new SearchParameterMap();
		ReferenceAndListParam patient = new ReferenceAndListParam();
		ReferenceOrListParam rolp = new ReferenceOrListParam();
		ReferenceParam rpd = new ReferenceParam();
		rpd.setValue("123");
		rolp.add(rpd);
		patient.addValue(rolp);
		paramMap.add(DocumentReference.SP_PATIENT, patient);

		TokenAndListParam talp = new TokenAndListParam();
		TokenOrListParam tp = new TokenOrListParam();
		tp.add(DocumentReference.SP_STATUS, "active");
		talp.addValue(tp);
		paramMap.add(DocumentReference.SP_STATUS, talp);
		assertTrue(ResourceProviderValidator.validateRequest(paramMap, ResourceTypes.DOCUMENTREFERENCE.toString()));
	}

	@Test
	void testDocumentReferencePatientEncounterTest() {
		SearchParameterMap paramMap = new SearchParameterMap();
		ReferenceAndListParam patient = new ReferenceAndListParam();
		ReferenceOrListParam rolp = new ReferenceOrListParam();
		ReferenceParam rpd = new ReferenceParam();
		rpd.setValue("123");
		rolp.add(rpd);
		patient.addValue(rolp);
		paramMap.add(DocumentReference.SP_PATIENT, patient);

		ReferenceAndListParam encounter = new ReferenceAndListParam();
		ReferenceOrListParam rolpe = new ReferenceOrListParam();
		ReferenceParam rpde = new ReferenceParam();
		rpde.setValue("1234");
		rolpe.add(rpd);
		encounter.addValue(rolpe);
		paramMap.add("encounter", encounter);
		assertTrue(ResourceProviderValidator.validateRequest(paramMap, ResourceTypes.DOCUMENTREFERENCE.toString()));
	}

	@Test
	void testDocumentReferencePatientTest() {
		SearchParameterMap paramMap = new SearchParameterMap();
		ReferenceAndListParam patient = new ReferenceAndListParam();
		ReferenceOrListParam rolp = new ReferenceOrListParam();
		ReferenceParam rpd = new ReferenceParam();
		rpd.setValue("123");
		rolp.add(rpd);
		patient.addValue(rolp);
		paramMap.add(DocumentReference.SP_PATIENT, patient);

		assertTrue(ResourceProviderValidator.validateRequest(paramMap, ResourceTypes.DOCUMENTREFERENCE.toString()));
	}

	@Test
	void testEncounterIdTest() {
		SearchParameterMap paramMap = new SearchParameterMap();
		paramMap = new SearchParameterMap();
		TokenAndListParam tokenAndListParm = new TokenAndListParam();
		TokenOrListParam tol = new TokenOrListParam();
		tol.add("_id", "123");
		tokenAndListParm.addValue(tol);
		paramMap.add(Encounter.SP_RES_ID, tokenAndListParm);
		assertTrue(ResourceProviderValidator.validateRequest(paramMap, ResourceTypes.ENCOUNTER.toString()));
	}

	@Test
	void testEncounterPatientDateTest() {
		SearchParameterMap paramMap = new SearchParameterMap();
		ReferenceAndListParam patient = new ReferenceAndListParam();
		ReferenceOrListParam rolp = new ReferenceOrListParam();
		ReferenceParam rpd = new ReferenceParam();
		rpd.setValue("123");
		rolp.add(rpd);
		patient.addValue(rolp);
		paramMap.add(Encounter.SP_PATIENT, patient);
		DateAndListParam dateAndListParam = new DateAndListParam();
		DateOrListParam dolp = new DateOrListParam();
		DateParam dp = new DateParam();
		dp.setValue(new Date());
		dolp.add(dp);
		dateAndListParam.addValue(dolp);
		paramMap.add(Encounter.SP_DATE, dateAndListParam);
		assertTrue(ResourceProviderValidator.validateRequest(paramMap, ResourceTypes.ENCOUNTER.toString()));
	}

	@Test
	void testEncounterPatientClassTest() {
		SearchParameterMap paramMap = new SearchParameterMap();
		ReferenceAndListParam patient = new ReferenceAndListParam();
		ReferenceOrListParam rolp = new ReferenceOrListParam();
		ReferenceParam rpd = new ReferenceParam();
		rpd.setValue("123");
		rolp.add(rpd);
		patient.addValue(rolp);
		paramMap.add(Encounter.SP_PATIENT, patient);
		TokenAndListParam toknAndListParam = new TokenAndListParam();
		TokenOrListParam top = new TokenOrListParam();
		top.add(Encounter.SP_CLASS, "normal");
		toknAndListParam.addValue(top);

		paramMap.add(Encounter.SP_CLASS, toknAndListParam);
		assertTrue(ResourceProviderValidator.validateRequest(paramMap, ResourceTypes.ENCOUNTER.toString()));
	}

	@Test
	void testEncounterPatientTypeTest() {
		SearchParameterMap paramMap = new SearchParameterMap();
		ReferenceAndListParam patient = new ReferenceAndListParam();
		ReferenceOrListParam rolp = new ReferenceOrListParam();
		ReferenceParam rpd = new ReferenceParam();
		rpd.setValue("123");
		rolp.add(rpd);
		patient.addValue(rolp);
		paramMap.add(Encounter.SP_PATIENT, patient);

		TokenAndListParam toknAndListParam = new TokenAndListParam();
		TokenOrListParam tosp = new TokenOrListParam();
		tosp.add(Encounter.SP_TYPE, "normal");
		toknAndListParam.addValue(tosp);
		paramMap.add(Encounter.SP_TYPE, toknAndListParam);
		assertTrue(ResourceProviderValidator.validateRequest(paramMap, ResourceTypes.ENCOUNTER.toString()));
	}

	@Test
	void testEncounterPatientStatusTest() {
		SearchParameterMap paramMap = new SearchParameterMap();
		ReferenceAndListParam patient = new ReferenceAndListParam();
		ReferenceOrListParam rolp = new ReferenceOrListParam();
		ReferenceParam rpd = new ReferenceParam();
		rpd.setValue("123");
		rolp.add(rpd);
		patient.addValue(rolp);
		paramMap.add(Encounter.SP_PATIENT, patient);

		TokenAndListParam talp = new TokenAndListParam();
		TokenOrListParam tp = new TokenOrListParam();
		tp.add(DocumentReference.SP_STATUS, "active");
		talp.addValue(tp);
		paramMap.add(Encounter.SP_STATUS, talp);
		assertTrue(ResourceProviderValidator.validateRequest(paramMap, ResourceTypes.ENCOUNTER.toString()));
	}

	@Test
	void testEncounterPatientTest() {
		SearchParameterMap paramMap = new SearchParameterMap();
		ReferenceAndListParam patient = new ReferenceAndListParam();
		ReferenceOrListParam rolp = new ReferenceOrListParam();
		ReferenceParam rpd = new ReferenceParam();
		rpd.setValue("123");
		rolp.add(rpd);
		patient.addValue(rolp);
		paramMap.add(Encounter.SP_PATIENT, patient);

		assertTrue(ResourceProviderValidator.validateRequest(paramMap, ResourceTypes.ENCOUNTER.toString()));
	}

	@Test
	void testEncounterIdentifierTest() {
		SearchParameterMap paramMap = new SearchParameterMap();
		TokenAndListParam tokenAndListParm = new TokenAndListParam();
		TokenOrListParam tol = new TokenOrListParam();
		tol.add("_id", "123");
		tokenAndListParm.addValue(tol);
		paramMap.add(Encounter.SP_IDENTIFIER, tokenAndListParm);

		assertTrue(ResourceProviderValidator.validateRequest(paramMap, ResourceTypes.ENCOUNTER.toString()));
	}

	@Test
	void testGoalIdTest() {
		SearchParameterMap paramMap = new SearchParameterMap();
		paramMap = new SearchParameterMap();
		TokenAndListParam tokenAndListParm = new TokenAndListParam();
		TokenOrListParam tol = new TokenOrListParam();
		tol.add("_id", "123");
		tokenAndListParm.addValue(tol);
		paramMap.add(Goal.SP_RES_ID, tokenAndListParm);
		assertTrue(ResourceProviderValidator.validateRequest(paramMap, ResourceTypes.GOAL.toString()));
	}

	@Test
	void testGoalPatientLifeCycleStatusTest() {
		SearchParameterMap paramMap = new SearchParameterMap();
		ReferenceAndListParam patient = new ReferenceAndListParam();
		ReferenceOrListParam rolp = new ReferenceOrListParam();
		ReferenceParam rpd = new ReferenceParam();
		rpd.setValue("123");
		rolp.add(rpd);
		patient.addValue(rolp);
		paramMap.add(Encounter.SP_PATIENT, patient);

		TokenAndListParam toknAndListParam = new TokenAndListParam();
		TokenOrListParam top = new TokenOrListParam();
		top.add(Goal.SP_LIFECYCLE_STATUS, "normal");
		toknAndListParam.addValue(top);
		paramMap.add(Goal.SP_LIFECYCLE_STATUS, toknAndListParam);
		assertTrue(ResourceProviderValidator.validateRequest(paramMap, ResourceTypes.GOAL.toString()));
	}

	@Test
	void testGoalPatientEncounterTest() {
		SearchParameterMap paramMap = new SearchParameterMap();
		ReferenceAndListParam patient = new ReferenceAndListParam();
		ReferenceOrListParam rolp = new ReferenceOrListParam();
		ReferenceParam rpd = new ReferenceParam();
		rpd.setValue("123");
		rolp.add(rpd);
		patient.addValue(rolp);
		paramMap.add(Encounter.SP_PATIENT, patient);

		ReferenceAndListParam encounter = new ReferenceAndListParam();
		ReferenceOrListParam rolpe = new ReferenceOrListParam();
		ReferenceParam rpde = new ReferenceParam();
		rpde.setValue("1234");
		rolpe.add(rpd);
		encounter.addValue(rolpe);
		paramMap.add("encounter", encounter);
		assertTrue(ResourceProviderValidator.validateRequest(paramMap, ResourceTypes.GOAL.toString()));
	}

	@Test
	void testGoalPatientTest() {
		SearchParameterMap paramMap = new SearchParameterMap();
		ReferenceAndListParam patient = new ReferenceAndListParam();
		ReferenceOrListParam rolp = new ReferenceOrListParam();
		ReferenceParam rpd = new ReferenceParam();
		rpd.setValue("123");
		rolp.add(rpd);
		patient.addValue(rolp);
		paramMap.add(Encounter.SP_PATIENT, patient);
		assertTrue(ResourceProviderValidator.validateRequest(paramMap, ResourceTypes.GOAL.toString()));
	}

	@Test
	void testLocationIdTest() {
		SearchParameterMap paramMap = new SearchParameterMap();
		paramMap = new SearchParameterMap();
		TokenAndListParam tokenAndListParm = new TokenAndListParam();
		TokenOrListParam tol = new TokenOrListParam();
		tol.add("_id", "123");
		tokenAndListParm.addValue(tol);
		paramMap.add(Location.SP_RES_ID, tokenAndListParm);
		assertTrue(ResourceProviderValidator.validateRequest(paramMap, ResourceTypes.LOCATION.toString()));
	}

	@Test
	void testLocationNameTest() {
		SearchParameterMap paramMap = new SearchParameterMap();
		StringAndListParam salp1 = new StringAndListParam();
		StringOrListParam solp1 = new StringOrListParam();
		StringParam sp1 = new StringParam();
		sp1.setValue("Bangalore");
		solp1.add(sp1);
		salp1.addValue(solp1);
		paramMap.add(Location.SP_NAME, salp1);
		assertTrue(ResourceProviderValidator.validateRequest(paramMap, ResourceTypes.LOCATION.toString()));
	}

	@Test
	void testLocationAddressTest() {
		SearchParameterMap paramMap = new SearchParameterMap();
		StringAndListParam salp2 = new StringAndListParam();
		StringOrListParam solp2 = new StringOrListParam();
		StringParam sp2 = new StringParam();
		sp2.setValue("Bangalore");
		solp2.add(sp2);
		salp2.addValue(solp2);
		paramMap.add(Location.SP_ADDRESS, salp2);
		assertTrue(ResourceProviderValidator.validateRequest(paramMap, ResourceTypes.LOCATION.toString()));
	}

	@Test
	void testLocationAddressCityTest() {
		SearchParameterMap paramMap = new SearchParameterMap();
		StringAndListParam salp3 = new StringAndListParam();
		StringOrListParam solp3 = new StringOrListParam();
		StringParam sp3 = new StringParam();
		sp3.setValue("Bangalore");
		solp3.add(sp3);
		salp3.addValue(solp3);
		paramMap.add(Location.SP_ADDRESS_CITY, salp3);
		assertTrue(ResourceProviderValidator.validateRequest(paramMap, ResourceTypes.LOCATION.toString()));
	}

	@Test
	void testLocationAddressPostalCodeTest() {
		SearchParameterMap paramMap = new SearchParameterMap();
		StringAndListParam salp5 = new StringAndListParam();
		StringOrListParam solp5 = new StringOrListParam();
		StringParam sp5 = new StringParam();
		sp5.setValue("781098");
		solp5.add(sp5);
		salp5.addValue(solp5);
		paramMap.add(Location.SP_ADDRESS_POSTALCODE, salp5);
		assertTrue(ResourceProviderValidator.validateRequest(paramMap, ResourceTypes.LOCATION.toString()));
	}

	@Test
	void testLocationAddressStateTest() {
		SearchParameterMap paramMap = new SearchParameterMap();
		StringAndListParam salp4 = new StringAndListParam();
		StringOrListParam solp4 = new StringOrListParam();
		StringParam sp4 = new StringParam();
		sp4.setValue("Karnataka");
		solp4.add(sp4);
		salp4.addValue(solp4);
		paramMap.add(Location.SP_ADDRESS_STATE, salp4);
		assertTrue(ResourceProviderValidator.validateRequest(paramMap, ResourceTypes.LOCATION.toString()));
	}

	@Test
	void testOrganizationIdTest() {
		SearchParameterMap paramMap = new SearchParameterMap();
		paramMap = new SearchParameterMap();
		TokenAndListParam tokenAndListParm = new TokenAndListParam();
		TokenOrListParam tol = new TokenOrListParam();
		tol.add("_id", "123");
		tokenAndListParm.addValue(tol);
		paramMap.add(Organization.SP_RES_ID, tokenAndListParm);
		assertTrue(ResourceProviderValidator.validateRequest(paramMap, ResourceTypes.ORGANIZATION.toString()));
	}

	@Test
	void testOrganizationAddressTest() {
		SearchParameterMap paramMap = new SearchParameterMap();
		StringAndListParam salp2 = new StringAndListParam();
		StringOrListParam solp2 = new StringOrListParam();
		StringParam sp2 = new StringParam();
		sp2.setValue("Bangalore");
		solp2.add(sp2);
		salp2.addValue(solp2);
		paramMap.add(Organization.SP_ADDRESS, salp2);
		assertTrue(ResourceProviderValidator.validateRequest(paramMap, ResourceTypes.ORGANIZATION.toString()));
	}

	@Test
	void testOrganizationNameTest() {
		SearchParameterMap paramMap = new SearchParameterMap();
		StringAndListParam salp1 = new StringAndListParam();
		StringOrListParam solp1 = new StringOrListParam();
		StringParam sp1 = new StringParam();
		sp1.setValue("Bangalore");
		solp1.add(sp1);
		salp1.addValue(solp1);
		paramMap.add(Organization.SP_NAME, salp1);
		assertTrue(ResourceProviderValidator.validateRequest(paramMap, ResourceTypes.ORGANIZATION.toString()));
	}

	@Test
	void testMedicationRequestIdTest() {
		SearchParameterMap paramMap = new SearchParameterMap();
		paramMap = new SearchParameterMap();
		TokenAndListParam tokenAndListParm = new TokenAndListParam();
		TokenOrListParam tol = new TokenOrListParam();
		tol.add("_id", "123");
		tokenAndListParm.addValue(tol);
		paramMap.add(MedicationRequest.SP_RES_ID, tokenAndListParm);
		assertTrue(ResourceProviderValidator.validateRequest(paramMap, ResourceTypes.MEDICATIONREQUEST.toString()));
	}

	@Test
	void testMedicationRequestPatientIntentTest() {
		SearchParameterMap paramMap = new SearchParameterMap();
		ReferenceAndListParam patient = new ReferenceAndListParam();
		ReferenceOrListParam rolp = new ReferenceOrListParam();
		ReferenceParam rpd = new ReferenceParam();
		rpd.setValue("123");
		rolp.add(rpd);
		patient.addValue(rolp);
		paramMap.add(MedicationRequest.SP_PATIENT, patient);

		TokenAndListParam toknAndListParam = new TokenAndListParam();
		TokenOrListParam top = new TokenOrListParam();
		top.add(MedicationRequest.SP_INTENT, "normal");
		toknAndListParam.addValue(top);
		paramMap.add(MedicationRequest.SP_INTENT, toknAndListParam);
		assertTrue(ResourceProviderValidator.validateRequest(paramMap, ResourceTypes.MEDICATIONREQUEST.toString()));
	}

	@Test
	void testMedicationRequestPatientMedicationTest() {
		SearchParameterMap paramMap = new SearchParameterMap();
		ReferenceAndListParam patient = new ReferenceAndListParam();
		ReferenceOrListParam rolp = new ReferenceOrListParam();
		ReferenceParam rpd = new ReferenceParam();
		rpd.setValue("123");
		rolp.add(rpd);
		patient.addValue(rolp);
		paramMap.add(MedicationRequest.SP_PATIENT, patient);

		TokenAndListParam toknAndListParam = new TokenAndListParam();
		TokenOrListParam top = new TokenOrListParam();
		top.add(MedicationRequest.SP_INTENT, "normal");
		toknAndListParam.addValue(top);
		paramMap.add(MedicationRequest.SP_INTENT, toknAndListParam);

		Set<Include> theIncludes = new HashSet<>();
		Include o = new Include("MedicationRequest:medication");
		theIncludes.add(o);
		paramMap.setIncludes(theIncludes);
		assertTrue(ResourceProviderValidator.validateRequest(paramMap, ResourceTypes.MEDICATIONREQUEST.toString()));
	}

	@Test
	void testMedicationRequestPatientIntentEncounterTest() {
		SearchParameterMap paramMap = new SearchParameterMap();
		ReferenceAndListParam patient = new ReferenceAndListParam();
		ReferenceOrListParam rolp = new ReferenceOrListParam();
		ReferenceParam rpd = new ReferenceParam();
		rpd.setValue("123");
		rolp.add(rpd);
		patient.addValue(rolp);
		paramMap.add(MedicationRequest.SP_PATIENT, patient);

		TokenAndListParam toknAndListParam = new TokenAndListParam();
		TokenOrListParam top = new TokenOrListParam();
		top.add(MedicationRequest.SP_INTENT, "normal");
		toknAndListParam.addValue(top);
		paramMap.add(MedicationRequest.SP_INTENT, toknAndListParam);
		ReferenceAndListParam encounter = new ReferenceAndListParam();
		ReferenceOrListParam rolpe = new ReferenceOrListParam();
		ReferenceParam rpde = new ReferenceParam();
		rpde.setValue("1234");
		rolpe.add(rpd);
		encounter.addValue(rolpe);
		paramMap.add("encounter", encounter);
		assertTrue(ResourceProviderValidator.validateRequest(paramMap, ResourceTypes.MEDICATIONREQUEST.toString()));
	}

	@Test
	void testMedicationRequestPatientEncounterTest() {
		SearchParameterMap paramMap = new SearchParameterMap();
		ReferenceAndListParam patient = new ReferenceAndListParam();
		ReferenceOrListParam rolp = new ReferenceOrListParam();
		ReferenceParam rpd = new ReferenceParam();
		rpd.setValue("123");
		rolp.add(rpd);
		patient.addValue(rolp);
		paramMap.add(MedicationRequest.SP_PATIENT, patient);

		ReferenceAndListParam encounter = new ReferenceAndListParam();
		ReferenceOrListParam rolpe = new ReferenceOrListParam();
		ReferenceParam rpde = new ReferenceParam();
		rpde.setValue("1234");
		rolpe.add(rpd);
		encounter.addValue(rolpe);
		paramMap.add("encounter", encounter);
		assertTrue(ResourceProviderValidator.validateRequest(paramMap, ResourceTypes.MEDICATIONREQUEST.toString()));
	}

	@Test
	void testMedicationRequestPatientEncounterMedicationTest() {
		SearchParameterMap paramMap = new SearchParameterMap();
		ReferenceAndListParam patient = new ReferenceAndListParam();
		ReferenceOrListParam rolp = new ReferenceOrListParam();
		ReferenceParam rpd = new ReferenceParam();
		rpd.setValue("123");
		rolp.add(rpd);
		patient.addValue(rolp);
		paramMap.add(MedicationRequest.SP_PATIENT, patient);

		ReferenceAndListParam encounter = new ReferenceAndListParam();
		ReferenceOrListParam rolpe = new ReferenceOrListParam();
		ReferenceParam rpde = new ReferenceParam();
		rpde.setValue("1234");
		rolpe.add(rpd);
		encounter.addValue(rolpe);
		paramMap.add("encounter", encounter);

		Set<Include> theIncludes = new HashSet<>();
		Include o = new Include("MedicationRequest:medication");
		theIncludes.add(o);
		paramMap.setIncludes(theIncludes);
		assertTrue(ResourceProviderValidator.validateRequest(paramMap, ResourceTypes.MEDICATIONREQUEST.toString()));
	}

	@Test
	void testMedicationRequestPatientIntentStatusTest() {
		SearchParameterMap paramMap = new SearchParameterMap();
		ReferenceAndListParam patient = new ReferenceAndListParam();
		ReferenceOrListParam rolp = new ReferenceOrListParam();
		ReferenceParam rpd = new ReferenceParam();
		rpd.setValue("123");
		rolp.add(rpd);
		patient.addValue(rolp);
		paramMap.add(MedicationRequest.SP_PATIENT, patient);

		TokenAndListParam toknAndListParam = new TokenAndListParam();
		TokenOrListParam top = new TokenOrListParam();
		top.add(MedicationRequest.SP_INTENT, "normal");
		toknAndListParam.addValue(top);
		paramMap.add(MedicationRequest.SP_INTENT, toknAndListParam);

		TokenAndListParam talp = new TokenAndListParam();
		TokenOrListParam tp = new TokenOrListParam();
		tp.add(MedicationRequest.SP_STATUS, "active");
		talp.addValue(tp);
		paramMap.add(MedicationRequest.SP_STATUS, talp);
		assertTrue(ResourceProviderValidator.validateRequest(paramMap, ResourceTypes.MEDICATIONREQUEST.toString()));
	}

	@Test
	void testMedicationRequestPatientIntentStatusMedicationTest() {
		SearchParameterMap paramMap = new SearchParameterMap();
		ReferenceAndListParam patient = new ReferenceAndListParam();
		ReferenceOrListParam rolp = new ReferenceOrListParam();
		ReferenceParam rpd = new ReferenceParam();
		rpd.setValue("123");
		rolp.add(rpd);
		patient.addValue(rolp);
		paramMap.add(MedicationRequest.SP_PATIENT, patient);

		TokenAndListParam toknAndListParam = new TokenAndListParam();
		TokenOrListParam top = new TokenOrListParam();
		top.add(MedicationRequest.SP_INTENT, "normal");
		toknAndListParam.addValue(top);
		paramMap.add(MedicationRequest.SP_INTENT, toknAndListParam);

		TokenAndListParam talp = new TokenAndListParam();
		TokenOrListParam tp = new TokenOrListParam();
		tp.add(MedicationRequest.SP_STATUS, "active");
		talp.addValue(tp);
		paramMap.add(MedicationRequest.SP_STATUS, talp);

		Set<Include> theIncludes = new HashSet<>();
		Include o = new Include("MedicationRequest:medication");
		theIncludes.add(o);
		paramMap.setIncludes(theIncludes);
		assertTrue(ResourceProviderValidator.validateRequest(paramMap, ResourceTypes.MEDICATIONREQUEST.toString()));
	}

	@Test
	void testMedicationRequestPatientIntentAuthoredOnTest() {
		SearchParameterMap paramMap = new SearchParameterMap();
		ReferenceAndListParam patient = new ReferenceAndListParam();
		ReferenceOrListParam rolp = new ReferenceOrListParam();
		ReferenceParam rpd = new ReferenceParam();
		rpd.setValue("123");
		rolp.add(rpd);
		patient.addValue(rolp);
		paramMap.add(MedicationRequest.SP_PATIENT, patient);

		TokenAndListParam toknAndListParam = new TokenAndListParam();
		TokenOrListParam top = new TokenOrListParam();
		top.add(MedicationRequest.SP_INTENT, "normal");
		toknAndListParam.addValue(top);
		paramMap.add(MedicationRequest.SP_INTENT, toknAndListParam);

		DateAndListParam dateAndListParam = new DateAndListParam();
		DateOrListParam dolp = new DateOrListParam();
		DateParam dp = new DateParam();
		dp.setValue(new Date());
		dolp.add(dp);
		dateAndListParam.addValue(dolp);
		paramMap.add(MedicationRequest.SP_AUTHOREDON, dateAndListParam);

		assertTrue(ResourceProviderValidator.validateRequest(paramMap, ResourceTypes.MEDICATIONREQUEST.toString()));
	}

	@Test
	void testMedicationRequestPatientIntentAuthoredOnMedicationTest() {
		SearchParameterMap paramMap = new SearchParameterMap();
		ReferenceAndListParam patient = new ReferenceAndListParam();
		ReferenceOrListParam rolp = new ReferenceOrListParam();
		ReferenceParam rpd = new ReferenceParam();
		rpd.setValue("123");
		rolp.add(rpd);
		patient.addValue(rolp);
		paramMap.add(MedicationRequest.SP_PATIENT, patient);

		TokenAndListParam toknAndListParam = new TokenAndListParam();
		TokenOrListParam top = new TokenOrListParam();
		top.add(MedicationRequest.SP_INTENT, "normal");
		toknAndListParam.addValue(top);
		paramMap.add(MedicationRequest.SP_INTENT, toknAndListParam);

		DateAndListParam dateAndListParam = new DateAndListParam();
		DateOrListParam dolp = new DateOrListParam();
		DateParam dp = new DateParam();
		dp.setValue(new Date());
		dolp.add(dp);
		dateAndListParam.addValue(dolp);
		paramMap.add(MedicationRequest.SP_AUTHOREDON, dateAndListParam);

		Set<Include> theIncludes = new HashSet<>();
		Include o = new Include("MedicationRequest:medication");
		theIncludes.add(o);
		paramMap.setIncludes(theIncludes);

		assertTrue(ResourceProviderValidator.validateRequest(paramMap, ResourceTypes.MEDICATIONREQUEST.toString()));
	}

	@Test
	void testMedicationRequestPatientAuthoredOnTest() {
		SearchParameterMap paramMap = new SearchParameterMap();
		ReferenceAndListParam patient = new ReferenceAndListParam();
		ReferenceOrListParam rolp = new ReferenceOrListParam();
		ReferenceParam rpd = new ReferenceParam();
		rpd.setValue("123");
		rolp.add(rpd);
		patient.addValue(rolp);
		paramMap.add(MedicationRequest.SP_PATIENT, patient);

		DateAndListParam dateAndListParam = new DateAndListParam();
		DateOrListParam dolp = new DateOrListParam();
		DateParam dp = new DateParam();
		dp.setValue(new Date());
		dolp.add(dp);
		dateAndListParam.addValue(dolp);
		paramMap.add(MedicationRequest.SP_AUTHOREDON, dateAndListParam);
		assertTrue(ResourceProviderValidator.validateRequest(paramMap, ResourceTypes.MEDICATIONREQUEST.toString()));
	}

	@Test
	void testMedicationRequestPatientAuthoredOnMedicationTest() {
		SearchParameterMap paramMap = new SearchParameterMap();
		ReferenceAndListParam patient = new ReferenceAndListParam();
		ReferenceOrListParam rolp = new ReferenceOrListParam();
		ReferenceParam rpd = new ReferenceParam();
		rpd.setValue("123");
		rolp.add(rpd);
		patient.addValue(rolp);
		paramMap.add(MedicationRequest.SP_PATIENT, patient);

		DateAndListParam dateAndListParam = new DateAndListParam();
		DateOrListParam dolp = new DateOrListParam();
		DateParam dp = new DateParam();
		dp.setValue(new Date());
		dolp.add(dp);
		dateAndListParam.addValue(dolp);
		paramMap.add(MedicationRequest.SP_AUTHOREDON, dateAndListParam);

		Set<Include> theIncludes = new HashSet<>();
		Include o = new Include("MedicationRequest:medication");
		theIncludes.add(o);
		paramMap.setIncludes(theIncludes);
		assertTrue(ResourceProviderValidator.validateRequest(paramMap, ResourceTypes.MEDICATIONREQUEST.toString()));
	}

	@Test
	void testMedicationRequestPatientTest() {
		SearchParameterMap paramMap = new SearchParameterMap();
		ReferenceAndListParam patient = new ReferenceAndListParam();
		ReferenceOrListParam rolp = new ReferenceOrListParam();
		ReferenceParam rpd = new ReferenceParam();
		rpd.setValue("123");
		rolp.add(rpd);
		patient.addValue(rolp);
		paramMap.add(MedicationRequest.SP_PATIENT, patient);

		assertTrue(ResourceProviderValidator.validateRequest(paramMap, ResourceTypes.MEDICATIONREQUEST.toString()));
	}

	@Test
	void testObservationIdTest() {
		SearchParameterMap paramMap = new SearchParameterMap();
		TokenAndListParam tokenAndListParm = new TokenAndListParam();
		TokenOrListParam tol = new TokenOrListParam();
		tol.add("_id", "123");
		tokenAndListParm.addValue(tol);

		paramMap.add(Observation.SP_RES_ID, tokenAndListParm);

		assertTrue(ResourceProviderValidator.validateRequest(paramMap, ResourceTypes.OBSERVATION.toString()));
	}

	@Test
	void testObservationPatientCategoryDateTest() {
		SearchParameterMap paramMap = new SearchParameterMap();
		ReferenceAndListParam patient = new ReferenceAndListParam();
		ReferenceOrListParam rolp = new ReferenceOrListParam();
		ReferenceParam rpd = new ReferenceParam();
		rpd.setValue("123");
		rolp.add(rpd);
		patient.addValue(rolp);
		paramMap.add(Observation.SP_PATIENT, patient);

		TokenAndListParam tokenAndListParam = new TokenAndListParam();
		TokenOrListParam tolp = new TokenOrListParam();
		tolp.add(Observation.SP_CATEGORY, "active");
		tokenAndListParam.addValue(tolp);
		paramMap.add(Observation.SP_CATEGORY, tokenAndListParam);

		DateAndListParam dateAndListParam = new DateAndListParam();
		DateOrListParam dolp = new DateOrListParam();
		DateParam dp = new DateParam();
		dp.setValue(new Date());
		dolp.add(dp);
		dateAndListParam.addValue(dolp);
		paramMap.add(Observation.SP_DATE, dateAndListParam);

		assertTrue(ResourceProviderValidator.validateRequest(paramMap, ResourceTypes.OBSERVATION.toString()));
	}

	@Test
	void testObservationPatientCodeDateTest() {
		SearchParameterMap paramMap = new SearchParameterMap();
		ReferenceAndListParam patient = new ReferenceAndListParam();
		ReferenceOrListParam rolp = new ReferenceOrListParam();
		ReferenceParam rpd = new ReferenceParam();
		rpd.setValue("123");
		rolp.add(rpd);
		patient.addValue(rolp);
		paramMap.add(Observation.SP_PATIENT, patient);

		TokenAndListParam tap = new TokenAndListParam();
		TokenOrListParam tpp = new TokenOrListParam();
		tpp.add(Observation.SP_CODE, "active");
		tap.addValue(tpp);
		paramMap.add(Observation.SP_CODE, tap);

		DateAndListParam dateAndListParam = new DateAndListParam();
		DateOrListParam dolp = new DateOrListParam();
		DateParam dp = new DateParam();
		dp.setValue(new Date());
		dolp.add(dp);
		dateAndListParam.addValue(dolp);
		paramMap.add(Observation.SP_DATE, dateAndListParam);

		assertTrue(ResourceProviderValidator.validateRequest(paramMap, ResourceTypes.OBSERVATION.toString()));
	}

	@Test
	void testObservationPatientCategoryStatusTest() {
		SearchParameterMap paramMap = new SearchParameterMap();
		ReferenceAndListParam patient = new ReferenceAndListParam();
		ReferenceOrListParam rolp = new ReferenceOrListParam();
		ReferenceParam rpd = new ReferenceParam();
		rpd.setValue("123");
		rolp.add(rpd);
		patient.addValue(rolp);
		paramMap.add(Observation.SP_PATIENT, patient);

		TokenAndListParam tokenAndListParam = new TokenAndListParam();
		TokenOrListParam tolp = new TokenOrListParam();
		tolp.add(Observation.SP_CATEGORY, "active");
		tokenAndListParam.addValue(tolp);

		paramMap.add(Observation.SP_CATEGORY, tokenAndListParam);

		TokenAndListParam talp = new TokenAndListParam();
		TokenOrListParam tp = new TokenOrListParam();
		tp.add(Observation.SP_STATUS, "active");
		talp.addValue(tp);
		paramMap.add(Observation.SP_STATUS, talp);

		assertTrue(ResourceProviderValidator.validateRequest(paramMap, ResourceTypes.OBSERVATION.toString()));
	}

	@Test
	void testObservationPatientCategoryEncounterTest() {
		SearchParameterMap paramMap = new SearchParameterMap();
		ReferenceAndListParam patient = new ReferenceAndListParam();
		ReferenceOrListParam rolp = new ReferenceOrListParam();
		ReferenceParam rpd = new ReferenceParam();
		rpd.setValue("123");
		rolp.add(rpd);
		patient.addValue(rolp);
		paramMap.add(Observation.SP_PATIENT, patient);

		TokenAndListParam tokenAndListParam = new TokenAndListParam();
		TokenOrListParam tolp = new TokenOrListParam();
		tolp.add(Observation.SP_CATEGORY, "active");
		tokenAndListParam.addValue(tolp);

		paramMap.add(Observation.SP_CATEGORY, tokenAndListParam);

		ReferenceAndListParam encounter = new ReferenceAndListParam();
		ReferenceOrListParam rolpe = new ReferenceOrListParam();
		ReferenceParam rpde = new ReferenceParam();
		rpde.setValue("1234");
		rolpe.add(rpd);
		encounter.addValue(rolpe);
		paramMap.add("encounter", encounter);

		assertTrue(ResourceProviderValidator.validateRequest(paramMap, ResourceTypes.OBSERVATION.toString()));
	}

	@Test
	void testObservationPatientCategoryTest() {
		SearchParameterMap paramMap = new SearchParameterMap();
		ReferenceAndListParam patient = new ReferenceAndListParam();
		ReferenceOrListParam rolp = new ReferenceOrListParam();
		ReferenceParam rpd = new ReferenceParam();
		rpd.setValue("123");
		rolp.add(rpd);
		patient.addValue(rolp);
		paramMap.add(Observation.SP_PATIENT, patient);

		TokenAndListParam tokenAndListParam = new TokenAndListParam();
		TokenOrListParam tolp = new TokenOrListParam();
		tolp.add(Observation.SP_CATEGORY, "active");
		tokenAndListParam.addValue(tolp);
		paramMap.add(Observation.SP_CATEGORY, tokenAndListParam);

		assertTrue(ResourceProviderValidator.validateRequest(paramMap, ResourceTypes.OBSERVATION.toString()));
	}

	@Test
	void testObservationPatientCodeTest() {
		SearchParameterMap paramMap = new SearchParameterMap();
		ReferenceAndListParam patient = new ReferenceAndListParam();
		ReferenceOrListParam rolp = new ReferenceOrListParam();
		ReferenceParam rpd = new ReferenceParam();
		rpd.setValue("123");
		rolp.add(rpd);
		patient.addValue(rolp);
		paramMap.add(Observation.SP_PATIENT, patient);

		TokenAndListParam tap = new TokenAndListParam();
		TokenOrListParam tpp = new TokenOrListParam();
		tpp.add(Observation.SP_CODE, "active");
		tap.addValue(tpp);
		paramMap.add(Observation.SP_CODE, tap);
		assertTrue(ResourceProviderValidator.validateRequest(paramMap, ResourceTypes.OBSERVATION.toString()));
	}

	@Test
	void testPatientIdTest() {
		SearchParameterMap paramMap = new SearchParameterMap();
		TokenAndListParam tlp = new TokenAndListParam();
		TokenOrListParam tl = new TokenOrListParam();
		tl.add("_id", "123");
		tlp.addValue(tl);
		paramMap.add(Patient.SP_RES_ID, tlp);
		assertTrue(ResourceProviderValidator.validateRequest(paramMap, ResourceTypes.PATIENT.toString()));
	}

	@Test
	void testPatientNameGenderTest() {
		SearchParameterMap paramMap = new SearchParameterMap();
		StringAndListParam salp1 = new StringAndListParam();
		StringOrListParam solp1 = new StringOrListParam();
		StringParam sp1 = new StringParam();
		sp1.setValue("Bangalore");
		solp1.add(sp1);
		salp1.addValue(solp1);
		paramMap.add(Patient.SP_NAME, salp1);

		TokenAndListParam tokenAndListParam = new TokenAndListParam();
		TokenOrListParam to = new TokenOrListParam();
		to.add(Patient.SP_GENDER, "male");
		tokenAndListParam.addValue(to);
		paramMap.add(Patient.SP_GENDER, tokenAndListParam);
		assertTrue(ResourceProviderValidator.validateRequest(paramMap, ResourceTypes.PATIENT.toString()));
	}

	@Test
	void testPatientNameBirthDateTest() {
		SearchParameterMap paramMap = new SearchParameterMap();
		StringAndListParam salp1 = new StringAndListParam();
		StringOrListParam solp1 = new StringOrListParam();
		StringParam sp1 = new StringParam();
		sp1.setValue("Bangalore");
		solp1.add(sp1);
		salp1.addValue(solp1);
		paramMap.add(Patient.SP_NAME, salp1);

		DateAndListParam dateAndListParam = new DateAndListParam();
		DateOrListParam dolp = new DateOrListParam();
		DateParam dp = new DateParam();
		dp.setValue(new Date());
		dolp.add(dp);
		dateAndListParam.addValue(dolp);
		assertTrue(ResourceProviderValidator.validateRequest(paramMap, ResourceTypes.PATIENT.toString()));
	}

	@Test
	void testPatientFamilyBirthDateTest() {
		SearchParameterMap paramMap = new SearchParameterMap();
		StringAndListParam sal = new StringAndListParam();
		StringOrListParam sol = new StringOrListParam();
		StringParam sp = new StringParam();
		sp.setValue("Bangalore");
		sol.add(sp);
		sal.addValue(sol);
		paramMap.add(Patient.SP_FAMILY, sal);

		DateAndListParam dateAndListParam = new DateAndListParam();
		DateOrListParam dolp = new DateOrListParam();
		DateParam dp = new DateParam();
		dp.setValue(new Date());
		dolp.add(dp);
		dateAndListParam.addValue(dolp);
		paramMap.add(Patient.SP_BIRTHDATE, sal);
		assertTrue(ResourceProviderValidator.validateRequest(paramMap, ResourceTypes.PATIENT.toString()));
	}

	@Test
	void testPatientFamilyGenderTest() {
		SearchParameterMap paramMap = new SearchParameterMap();
		StringAndListParam sal = new StringAndListParam();
		StringOrListParam sol = new StringOrListParam();
		StringParam sp = new StringParam();
		sp.setValue("Bangalore");
		sol.add(sp);
		sal.addValue(sol);
		paramMap.add(Patient.SP_FAMILY, sal);

		TokenAndListParam token = new TokenAndListParam();
		TokenOrListParam to = new TokenOrListParam();
		to.add(Patient.SP_GENDER, "male");
		token.addValue(to);
		paramMap.add(Patient.SP_GENDER, token);
		assertTrue(ResourceProviderValidator.validateRequest(paramMap, ResourceTypes.PATIENT.toString()));
	}

	@Test
	void testPatientNameTest() {
		SearchParameterMap paramMap = new SearchParameterMap();
		StringAndListParam salp1 = new StringAndListParam();
		StringOrListParam solp1 = new StringOrListParam();
		StringParam sp1 = new StringParam();
		sp1.setValue("Bangalore");
		solp1.add(sp1);
		salp1.addValue(solp1);
		paramMap.add(Patient.SP_NAME, salp1);
		assertTrue(ResourceProviderValidator.validateRequest(paramMap, ResourceTypes.PATIENT.toString()));
	}

	@Test
	void testPatientIdentifierTest() {
		SearchParameterMap paramMap = new SearchParameterMap();
		TokenAndListParam tokenAndListParm = new TokenAndListParam();
		TokenOrListParam tol = new TokenOrListParam();
		tol.add("_id", "123");
		tokenAndListParm.addValue(tol);

		paramMap.add(Patient.SP_IDENTIFIER, tokenAndListParm);
		assertTrue(ResourceProviderValidator.validateRequest(paramMap, ResourceTypes.PATIENT.toString()));
	}

	@Test
	void testPractitionerIdTest() {
		SearchParameterMap paramMap = new SearchParameterMap();
		TokenAndListParam tokenAndListParm = new TokenAndListParam();
		TokenOrListParam tol = new TokenOrListParam();
		tol.add("_id", "123");
		tokenAndListParm.addValue(tol);

		paramMap.add(Practitioner.SP_RES_ID, tokenAndListParm);
		assertTrue(ResourceProviderValidator.validateRequest(paramMap, ResourceTypes.PRACTITIONER.toString()));
	}

	@Test
	void testPractitionerIdentifierTest() {
		SearchParameterMap paramMap = new SearchParameterMap();
		TokenAndListParam tokenAndListParm = new TokenAndListParam();
		TokenOrListParam tol = new TokenOrListParam();
		tol.add("_id", "123");
		tokenAndListParm.addValue(tol);
		paramMap.add(Practitioner.SP_IDENTIFIER, tokenAndListParm);
		assertTrue(ResourceProviderValidator.validateRequest(paramMap, ResourceTypes.PRACTITIONER.toString()));
	}

	@Test
	void testPractitionerNameTest() {
		SearchParameterMap paramMap = new SearchParameterMap();
		StringAndListParam salp1 = new StringAndListParam();
		StringOrListParam solp1 = new StringOrListParam();
		StringParam sp1 = new StringParam();
		sp1.setValue("Bangalore");
		solp1.add(sp1);
		salp1.addValue(solp1);
		paramMap.add(Practitioner.SP_NAME, salp1);
		assertTrue(ResourceProviderValidator.validateRequest(paramMap, ResourceTypes.PRACTITIONER.toString()));
	}

	@Test
	void testPractitionerRoleIdTest() {
		SearchParameterMap paramMap = new SearchParameterMap();
		TokenAndListParam tokenAndListParm = new TokenAndListParam();
		TokenOrListParam tol = new TokenOrListParam();
		tol.add("_id", "123");
		tokenAndListParm.addValue(tol);

		paramMap.add(PractitionerRole.SP_RES_ID, tokenAndListParm);
		assertTrue(ResourceProviderValidator.validateRequest(paramMap, ResourceTypes.PRACTITIONERROLE.toString()));
	}

	@Test
	void testPractitionerRoleSpecialityTest() {
		SearchParameterMap paramMap = new SearchParameterMap();
		TokenAndListParam toknAndListParam = new TokenAndListParam();
		TokenOrListParam top = new TokenOrListParam();
		top.add(PractitionerRole.SP_SPECIALTY, "normal");
		toknAndListParam.addValue(top);
		paramMap.add(PractitionerRole.SP_SPECIALTY, toknAndListParam);
		assertTrue(ResourceProviderValidator.validateRequest(paramMap, ResourceTypes.PRACTITIONERROLE.toString()));
	}

	@Test
	void testPractitionerRoleIdentifierTest() {
		SearchParameterMap paramMap = new SearchParameterMap();
		TokenAndListParam theIdentifier = new TokenAndListParam();
		TokenOrListParam tolp = new TokenOrListParam();
		tolp.add("http://hospital.smarthealthit.org", "1032702");
		theIdentifier.addValue(tolp);
		paramMap.add("practitioner.identifier", theIdentifier);
		assertTrue(ResourceProviderValidator.validateRequest(paramMap, ResourceTypes.PRACTITIONERROLE.toString()));
	}

	@Test
	void testPractitionerRoleNameTest() {
		SearchParameterMap paramMap = new SearchParameterMap();
		StringAndListParam salp1 = new StringAndListParam();
		StringOrListParam solp1 = new StringOrListParam();
		StringParam sp1 = new StringParam();
		sp1.setValue("Bangalore");
		solp1.add(sp1);
		salp1.addValue(solp1);
		paramMap.add(AppConstants.PRACTITIONER_NAME, salp1);
		assertTrue(ResourceProviderValidator.validateRequest(paramMap, ResourceTypes.PRACTITIONERROLE.toString()));
	}

	@Test
	void testImmunizationIdTest() {
		SearchParameterMap paramMap = new SearchParameterMap();
		TokenAndListParam tokenAndListParm = new TokenAndListParam();
		TokenOrListParam tol = new TokenOrListParam();
		tol.add("_id", "123");
		tokenAndListParm.addValue(tol);
		paramMap.add(Immunization.SP_RES_ID, tokenAndListParm);
		assertTrue(ResourceProviderValidator.validateRequest(paramMap, ResourceTypes.IMMUNIZATION.toString()));
	}

	@Test
	void testImmunizationPatientDateTest() {
		SearchParameterMap paramMap = new SearchParameterMap();
		ReferenceAndListParam patient = new ReferenceAndListParam();
		ReferenceOrListParam rolp = new ReferenceOrListParam();
		ReferenceParam rpd = new ReferenceParam();
		rpd.setValue("123");
		rolp.add(rpd);
		patient.addValue(rolp);
		paramMap.add(Immunization.SP_PATIENT, patient);

		DateAndListParam dateAndListParam = new DateAndListParam();
		DateOrListParam dolp = new DateOrListParam();
		DateParam dp = new DateParam();
		dp.setValue(new Date());
		dolp.add(dp);
		dateAndListParam.addValue(dolp);
		paramMap.add(Immunization.SP_DATE, dateAndListParam);
		assertTrue(ResourceProviderValidator.validateRequest(paramMap, ResourceTypes.IMMUNIZATION.toString()));
	}

	@Test
	void testImmunizationPatientStatusTest() {
		SearchParameterMap paramMap = new SearchParameterMap();
		ReferenceAndListParam patient = new ReferenceAndListParam();
		ReferenceOrListParam rolp = new ReferenceOrListParam();
		ReferenceParam rpd = new ReferenceParam();
		rpd.setValue("123");
		rolp.add(rpd);
		patient.addValue(rolp);
		paramMap.add(Immunization.SP_PATIENT, patient);

		TokenAndListParam tokenAndListPam = new TokenAndListParam();
		TokenOrListParam to = new TokenOrListParam();
		to.add(Immunization.SP_STATUS, "active");
		tokenAndListPam.addValue(to);
		paramMap.add(Immunization.SP_STATUS, tokenAndListPam);
		assertTrue(ResourceProviderValidator.validateRequest(paramMap, ResourceTypes.IMMUNIZATION.toString()));
	}

	@Test
	void testImmunizationEncounterTest() {
		SearchParameterMap paramMap = new SearchParameterMap();
		ReferenceAndListParam encounter = new ReferenceAndListParam();
		ReferenceOrListParam rolpe = new ReferenceOrListParam();
		ReferenceParam rpde = new ReferenceParam();
		rpde.setValue("1234");
		rolpe.add(rpde);
		encounter.addValue(rolpe);
		paramMap.add("encounter", encounter);
		assertTrue(ResourceProviderValidator.validateRequest(paramMap, ResourceTypes.IMMUNIZATION.toString()));
	}

	@Test
	void testImmunizationPatientTest() {
		SearchParameterMap paramMap = new SearchParameterMap();
		ReferenceAndListParam patient = new ReferenceAndListParam();
		ReferenceOrListParam rolp = new ReferenceOrListParam();
		ReferenceParam rpd = new ReferenceParam();
		rpd.setValue("123");
		rolp.add(rpd);
		patient.addValue(rolp);
		paramMap.add(Immunization.SP_PATIENT, patient);
		assertTrue(ResourceProviderValidator.validateRequest(paramMap, ResourceTypes.IMMUNIZATION.toString()));
	}

	@Test
	void testProcedureIdTest() {
		SearchParameterMap paramMap = new SearchParameterMap();
		TokenAndListParam tokenAndListParm = new TokenAndListParam();
		TokenOrListParam tol = new TokenOrListParam();
		tol.add("_id", "123");
		tokenAndListParm.addValue(tol);
		paramMap.add(Procedure.SP_RES_ID, tokenAndListParm);
		assertTrue(ResourceProviderValidator.validateRequest(paramMap, ResourceTypes.PROCEDURE.toString()));
	}

	@Test
	void testProcedurePatientCodeDateTest() {
		SearchParameterMap paramMap = new SearchParameterMap();
		ReferenceAndListParam patient = new ReferenceAndListParam();
		ReferenceOrListParam rolp = new ReferenceOrListParam();
		ReferenceParam rpd = new ReferenceParam();
		rpd.setValue("123");
		rolp.add(rpd);
		patient.addValue(rolp);
		paramMap.add(Procedure.SP_PATIENT, patient);

		TokenAndListParam toknAndListParam = new TokenAndListParam();
		TokenOrListParam top = new TokenOrListParam();
		top.add(Procedure.SP_CODE, "1445");
		toknAndListParam.addValue(top);
		paramMap.add(Condition.SP_CODE, toknAndListParam);

		DateAndListParam dateAndListParam = new DateAndListParam();
		DateOrListParam dolp = new DateOrListParam();
		DateParam dp = new DateParam();
		dp.setValue(new Date());
		dolp.add(dp);
		dateAndListParam.addValue(dolp);
		paramMap.add(Procedure.SP_DATE, dateAndListParam);
		assertTrue(ResourceProviderValidator.validateRequest(paramMap, ResourceTypes.PROCEDURE.toString()));
	}

	@Test
	void testProcedurePatientDateTest() {
		SearchParameterMap paramMap = new SearchParameterMap();
		ReferenceAndListParam patient = new ReferenceAndListParam();
		ReferenceOrListParam rolp = new ReferenceOrListParam();
		ReferenceParam rpd = new ReferenceParam();
		rpd.setValue("123");
		rolp.add(rpd);
		patient.addValue(rolp);
		paramMap.add(Procedure.SP_PATIENT, patient);

		DateAndListParam dateAndListParam = new DateAndListParam();
		DateOrListParam dolp = new DateOrListParam();
		DateParam dp = new DateParam();
		dp.setValue(new Date());
		dolp.add(dp);
		dateAndListParam.addValue(dolp);
		paramMap.add(Procedure.SP_DATE, dateAndListParam);
		assertTrue(ResourceProviderValidator.validateRequest(paramMap, ResourceTypes.PROCEDURE.toString()));
	}

	@Test
	void testProcedurePatientStatusTest() {
		SearchParameterMap paramMap = new SearchParameterMap();
		ReferenceAndListParam patient = new ReferenceAndListParam();
		ReferenceOrListParam rolp = new ReferenceOrListParam();
		ReferenceParam rpd = new ReferenceParam();
		rpd.setValue("123");
		rolp.add(rpd);
		patient.addValue(rolp);
		paramMap.add(Procedure.SP_PATIENT, patient);

		TokenAndListParam tokenAndListPam = new TokenAndListParam();
		TokenOrListParam to = new TokenOrListParam();
		to.add(Procedure.SP_STATUS, "active");
		tokenAndListPam.addValue(to);
		paramMap.add(Procedure.SP_STATUS, tokenAndListPam);
		assertTrue(ResourceProviderValidator.validateRequest(paramMap, ResourceTypes.PROCEDURE.toString()));
	}

	@Test
	void testProcedureEncounterTest() {
		SearchParameterMap paramMap = new SearchParameterMap();
		ReferenceAndListParam encounter = new ReferenceAndListParam();
		ReferenceOrListParam rolpe = new ReferenceOrListParam();
		ReferenceParam rpde = new ReferenceParam();
		rpde.setValue("1234");
		rolpe.add(rpde);
		encounter.addValue(rolpe);
		paramMap.add("encounter", encounter);
		assertTrue(ResourceProviderValidator.validateRequest(paramMap, ResourceTypes.PROCEDURE.toString()));
	}

	@Test
	void testProcedurePatientTest() {
		SearchParameterMap paramMap = new SearchParameterMap();
		ReferenceAndListParam patient = new ReferenceAndListParam();
		ReferenceOrListParam rolp = new ReferenceOrListParam();
		ReferenceParam rpd = new ReferenceParam();
		rpd.setValue("123");
		rolp.add(rpd);
		patient.addValue(rolp);
		paramMap.add(Procedure.SP_PATIENT, patient);
		assertTrue(ResourceProviderValidator.validateRequest(paramMap, ResourceTypes.PROCEDURE.toString()));
	}

	@Test
	void testMedicationAdministrationIdTest() {
		SearchParameterMap paramMap = new SearchParameterMap();
		TokenAndListParam tokenAndListParm = new TokenAndListParam();
		TokenOrListParam tol = new TokenOrListParam();
		tol.add("_id", "123");
		tokenAndListParm.addValue(tol);
		paramMap.add(MedicationAdministration.SP_RES_ID, tokenAndListParm);
		assertTrue(ResourceProviderValidator.validateRequest(paramMap, ResourceTypes.MEDICATIONADMINISTRATION.toString()));
	}

	@Test
	void testMedicationAdministrationPatientContextTest() {
		ResourceProviderValidator rpv = new ResourceProviderValidator ();
		SearchParameterMap paramMap = new SearchParameterMap();
		ReferenceAndListParam patient = new ReferenceAndListParam();
		ReferenceOrListParam rolp = new ReferenceOrListParam();
		ReferenceParam rpd = new ReferenceParam();
		rpd.setValue("123");
		rolp.add(rpd);
		patient.addValue(rolp);
		paramMap.add(MedicationAdministration.SP_PATIENT, patient);

		ReferenceAndListParam context = new ReferenceAndListParam();
		ReferenceOrListParam ro = new ReferenceOrListParam();
		ReferenceParam rd = new ReferenceParam();
		rd.setValue("123");
		ro.add(rd);
		context.addValue(ro);
		paramMap.add(MedicationAdministration.SP_CONTEXT, context);
		assertTrue(ResourceProviderValidator.validateRequest(paramMap, ResourceTypes.MEDICATIONADMINISTRATION.toString()));
	}
	
	
}
