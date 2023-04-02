package com.sber.java13.ecoteam.utils;

import org.springframework.mail.SimpleMailMessage;

public class MailUtils {
    
    private MailUtils() {
    }
    
    public static SimpleMailMessage createEmailMessage(final String email, final String subject, final String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject(subject);
        message.setText(text);
        return message;
    }
}
