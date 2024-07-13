package de.sitmcella.simplecad;

import de.sitmcella.simplecad.drawer.Curve;
import de.sitmcella.simplecad.drawer.DrawActions;
import de.sitmcella.simplecad.drawer.DrawerProperties;
import de.sitmcella.simplecad.drawer.Line;
import de.sitmcella.simplecad.drawer.PropertiesChangeEvent;
import de.sitmcella.simplecad.drawer.Select;
import de.sitmcella.simplecad.drawer.Shape;
import de.sitmcella.simplecad.drawer.ShapeDrawer;
import de.sitmcella.simplecad.drawer.ShapeDrawerChangeEvent;
import de.sitmcella.simplecad.drawer.ShapeDrawerEvent;
import de.sitmcella.simplecad.drawer.ShapeDrawerListener;
import de.sitmcella.simplecad.drawer.Shapes;
import de.sitmcella.simplecad.menu.MenuItemEvent;
import de.sitmcella.simplecad.menu.MenuItemListener;
import de.sitmcella.simplecad.operation.Cut;
import de.sitmcella.simplecad.operation.Duplicate;
import de.sitmcella.simplecad.operation.Operation;
import de.sitmcella.simplecad.operation.OperationAction;
import de.sitmcella.simplecad.operation.OperationChangedEvent;
import de.sitmcella.simplecad.operation.OperationListener;
import de.sitmcella.simplecad.operation.Point;
import de.sitmcella.simplecad.property.CadProperties;
import de.sitmcella.simplecad.property.CanvasPropertyChangeEvent;
import de.sitmcella.simplecad.property.CanvasPropertyListener;
import de.sitmcella.simplecad.property.CanvasSizeProperty;
import de.sitmcella.simplecad.property.CurveProperty;
import de.sitmcella.simplecad.property.LineProperty;
import de.sitmcella.simplecad.property.ShapeType;
import de.sitmcella.simplecad.storage.CanvasStorage;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CadProject
        implements MenuItemListener,
                ShapeDrawerListener,
                OperationListener,
                CanvasPropertyListener {

    private static final Logger logger = LogManager.getLogger();

    private final Pane pane;

    private final CadProperties cadProperties;

    private final CadCanvas cadCanvas;

    private final Select select;

    private final Line line;

    private final Curve curve;

    private final List<Shape> shapeDrawers;

    private final Duplicate duplicate;

    private final Cut cut;

    private final Point point;

    private final List<Operation> operations;

    private DrawerProperties drawerProperties;

    private final CanvasStorage canvasStorage;

    public CadProject(final Pane pane, final CadProperties cadProperties) {
        this.pane = pane;
        this.cadProperties = cadProperties;
        this.drawerProperties =
                new DrawerProperties(DrawActions.SELECT, OperationAction.NULL, false);
        this.cadCanvas = new CadCanvas(pane, drawerProperties);
        this.cadProperties.addListener(this);
        this.select = new Select(this.cadCanvas, this);
        this.line = new Line(this.cadCanvas, this);
        this.curve = new Curve(this.cadCanvas, this);
        this.shapeDrawers =
                new ArrayList<>() {
                    {
                        add(select);
                        add(line);
                        add(curve);
                    }
                };
        this.duplicate = new Duplicate(this.cadCanvas, this);
        this.cut = new Cut(this.cadCanvas, this);
        this.point = new Point(this.cadCanvas, this);
        this.operations =
                new ArrayList<>() {
                    {
                        add(duplicate);
                        add(cut);
                        add(point);
                    }
                };
        this.canvasStorage = new CanvasStorage(cadCanvas, line);
    }

    public void configureEventListeners(VBox root) {
        this.pane.setOnMouseClicked(this::handleMouseClick);
        this.pane.setOnMouseMoved(this::handleMouseMove);
        root.setOnKeyPressed(this::handleKeyPressed);
        root.setOnKeyReleased(this::handleKeyReleased);
        root.setFocusTraversable(true);
    }

    @Override
    public void menuItemSelected(MenuItemEvent menuItemEvent) {
        switch (menuItemEvent.getMenuItemValue()) {
            case FILE_CREATE -> createCadProject();
            case FILE_OPEN -> canvasStorage.open();
            case FILE_SAVE -> canvasStorage.save();
            case FILE_CLOSE -> Platform.exit();
            default -> logger.info("Unknown MenuItemEvent");
        }
    }

    @Override
    public void shapeDrawerChanged(ShapeDrawerChangeEvent shapeDrawerChangeEvent) {
        this.drawerProperties =
                new DrawerProperties(
                        shapeDrawerChangeEvent.getDrawAction(),
                        this.drawerProperties.operationAction(),
                        this.drawerProperties.constraintAngles());
        this.cadCanvas.setProperties(drawerProperties);
        var selectedButton =
                (ToggleButton) ((MouseEvent) shapeDrawerChangeEvent.getSource()).getSource();
        shapeDrawers.stream()
                .filter(shapeDrawer -> shapeDrawer.getButton() != selectedButton)
                .forEach(shapeDrawer -> shapeDrawer.getButton().setSelected(false));
        selectedButton.setSelected(true);
    }

    @Override
    public void shapeDrawer(ShapeDrawerEvent shapeDrawerEvent) {
        switch (this.drawerProperties.drawAction()) {
            case SELECT -> {
                if (cadCanvas.selectedShape != null) {
                    var event = (MouseEvent) shapeDrawerEvent.getSource();
                    var closestPoint = this.cadCanvas.selectClosePoint(event, true);
                    if (closestPoint != null) {
                        this.cadCanvas.cleanPoints(closestPoint);
                        switch (getShapeTypeFromSelectedShape()) {
                            case LINE -> moveShapePoint(DrawActions.LINE_DRAW, closestPoint);
                            case CURVE -> moveShapePoint(DrawActions.CURVE_DRAW, closestPoint);
                        }
                        cadCanvas.selectedShape = null;
                        this.cadCanvas.setProperties(drawerProperties);
                    } else {
                        cadCanvas.hoverShape = null;
                        cadCanvas.selectShape(event);
                        this.cadProperties.addConfiguration(ShapeType.CANVAS, null);
                    }
                } else {
                    this.cadCanvas.selectShape((MouseEvent) shapeDrawerEvent.getSource());
                    if (this.cadCanvas.selectedShape != null) {
                        ShapeType shapeType = getShapeTypeFromSelectedShape();
                        this.cadProperties.addConfiguration(shapeType, cadCanvas.selectedShape);
                    } else {
                        this.cadProperties.addConfiguration(ShapeType.CANVAS, null);
                    }
                }
            }
        }
    }

    private void moveShapePoint(DrawActions drawAction, CanvasPoint closestPoint) {
        var shape = getShapeDrawer(drawAction).use(cadCanvas.selectedShape, closestPoint.circle());
        cadCanvas.recreatePoint(closestPoint, shape);
        this.drawerProperties =
                new DrawerProperties(
                        drawAction,
                        this.drawerProperties.operationAction(),
                        this.drawerProperties.constraintAngles());
        shapeDrawers.stream()
                .forEach(
                        shapeDrawer -> {
                            var selected = shapeDrawer.getDrawAction() == drawAction;
                            shapeDrawer.getButton().setSelected(selected);
                        });
    }

    @Override
    public void operationsChanged(OperationChangedEvent operationChangedEvent) {
        this.drawerProperties =
                new DrawerProperties(
                        this.drawerProperties.drawAction(),
                        operationChangedEvent.getOperationAction(),
                        this.drawerProperties.constraintAngles());
        this.cadCanvas.setProperties(drawerProperties);
        if (this.cadCanvas.selectedShape == null) {
            this.cadCanvas.triggerOperation(select);
            return;
        }
        ShapeDrawer shape = getShapeDrawerFromSelectedShape();
        this.cadCanvas.triggerOperation(shape);
    }

    @Override
    public void canvasPropertyChanged(CanvasPropertyChangeEvent canvasPropertyChangeEvent) {
        var canvasProperty = canvasPropertyChangeEvent.getCanvasProperty();
        switch (canvasProperty.getShapeType()) {
            case CANVAS -> {
                var canvasSizeProperty = (CanvasSizeProperty) canvasProperty.getProperty();
                this.cadCanvas.setCanvasSize(
                        canvasSizeProperty.canvasWidth(), canvasSizeProperty.canvasHeight());
            }
            case LINE -> {
                if (this.cadCanvas.selectedShape == null) {
                    return;
                }
                var lineProperty = (LineProperty) canvasProperty.getProperty();
                var line = (javafx.scene.shape.Line) this.cadCanvas.selectedShape;
                this.line.update(line, lineProperty);
            }
            case CURVE -> {
                if (this.cadCanvas.selectedShape == null) {
                    return;
                }
                var curveProperty = (CurveProperty) canvasProperty.getProperty();
                var curve = (javafx.scene.shape.QuadCurve) this.cadCanvas.selectedShape;
                this.curve.update(curve, curveProperty);
            }
        }
    }

    private ShapeDrawer getShapeDrawerFromSelectedShape() {
        return switch (ShapeType.fromClass(cadCanvas.selectedShape)) {
            case LINE -> line;
            case CURVE -> curve;
            default -> select;
        };
    }

    private ShapeType getShapeTypeFromSelectedShape() {
        return ShapeType.fromClass(cadCanvas.selectedShape);
    }

    public List<Shape> getShapeDrawers() {
        return shapeDrawers;
    }

    public List<Button> getOperationButtons() {
        return operations.stream().map(Operation::getButton).collect(Collectors.toList());
    }

    private void createCadProject() {
        this.cadCanvas.clearCanvas();
    }

    private void handleMouseClick(MouseEvent event) {
        ShapeDrawer shapeDrawer = getShapeDrawer();
        Shapes shapes = shapeDrawer.handleMouseClick(event);
        this.cadCanvas.addShapes(shapes);
        if (shapes != null && !shapes.shapes().isEmpty()) {
            this.cadProperties.addConfiguration(ShapeType.CANVAS, null);
        }
    }

    private void handleMouseMove(MouseEvent event) {
        getShapeDrawer().handleMouseMove(event);
    }

    private void handleKeyPressed(KeyEvent event) {
        if (!this.drawerProperties.constraintAngles() && event.isShiftDown()) {
            this.drawerProperties =
                    new DrawerProperties(
                            this.drawerProperties.drawAction(),
                            this.drawerProperties.operationAction(),
                            true);
            firePropertiesChangeEvent(event, drawerProperties);
        }
    }

    private void handleKeyReleased(KeyEvent event) {
        if (this.drawerProperties.constraintAngles() && event.getCode() == KeyCode.SHIFT) {
            this.drawerProperties =
                    new DrawerProperties(
                            this.drawerProperties.drawAction(),
                            this.drawerProperties.operationAction(),
                            false);
            firePropertiesChangeEvent(event, drawerProperties);
        }
    }

    private ShapeDrawer getShapeDrawer() {
        return switch (this.drawerProperties.drawAction()) {
            case LINE_DRAW -> this.line;
            case CURVE_DRAW -> this.curve;
            default -> this.select;
        };
    }

    private ShapeDrawer getShapeDrawer(DrawActions drawAction) {
        return switch (drawAction) {
            case LINE_DRAW -> this.line;
            case CURVE_DRAW -> this.curve;
            default -> this.select;
        };
    }

    private void firePropertiesChangeEvent(KeyEvent event, DrawerProperties drawerProperties) {
        for (Shape shape : shapeDrawers) {
            shape.propertyChanged(new PropertiesChangeEvent(event, drawerProperties));
        }
        this.cadCanvas.setProperties(drawerProperties);
    }
}
