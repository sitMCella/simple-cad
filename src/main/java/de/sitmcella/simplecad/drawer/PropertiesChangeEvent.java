package de.sitmcella.simplecad.drawer;

import java.util.EventObject;

public class PropertiesChangeEvent extends EventObject {

    private final DrawerProperties drawerProperties;

    /**
     * Constructs a Properties Change Event.
     *
     * @param source the object on which the Event initially occurred
     * @throws IllegalArgumentException if source is null
     */
    public PropertiesChangeEvent(Object source, final DrawerProperties drawerProperties) {
        super(source);
        this.drawerProperties = drawerProperties;
    }

    public DrawerProperties getProperties() {
        return this.drawerProperties;
    }
}
