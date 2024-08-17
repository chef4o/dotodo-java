package com.pago.dotodo.web.mvc;

import com.pago.dotodo.model.dto.ContactFormDto;
import com.pago.dotodo.service.EmailService;
import com.pago.dotodo.util.ModelAndViewParser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/contacts")
public class ContactsController extends BaseController {
    private static final String PAGE_NAME = "contacts";
    private final ModelAndViewParser attributeBuilder;
    private final EmailService emailService;

    public ContactsController(ModelAndViewParser attributeBuilder, EmailService emailService) {
        this.attributeBuilder = attributeBuilder;
        this.emailService = emailService;
    }

    @GetMapping
    public ModelAndView getContacts(@RequestParam(value = "emailSent", required = false, defaultValue = "false") boolean emailSent) {
        return this.view("index", attributeBuilder.build(
                "pageName", PAGE_NAME,
                "emailSent", emailSent
        ));
    }

    @PostMapping("/send")
    public ModelAndView sendContactRequest(@ModelAttribute ContactFormDto contactFormDto) {
        final String valueError;

        if (contactFormDto.getEmail().isBlank() && contactFormDto.getPhone().isBlank()) {
            valueError = "We need either phone or email to be able to contact you back";
        } else if (contactFormDto.getContent().isBlank()) {
            valueError = "Content cannot be empty";
        } else {
            valueError = "";
        }

        if (!valueError.isBlank()) {
            return this.view("index", attributeBuilder.build(
                    "pageName", PAGE_NAME,
                    "valueError", valueError,
                    "contactFormDto", contactFormDto));
        }

        emailService.sendContactMessage(contactFormDto);

        return super.redirect("/contacts?emailSent=true");
    }

    @ModelAttribute("emailSent")
    public boolean emailSent() {
        return false;
    }

    @ModelAttribute("contactFormDto")
    public ContactFormDto contactFormDto() {
        return new ContactFormDto();
    }

}
