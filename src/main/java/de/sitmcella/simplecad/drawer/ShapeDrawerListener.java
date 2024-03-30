package de.sitmcella.simplecad.drawer;

import java.util.EventListener;

public interface ShapeDrawerListener extends EventListener {

    void shapeDrawerChanged(ShapeDrawerChangeEvent shapeDrawerChangeEvent);

    void shapeDrawer(ShapeDrawerEvent shapeDrawerEvent);
}
