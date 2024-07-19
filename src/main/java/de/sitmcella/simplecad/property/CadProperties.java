package de.sitmcella.simplecad.property;

import de.sitmcella.simplecad.CadShape;
import de.sitmcella.simplecad.category.Category;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.Tab;

public class CadProperties {

    private PropertiesUtility propertiesUtility;

    private final List<CanvasPropertyListener> canvasPropertyListeners;

    private final CanvasProperties canvasProperties;

    private final LineShapeProperties lineShapeProperties;

    private final CurveShapeProperties curveShapeProperties;

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
                new LineShapeProperties(
                        propertiesTab,
                        canvasPropertyListeners,
                        propertiesUtility,
                        new ArrayList<>());
        this.curveShapeProperties =
                new CurveShapeProperties(
                        propertiesTab,
                        canvasPropertyListeners,
                        propertiesUtility,
                        new ArrayList<>());
        addConfiguration(ShapeType.CANVAS, null);
    }

    public void addListener(final CanvasPropertyListener canvasPropertyListener) {
        this.canvasPropertyListeners.add(canvasPropertyListener);
    }

    public void addConfiguration(ShapeType shapeType, CadShape cadShape) {
        switch (shapeType) {
            case CANVAS -> {
                canvasProperties.showCanvasProperties();
            }
            case LINE -> {
                lineShapeProperties.showLineShapeProperties(cadShape);
            }
            case CURVE -> {
                curveShapeProperties.showCurveShapeProperties(cadShape);
            }
        }
    }

    public void setCategories(List<Category> categories) {
        this.lineShapeProperties.setCategories(categories);
        this.curveShapeProperties.setCategories(categories);
    }
}
