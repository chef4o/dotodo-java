package com.pago.dotodo.main.service;

import com.pago.dotodo.common.service.EmailService;
import com.pago.dotodo.main.model.dto.ContactFormDto;
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
