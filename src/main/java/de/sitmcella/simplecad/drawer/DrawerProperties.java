package de.sitmcella.simplecad.drawer;

import de.sitmcella.simplecad.operation.OperationAction;

public record DrawerProperties(
        DrawActions drawAction, OperationAction operationAction, boolean constraintAngles) {}
