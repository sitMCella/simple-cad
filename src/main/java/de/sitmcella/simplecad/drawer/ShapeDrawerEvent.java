package de.sitmcella.simplecad.drawer;

import java.util.EventObject;

public class ShapeDrawerEvent extends EventObject {

    /**
     * Constructs a Shape Drawer Event.
     *
     * @param source the object on which the Event initially occurred
     * @throws IllegalArgumentException if source is null
     */
    public ShapeDrawerEvent(Object source) {
        super(source);
    }
}
