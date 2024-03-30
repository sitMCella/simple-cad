package de.sitmcella.simplecad.operation;

import de.sitmcella.simplecad.CadCanvas;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

public abstract class Operation {

    protected final CadCanvas cadCanvas;

    protected final OperationAction operationAction;

    protected final List<OperationListener> operationListeners;

    protected final Button button;

    public Operation(
            final CadCanvas cadCanvas,
            final OperationListener operationListener,
            final ButtonConfiguration buttonConfiguration) {
        this.cadCanvas = cadCanvas;
        this.operationListeners =
                new ArrayList<>() {
                    {
                        add(operationListener);
                    }
                };
        this.operationAction = buttonConfiguration.operationAction();
        Button button = new Button();
        button.setGraphic(buttonConfiguration.fontIcon());
        button.setId(buttonConfiguration.buttonId());
        button.setOnMouseClicked(this::handleButtonClick);
        this.button = button;
    }

    public Button getButton() {
        return this.button;
    }

    private void handleButtonClick(MouseEvent mouseEvent) {
        operationListeners.forEach(
                operationListener ->
                        operationListener.operationsChanged(
                                new OperationChangedEvent(mouseEvent, this.operationAction)));
    }
}
