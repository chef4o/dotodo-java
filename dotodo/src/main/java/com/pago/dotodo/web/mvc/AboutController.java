package com.pago.dotodo.web.mvc;

import com.pago.dotodo.configuration.constraint.modelAttribute.AboutAttribute;
import com.pago.dotodo.configuration.constraint.modelAttribute.CommonAttribute;
import com.pago.dotodo.util.ModelAndViewParser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/about")
public class AboutController extends BaseController {
    private final ModelAndViewParser attributeBuilder;

    public AboutController(ModelAndViewParser attributeBuilder) {
        this.attributeBuilder = attributeBuilder;
    }

    @GetMapping
    public ModelAndView getAboutUs() {
        return this.globalView(attributeBuilder.build(
                CommonAttribute.PAGE_NAME, AboutAttribute.LOCAL_VIEW
        ));
    }
}
