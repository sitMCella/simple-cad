package de.sitmcella.simplecad.menu;

import java.util.EventObject;

public class MenuItemEvent extends EventObject {

    private final MenuItemValue menuItemValue;

    /**
     * Constructs a menu item Event.
     *
     * @param source the object on which the Event initially occurred
     * @throws IllegalArgumentException if source is null
     */
    public MenuItemEvent(Object source, MenuItemValue menuItemValue) {
        super(source);
        this.menuItemValue = menuItemValue;
    }

    public MenuItemValue getMenuItemValue() {
        return this.menuItemValue;
    }
}
