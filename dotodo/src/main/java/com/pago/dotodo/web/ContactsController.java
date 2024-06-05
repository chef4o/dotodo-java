package com.pago.dotodo.web;

import com.pago.dotodo.service.SkeletonService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/contacts")
public class ContactsController extends BaseController {
    private static final String PAGE_NAME = "contacts";
    private final SkeletonService skeletonService;

    public ContactsController(SkeletonService skeletonService) {
        this.skeletonService = skeletonService;
    }

    @GetMapping
    public ModelAndView getHome() {
        return this.view("index",
                "pageName", PAGE_NAME,
                "tasks", skeletonService.getHomeItems(),
                "topbarNavItems", skeletonService.getTopbarNavItems(),
                "sidebarNavItems", skeletonService.getSidebarNavItems(),
                "bottombarNavItems", skeletonService.getBottombarNavItems(),
                "connectNavItems", skeletonService.getConnectNavItems()
        );
    }
}