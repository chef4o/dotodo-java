package com.pago.dotodo.main.web.mvc;

import com.pago.dotodo.common.web.constraint.CommonAttribute;
import com.pago.dotodo.main.web.mvc.constraint.ContactAttribute;
import com.pago.dotodo.common.web.BaseController;
import com.pago.dotodo.main.model.dto.ContactFormDto;
import com.pago.dotodo.common.error.CustomErrorHandler;
import com.pago.dotodo.common.service.EmailService;
import com.pago.dotodo.common.util.ModelAndViewParser;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

/**
 * Controller responsible for handling requests to the contacts page.
 * It manages displaying the contact form and sending contact messages.
 */
@Controller
@RequestMapping("/contacts")
public class ContactsController extends BaseController {

    private final ModelAndViewParser attributeBuilder;
    private final EmailService emailService;
    private final CustomErrorHandler errorHandler;

    /**
     * Constructs the ContactsController with the required services.
     *
     * @param attributeBuilder Utility for building model attributes to pass to views.
     * @param emailService     Service responsible for handling email sending.
     */
    public ContactsController(ModelAndViewParser attributeBuilder,
                              EmailService emailService,
                              CustomErrorHandler errorHandler) {
        this.attributeBuilder = attributeBuilder;
        this.emailService = emailService;
        this.errorHandler = errorHandler;
    }

    /**
     * Handles GET requests to the contact page. Displays the form for sending contact messages.
     * Optionally shows a success message if an email has been sent.
     *
     * @param emailSent Indicates whether an email has been successfully sent (defaults to false).
     * @return ModelAndView representing the contact page view.
     */
    @GetMapping
    public ModelAndView getContacts(@RequestParam(value = ContactAttribute.EMAIL_SENT,
            required = false, defaultValue = "false") boolean emailSent) {
        return this.globalView(attributeBuilder.build(
                CommonAttribute.PAGE_NAME, ContactAttribute.LOCAL_VIEW,
                ContactAttribute.EMAIL_SENT, emailSent));
    }

    /**
     * Handles POST requests to send a contact message.
     * Validates the submitted form data and, if valid, sends an email. Otherwise, displays errors.
     *
     * @param contactFormDto The form data submitted by the user.
     * @param bindingResult  The result of validating the form data.
     * @return ModelAndView for either displaying validation errors or redirecting on success.
     */
    @PostMapping("/send")
    public ModelAndView sendContactRequest(@Valid @ModelAttribute ContactFormDto contactFormDto,
                                           BindingResult bindingResult) {
        Map<String, String> valueErrors = errorHandler.loadContactErrors(bindingResult, contactFormDto);

        if (!valueErrors.isEmpty()) {
            return this.globalView(attributeBuilder.build(
                    CommonAttribute.PAGE_NAME, ContactAttribute.LOCAL_VIEW,
                    CommonAttribute.VALUE_ERRORS, valueErrors,
                    ContactAttribute.CONTACT_FORM_DTO, contactFormDto));
        }

        emailService.sendContactMessage(contactFormDto);

        return super.redirect("/contacts?emailSent=true");
    }

    /**
     * Provides a default value for the "emailSent" attribute, which indicates if an email has been sent.
     *
     * @return false, indicating no email has been sent by default.
     */
    @ModelAttribute(ContactAttribute.EMAIL_SENT)
    public boolean emailSent() {
        return false;
    }

    /**
     * Provides a new instance of ContactFormDto to be used in the contact form.
     *
     * @return A new ContactFormDto instance.
     */
    @ModelAttribute(ContactAttribute.CONTACT_FORM_DTO)
    public ContactFormDto contactFormDto() {
        return new ContactFormDto();
    }
}
