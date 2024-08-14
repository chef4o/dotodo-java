package com.pago.dotodo.web.mvc;

import com.pago.dotodo.security.CustomAuthUserDetails;
import com.pago.dotodo.service.EventService;
import com.pago.dotodo.util.DateTimeUtil;
import com.pago.dotodo.util.ModelAndViewParser;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/events")
public class EventController extends BaseController {

    private static final String PAGE_NAME = "events";
    private final EventService eventService;
    private final ModelAndViewParser attributeBuilder;
    private final DateTimeUtil dateTimeUtil;

    public EventController(EventService eventService, ModelAndViewParser attributeBuilder, DateTimeUtil dateTimeUtil) {
        this.eventService = eventService;
        this.attributeBuilder = attributeBuilder;
        this.dateTimeUtil = dateTimeUtil;
    }

    @GetMapping
    public ModelAndView getEvents(@AuthenticationPrincipal CustomAuthUserDetails userDetails) {
        return this.view("index", attributeBuilder.build(
                "pageName", PAGE_NAME,
                "events", eventService.getAll(userDetails.getId()))
        );
    }
}
