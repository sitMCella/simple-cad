package de.sitmcella.simplecad.menu;

import java.util.EventListener;

public interface MenuItemListener extends EventListener {

    void menuItemSelected(MenuItemEvent menuItemEvent);
}
