package de.sitmcella.simplecad.property;

import java.util.List;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class CanvasProperties {

    private final Tab propertiesTab;

    private CanvasSizeProperty canvasSizeProperty;

    private final List<CanvasPropertyListener> canvasPropertyListeners;

    private final PropertiesUtility propertiesUtility;

    public CanvasProperties(
            final Tab propertiesTab,
            final CanvasSizeProperty canvasSizeProperty,
            final List<CanvasPropertyListener> canvasPropertyListeners,
            final PropertiesUtility propertiesUtility) {
        this.propertiesTab = propertiesTab;
        this.canvasSizeProperty = canvasSizeProperty;
        this.canvasPropertyListeners = canvasPropertyListeners;
        this.propertiesUtility = propertiesUtility;
    }

    public void showCanvasProperties() {
        Label title = new Label("Canvas");
        title.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
        TextFieldSection canvasWidthProperties =
                propertiesUtility.addTextSection(
                        "Width:", String.valueOf(canvasSizeProperty.canvasWidth()));
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
                                propertiesUtility.propertyChanged(
                                        canvasPropertyListeners,
                                        event,
                                        new CanvasProperty(
                                                ShapeType.CANVAS,
                                                new CanvasSizeProperty(width, heigth)));
                            }
                        });
        TextFieldSection canvasHeightProperties =
                propertiesUtility.addTextSection(
                        "Height:", String.valueOf(canvasSizeProperty.canvasHeight()));
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
                                propertiesUtility.propertyChanged(
                                        canvasPropertyListeners,
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

    public CanvasSizeProperty getCanvasSizeProperty() {
        return this.canvasSizeProperty;
    }
}
