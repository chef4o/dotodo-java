package com.pago.dotodo.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfig {

    @Bean
    public JavaMailSender javaMailSender(@Value("${mail.host}") String mailHost,
                                         @Value("${mail.port}") Integer mailPort,
                                         @Value("${mail.username}") String mailUsername,
                                         @Value("${mail.password}") String mailPassword) {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(mailHost);
        mailSender.setPort(mailPort);
        mailSender.setUsername(mailUsername);
        mailSender.setPassword(mailPassword);
        mailSender.setDefaultEncoding("UTF-8");
        mailSender.setJavaMailProperties(mailProperties());

        return mailSender;
    }

    private Properties mailProperties() {
        Properties properties = new Properties();

        properties.put("mail.smtp.auth", "true");
        properties.put("mail.transport.protocol", "smtp");
        
        return properties;
    }
}
