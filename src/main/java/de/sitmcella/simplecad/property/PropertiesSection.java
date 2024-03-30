package de.sitmcella.simplecad.property;

import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class PropertiesSection {

    private final HBox parent;

    private final TextField textField;

    public PropertiesSection(final HBox parent, final TextField textField) {
        this.parent = parent;
        this.textField = textField;
    }

    public HBox getParent() {
        return this.parent;
    }

    public TextField getTextField() {
        return this.textField;
    }

    public String getValue() {
        return this.textField.getText();
    }
}
