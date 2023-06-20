 package com.interopx.fhir.facade.queue;

 import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.util.Date;

import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import com.azure.messaging.servicebus.ServiceBusReceivedMessage;
import com.interopx.fhir.facade.dao.AzureQueueDao;
import com.interopx.fhir.facade.model.RequestResponseLog;
import com.interopx.fhir.facade.provider.TestUtil;
import com.interopx.fhir.facade.service.AuthConfigurationService;
import com.interopx.fhir.facade.service.AzureQueueService;
import com.interopx.fhir.facade.util.InteropxRequestQReceiver;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
 class InteropxRequestQReceiverTest {
	protected static final TestRestTemplate testRestTemplate = new TestRestTemplate();
	@Value("${api.base.url}")
	private String baseUrl;
	@SpyBean
	AzureQueueService azureQueueService;
	@SpyBean
	AzureQueueDao azureQueueDao;
	RequestResponseLog response = null;
	@MockBean
	RestTemplate resttemplate;
	
	@SpyBean
	AuthConfigurationService authConfigurationService;
	
	@LocalServerPort
	private Integer port;
	
	 @BeforeEach
		private void setUp() throws IOException, JSONException {
		 
			String jsonString = TestUtil.convertJsonToJsonString("bundle/immunization.json");
			response = new RequestResponseLog();
			response.setPayload(jsonString);
			response.setRequestId("4bf0f3e7-7b0e-4ed1-b486-0d3bf4e47ac9");
			response.setStatus("Completed");
			response.setTimestamp(new Date());
		}
	//@Test
	void ProcessMessageTest() {
		 InteropxRequestQReceiver receiver = mock(InteropxRequestQReceiver.class);	
		 ServiceBusReceivedMessage  serviceBusReceivedMessage  = Mockito.mock(ServiceBusReceivedMessage .class);
		// doReturn(jsonParser).when(fhirR4Context).newJsonParser();
		 
		 assertTrue(ReflectionTestUtils.invokeMethod(receiver, "saveResponseLog", response.getPayload())
			      .equals("id: 1; name: Smith, John"));
		 
	    }

 }
