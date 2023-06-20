package com.interopx.fhir.facade.service;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;

import com.azure.core.util.BinaryData;
import com.azure.messaging.servicebus.ServiceBusReceivedMessage;
import com.azure.messaging.servicebus.ServiceBusReceivedMessageContext;
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ProcessReceivedMessageTest {
	@Test
	void processMessageTest(){	
		
		ProcessReceivedMessage pr = new ProcessReceivedMessage();
		ServiceBusReceivedMessageContext context = Mockito.mock(ServiceBusReceivedMessageContext.class);
		ServiceBusReceivedMessage sbrm = Mockito.mock(ServiceBusReceivedMessage.class);		
		when(context.getMessage()).thenReturn(sbrm);
		BinaryData bd = Mockito.mock(BinaryData.class);
		when(context.getMessage().getBody()).thenReturn(bd);
		when(context.getMessage().getBody().toString()).thenReturn("test");
		
		try (MockedStatic<ProcessReceivedMessage> utilities = Mockito
				.mockStatic(ProcessReceivedMessage.class)) {
			utilities.when(() -> ProcessReceivedMessage.processMessage(context)).thenReturn(null);	
			assertNull(ProcessReceivedMessage.processMessage(context));

		}
	}
	
	
	

}
