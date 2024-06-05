package com.pago.dotodo.web;

import com.pago.dotodo.util.ModelAndViewParser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/contacts")
public class ContactsController extends BaseController {
    private static final String PAGE_NAME = "contacts";
    private final ModelAndViewParser attributeBuilder;

    public ContactsController(ModelAndViewParser attributeBuilder) {
        this.attributeBuilder = attributeBuilder;
    }

    @GetMapping
    public ModelAndView getContacts() {
        return this.view("index", attributeBuilder.build(
                "pageName", PAGE_NAME
        ));
    }
}
