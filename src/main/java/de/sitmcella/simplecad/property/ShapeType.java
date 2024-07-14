package de.sitmcella.simplecad.property;

public enum ShapeType {
    CANVAS,
    LINE,
    CURVE;

    public static ShapeType fromClass(Object object) {
        return switch (object.getClass().getCanonicalName()) {
            case "javafx.scene.shape.Line" -> LINE;
            case "javafx.scene.shape.QuadCurve" -> CURVE;
            default -> CANVAS;
        };
    }

    public static ShapeType from(String shapeName) {
        return ShapeType.valueOf(shapeName);
    }
}
