package de.sitmcella.simplecad.property;

import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;

public class DropDownSection {

    private final HBox parent;

    private ComboBox comboBox;

    public DropDownSection(HBox parent, ComboBox comboBox) {
        this.parent = parent;
        this.comboBox = comboBox;
    }

    public ComboBox getComboBox() {
        return comboBox;
    }

    public HBox getParent() {
        return parent;
    }
}
