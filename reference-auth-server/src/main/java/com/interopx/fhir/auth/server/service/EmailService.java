package com.interopx.fhir.auth.server.service;

import java.util.Properties;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @Autowired UsersService userRegistrationService;

  public void sendEmail(String toAddress, String subject, String content) {
    logger.debug("Start in sendEmail() of EmailService class ");
    String authUser = userRegistrationService.getPropertyValue("mail_sender_username");
    String authPwd = userRegistrationService.getPropertyValue("mail_sender_password");
    String fromEmail = userRegistrationService.getPropertyValue("sender_id");
    try {
      final Session mailSession =
          Session.getInstance(
              this.getEmailProperties(),
              new Authenticator() {

                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                  return new PasswordAuthentication(authUser, authPwd);
                }
              });

      mailSession.setDebug(true);
      MimeMessage message = new MimeMessage(mailSession);
      message.setSubject(subject);
      message.setContent(content, "text/html");
      Address[] from = InternetAddress.parse(fromEmail);
      message.addFrom(from);
      message.setRecipient(Message.RecipientType.TO, new InternetAddress(toAddress));
      Transport.send(message);
      logger.debug("Completed sending email successfully...");
    } catch (Exception e) {
      logger.error("Exception in sendEmail() of EmailService class ", e);
    }
  }

  public Properties getEmailProperties() {
    logger.debug("Start in getEmailProperties() of EmailService class ");
    final Properties props = new Properties();
    try {
      String mailTransportProtocolKey =
          userRegistrationService.getPropertyValue("mail_transport_protocol_key");
      String mailTransportProtocolValue =
          userRegistrationService.getPropertyValue("mail_transport_protocol_value");
      String mailSmtpPortKey = userRegistrationService.getPropertyValue("mail_smtp_port_key");
      String mailSmtpPortValue = userRegistrationService.getPropertyValue("mail_smtp_port_value");
      String mailSmtpHostKey = userRegistrationService.getPropertyValue("mail_smtp_host_key");
      String mailSmtpHostValue = userRegistrationService.getPropertyValue("mail_smtp_host_value");
      String mailSmtpAuthKey = userRegistrationService.getPropertyValue("mail_smtp_auth_key");
      String mailSmtpAuthValue = userRegistrationService.getPropertyValue("mail_smtp_auth_value");
      String mailSmtpStarttlsEnableKey =
          userRegistrationService.getPropertyValue("mail_smtp_starttls_enable_key");
      String mailSmtpStarttlsEnableValue =
          userRegistrationService.getPropertyValue("mail_smtp_starttls_enable_value");

      props.put(mailTransportProtocolKey, mailTransportProtocolValue);
      props.put(mailSmtpPortKey, Integer.parseInt(mailSmtpPortValue));
      props.put(mailSmtpHostKey, mailSmtpHostValue);
      props.put(mailSmtpAuthKey, mailSmtpAuthValue);
      props.put(mailSmtpStarttlsEnableKey, mailSmtpStarttlsEnableValue);
    } catch (Exception e) {
      logger.error("Exception in sendEmail() of EmailService class ", e);
    }
    logger.debug("End in getEmailProperties() of EmailService class ");
    return props;
  }
}
