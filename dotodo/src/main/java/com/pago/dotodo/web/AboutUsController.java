package com.pago.dotodo.web;

import com.pago.dotodo.service.LayoutService;
import com.pago.dotodo.util.ModelAndViewParser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/about-us")
public class AboutUsController extends BaseController {
    private static final String PAGE_NAME = "about-us";
    private final ModelAndViewParser attributeBuilder;

    public AboutUsController(ModelAndViewParser attributeBuilder) {
        this.attributeBuilder = attributeBuilder;
    }

    @GetMapping
    public ModelAndView getAboutUs() {
        return this.view("index", attributeBuilder.build(
                "pageName", PAGE_NAME
        ));
    }
}
