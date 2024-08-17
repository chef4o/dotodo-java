package com.pago.dotodo.service;

import com.pago.dotodo.model.dto.ContactFormDto;
import org.springframework.stereotype.Service;

@Service
public class ContactUsService {

    private final EmailService emailService;

    public ContactUsService(EmailService emailService) {
        this.emailService = emailService;
    }

    public boolean sendMessage(ContactFormDto contactFormDto) {
        emailService.sendContactMessage(contactFormDto);
        return true;
    }
}
