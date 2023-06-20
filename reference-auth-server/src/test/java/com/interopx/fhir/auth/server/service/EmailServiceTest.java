package com.interopx.fhir.auth.server.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Service;
import javax.mail.Transport;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
class EmailServiceTest {

	@Mock
	UsersService userRegistrationService;
	
	@InjectMocks
	EmailService EmailService;
	@Mock
	Properties properties;
	
	@Mock
	Service service;
	@Mock
	Message message;
	@Mock
	Transport transport;
	
	@Test
	void testSendEmail() throws MessagingException {

		String toAddress="bangalore";
		String subject="subject";
		String content="10";
		
		Mockito.when(userRegistrationService.getPropertyValue("mail_sender_username")).thenReturn(content);
		Mockito.when(userRegistrationService.getPropertyValue("mail_sender_password")).thenReturn(subject);
		Mockito.when(userRegistrationService.getPropertyValue("sender_id")).thenReturn(toAddress);
		
	
		Mockito.when(userRegistrationService.getPropertyValue("mail_transport_protocol_key")).thenReturn(content);
		Mockito.when(userRegistrationService.getPropertyValue("mail_transport_protocol_value")).thenReturn(subject);
		Mockito.when(userRegistrationService.getPropertyValue("mail_smtp_port_key")).thenReturn(toAddress);
		Mockito.when(userRegistrationService.getPropertyValue("mail_smtp_port_value")).thenReturn(content);
		Mockito.when(userRegistrationService.getPropertyValue("mail_smtp_host_key")).thenReturn(subject);
		Mockito.when(userRegistrationService.getPropertyValue("mail_smtp_host_value")).thenReturn(content);
		Mockito.when(userRegistrationService.getPropertyValue("mail_smtp_auth_key")).thenReturn(subject);
		Mockito.when(userRegistrationService.getPropertyValue("mail_smtp_auth_value")).thenReturn(toAddress);
		Mockito.when(userRegistrationService.getPropertyValue("mail_smtp_starttls_enable_key")).thenReturn(subject);
		Mockito.when(userRegistrationService.getPropertyValue("mail_smtp_starttls_enable_value")).thenReturn(content);
		EmailService.sendEmail(toAddress, subject, content);
		
	}

}
