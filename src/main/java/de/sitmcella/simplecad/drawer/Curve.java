package de.sitmcella.simplecad.drawer;

import de.sitmcella.simplecad.CadCanvas;
import de.sitmcella.simplecad.property.CurveProperty;
import java.util.ArrayList;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.QuadCurve;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign2.MaterialDesignV;

public class Curve extends Shape implements ShapeDrawer {

    private static final DrawActions DRAW_ACTION = DrawActions.CURVE_DRAW;

    private static final FontIcon ICON = new FontIcon(MaterialDesignV.VECTOR_CURVE);

    private Stage stage;

    private javafx.scene.shape.QuadCurve curve;

    public Curve(final CadCanvas cadCanvas, final ShapeDrawerListener shapeDrawerListener) {
        super(
                cadCanvas,
                shapeDrawerListener,
                new ButtonConfiguration(DRAW_ACTION, ICON, "curve-button"));
        this.stage = Stage.START;
        this.curve = null;
    }

    public Shapes handleMouseClick(MouseEvent event) {
        switch (this.stage) {
            case START -> {
                Circle startPoint = new Circle(event.getX(), event.getY(), 3.0d);
                var closestPoint = this.cadCanvas.selectClosePoint(event, false);
                if (closestPoint != null) {
                    var point = closestPoint.circle();
                    startPoint = new Circle(point.getCenterX(), point.getCenterY(), 3.0d);
                }
                startPoint.setOnMouseExited(this.cadCanvas::handlePointMouseExited);
                javafx.scene.shape.QuadCurve curve =
                        create(
                                startPoint.getCenterX(),
                                startPoint.getCenterY(),
                                startPoint.getCenterX(),
                                startPoint.getCenterY());
                this.curve = curve;
                this.stage = Stage.CONTROL;
                var shapes = new ArrayList<javafx.scene.shape.Shape>();
                shapes.add(startPoint);
                shapes.add(curve);
                return new Shapes(shapes, curve);
            }
            case CONTROL -> {
                var closestPoint = this.cadCanvas.selectClosePoint(event, false);
                if (closestPoint != null) {
                    var point = closestPoint.circle();
                    updateCurveControl(point.getCenterX(), point.getCenterY());
                } else {
                    updateCurveControl(event.getX(), event.getY());
                }
                Circle controlPoint = new Circle(curve.getControlX(), curve.getControlY(), 3.0d);
                controlPoint.setOnMouseExited(this.cadCanvas::handlePointMouseExited);
                this.stage = Stage.END;
                var shapes = new ArrayList<javafx.scene.shape.Shape>() {};
                return new Shapes(shapes, curve);
            }
            case END -> {
                var closestPoint = this.cadCanvas.selectClosePoint(event, false);
                if (closestPoint != null) {
                    var point = closestPoint.circle();
                    updateCurveEnd(point.getCenterX(), point.getCenterY());
                } else {
                    updateCurveEnd(event.getX(), event.getY());
                }
                Circle endPoint = new Circle(curve.getEndX(), curve.getEndY(), 3.0d);
                endPoint.setOnMouseExited(this.cadCanvas::handlePointMouseExited);
                this.stage = Stage.START;
                var shapes =
                        new ArrayList<javafx.scene.shape.Shape>() {
                            {
                                add(endPoint);
                            }
                        };
                return new Shapes(shapes, curve);
            }
        }
        return null;
    }

    private javafx.scene.shape.QuadCurve create(
            double startX, double startY, double controlX, double controlY) {
        return create(startX, startY, controlX, controlY, controlX, controlY);
    }

    private javafx.scene.shape.QuadCurve create(
            double startX,
            double startY,
            double controlX,
            double controlY,
            double endX,
            double endY) {
        javafx.scene.shape.QuadCurve curve = new javafx.scene.shape.QuadCurve();
        curve.setStartX(startX);
        curve.setStartY(startY);
        curve.setControlX(controlX);
        curve.setControlY(controlY);
        curve.setEndX(endX);
        curve.setEndY(endY);
        curve.setOnMouseEntered(this.cadCanvas::handleShapeMouseEntered);
        curve.setOnMouseExited(this.cadCanvas::handleShapeMouseExited);
        curve.setFill(Color.TRANSPARENT);
        curve.setStroke(Color.BLACK);
        curve.setStrokeWidth(1.0d);
        return curve;
    }

