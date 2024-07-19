package de.sitmcella.simplecad.property;

import de.sitmcella.simplecad.CadShape;
import de.sitmcella.simplecad.category.Category;
import java.util.List;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
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

    private List<Category> categories;

    public CurveShapeProperties(
            final Tab propertiesTab,
            final List<CanvasPropertyListener> canvasPropertyListeners,
            final PropertiesUtility propertiesUtility,
            List<Category> categories) {
        this.propertiesTab = propertiesTab;
        this.canvasPropertyListeners = canvasPropertyListeners;
        this.propertiesUtility = propertiesUtility;
        this.curveProperty = null;
        this.categories = categories;
    }

    public void showCurveShapeProperties(CadShape cadShape) {
        var curve = (QuadCurve) cadShape.shape();
        var category = cadShape.category() != null ? cadShape.category().value() : null;
        this.curveProperty =
                new CurveProperty(
                        curve.getStartX(),
                        curve.getStartY(),
                        curve.getControlX(),
                        curve.getControlY(),
                        curve.getEndX(),
                        curve.getEndY(),
                        category);
        Label title = new Label("Curve");
        title.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
        TextFieldSection startXProperty =
                propertiesUtility.addTextSection("Start X:", String.valueOf(curve.getStartX()));
        startXProperty
                .getTextField()
                .setOnKeyPressed(
                        event -> {
                            if (event.getCode() == KeyCode.ENTER) {
                                var startX =
                                        Double.valueOf(startXProperty.getTextField().getText());
                                var categoryValue =
                                        cadShape.category() != null
                                                ? cadShape.category().value()
                                                : null;
                                this.curveProperty =
                                        new CurveProperty(
                                                startX,
                                                curve.getStartY(),
                                                curve.getControlX(),
                                                curve.getControlY(),
                                                curve.getEndX(),
                                                curve.getEndY(),
                                                categoryValue);
                                propertiesUtility.propertyChanged(
                                        canvasPropertyListeners,
                                        event,
                                        new CanvasProperty(ShapeType.CURVE, this.curveProperty));
                            }
                        });
        TextFieldSection startYProperty =
                propertiesUtility.addTextSection("Start Y:", String.valueOf(curve.getStartY()));
        startYProperty
                .getTextField()
                .setOnKeyPressed(
                        event -> {
                            if (event.getCode() == KeyCode.ENTER) {
                                var startY =
                                        Double.valueOf(startYProperty.getTextField().getText());
                                var categoryValue =
                                        cadShape.category() != null
                                                ? cadShape.category().value()
                                                : null;
                                this.curveProperty =
                                        new CurveProperty(
                                                curve.getStartX(),
                                                startY,
                                                curve.getControlX(),
                                                curve.getControlY(),
                                                curve.getEndX(),
                                                curve.getEndY(),
                                                categoryValue);
                                propertiesUtility.propertyChanged(
                                        canvasPropertyListeners,
                                        event,
                                        new CanvasProperty(ShapeType.CURVE, this.curveProperty));
                            }
                        });
        TextFieldSection controlXProperty =
                propertiesUtility.addTextSection("Control X:", String.valueOf(curve.getControlX()));
        controlXProperty
                .getTextField()
                .setOnKeyPressed(
                        event -> {
                            if (event.getCode() == KeyCode.ENTER) {
                                var controlX =
                                        Double.valueOf(controlXProperty.getTextField().getText());
                                var categoryValue =
                                        cadShape.category() != null
                                                ? cadShape.category().value()
                                                : null;
                                this.curveProperty =
                                        new CurveProperty(
                                                curve.getStartX(),
                                                curve.getStartY(),
                                                controlX,
                                                curve.getControlY(),
                                                curve.getEndX(),
                                                curve.getEndY(),
                                                categoryValue);
                                propertiesUtility.propertyChanged(
                                        canvasPropertyListeners,
                                        event,
                                        new CanvasProperty(ShapeType.CURVE, this.curveProperty));
                            }
                        });
        TextFieldSection controlYProperty =
                propertiesUtility.addTextSection("Control Y:", String.valueOf(curve.getControlY()));
        controlYProperty
                .getTextField()
                .setOnKeyPressed(
                        event -> {
                            if (event.getCode() == KeyCode.ENTER) {
                                var controlY =
                                        Double.valueOf(controlYProperty.getTextField().getText());
                                var categoryValue =
                                        cadShape.category() != null
                                                ? cadShape.category().value()
                                                : null;
                                this.curveProperty =
                                        new CurveProperty(
                                                curve.getStartX(),
                                                curve.getStartY(),
                                                curve.getControlX(),
                                                controlY,
                                                curve.getEndX(),
                                                curve.getEndY(),
                                                categoryValue);
                                propertiesUtility.propertyChanged(
                                        canvasPropertyListeners,
                                        event,
                                        new CanvasProperty(ShapeType.CURVE, this.curveProperty));
                            }
                        });
        TextFieldSection endXProperty =
                propertiesUtility.addTextSection("End X:", String.valueOf(curve.getEndX()));
        endXProperty
                .getTextField()
                .setOnKeyPressed(
                        event -> {
                            if (event.getCode() == KeyCode.ENTER) {
                                var endX = Double.valueOf(endXProperty.getTextField().getText());
                                var categoryValue =
                                        cadShape.category() != null
                                                ? cadShape.category().value()
                                                : null;
                                this.curveProperty =
                                        new CurveProperty(
                                                curve.getStartX(),
                                                curve.getStartY(),
                                                curve.getControlX(),
                                                curve.getControlY(),
                                                endX,
                                                curve.getEndY(),
                                                categoryValue);
                                propertiesUtility.propertyChanged(
                                        canvasPropertyListeners,
                                        event,
                                        new CanvasProperty(ShapeType.CURVE, this.curveProperty));
                            }
                        });
        TextFieldSection endYProperty =
                propertiesUtility.addTextSection("End Y:", String.valueOf(curve.getEndY()));
        endYProperty
                .getTextField()
                .setOnKeyPressed(
                        event -> {
                            if (event.getCode() == KeyCode.ENTER) {
                                var endY = Double.valueOf(endYProperty.getTextField().getText());
                                var categoryValue =
                                        cadShape.category() != null
                                                ? cadShape.category().value()
                                                : null;
                                this.curveProperty =
                                        new CurveProperty(
                                                curve.getStartX(),
                                                curve.getStartY(),
                                                curve.getControlX(),
                                                curve.getControlY(),
                                                curve.getEndX(),
                                                endY,
                                                categoryValue);
                                propertiesUtility.propertyChanged(
                                        canvasPropertyListeners,
                                        event,
                                        new CanvasProperty(ShapeType.CURVE, this.curveProperty));
                            }
                        });

        Separator separator = new Separator();

        Label categoryTitle = new Label("Category");
        categoryTitle.setFont(Font.font("Verdana", FontWeight.BOLD, 12));

        List<String> categoryValues = categories.stream().map(Category::value).toList();
        DropDownSection existentCategories = propertiesUtility.addDropdownSection(categoryValues);
        var categoryValue = cadShape.category() != null ? cadShape.category().value() : null;
        existentCategories.getComboBox().setValue(categoryValue);

        existentCategories
                .getComboBox()
                .setOnAction(
                        event -> {
                            this.curveProperty =
                                    new CurveProperty(
                                            curve.getStartX(),
                                            curve.getStartY(),
                                            curve.getControlX(),
                                            curve.getControlY(),
                                            curve.getEndX(),
                                            curve.getEndY(),
                                            (String) existentCategories.getComboBox().getValue());
                            propertiesUtility.propertyChanged(
                                    canvasPropertyListeners,
                                    event,
                                    new CanvasProperty(ShapeType.CURVE, this.curveProperty));
                        });

        Button resetFilterButton = new Button();
        resetFilterButton.setText("Reset");
        resetFilterButton.setOnMouseClicked(
                (e) -> {
                    existentCategories.getComboBox().setValue(null);
                    this.curveProperty =
                            new CurveProperty(
                                    curve.getStartX(),
                                    curve.getStartY(),
                                    curve.getControlX(),
                                    curve.getControlY(),
                                    curve.getEndX(),
                                    curve.getEndY(),
                                    null);
                    propertiesUtility.propertyChanged(
                            canvasPropertyListeners,
                            e,
                            new CanvasProperty(ShapeType.CURVE, this.curveProperty));
                });

        VBox vBox =
                new VBox(
                        title,
                        startXProperty.getParent(),
                        startYProperty.getParent(),
                        controlXProperty.getParent(),
                        controlYProperty.getParent(),
                        endXProperty.getParent(),
                        endYProperty.getParent(),
                        separator,
                        categoryTitle,
                        existentCategories.getParent(),
                        resetFilterButton);
        vBox.setSpacing(5);
        vBox.getStyleClass().add("canvas-properties-section");
        this.propertiesTab.setContent(vBox);
    }

    public void setCategories(final List<Category> categories) {
        this.categories = categories;
    }
}
