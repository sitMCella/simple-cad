package de.sitmcella.simplecad.operation;

import de.sitmcella.simplecad.CadCanvas;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign2.MaterialDesignC;

public class Duplicate extends Operation {

    private static final OperationAction OPERATION_ACTION = OperationAction.DUPLICATE;

    private static final FontIcon ICON = new FontIcon(MaterialDesignC.CONTENT_DUPLICATE);

    public Duplicate(final CadCanvas cadCanvas, final OperationListener operationListener) {
        super(
                cadCanvas,
                operationListener,
                new ButtonConfiguration(OPERATION_ACTION, ICON, "duplicate-button"));
    }
}
