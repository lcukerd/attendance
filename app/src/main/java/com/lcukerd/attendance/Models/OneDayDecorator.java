package com.lcukerd.attendance.Models;


import android.content.Context;
import android.graphics.Color;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;

import com.lcukerd.attendance.R;
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
    private int color;

    public OneDayDecorator(Date date) {
        this.date = CalendarDay.from(date);
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return date != null && day.equals(date);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new ForegroundColorSpan(color));
    }

    public void setColor(int n)
    {
        switch (n)
        {
            case 1:
                color = Color.rgb(58, 224, 64);
                break;
            case 0:
                color = Color.rgb(246,104,94);
                break;
            case 2:
                color = Color.LTGRAY;
                break;
        }
    }

}
