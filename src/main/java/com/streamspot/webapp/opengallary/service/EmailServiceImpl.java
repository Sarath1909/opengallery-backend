package com.streamspot.webapp.opengallary.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.streamspot.webapp.opengallary.exception.EmailSendException;

@Service
public class EmailServiceImpl implements EmailService {

	@Autowired
	JavaMailSender mailSender;
    private static final Logger LOGGER = LoggerFactory.getLogger(EmailServiceImpl.class);
    
    @Override
    public void sendEmail(String toAddress, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toAddress);
            message.setSubject(subject);
            message.setText(body);

            mailSender.send(message);
            LOGGER.info("Email successfully sent to {}", toAddress);

        } catch (MailException ex) {
            // Covers MessagingException, MailSendException, MailAuthenticationException etc.
            LOGGER.error("Failed to send email to {}. Error: {}", toAddress, ex.getMessage(), ex);
            // Optionally rethrow or wrap in custom exception if you want to notify upstream services
            throw new EmailSendException("Email delivery failed to " + toAddress, ex);
        }
    }
}

