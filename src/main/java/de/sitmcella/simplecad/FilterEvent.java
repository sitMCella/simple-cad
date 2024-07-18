package de.sitmcella.simplecad;

import java.util.EventObject;

public class FilterEvent extends EventObject {

    private final Category category;

    /**
     * Constructs a prototypical Event.
     *
     * @param source the object on which the Event initially occurred
     * @throws IllegalArgumentException if source is null
     */
    public FilterEvent(Object source, Category category) {
        super(source);
        this.category = category;
    }

    public Category getCategory() {
        return category;
    }
}
