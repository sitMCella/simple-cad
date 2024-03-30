package de.sitmcella.simplecad;

import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

public record CanvasPoint(double x, double y, Circle circle, Shape mainShape) {}
