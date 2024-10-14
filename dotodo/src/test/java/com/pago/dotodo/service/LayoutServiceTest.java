package com.pago.dotodo.service;

import com.pago.dotodo.main.model.elements.MenuItem;
import com.pago.dotodo.main.service.LayoutService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class LayoutServiceTest {

    private LayoutService layoutService;

    @BeforeEach
    void setUp() throws Exception {
        layoutService = new LayoutService();
        Method initMethod = LayoutService.class.getDeclaredMethod("init");
        initMethod.setAccessible(true);
        initMethod.invoke(layoutService);
    }

    @Test
    void testGetTopbarNavItems() {
        List<MenuItem> topbarItems = layoutService.getTopbarNavItems();
        assertFalse(topbarItems.isEmpty(), "Topbar nav items should not be empty");
        assertEquals(4, topbarItems.size(), "There should be 4 topbar nav items");
        assertEquals("administration", topbarItems.get(0).getName(), "First topbar item should be 'administration'");
    }

    @Test
    void testGetSidebarNavItems() {
        List<MenuItem> sidebarItems = layoutService.getSidebarNavItems();
        assertFalse(sidebarItems.isEmpty(), "Sidebar nav items should not be empty");
        assertEquals(5, sidebarItems.size(), "There should be 5 sidebar nav items");
        assertEquals("home", sidebarItems.get(0).getName(), "First sidebar item should be 'home'");
    }

    @Test
    void testGetBottombarNavItems() {
        List<MenuItem> bottombarItems = layoutService.getBottombarNavItems();
        assertFalse(bottombarItems.isEmpty(), "Bottombar nav items should not be empty");
        assertEquals(3, bottombarItems.size(), "There should be 3 bottombar nav items");
        assertEquals("about", bottombarItems.get(0).getName(), "First bottombar item should be 'about'");
    }

    @Test
    void testGetConnectNavItems() {
        List<MenuItem> connectItems = layoutService.getConnectNavItems();
        assertFalse(connectItems.isEmpty(), "Connect nav items should not be empty");
        assertEquals(4, connectItems.size(), "There should be 4 connect nav items");
        assertEquals("github", connectItems.get(0).getName(), "First connect item should be 'github'");
    }

    @Test
    void testGetHomeItems() {
        List<MenuItem> homeItems = layoutService.getHomeItems();
        assertFalse(homeItems.isEmpty(), "Home items should not be empty");
        assertEquals(3, homeItems.size(), "There should be 3 home items");
        assertEquals("Notes", homeItems.get(0).getName(), "First home item should be 'Notes'");
    }
}
