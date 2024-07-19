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
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign2.MaterialDesignC;

public class ProjectFilter {

    private static final FontIcon CLOSE_ICON = new FontIcon(MaterialDesignC.CLOSE);

    private final Pane rightPanelSection;

    private List<Category> categories;

    private final PropertiesUtility propertiesUtility;

    private TabPane tabPane;

    private final List<FilterListener> filterListeners;

    public ProjectFilter(
            final Pane rightPanelSection,
            List<Category> categories,
            FilterListener filterListener) {
        this.propertiesUtility = new PropertiesUtility();
        this.rightPanelSection = rightPanelSection;
        this.categories = new ArrayList<>();
        categories.forEach(c -> this.categories.add(new Category(c.value())));
        for (Node child : rightPanelSection.getChildren()) {
            this.tabPane = (TabPane) child;
        }
        this.filterListeners =
                new ArrayList<>() {
                    {
                        add(filterListener);
                    }
                };
    }

    public void open(Category selectedCategory) {
        Pane projectFilterSection = new Pane();
        projectFilterSection.getStyleClass().add("project-filter-section");
        Tab projectFilterTab = new Tab("Filter", projectFilterSection);
        projectFilterTab.setClosable(false);
        Tab originalTab = tabPane.getTabs().get(0);
        tabPane.getTabs().removeAll(originalTab);
        tabPane.getTabs().add(projectFilterTab);

        Button closeButton = new Button();
        closeButton.setGraphic(CLOSE_ICON);
        closeButton.setId("close-button");
        HBox filterHeader = new HBox(closeButton);
        filterHeader.setMinWidth(185);
        filterHeader.setMaxWidth(185);
        filterHeader.setSpacing(10);
        filterHeader.setAlignment(Pos.TOP_RIGHT);
        rightPanelSection.getChildren().add(filterHeader);
        closeButton.setOnMouseClicked(
                (e) -> {
                    tabPane.getTabs().removeAll(projectFilterTab);
                    tabPane.getTabs().add(originalTab);
                    rightPanelSection.getChildren().remove(filterHeader);
                });

        Label title = new Label("Category");
        title.setFont(Font.font("Verdana", FontWeight.BOLD, 12));

        List<String> categoryValues = categories.stream().map(Category::value).toList();
        DropDownSection existentCategories = propertiesUtility.addDropdownSection(categoryValues);

        if (selectedCategory != null) {
            existentCategories.getComboBox().setValue(selectedCategory.value());
        }

        existentCategories
                .getComboBox()
                .valueProperty()
                .addListener(
                        (ChangeListener<String>)
                                (observableValue, previousValue, newValue) -> {
                                    filterListeners.forEach(
                                            filterListener ->
                                                    filterListener.filter(
                                                            new FilterEvent(
                                                                    this, new Category(newValue))));
                                });

        Button resetFilterButton = new Button();
        resetFilterButton.setText("Reset");
        resetFilterButton.setOnMouseClicked(
                (e) -> {
                    existentCategories.getComboBox().setValue(null);
                    filterListeners.forEach(
                            filterListener -> filterListener.filter(new FilterEvent(this, null)));
                });

        VBox vBox = new VBox(title, existentCategories.getParent(), resetFilterButton);
        vBox.setSpacing(5);
        vBox.getStyleClass().add("canvas-properties-section");
        projectFilterTab.setContent(vBox);
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }
}
