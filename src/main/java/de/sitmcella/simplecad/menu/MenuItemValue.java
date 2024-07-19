package de.sitmcella.simplecad.menu;

public enum MenuItemValue {
    FILE_CREATE("Create"),
    FILE_OPEN("Open"),
    FILE_SAVE("Save"),
    FILE_CLOSE("Close"),
    PROJECT_CATEGORIES("Categories"),
    PROJECT_FILTER("Filter");

    private final String text;

    MenuItemValue(final String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }

    public static MenuItemValue getFromText(String text) {
        return switch (text) {
            case "Create" -> FILE_CREATE;
            case "Open" -> FILE_OPEN;
            case "Save" -> FILE_SAVE;
            case "Close" -> FILE_CLOSE;
            case "Categories" -> PROJECT_CATEGORIES;
            case "Filter" -> PROJECT_FILTER;
            default -> FILE_CLOSE;
        };
    }
}
