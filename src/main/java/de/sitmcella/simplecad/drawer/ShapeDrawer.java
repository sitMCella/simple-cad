package de.sitmcella.simplecad.drawer;

import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;

public interface ShapeDrawer {

    Shapes handleMouseClick(MouseEvent event);

    void handleMouseMove(MouseEvent event);

    Shapes copy(javafx.scene.shape.Shape selectedShape);

    javafx.scene.shape.Shape use(javafx.scene.shape.Shape selectedShape, Circle closestPoint);
}
