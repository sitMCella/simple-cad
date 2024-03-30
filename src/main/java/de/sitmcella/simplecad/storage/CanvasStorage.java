package de.sitmcella.simplecad.storage;

import de.sitmcella.simplecad.CadCanvas;
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CanvasStorage {

    private static final Logger logger = LogManager.getLogger();

    private static final String COMMA_DELIMITER = ",";

    private final CadCanvas cadCanvas;

    public CanvasStorage(final CadCanvas cadCanvas) {
        this.cadCanvas = cadCanvas;
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
                                var line = (Line) shape;
                                String[] lineData =
                                        new String[] {
                                            ShapeType.LINE.toString(),
                                            String.valueOf(line.getStartX()),
                                            String.valueOf(line.getStartY()),
                                            String.valueOf(line.getEndX()),
                                            String.valueOf(line.getEndY())
                                        };
                                printWriter.println(convertToCSV(lineData));
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
                if (data.size() == 5 && data.get(0).equals(ShapeType.LINE.toString())) {
                    double startX = Double.parseDouble(data.get(1));
                    double startY = Double.parseDouble(data.get(2));
                    double endX = Double.parseDouble(data.get(3));
                    double endY = Double.parseDouble(data.get(4));
                    this.cadCanvas.addLineShape(startX, startY, endX, endY);
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
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
