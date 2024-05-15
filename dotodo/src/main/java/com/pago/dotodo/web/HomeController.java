package com.pago.dotodo.web;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/")
public class HomeController extends BaseController {

    public HomeController() {
    }

    @GetMapping
    public ModelAndView getHome(ModelAndView modelAndView) {
        modelAndView.addObject("pageName", "home");
        return super.view("index");
    }
}
