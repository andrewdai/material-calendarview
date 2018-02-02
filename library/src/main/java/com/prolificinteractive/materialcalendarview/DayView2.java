package com.prolificinteractive.materialcalendarview;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.prolificinteractive.materialcalendarview.MaterialCalendarView.ShowOtherDates;
import com.prolificinteractive.materialcalendarview.format.DayFormatter;

import java.util.List;

import static com.prolificinteractive.materialcalendarview.MaterialCalendarView.showDecoratedDisabled;
import static com.prolificinteractive.materialcalendarview.MaterialCalendarView.showOtherMonths;
import static com.prolificinteractive.materialcalendarview.MaterialCalendarView.showOutOfRange;


class DayView2 extends RelativeLayout {

    private SquareLinearLayout mRootLayout;
    private TextView mDateText;
    private ImageView mCircleImage;

    private Drawable imageDrawable;

    private DayFormatter formatter = DayFormatter.DEFAULT;

    private boolean isInRange = true;
    private boolean isInMonth = true;
    private boolean isDecoratedDisabled = false;
    @ShowOtherDates
    private int showOtherDates = MaterialCalendarView.SHOW_DEFAULTS;
    private CalendarDay day;

    public DayView2(Context context) {
        super(context);
    }

    public DayView2(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mRootLayout = findViewById(R.id.dv2_root);
        mDateText = findViewById(R.id.dv2_date_text);
        mCircleImage = findViewById(R.id.dv2_circle_image);
    }

    public void init(CalendarDay day) {
        this.day = day;
        mDateText.setText(formatter.format(day));
        if (CalendarDay.today().equals(day)) {
            mDateText.setTextColor(new ColorStateList(new int[][]{
                    new int[]{android.R.attr.state_enabled}, // enabled
                    new int[]{-android.R.attr.state_enabled}, // disabled
                    new int[]{android.R.attr.state_selected}  // selected
            }, new int[]{
                    ContextCompat.getColor(getContext(), R.color.painscale_blue),
                    ContextCompat.getColor(getContext(), R.color.off_grey),
                    ContextCompat.getColor(getContext(), android.R.color.white)
            }));
            mRootLayout.setBackgroundResource(R.drawable.custom_today_view_bg_selector);
        } else {
            mDateText.setTextColor(new ColorStateList(new int[][]{
                    new int[]{android.R.attr.state_selected}, // selected
                    new int[]{-android.R.attr.state_enabled}, // disabled
                    new int[]{android.R.attr.state_enabled}   // enabled
            }, new int[]{
                    ContextCompat.getColor(getContext(), android.R.color.white),
                    ContextCompat.getColor(getContext(), R.color.off_grey),
                    ContextCompat.getColor(getContext(), R.color.text_black)
            }));
            mRootLayout.setBackgroundResource(R.drawable.custom_day_view_bg_selector);
        }
    }

    @NonNull
    public String getLabel() {
        return formatter.format(day);
    }

    private void setEnabled() {
        setEnabled(true);
        boolean enabled = isInMonth && isInRange && !isDecoratedDisabled;
        super.setEnabled(isInRange && !isDecoratedDisabled);

        boolean showOtherMonths = showOtherMonths(showOtherDates);
        boolean showOutOfRange = showOutOfRange(showOtherDates) || showOtherMonths;
        boolean showDecoratedDisabled = showDecoratedDisabled(showOtherDates);

        boolean shouldBeVisible = enabled;

        if (!isInMonth && showOtherMonths) {
            shouldBeVisible = true;
        }

        if (!isInRange && showOutOfRange) {
            shouldBeVisible |= isInMonth;
        }

        if (isDecoratedDisabled && showDecoratedDisabled) {
            shouldBeVisible |= isInMonth && isInRange;
        }

        if (!isInMonth && shouldBeVisible) {
            mDateText.setTextColor(Color.GRAY);
        }
        setVisibility(shouldBeVisible ? View.VISIBLE : View.INVISIBLE);
    }

    protected void setupSelection(@ShowOtherDates int showOtherDates, boolean inRange, boolean inMonth) {
        this.showOtherDates = showOtherDates;
        this.isInMonth = inMonth;
        this.isInRange = inRange;
        setEnabled();
    }

    private void generateImageDrawable() {
        mCircleImage.setImageDrawable(imageDrawable != null ? imageDrawable :
                ContextCompat.getDrawable(getContext(), R.drawable.empty_circle_bg));
    }

    public void setSelectionColor(int color) {
        // do nothing
    }

    /**
     * @param drawable custom selection drawable
     */
    public void setImageDrawable(Drawable drawable) {
        if (drawable == null) {
            this.imageDrawable = null;
        } else {
            this.imageDrawable = drawable.getConstantState().newDrawable(getResources());
        }
        generateImageDrawable();
    }

    /**
     * @param facade apply the facade to us
     */
    void applyFacade(DayViewFacade facade) {
        this.isDecoratedDisabled = facade.areDaysDisabled();
        setEnabled();

        setImageDrawable(facade.getSelectionDrawable());

        // Facade has spans
        List<DayViewFacade.Span> spans = facade.getSpans();
        if (!spans.isEmpty()) {
            String label = getLabel();
            SpannableString formattedLabel = new SpannableString(getLabel());
            for (DayViewFacade.Span span : spans) {
                formattedLabel.setSpan(span.span, 0, label.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            mDateText.setText(formattedLabel);
        }
        // Reset in case it was customized previously
        else {
            mDateText.setText(getLabel());
        }
    }

    public void setTextAppearance(Context context, int taId) {
        mDateText.setTextAppearance(context, taId);
    }

    /**
     * Set the new label formatter and reformat the current label. This preserves current spans.
     *
     * @param formatter new label formatter
     */
    public void setDayFormatter(DayFormatter formatter) {
        this.formatter = formatter == null ? DayFormatter.DEFAULT : formatter;
        CharSequence currentLabel = mDateText.getText();
        Object[] spans = null;
        if (currentLabel instanceof Spanned) {
            spans = ((Spanned) currentLabel).getSpans(0, currentLabel.length(), Object.class);
        }
        SpannableString newLabel = new SpannableString(getLabel());
        if (spans != null) {
            for (Object span : spans) {
                newLabel.setSpan(span, 0, newLabel.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        mDateText.setText(newLabel);
    }

    public CalendarDay getDate() {
        return day;
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        mRootLayout.setEnabled(enabled);
        mDateText.setEnabled(enabled);
    }

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        mRootLayout.setSelected(selected);
        mDateText.setSelected(selected);
    }
}
