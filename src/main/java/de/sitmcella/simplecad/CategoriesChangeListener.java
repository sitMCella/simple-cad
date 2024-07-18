package de.sitmcella.simplecad;

import java.util.EventListener;

public interface CategoriesChangeListener extends EventListener {

    void categoriesChanged(CategoriesChangeEvent categoriesChangeEvent);
}
