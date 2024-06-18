package com.pago.dotodo.web.mvc;

import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

public abstract class BaseController {
    private static final String REDIRECT = "redirect:";

    protected ModelAndView view(String viewName) {
        return this.view(viewName, new HashMap<>());
    }

    protected ModelAndView view(String viewName, Map<String, Object> attributes) {
        ModelAndView modelAndView = new ModelAndView(viewName);

        for (Map.Entry<String, Object> entry : attributes.entrySet()) {
            modelAndView.addObject(entry.getKey(), entry.getValue());
        }

        return modelAndView;
    }

    public ModelAndView redirect(String url) {
        return this.view(REDIRECT + url);
    }

}
