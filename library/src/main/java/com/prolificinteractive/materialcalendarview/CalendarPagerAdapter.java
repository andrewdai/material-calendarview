package com.prolificinteractive.materialcalendarview;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.prolificinteractive.materialcalendarview.MaterialCalendarView.ShowOtherDates;
import com.prolificinteractive.materialcalendarview.format.DayFormatter;
import com.prolificinteractive.materialcalendarview.format.TitleFormatter;
import com.prolificinteractive.materialcalendarview.format.WeekDayFormatter;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Pager adapter backing the calendar view
 */
abstract class CalendarPagerAdapter<V extends CalendarPagerView> extends PagerAdapter {
    private static final String TAG = "CalendarPagerAdapter";

    private final ArrayDeque<V> currentViews;

    final MaterialCalendarView mcv;
    private final CalendarDay today;

    private TitleFormatter titleFormatter = null;
    private Integer dateTextAppearance = null;
    private Integer weekDayTextAppearance = null;
    @ShowOtherDates
    private int showOtherDates = MaterialCalendarView.SHOW_DEFAULTS;
    private CalendarDay minDate = null;
    private CalendarDay maxDate = null;
    private DateRangeIndex rangeIndex;
    private List<CalendarDay> selectedDates = new ArrayList<>();
    private WeekDayFormatter weekDayFormatter = WeekDayFormatter.DEFAULT;
    private DayFormatter dayFormatter = DayFormatter.DEFAULT;
    private List<DayViewDecorator> decorators = new ArrayList<>();
    private boolean selectionEnabled = true;

    CalendarPagerAdapter(MaterialCalendarView mcv) {
        this.mcv = mcv;
        this.today = CalendarDay.today();
        currentViews = new ArrayDeque<>();
        currentViews.iterator();
        setRangeDates(null, null);
    }

    void setDecorators(List<DayViewDecorator> decorators) {
        this.decorators = decorators;
        invalidateDecorators();
    }

    void invalidateDecorators() {
        for (V pagerView : currentViews) {
            pagerView.setDayViewDecorators(decorators);
        }
    }

    @Override
    public int getCount() {
        return rangeIndex.getCount();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titleFormatter == null ? "" : titleFormatter.format(getItem(position));
    }

    CalendarPagerAdapter<?> migrateStateAndReturn(CalendarPagerAdapter<?> newAdapter) {
        newAdapter.titleFormatter = titleFormatter;
        newAdapter.dateTextAppearance = dateTextAppearance;
        newAdapter.weekDayTextAppearance = weekDayTextAppearance;
        newAdapter.showOtherDates = showOtherDates;
        newAdapter.minDate = minDate;
        newAdapter.maxDate = maxDate;
        newAdapter.selectedDates = selectedDates;
        newAdapter.weekDayFormatter = weekDayFormatter;
        newAdapter.dayFormatter = dayFormatter;
        newAdapter.decorators = decorators;
        newAdapter.selectionEnabled = selectionEnabled;
        return newAdapter;
    }

    int getIndexForDay(CalendarDay day) {
        if (day == null) {
            return getCount() / 2;
        }
        if (minDate != null && day.isBefore(minDate)) {
            return 0;
        }
        if (maxDate != null && day.isAfter(maxDate)) {
            return getCount() - 1;
        }
        return rangeIndex.indexOf(day);
    }

    protected abstract V createView(int position);

    protected abstract int indexOf(V view);

    protected abstract boolean isInstanceOfView(Object object);

    protected abstract DateRangeIndex createRangeIndex(CalendarDay min, CalendarDay max);

    @Override
    public int getItemPosition(@NonNull Object object) {
        if (!(isInstanceOfView(object))) {
            return POSITION_NONE;
        }
        CalendarPagerView pagerView = (CalendarPagerView) object;
        CalendarDay firstViewDay = pagerView.getFirstViewDay();
        if (firstViewDay == null) {
            return POSITION_NONE;
        }
        int index = indexOf((V) object);
        if (index < 0) {
            return POSITION_NONE;
        }
        return index;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        V pagerView = createView(position);
        pagerView.setContentDescription(mcv.getCalendarContentDescription());
        pagerView.setAlpha(0);
        pagerView.setSelectionEnabled(selectionEnabled);

        pagerView.setWeekDayFormatter(weekDayFormatter);
        pagerView.setDayFormatter(dayFormatter);
        if (dateTextAppearance != null) {
            pagerView.setDateTextAppearance(dateTextAppearance);
        }
        if (weekDayTextAppearance != null) {
            pagerView.setWeekDayTextAppearance(weekDayTextAppearance);
        }
        pagerView.setShowOtherDates(showOtherDates);
        pagerView.setMinimumDate(minDate);
        pagerView.setMaximumDate(maxDate);
        pagerView.setSelectedDates(selectedDates);

        container.addView(pagerView);
        currentViews.add(pagerView);

        pagerView.setDayViewDecorators(decorators);

        return pagerView;
    }

