package com.pago.dotodo.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.HashMap;
import java.util.Map;

@Service
public class EmailService {

    private final String SUPPORT_EMAIL = "support@dotodo.com";
    private final String TEMPL_FOLDER = "email/";

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    private final JavaMailSender javaMailSender;
    private final UserService userService;
    private final JavaMailSender emailSender;

    public EmailService(JavaMailSender mailSender, TemplateEngine templateEngine, JavaMailSender javaMailSender, UserService userService, JavaMailSender emailSender) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
        this.javaMailSender = javaMailSender;
        this.userService = userService;
        this.emailSender = emailSender;
    }

    public void sendExpiringTodoEmail(String userEmail, String reminderType) {
        Map<String, String> params = new HashMap<>();
        params.put("username", userService.getUserByEmail(userEmail).get().getUsername());
        params.put("subject", "Expiring " + reminderType);
        params.put("reminderType", reminderType);

        generateMessage(userEmail, "expiring-todo", params);
    }

    public void sendBirthdayEmail(String userEmail) {
        Map<String, String> params = new HashMap<>();
        params.put("username", userService.getUserByEmail(userEmail).get().getUsername());
        params.put("subject", "Happy Birthday!");

        generateMessage(userEmail, "birthday", params);
    }

    private void generateMessage(String toEmail, String template, Map<String, String> params) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);

        try {
            mimeMessageHelper.setFrom(SUPPORT_EMAIL);
            mimeMessageHelper.setTo(toEmail);
            mimeMessageHelper.setSubject(params.get("subject"));
            mimeMessageHelper.setText(generateContent(template, params), true);

            javaMailSender.send(mimeMessageHelper.getMimeMessage());
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    private String generateContent(String templateName, Map<String, String> params) {
        Context ctx = new Context();

        for (Map.Entry<String, String> entry : params.entrySet()) {
            ctx.setVariable(entry.getKey(), entry.getValue());
        }

        return templateEngine.process(TEMPL_FOLDER + templateName, ctx);
    }

    public void sendSimpleMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }
}
