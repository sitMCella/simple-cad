package de.sitmcella.simplecad.drawer;

import de.sitmcella.simplecad.CadCanvas;
import de.sitmcella.simplecad.operation.OperationAction;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;

public class Shape implements PropertiesListener {

    protected final CadCanvas cadCanvas;

    protected final List<ShapeDrawerListener> shapeDrawerListeners;

    protected final DrawActions drawAction;

    protected final ToggleButton button;

    protected DrawerProperties drawerProperties;

    public Shape(
            final CadCanvas cadCanvas,
            final ShapeDrawerListener shapeDrawerListener,
            final ButtonConfiguration buttonConfiguration) {
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
}
