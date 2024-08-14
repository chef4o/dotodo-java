package com.pago.dotodo.web.mvc;

import com.pago.dotodo.service.LayoutService;
import com.pago.dotodo.util.ModelAndViewParser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/")
public class HomeController extends BaseController {
    private static final String PAGE_NAME = "home";
    private final LayoutService layoutService;
    private final ModelAndViewParser attributeBuilder;

    public HomeController(LayoutService layoutService, ModelAndViewParser attributeBuilder) {
        this.layoutService = layoutService;
        this.attributeBuilder = attributeBuilder;
    }

    @GetMapping
    public ModelAndView getHome() {
        return this.view("index", attributeBuilder.build(
                "pageName", PAGE_NAME,
                "tasks", layoutService.getHomeItems()
        ));
    }
}
