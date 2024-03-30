package de.sitmcella.simplecad.drawer;

import java.util.EventObject;

public class ShapeDrawerChangeEvent extends EventObject {

    private final DrawActions drawAction;

    /**
     * Constructs a prototypical Event.
     *
     * @param source the object on which the Event initially occurred
     * @throws IllegalArgumentException if source is null
     */
    public ShapeDrawerChangeEvent(Object source, final DrawActions drawAction) {
        super(source);
        this.drawAction = drawAction;
    }

    public DrawActions getDrawAction() {
        return this.drawAction;
    }
}
