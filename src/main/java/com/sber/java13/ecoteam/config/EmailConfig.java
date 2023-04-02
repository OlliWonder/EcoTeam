package com.sber.java13.ecoteam.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class EmailConfig {
    @Value("${spring.mail.host}")
    private String mailServerHost;
    
    @Value("${spring.mail.port}")
    private Integer mailServerPort;
    
    @Value("${spring.mail.username}")
    private String mailServerUsername;
    
    @Value("${spring.mail.password}")
    private String mailServerPassword;
    
    @Value("${spring.mail.properties.mail.smtp.auth}")
    private String mailServerAuth;
    
    @Value("${spring.mail.properties.mail.from}")
    private String mailFrom;
    
    @Value("${spring.mail.properties.mail.smtp.ssl.enable}")
    private String mailServerSsl;
    
    @Value("${spring.mail.protocol}")
    private String mailProtocol;
    
    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(mailServerHost);
        mailSender.setPort(mailServerPort);
        mailSender.setUsername(mailServerUsername);
        mailSender.setPassword(mailServerPassword);
        mailSender.setProtocol(mailProtocol);
        
        Properties properties = mailSender.getJavaMailProperties();
        properties.put("mail.from", mailFrom);
        properties.put("mail.smtp.auth", mailServerAuth);
        properties.put("mail.smtp.ssl.enable", mailServerSsl);
        return mailSender;
    }
}