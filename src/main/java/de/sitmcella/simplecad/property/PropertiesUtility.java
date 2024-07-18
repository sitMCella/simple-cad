package de.sitmcella.simplecad.property;

import java.util.List;
import javafx.event.Event;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class PropertiesUtility {

    public PropertiesUtility() {}

    public TextFieldSection addTextSection(String titleText, String valueText) {
        Label label = new Label(titleText);
        TextField textField = new TextField(valueText);
        textField.setMinWidth(100);
        textField.setMaxWidth(100);
        HBox hBox = new HBox(label, textField);
        hBox.setMinWidth(185);
        hBox.setMaxWidth(185);
        hBox.setSpacing(10);
        hBox.setAlignment(Pos.CENTER_LEFT);
        return new TextFieldSection(hBox, textField);
    }

    public void propertyChanged(
            final List<CanvasPropertyListener> canvasPropertyListeners,
            Event event,
            final CanvasProperty canvasProperty) {
        canvasPropertyListeners.forEach(
                canvasPropertyListener ->
                        canvasPropertyListener.canvasPropertyChanged(
                                new CanvasPropertyChangeEvent(event, canvasProperty)));
    }

    public DropDownSection addDropdownSection(List<String> valuesText) {
        var comboBox = new ComboBox();
        comboBox.getItems().addAll(valuesText);
        comboBox.setMinWidth(150);
        comboBox.setMaxWidth(150);
        HBox hBox = new HBox(comboBox);
        hBox.setMinWidth(185);
        hBox.setMaxWidth(185);
        hBox.setSpacing(10);
        hBox.setAlignment(Pos.CENTER_LEFT);
        return new DropDownSection(hBox, comboBox);
    }
}
