package de.sitmcella.simplecad.drawer;

import java.util.EventListener;

public interface PropertiesListener extends EventListener {

    void propertyChanged(PropertiesChangeEvent propertiesChangeEvent);
}
