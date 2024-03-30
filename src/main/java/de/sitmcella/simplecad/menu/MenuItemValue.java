package de.sitmcella.simplecad.menu;

public enum MenuItemValue {
    FILE_CREATE("Create"),
    FILE_OPEN("Open"),
    FILE_SAVE("Save"),
    FILE_CLOSE("Close");

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
            default -> FILE_CLOSE;
        };
    }
}
