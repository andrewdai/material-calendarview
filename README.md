This is a fork of the project originally created by Prolific Interactive. The original project can be found [here](https://github.com/prolificinteractive/material-calendarview)

Major Change in 1.5.0
---------------------
* Breaking Change: Allow decorators to accept a data set and moved logic to decorate the facade into the day view invalidate. This gives more flexibility to the decorator to decide how to decorate the facade when applying to the day view, and enables the ability to use different facades within the same decorator, reducing the need to add more decorators.
* Breaking Change: Day views are disabled by default
* New: Add ability to explicitly state whether or not the top bar should be attached to the calendar

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