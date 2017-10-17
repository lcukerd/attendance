package com.lcukerd.attendance.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.lcukerd.attendance.Database.DbInteract;
import com.lcukerd.attendance.Database.eventDBcontract;
import com.lcukerd.attendance.Models.GridViewData;
import com.lcukerd.attendance.Models.OneDayDecorator;
import com.lcukerd.attendance.R;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Programmer on 15-10-2017.
 */

public class GridViewAdapter extends BaseAdapter
{
    private static final String tag = GridViewAdapter.class.getSimpleName();
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<GridViewData> gridViewDatas;
    private String idurl;
    private DbInteract interact;
    private boolean wait = false, nomoreposts = false, Private = false, internetworking = true;


    public GridViewAdapter(Context context, ArrayList<GridViewData> gridViewDatas)
    {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContext = context;
        interact = new DbInteract(context);
        this.gridViewDatas = gridViewDatas;
    }

    private static class ViewHolder
    {
        TextView nameV, rollnoV, percV;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        final ViewHolder holder;
        if (convertView == null)
        {
            convertView = mInflater.inflate(R.layout.grid_item, parent, false);
            holder = new ViewHolder();
            holder.nameV = convertView.findViewById(R.id.nameGrid);
            holder.rollnoV = convertView.findViewById(R.id.rollnoGrid);
            holder.percV = convertView.findViewById(R.id.percGrid);

            convertView.setTag(holder);
        } else
        {
            holder = (ViewHolder) convertView.getTag();
        }
        final String currName = gridViewDatas.get(position).name;
        holder.nameV.setText(formatName(currName,0));
        holder.rollnoV.setText(String.valueOf(gridViewDatas.get(position).rollno));
        holder.percV.setText(String.valueOf(gridViewDatas.get(position).Aperc) + "%");
        convertView.setBackgroundColor(backColor(gridViewDatas.get(position).Aperc));
        convertView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                detail(currName);
            }
        });

        return convertView;
    }

    private String formatName(String name,int type)
    {
        try
        {
            Integer.parseInt(String.valueOf(name.charAt(name.length() - 1)));
            name = name.substring(0,name.length()-1);
        }
        catch (NumberFormatException e)
        {
            Log.e(tag,"No duplicacy");
        }
        if (name.length() > 7&&type==0)
            name = name.substring(0, 5) + "...";
        return name;
    }

    private void detail(String currName)
    {
        final AlertDialog.Builder detail = new AlertDialog.Builder(mContext, R.style.dialogStyle);
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogb = inflater.inflate(R.layout.detailattendance, null);
        detail.setView(dialogb);
        final AlertDialog dialog = detail.create();
        dialog.setTitle(formatName(currName,1) + "'s Attendance");
        dialog.show();
        MaterialCalendarView calendarView = dialogb.findViewById(R.id.calendarView);
        colorCalendar(calendarView, currName);
    }

    private void colorCalendar(MaterialCalendarView calendarView, String currname)
    {
        currname = currname.replace(' ', '_');
        Cursor cursor = interact.readStudentReport(currname);
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

        int todaysDate = Integer.parseInt(new SimpleDateFormat("yyyyMMdd")
                .format(Calendar.getInstance().getTime()));
        cursor.moveToFirst();
        int currDate = Integer.parseInt(cursor.getString(cursor
                .getColumnIndex(eventDBcontract.ListofItem.columndate)));
        int tempDate = currDate;

        calendar.setTime(sdf.parse(String.valueOf(tempDate), new ParsePosition(0)));
        int status = cursor.getInt(cursor.getColumnIndex(currname));
        do
        {
            OneDayDecorator decorator = new OneDayDecorator(calendar.getTime());

            Log.d(tag, String.valueOf(currDate) + " " + String.valueOf(tempDate));
            if (currDate == tempDate)
            {
                decorator.setColor(status);
                if (cursor.moveToNext())
                {
                    currDate = Integer.parseInt(cursor.getString(cursor
                            .getColumnIndex(eventDBcontract.ListofItem.columndate)));
                    status = cursor.getInt(cursor.getColumnIndex(currname));
                }
            } else
            {
                decorator.setColor(2);
            }
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            tempDate = Integer.parseInt(sdf.format(calendar.getTime()));
            calendarView.addDecorator(decorator);

        } while (tempDate <= todaysDate);

    }


    private int backColor(int perc)
    {
        return Color.rgb(244 - (int) (1.68f * perc), 67 + (int) (1.08f * perc), 54 + (int) (0.26f * perc));
    }


    @Override
    public int getCount()
    {
        return gridViewDatas.size();
    }

    @Override
    public Object getItem(int position)
    {
        return gridViewDatas.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return 0;
    }

}
