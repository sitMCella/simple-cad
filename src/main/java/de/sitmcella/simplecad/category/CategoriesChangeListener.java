package de.sitmcella.simplecad.category;

import java.util.EventListener;

public interface CategoriesChangeListener extends EventListener {

    void categoriesChanged(CategoriesChangeEvent categoriesChangeEvent);
}
