package com.pago.dotodo.common.configuration;

import com.pago.dotodo.common.web.constraint.CommonAttribute;
import com.pago.dotodo.main.web.mvc.constraint.NavAttribute;
import com.pago.dotodo.main.model.elements.MenuItem;
import com.pago.dotodo.main.service.LayoutService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@ControllerAdvice
public class GlobalModelAttributes {

    private final LayoutService layoutService;

    @Autowired
    public GlobalModelAttributes(LayoutService layoutService) {
        this.layoutService = layoutService;
    }

    @ModelAttribute(NavAttribute.TOPBAR_ITEMS)
    public List<MenuItem> populateTopbarNavItems() {
        return layoutService.getTopbarNavItems();
    }

    @ModelAttribute(NavAttribute.SIDEBAR_ITEMS)
    public List<MenuItem> populateSidebarNavItems() {
        return layoutService.getSidebarNavItems();
    }

    @ModelAttribute(NavAttribute.BOTTOMBAR_ITEMS)
    public List<MenuItem> populateBottombarNavItems() {
        return layoutService.getBottombarNavItems();
    }

    @ModelAttribute(NavAttribute.CONNECT_ITEMS)
    public List<MenuItem> populateConnectNavItems() {
        return layoutService.getConnectNavItems();
    }

    @ModelAttribute(CommonAttribute.BACKGROUND_PAGE)
    public String populateBackgroundPage(HttpServletRequest request) {
        layoutService.setBackgroundPage(request);
        return layoutService.getBackgroundPage();
    }
}
