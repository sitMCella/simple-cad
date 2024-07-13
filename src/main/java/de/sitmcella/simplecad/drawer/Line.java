package de.sitmcella.simplecad.drawer;

import de.sitmcella.simplecad.CadCanvas;
import de.sitmcella.simplecad.property.LineProperty;
import java.util.ArrayList;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign2.MaterialDesignR;

public class Line extends Shape implements ShapeDrawer {

    private static final DrawActions DRAW_ACTION = DrawActions.LINE_DRAW;

    private static final FontIcon ICON = new FontIcon(MaterialDesignR.RAY_START_END);

    private boolean actionOngoing;

    private javafx.scene.shape.Line line;

    public Line(final CadCanvas cadCanvas, final ShapeDrawerListener shapeDrawerListener) {
        super(
                cadCanvas,
                shapeDrawerListener,
                new ButtonConfiguration(DRAW_ACTION, ICON, "line-button"));
        this.actionOngoing = false;
        this.line = null;
    }

    public Shapes handleMouseClick(MouseEvent event) {
        if (this.actionOngoing) {
            var closestPoint = this.cadCanvas.selectClosePoint(event, false);
            if (closestPoint != null) {
                var point = closestPoint.circle();
                updateLine(point.getCenterX(), point.getCenterY());
            } else {
                updateLine(event.getX(), event.getY());
            }
            Circle endPoint = new Circle(line.getEndX(), line.getEndY(), 3.0d);
            endPoint.setOnMouseExited(this.cadCanvas::handlePointMouseExited);
            this.actionOngoing = false;
            var shapes =
                    new ArrayList<javafx.scene.shape.Shape>() {
                        {
                            add(endPoint);
                        }
                    };
            return new Shapes(shapes, line);
        } else {
            Circle startPoint = new Circle(event.getX(), event.getY(), 3.0d);
            var closestPoint = this.cadCanvas.selectClosePoint(event, false);
            if (closestPoint != null) {
                var point = closestPoint.circle();
                startPoint = new Circle(point.getCenterX(), point.getCenterY(), 3.0d);
            }
            startPoint.setOnMouseExited(this.cadCanvas::handlePointMouseExited);
            javafx.scene.shape.Line line =
                    create(
                            startPoint.getCenterX(),
                            startPoint.getCenterY(),
                            startPoint.getCenterX(),
                            startPoint.getCenterY());
            this.line = line;
            this.actionOngoing = true;
            var shapes = new ArrayList<javafx.scene.shape.Shape>();
            shapes.add(startPoint);
            shapes.add(line);
            return new Shapes(shapes, line);
        }
    }

    public void handleMouseMove(MouseEvent event) {
        var closestPoint = this.cadCanvas.selectClosePoint(event, false);
        if (closestPoint != null) {
            var point = closestPoint.circle();
            updateLine(point.getCenterX(), point.getCenterY());
        } else {
            updateLine(event.getX(), event.getY());
        }
    }

    public Shapes copy(javafx.scene.shape.Shape selectedShape) {
        var originalShape = (javafx.scene.shape.Line) selectedShape;
        var startX = originalShape.getStartX() - 20;
        var startY = originalShape.getStartY() - 20;
        var endX = originalShape.getEndX() - 20;
        var endY = originalShape.getEndY() - 20;
        return createLineShape(startX, startY, endX, endY);
    }

    public Shapes createLineShape(double startX, double startY, double endX, double endY) {
        javafx.scene.shape.Line line = create(startX, startY, endX, endY);
        Circle startPoint = new Circle(startX, startY, 3.0d);
        startPoint.setOnMouseExited(this.cadCanvas::handlePointMouseExited);
        Circle endPoint = new Circle(endX, endY, 3.0d);
        endPoint.setOnMouseExited(this.cadCanvas::handlePointMouseExited);
        var shapes = new ArrayList<javafx.scene.shape.Shape>();
        shapes.add(startPoint);
        shapes.add(line);
        shapes.add(endPoint);
        return new Shapes(shapes, line);
    }

    public void update(javafx.scene.shape.Line line, final LineProperty lineProperty) {
        var lineStartX = line.getStartX();
        var lineStartY = line.getStartY();
        var lineEndX = line.getEndX();
        var lineEndY = line.getEndY();
        line.setStartX(lineProperty.startX());
        line.setStartY(lineProperty.startY());
        line.setEndX(lineProperty.endX());
        line.setEndY(lineProperty.endY());
        this.cadCanvas.selectedShape = line;
        var initialStartPoint = this.cadCanvas.getPoint(line, lineStartX, lineStartY);
        var startCircle = initialStartPoint.circle();
        this.cadCanvas.removePoint(initialStartPoint);
        this.cadCanvas.addPoint(lineProperty.startX(), lineProperty.startY(), startCircle, line);

        var initialEndPoint = this.cadCanvas.getPoint(line, lineEndX, lineEndY);
        var endCircle = initialEndPoint.circle();
        this.cadCanvas.removePoint(initialEndPoint);
        this.cadCanvas.addPoint(lineProperty.endX(), lineProperty.endY(), endCircle, line);
    }

    private javafx.scene.shape.Line create(double startX, double startY, double endX, double endY) {
        javafx.scene.shape.Line line = new javafx.scene.shape.Line();
        line.setStartX(startX);
        line.setStartY(startY);
        line.setEndX(endX);
        line.setEndY(endY);
        line.setStrokeWidth(1.0);
        line.setOnMouseEntered(this.cadCanvas::handleShapeMouseEntered);
        line.setOnMouseExited(this.cadCanvas::handleShapeMouseExited);
        return line;
    }

    private void updateLine(double endX, double endY) {
        if (this.actionOngoing) {
            if (this.drawerProperties.constraintAngles()) {
                if (Math.abs(endX - line.getStartX()) > Math.abs(endY - line.getStartY())) {
                    line.setEndX(endX);
                    line.setEndY(line.getStartY());
                } else {
                    line.setEndX(line.getStartX());
                    line.setEndY(endY);
                }
            } else {
                line.setEndX(endX);
                line.setEndY(endY);
            }
        }
    }

    public javafx.scene.shape.Shape use(
            javafx.scene.shape.Shape selectedShape, Circle closestPoint) {
        this.actionOngoing = true;
        this.line = (javafx.scene.shape.Line) selectedShape;
        this.line.setStrokeWidth(1.0d);
        this.line.setStroke(Color.BLACK);
        if (closestPoint.getCenterX() == line.getStartX()
                && closestPoint.getCenterY() == line.getStartY()) {
            var endX = line.getEndX();
            var endY = line.getEndY();
            line.setStartX(endX);
            line.setStartY(endY);
            line.setEndX(closestPoint.getCenterX());
            line.setEndY(closestPoint.getCenterY());
        }
        return line;
    }
}
