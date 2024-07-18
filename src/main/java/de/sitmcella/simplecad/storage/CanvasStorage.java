package de.sitmcella.simplecad.storage;

import static de.sitmcella.simplecad.property.ShapeType.LINE;

import de.sitmcella.simplecad.CadCanvas;
import de.sitmcella.simplecad.CategoriesChangeEvent;
import de.sitmcella.simplecad.CategoriesChangeListener;
import de.sitmcella.simplecad.Category;
import de.sitmcella.simplecad.property.ShapeType;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javafx.scene.shape.Line;
import javafx.scene.shape.QuadCurve;
import javafx.scene.shape.Shape;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CanvasStorage {

    private static final Logger logger = LogManager.getLogger();

    private static final String COMMA_DELIMITER = ",";

    private final CadCanvas cadCanvas;

    private final de.sitmcella.simplecad.drawer.Line line;

    private final de.sitmcella.simplecad.drawer.Curve curve;

    private List<Category> categories;

    private final List<CategoriesChangeListener> categoriesChangeListeners;

    public CanvasStorage(
            final CadCanvas cadCanvas,
            final de.sitmcella.simplecad.drawer.Line line,
            de.sitmcella.simplecad.drawer.Curve curve,
            List<Category> categories,
            final CategoriesChangeListener categoriesChangeListener) {
        this.cadCanvas = cadCanvas;
        this.line = line;
        this.curve = curve;
        this.categories = categories;
        this.categoriesChangeListeners =
                new ArrayList<>() {
                    {
                        add(categoriesChangeListener);
                    }
                };
    }

    public void save(String filePath) {
        File file = new File(filePath);
        File newFile = null;
        try {
            newFile = File.createTempFile("cad", "csv");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try (PrintWriter printWriter = new PrintWriter(newFile)) {
            var canvasSize = this.cadCanvas.getCanvasSize();
            String[] canvasData =
                    new String[] {
                        ShapeType.CANVAS.toString(),
                        String.valueOf(canvasSize.canvasWidth()),
                        String.valueOf(canvasSize.canvasHeight())
                    };
            printWriter.println(convertToCSV(canvasData));
            if (this.categories.isEmpty()) {
                printWriter.println(convertToCSV(new String[] {"None"}));
            } else {
                var categoryEntries = this.categories.stream().map(Category::value).toList();
                String[] categoriesData = categoryEntries.toArray(String[]::new);
                printWriter.println(convertToCSV(categoriesData));
            }
            cadCanvas.getShapes().stream()
                    .forEach(
                            shape -> {
                                var shapeType = getShapeTypeFromShape(shape.shape());
                                switch (shapeType) {
                                    case LINE -> {
                                        var line = (Line) shape.shape();
                                        String[] lineData =
                                                new String[] {
                                                    LINE.toString(),
                                                    shape.category().value(),
                                                    String.valueOf(line.getStartX()),
                                                    String.valueOf(line.getStartY()),
                                                    String.valueOf(line.getEndX()),
                                                    String.valueOf(line.getEndY())
                                                };
                                        printWriter.println(convertToCSV(lineData));
                                    }
                                    case CURVE -> {
                                        var curve = (QuadCurve) shape.shape();
                                        String[] curveData =
                                                new String[] {
                                                    ShapeType.CURVE.toString(),
                                                    shape.category().value(),
                                                    String.valueOf(curve.getStartX()),
                                                    String.valueOf(curve.getStartY()),
                                                    String.valueOf(curve.getControlX()),
                                                    String.valueOf(curve.getControlY()),
                                                    String.valueOf(curve.getEndX()),
                                                    String.valueOf(curve.getEndY())
                                                };
                                        printWriter.println(convertToCSV(curveData));
                                    }
                                }
                            });
            file.delete();
            newFile.renameTo(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void open(String filePath) {
        File file = new File(filePath);
        try (Scanner scanner = new Scanner(file)) {
            if (!scanner.hasNextLine()) {
                logger.warn("The file is empty.");
                return;
            }
            var canvasData = getRecordFromLine(scanner.nextLine());
            if (canvasData.size() == 3 && canvasData.get(0).equals(ShapeType.CANVAS.toString())) {
                this.cadCanvas.clearCanvas();
                this.cadCanvas.setCanvasSize(
                        Double.parseDouble(canvasData.get(1)),
                        Double.parseDouble(canvasData.get(2)));
            } else {
                logger.warn("Cannot parse the file.");
                return;
            }
            var categoriesEntries = getRecordFromLine(scanner.nextLine());
            if (categoriesEntries.size() == 1 && categoriesEntries.contains("None")) {
                this.categories = new ArrayList<>();
            } else {
                this.categories = categoriesEntries.stream().map(Category::new).toList();
            }
            categoriesChangeListeners.forEach(
                    categoriesChangeListener ->
                            categoriesChangeListener.categoriesChanged(
                                    new CategoriesChangeEvent(this, this.categories)));
            while (scanner.hasNextLine()) {
                var data = getRecordFromLine(scanner.nextLine());
                switch (ShapeType.from(data.get(0))) {
                    case LINE -> {
                        if (data.size() == 6) {
                            String category = data.get(1);
                            double startX = Double.parseDouble(data.get(2));
                            double startY = Double.parseDouble(data.get(3));
                            double endX = Double.parseDouble(data.get(4));
                            double endY = Double.parseDouble(data.get(5));
                            this.line.setCategories(this.categories);
                            var shapes =
                                    this.line.createLineShape(startX, startY, endX, endY, category);
                            this.cadCanvas.addShapes(shapes);
                        }
                    }
                    case CURVE -> {
                        if (data.size() == 8) {
                            String category = data.get(1);
                            double startX = Double.parseDouble(data.get(2));
                            double startY = Double.parseDouble(data.get(3));
                            double controlX = Double.parseDouble(data.get(4));
                            double controlY = Double.parseDouble(data.get(5));
                            double endX = Double.parseDouble(data.get(6));
                            double endY = Double.parseDouble(data.get(7));
                            this.curve.setCategories(this.categories);
                            var shapes =
                                    this.curve.createCurveShape(
                                            startX, startY, controlX, controlY, endX, endY,
                                            category);
                            this.cadCanvas.addShapes(shapes);
                        }
                    }
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private ShapeType getShapeTypeFromShape(Shape shape) {
        return ShapeType.fromClass(shape);
    }

    private String convertToCSV(String[] data) {
        return Stream.of(data)
                .map(this::escapeSpecialCharacters)
                .collect(Collectors.joining(COMMA_DELIMITER));
    }

    private String escapeSpecialCharacters(String data) {
        if (data == null) {
            throw new IllegalArgumentException("Input data cannot be null");
        }
        String escapedData = data.replaceAll("\\R", " ");
        if (data.contains(",") || data.contains("\"") || data.contains("'")) {
            data = data.replace("\"", "\"\"");
            escapedData = "\"" + data + "\"";
        }
        return escapedData;
    }

    private List<String> getRecordFromLine(String line) {
        List<String> values = new ArrayList<>();
        try (Scanner rowScanner = new Scanner(line)) {
            rowScanner.useDelimiter(COMMA_DELIMITER);
            while (rowScanner.hasNext()) {
                values.add(rowScanner.next());
            }
        }
        return values;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }
}
