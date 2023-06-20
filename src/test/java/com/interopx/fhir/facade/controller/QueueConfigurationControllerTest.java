package com.interopx.fhir.facade.controller;


import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interopx.fhir.facade.service.QueueConfigurationService;
import com.interopx.fhir.facade.util.QueueConfigurationDto;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class QueueConfigurationControllerTest {

	private MockMvc mockMvc;

	@SpyBean
	QueueConfigurationService queueConfigurationService;

	@SpyBean
	QueueConfigurationController queueConfigurationController;
	
	private static final int queueConfigId = 1;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(queueConfigurationController).build();
	}
	
	@Test
	public void saveConfigurationTest() throws Exception {
		QueueConfigurationDto queueConfigurationDto = new QueueConfigurationDto();
		queueConfigurationDto.setOutQueueConnectionString(
				"Endpoint=sb://ix-service-bus-demo-ns.servicebus.windows.net/;SharedAccessKeyName=Queue2Policy;SharedAccessKey=bVGpSrEsFztYhCORehp7PLiNKqCLrUvm8gBSupp8qTs=;EntityPath=test_queue_2");
		queueConfigurationDto.setOutQueueName("test_queue_2");	
		queueConfigurationDto.setListenersSize(1);
		ObjectMapper mapper = new ObjectMapper();
		String queueString = mapper.writeValueAsString(queueConfigurationDto);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/configuration/save").content(queueString)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
		assertEquals("Configuration created successfully", result.getResponse().getContentAsString());
	}
	
	@Test
	public void saveConfigurationExceptionTest() throws Exception {
		QueueConfigurationDto queueConfigurationDto = new QueueConfigurationDto();
		queueConfigurationDto.setOutQueueConnectionString(
				"Endpoint=sb://ix-service-bus-demo-ns.servicebus.windows.net/;SharedAccessKeyName=Queue2Policy;SharedAccessKey=bVGpSrEsFztYhCORehp7PLiNKqCLrUvm8gBSupp8qTs=;EntityPath=test_queue_2");
		//queueConfigurationDto.setOutQueueName("test_queue_2");	
		queueConfigurationDto.setListenersSize(1);
		ObjectMapper mapper = new ObjectMapper();
		String queueString = mapper.writeValueAsString(queueConfigurationDto);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/configuration/save").content(queueString)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().is5xxServerError()).andReturn();
		assertEquals("Unable to save configuration", result.getResponse().getContentAsString());
	}

	@Test
	public void updateConfigurationTest() throws Exception {
		QueueConfigurationDto queueConfigurationDto = new QueueConfigurationDto();
		queueConfigurationDto.setOutQueueConnectionString(
				"Endpoint=sb://ix-service-bus-demo-ns.servicebus.windows.net/;SharedAccessKeyName=Queue2Policy;SharedAccessKey=bVGpSrEsFztYhCORehp7PLiNKqCLrUvm8gBSupp8qTs=;EntityPath=test_queue_2");
		queueConfigurationDto.setOutQueueName("test_queue_2");
		ObjectMapper mapper = new ObjectMapper();
		String queueString = mapper.writeValueAsString(queueConfigurationDto);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/configuration/update/"+queueConfigId).content(queueString)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
		assertEquals("Configuration updated successfully", result.getResponse().getContentAsString());

	}
	

	@Test
	public void updateConfigurationExceptionTest() throws Exception {
		QueueConfigurationDto queueConfigurationDto = new QueueConfigurationDto();
		queueConfigurationDto.setOutQueueConnectionString(
				"Endpoint=sb://ix-service-bus-demo-ns.servicebus.windows.net/;SharedAccessKeyName=Queue2Policy;SharedAccessKey=bVGpSrEsFztYhCORehp7PLiNKqCLrUvm8gBSupp8qTs=;EntityPath=test_queue_2");
		queueConfigurationDto.setOutQueueName("test_queue_2");
		ObjectMapper mapper = new ObjectMapper();
		String queueString = mapper.writeValueAsString(queueConfigurationDto);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/configuration/update/"+123).content(queueString)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().is5xxServerError()).andReturn();
		assertEquals("Unable to update configuration", result.getResponse().getContentAsString());

	}

	@Test
	public void deleteConfigurationTest() throws Exception{
		RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/configuration/delete/"+queueConfigId)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
		assertEquals("Configuration deleted successfully", result.getResponse().getContentAsString());
		
	}
	@Test
	public void deleteConfigurationExceptionTest() throws Exception{
		RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/configuration/delete/"+123)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().is5xxServerError()).andReturn();
		assertEquals("Unable to delete configuration", result.getResponse().getContentAsString());
		
	}

}
