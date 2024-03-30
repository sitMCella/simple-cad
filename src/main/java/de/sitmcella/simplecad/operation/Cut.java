package de.sitmcella.simplecad.operation;

import de.sitmcella.simplecad.CadCanvas;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign2.MaterialDesignC;

public class Cut extends Operation {

    private static final OperationAction OPERATION_ACTION = OperationAction.CUT;

    private static final FontIcon ICON = new FontIcon(MaterialDesignC.CONTENT_CUT);

    public Cut(final CadCanvas cadCanvas, final OperationListener operationListener) {
        super(
                cadCanvas,
                operationListener,
                new ButtonConfiguration(OPERATION_ACTION, ICON, "cut-button"));
    }
}
