package de.sitmcella.simplecad.category;

import de.sitmcella.simplecad.property.DropDownSection;
import de.sitmcella.simplecad.property.PropertiesUtility;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign2.MaterialDesignC;
import org.kordamp.ikonli.materialdesign2.MaterialDesignT;

public class ProjectCategories {

    private static final FontIcon TRASH_ICON = new FontIcon(MaterialDesignT.TRASH_CAN);

    private static final FontIcon CLOSE_ICON = new FontIcon(MaterialDesignC.CLOSE);

    private final Pane rightPanelSection;

    private List<Category> categories;

    private final PropertiesUtility propertiesUtility;

    private TabPane tabPane;

    private final List<CategoriesChangeListener> categoriesChangeListeners;

    public ProjectCategories(
            final Pane rightPanelSection,
            List<Category> categories,
            final CategoriesChangeListener categoriesChangeListener) {
        this.rightPanelSection = rightPanelSection;
        this.categories = new ArrayList<>();
        categories.forEach(c -> this.categories.add(new Category(c.value())));
        this.propertiesUtility = new PropertiesUtility();
        for (Node child : rightPanelSection.getChildren()) {
            this.tabPane = (TabPane) child;
        }
        this.categoriesChangeListeners =
                new ArrayList<>() {
                    {
                        add(categoriesChangeListener);
                    }
                };
    }

    public void open() {
        Pane projectConfigurationSection = new Pane();
        projectConfigurationSection.getStyleClass().add("project-configuration-section");
        Tab projectConfigurationTab = new Tab("Configuration", projectConfigurationSection);
        projectConfigurationTab.setClosable(false);
        Tab originalTab = tabPane.getTabs().get(0);
        tabPane.getTabs().removeAll(originalTab);
        tabPane.getTabs().add(projectConfigurationTab);

        Button closeButton = new Button();
        closeButton.setGraphic(CLOSE_ICON);
        closeButton.setId("close-button");
        HBox configurationHeader = new HBox(closeButton);
        configurationHeader.setMinWidth(185);
        configurationHeader.setMaxWidth(185);
        configurationHeader.setSpacing(10);
        configurationHeader.setAlignment(Pos.TOP_RIGHT);
        rightPanelSection.getChildren().add(configurationHeader);
        closeButton.setOnMouseClicked(
                (e) -> {
                    tabPane.getTabs().removeAll(projectConfigurationTab);
                    tabPane.getTabs().add(originalTab);
                    rightPanelSection.getChildren().remove(configurationHeader);
                });

        Label title = new Label("Categories");
        title.setFont(Font.font("Verdana", FontWeight.BOLD, 12));

        List<String> categoryValues = categories.stream().map(Category::value).toList();
        DropDownSection existentCategories = propertiesUtility.addDropdownSection(categoryValues);

        Label selectedCategoryTitle = new Label("Selected Category");
        selectedCategoryTitle.setFont(Font.font("Verdana", FontWeight.BOLD, 12));

        TextField selectedCategory = new TextField("");
        selectedCategory.setMinWidth(100);
        selectedCategory.setMaxWidth(100);

        existentCategories
                .getComboBox()
                .valueProperty()
                .addListener(
                        (ChangeListener<String>)
                                (observableValue, previousValue, newValue) -> {
                                    selectedCategory.setText(newValue);
                                });

        selectedCategory.setOnKeyPressed(
                event -> {
                    if (event.getCode() == KeyCode.ENTER) {
                        var newCategoryValue = selectedCategory.getText();
                        if(this.categories.contains(new Category(newCategoryValue))) {
                            return;
                        }
                        var selectedCategoryValue = (String)existentCategories.getComboBox().getValue();
                        List<Category> newCategories = new ArrayList<>();
                        this.categories.stream().forEach(c -> newCategories.add(c));
                        newCategories.remove(new Category(selectedCategoryValue));
                        newCategories.add(new Category(newCategoryValue));
                        categoriesChangeListeners.forEach(
                                categoriesChangeListener ->
                                        categoriesChangeListener.categoriesChanged(
                                                new CategoriesChangeEvent(this, newCategories)));
                        existentCategories.getComboBox().getItems().remove(selectedCategoryValue);
                        existentCategories.getComboBox().getItems().add(newCategoryValue);
                    }
                });

        Button tashButton = new Button();
        tashButton.setGraphic(TRASH_ICON);
        tashButton.setId("trash-button");
        tashButton.setOnMouseClicked(
                (e) -> {
                    if(!selectedCategory.getText().equals(existentCategories.getComboBox().getValue())) {
                        return;
                    }
                    List<Category> updatedCategories = new ArrayList<>();
                    this.categories.stream().forEach(c -> updatedCategories.add(new Category(c.value())));
                    updatedCategories.remove(new Category(selectedCategory.getText()));
                    categoriesChangeListeners.forEach(
                            categoriesChangeListener ->
                                    categoriesChangeListener.categoriesChanged(
                                            new CategoriesChangeEvent(this, updatedCategories)));
                    existentCategories.getComboBox().getItems().remove(selectedCategory.getText());
                    selectedCategory.setText("");
                });
        HBox hBox = new HBox(selectedCategory, tashButton);
        hBox.setMinWidth(50);
        hBox.setMaxWidth(50);
        hBox.setSpacing(10);
        hBox.setAlignment(Pos.CENTER_LEFT);

        Label addCategoryTitle = new Label("Add Category");
        addCategoryTitle.setFont(Font.font("Verdana", FontWeight.BOLD, 12));

        TextField addCategory = new TextField("");
        addCategory.setMinWidth(100);
        addCategory.setMaxWidth(100);

        addCategory.setOnKeyPressed(
                event -> {
                    if (event.getCode() == KeyCode.ENTER) {
                        var newCategoryValue = addCategory.getText();
                        if(this.categories.contains(new Category(newCategoryValue)) || Categories.isNone(newCategoryValue)) {
                            return;
                        }
                        List<Category> newCategories = new ArrayList<>();
                        this.categories.stream().forEach(c -> newCategories.add(c));
                        newCategories.add(new Category(newCategoryValue));
                        categoriesChangeListeners.forEach(
                                categoriesChangeListener ->
                                        categoriesChangeListener.categoriesChanged(
                                                new CategoriesChangeEvent(this, newCategories)));
                        existentCategories.getComboBox().getItems().add(newCategoryValue);
                        addCategory.setText("");
                    }
                });

        VBox vBox =
                new VBox(
                        title,
                        existentCategories.getParent(),
                        selectedCategoryTitle,
                        hBox,
                        addCategoryTitle,
                        addCategory);
        vBox.setSpacing(5);
        vBox.getStyleClass().add("canvas-properties-section");
        projectConfigurationTab.setContent(vBox);
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }
}
