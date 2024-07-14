package de.sitmcella.simplecad;

import atlantafx.base.theme.PrimerDark;
import de.sitmcella.simplecad.menu.ApplicationMenu;
import de.sitmcella.simplecad.property.CadProperties;
import de.sitmcella.simplecad.property.CanvasSizeProperty;
import java.util.Objects;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class App extends Application {

    private static final Logger logger = LogManager.getLogger();

    private double stageWidth;

    private double stageHeight;

    @Override
    public void start(Stage stage) {
        var javaVersion = SystemInfo.javaVersion();
        var javafxVersion = SystemInfo.javafxVersion();
        logger.info("Java version: " + javaVersion + " - JavaFX version: " + javafxVersion);
        stage.initStyle(StageStyle.DECORATED);
        stage.setTitle("Simple CAD");

        Pane canvasSection = new Pane();
        canvasSection.setMinSize(2000, 1900);
        canvasSection.getStyleClass().add("canvas-section");

        ScrollPane canvasSectionOuter = new ScrollPane();
        canvasSectionOuter.setContent(canvasSection);
        canvasSectionOuter.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        canvasSectionOuter.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        canvasSectionOuter.setPannable(false);
        canvasSectionOuter.getStyleClass().add("canvas-section-outer");

        Line measure = new Line(0, 5, 100, 5);
        measure.setStrokeWidth(1.0);
        measure.setStroke(Color.BLACK);
        Line measureBorderLeft = new Line(0, 5, 0, 10);
        measureBorderLeft.setStrokeWidth(1.0);
        measureBorderLeft.setStroke(Color.BLACK);
        Line measureBorderRight = new Line(0, 5, 0, 10);
        measureBorderRight.setStrokeWidth(1.0);
        measureBorderRight.setStroke(Color.BLACK);

        Label measureLabel = new Label("10 meters");
        measureLabel.setTextFill(Color.BLACK);
        AnchorPane bottomSection =
                new AnchorPane(measureBorderLeft, measure, measureBorderRight, measureLabel);
        bottomSection.setMinHeight(30);
        bottomSection.getStyleClass().add("bottom-section");
        AnchorPane.setRightAnchor(measureBorderLeft, 125.0);
        AnchorPane.setRightAnchor(measureBorderRight, 25.0);
        AnchorPane.setRightAnchor(measure, 25.0);
        AnchorPane.setRightAnchor(measureLabel, 50.0);
        AnchorPane.setBottomAnchor(measure, 22.5);
        AnchorPane.setBottomAnchor(measureLabel, 5.0);

        TabPane tabPane = new TabPane();
        Pane propertiesSection = new Pane();
        propertiesSection.getStyleClass().add("properties-section");
        Tab propertiesTab = new Tab("Properties", propertiesSection);
        propertiesTab.setClosable(false);
        tabPane.getTabs().add(propertiesTab);
        Pane rightPanelSection = new Pane(tabPane);

        CadProperties cadProperties =
                new CadProperties(propertiesTab, new CanvasSizeProperty(2000, 1900));
        CadProject cadProject = new CadProject(canvasSection, cadProperties);

        ApplicationMenu applicationMenu = new ApplicationMenu(cadProject, stage);
        MenuBar menuBar = applicationMenu.create();

        var operationButtons = cadProject.getOperationButtons();
        HBox operationsBar = new HBox();
        operationsBar.getStyleClass().add("operations-bar");
        operationButtons.forEach(button -> operationsBar.getChildren().add(button));
        for (Node child : operationsBar.getChildren()) {
            HBox.setHgrow(child, Priority.ALWAYS);
        }

        var shapeDrawers = cadProject.getShapeDrawers();
        VBox leftBar = new VBox();
        leftBar.getStyleClass().add("left-bar");
        shapeDrawers.forEach(shapeDrawer -> leftBar.getChildren().add(shapeDrawer.getButton()));
        for (Node child : leftBar.getChildren()) {
            VBox.setVgrow(child, Priority.ALWAYS);
        }

        VBox drawerSection = new VBox(operationsBar, canvasSectionOuter, bottomSection);

        HBox hbox = new HBox(leftBar, drawerSection, rightPanelSection);
        hbox.getStyleClass().add("content-section");
        hbox.setSpacing(5);
        VBox vbox = new VBox(menuBar, hbox);
        cadProject.configureEventListeners(vbox);
        Group root = new Group(vbox);
        root.getStyleClass().add("scene-section");

        var scene = new Scene(root, 600, 500);
        scene.getStylesheets()
                .add(
                        Objects.requireNonNull(getClass().getResource("/stylesheet.css"))
                                .toExternalForm());
        stage.setScene(scene);
        stage.getIcons()
                .add(
                        new Image(
                                Objects.requireNonNull(getClass().getResource("/favicon-32x32.png"))
                                        .toExternalForm()));
        Application.setUserAgentStylesheet(new PrimerDark().getUserAgentStylesheet());
        stage.show();
        stageWidth = stage.getWidth();
        stageHeight = stage.getHeight();
        resizeCanvas(
                stageWidth,
                stageHeight,
                canvasSectionOuter,
                leftBar,
                menuBar,
                operationsBar,
                tabPane);

        stage.widthProperty()
                .addListener(
                        (obs, oldVal, newVal) -> {
                            stageWidth = newVal.doubleValue();
                            resizeCanvas(
                                    stageWidth,
                                    stageHeight,
                                    canvasSectionOuter,
                                    leftBar,
                                    menuBar,
                                    operationsBar,
                                    tabPane);
                        });

        stage.heightProperty()
                .addListener(
                        (obs, oldVal, newVal) -> {
                            stageHeight = newVal.doubleValue();
                            resizeCanvas(
                                    stageWidth,
                                    stageHeight,
                                    canvasSectionOuter,
                                    leftBar,
                                    menuBar,
                                    operationsBar,
                                    tabPane);
                        });
    }

    private void resizeCanvas(
            double stageWidth,
            double stageHeight,
            ScrollPane canvasSectionOuter,
            VBox leftBar,
            MenuBar menuBar,
            HBox operationsBar,
            TabPane tabPane) {
        canvasSectionOuter.setMinSize(
                stageWidth - leftBar.getWidth() - 5 - 200,
                stageHeight - menuBar.getHeight() - operationsBar.getHeight() - 33 - 30);
        canvasSectionOuter.setMaxSize(
                stageWidth - leftBar.getWidth() - 5 - 200,
                stageHeight - menuBar.getHeight() - operationsBar.getHeight() - 33);
        tabPane.setMinSize(195, stageHeight - menuBar.getHeight() - operationsBar.getHeight());
    }

    public static void main(String[] args) {
        launch();
    }
}
