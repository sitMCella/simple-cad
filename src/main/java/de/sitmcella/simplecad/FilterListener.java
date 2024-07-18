package de.sitmcella.simplecad;

import java.util.EventListener;

public interface FilterListener extends EventListener {

    void filter(FilterEvent filterEvent);
}
