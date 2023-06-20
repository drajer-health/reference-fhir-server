package com.interopx.fhir.facade.util;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.hl7.fhir.r4.model.Location;
import org.hl7.fhir.r4.model.Procedure;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;

import ca.uhn.fhir.rest.param.StringAndListParam;
import ca.uhn.fhir.rest.param.StringOrListParam;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.TokenAndListParam;
import ca.uhn.fhir.rest.param.TokenOrListParam;
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class SearchParameterMapTest {

	@Test
	void test1() {
		SearchParameterMap paramMap = new SearchParameterMap();
		TokenAndListParam tokenAndListPam = new TokenAndListParam();
		TokenOrListParam to = new TokenOrListParam();
		to.add(Procedure.SP_STATUS, "active");
		tokenAndListPam.addValue(to);
		paramMap.add(Procedure.SP_STATUS, tokenAndListPam);	
		assertNotNull(paramMap.toString());	
	}
	@Test
	void test2() {
		SearchParameterMap paramMap = new SearchParameterMap();
		TokenOrListParam to = new TokenOrListParam();
		to.add(Procedure.SP_STATUS, "active");
		paramMap.add(Procedure.SP_STATUS, to);
		paramMap.getLastUpdated();
		paramMap.getIncludes();
		paramMap.getLastUpdatedAndRemove();		
		paramMap.getSort();	
		assertNotNull(paramMap.toString());			
	}
	@Test
	void test3() {
		SearchParameterMap.EverythingModeEnum.ENCOUNTER_INSTANCE.isPatient();
		SearchParameterMap paramMap = new SearchParameterMap();
		StringAndListParam salp2 = new StringAndListParam();
		StringOrListParam solp2 = new StringOrListParam();
		StringParam sp2 = new StringParam();
		sp2.setValue("Bangalore");
		solp2.add(sp2);
		salp2.addValue(solp2);
		paramMap.add(Location.SP_ADDRESS, salp2);
		assertNotNull(paramMap.toString());
	}
	
	@Test
	void test4() {
		SearchParameterMap.EverythingModeEnum.ENCOUNTER_INSTANCE.isPatient();
		SearchParameterMap paramMap = new SearchParameterMap();
		StringAndListParam salp2 = new StringAndListParam();
		StringOrListParam solp2 = new StringOrListParam();
		StringParam sp2 = new StringParam();
		sp2.setValue("Bangalore");
		solp2.add(sp2);
		salp2.addValue(solp2);
		paramMap.add(Location.SP_ADDRESS_CITY, salp2);		
		assertNotNull(paramMap.toString());
	}


}
