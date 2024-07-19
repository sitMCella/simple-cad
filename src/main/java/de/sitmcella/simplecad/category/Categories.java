package de.sitmcella.simplecad.category;

import java.util.List;

public enum Categories {
    NONE("None");

    private final String text;

    Categories(final String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }

    public static boolean isNone(String categoryValue) {
        return NONE.getText().equals(categoryValue);
    }

    public static String getCategoryValue(Category category) {
        return category != null ? category.value() : NONE.getText();
    }

    public static boolean isNone(List<String> categories) {
        return categories.size() == 1 && categories.contains(NONE.getText());
    }
}
