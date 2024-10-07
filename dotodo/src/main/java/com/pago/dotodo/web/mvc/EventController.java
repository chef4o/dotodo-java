package com.pago.dotodo.web.mvc;

import com.pago.dotodo.configuration.constraint.modelAttribute.CommonAttribute;
import com.pago.dotodo.configuration.constraint.modelAttribute.EventAttribute;
import com.pago.dotodo.security.CustomAuthUserDetails;
import com.pago.dotodo.service.EventService;
import com.pago.dotodo.util.DateTimeUtil;
import com.pago.dotodo.util.ModelAndViewParser;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;

@Controller
@RequestMapping("/events")
public class EventController extends BaseController {

    private final EventService eventService;
    private final ModelAndViewParser attributeBuilder;

    public EventController(EventService eventService, ModelAndViewParser attributeBuilder, DateTimeUtil dateTimeUtil) {
        this.eventService = eventService;
        this.attributeBuilder = attributeBuilder;
    }

    @GetMapping
    public ModelAndView getEvents(@AuthenticationPrincipal CustomAuthUserDetails userDetails) {
        return this.globalView(attributeBuilder.build(
                CommonAttribute.PAGE_NAME, EventAttribute.LOCAL_VIEW,
                EventAttribute.ALL_EVENTS, eventService.getAll(userDetails.getId()),
                CommonAttribute.CURRENT_DATE_FIELD, new Date())
        );
    }
}
