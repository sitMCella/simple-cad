package de.sitmcella.simplecad;

import de.sitmcella.simplecad.drawer.DrawActions;
import de.sitmcella.simplecad.drawer.DrawerProperties;
import de.sitmcella.simplecad.drawer.ShapeDrawer;
import de.sitmcella.simplecad.drawer.Shapes;
import de.sitmcella.simplecad.property.CanvasPropertyChangeEvent;
import de.sitmcella.simplecad.property.CanvasPropertyListener;
import de.sitmcella.simplecad.property.CanvasSizeProperty;
import de.sitmcella.simplecad.property.LineProperty;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;

public class CadCanvas implements CanvasPropertyListener {

    private final Pane pane;

    private final List<Shape> shapes;

    private DrawerProperties drawerProperties;

    public List<CanvasPoint> canvasPoints;

    public Shape hoverShape;

    public Shape selectedShape;

    public CadCanvas(final Pane pane, DrawerProperties drawerProperties) {
        this.pane = pane;
        this.shapes = new ArrayList<>();
        this.drawerProperties = drawerProperties;
        this.canvasPoints = new ArrayList<>();
        this.hoverShape = null;
        this.selectedShape = null;
    }

    public void addShapes(Shapes shapes) {
        for (var shape : shapes.shapes()) {
            this.pane.getChildren().add(shape);
            if (shape != shapes.mainShape()) {
                addPointCoordinates((Circle) shape, shapes.mainShape());
            } else {
                this.shapes.add(shape);
            }
        }
    }

    public void addLineShape(double startX, double startY, double endX, double endY) {
        javafx.scene.shape.Line line = new javafx.scene.shape.Line();
        line.setStartX(startX);
        line.setStartY(startY);
        line.setEndX(endX);
        line.setEndY(endY);
        line.setStrokeWidth(1.0);
        line.setOnMouseEntered(this::handleShapeMouseEntered);
        line.setOnMouseExited(this::handleShapeMouseExited);
        Circle startPoint = new Circle(startX, startY, 3.0d);
        startPoint.setOnMouseExited(this::handlePointMouseExited);
        Circle endPoint = new Circle(endX, endY, 3.0d);
        endPoint.setOnMouseExited(this::handlePointMouseExited);
        var shapes = new ArrayList<javafx.scene.shape.Shape>();
        shapes.add(startPoint);
        shapes.add(line);
        shapes.add(endPoint);
        addShapes(new Shapes(shapes, line));
    }

    public void addPointCoordinates(Circle circle, Shape mainShape) {
        CanvasPoint canvasPoint =
                new CanvasPoint(circle.getCenterX(), circle.getCenterY(), circle, mainShape);
        canvasPoints.add(canvasPoint);
    }

    public CanvasPoint selectClosePoint(MouseEvent event, boolean isSelected) {
        var closestPoint =
                canvasPoints.stream()
                        .filter(
                                point ->
                                        isSelected
                                                ? (point.mainShape() == this.selectedShape
                                                        && Math.abs(point.x() - event.getX()) < 5
                                                        && Math.abs(point.y() - event.getY()) < 5)
                                                : (Math.abs(point.x() - event.getX()) < 5
                                                        && Math.abs(point.y() - event.getY()) < 5))
                        .findFirst();
        return closestPoint.orElse(null);
    }

    public void setProperties(DrawerProperties drawerProperties) {
        this.drawerProperties = drawerProperties;
    }

    public void handleShapeMouseEntered(MouseEvent mouseEvent) {
        if (drawerProperties.drawAction() == DrawActions.SELECT) {
            if (this.selectedShape == null) {
                ((Shape) mouseEvent.getSource()).setStrokeWidth(3.0d);
                ((Shape) mouseEvent.getSource()).setStroke(Color.DARKBLUE);
            }
            this.hoverShape = (Shape) mouseEvent.getSource();
        }
    }

    public void handleShapeMouseExited(MouseEvent mouseEvent) {
        if (drawerProperties.drawAction() == DrawActions.SELECT) {
            if (this.selectedShape == null) {
                ((Shape) mouseEvent.getSource()).setStrokeWidth(1.0d);
                ((Shape) mouseEvent.getSource()).setStroke(Color.BLACK);
            }
            this.hoverShape = null;
        }
    }

    public List<Shape> getShapes() {
        return this.shapes;
    }

    public void handlePointMouseExited(MouseEvent mouseEvent) {
        ((Shape) mouseEvent.getSource()).setStroke(Color.BLACK);
    }

    public void selectShape(MouseEvent event) {
        if (this.hoverShape == null) {
            this.selectedShape = null;
            shapes.stream()
                    .forEach(
                            shape -> {
                                shape.setStrokeWidth(1.0d);
                                shape.setStroke(Color.BLACK);
                            });
        } else {
            shapes.stream()
                    .filter(shape -> (this.hoverShape == shape))
                    .findFirst()
                    .ifPresent(
                            shape -> {
                                this.selectedShape = shape;
                                shape.setStroke(Color.DARKBLUE);
                            });
        }
    }

