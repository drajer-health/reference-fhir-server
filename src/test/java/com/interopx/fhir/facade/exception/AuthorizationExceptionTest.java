package com.interopx.fhir.facade.exception;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.hl7.fhir.r4.model.OperationOutcome;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class AuthorizationExceptionTest {

	@Test
	void UnAutorizedCodeTest() {		
		
		assertNotNull(new AuthorizationException("401 Unauthorized: Authentication token is invalid.",
				getOperationOutcome(401)));
	}
	
	@Test
	void ForbiddenTest() {		
		
		assertNotNull(new AuthorizationException(403,"Forbidden: Authentication was provided, but the authenticated user is not permitted to perform the requested operation",
				getOperationOutcome(403)));	
		
	}
	
	private OperationOutcome getOperationOutcome(int statusCode) {
		OperationOutcome oo = new OperationOutcome();
		if (statusCode == 403) {
			oo.addIssue().setSeverity(OperationOutcome.IssueSeverity.ERROR).setDiagnostics(
					"Forbidden: Authentication was provided, but the authenticated user is not permitted to perform the requested operation");
		} else {
			oo.addIssue().setSeverity(OperationOutcome.IssueSeverity.ERROR)
					.setDiagnostics("Unauthorized: No valid token found Authentication token is  invalid OR expired");
		}

		return oo;
	}

}
