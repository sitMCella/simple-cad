package de.sitmcella.simplecad.property;

public record CurveProperty(
        double startX,
        double startY,
        double controlX,
        double controlY,
        double endX,
        double endY,
        String category)
        implements Property {}
