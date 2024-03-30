package de.sitmcella.simplecad.property;

import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class CanvasProperties {

    private final Tab propertiesTab;

    private CanvasSizeProperty canvasSizeProperty;

    private LineProperty lineProperty;

    private final List<CanvasPropertyListener> canvasPropertyListeners;

    public CanvasProperties(final Tab propertiesTab, final CanvasSizeProperty canvasSizeProperty) {
        this.propertiesTab = propertiesTab;
        this.canvasSizeProperty = canvasSizeProperty;
        this.lineProperty = null;
        this.canvasPropertyListeners = new ArrayList<>();
        this.showCanvasProperties();
    }

    public void addListener(final CanvasPropertyListener canvasPropertyListener) {
        this.canvasPropertyListeners.add(canvasPropertyListener);
    }

    public void addConfiguration(ShapeType shapeType, Shape shape) {
        switch (shapeType) {
            case CANVAS -> {
                showCanvasProperties();
            }
            case LINE -> {
                showLineShapeProperties((Line) shape);
            }
        }
    }

    public CanvasSizeProperty getCanvasSizeProperty() {
        return this.canvasSizeProperty;
    }

    private void showCanvasProperties() {
        Label title = new Label("Canvas");
        title.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
        PropertiesSection canvasWidthProperties =
                addTextSection("Width:", String.valueOf(canvasSizeProperty.canvasWidth()));
        canvasWidthProperties
                .getTextField()
                .setOnKeyPressed(
                        event -> {
                            if (event.getCode() == KeyCode.ENTER) {
                                var width =
                                        Double.valueOf(
                                                canvasWidthProperties.getTextField().getText());
                                var heigth = this.canvasSizeProperty.canvasHeight();
                                this.canvasSizeProperty = new CanvasSizeProperty(width, heigth);
                                propertyChanged(
                                        event,
                                        new CanvasProperty(
                                                ShapeType.CANVAS,
                                                new CanvasSizeProperty(width, heigth)));
                            }
                        });
        PropertiesSection canvasHeightProperties =
                addTextSection("Height:", String.valueOf(canvasSizeProperty.canvasHeight()));
        canvasHeightProperties
                .getTextField()
                .setOnKeyPressed(
                        event -> {
                            if (event.getCode() == KeyCode.ENTER) {
                                var width = this.canvasSizeProperty.canvasWidth();
                                var heigth =
                                        Double.valueOf(
                                                canvasHeightProperties.getTextField().getText());
                                this.canvasSizeProperty = new CanvasSizeProperty(width, heigth);
                                propertyChanged(
                                        event,
                                        new CanvasProperty(
                                                ShapeType.CANVAS, this.canvasSizeProperty));
                            }
                        });
        VBox vBox =
                new VBox(
                        title,
                        canvasWidthProperties.getParent(),
                        canvasHeightProperties.getParent());
        vBox.setSpacing(5);
        vBox.getStyleClass().add("canvas-properties-section");
        this.propertiesTab.setContent(vBox);
    }

    private void showLineShapeProperties(Line line) {
        this.lineProperty =
                new LineProperty(
                        line.getStartX(), line.getStartY(), line.getEndX(), line.getEndY());
        Label title = new Label("Line");
        title.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
        PropertiesSection startXProperty =
                addTextSection("Start X:", String.valueOf(line.getStartX()));
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
                                propertyChanged(
                                        event,
                                        new CanvasProperty(ShapeType.LINE, this.lineProperty));
                            }
                        });
        PropertiesSection startYProperty =
                addTextSection("Start Y:", String.valueOf(line.getStartY()));
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
                                propertyChanged(
                                        event,
                                        new CanvasProperty(ShapeType.LINE, this.lineProperty));
                            }
                        });
        PropertiesSection endXProperty = addTextSection("End X:", String.valueOf(line.getEndX()));
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
                                propertyChanged(
                                        event,
                                        new CanvasProperty(ShapeType.LINE, this.lineProperty));
                            }
                        });
        PropertiesSection endYProperty = addTextSection("End Y:", String.valueOf(line.getEndY()));
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
                                propertyChanged(
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

    private PropertiesSection addTextSection(String titleText, String valueText) {
        Label label = new Label(titleText);
        TextField textField = new TextField(valueText);
        textField.setMinWidth(70);
        textField.setMaxWidth(70);
        HBox hBox = new HBox(label, textField);
        hBox.setMinWidth(185);
        hBox.setMaxWidth(185);
        hBox.setSpacing(10);
        hBox.setAlignment(Pos.CENTER);
        return new PropertiesSection(hBox, textField);
    }

    private void propertyChanged(KeyEvent keyEvent, final CanvasProperty canvasProperty) {
        this.canvasPropertyListeners.forEach(
                canvasPropertyListener ->
                        canvasPropertyListener.canvasPropertyChanged(
                                new CanvasPropertyChangeEvent(keyEvent, canvasProperty)));
    }
}
