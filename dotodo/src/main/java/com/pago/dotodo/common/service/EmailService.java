package com.pago.dotodo.common.service;

import com.pago.dotodo.main.model.dto.ContactFormDto;
import com.pago.dotodo.user.service.UserService;
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

    private static final String SUPPORT_EMAIL = "support@dotodo.com";
    private static final String TEMPL_FOLDER = "email/";

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    private final UserService userService;

    public EmailService(JavaMailSender mailSender, TemplateEngine templateEngine, UserService userService) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
        this.userService = userService;
    }

    public void sendExpiringTodoEmail(String userEmail, String reminderType) {
        userService.getUserByEmail(userEmail).ifPresent(user -> {
            Map<String, String> params = new HashMap<>();
            params.put("username", user.getUsername());
            params.put("subject", "Expiring " + reminderType);
            params.put("reminderType", reminderType);

            generateMessage(userEmail, "expiring-todo", params);
        });
    }

    public void sendBirthdayEmail(String userEmail) {
        userService.getUserByEmail(userEmail).ifPresent(user -> {
            Map<String, String> params = new HashMap<>();
            params.put("username", user.getUsername());
            params.put("subject", "Happy Birthday!");

            generateMessage(userEmail, "birthday", params);
        });
    }

    public void sendContactMessage(ContactFormDto sender) {
        Map<String, String> params = new HashMap<>();

        params.put("content", sender.getContent());
        params.put("subject", "Incoming contact");

        if (sender.getName() != null) {
            params.put("name", sender.getName());
        }

        if (sender.getEmail() != null) {
            params.put("email", sender.getEmail());
        }

        if (sender.getPhone() != null) {
            params.put("phone", sender.getPhone());
        }

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);

        try {
            mimeMessageHelper.setFrom(sender.getEmail() != null ? sender.getEmail() : "unknown_sender@dotodo.com");
            mimeMessageHelper.setTo(SUPPORT_EMAIL);
            mimeMessageHelper.setSubject(params.get("subject"));
            mimeMessageHelper.setText(generateContent("contact", params), true);

            mailSender.send(mimeMessageHelper.getMimeMessage());
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    private void generateMessage(String toEmail, String template, Map<String, String> params) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);

        try {
            mimeMessageHelper.setFrom(SUPPORT_EMAIL);
            mimeMessageHelper.setTo(toEmail);
            mimeMessageHelper.setSubject(params.get("subject"));
            mimeMessageHelper.setText(generateContent(template, params), true);

            mailSender.send(mimeMessageHelper.getMimeMessage());
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    private String generateContent(String templateName, Map<String, String> params) {
        Context ctx = new Context();
        params.forEach(ctx::setVariable);
        return templateEngine.process(TEMPL_FOLDER + templateName, ctx);
    }

    public void sendSimpleMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }
}
