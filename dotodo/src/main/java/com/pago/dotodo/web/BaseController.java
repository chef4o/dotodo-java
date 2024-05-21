package com.pago.dotodo.web;

import org.springframework.web.servlet.ModelAndView;

public abstract class BaseController {
    private static final String REDIRECT = "redirect:";

    protected ModelAndView view(String viewName) {
        return this.view(viewName, new ModelAndView());
    }

    protected ModelAndView view(String viewName, Object... attributes) {
        ModelAndView modelAndView = new ModelAndView(viewName);

        for (int i = 0; i < attributes.length; i += 2) {
            modelAndView.addObject((String) attributes[i], attributes[i + 1]);
        }
        return modelAndView;
    }

    public ModelAndView redirect(String url) {
        return this.view(REDIRECT + url);
    }

}
