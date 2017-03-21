package com.prolificinteractive.materialcalendarview;

import java.util.List;

/**
 * Decorate Day views with drawables and text manipulation
 */
public interface DayViewDecorator {

    /**
     * Determine if a specific day should be decorated
     *
     * @param day {@linkplain CalendarDay} to possibly decorate
     * @return true if this decorator should be applied to the provided day
     */
    boolean shouldDecorate(CalendarDay day);

    /**
     * Set decoration options onto a facade to be applied to all relevant days
     *
     * @param data The data associated with this decorator
     * @param view View to decorate
     */
    void decorate(List<Object> data, DayViewFacade view);

    /**
     * Get the data associated with this decorator
     */
    List<Object> getData();
}
