package de.sitmcella.simplecad.property;

public class CanvasProperty {

    private final ShapeType shapeType;

    private final Property property;

    public CanvasProperty(final ShapeType shapeType, final Property property) {
        this.shapeType = shapeType;
        this.property = property;
    }

    public ShapeType getShapeType() {
        return this.shapeType;
    }

    public Property getProperty() {
        return this.property;
    }
}