    public void triggerOperation(ShapeDrawer shape) {
        switch (this.drawerProperties.operationAction()) {
            case CUT -> {
                if (this.selectedShape == null) {
                    return;
                }
                this.shapes.remove(this.selectedShape);
                this.pane.getChildren().remove(this.selectedShape);
                var pointsToDelete =
                        this.canvasPoints.stream()
                                .filter(
                                        canvasPoint ->
                                                canvasPoint.mainShape() == this.selectedShape)
                                .toList();
                removePoints(pointsToDelete);
                this.selectedShape = null;
            }
            case DUPLICATE -> {
                if (this.selectedShape == null) {
                    return;
                }
                var shapes = shape.copy(this.selectedShape);
                addShapes(shapes);
            }
            case POINT -> {
                if (this.canvasPoints.isEmpty()) {
                    return;
                }
                var status = canvasPoints.get(0).circle().getStrokeWidth();
                this.canvasPoints.stream()
                        .forEach(
                                point -> {
                                    if (status == 0) {
                                        point.circle().setFill(Color.BLACK);
                                        point.circle().setStrokeWidth(1);
                                    } else {
                                        point.circle().setFill(Color.TRANSPARENT);
                                        point.circle().setStrokeWidth(0);
                                    }
                                });
            }
        }
    }

    public void cleanPoints(CanvasPoint closestPoint) {
        var pointsToDelete =
                this.canvasPoints.stream()
                        .filter(
                                canvasPoint ->
                                        canvasPoint.mainShape() == closestPoint.mainShape()
                                                && canvasPoint == closestPoint)
                        .toList();
        removePoints(pointsToDelete);
    }

    public void recreatePoint(CanvasPoint closestPoint, Shape shape) {
        this.canvasPoints.remove(closestPoint);
        this.canvasPoints.add(
                new CanvasPoint(closestPoint.x(), closestPoint.y(), closestPoint.circle(), shape));
    }

    public void clearCanvas() {
        this.pane.getChildren().clear();
        this.shapes.clear();
        this.canvasPoints.clear();
    }

    public CanvasSizeProperty getCanvasSize() {
        return new CanvasSizeProperty(this.pane.getWidth(), this.pane.getHeight());
    }

    private void removePoints(List<CanvasPoint> points) {
        points.stream()
                .map(CanvasPoint::circle)
                .forEach(point -> this.pane.getChildren().remove(point));
        this.canvasPoints.removeAll(points);
    }

    @Override
    public void canvasPropertyChanged(CanvasPropertyChangeEvent canvasPropertyChangeEvent) {
        var canvasProperty = canvasPropertyChangeEvent.getCanvasProperty();
        switch (canvasProperty.getShapeType()) {
            case CANVAS -> {
                var canvasSizeProperty = (CanvasSizeProperty) canvasProperty.getProperty();
                setCanvasSize(canvasSizeProperty.canvasWidth(), canvasSizeProperty.canvasHeight());
            }
            case LINE -> {
                if (this.selectedShape == null) {
                    return;
                }
                var lineProperty = (LineProperty) canvasProperty.getProperty();
                var line = (Line) this.selectedShape;
                var lineStartX = line.getStartX();
                var lineStartY = line.getStartY();
                var lineEndX = line.getEndX();
                var lineEndY = line.getEndY();
                line.setStartX(lineProperty.startX());
                line.setStartY(lineProperty.startY());
                line.setEndX(lineProperty.endX());
                line.setEndY(lineProperty.endY());
                this.selectedShape = line;
                var initialStartPoint =
                        this.canvasPoints.stream()
                                .filter(canvasPoint -> canvasPoint.mainShape() == line)
                                .filter(
                                        canvasPoint ->
                                                canvasPoint.x() == lineStartX
                                                        && canvasPoint.y() == lineStartY)
                                .findFirst()
                                .get();
                var startCircle = initialStartPoint.circle();
                this.canvasPoints.remove(initialStartPoint);
                startCircle.setCenterX(lineProperty.startX());
                startCircle.setCenterY(lineProperty.startY());
                var newStartPoint =
                        new CanvasPoint(
                                lineProperty.startX(), lineProperty.startY(), startCircle, line);
                this.canvasPoints.add(newStartPoint);

                var initialEndPoint =
                        this.canvasPoints.stream()
                                .filter(canvasPoint -> canvasPoint.mainShape() == line)
                                .filter(
                                        canvasPoint ->
                                                canvasPoint.x() == lineEndX
                                                        && canvasPoint.y() == lineEndY)
                                .findFirst()
                                .get();
                var endCircle = initialEndPoint.circle();
                this.canvasPoints.remove(initialEndPoint);
                endCircle.setCenterX(lineProperty.endX());
                endCircle.setCenterY(lineProperty.endY());
                var newEndPoint =
                        new CanvasPoint(lineProperty.endX(), lineProperty.endY(), endCircle, line);
                this.canvasPoints.add(newEndPoint);
            }
        }
    }

    public void setCanvasSize(double width, double height) {
        this.pane.setMinWidth(width);
        this.pane.setMaxWidth(width);
        this.pane.setMinHeight(height);
        this.pane.setMaxHeight(height);
    }
}
