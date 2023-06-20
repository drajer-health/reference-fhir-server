package com.interopx.fhir.facade.exception;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.hl7.fhir.r4.model.OperationOutcome;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class InternalErrorExceptionTest {

	@Test
	void InternalServerErrorTest() {		
		
		assertNotNull(new InternalErrorException("500 Internal Server Error: Unable to process request",
				getOperationOutcome(500)));
	}
	
	@Test
	void InternalServerErrorTest2() {		
		
		assertNotNull(new InternalErrorException("500 Internal Server Error: Unable to process request"));	
		
	}
	
	public static OperationOutcome getOperationOutcome(int statusCode) {
		OperationOutcome operationOutcome = new OperationOutcome();
		if (statusCode == 500) {
			operationOutcome.addIssue().setSeverity(OperationOutcome.IssueSeverity.ERROR)
			.setCode(OperationOutcome.IssueType.PROCESSING)
			.setDiagnostics("Server Error: Unable to process Request.");
		} 
		return operationOutcome;
	}

}
