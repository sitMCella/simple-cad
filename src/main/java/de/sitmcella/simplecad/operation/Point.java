package de.sitmcella.simplecad.operation;

import de.sitmcella.simplecad.CadCanvas;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign2.MaterialDesignV;

public class Point extends Operation {

    private static final OperationAction OPERATION_ACTION = OperationAction.POINT;

    private static final FontIcon ICON = new FontIcon(MaterialDesignV.VECTOR_SQUARE);

    public Point(CadCanvas cadCanvas, OperationListener operationListener) {
        super(
                cadCanvas,
                operationListener,
                new ButtonConfiguration(OPERATION_ACTION, ICON, "point-button"));
    }
}