    void setSelectionEnabled(boolean enabled) {
        selectionEnabled = enabled;
        for (V pagerView : currentViews) {
            pagerView.setSelectionEnabled(selectionEnabled);
        }
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        CalendarPagerView pagerView = (CalendarPagerView) object;
        currentViews.remove(pagerView);
        container.removeView(pagerView);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    void setTitleFormatter(@NonNull TitleFormatter titleFormatter) {
        this.titleFormatter = titleFormatter;
    }

    void setDateTextAppearance(int taId) {
        if (taId == 0) {
            return;
        }
        this.dateTextAppearance = taId;
        for (V pagerView : currentViews) {
            pagerView.setDateTextAppearance(taId);
        }
    }

    void setShowOtherDates(@ShowOtherDates int showFlags) {
        this.showOtherDates = showFlags;
        for (V pagerView : currentViews) {
            pagerView.setShowOtherDates(showFlags);
        }
    }

    void setWeekDayFormatter(WeekDayFormatter formatter) {
        this.weekDayFormatter = formatter;
        for (V pagerView : currentViews) {
            pagerView.setWeekDayFormatter(formatter);
        }
    }

    void setDayFormatter(DayFormatter formatter) {
        this.dayFormatter = formatter;
        for (V pagerView : currentViews) {
            pagerView.setDayFormatter(formatter);
        }
    }

    @ShowOtherDates
    int getShowOtherDates() {
        return showOtherDates;
    }

    void setWeekDayTextAppearance(int taId) {
        if (taId == 0) {
            return;
        }
        this.weekDayTextAppearance = taId;
        for (V pagerView : currentViews) {
            pagerView.setWeekDayTextAppearance(taId);
        }
    }

    void setRangeDates(CalendarDay min, CalendarDay max) {
        this.minDate = min;
        this.maxDate = max;
        for (V pagerView : currentViews) {
            pagerView.setMinimumDate(min);
            pagerView.setMaximumDate(max);
        }

        if (min == null) {
            min = CalendarDay.from(today.getYear() - 200, today.getMonth(), today.getDay());
        }

        if (max == null) {
            max = CalendarDay.from(today.getYear() + 200, today.getMonth(), today.getDay());
        }

        rangeIndex = createRangeIndex(min, max);

        notifyDataSetChanged();
        invalidateSelectedDates();
    }

    DateRangeIndex getRangeIndex() {
        return rangeIndex;
    }

    void clearSelections() {
        selectedDates.clear();
        invalidateSelectedDates();
    }

    void setDateSelected(CalendarDay day, boolean selected) {
        if (selected) {
            if (!selectedDates.contains(day)) {
                selectedDates.add(day);
                invalidateSelectedDates();
            }
        } else {
            if (selectedDates.contains(day)) {
                selectedDates.remove(day);
                invalidateSelectedDates();
            }
        }
    }

    private void invalidateSelectedDates() {
        validateSelectedDates();
        for (V pagerView : currentViews) {
            pagerView.setSelectedDates(selectedDates);
        }
    }

    private void validateSelectedDates() {
        for (int i = 0; i < selectedDates.size(); i++) {
            CalendarDay date = selectedDates.get(i);

            if ((minDate != null && minDate.isAfter(date)) || (maxDate != null && maxDate.isBefore(date))) {
                selectedDates.remove(i);
                mcv.onDateUnselected(date);
                i -= 1;
            }
        }
    }

    CalendarDay getItem(int position) {
        return rangeIndex.getItem(position);
    }

    @NonNull
    List<CalendarDay> getSelectedDates() {
        return Collections.unmodifiableList(selectedDates);
    }

    int getDateTextAppearance() {
        return dateTextAppearance == null ? 0 : dateTextAppearance;
    }

    int getWeekDayTextAppearance() {
        return weekDayTextAppearance == null ? 0 : weekDayTextAppearance;
    }
}
