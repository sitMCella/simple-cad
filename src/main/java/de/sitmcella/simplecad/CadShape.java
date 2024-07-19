package de.sitmcella.simplecad;

import de.sitmcella.simplecad.category.Category;
import javafx.scene.shape.Shape;

public record CadShape(Shape shape, Category category) {}
