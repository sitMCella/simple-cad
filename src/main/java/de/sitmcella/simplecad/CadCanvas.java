package de.sitmcella.simplecad;

import de.sitmcella.simplecad.drawer.DrawActions;
import de.sitmcella.simplecad.drawer.DrawerProperties;
import de.sitmcella.simplecad.drawer.ShapeDrawer;
import de.sitmcella.simplecad.drawer.Shapes;
import de.sitmcella.simplecad.property.CanvasSizeProperty;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

public class CadCanvas {

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

    public CanvasPoint getPoint(Shape mainShape, double x, double y) {
        return this.canvasPoints.stream()
                .filter(canvasPoint -> canvasPoint.mainShape() == mainShape)
                .filter(canvasPoint -> canvasPoint.x() == x && canvasPoint.y() == y)
                .findFirst()
                .get();
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

    public void addPoint(double x, double y, Circle circle, Shape shape) {
        circle.setCenterX(x);
        circle.setCenterY(y);
        var point = new CanvasPoint(x, y, circle, shape);
        this.canvasPoints.add(point);
    }

    public void removePoint(CanvasPoint point) {
        this.canvasPoints.remove(point);
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

    public void setCanvasSize(double width, double height) {
        this.pane.setMinWidth(width);
        this.pane.setMaxWidth(width);
        this.pane.setMinHeight(height);
        this.pane.setMaxHeight(height);
    }
}
