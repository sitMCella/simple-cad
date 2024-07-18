package de.sitmcella.simplecad;

import java.util.EventObject;
import java.util.List;

public class CategoriesChangeEvent extends EventObject {

    private List<Category> categories;

    /**
     * Constructs a prototypical Event.
     *
     * @param source the object on which the Event initially occurred
     * @throws IllegalArgumentException if source is null
     */
    public CategoriesChangeEvent(Object source, List<Category> categories) {
        super(source);
        this.categories = categories;
    }

    public List<Category> getCategories() {
        return categories;
    }
}
