package com.interopx.fhir.facade.provider;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.hl7.fhir.r4.model.Enumerations.ResourceType;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import com.interopx.fhir.facade.exception.AuthorizationException;
import com.interopx.fhir.facade.interceptor.IntrospectInterceptor;
import com.interopx.fhir.facade.service.AuthConfigurationService;

import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.AuthenticationException;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class IntrospectInterceptorTest {
	protected static final TestRestTemplate testRestTemplate = new TestRestTemplate();
	@InjectMocks
	IntrospectInterceptor introspectInterceptor;

	@Mock
	SessionFactory sessionFactory;

	@Mock
	Session session;

	@Mock
	Query query;

	@Mock
	RestTemplate resttemplate;
	@Mock
	private HttpServletRequest requestObj;

	@Mock
	private HttpServletResponse responseObj;

	@Mock
	AuthConfigurationService authConfigurationService;

	@LocalServerPort
	private Integer port;

	@SuppressWarnings("deprecation")
	@BeforeEach
	private void setUp() throws IOException, JSONException {
		MockitoAnnotations.initMocks(this);
		String isAuditEnabled = "false";
		ReflectionTestUtils.setField(introspectInterceptor, "isAuditEnabled", isAuditEnabled);
		when(sessionFactory.getCurrentSession()).thenReturn(session);

		if (sessionFactory == null) {
			System.out.println("Session Factory is null");
		} else {
			System.out.println("Session Factory is not null");
			session = sessionFactory.getCurrentSession();

			if (session != null) {
				System.out.print("Session is not null");
			} else {
				System.out.println("Session is null");
			}
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	void TestImmunization() throws IOException {
		RequestDetails requestDetails = Mockito.mock(RequestDetails.class);
		when(requestDetails.getRestOperationType()).thenReturn(RestOperationTypeEnum.READ);
		when(requestDetails.getResourceName()).thenReturn(ResourceType.IMMUNIZATION.toString());
		when(requestDetails.getHeader("Authorization")).thenReturn("bearer test.test.test");
		when(requestDetails.getRequestPath())
				.thenReturn("http://localhost:8090/fhir/r4/1005/Immunization?encounter=123");
		when(requestDetails.getCompleteUrl())
				.thenReturn("http://localhost:8090/fhir/r4/1005/Immunization?encounter=123");
		Mockito.when(authConfigurationService.getBaseURL(anyString())).thenReturn("http://localhost:8090/fhir/r4/1005");
		String responseBody = TestUtil.convertJsonToJsonString("bundle/tokenresponse.json");
		when(authConfigurationService.getAuthConfiguration()).thenReturn(TestUtil.getAuthConfiguration());

		ResponseEntity<String> responseEntity = new ResponseEntity(responseBody, HttpStatus.OK);
		when(resttemplate.postForEntity(anyString(), any(), eq(String.class))).thenReturn(responseEntity);
		when(requestObj.getMethod()).thenReturn("method name");
		when(responseObj.getStatus()).thenReturn(200);
		when(requestObj.getRequestURI()).thenReturn("Url");
		assertNotNull(introspectInterceptor.buildRuleList(requestDetails));
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	void testPatient() throws IOException {
		RequestDetails requestDetails = Mockito.mock(RequestDetails.class);
		when(requestDetails.getRestOperationType()).thenReturn(RestOperationTypeEnum.SEARCH_TYPE);
		when(requestDetails.getResourceName()).thenReturn(ResourceType.PATIENT.toString());
		when(requestDetails.getHeader("Authorization")).thenReturn("bearer test.test.test");
		when(requestDetails.getRequestPath()).thenReturn("http://localhost:8090/fhir/r4/1005/Patient/1032702");
		when(requestDetails.getCompleteUrl()).thenReturn("http://localhost:8090/fhir/r4/1005/Patient/1032702");
		Mockito.when(authConfigurationService.getBaseURL(anyString())).thenReturn("http://localhost:8090/fhir/r4/1005");
		String responseBody = TestUtil.convertJsonToJsonString("bundle/tokenresponse.json");
		when(authConfigurationService.getAuthConfiguration()).thenReturn(TestUtil.getAuthConfiguration());

		ResponseEntity<String> responseEntity = new ResponseEntity(responseBody, HttpStatus.OK);
		when(resttemplate.postForEntity(anyString(), any(), eq(String.class))).thenReturn(responseEntity);
		when(requestObj.getMethod()).thenReturn("method name");
		when(responseObj.getStatus()).thenReturn(200);
		when(requestObj.getRequestURI()).thenReturn("Url");
		assertNotNull(introspectInterceptor.buildRuleList(requestDetails));
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	void testPractitionerRole() throws IOException {
		RequestDetails requestDetails = Mockito.mock(RequestDetails.class);
		when(requestDetails.getRestOperationType()).thenReturn(RestOperationTypeEnum.READ);
		when(requestDetails.getResourceName()).thenReturn(ResourceType.PRACTITIONERROLE.toString());
		when(requestDetails.getHeader("Authorization")).thenReturn("bearer test.test.test");
		when(requestDetails.getRequestPath()).thenReturn("http://localhost:8090/fhir/r4/1005/PractitionerRole/1032702");
		when(requestDetails.getCompleteUrl()).thenReturn("http://localhost:8090/fhir/r4/1005/PractitionerRole/1032702");
		Mockito.when(authConfigurationService.getBaseURL(anyString())).thenReturn("http://localhost:8090/fhir/r4/1005");
		String responseBody = TestUtil.convertJsonToJsonString("bundle/tokenresponse.json");
		when(authConfigurationService.getAuthConfiguration()).thenReturn(TestUtil.getAuthConfiguration());

		ResponseEntity<String> responseEntity = new ResponseEntity(responseBody, HttpStatus.OK);
		when(resttemplate.postForEntity(anyString(), any(), eq(String.class))).thenReturn(responseEntity);
		when(requestObj.getMethod()).thenReturn("method name");
		when(responseObj.getStatus()).thenReturn(200);
		when(requestObj.getRequestURI()).thenReturn("Url");
		assertNotNull(introspectInterceptor.buildRuleList(requestDetails));
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	void testAllergyIntolerance() throws IOException {
		RequestDetails requestDetails = Mockito.mock(RequestDetails.class);
		when(requestDetails.getRestOperationType()).thenReturn(RestOperationTypeEnum.READ);
		when(requestDetails.getResourceName()).thenReturn(ResourceType.ALLERGYINTOLERANCE.toString());
		when(requestDetails.getHeader("Authorization")).thenReturn("bearer test.test.test");
		when(requestDetails.getRequestPath())
				.thenReturn("http://localhost:8090/fhir/r4/1005/AllergyIntolerance/1032702");
		when(requestDetails.getCompleteUrl())
				.thenReturn("http://localhost:8090/fhir/r4/1005/AllergyIntolerance/1032702");
		Mockito.when(authConfigurationService.getBaseURL(anyString())).thenReturn("http://localhost:8090/fhir/r4/1005");
		String responseBody = TestUtil.convertJsonToJsonString("bundle/tokenresponse.json");
		when(authConfigurationService.getAuthConfiguration()).thenReturn(TestUtil.getAuthConfiguration());

		ResponseEntity<String> responseEntity = new ResponseEntity(responseBody, HttpStatus.OK);
		when(resttemplate.postForEntity(anyString(), any(), eq(String.class))).thenReturn(responseEntity);
		when(requestObj.getMethod()).thenReturn("method name");
		when(responseObj.getStatus()).thenReturn(200);
		when(requestObj.getRequestURI()).thenReturn("Url");
		assertNotNull(introspectInterceptor.buildRuleList(requestDetails));
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	void testCareTeam() throws IOException {
		RequestDetails requestDetails = Mockito.mock(RequestDetails.class);
		when(requestDetails.getRestOperationType()).thenReturn(RestOperationTypeEnum.READ);
		when(requestDetails.getResourceName()).thenReturn(ResourceType.CARETEAM.toString());
		when(requestDetails.getHeader("Authorization")).thenReturn("bearer test.test.test");
		when(requestDetails.getRequestPath()).thenReturn("http://localhost:8090/fhir/r4/1005/CareTeam/1032702");
		when(requestDetails.getCompleteUrl()).thenReturn("http://localhost:8090/fhir/r4/1005/CareTeam/1032702");
		Mockito.when(authConfigurationService.getBaseURL(anyString())).thenReturn("http://localhost:8090/fhir/r4/1005");
		String responseBody = TestUtil.convertJsonToJsonString("bundle/tokenresponse.json");
		when(authConfigurationService.getAuthConfiguration()).thenReturn(TestUtil.getAuthConfiguration());

		ResponseEntity<String> responseEntity = new ResponseEntity(responseBody, HttpStatus.OK);
		when(resttemplate.postForEntity(anyString(), any(), eq(String.class))).thenReturn(responseEntity);
		when(requestObj.getMethod()).thenReturn("method name");
		when(responseObj.getStatus()).thenReturn(200);
		when(requestObj.getRequestURI()).thenReturn("Url");
		assertNotNull(introspectInterceptor.buildRuleList(requestDetails));
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	void testCarePlan() throws IOException {
		RequestDetails requestDetails = Mockito.mock(RequestDetails.class);
		when(requestDetails.getRestOperationType()).thenReturn(RestOperationTypeEnum.READ);
		when(requestDetails.getResourceName()).thenReturn(ResourceType.CAREPLAN.toString());
		when(requestDetails.getHeader("Authorization")).thenReturn("bearer test.test.test");
		when(requestDetails.getRequestPath()).thenReturn("http://localhost:8090/fhir/r4/1005/CarePlan/1032702");
		when(requestDetails.getCompleteUrl()).thenReturn("http://localhost:8090/fhir/r4/1005/CarePlan/1032702");
		Mockito.when(authConfigurationService.getBaseURL(anyString())).thenReturn("http://localhost:8090/fhir/r4/1005");
		String responseBody = TestUtil.convertJsonToJsonString("bundle/tokenresponse.json");
		when(authConfigurationService.getAuthConfiguration()).thenReturn(TestUtil.getAuthConfiguration());

		ResponseEntity<String> responseEntity = new ResponseEntity(responseBody, HttpStatus.OK);
		when(resttemplate.postForEntity(anyString(), any(), eq(String.class))).thenReturn(responseEntity);
		when(requestObj.getMethod()).thenReturn("method name");
		when(responseObj.getStatus()).thenReturn(200);
		when(requestObj.getRequestURI()).thenReturn("Url");
		assertNotNull(introspectInterceptor.buildRuleList(requestDetails));
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	void testCondition() throws IOException {
		RequestDetails requestDetails = Mockito.mock(RequestDetails.class);
		when(requestDetails.getRestOperationType()).thenReturn(RestOperationTypeEnum.READ);
		when(requestDetails.getResourceName()).thenReturn(ResourceType.CONDITION.toString());
		when(requestDetails.getHeader("Authorization")).thenReturn("bearer test.test.test");
		when(requestDetails.getRequestPath()).thenReturn("http://localhost:8090/fhir/r4/1005/Condition/1032702");
		when(requestDetails.getCompleteUrl()).thenReturn("http://localhost:8090/fhir/r4/1005/Condition/1032702");
		Mockito.when(authConfigurationService.getBaseURL(anyString())).thenReturn("http://localhost:8090/fhir/r4/1005");
		String responseBody = TestUtil.convertJsonToJsonString("bundle/tokenresponse.json");
		when(authConfigurationService.getAuthConfiguration()).thenReturn(TestUtil.getAuthConfiguration());

		ResponseEntity<String> responseEntity = new ResponseEntity(responseBody, HttpStatus.OK);
		when(resttemplate.postForEntity(anyString(), any(), eq(String.class))).thenReturn(responseEntity);
		when(requestObj.getMethod()).thenReturn("method name");
		when(responseObj.getStatus()).thenReturn(200);
		when(requestObj.getRequestURI()).thenReturn("Url");
		assertNotNull(introspectInterceptor.buildRuleList(requestDetails));
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	void testDevice() throws IOException {
		RequestDetails requestDetails = Mockito.mock(RequestDetails.class);
		when(requestDetails.getRestOperationType()).thenReturn(RestOperationTypeEnum.READ);
		when(requestDetails.getResourceName()).thenReturn(ResourceType.DEVICE.toString());
		when(requestDetails.getHeader("Authorization")).thenReturn("bearer test.test.test");
		when(requestDetails.getRequestPath()).thenReturn("http://localhost:8090/fhir/r4/1005/Device/1032702");
		when(requestDetails.getCompleteUrl()).thenReturn("http://localhost:8090/fhir/r4/1005/Device/1032702");
		Mockito.when(authConfigurationService.getBaseURL(anyString())).thenReturn("http://localhost:8090/fhir/r4/1005");
		String responseBody = TestUtil.convertJsonToJsonString("bundle/tokenresponse.json");
		when(authConfigurationService.getAuthConfiguration()).thenReturn(TestUtil.getAuthConfiguration());

		ResponseEntity<String> responseEntity = new ResponseEntity(responseBody, HttpStatus.OK);
		when(resttemplate.postForEntity(anyString(), any(), eq(String.class))).thenReturn(responseEntity);
		when(requestObj.getMethod()).thenReturn("method name");
		when(responseObj.getStatus()).thenReturn(200);
		when(requestObj.getRequestURI()).thenReturn("Url");
		assertNotNull(introspectInterceptor.buildRuleList(requestDetails));
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	void testDiagnosticReport() throws IOException {
		RequestDetails requestDetails = Mockito.mock(RequestDetails.class);
		when(requestDetails.getRestOperationType()).thenReturn(RestOperationTypeEnum.READ);
		when(requestDetails.getResourceName()).thenReturn(ResourceType.DIAGNOSTICREPORT.toString());
		when(requestDetails.getHeader("Authorization")).thenReturn("bearer test.test.test");
		when(requestDetails.getRequestPath()).thenReturn("http://localhost:8090/fhir/r4/1005/DiagnosticReport/1032702");
		when(requestDetails.getCompleteUrl()).thenReturn("http://localhost:8090/fhir/r4/1005/DiagnosticReport/1032702");
		Mockito.when(authConfigurationService.getBaseURL(anyString())).thenReturn("http://localhost:8090/fhir/r4/1005");
		String responseBody = TestUtil.convertJsonToJsonString("bundle/tokenresponse.json");
		when(authConfigurationService.getAuthConfiguration()).thenReturn(TestUtil.getAuthConfiguration());

		ResponseEntity<String> responseEntity = new ResponseEntity(responseBody, HttpStatus.OK);
		when(resttemplate.postForEntity(anyString(), any(), eq(String.class))).thenReturn(responseEntity);
		when(requestObj.getMethod()).thenReturn("method name");
		when(responseObj.getStatus()).thenReturn(200);
		when(requestObj.getRequestURI()).thenReturn("Url");
		assertNotNull(introspectInterceptor.buildRuleList(requestDetails));
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	void testDocumentReference() throws IOException {
		RequestDetails requestDetails = Mockito.mock(RequestDetails.class);
		when(requestDetails.getRestOperationType()).thenReturn(RestOperationTypeEnum.READ);
		when(requestDetails.getResourceName()).thenReturn(ResourceType.DOCUMENTREFERENCE.toString());
		when(requestDetails.getHeader("Authorization")).thenReturn("bearer test.test.test");
		when(requestDetails.getRequestPath())
				.thenReturn("http://localhost:8090/fhir/r4/1005/DocumentReference/1032702");
		when(requestDetails.getCompleteUrl())
				.thenReturn("http://localhost:8090/fhir/r4/1005/DocumentReference/1032702");
		Mockito.when(authConfigurationService.getBaseURL(anyString())).thenReturn("http://localhost:8090/fhir/r4/1005");
		String responseBody = TestUtil.convertJsonToJsonString("bundle/tokenresponse.json");
		when(authConfigurationService.getAuthConfiguration()).thenReturn(TestUtil.getAuthConfiguration());

		ResponseEntity<String> responseEntity = new ResponseEntity(responseBody, HttpStatus.OK);
		when(resttemplate.postForEntity(anyString(), any(), eq(String.class))).thenReturn(responseEntity);
		when(requestObj.getMethod()).thenReturn("method name");
		when(responseObj.getStatus()).thenReturn(200);
		when(requestObj.getRequestURI()).thenReturn("Url");
		assertNotNull(introspectInterceptor.buildRuleList(requestDetails));
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	void testEncounter() throws IOException {
		RequestDetails requestDetails = Mockito.mock(RequestDetails.class);
		when(requestDetails.getRestOperationType()).thenReturn(RestOperationTypeEnum.READ);
		when(requestDetails.getResourceName()).thenReturn(ResourceType.ENCOUNTER.toString());
		when(requestDetails.getHeader("Authorization")).thenReturn("bearer test.test.test");
		when(requestDetails.getRequestPath()).thenReturn("http://localhost:8090/fhir/r4/1005/Encounter/1032702");
		when(requestDetails.getCompleteUrl()).thenReturn("http://localhost:8090/fhir/r4/1005/Encounter/1032702");
		Mockito.when(authConfigurationService.getBaseURL(anyString())).thenReturn("http://localhost:8090/fhir/r4/1005");
		String responseBody = TestUtil.convertJsonToJsonString("bundle/tokenresponse.json");
		when(authConfigurationService.getAuthConfiguration()).thenReturn(TestUtil.getAuthConfiguration());

		ResponseEntity<String> responseEntity = new ResponseEntity(responseBody, HttpStatus.OK);
		when(resttemplate.postForEntity(anyString(), any(), eq(String.class))).thenReturn(responseEntity);
		when(requestObj.getMethod()).thenReturn("method name");
		when(responseObj.getStatus()).thenReturn(200);
		when(requestObj.getRequestURI()).thenReturn("Url");
		assertNotNull(introspectInterceptor.buildRuleList(requestDetails));
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	void testGoal() throws IOException {
		RequestDetails requestDetails = Mockito.mock(RequestDetails.class);
		when(requestDetails.getRestOperationType()).thenReturn(RestOperationTypeEnum.READ);
		when(requestDetails.getResourceName()).thenReturn(ResourceType.GOAL.toString());
		when(requestDetails.getHeader("Authorization")).thenReturn("bearer test.test.test");
		when(requestDetails.getRequestPath()).thenReturn("http://localhost:8090/fhir/r4/1005/Goal/1032702");
		when(requestDetails.getCompleteUrl()).thenReturn("http://localhost:8090/fhir/r4/1005/Goal/1032702");
		Mockito.when(authConfigurationService.getBaseURL(anyString())).thenReturn("http://localhost:8090/fhir/r4/1005");
		String responseBody = TestUtil.convertJsonToJsonString("bundle/tokenresponse.json");
		System.out.println("  responseBody   {} " + responseBody);
		when(authConfigurationService.getAuthConfiguration()).thenReturn(TestUtil.getAuthConfiguration());

		ResponseEntity<String> responseEntity = new ResponseEntity(responseBody, HttpStatus.OK);
		when(resttemplate.postForEntity(anyString(), any(), eq(String.class))).thenReturn(responseEntity);
		when(requestObj.getMethod()).thenReturn("method name");
		when(responseObj.getStatus()).thenReturn(200);
		when(requestObj.getRequestURI()).thenReturn("Url");
		assertNotNull(introspectInterceptor.buildRuleList(requestDetails));
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	void testLocation() throws IOException {
		RequestDetails requestDetails = Mockito.mock(RequestDetails.class);
		when(requestDetails.getRestOperationType()).thenReturn(RestOperationTypeEnum.READ);
		when(requestDetails.getResourceName()).thenReturn(ResourceType.LOCATION.toString());
		when(requestDetails.getHeader("Authorization")).thenReturn("bearer test.test.test");
		when(requestDetails.getRequestPath()).thenReturn("http://localhost:8090/fhir/r4/1005/Location/1032702");
		when(requestDetails.getCompleteUrl()).thenReturn("http://localhost:8090/fhir/r4/1005/Location/1032702");
		Mockito.when(authConfigurationService.getBaseURL(anyString())).thenReturn("http://localhost:8090/fhir/r4/1005");
		String responseBody = TestUtil.convertJsonToJsonString("bundle/tokenresponse.json");
		when(authConfigurationService.getAuthConfiguration()).thenReturn(TestUtil.getAuthConfiguration());

		ResponseEntity<String> responseEntity = new ResponseEntity(responseBody, HttpStatus.OK);
		when(resttemplate.postForEntity(anyString(), any(), eq(String.class))).thenReturn(responseEntity);
		when(requestObj.getMethod()).thenReturn("method name");
		when(responseObj.getStatus()).thenReturn(200);
		when(requestObj.getRequestURI()).thenReturn("Url");
		assertNotNull(introspectInterceptor.buildRuleList(requestDetails));
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	void testMedication() throws IOException {
		RequestDetails requestDetails = Mockito.mock(RequestDetails.class);
		when(requestDetails.getRestOperationType()).thenReturn(RestOperationTypeEnum.READ);
		when(requestDetails.getResourceName()).thenReturn(ResourceType.MEDICATION.toString());
		when(requestDetails.getHeader("Authorization")).thenReturn("bearer test.test.test");
		when(requestDetails.getRequestPath()).thenReturn("http://localhost:8090/fhir/r4/1005/Medication/1032702");
		when(requestDetails.getCompleteUrl()).thenReturn("http://localhost:8090/fhir/r4/1005/Medication/1032702");
		Mockito.when(authConfigurationService.getBaseURL(anyString())).thenReturn("http://localhost:8090/fhir/r4/1005");
		String responseBody = TestUtil.convertJsonToJsonString("bundle/tokenresponse.json");
		when(authConfigurationService.getAuthConfiguration()).thenReturn(TestUtil.getAuthConfiguration());

		ResponseEntity<String> responseEntity = new ResponseEntity(responseBody, HttpStatus.OK);
		when(resttemplate.postForEntity(anyString(), any(), eq(String.class))).thenReturn(responseEntity);
		when(requestObj.getMethod()).thenReturn("method name");
		when(responseObj.getStatus()).thenReturn(200);
		when(requestObj.getRequestURI()).thenReturn("Url");
		assertNotNull(introspectInterceptor.buildRuleList(requestDetails));
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	void testMedicationRequest() throws IOException {
		RequestDetails requestDetails = Mockito.mock(RequestDetails.class);
		when(requestDetails.getRestOperationType()).thenReturn(RestOperationTypeEnum.READ);
		when(requestDetails.getResourceName()).thenReturn(ResourceType.MEDICATION.toString());
		when(requestDetails.getHeader("Authorization")).thenReturn("bearer test.test.test");
		when(requestDetails.getRequestPath())
				.thenReturn("http://localhost:8090/fhir/r4/1005/MedicationRequest/1032702");
		when(requestDetails.getCompleteUrl())
				.thenReturn("http://localhost:8090/fhir/r4/1005/MedicationRequest/1032702");
		Mockito.when(authConfigurationService.getBaseURL(anyString())).thenReturn("http://localhost:8090/fhir/r4/1005");
		String responseBody = TestUtil.convertJsonToJsonString("bundle/tokenresponse.json");
		when(authConfigurationService.getAuthConfiguration()).thenReturn(TestUtil.getAuthConfiguration());

		ResponseEntity<String> responseEntity = new ResponseEntity(responseBody, HttpStatus.OK);
		when(resttemplate.postForEntity(anyString(), any(), eq(String.class))).thenReturn(responseEntity);
		when(requestObj.getMethod()).thenReturn("method name");
		when(responseObj.getStatus()).thenReturn(200);
		when(requestObj.getRequestURI()).thenReturn("Url");
		assertNotNull(introspectInterceptor.buildRuleList(requestDetails));
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	void testObservation() throws IOException {
		RequestDetails requestDetails = Mockito.mock(RequestDetails.class);
		when(requestDetails.getRestOperationType()).thenReturn(RestOperationTypeEnum.READ);
		when(requestDetails.getResourceName()).thenReturn(ResourceType.OBSERVATION.toString());
		when(requestDetails.getHeader("Authorization")).thenReturn("bearer test.test.test");
		when(requestDetails.getRequestPath()).thenReturn("http://localhost:8090/fhir/r4/1005/Observation/1032702");
		when(requestDetails.getCompleteUrl()).thenReturn("http://localhost:8090/fhir/r4/1005/Observation/1032702");
		Mockito.when(authConfigurationService.getBaseURL(anyString())).thenReturn("http://localhost:8090/fhir/r4/1005");
		String responseBody = TestUtil.convertJsonToJsonString("bundle/tokenresponse.json");
		when(authConfigurationService.getAuthConfiguration()).thenReturn(TestUtil.getAuthConfiguration());

		ResponseEntity<String> responseEntity = new ResponseEntity(responseBody, HttpStatus.OK);
		when(resttemplate.postForEntity(anyString(), any(), eq(String.class))).thenReturn(responseEntity);
		when(requestObj.getMethod()).thenReturn("method name");
		when(responseObj.getStatus()).thenReturn(200);
		when(requestObj.getRequestURI()).thenReturn("Url");
		assertNotNull(introspectInterceptor.buildRuleList(requestDetails));
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	void testOrganization() throws IOException {
		RequestDetails requestDetails = Mockito.mock(RequestDetails.class);
		when(requestDetails.getRestOperationType()).thenReturn(RestOperationTypeEnum.READ);
		when(requestDetails.getResourceName()).thenReturn(ResourceType.ORGANIZATION.toString());
		when(requestDetails.getHeader("Authorization")).thenReturn("bearer test.test.test");
		when(requestDetails.getRequestPath()).thenReturn("http://localhost:8090/fhir/r4/1005/Organization/1032702");
		when(requestDetails.getCompleteUrl()).thenReturn("http://localhost:8090/fhir/r4/1005/Organization/1032702");
		Mockito.when(authConfigurationService.getBaseURL(anyString())).thenReturn("http://localhost:8090/fhir/r4/1005");
		String responseBody = TestUtil.convertJsonToJsonString("bundle/tokenresponse.json");
		when(authConfigurationService.getAuthConfiguration()).thenReturn(TestUtil.getAuthConfiguration());

		ResponseEntity<String> responseEntity = new ResponseEntity(responseBody, HttpStatus.OK);
		when(resttemplate.postForEntity(anyString(), any(), eq(String.class))).thenReturn(responseEntity);
		when(requestObj.getMethod()).thenReturn("method name");
		when(responseObj.getStatus()).thenReturn(200);
		when(requestObj.getRequestURI()).thenReturn("Url");
		assertNotNull(introspectInterceptor.buildRuleList(requestDetails));
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	void testPractitioner() throws IOException {
		RequestDetails requestDetails = Mockito.mock(RequestDetails.class);
		when(requestDetails.getRestOperationType()).thenReturn(RestOperationTypeEnum.READ);
		when(requestDetails.getResourceName()).thenReturn(ResourceType.PRACTITIONER.toString());
		when(requestDetails.getHeader("Authorization")).thenReturn("bearer test.test.test");
		when(requestDetails.getRequestPath()).thenReturn("http://localhost:8090/fhir/r4/1005/Practitioner/1032702");
		when(requestDetails.getCompleteUrl()).thenReturn("http://localhost:8090/fhir/r4/1005/Practitioner/1032702");
		Mockito.when(authConfigurationService.getBaseURL(anyString())).thenReturn("http://localhost:8090/fhir/r4/1005");
		String responseBody = TestUtil.convertJsonToJsonString("bundle/tokenresponse.json");
		when(authConfigurationService.getAuthConfiguration()).thenReturn(TestUtil.getAuthConfiguration());

		ResponseEntity<String> responseEntity = new ResponseEntity(responseBody, HttpStatus.OK);
		when(resttemplate.postForEntity(anyString(), any(), eq(String.class))).thenReturn(responseEntity);
		when(requestObj.getMethod()).thenReturn("method name");
		when(responseObj.getStatus()).thenReturn(200);
		when(requestObj.getRequestURI()).thenReturn("Url");
		assertNotNull(introspectInterceptor.buildRuleList(requestDetails));
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	void testProcedure() throws IOException {
		RequestDetails requestDetails = Mockito.mock(RequestDetails.class);
		when(requestDetails.getRestOperationType()).thenReturn(RestOperationTypeEnum.READ);
		when(requestDetails.getResourceName()).thenReturn(ResourceType.PROCEDURE.toString());
		when(requestDetails.getHeader("Authorization")).thenReturn("bearer test.test.test");
		when(requestDetails.getRequestPath()).thenReturn("http://localhost:8090/fhir/r4/1005/Procedure/1032702");
		when(requestDetails.getCompleteUrl()).thenReturn("http://localhost:8090/fhir/r4/1005/Procedure/1032702");
		Mockito.when(authConfigurationService.getBaseURL(anyString())).thenReturn("http://localhost:8090/fhir/r4/1005");
		String responseBody = TestUtil.convertJsonToJsonString("bundle/tokenresponse.json");
		when(authConfigurationService.getAuthConfiguration()).thenReturn(TestUtil.getAuthConfiguration());

		ResponseEntity<String> responseEntity = new ResponseEntity(responseBody, HttpStatus.OK);
		when(resttemplate.postForEntity(anyString(), any(), eq(String.class))).thenReturn(responseEntity);
		when(requestObj.getMethod()).thenReturn("method name");
		when(responseObj.getStatus()).thenReturn(200);
		when(requestObj.getRequestURI()).thenReturn("Url");
		assertNotNull(introspectInterceptor.buildRuleList(requestDetails));
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	void testProvenance() throws IOException {
		RequestDetails requestDetails = Mockito.mock(RequestDetails.class);
		when(requestDetails.getRestOperationType()).thenReturn(RestOperationTypeEnum.READ);
		when(requestDetails.getResourceName()).thenReturn(ResourceType.PROVENANCE.toString());
		when(requestDetails.getHeader("Authorization")).thenReturn("bearer test.test.test");
		when(requestDetails.getRequestPath()).thenReturn("http://localhost:8090/fhir/r4/1005/Provenance/1032702");
		when(requestDetails.getCompleteUrl()).thenReturn("http://localhost:8090/fhir/r4/1005/Provenance/1032702");
		Mockito.when(authConfigurationService.getBaseURL(anyString())).thenReturn("http://localhost:8090/fhir/r4/1005");
		String responseBody = TestUtil.convertJsonToJsonString("bundle/tokenresponse.json");
		when(authConfigurationService.getAuthConfiguration()).thenReturn(TestUtil.getAuthConfiguration());

		ResponseEntity<String> responseEntity = new ResponseEntity(responseBody, HttpStatus.OK);
		when(resttemplate.postForEntity(anyString(), any(), eq(String.class))).thenReturn(responseEntity);
		when(requestObj.getMethod()).thenReturn("method name");
		when(responseObj.getStatus()).thenReturn(200);
		when(requestObj.getRequestURI()).thenReturn("Url");
		assertNotNull(introspectInterceptor.buildRuleList(requestDetails));
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	void testRelatedPerson() throws IOException {
		RequestDetails requestDetails = Mockito.mock(RequestDetails.class);
		when(requestDetails.getRestOperationType()).thenReturn(RestOperationTypeEnum.READ);
		when(requestDetails.getResourceName()).thenReturn(ResourceType.RELATEDPERSON.toString());
		when(requestDetails.getHeader("Authorization")).thenReturn("bearer test.test.test");
		when(requestDetails.getRequestPath()).thenReturn("http://localhost:8090/fhir/r4/1005/RelatedPerson/1032702");
		when(requestDetails.getCompleteUrl()).thenReturn("http://localhost:8090/fhir/r4/1005/RelatedPerson/1032702");
		Mockito.when(authConfigurationService.getBaseURL(anyString())).thenReturn("http://localhost:8090/fhir/r4/1005");
		String responseBody = TestUtil.convertJsonToJsonString("bundle/tokenresponse.json");
		when(authConfigurationService.getAuthConfiguration()).thenReturn(TestUtil.getAuthConfiguration());

		ResponseEntity<String> responseEntity = new ResponseEntity(responseBody, HttpStatus.OK);
		when(resttemplate.postForEntity(anyString(), any(), eq(String.class))).thenReturn(responseEntity);
		when(requestObj.getMethod()).thenReturn("method name");
		when(responseObj.getStatus()).thenReturn(200);
		when(requestObj.getRequestURI()).thenReturn("Url");
		assertNotNull(introspectInterceptor.buildRuleList(requestDetails));
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	void testSearchPatient() throws IOException {
		RequestDetails requestDetails = Mockito.mock(RequestDetails.class);
		when(requestDetails.getRestOperationType()).thenReturn(RestOperationTypeEnum.SEARCH_TYPE);
		when(requestDetails.getResourceName()).thenReturn(ResourceType.PATIENT.toString());
		when(requestDetails.getHeader("Authorization")).thenReturn("bearer test.test.test");
		when(requestDetails.getRequestPath()).thenReturn("http://localhost:8090/fhir/r4/1005/searchPatient/1032702");
		when(requestDetails.getCompleteUrl()).thenReturn("http://localhost:8090/fhir/r4/1005/searchPatient/1032702");
		Mockito.when(authConfigurationService.getBaseURL(anyString())).thenReturn("http://localhost:8090/fhir/r4/1005");
		String responseBody = TestUtil.convertJsonToJsonString("bundle/tokenresponse.json");
		when(authConfigurationService.getAuthConfiguration()).thenReturn(TestUtil.getAuthConfiguration());

		ResponseEntity<String> responseEntity = new ResponseEntity(responseBody, HttpStatus.OK);
		when(resttemplate.postForEntity(anyString(), any(), eq(String.class))).thenReturn(responseEntity);
		when(requestObj.getMethod()).thenReturn("method name");
		when(responseObj.getStatus()).thenReturn(200);
		when(requestObj.getRequestURI()).thenReturn("Url");
		assertNotNull(introspectInterceptor.buildRuleList(requestDetails));
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	void testServiceRequest() throws IOException {
		RequestDetails requestDetails = Mockito.mock(RequestDetails.class);
		when(requestDetails.getRestOperationType()).thenReturn(RestOperationTypeEnum.READ);
		when(requestDetails.getResourceName()).thenReturn(ResourceType.SERVICEREQUEST.toString());
		when(requestDetails.getHeader("Authorization")).thenReturn("bearer test.test.test");
		when(requestDetails.getRequestPath()).thenReturn("http://localhost:8090/fhir/r4/1005/ServiceRequest/1032702");
		when(requestDetails.getCompleteUrl()).thenReturn("http://localhost:8090/fhir/r4/1005/ServiceRequest/1032702");
		Mockito.when(authConfigurationService.getBaseURL(anyString())).thenReturn("http://localhost:8090/fhir/r4/1005");
		String responseBody = TestUtil.convertJsonToJsonString("bundle/tokenresponse.json");
		when(authConfigurationService.getAuthConfiguration()).thenReturn(TestUtil.getAuthConfiguration());

		ResponseEntity<String> responseEntity = new ResponseEntity(responseBody, HttpStatus.OK);
		when(resttemplate.postForEntity(anyString(), any(), eq(String.class))).thenReturn(responseEntity);
		when(requestObj.getMethod()).thenReturn("method name");
		when(responseObj.getStatus()).thenReturn(200);
		when(requestObj.getRequestURI()).thenReturn("Url");
		assertNotNull(introspectInterceptor.buildRuleList(requestDetails));
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	void testClaim() throws IOException {
		RequestDetails requestDetails = Mockito.mock(RequestDetails.class);
		when(requestDetails.getRestOperationType()).thenReturn(RestOperationTypeEnum.READ);
		when(requestDetails.getResourceName()).thenReturn(ResourceType.CLAIM.toString());
		when(requestDetails.getHeader("Authorization")).thenReturn("bearer test.test.test");
		when(requestDetails.getRequestPath()).thenReturn("http://localhost:8090/fhir/r4/1005/Claim/1032702");
		when(requestDetails.getCompleteUrl()).thenReturn("http://localhost:8090/fhir/r4/1005/Claim/1032702");
		Mockito.when(authConfigurationService.getBaseURL(anyString())).thenReturn("http://localhost:8090/fhir/r4/1005");
		String responseBody = TestUtil.convertJsonToJsonString("bundle/tokenresponse.json");
		when(authConfigurationService.getAuthConfiguration()).thenReturn(TestUtil.getAuthConfiguration());

		ResponseEntity<String> responseEntity = new ResponseEntity(responseBody, HttpStatus.OK);
		when(resttemplate.postForEntity(anyString(), any(), eq(String.class))).thenReturn(responseEntity);
		when(requestObj.getMethod()).thenReturn("method name");
		when(responseObj.getStatus()).thenReturn(200);
		when(requestObj.getRequestURI()).thenReturn("Url");
		assertNotNull(introspectInterceptor.buildRuleList(requestDetails));
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	void testClaimResponse() throws IOException {
		RequestDetails requestDetails = Mockito.mock(RequestDetails.class);
		when(requestDetails.getRestOperationType()).thenReturn(RestOperationTypeEnum.READ);
		when(requestDetails.getResourceName()).thenReturn(ResourceType.CLAIMRESPONSE.toString());
		when(requestDetails.getHeader("Authorization")).thenReturn("bearer test.test.test");
		when(requestDetails.getRequestPath()).thenReturn("http://localhost:8090/fhir/r4/1005/ClaimResponse/1032702");
		when(requestDetails.getCompleteUrl()).thenReturn("http://localhost:8090/fhir/r4/1005/ClaimResponse/1032702");
		Mockito.when(authConfigurationService.getBaseURL(anyString())).thenReturn("http://localhost:8090/fhir/r4/1005");
		String responseBody = TestUtil.convertJsonToJsonString("bundle/tokenresponse.json");
		when(authConfigurationService.getAuthConfiguration()).thenReturn(TestUtil.getAuthConfiguration());

		ResponseEntity<String> responseEntity = new ResponseEntity(responseBody, HttpStatus.OK);
		when(resttemplate.postForEntity(anyString(), any(), eq(String.class))).thenReturn(responseEntity);
		when(requestObj.getMethod()).thenReturn("method name");
		when(responseObj.getStatus()).thenReturn(200);
		when(requestObj.getRequestURI()).thenReturn("Url");
		assertNotNull(introspectInterceptor.buildRuleList(requestDetails));
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	void testCoverage() throws IOException {
		RequestDetails requestDetails = Mockito.mock(RequestDetails.class);
		when(requestDetails.getRestOperationType()).thenReturn(RestOperationTypeEnum.READ);
		when(requestDetails.getResourceName()).thenReturn(ResourceType.COVERAGE.toString());
		when(requestDetails.getHeader("Authorization")).thenReturn("bearer test.test.test");
		when(requestDetails.getRequestPath()).thenReturn("http://localhost:8090/fhir/r4/1005/Coverage/1032702");
		when(requestDetails.getCompleteUrl()).thenReturn("http://localhost:8090/fhir/r4/1005/Coverage/1032702");
		Mockito.when(authConfigurationService.getBaseURL(anyString())).thenReturn("http://localhost:8090/fhir/r4/1005");
		String responseBody = TestUtil.convertJsonToJsonString("bundle/tokenresponse.json");
		when(authConfigurationService.getAuthConfiguration()).thenReturn(TestUtil.getAuthConfiguration());

		ResponseEntity<String> responseEntity = new ResponseEntity(responseBody, HttpStatus.OK);
		when(resttemplate.postForEntity(anyString(), any(), eq(String.class))).thenReturn(responseEntity);
		when(requestObj.getMethod()).thenReturn("method name");
		when(responseObj.getStatus()).thenReturn(200);
		when(requestObj.getRequestURI()).thenReturn("Url");
		assertNotNull(introspectInterceptor.buildRuleList(requestDetails));
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	void testExplanationOfBenefit() throws IOException {
		RequestDetails requestDetails = Mockito.mock(RequestDetails.class);
		when(requestDetails.getRestOperationType()).thenReturn(RestOperationTypeEnum.READ);
		when(requestDetails.getResourceName()).thenReturn(ResourceType.EXPLANATIONOFBENEFIT.toString());
		when(requestDetails.getHeader("Authorization")).thenReturn("bearer test.test.test");
		when(requestDetails.getRequestPath())
				.thenReturn("http://localhost:8090/fhir/r4/1005/ExplanationOfBenefit/1032702");
		when(requestDetails.getCompleteUrl())
				.thenReturn("http://localhost:8090/fhir/r4/1005/ExplanationOfBenefit/1032702");
		Mockito.when(authConfigurationService.getBaseURL(anyString())).thenReturn("http://localhost:8090/fhir/r4/1005");
		String responseBody = TestUtil.convertJsonToJsonString("bundle/tokenresponse.json");
		when(authConfigurationService.getAuthConfiguration()).thenReturn(TestUtil.getAuthConfiguration());

		ResponseEntity<String> responseEntity = new ResponseEntity(responseBody, HttpStatus.OK);
		when(resttemplate.postForEntity(anyString(), any(), eq(String.class))).thenReturn(responseEntity);
		when(requestObj.getMethod()).thenReturn("method name");
		when(responseObj.getStatus()).thenReturn(200);
		when(requestObj.getRequestURI()).thenReturn("Url");
		assertNotNull(introspectInterceptor.buildRuleList(requestDetails));
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	void testFamilyMemberHistory() throws IOException {
		RequestDetails requestDetails = Mockito.mock(RequestDetails.class);
		when(requestDetails.getRestOperationType()).thenReturn(RestOperationTypeEnum.READ);
		when(requestDetails.getResourceName()).thenReturn(ResourceType.FAMILYMEMBERHISTORY.toString());
		when(requestDetails.getHeader("Authorization")).thenReturn("bearer test.test.test");
		when(requestDetails.getRequestPath())
				.thenReturn("http://localhost:8090/fhir/r4/1005/FamilyMemberHistory/1032702");
		when(requestDetails.getCompleteUrl())
				.thenReturn("http://localhost:8090/fhir/r4/1005/FamilyMemberHistory/1032702");
		Mockito.when(authConfigurationService.getBaseURL(anyString())).thenReturn("http://localhost:8090/fhir/r4/1005");
		String responseBody = TestUtil.convertJsonToJsonString("bundle/tokenresponse.json");
		when(authConfigurationService.getAuthConfiguration()).thenReturn(TestUtil.getAuthConfiguration());

		ResponseEntity<String> responseEntity = new ResponseEntity(responseBody, HttpStatus.OK);
		when(resttemplate.postForEntity(anyString(), any(), eq(String.class))).thenReturn(responseEntity);
		when(requestObj.getMethod()).thenReturn("method name");
		when(responseObj.getStatus()).thenReturn(200);
		when(requestObj.getRequestURI()).thenReturn("Url");
		assertNotNull(introspectInterceptor.buildRuleList(requestDetails));
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	void testHealthcareService() throws IOException {
		RequestDetails requestDetails = Mockito.mock(RequestDetails.class);
		when(requestDetails.getRestOperationType()).thenReturn(RestOperationTypeEnum.READ);
		when(requestDetails.getResourceName()).thenReturn(ResourceType.HEALTHCARESERVICE.toString());
		when(requestDetails.getHeader("Authorization")).thenReturn("bearer test.test.test");
		when(requestDetails.getRequestPath())
				.thenReturn("http://localhost:8090/fhir/r4/1005/HealthcareService/1032702");
		when(requestDetails.getCompleteUrl())
				.thenReturn("http://localhost:8090/fhir/r4/1005/HealthcareService/1032702");
		Mockito.when(authConfigurationService.getBaseURL(anyString())).thenReturn("http://localhost:8090/fhir/r4/1005");
		String responseBody = TestUtil.convertJsonToJsonString("bundle/tokenresponse.json");
		when(authConfigurationService.getAuthConfiguration()).thenReturn(TestUtil.getAuthConfiguration());

		ResponseEntity<String> responseEntity = new ResponseEntity(responseBody, HttpStatus.OK);
		when(resttemplate.postForEntity(anyString(), any(), eq(String.class))).thenReturn(responseEntity);
		when(requestObj.getMethod()).thenReturn("method name");
		when(responseObj.getStatus()).thenReturn(200);
		when(requestObj.getRequestURI()).thenReturn("Url");
		assertNotNull(introspectInterceptor.buildRuleList(requestDetails));
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	void testEndpoint() throws IOException {
		RequestDetails requestDetails = Mockito.mock(RequestDetails.class);
		when(requestDetails.getRestOperationType()).thenReturn(RestOperationTypeEnum.READ);
		when(requestDetails.getResourceName()).thenReturn(ResourceType.ENDPOINT.toString());
		when(requestDetails.getHeader("Authorization")).thenReturn("bearer test.test.test");
		when(requestDetails.getRequestPath()).thenReturn("http://localhost:8090/fhir/r4/1005/Endpoint/1032702");
		when(requestDetails.getCompleteUrl()).thenReturn("http://localhost:8090/fhir/r4/1005/Endpoint/1032702");
		Mockito.when(authConfigurationService.getBaseURL(anyString())).thenReturn("http://localhost:8090/fhir/r4/1005");
		String responseBody = TestUtil.convertJsonToJsonString("bundle/tokenresponse.json");
		when(authConfigurationService.getAuthConfiguration()).thenReturn(TestUtil.getAuthConfiguration());

		ResponseEntity<String> responseEntity = new ResponseEntity(responseBody, HttpStatus.OK);
		when(resttemplate.postForEntity(anyString(), any(), eq(String.class))).thenReturn(responseEntity);
		when(requestObj.getMethod()).thenReturn("method name");
		when(responseObj.getStatus()).thenReturn(200);
		when(requestObj.getRequestURI()).thenReturn("Url");
		assertNotNull(introspectInterceptor.buildRuleList(requestDetails));
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	void testInsurancePlan() throws IOException {
		RequestDetails requestDetails = Mockito.mock(RequestDetails.class);
		when(requestDetails.getRestOperationType()).thenReturn(RestOperationTypeEnum.READ);
		when(requestDetails.getResourceName()).thenReturn(ResourceType.INSURANCEPLAN.toString());
		when(requestDetails.getHeader("Authorization")).thenReturn("bearer test.test.test");
		when(requestDetails.getRequestPath()).thenReturn("http://localhost:8090/fhir/r4/1005/InsurancePlan/1032702");
		when(requestDetails.getCompleteUrl()).thenReturn("http://localhost:8090/fhir/r4/1005/InsurancePlan/1032702");
		Mockito.when(authConfigurationService.getBaseURL(anyString())).thenReturn("http://localhost:8090/fhir/r4/1005");
		String responseBody = TestUtil.convertJsonToJsonString("bundle/tokenresponse.json");
		when(authConfigurationService.getAuthConfiguration()).thenReturn(TestUtil.getAuthConfiguration());

		ResponseEntity<String> responseEntity = new ResponseEntity(responseBody, HttpStatus.OK);
		when(resttemplate.postForEntity(anyString(), any(), eq(String.class))).thenReturn(responseEntity);
		when(requestObj.getMethod()).thenReturn("method name");
		when(responseObj.getStatus()).thenReturn(200);
		when(requestObj.getRequestURI()).thenReturn("Url");
		assertNotNull(introspectInterceptor.buildRuleList(requestDetails));
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	void testList() throws IOException {
		RequestDetails requestDetails = Mockito.mock(RequestDetails.class);
		when(requestDetails.getRestOperationType()).thenReturn(RestOperationTypeEnum.READ);
		when(requestDetails.getResourceName()).thenReturn(ResourceType.LIST.toString());
		when(requestDetails.getHeader("Authorization")).thenReturn("bearer test.test.test");
		when(requestDetails.getRequestPath()).thenReturn("http://localhost:8090/fhir/r4/1005/List/1032702");
		when(requestDetails.getCompleteUrl()).thenReturn("http://localhost:8090/fhir/r4/1005/List/1032702");
		Mockito.when(authConfigurationService.getBaseURL(anyString())).thenReturn("http://localhost:8090/fhir/r4/1005");
		String responseBody = TestUtil.convertJsonToJsonString("bundle/tokenresponse.json");
		when(authConfigurationService.getAuthConfiguration()).thenReturn(TestUtil.getAuthConfiguration());

		ResponseEntity<String> responseEntity = new ResponseEntity(responseBody, HttpStatus.OK);
		when(resttemplate.postForEntity(anyString(), any(), eq(String.class))).thenReturn(responseEntity);
		when(requestObj.getMethod()).thenReturn("method name");
		when(responseObj.getStatus()).thenReturn(200);
		when(requestObj.getRequestURI()).thenReturn("Url");
		assertNotNull(introspectInterceptor.buildRuleList(requestDetails));
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	void testTokenFalseNegativeScenerio() throws IOException {
		RequestDetails requestDetails = Mockito.mock(RequestDetails.class);
		when(requestDetails.getRestOperationType()).thenReturn(RestOperationTypeEnum.READ);
		when(requestDetails.getResourceName()).thenReturn(ResourceType.LIST.toString());
		when(requestDetails.getHeader("Authorization")).thenReturn("bearer test.test.test");
		when(requestDetails.getRequestPath()).thenReturn("http://localhost:8090/fhir/r4/1005/List/1032702");
		when(requestDetails.getCompleteUrl()).thenReturn("http://localhost:8090/fhir/r4/1005/List/1032702");
		Mockito.when(authConfigurationService.getBaseURL(anyString())).thenReturn("http://localhost:8090/fhir/r4/1005");
		String responseBody = TestUtil.convertJsonToJsonString("bundle/negative-tokenresponse.json");
		when(authConfigurationService.getAuthConfiguration()).thenReturn(TestUtil.getAuthConfiguration());

		ResponseEntity<String> responseEntity = new ResponseEntity(responseBody, HttpStatus.OK);
		when(resttemplate.postForEntity(anyString(), any(), eq(String.class))).thenReturn(responseEntity);
		when(requestObj.getMethod()).thenReturn("method name");
		when(responseObj.getStatus()).thenReturn(200);
		when(requestObj.getRequestURI()).thenReturn("Url");
		assertThrows(AuthorizationException.class, () -> introspectInterceptor.buildRuleList(requestDetails));

	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	void testPracticeandPatientMatchNegativeScenerio() throws IOException {
		RequestDetails requestDetails = Mockito.mock(RequestDetails.class);
		when(requestDetails.getRequestPath()).thenReturn("http://localhost:8090/fhir/r4/1008/List/1032702");
		when(requestDetails.getCompleteUrl()).thenReturn("http://localhost:8090/fhir/r4/1008/List/1032702");
		Mockito.when(authConfigurationService.getBaseURL(anyString())).thenReturn("http://localhost:8090/fhir/r4/1008");
		String responseBody = TestUtil.convertJsonToJsonString("bundle/tokenresponse.json");
		when(authConfigurationService.getAuthConfiguration()).thenReturn(TestUtil.getAuthConfiguration());

		ResponseEntity<String> responseEntity = new ResponseEntity(responseBody, HttpStatus.OK);
		when(resttemplate.postForEntity(anyString(), any(), eq(String.class))).thenReturn(responseEntity);
		when(requestObj.getMethod()).thenReturn("method name");
		when(responseObj.getStatus()).thenReturn(200);
		when(requestObj.getRequestURI()).thenReturn("Url");
		assertThrows(AuthorizationException.class, () -> introspectInterceptor.buildRuleList(requestDetails));

	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	void testWellKnown() throws IOException {
		RequestDetails requestDetails = Mockito.mock(RequestDetails.class);
		when(requestDetails.getRequestPath()).thenReturn("http://localhost:8090/fhir/.well-known");
		when(requestDetails.getCompleteUrl()).thenReturn("http://localhost:8090/fhir/.well-known");
		Mockito.when(authConfigurationService.getBaseURL(anyString())).thenReturn("http://localhost:8090/fhir/r4/1005");
		String responseBody = TestUtil.convertJsonToJsonString("bundle/tokenresponse.json");
		when(authConfigurationService.getAuthConfiguration()).thenReturn(TestUtil.getAuthConfiguration());

		ResponseEntity<String> responseEntity = new ResponseEntity(responseBody, HttpStatus.OK);
		when(resttemplate.postForEntity(anyString(), any(), eq(String.class))).thenReturn(responseEntity);
		when(requestObj.getMethod()).thenReturn("method name");
		when(responseObj.getStatus()).thenReturn(200);
		when(requestObj.getRequestURI()).thenReturn("Url");
		assertNotNull(introspectInterceptor.buildRuleList(requestDetails));

	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	void testSearchAPI() throws IOException {
		RequestDetails requestDetails = Mockito.mock(RequestDetails.class);
		when(requestDetails.getResourceName()).thenReturn(ResourceType.PATIENT.toString());
		when(requestDetails.getRestOperationType()).thenReturn(RestOperationTypeEnum.SEARCH_TYPE);
		when(requestDetails.getHeader("Authorization")).thenReturn("bearer test.test.test");
		when(requestDetails.getRequestPath()).thenReturn("http://localhost:8090/fhir/r4/1005/search-api/1032702");
		when(requestDetails.getCompleteUrl()).thenReturn("http://localhost:8090/fhir/r4/1005/search-api/1032702");
		Mockito.when(authConfigurationService.getBaseURL(anyString())).thenReturn("http://localhost:8090/fhir/r4/1005");
		String responseBody = TestUtil.convertJsonToJsonString("bundle/tokenresponse.json");
		when(authConfigurationService.getAuthConfiguration()).thenReturn(TestUtil.getAuthConfiguration());

		ResponseEntity<String> responseEntity = new ResponseEntity(responseBody, HttpStatus.OK);
		when(resttemplate.postForEntity(anyString(), any(), eq(String.class))).thenReturn(responseEntity);
		when(requestObj.getMethod()).thenReturn("method name");
		when(responseObj.getStatus()).thenReturn(200);
		when(requestObj.getRequestURI()).thenReturn("Url");
		assertNotNull(introspectInterceptor.buildRuleList(requestDetails));

	}

	// @Test
	/*
	 * void InvalidRequestTest() throws IOException{
	 * 
	 * MultiValueMap<String, String> headers = new LinkedMultiValueMap<String,
	 * String>(); headers.add("Accept", "application/fhir+json;fhirVersion=4.0");
	 * headers.add("Content-Type", "application/fhir+json;fhirVersion=4.0");
	 * headers.add("X-AUTH-TOKEN", "test"); headers.add("Authorization",
	 * "bearer test"); HttpEntity<Object> request = new HttpEntity<>(headers);
	 * RequestDetails requestDetails = Mockito.mock(RequestDetails.class);
	 * when(requestDetails.getHeader("Authorization")).thenReturn("bearer test");
	 * when(requestDetails.getRequestPath()).thenReturn(
	 * "http://localhost:8090/fhir/r4/1006/invalid-url/1032702");
	 * when(requestDetails.getCompleteUrl()).thenReturn(
	 * "http://localhost:8090/fhir/r4/1006/invalid-url/1032702");
	 * Mockito.when(authConfigurationService.getBaseURL(anyString())).thenReturn(
	 * "http://localhost:8090/fhir/r4/1005"); String responseBody =
	 * TestUtil.convertJsonToJsonString("bundle/tokenresponse.json");
	 * when(authConfigurationService.getAuthConfiguration()).thenReturn(TestUtil.
	 * getAuthConfiguration());
	 * 
	 * ResponseEntity<String> responseEntity = new ResponseEntity(responseBody,
	 * HttpStatus.OK); when(resttemplate.postForEntity(anyString(), any(),
	 * eq(String.class))).thenReturn(responseEntity);
	 * when(requestObj.getMethod()).thenReturn("method name");
	 * when(responseObj.getStatus()).thenReturn(200);
	 * when(requestObj.getRequestURI()).thenReturn("Url");
	 * //assertNotNull(introspectInterceptor.buildRuleList(requestDetails));
	 * assertThrows(AuthorizationException.class,()->introspectInterceptor.
	 * buildRuleList(requestDetails));
	 * 
	 * 
	 * }
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	void InvalidRequestHeader() throws IOException {
		RequestDetails requestDetails = Mockito.mock(RequestDetails.class);
		when(requestDetails.getRestOperationType()).thenReturn(RestOperationTypeEnum.READ);
		when(requestDetails.getHeader("Authorization")).thenReturn("bearer test.test.test");
		when(requestDetails.getRequestPath()).thenReturn("http://localhost:8090/fhir/r4/1005/invalid-url/1032702");
		when(requestDetails.getCompleteUrl()).thenReturn("http://localhost:8090/fhir/r4/1005/invalid-url/1032702");
		Mockito.when(authConfigurationService.getBaseURL(anyString())).thenReturn("http://localhost:8090/fhir/r4/1005");
		String responseBody = TestUtil.convertJsonToJsonString("bundle/missing-tokenresponse.json");
		when(authConfigurationService.getAuthConfiguration()).thenReturn(TestUtil.getAuthConfiguration());

		ResponseEntity<String> responseEntity = new ResponseEntity(responseBody, HttpStatus.OK);
		when(resttemplate.postForEntity(anyString(), any(), eq(String.class))).thenReturn(responseEntity);
		when(requestObj.getMethod()).thenReturn("method name");
		when(responseObj.getStatus()).thenReturn(200);
		when(requestObj.getRequestURI()).thenReturn("Url");
		when(requestDetails.getHeader("Authorization")).thenThrow(AuthenticationException.class);
		assertNull(introspectInterceptor.buildRuleList(requestDetails));

	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	void testLaunchPatient() throws IOException {
		RequestDetails requestDetails = Mockito.mock(RequestDetails.class);
		when(requestDetails.getRestOperationType()).thenReturn(RestOperationTypeEnum.READ);
		when(requestDetails.getResourceName()).thenReturn(ResourceType.PATIENT.toString());
		when(requestDetails.getHeader("Authorization")).thenReturn("bearer test.test.test");
		when(requestDetails.getRequestPath()).thenReturn("http://localhost:8090/fhir/r4/1005/launchpatient/1032702");
		when(requestDetails.getCompleteUrl()).thenReturn("http://localhost:8090/fhir/r4/1005/launchpatient/1032702");
		Mockito.when(authConfigurationService.getBaseURL(anyString())).thenReturn("http://localhost:8090/fhir/r4/1005");
		String responseBody = TestUtil.convertJsonToJsonString("bundle/tokenresponse.json");
		when(authConfigurationService.getAuthConfiguration()).thenReturn(TestUtil.getAuthConfiguration());

		ResponseEntity<String> responseEntity = new ResponseEntity(responseBody, HttpStatus.OK);
		when(resttemplate.postForEntity(anyString(), any(), eq(String.class))).thenReturn(responseEntity);
		when(requestObj.getMethod()).thenReturn("method name");
		when(responseObj.getStatus()).thenReturn(200);
		when(requestObj.getRequestURI()).thenReturn("Url");
		assertNotNull(introspectInterceptor.buildRuleList(requestDetails));

	}
//	@SuppressWarnings({ "unchecked", "rawtypes" })
//	@Test
//	void testResourcelist() throws IOException {
//		RequestDetails requestDetails = Mockito.mock(RequestDetails.class);
//		//when(requestDetails.getRestOperationType()).thenReturn(RestOperationTypeEnum.READ);
//		//when(requestDetails.getHeader("Authorization")).thenReturn("bearer test.test.test");
//		when(requestDetails.getRequestPath())
//				.thenReturn("http://localhost:8090/fhir/r4/1005/resourcelist/count/1032702");
//		when(requestDetails.getCompleteUrl())
//				.thenReturn("http://localhost:8090/fhir/r4/1005/resourcelist/count/1032702");
//		Mockito.when(authConfigurationService.getBaseURL(anyString())).thenReturn("http://localhost:8090/fhir/r4/1005");
//		String responseBody = TestUtil.convertJsonToJsonString("bundle/tokenresponse.json");
//		when(authConfigurationService.getAuthConfiguration()).thenReturn(TestUtil.getAuthConfiguration());
//
//		ResponseEntity<String> responseEntity = new ResponseEntity(responseBody, HttpStatus.OK);
//		when(resttemplate.postForEntity(anyString(), any(), eq(String.class))).thenReturn(responseEntity);
//		when(requestObj.getMethod()).thenReturn("method name");
//		when(responseObj.getStatus()).thenReturn(200);
//		when(requestObj.getRequestURI()).thenReturn("Url");
//		assertNotNull(introspectInterceptor.buildRuleList(requestDetails));
//
//	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	void testInvalidTokenHeader() throws IOException {
		RequestDetails requestDetails = Mockito.mock(RequestDetails.class);
		when(requestDetails.getHeader("Authorization")).thenReturn("beare test.test.test");
		when(requestDetails.getRestOperationType()).thenReturn(RestOperationTypeEnum.READ);
		when(requestDetails.getRequestPath())
				.thenReturn("http://localhost:8090/fhir/r4/1005/resourcelist/count/1032702");
		when(requestDetails.getCompleteUrl())
				.thenReturn("http://localhost:8090/fhir/r4/1005/resourcelist/count/1032702");
		Mockito.when(authConfigurationService.getBaseURL(anyString())).thenReturn("http://localhost:8090/fhir/r4/1005");
		String responseBody = TestUtil.convertJsonToJsonString("bundle/tokenresponse.json");
		when(authConfigurationService.getAuthConfiguration()).thenReturn(TestUtil.getAuthConfiguration());

		ResponseEntity<String> responseEntity = new ResponseEntity(responseBody, HttpStatus.OK);
		when(resttemplate.postForEntity(anyString(), any(), eq(String.class))).thenReturn(responseEntity);
		when(requestObj.getMethod()).thenReturn("method name");
		when(responseObj.getStatus()).thenReturn(200);
		when(requestObj.getRequestURI()).thenReturn("Url");
		assertThrows(AuthorizationException.class, () -> introspectInterceptor.buildRuleList(requestDetails));

	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	void testForbidden() throws IOException {
		RequestDetails requestDetails = Mockito.mock(RequestDetails.class);
		when(requestDetails.getHeader("Authorization")).thenReturn("bearer test.test.test");
		when(requestDetails.getRestOperationType()).thenReturn(RestOperationTypeEnum.READ);
		when(requestDetails.getResourceName()).thenReturn(ResourceType.ALLERGYINTOLERANCE.toString());
		when(requestDetails.getRequestPath()).thenReturn("http://localhost:8090/fhir/r4/1005/testurl/1032702");
		when(requestDetails.getCompleteUrl()).thenReturn("http://localhost:8090/fhir/r4/1005/testurl/1032702");
		Mockito.when(authConfigurationService.getBaseURL(anyString())).thenReturn("http://localhost:8090/fhir/r4/1005");
		String responseBody = TestUtil.convertJsonToJsonString("bundle/tokenresponse.json");
		when(authConfigurationService.getAuthConfiguration()).thenReturn(TestUtil.getAuthConfiguration());

		ResponseEntity<String> responseEntity = new ResponseEntity(responseBody, HttpStatus.OK);
		when(resttemplate.postForEntity(anyString(), any(), eq(String.class))).thenReturn(responseEntity);
		when(requestObj.getMethod()).thenReturn("method name");
		when(responseObj.getStatus()).thenReturn(200);
		when(requestObj.getRequestURI()).thenReturn("Url");
		assertNotNull(introspectInterceptor.buildRuleList(requestDetails));

	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	void testBinary() throws IOException {
		RequestDetails requestDetails = Mockito.mock(RequestDetails.class);
		when(requestDetails.getHeader("Authorization")).thenReturn("bearer test.test.test");
		when(requestDetails.getRestOperationType()).thenReturn(RestOperationTypeEnum.READ);
		when(requestDetails.getResourceName()).thenReturn(ResourceType.BINARY.toString());
		when(requestDetails.getRequestPath()).thenReturn("http://localhost:8090/fhir/r4/1005/Binary/1032702");
		when(requestDetails.getCompleteUrl()).thenReturn("http://localhost:8090/fhir/r4/1005/Binary/1032702");
		Mockito.when(authConfigurationService.getBaseURL(anyString())).thenReturn("http://localhost:8090/fhir/r4/1005");
		String responseBody = TestUtil.convertJsonToJsonString("bundle/tokenresponse.json");
		when(authConfigurationService.getAuthConfiguration()).thenReturn(TestUtil.getAuthConfiguration());

		ResponseEntity<String> responseEntity = new ResponseEntity(responseBody, HttpStatus.OK);
		when(resttemplate.postForEntity(anyString(), any(), eq(String.class))).thenReturn(responseEntity);
		when(requestObj.getMethod()).thenReturn("method name");
		when(responseObj.getStatus()).thenReturn(200);
		when(requestObj.getRequestURI()).thenReturn("Url");
		assertNotNull(introspectInterceptor.buildRuleList(requestDetails));
	}

}
