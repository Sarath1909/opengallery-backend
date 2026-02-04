package com.streamspot.webapp.opengallary.service;

public interface EmailService {
    void sendEmail(String to, String subject, String body);
}

