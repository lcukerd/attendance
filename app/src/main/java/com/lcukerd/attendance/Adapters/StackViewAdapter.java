package com.lcukerd.attendance.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lcukerd.attendance.Models.StackViewData;
import com.lcukerd.attendance.R;

import java.util.ArrayList;
import java.util.zip.Inflater;

/**
 * Created by Programmer on 14-10-2017.
 */

public class StackViewAdapter extends BaseAdapter
{
    private ArrayList<StackViewData> stackViewDatas;
    private ArrayList<StackViewData> temp;
    private LayoutInflater mInflater;
    private int rollbackamt=0;
    private static final String tag = StackViewAdapter.class.getSimpleName();

    public StackViewAdapter(Context context,ArrayList<StackViewData> stackViewDatas)
    {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.stackViewDatas = stackViewDatas;
        temp = new ArrayList<>();
        temp.addAll(stackViewDatas);
    }

    private static class ViewHolder
    {
        TextView name,rollno,comment;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        final ViewHolder holder;
        if (convertView == null)
        {
            convertView = mInflater.inflate(R.layout.items, parent, false);
            holder = new ViewHolder();
            holder.name = convertView.findViewById(R.id.nameT);
            holder.rollno = convertView.findViewById(R.id.rollnoT);
            holder.comment = convertView.findViewById(R.id.cmtT);

            convertView.setTag(holder);
        } else
        {
            holder = (ViewHolder) convertView.getTag();
        }
        StackViewData current = stackViewDatas.get(position);
        holder.name.setText(current.name);
        holder.rollno.setText(String.valueOf(current.rollno));
        return convertView;
    }

    public void rollback(int pos)
    {
        stackViewDatas.clear();
        Log.d(tag,String.valueOf(pos));
        for (; pos<temp.size();pos++)
            stackViewDatas.add(temp.get(pos));
        notifyDataSetInvalidated();
    }

    @Override
    public int getCount()
    {
        return stackViewDatas.size();
    }

    @Override
    public Object getItem(int position)
    {
        return stackViewDatas.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return 0;
    }
}
