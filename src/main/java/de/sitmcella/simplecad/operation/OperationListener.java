package de.sitmcella.simplecad.operation;

import java.util.EventListener;

public interface OperationListener extends EventListener {

    void operationsChanged(OperationChangedEvent operationChangedEvent);
}
