package de.sitmcella.simplecad.property;

import java.util.List;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.scene.shape.QuadCurve;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class CurveShapeProperties {

    private final Tab propertiesTab;

    private final List<CanvasPropertyListener> canvasPropertyListeners;

    private final PropertiesUtility propertiesUtility;

    private CurveProperty curveProperty;

    public CurveShapeProperties(
            final Tab propertiesTab,
            final List<CanvasPropertyListener> canvasPropertyListeners,
            final PropertiesUtility propertiesUtility) {
        this.propertiesTab = propertiesTab;
        this.canvasPropertyListeners = canvasPropertyListeners;
        this.propertiesUtility = propertiesUtility;
        this.curveProperty = null;
    }

    public void showCurveShapeProperties(QuadCurve curve) {
        this.curveProperty =
                new CurveProperty(
                        curve.getStartX(),
                        curve.getStartY(),
                        curve.getControlX(),
                        curve.getControlY(),
                        curve.getEndX(),
                        curve.getEndY());
        Label title = new Label("Curve");
        title.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
        PropertiesSection startXProperty =
                propertiesUtility.addTextSection("Start X:", String.valueOf(curve.getStartX()));
        startXProperty
                .getTextField()
                .setOnKeyPressed(
                        event -> {
                            if (event.getCode() == KeyCode.ENTER) {
                                var startX =
                                        Double.valueOf(startXProperty.getTextField().getText());
                                this.curveProperty =
                                        new CurveProperty(
                                                startX,
                                                curve.getStartY(),
                                                curve.getControlX(),
                                                curve.getControlY(),
                                                curve.getEndX(),
                                                curve.getEndY());
                                propertiesUtility.propertyChanged(
                                        canvasPropertyListeners,
                                        event,
                                        new CanvasProperty(ShapeType.CURVE, this.curveProperty));
                            }
                        });
        PropertiesSection startYProperty =
                propertiesUtility.addTextSection("Start Y:", String.valueOf(curve.getStartY()));
        startYProperty
                .getTextField()
                .setOnKeyPressed(
                        event -> {
                            if (event.getCode() == KeyCode.ENTER) {
                                var startY =
                                        Double.valueOf(startYProperty.getTextField().getText());
                                this.curveProperty =
                                        new CurveProperty(
                                                curve.getStartX(),
                                                startY,
                                                curve.getControlX(),
                                                curve.getControlY(),
                                                curve.getEndX(),
                                                curve.getEndY());
                                propertiesUtility.propertyChanged(
                                        canvasPropertyListeners,
                                        event,
                                        new CanvasProperty(ShapeType.CURVE, this.curveProperty));
                            }
                        });
        PropertiesSection controlXProperty =
                propertiesUtility.addTextSection("Control X:", String.valueOf(curve.getControlX()));
        controlXProperty
                .getTextField()
                .setOnKeyPressed(
                        event -> {
                            if (event.getCode() == KeyCode.ENTER) {
                                var controlX =
                                        Double.valueOf(controlXProperty.getTextField().getText());
                                this.curveProperty =
                                        new CurveProperty(
                                                curve.getStartX(),
                                                curve.getStartY(),
                                                controlX,
                                                curve.getControlY(),
                                                curve.getEndX(),
                                                curve.getEndY());
                                propertiesUtility.propertyChanged(
                                        canvasPropertyListeners,
                                        event,
                                        new CanvasProperty(ShapeType.CURVE, this.curveProperty));
                            }
                        });
        PropertiesSection controlYProperty =
                propertiesUtility.addTextSection("Control Y:", String.valueOf(curve.getControlY()));
        controlYProperty
                .getTextField()
                .setOnKeyPressed(
                        event -> {
                            if (event.getCode() == KeyCode.ENTER) {
                                var controlY =
                                        Double.valueOf(controlYProperty.getTextField().getText());
                                this.curveProperty =
                                        new CurveProperty(
                                                curve.getStartX(),
                                                curve.getStartY(),
                                                curve.getControlX(),
                                                controlY,
                                                curve.getEndX(),
                                                curve.getEndY());
                                propertiesUtility.propertyChanged(
                                        canvasPropertyListeners,
                                        event,
                                        new CanvasProperty(ShapeType.CURVE, this.curveProperty));
                            }
                        });
        PropertiesSection endXProperty =
                propertiesUtility.addTextSection("End X:", String.valueOf(curve.getEndX()));
        endXProperty
                .getTextField()
                .setOnKeyPressed(
                        event -> {
                            if (event.getCode() == KeyCode.ENTER) {
                                var endX = Double.valueOf(endXProperty.getTextField().getText());
                                this.curveProperty =
                                        new CurveProperty(
                                                curve.getStartX(),
                                                curve.getStartY(),
                                                curve.getControlX(),
                                                curve.getControlY(),
                                                endX,
                                                curve.getEndY());
                                propertiesUtility.propertyChanged(
                                        canvasPropertyListeners,
                                        event,
                                        new CanvasProperty(ShapeType.CURVE, this.curveProperty));
                            }
                        });
        PropertiesSection endYProperty =
                propertiesUtility.addTextSection("End Y:", String.valueOf(curve.getEndY()));
        endYProperty
                .getTextField()
                .setOnKeyPressed(
                        event -> {
                            if (event.getCode() == KeyCode.ENTER) {
                                var endY = Double.valueOf(endYProperty.getTextField().getText());
                                this.curveProperty =
                                        new CurveProperty(
                                                curve.getStartX(),
                                                curve.getStartY(),
                                                curve.getControlX(),
                                                curve.getControlY(),
                                                curve.getEndX(),
                                                endY);
                                propertiesUtility.propertyChanged(
                                        canvasPropertyListeners,
                                        event,
                                        new CanvasProperty(ShapeType.CURVE, this.curveProperty));
                            }
                        });
        VBox vBox =
                new VBox(
                        title,
                        startXProperty.getParent(),
                        startYProperty.getParent(),
                        controlXProperty.getParent(),
                        controlYProperty.getParent(),
                        endXProperty.getParent(),
                        endYProperty.getParent());
        vBox.setSpacing(5);
        vBox.getStyleClass().add("canvas-properties-section");
        this.propertiesTab.setContent(vBox);
    }
}
