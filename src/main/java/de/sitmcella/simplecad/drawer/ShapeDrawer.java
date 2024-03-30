package de.sitmcella.simplecad.drawer;

import javafx.scene.input.MouseEvent;

public interface ShapeDrawer {

    Shapes handleMouseClick(MouseEvent event);

    void handleMouseMove(MouseEvent event);

    Shapes copy(javafx.scene.shape.Shape selectedShape);
}
