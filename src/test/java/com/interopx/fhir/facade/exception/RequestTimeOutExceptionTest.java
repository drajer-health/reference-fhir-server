package com.interopx.fhir.facade.exception;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.hl7.fhir.r4.model.OperationOutcome;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class RequestTimeOutExceptionTest {

	@Test
	void RequestTimeOutExceptionTest1() {		
		
		assertNotNull(new RequestTimeOutException());
	}
	
	@Test
	void RequestTimeOutExceptionTest2() {		
		
		assertNotNull(new RequestTimeOutException("500 Server Error: Could not process request"));	
		
	}
	@Test
	void RequestTimeOutExceptionTest3() {		
		
		assertNotNull(new RequestTimeOutException("500 Server Error: Could not process request",
				new Throwable("500 Server Error: Could not process request")));	
		
	}
	@Test
	void RequestTimeOutExceptionTest4() {		
		
		assertNotNull(new MessagingException("Forbidden: Authentication was provided, but the authenticated user is not permitted to perform the requested operation",
				getOperationOutcome(500)));	
		
	}
	
	
	public static OperationOutcome getOperationOutcome(int statusCode) {
		OperationOutcome operationOutcome = new OperationOutcome();
		if (statusCode == 500) {
			operationOutcome.addIssue()
			.setSeverity(OperationOutcome.IssueSeverity.ERROR)
			.setCode(OperationOutcome.IssueType.PROCESSING)
			.setDiagnostics("Server Error: Unable to send messages to Azure Messaging Queue");
		} 
		return operationOutcome;
	}
}
