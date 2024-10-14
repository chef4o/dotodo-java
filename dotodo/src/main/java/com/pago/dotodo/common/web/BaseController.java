package com.pago.dotodo.common.web;

import com.pago.dotodo.common.web.constraint.CommonAttribute;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

/**
 * Abstract base controller that provides utility methods for rendering views and performing redirects.
 * This class is designed to reduce code duplication by offering methods for creating {@link ModelAndView} objects
 * with or without additional attributes and handling both views and redirects.
 */
public abstract class BaseController {
    private static final String REDIRECT = "redirect:";

    /**
     * Renders a specific view without passing any additional attributes to the model.
     *
     * @param viewName The name of the view to be rendered.
     * @return A {@link ModelAndView} object configured for the specified view.
     */
    protected ModelAndView view(String viewName) {
        return this.localView(viewName, new HashMap<>());
    }

    /**
     * Renders a specific view and adds a set of attributes to the model.
     *
     * @param viewName   The name of the view to be rendered.
     * @param attributes A {@link Map} containing attribute names and values to be passed to the view.
     * @return A {@link ModelAndView} object configured for the specified view with the provided attributes.
     */
    protected ModelAndView localView(String viewName, Map<String, Object> attributes) {
        ModelAndView modelAndView = new ModelAndView(viewName);

        for (Map.Entry<String, Object> entry : attributes.entrySet()) {
            modelAndView.addObject(entry.getKey(), entry.getValue());
        }

        return modelAndView;
    }

    /**
     * Renders a global view by default, which typically refers to a common layout or template
     * shared across different pages.
     * Adds a set of attributes to the model for the global view.
     *
     * @param attributes A {@link Map} containing attribute names and values to be passed to the global view.
     * @return A {@link ModelAndView} object configured for the global view with the provided attributes.
     */
    protected ModelAndView globalView(Map<String, Object> attributes) {
        ModelAndView modelAndView = new ModelAndView(CommonAttribute.GLOBAL_VIEW);

        for (Map.Entry<String, Object> entry : attributes.entrySet()) {
            modelAndView.addObject(entry.getKey(), entry.getValue());
        }

        return modelAndView;
    }

    /**
     * Redirects to the specified URL without passing any additional attributes.
     *
     * @param url The URL to redirect to.
     * @return A {@link ModelAndView} object configured to redirect to the specified URL.
     */
    public ModelAndView redirect(String url) {
        return this.view(REDIRECT + url);
    }

    /**
     * Redirects to the specified URL and passes a set of attributes along with the redirect request.
     *
     * @param url        The URL to redirect to.
     * @param attributes A {@link Map} containing attribute names and values to be passed during the redirect.
     * @return A {@link ModelAndView} object configured to redirect to the specified URL with the provided attributes.
     */
    public ModelAndView redirect(String url, Map<String, Object> attributes) {
        ModelAndView modelAndView = new ModelAndView(REDIRECT + url);

        for (Map.Entry<String, Object> entry : attributes.entrySet()) {
            modelAndView.addObject(entry.getKey(), entry.getValue());
        }

        return modelAndView;
    }
}
