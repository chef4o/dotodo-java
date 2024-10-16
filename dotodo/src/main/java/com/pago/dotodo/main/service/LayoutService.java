package com.pago.dotodo.main.service;

import com.pago.dotodo.main.model.elements.MenuItem;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class LayoutService {

    private final List<MenuItem> topbarNavItems;
    private final List<MenuItem> sidebarNavItems;
    private final List<MenuItem> bottombarNavItems;
    private final List<MenuItem> connectNavItems;
    String backgroundPage;

    public LayoutService() {
        this.topbarNavItems = new ArrayList<>();
        this.sidebarNavItems = new ArrayList<>();
        this.bottombarNavItems = new ArrayList<>();
        this.connectNavItems = new ArrayList<>();
        this.backgroundPage = "home";
    }

    private final List<MenuItem> homeItems = new ArrayList<>();

    @PostConstruct
    private void init() {
        initializeTopbarNavItems();
        initializeSidebarNavItems();
        initializeBottombarNavItems();
        initializeConnectNavItems();
        initializeHomeItems();
    }

    public List<MenuItem> getTopbarNavItems() {
        return topbarNavItems;
    }

    public List<MenuItem> getSidebarNavItems() {
        return sidebarNavItems;
    }

    public List<MenuItem> getBottombarNavItems() {
        return bottombarNavItems;
    }

    public List<MenuItem> getConnectNavItems() {
        return connectNavItems;
    }

    public List<MenuItem> getHomeItems() {
        return homeItems;
    }

    public ArrayList<String> getMenuNames(List<MenuItem> menuItems) {
        return menuItems.stream()
                .map(MenuItem::getName)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private void initializeHomeItems() {
        addItems(homeItems,
                new MenuItem()
                        .add("name", "Notes")
                        .add("href", "/notes")
                        .add("headerText", "Basic text notes")
                        .add("description", "Create quick notes/memos by typing-in what's on your mind."),
                new MenuItem()
                        .add("name", "Checklists")
                        .add("href", "/checklists")
                        .add("headerText", "List of to-do items")
                        .add("description", "Create a checklist of items or tasks you would like to track."),
                new MenuItem()
                        .add("name", "Events")
                        .add("href", "/events")
                        .add("headerText", "Calendar reminders")
                        .add("description", "Create event reminders such as birthdate alerts, social engagements, etc.")
        );
    }

    private void initializeTopbarNavItems() {
        addItems(topbarNavItems,
                new MenuItem()
                        .add("name", "administration")
                        .add("href", "/admin-panel"),
                new MenuItem()
                        .add("name", "login")
                        .add("href", "/auth/login"),
                new MenuItem()
                        .add("name", "register")
                        .add("href", "/auth/register"),
                new MenuItem()
                        .add("name", "logout")
                        .add("href", "/auth/logout")
        );
    }

    private void initializeSidebarNavItems() {
        addItems(sidebarNavItems,
                new MenuItem()
                        .add("name", "home")
                        .add("href", "/")
                        .add("icon", "fa-solid fa-house"),
                new MenuItem()
                        .add("name", "notes")
                        .add("href", "/notes")
                        .add("icon", "fa-solid fa-note-sticky"),
                new MenuItem()
                        .add("name", "checklists")
                        .add("href", "/checklists")
                        .add("icon", "fa-solid fa-list-check"),
                new MenuItem()
                        .add("name", "events")
                        .add("href", "/events")
                        .add("icon", "fa-regular fa-calendar-check"),
                new MenuItem()
                        .add("name", "profile")
                        .add("href", "/profile")
                        .add("icon", "fa-solid fa-user"));
    }

    private void initializeBottombarNavItems() {
        addItems(bottombarNavItems,
                new MenuItem()
                        .add("name", "about")
                        .add("href", "/about"),
                new MenuItem()
                        .add("name", "contacts")
                        .add("href", "/contacts"),
                new MenuItem()
                        .add("name", "news")
                        .add("href", "/news"));
    }

    private void initializeConnectNavItems() {
        addItems(connectNavItems,
                new MenuItem()
                        .add("name", "github")
                        .add("href", "https://github.com/chef4o")
                        .add("icon", "fa-brands fa-square-github"),
                new MenuItem()
                        .add("name", "lnkdin")
                        .add("href", "https://www.linkedin.com/in/sihristov")
                        .add("icon", "fa-brands fa-linkedin"),
                new MenuItem()
                        .add("name", "fb")
                        .add("href", "https://www.facebook.com/chef4o")
                        .add("icon", "fa-brands fa-square-facebook"),
                new MenuItem()
                        .add("name", "email")
                        .add("href", "mailto: sihristov@hotmail.com")
                        .add("icon", "fa-solid fa-square-envelope"));
    }

    private void addItems(List<MenuItem> list, MenuItem... items) {
        for (MenuItem item : items) {
            addItem(list, item);
        }
    }

    private void addItem(List<MenuItem> list, MenuItem item) {
        list.add(item);
    }

    private List<String> getItemsByType(List<MenuItem> items, String type) {
        return items.stream().map(e -> e.get(type)).toList();
    }

    public List<String> getBackgroundMenuPages() {
        return Stream
                .concat(getMenuNames(getSidebarNavItems()).stream(),
                        getMenuNames(getBottombarNavItems()).stream())
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public String getBackgroundPage() {
        return backgroundPage;
    }

    public void setBackgroundPage(HttpServletRequest request) {
        String lastPage = request.getHeader("Referer") != null
                ? request.getHeader("Referer")
                .substring(request.getHeader("Referer").lastIndexOf("/") + 1)
                : "";

        if (lastPage.isEmpty()) {
            this.backgroundPage = "home";
            return;
        }

        if (getBackgroundMenuPages().contains(lastPage)) {
            this.backgroundPage = lastPage;
        }
    }
}
