package de.sitmcella.simplecad.property;

import java.util.EventObject;

public class CanvasPropertyChangeEvent extends EventObject {

    private final CanvasProperty canvasProperty;

    /**
     * Constructs a Canvas Property Change Event.
     *
     * @param source the object on which the Event initially occurred
     * @throws IllegalArgumentException if source is null
     */
    public CanvasPropertyChangeEvent(Object source, final CanvasProperty canvasProperty) {
        super(source);
        this.canvasProperty = canvasProperty;
    }

    public CanvasProperty getCanvasProperty() {
        return this.canvasProperty;
    }
}
