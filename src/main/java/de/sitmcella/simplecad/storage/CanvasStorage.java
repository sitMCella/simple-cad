package de.sitmcella.simplecad.storage;

import de.sitmcella.simplecad.CadCanvas;
import de.sitmcella.simplecad.drawer.Curve;
import de.sitmcella.simplecad.property.ShapeType;
import java.io.File;
import java.io.FileNotFoundException;
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

import static de.sitmcella.simplecad.property.ShapeType.LINE;

public class CanvasStorage {

    private static final Logger logger = LogManager.getLogger();

    private static final String COMMA_DELIMITER = ",";

    private final CadCanvas cadCanvas;

    private final de.sitmcella.simplecad.drawer.Line line;

    private final de.sitmcella.simplecad.drawer.Curve curve;

    public CanvasStorage(final CadCanvas cadCanvas, final de.sitmcella.simplecad.drawer.Line line, de.sitmcella.simplecad.drawer.Curve curve) {
        this.cadCanvas = cadCanvas;
        this.line = line;
        this.curve = curve;
    }

    public void save() {
        File file = new File("canvas.csv");
        String tmpdir = System.getProperty("java.io.tmpdir");
        File tempFolder = new File(tmpdir);
        File newFile = new File(tempFolder, "canvas.csv");
        try (PrintWriter printWriter = new PrintWriter(newFile)) {
            var canvasSize = this.cadCanvas.getCanvasSize();
            String[] canvasData =
                    new String[] {
                        ShapeType.CANVAS.toString(),
                        String.valueOf(canvasSize.canvasWidth()),
                        String.valueOf(canvasSize.canvasHeight())
                    };
            printWriter.println(convertToCSV(canvasData));
            cadCanvas.getShapes().stream()
                    .forEach(
                            shape -> {
                                var shapeType = getShapeTypeFromShape(shape);
                                switch (shapeType) {
                                    case LINE -> {
                                        var line = (Line) shape;
                                        String[] lineData =
                                                new String[] {
                                                        LINE.toString(),
                                                        String.valueOf(line.getStartX()),
                                                        String.valueOf(line.getStartY()),
                                                        String.valueOf(line.getEndX()),
                                                        String.valueOf(line.getEndY())
                                                };
                                        printWriter.println(convertToCSV(lineData));
                                    }
                                    case CURVE -> {
                                        var curve = (QuadCurve) shape;
                                        String[] curveData =
                                                new String[] {
                                                        ShapeType.CURVE.toString(),
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
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void open() {
        File file = new File("canvas.csv");
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
            while (scanner.hasNextLine()) {
                var data = getRecordFromLine(scanner.nextLine());
                switch (ShapeType.from(data.get(0))) {
                    case LINE -> {
                        if (data.size() == 5) {
                            double startX = Double.parseDouble(data.get(1));
                            double startY = Double.parseDouble(data.get(2));
                            double endX = Double.parseDouble(data.get(3));
                            double endY = Double.parseDouble(data.get(4));
                            var shapes = this.line.createLineShape(startX, startY, endX, endY);
                            this.cadCanvas.addShapes(shapes);
                        }
                    }
                    case CURVE -> {
                        if (data.size() == 7) {
                            double startX = Double.parseDouble(data.get(1));
                            double startY = Double.parseDouble(data.get(2));
                            double controlX = Double.parseDouble(data.get(3));
                            double controlY = Double.parseDouble(data.get(4));
                            double endX = Double.parseDouble(data.get(5));
                            double endY = Double.parseDouble(data.get(6));
                            var shapes = this.curve.createCurveShape(startX, startY, controlX, controlY, endX, endY);
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
}
