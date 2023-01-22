package com.example.application.views.shared;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.TabsVariant;

public class MainTab extends HorizontalLayout {
    public MainTab(String currentLocation) {
        Tab usersTab = new Tab("Users");
        usersTab.getElement().addEventListener("click", domEvent -> {
            UI.getCurrent().navigate("users");
        });

        Tab filmsTab = new Tab("Films");
        filmsTab.getElement().addEventListener("click", domEvent -> {
            UI.getCurrent().navigate("films");
        });

        Tab calendarTab = new Tab("Calendar");
        calendarTab.getElement().addEventListener("click", domEvent -> {
            UI.getCurrent().navigate("calendar");
        });

        Tab ticketsTab = new Tab("Buy ticket");
        ticketsTab.getElement().addEventListener("click", domEvent -> {
            UI.getCurrent().navigate("tickets/buy");
        });

        Tabs tabs = new Tabs(usersTab, filmsTab, calendarTab, ticketsTab);
        tabs.addThemeVariants(TabsVariant.LUMO_EQUAL_WIDTH_TABS);

        switch(currentLocation) {
            case "films": tabs.setSelectedTab(filmsTab); break;
            case "users": tabs.setSelectedTab(usersTab); break;
            case "calendar": tabs.setSelectedTab(calendarTab); break;
            case "tickets/buy": tabs.setSelectedTab(ticketsTab); break;
        }

        add(tabs);
    }
}
