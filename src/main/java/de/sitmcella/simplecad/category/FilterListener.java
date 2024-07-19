package de.sitmcella.simplecad.category;

import java.util.EventListener;

public interface FilterListener extends EventListener {

    void filter(FilterEvent filterEvent);
}