    private void updateCurveControl(double controlX, double controlY) {
        this.curve.setControlX(controlX);
        this.curve.setControlY(controlY);
        updateCurveEnd(controlX, controlY);
    }

    private void updateCurveEnd(double endX, double endY) {
        this.curve.setEndX(endX);
        this.curve.setEndY(endY);
    }

    public void handleMouseMove(MouseEvent event) {
        var closestPoint = this.cadCanvas.selectClosePoint(event, false);
        switch (this.stage) {
            case START -> {}
            case CONTROL -> {
                if (closestPoint != null) {
                    var point = closestPoint.circle();
                    updateCurveControl(point.getCenterX(), point.getCenterY());
                } else {
                    updateCurveControl(event.getX(), event.getY());
                }
            }
            case END -> {
                if (closestPoint != null) {
                    var point = closestPoint.circle();
                    updateCurveEnd(point.getCenterX(), point.getCenterY());
                } else {
                    updateCurveEnd(event.getX(), event.getY());
                }
            }
        }
    }

    public Shapes copy(javafx.scene.shape.Shape selectedShape) {
        this.stage = Stage.END;
        var originalCurve = (javafx.scene.shape.QuadCurve) selectedShape;
        var startX = originalCurve.getStartX() - 20;
        var startY = originalCurve.getStartY() - 20;
        var controlX = originalCurve.getControlX() - 20;
        var controlY = originalCurve.getControlY() - 20;
        var endX = originalCurve.getEndX() - 20;
        var endY = originalCurve.getEndY() - 20;
        return createCurveShape(startX, startY, controlX, controlY, endX, endY);
    }

    public Shapes createCurveShape(
            double startX,
            double startY,
            double controlX,
            double controlY,
            double endX,
            double endY) {
        javafx.scene.shape.QuadCurve curve = create(startX, startY, controlX, controlY, endX, endY);
        Circle startPoint = new Circle(startX, startY, 3.0d);
        startPoint.setOnMouseExited(this.cadCanvas::handlePointMouseExited);
        Circle endPoint = new Circle(endX, endY, 3.0d);
        endPoint.setOnMouseExited(this.cadCanvas::handlePointMouseExited);
        var shapes = new ArrayList<javafx.scene.shape.Shape>();
        shapes.add(startPoint);
        shapes.add(curve);
        shapes.add(endPoint);
        return new Shapes(shapes, curve);
    }

    public void update(QuadCurve curve, CurveProperty curveProperty) {
        var curveStartX = curve.getStartX();
        var curveStartY = curve.getStartY();
        var curveEndX = curve.getEndX();
        var curveEndY = curve.getEndY();
        curve.setStartX(curveProperty.startX());
        curve.setStartY(curveProperty.startY());
        curve.setControlX(curveProperty.controlX());
        curve.setControlY(curveProperty.controlY());
        curve.setEndX(curveProperty.endX());
        curve.setEndY(curveProperty.endY());
        this.cadCanvas.selectedShape = curve;
        var initialStartPoint = this.cadCanvas.getPoint(curve, curveStartX, curveStartY);
        var startCircle = initialStartPoint.circle();
        this.cadCanvas.removePoint(initialStartPoint);
        this.cadCanvas.addPoint(curveProperty.startX(), curveProperty.startY(), startCircle, curve);

        var initialEndPoint = this.cadCanvas.getPoint(curve, curveEndX, curveEndY);
        var endCircle = initialEndPoint.circle();
        this.cadCanvas.removePoint(initialEndPoint);
        this.cadCanvas.addPoint(curveProperty.endX(), curveProperty.endY(), endCircle, curve);
    }

    public javafx.scene.shape.Shape use(
            javafx.scene.shape.Shape selectedShape, Circle closestPoint) {
        this.stage = Stage.CONTROL;
        this.curve = (javafx.scene.shape.QuadCurve) selectedShape;
        this.curve.setStrokeWidth(1.0d);
        this.curve.setStroke(Color.BLACK);
        if (closestPoint.getCenterX() == curve.getStartX()
                && closestPoint.getCenterY() == curve.getStartY()) {
            var endX = curve.getEndX();
            var endY = curve.getEndY();
            curve.setStartX(endX);
            curve.setStartY(endY);
            curve.setEndX(closestPoint.getCenterX());
            curve.setEndY(closestPoint.getCenterY());
        }
        return curve;
    }
}

enum Stage {
    START,
    CONTROL,
    END
}
