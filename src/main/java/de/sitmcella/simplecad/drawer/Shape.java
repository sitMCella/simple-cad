package de.sitmcella.simplecad.drawer;

import de.sitmcella.simplecad.CadCanvas;
import de.sitmcella.simplecad.CadShape;
import de.sitmcella.simplecad.CanvasPoint;
import de.sitmcella.simplecad.Category;
import de.sitmcella.simplecad.operation.OperationAction;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class Shape implements PropertiesListener {

    protected final CadCanvas cadCanvas;

    protected final List<ShapeDrawerListener> shapeDrawerListeners;

    protected final DrawActions drawAction;

    protected final ToggleButton button;

    protected DrawerProperties drawerProperties;

    public List<Category> categories;

    public Shape(
            final CadCanvas cadCanvas,
            final ShapeDrawerListener shapeDrawerListener,
            final ButtonConfiguration buttonConfiguration,
            List<Category> categories) {
        this.cadCanvas = cadCanvas;
        this.shapeDrawerListeners =
                new ArrayList<>() {
                    {
                        add(shapeDrawerListener);
                    }
                };
        this.drawAction = buttonConfiguration.drawAction();
        ToggleButton button = new ToggleButton();
        button.setGraphic(buttonConfiguration.fontIcon());
        button.setId(buttonConfiguration.buttonId());
        button.setOnMouseClicked(this::handleButtonClick);
        this.button = button;
        this.drawerProperties =
                new DrawerProperties(DrawActions.SELECT, OperationAction.NULL, false);
        this.categories = categories;
    }

    @Override
    public void propertyChanged(PropertiesChangeEvent propertiesChangeEvent) {
        this.drawerProperties = propertiesChangeEvent.getProperties();
    }

    public DrawActions getDrawAction() {
        return drawAction;
    }

    public ToggleButton getButton() {
        return this.button;
    }

    private void handleButtonClick(MouseEvent mouseEvent) {
        shapeDrawerListeners.forEach(
                shapeDrawerListener ->
                        shapeDrawerListener.shapeDrawerChanged(
                                new ShapeDrawerChangeEvent(mouseEvent, this.drawAction)));
    }

    protected boolean categoryExists(Category category) {
        return this.categories.contains(category);
    }

    public void setCategories(final List<Category> categories) {
        this.categories = categories;
    }

    public void filter(CadShape cadShape, Category category) {
        if (category == null) {
            cadShape.shape().setStroke(Color.BLACK);
            return;
        }
        if (!cadShape.category().equals(category)) {
            cadShape.shape().setStroke(Color.gray(0.7));
        } else {
            cadShape.shape().setStroke(Color.BLACK);
        }
    }

    public void filter(CanvasPoint canvasPoint, CadShape mainCadShape, Category category) {
        if (category == null) {
            canvasPoint.circle().setStroke(Color.BLACK);
            return;
        }
        if (!mainCadShape.category().equals(category)) {
            canvasPoint.circle().setStroke(Color.gray(0.7));
        } else {
            canvasPoint.circle().setStroke(Color.BLACK);
        }
    }

    public boolean isActive(CadShape cadShape) {
        return cadShape.shape().getStroke().equals(Color.BLACK);
    }
}
