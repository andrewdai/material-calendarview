DayViewDecorators
=================

The decorator API is a flexible way to customize individual days.
Specifically, it allows you to:

* Set custom backgrounds
* Set custom selectors
* Apply spans to the entire day's text
    * We provide `DotSpan` which will draw a dot centered below the text
* Set dates as disabled

This doc will explain how the API works and examples of how to use it.

## How It Works

A `DayViewDecorator` is an interface that has three methods you need to implement, `shouldDecorate(CalendarDay)`, `decorate(List<Object>, DayViewFacade)`, and getData().
`shouldDecorate()` is called for every date in the calendar to determine if the decorator should be applied to that date.
`decorate()` is called every time `shouldDecorate()` returns true, and will render the customizations used for this specific day's facade.
`getData()` is called internally to pass the data to the `decorate()` method to be used to change the customization of the facade if needed.
This is so we can apply different decorations to different days without having to create a new decorator. It is significantly more expensive to have more decorators calling `shouldDecorate()` on all 42 days every month than it is to have the `decorate()` method do a little extra work to figure out how to change the facade per day.

The `decorate()` method provides you with a `DayViewFacade` that has four methods to allow decoration:

1. `setBackgroundDrawable(Drawable)`
    * You can set a drawable to draw behind everything else.
    * This also responds to state changes.
2. `setSelectionDrawable(Drawable)`
    * This customizes the selection indicator.
3. `addSpan(Object)`
    * Allows you to set a span on the entire day label.
    * We provide a `DotSpan` that draws a dot centered below the label.
    * For an introduction to spans, see [this article](http://androidcocktail.blogspot.com/2014/03/android-spannablestring-example.html).
    * If you want to learn more about custom spans, check out [this article](http://flavienlaurent.com/blog/2014/01/31/spans/).
    * The span is set using `setSpan(yourSpan, 0, label.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);`
4. `setDaysDisabled(boolean)`
    * Allows you to disable and re-enable days.
    * This will not affect minimum and maximum dates.
    * Days decorated as disabled can be re-enabled with other decorators.
    * Days are disabled by default

If one of your decorators changes after it has been added to the calendar view,
make sure you call `invalidateDecorators()` to have those changes reflected.
The decorators are automatically invalidated when you add or remove decorators from the view.

To add a decorator to the calendar, you can call `addDecorator()`.
The order that decorators are added are the order in which they will be applied.
You can remove decorators by calling `removeDecorator()` or `removeDecorators()`.

When implementing a `DayViewDecorator`, make sure that they are as efficient as possible.
Remember that `shouldDecorate()` needs to be called 42 times for each month view.
An easy way to be more efficient is to convert your data to `CalendarDay`s outside of `shouldDecorate()`.

## Responding To State

If you provide custom drawables, make sure they respond to touches and states.
Read more in the [custom selector documentation](CUSTOM_SELECTORS.md).

### Events

Here is a simple example decorator that will draw a dot under a set of dates.

```java
public class EventDecorator implements DayViewDecorator {

    private final List<Object> dataSet;
    private final HashSet<CalendarDay> dates;
    private CalendarDay day;

    public EventDecorator(List<Object> dataSet, Collection<CalendarDay> dates) {
        this.dataSet = dataSet;
        this.dates = new HashSet<>(dates);
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        this.day = day;
        return dates.contains(day);
    }

    @Override
    public void decorate(List<Object> dataSet, DayViewFacade view) {
      //for (Object o : dataSet) {
      //    if (o instanceof Integer) {
      //        view.setBackgroundDrawable(Util.getDrawableBasedOnData(o);
      //        view.addSpan(new DotSpan(5, Util.getColorBasedOnData(o));
      //    }
      //}
    }

    @Override
    public List<Object> getData() {
        return dataSet;
    }
}
```
