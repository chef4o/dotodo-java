package com.pago.dotodo.main.web.mvc;

import com.pago.dotodo.common.web.constraint.CommonAttribute;
import com.pago.dotodo.main.web.mvc.constraint.HomeAttribute;
import com.pago.dotodo.common.web.BaseController;
import com.pago.dotodo.main.service.LayoutService;
import com.pago.dotodo.common.util.ModelAndViewParser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller responsible for handling requests to the home page.
 * It retrieves the necessary layout items and tasks to display on the home page.
 */
@Controller
@RequestMapping("/")
public class HomeController extends BaseController {

    private final LayoutService layoutService;
    private final ModelAndViewParser attributeBuilder;

    /**
     * Constructs a HomeController with the specified LayoutService and ModelAndViewParser.
     *
     * @param layoutService    The service providing layout items for the home page.
     * @param attributeBuilder Utility for building model attributes to be passed to the view.
     */
    public HomeController(LayoutService layoutService, ModelAndViewParser attributeBuilder) {
        this.layoutService = layoutService;
        this.attributeBuilder = attributeBuilder;
    }

    /**
     * Handles GET requests to the home page.
     * It populates the model with the page name and tasks from the LayoutService
     * and returns the view for the home page.
     *
     * @return ModelAndView representing the home page view with the appropriate model attributes.
     */
    @GetMapping
    public ModelAndView getHome() {
        return this.globalView(attributeBuilder.build(
                CommonAttribute.PAGE_NAME, HomeAttribute.LOCAL_VIEW,
                HomeAttribute.TASKS, layoutService.getHomeItems()
        ));
    }
}
