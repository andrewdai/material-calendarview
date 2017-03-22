This is a fork of the project originally created by Prolific Interactive. The original project can be found [here](https://github.com/prolificinteractive/material-calendarview)

Material Calendar View
======================

A Material design back port of Android's CalendarView. The goal is to have a Material look
and feel, rather than 100% parity with the platform's implementation.

<img src="/images/screencast.gif" alt="Demo Screen Capture" width="300px" />

Usage
-----

1. Add `compile 'com.andrewdai:material-calendarview:1.5.0'` to your dependencies.
2. Add `MaterialCalendarView` into your layouts or view hierarchy.
3. Set a `OnDateSelectedListener` or call `MaterialCalendarView.getSelectedDates()` when you need it.


Example:

```xml
<com.prolificinteractive.materialcalendarview.MaterialCalendarView
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:id="@+id/calendarView"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:mcv_showOtherDates="all"
    app:mcv_selectionColor="#00F"
    />
```
#### @Experimental
`CalendarMode.WEEK` and all week mode functionality is officially marked `@Experimental`. All APIs
marked `@Experimental` are subject to change quickly and should not be used in production code. They
are allowed for testing and feedback.


Major Change in 1.5.0
---------------------
* Breaking Change: Allow decorators to accept a data set and moved logic to decorate the facade into the day view invalidate. This gives more flexibility to the decorator to decide how to decorate the facade when applying to the day view, and enables the ability to use different facades within the same decorator, reducing the need to add more decorators.


Documentation
-------------

Make sure to check all the documentation available [here](docs/README.md).

Customization
-------------

One of the aims of this library is to be customizable. The many options include:

* [Define the view's width and height in terms of tile size](docs/CUSTOMIZATION.md#tile-size)
* [Single or Multiple date selection, or disabling selection entirely](docs/CUSTOMIZATION.md#date-selection)
* [Showing dates from other months or those out of range](docs/CUSTOMIZATION.md#showing-other-dates)
* [Setting the first day of the week](docs/CUSTOMIZATION_BUILDER.md#first-day-of-the-week)
* [Show only a range of dates](docs/CUSTOMIZATION_BUILDER.md#date-ranges)
* [Customize the top bar](docs/CUSTOMIZATION.md#topbar-options)
* [Custom labels for the header, weekdays, or individual days](docs/CUSTOMIZATION.md#custom-labels)


### Events, Highlighting, Custom Selectors, and More!

All of this and more can be done via the decorator api. Please check out the [decorator documentation](docs/DECORATORS.md).

### Custom Selectors and Colors

If you provide custom drawables or colors, you'll want to make sure they respond to state.
Check out the [documentation for custom states](docs/CUSTOM_SELECTORS.md).