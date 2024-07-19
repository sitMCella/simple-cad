package de.sitmcella.simplecad.drawer;

import de.sitmcella.simplecad.CadCanvas;
import de.sitmcella.simplecad.CadShape;
import de.sitmcella.simplecad.category.Category;
import de.sitmcella.simplecad.property.LineProperty;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign2.MaterialDesignR;

public class Line extends Shape implements ShapeDrawer {

    private static final DrawActions DRAW_ACTION = DrawActions.LINE_DRAW;

    private static final FontIcon ICON = new FontIcon(MaterialDesignR.RAY_START_END);

    private LineDrawerStage stage;

    private javafx.scene.shape.Line line;

    public Line(
            final CadCanvas cadCanvas,
            final ShapeDrawerListener shapeDrawerListener,
            final List<Category> categories) {
        super(
                cadCanvas,
                shapeDrawerListener,
                new ButtonConfiguration(DRAW_ACTION, ICON, "line-button"),
                categories);
        this.stage = LineDrawerStage.START;
        this.line = null;
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
                javafx.scene.shape.Line line =
                        create(
                                startPoint.getCenterX(),
                                startPoint.getCenterY(),
                                startPoint.getCenterX(),
                                startPoint.getCenterY());
                this.line = line;
                this.stage = LineDrawerStage.END;
                var shapes = new ArrayList<CadShape>();
                shapes.add(new CadShape(startPoint, null));
                shapes.add(new CadShape(line, null));
                return new Shapes(shapes, line);
            }
            case END -> {
                var closestPoint = this.cadCanvas.selectClosePoint(event, false);
                if (closestPoint != null) {
                    var point = closestPoint.circle();
                    updateLine(point.getCenterX(), point.getCenterY());
                } else {
                    updateLine(event.getX(), event.getY());
                }
                Circle endPoint = new Circle(line.getEndX(), line.getEndY(), 3.0d);
                endPoint.setOnMouseExited(this.cadCanvas::handlePointMouseExited);
                this.stage = LineDrawerStage.START;
                var shapes =
                        new ArrayList<CadShape>() {
                            {
                                add(new CadShape(endPoint, null));
                            }
                        };
                return new Shapes(shapes, line);
            }
        }
        return null;
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

    public Shapes copy(CadShape cadShape) {
        var originalShape = (javafx.scene.shape.Line) cadShape.shape();
        var startX = originalShape.getStartX() - 20;
        var startY = originalShape.getStartY() - 20;
        var endX = originalShape.getEndX() - 20;
        var endY = originalShape.getEndY() - 20;
        var categoryValue = cadShape.category() != null ? cadShape.category().value() : null;
        return createLineShape(startX, startY, endX, endY, categoryValue);
    }

    public Shapes createLineShape(
            double startX, double startY, double endX, double endY, String category) {
        var categoryEntry = getCategory(category);
        javafx.scene.shape.Line line = create(startX, startY, endX, endY, categoryEntry);
        Circle startPoint = new Circle(startX, startY, 3.0d);
        startPoint.setOnMouseExited(this.cadCanvas::handlePointMouseExited);
        Circle endPoint = new Circle(endX, endY, 3.0d);
        endPoint.setOnMouseExited(this.cadCanvas::handlePointMouseExited);
        var shapes = new ArrayList<CadShape>();
        shapes.add(new CadShape(startPoint, categoryEntry));
        shapes.add(new CadShape(line, categoryEntry));
        shapes.add(new CadShape(endPoint, categoryEntry));
        return new Shapes(shapes, line);
    }

    public void update(CadShape cadShape, final LineProperty lineProperty) {
        var line = (javafx.scene.shape.Line) cadShape.shape();
        var lineStartX = line.getStartX();
        var lineStartY = line.getStartY();
        var lineEndX = line.getEndX();
        var lineEndY = line.getEndY();
        line.setStartX(lineProperty.startX());
        line.setStartY(lineProperty.startY());
        line.setEndX(lineProperty.endX());
        line.setEndY(lineProperty.endY());
        var categoryEntry = getCategory(lineProperty.category());
        this.cadCanvas.cadShape = new CadShape(line, categoryEntry);
        var initialStartPoint = this.cadCanvas.getPoint(line, lineStartX, lineStartY);
        var startCircle = initialStartPoint.circle();
        this.cadCanvas.removePoint(initialStartPoint);
        this.cadCanvas.addPoint(lineProperty.startX(), lineProperty.startY(), startCircle, line);
        var initialEndPoint = this.cadCanvas.getPoint(line, lineEndX, lineEndY);
        var endCircle = initialEndPoint.circle();
        this.cadCanvas.removePoint(initialEndPoint);
        this.cadCanvas.addPoint(lineProperty.endX(), lineProperty.endY(), endCircle, line);
        this.cadCanvas.updateShape(this.cadCanvas.cadShape);
    }

    private javafx.scene.shape.Line create(double startX, double startY, double endX, double endY) {
        return create(startX, startY, endX, endY, null);
    }

    private javafx.scene.shape.Line create(
            double startX, double startY, double endX, double endY, Category category) {
        javafx.scene.shape.Line line = new javafx.scene.shape.Line();
        line.setStartX(startX);
        line.setStartY(startY);
        line.setEndX(endX);
        line.setEndY(endY);
        line.setStrokeWidth(1.0);
        configureStroke(line, this.cadCanvas, category);
        line.setOnMouseEntered(this.cadCanvas::handleShapeMouseEntered);
        line.setOnMouseExited(this.cadCanvas::handleShapeMouseExited);
        return line;
    }

    private void updateLine(double endX, double endY) {
        if (this.stage.equals(LineDrawerStage.END)) {
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
        this.stage = LineDrawerStage.END;
        this.line = (javafx.scene.shape.Line) selectedShape;
        this.line.setStrokeWidth(1.0d);
        configureStroke(this.line, this.cadCanvas, this.cadCanvas.getShape().category());
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

enum LineDrawerStage {
    START,
    END
}
