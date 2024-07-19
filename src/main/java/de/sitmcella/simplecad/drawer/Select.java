package de.sitmcella.simplecad.drawer;

import de.sitmcella.simplecad.CadCanvas;
import de.sitmcella.simplecad.CadShape;
import de.sitmcella.simplecad.category.Category;
import java.util.Collections;
import java.util.List;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign2.MaterialDesignA;

public class Select extends Shape implements ShapeDrawer, PropertiesListener {

    private static final DrawActions DRAW_ACTION = DrawActions.SELECT;
    private static final FontIcon ICON = new FontIcon(MaterialDesignA.ARROW_TOP_RIGHT);

    public Select(
            final CadCanvas cadCanvas,
            final ShapeDrawerListener shapeDrawerListener,
            final List<Category> categories) {
        super(
                cadCanvas,
                shapeDrawerListener,
                new ButtonConfiguration(DRAW_ACTION, ICON, "select-button"),
                categories);
    }

    public Shapes handleMouseClick(MouseEvent event) {
        shapeDrawerListeners.forEach(
                shapeDrawerListener ->
                        shapeDrawerListener.shapeDrawer(new ShapeDrawerEvent(event)));
        return new Shapes(Collections.emptyList(), null);
    }

    public void handleMouseMove(MouseEvent event) {}

    public Shapes copy(CadShape cadShape) {
        return new Shapes(Collections.emptyList(), cadShape.shape());
    }

    public javafx.scene.shape.Shape use(
            javafx.scene.shape.Shape selectedShape, Circle closestPoint) {
        return null;
    }
}
