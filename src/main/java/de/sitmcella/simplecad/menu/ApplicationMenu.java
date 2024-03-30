package de.sitmcella.simplecad.menu;

import java.util.ArrayList;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ApplicationMenu {

    private final List<MenuItemListener> menuItemListeners;

    private static final Logger logger = LogManager.getLogger();

    public ApplicationMenu(MenuItemListener menuItemListener) {
        this.menuItemListeners = new ArrayList<>();
        menuItemListeners.add(menuItemListener);
    }

    public MenuBar create() {
        Menu menu = new Menu("File");

        MenuItem menuCreate = new MenuItem(MenuItemValue.FILE_CREATE.getText());
        MenuItem menuOpen = new MenuItem(MenuItemValue.FILE_OPEN.getText());
        MenuItem menuSave = new MenuItem(MenuItemValue.FILE_SAVE.getText());
        MenuItem menuClose = new MenuItem(MenuItemValue.FILE_CLOSE.getText());

        EventHandler<ActionEvent> menuCreateEvent =
                event -> {
                    MenuItemValue menuItemValue =
                            MenuItemValue.getFromText(((MenuItem) event.getSource()).getText());
                    fireMenuItemEvent(new MenuItemEvent(event.getSource(), menuItemValue));
                };

        menuCreate.setOnAction(menuCreateEvent);
        menuOpen.setOnAction(menuCreateEvent);
        menuSave.setOnAction(menuCreateEvent);
        menuClose.setOnAction(menuCreateEvent);

        menu.getItems().addAll(menuCreate, menuOpen, menuSave, menuClose);

        MenuBar menuBar = new MenuBar();

        menuBar.getMenus().add(menu);

        return menuBar;
    }

    private void fireMenuItemEvent(MenuItemEvent event) {
        for (MenuItemListener listener : menuItemListeners) {
            listener.menuItemSelected(event);
        }
    }
}
