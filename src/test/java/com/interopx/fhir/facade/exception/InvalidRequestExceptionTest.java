package com.interopx.fhir.facade.exception;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.hl7.fhir.r4.model.OperationOutcome;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;

import net.bytebuddy.implementation.bytecode.Throw;
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class InvalidRequestExceptionTest {

	@Test
	void InvalidRequestTest1() {

		assertNotNull(new InvalidRequestException("401 Unauthorized: Authentication token is invalid.",
				new Throwable("401 Unauthorised Exception")));
	}
	@Test
	void InvalidRequestTest2() {

		assertNotNull(new InvalidRequestException("401 Unauthorized: Authentication token is invalid."));
	}

	@Test
	void InvalidRequestTest3() {

		assertNotNull(new InvalidRequestException(
				"400 Bad Request: Unable to process request",
				getOperationOutcome("patient")));

	}
	
	@Test
	void InvalidRequestTest4() {

		assertNotNull(new InvalidRequestException(new Throwable("401 Unauthorised Exception")));
	}

	public static OperationOutcome getOperationOutcome(String params) {
		OperationOutcome operationOutcome = new OperationOutcome();
		operationOutcome.addIssue().setSeverity(OperationOutcome.IssueSeverity.ERROR)
				.setCode(OperationOutcome.IssueType.PROCESSING).setDiagnostics(String.format(
						"Invalid request: Does not know how to handle get operation with parameter [%s]", params));

		return operationOutcome;
	}
}
