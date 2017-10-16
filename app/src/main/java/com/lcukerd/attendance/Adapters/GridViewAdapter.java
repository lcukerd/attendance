package com.lcukerd.attendance.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lcukerd.attendance.Database.DbInteract;
import com.lcukerd.attendance.Models.GridViewData;
import com.lcukerd.attendance.R;

import java.util.ArrayList;

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
        String currName = gridViewDatas.get(position).name;
        if (currName.length()>7)
            holder.nameV.setText(currName.substring(0,5)+"...");
        else
            holder.nameV.setText(currName);
        holder.rollnoV.setText(String.valueOf(gridViewDatas.get(position).rollno));
        holder.percV.setText(String.valueOf(gridViewDatas.get(position).Aperc)+"%");
        convertView.setBackgroundColor(backColor(gridViewDatas.get(position).Aperc));
        //convertView.setBackgroundColor(backColor(position*10));
/*        convertView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(mContext, SwipePic.class);
                intent.putExtra("id", idurl);
                intent.putExtra("position", position);
                mContext.startActivity(intent);
            }
        });*/

        return convertView;
    }

    private int backColor(int perc)
    {
        return Color.rgb(244-(int)(1.68f*perc), 67 +(int)(1.08f*perc), 54 + (int)(0.26f*perc));
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
