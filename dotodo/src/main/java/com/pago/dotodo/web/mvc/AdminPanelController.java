package com.pago.dotodo.web.mvc;

import com.pago.dotodo.util.ModelAndViewParser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/admin-panel")
public class AdminPanelController extends BaseController {
    private static final String PAGE_NAME = "admin-panel";
    private final ModelAndViewParser attributeBuilder;

    public AdminPanelController(ModelAndViewParser attributeBuilder) {
        this.attributeBuilder = attributeBuilder;
    }

    @GetMapping
    public ModelAndView getAdminPanel() {
        return this.view("index", attributeBuilder.build(
                "pageName", PAGE_NAME
        ));
    }
}
