package de.sitmcella.simplecad.operation;

import java.util.EventObject;

public class OperationChangedEvent extends EventObject {

    private final OperationAction operationAction;

    /**
     * Constructs an Operation Action Event.
     *
     * @param source the object on which the Event initially occurred
     * @throws IllegalArgumentException if source is null
     */
    public OperationChangedEvent(Object source, final OperationAction operationAction) {
        super(source);
        this.operationAction = operationAction;
    }

    public OperationAction getOperationAction() {
        return this.operationAction;
    }
}
