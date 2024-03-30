package de.sitmcella.simplecad.property;

import java.util.EventListener;

public interface CanvasPropertyListener extends EventListener {

    void canvasPropertyChanged(CanvasPropertyChangeEvent canvasPropertyChangeEvent);
}
