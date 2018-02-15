package com.prolificinteractive.materialcalendarview;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

@Experimental
public class WeekPagerAdapter extends CalendarPagerAdapter<WeekView> {
    private static final String TAG = "WeekPagerAdapter";

    public WeekPagerAdapter(MaterialCalendarView mcv) {
        super(mcv);
    }

    @Override
    protected WeekView createView(int position) {
        return new WeekView(mcv, getItem(position), mcv.getFirstDayOfWeek());
    }

    @Override
    protected int indexOf(WeekView view) {
        CalendarDay week = view.getFirstViewDay();
        return getRangeIndex().indexOf(week);
    }

    @Override
    protected boolean isInstanceOfView(Object object) {
        return object instanceof WeekView;
    }

    @Override
    protected DateRangeIndex createRangeIndex(CalendarDay min, CalendarDay max) {
        return new Weekly(min, max, mcv.getFirstDayOfWeek());
    }

    public static class Weekly implements DateRangeIndex {

        private static final float DAYS_IN_WEEK = 7f;
        private final CalendarDay min;
        private final int count;

        public Weekly(@NonNull CalendarDay min, @NonNull CalendarDay max, int firstDayOfWeek) {
            this.min = getFirstDayOfWeek(min, firstDayOfWeek);
            this.count = weekNumberDifference(this.min, max) + 1;
        }

        @Override
        public int getCount() {
            return count;
        }

        @Override
        public int indexOf(CalendarDay day) {
            return weekNumberDifference(min, day);
        }

        @Override
        public CalendarDay getItem(int position) {
            Calendar instance = Calendar.getInstance();
            instance.setTimeInMillis(min.getDate().getTime());
            instance.add(Calendar.DATE, (int) (position * DAYS_IN_WEEK));
            return CalendarDay.from(instance);
        }

        private int weekNumberDifference(@NonNull CalendarDay min, @NonNull CalendarDay max) {
            long millisDiff = max.getDate().getTime() - min.getDate().getTime();

            int timeOffsetMax = max.getCalendar().get(Calendar.DST_OFFSET) + max.getCalendar().get(Calendar.ZONE_OFFSET);
            int timeOffsetMin = min.getCalendar().get(Calendar.DST_OFFSET) + min.getCalendar().get(Calendar.ZONE_OFFSET);

            long dayDiff = TimeUnit.DAYS.convert(millisDiff + timeOffsetMax - timeOffsetMin, TimeUnit.MILLISECONDS);
            return (int) (dayDiff / DAYS_IN_WEEK);
        }

        /*
         * Necessary because of how Calendar handles getting the first day of week internally.
         */
        private CalendarDay getFirstDayOfWeek(@NonNull CalendarDay min, int wantedFirstDayOfWeek) {
            Calendar calendar = Calendar.getInstance();
            min.copyTo(calendar);
            while (calendar.get(Calendar.DAY_OF_WEEK) != wantedFirstDayOfWeek) {
                calendar.add(Calendar.DAY_OF_MONTH, -1);
            }
            return CalendarDay.from(calendar);
        }
    }
}
