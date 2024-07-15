package de.sitmcella.simplecad.drawer;

import de.sitmcella.simplecad.CadShape;
import java.util.List;

public record Shapes(List<CadShape> shapes, javafx.scene.shape.Shape mainShape) {}
