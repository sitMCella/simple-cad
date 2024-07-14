package de.sitmcella.simplecad.menu;

import java.util.EventObject;

public class MenuItemEvent extends EventObject {

    private final MenuItemValue menuItemValue;

    private final String parameter;

    /**
     * Constructs a Menu Item Event.
     *
     * @param source the object on which the Event initially occurred
     * @throws IllegalArgumentException if source is null
     */
    public MenuItemEvent(Object source, MenuItemValue menuItemValue, String parameter) {
        super(source);
        this.menuItemValue = menuItemValue;
        this.parameter = parameter;
    }

    public MenuItemValue getMenuItemValue() {
        return this.menuItemValue;
    }

    public String getParameter() {
        return this.parameter;
    }
}
