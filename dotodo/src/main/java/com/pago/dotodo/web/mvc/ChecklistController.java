package com.pago.dotodo.web.mvc;

import com.pago.dotodo.configuration.constraint.modelAttribute.CommonAttribute;
import com.pago.dotodo.util.ModelAndViewParser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/checklists")
public class ChecklistController extends BaseController {
    private static final String PAGE_NAME = "checklists";
    private final ModelAndViewParser attributeBuilder;

    public ChecklistController(ModelAndViewParser attributeBuilder) {
        this.attributeBuilder = attributeBuilder;
    }

    @GetMapping
    public ModelAndView getChecklists() {
        return this.globalView(attributeBuilder.build(
                CommonAttribute.PAGE_NAME, PAGE_NAME));
    }
}
