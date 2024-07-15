package de.sitmcella.simplecad.property;

import de.sitmcella.simplecad.CadShape;
import java.util.List;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class LineShapeProperties {

    private final Tab propertiesTab;

    private final List<CanvasPropertyListener> canvasPropertyListeners;

    private final PropertiesUtility propertiesUtility;

    private LineProperty lineProperty;

    public LineShapeProperties(
            final Tab propertiesTab,
            final List<CanvasPropertyListener> canvasPropertyListeners,
            final PropertiesUtility propertiesUtility) {
        this.propertiesTab = propertiesTab;
        this.canvasPropertyListeners = canvasPropertyListeners;
        this.propertiesUtility = propertiesUtility;
        this.lineProperty = null;
    }

    public void showLineShapeProperties(CadShape cadShape) {
        var line = (Line) cadShape.shape();
        this.lineProperty =
                new LineProperty(
                        line.getStartX(), line.getStartY(), line.getEndX(), line.getEndY());
        Label title = new Label("Line");
        title.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
        PropertiesSection startXProperty =
                propertiesUtility.addTextSection("Start X:", String.valueOf(line.getStartX()));
        startXProperty
                .getTextField()
                .setOnKeyPressed(
                        event -> {
                            if (event.getCode() == KeyCode.ENTER) {
                                var startX =
                                        Double.valueOf(startXProperty.getTextField().getText());
                                this.lineProperty =
                                        new LineProperty(
                                                startX,
                                                this.lineProperty.startY(),
                                                this.lineProperty.endX(),
                                                this.lineProperty.endY());
                                propertiesUtility.propertyChanged(
                                        canvasPropertyListeners,
                                        event,
                                        new CanvasProperty(ShapeType.LINE, this.lineProperty));
                            }
                        });
        PropertiesSection startYProperty =
                propertiesUtility.addTextSection("Start Y:", String.valueOf(line.getStartY()));
        startYProperty
                .getTextField()
                .setOnKeyPressed(
                        event -> {
                            if (event.getCode() == KeyCode.ENTER) {
                                var startY =
                                        Double.valueOf(startYProperty.getTextField().getText());
                                this.lineProperty =
                                        new LineProperty(
                                                this.lineProperty.startX(),
                                                startY,
                                                this.lineProperty.endX(),
                                                this.lineProperty.endY());
                                propertiesUtility.propertyChanged(
                                        canvasPropertyListeners,
                                        event,
                                        new CanvasProperty(ShapeType.LINE, this.lineProperty));
                            }
                        });
        PropertiesSection endXProperty =
                propertiesUtility.addTextSection("End X:", String.valueOf(line.getEndX()));
        endXProperty
                .getTextField()
                .setOnKeyPressed(
                        event -> {
                            if (event.getCode() == KeyCode.ENTER) {
                                var endX = Double.valueOf(endXProperty.getTextField().getText());
                                this.lineProperty =
                                        new LineProperty(
                                                this.lineProperty.startX(),
                                                this.lineProperty.startY(),
                                                endX,
                                                this.lineProperty.endY());
                                propertiesUtility.propertyChanged(
                                        canvasPropertyListeners,
                                        event,
                                        new CanvasProperty(ShapeType.LINE, this.lineProperty));
                            }
                        });
        PropertiesSection endYProperty =
                propertiesUtility.addTextSection("End Y:", String.valueOf(line.getEndY()));
        endYProperty
                .getTextField()
                .setOnKeyPressed(
                        event -> {
                            if (event.getCode() == KeyCode.ENTER) {
                                var endY = Double.valueOf(endYProperty.getTextField().getText());
                                this.lineProperty =
                                        new LineProperty(
                                                this.lineProperty.startX(),
                                                this.lineProperty.startY(),
                                                this.lineProperty.endX(),
                                                endY);
                                propertiesUtility.propertyChanged(
                                        canvasPropertyListeners,
                                        event,
                                        new CanvasProperty(ShapeType.LINE, this.lineProperty));
                            }
                        });
        VBox vBox =
                new VBox(
                        title,
                        startXProperty.getParent(),
                        startYProperty.getParent(),
                        endXProperty.getParent(),
                        endYProperty.getParent());
        vBox.setSpacing(5);
        vBox.getStyleClass().add("canvas-properties-section");
        this.propertiesTab.setContent(vBox);
    }
}
