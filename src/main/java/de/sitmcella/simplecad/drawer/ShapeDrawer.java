package de.sitmcella.simplecad.drawer;

import de.sitmcella.simplecad.CadShape;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;

public interface ShapeDrawer {

    Shapes handleMouseClick(MouseEvent event);

    void handleMouseMove(MouseEvent event);

    Shapes copy(CadShape cadShape);

    javafx.scene.shape.Shape use(javafx.scene.shape.Shape selectedShape, Circle closestPoint);
}
