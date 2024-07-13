package de.sitmcella.simplecad.property;

import java.util.List;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;

public class PropertiesUtility {

    public PropertiesUtility() {}

    public PropertiesSection addTextSection(String titleText, String valueText) {
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

    public void propertyChanged(
            final List<CanvasPropertyListener> canvasPropertyListeners,
            KeyEvent keyEvent,
            final CanvasProperty canvasProperty) {
        canvasPropertyListeners.forEach(
                canvasPropertyListener ->
                        canvasPropertyListener.canvasPropertyChanged(
                                new CanvasPropertyChangeEvent(keyEvent, canvasProperty)));
    }
}
