package de.sitmcella.simplecad.property;

public record LineProperty(double startX, double startY, double endX, double endY)
        implements Property {}
