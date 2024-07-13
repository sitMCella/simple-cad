package de.sitmcella.simplecad.property;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.Tab;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;

public class CadProperties {

    private PropertiesUtility propertiesUtility;

    private final List<CanvasPropertyListener> canvasPropertyListeners;

    private final CanvasProperties canvasProperties;

    private final LineShapeProperties lineShapeProperties;

    public CadProperties(final Tab propertiesTab, final CanvasSizeProperty canvasSizeProperty) {
        this.canvasPropertyListeners = new ArrayList<>();
        this.propertiesUtility = new PropertiesUtility();
        this.canvasProperties =
                new CanvasProperties(
                        propertiesTab,
                        canvasSizeProperty,
                        canvasPropertyListeners,
                        propertiesUtility);
        this.lineShapeProperties =
                new LineShapeProperties(propertiesTab, canvasPropertyListeners, propertiesUtility);
        addConfiguration(ShapeType.CANVAS, null);
    }

    public void addListener(final CanvasPropertyListener canvasPropertyListener) {
        this.canvasPropertyListeners.add(canvasPropertyListener);
    }

    public void addConfiguration(ShapeType shapeType, Shape shape) {
        switch (shapeType) {
            case CANVAS -> {
                canvasProperties.showCanvasProperties();
            }
            case LINE -> {
                lineShapeProperties.showLineShapeProperties((Line) shape);
            }
        }
    }

    public CanvasSizeProperty getCanvasSizeProperty() {
        return this.canvasProperties.getCanvasSizeProperty();
    }
}
