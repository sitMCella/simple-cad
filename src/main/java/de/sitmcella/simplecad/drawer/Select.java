package de.sitmcella.simplecad.drawer;

import de.sitmcella.simplecad.CadCanvas;
import java.util.Collections;
import javafx.scene.input.MouseEvent;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign2.MaterialDesignA;

public class Select extends Shape implements ShapeDrawer, PropertiesListener {

    private static final DrawActions DRAW_ACTION = DrawActions.SELECT;
    private static final FontIcon ICON = new FontIcon(MaterialDesignA.ARROW_TOP_RIGHT);

    public Select(final CadCanvas cadCanvas, final ShapeDrawerListener shapeDrawerListener) {
        super(
                cadCanvas,
                shapeDrawerListener,
                new ButtonConfiguration(DRAW_ACTION, ICON, "select-button"));
    }

    public Shapes handleMouseClick(MouseEvent event) {
        shapeDrawerListeners.forEach(
                shapeDrawerListener ->
                        shapeDrawerListener.shapeDrawer(new ShapeDrawerEvent(event)));
        return new Shapes(Collections.emptyList(), null);
    }

    public void handleMouseMove(MouseEvent event) {}

    public Shapes copy(javafx.scene.shape.Shape selectedShape) {
        return new Shapes(Collections.emptyList(), selectedShape);
    }
}
