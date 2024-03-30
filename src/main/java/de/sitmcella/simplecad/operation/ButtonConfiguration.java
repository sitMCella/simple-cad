package de.sitmcella.simplecad.operation;

import org.kordamp.ikonli.javafx.FontIcon;

public record ButtonConfiguration(
        OperationAction operationAction, FontIcon fontIcon, String buttonId) {}
