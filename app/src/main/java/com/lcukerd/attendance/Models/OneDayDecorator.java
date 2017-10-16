package com.lcukerd.attendance.Models;


import android.graphics.Color;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.Date;

/**
 * Created by Programmer on 16-10-2017.
 */

public class OneDayDecorator implements DayViewDecorator
{

    private CalendarDay date;

    public OneDayDecorator(Date date) {
        this.date = CalendarDay.from(date);
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return date != null && day.equals(date);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new BackgroundColorSpan(Color.rgb(74, 201, 79)));
    }
}
