package de.sitmcella.simplecad.menu;

import java.util.ArrayList;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ApplicationMenu {

    private final List<MenuItemListener> menuItemListeners;

    private final Stage stage;

    private static final Logger logger = LogManager.getLogger();

    private String parameter;

    public ApplicationMenu(MenuItemListener menuItemListener, Stage stage) {
        this.menuItemListeners = new ArrayList<>();
        this.stage = stage;
        menuItemListeners.add(menuItemListener);
        this.parameter = null;
    }

    public MenuBar create() {
        Menu menu = new Menu("File");
        MenuItem menuCreate = new MenuItem(MenuItemValue.FILE_CREATE.getText());
        MenuItem menuOpen = new MenuItem(MenuItemValue.FILE_OPEN.getText());
        MenuItem menuSave = new MenuItem(MenuItemValue.FILE_SAVE.getText());
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialFileName("cad_project.csv");
        fileChooser
                .getExtensionFilters()
                .addAll(new FileChooser.ExtensionFilter("All Files", "*.*"));
        MenuItem menuClose = new MenuItem(MenuItemValue.FILE_CLOSE.getText());

        EventHandler<ActionEvent> menuCreateEvent =
                event -> {
                    MenuItemValue menuItemValue =
                            MenuItemValue.getFromText(((MenuItem) event.getSource()).getText());
                    fireMenuItemEvent(
                            new MenuItemEvent(event.getSource(), menuItemValue, parameter));
                };

        EventHandler<ActionEvent> menuSaveEvent =
                event -> {
                    fileChooser.setTitle("Save Project");
                    fileChooser
                            .getExtensionFilters()
                            .addAll(new FileChooser.ExtensionFilter("csv files (*.csv)", "*.csv"));
                    parameter = fileChooser.showSaveDialog(stage).getAbsolutePath();
                    MenuItemValue menuItemValue =
                            MenuItemValue.getFromText(((MenuItem) event.getSource()).getText());
                    fireMenuItemEvent(
                            new MenuItemEvent(event.getSource(), menuItemValue, parameter));
                };

        EventHandler<ActionEvent> menuOpenEvent =
                event -> {
                    fileChooser.setTitle("Open Project");
                    fileChooser
                            .getExtensionFilters()
                            .addAll(new FileChooser.ExtensionFilter("csv files (*.csv)", "*.csv"));
                    parameter = fileChooser.showOpenDialog(stage).getAbsolutePath();
                    MenuItemValue menuItemValue =
                            MenuItemValue.getFromText(((MenuItem) event.getSource()).getText());
                    fireMenuItemEvent(
                            new MenuItemEvent(event.getSource(), menuItemValue, parameter));
                };

        EventHandler<ActionEvent> menuCloseEvent =
                event -> {
                    MenuItemValue menuItemValue =
                            MenuItemValue.getFromText(((MenuItem) event.getSource()).getText());
                    fireMenuItemEvent(
                            new MenuItemEvent(event.getSource(), menuItemValue, parameter));
                };

        menuCreate.setOnAction(menuCreateEvent);
        menuOpen.setOnAction(menuOpenEvent);
        menuSave.setOnAction(menuSaveEvent);
        menuClose.setOnAction(menuCloseEvent);

        menu.getItems().addAll(menuCreate, menuOpen, menuSave, menuClose);

        MenuBar menuBar = new MenuBar();

        menuBar.getMenus().add(menu);

        Menu projectMenu = new Menu("Project");
        MenuItem projectConfiguration = new MenuItem(MenuItemValue.PROJECT_CATEGORIES.getText());

        EventHandler<ActionEvent> menuSettingsEvent =
                event -> {
                    MenuItemValue menuItemValue =
                            MenuItemValue.getFromText(((MenuItem) event.getSource()).getText());
                    fireMenuItemEvent(
                            new MenuItemEvent(event.getSource(), menuItemValue, parameter));
                };

        projectConfiguration.setOnAction(menuSettingsEvent);

        MenuItem projectFilter = new MenuItem(MenuItemValue.PROJECT_FILTER.getText());

        EventHandler<ActionEvent> projectFilterEvent =
                event -> {
                    MenuItemValue menuItemValue =
                            MenuItemValue.getFromText(((MenuItem) event.getSource()).getText());
                    fireMenuItemEvent(
                            new MenuItemEvent(event.getSource(), menuItemValue, parameter));
                };

        projectFilter.setOnAction(projectFilterEvent);

        projectMenu.getItems().addAll(projectConfiguration, projectFilter);

        menuBar.getMenus().add(projectMenu);

        return menuBar;
    }

    private void fireMenuItemEvent(MenuItemEvent event) {
        for (MenuItemListener listener : menuItemListeners) {
            listener.menuItemSelected(event);
        }
    }
}
