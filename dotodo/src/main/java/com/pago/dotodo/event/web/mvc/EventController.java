package com.pago.dotodo.event.web.mvc;

import com.pago.dotodo.common.web.constraint.CommonAttribute;
import com.pago.dotodo.event.web.mvc.constraint.EventAttribute;
import com.pago.dotodo.common.security.CustomAuthUserDetails;
import com.pago.dotodo.common.web.BaseController;
import com.pago.dotodo.event.service.EventService;
import com.pago.dotodo.common.util.DateTimeUtil;
import com.pago.dotodo.common.util.ModelAndViewParser;
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
