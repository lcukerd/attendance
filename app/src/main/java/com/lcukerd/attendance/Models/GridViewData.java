package com.lcukerd.attendance.Models;

/**
 * Created by Programmer on 15-10-2017.
 */

public class GridViewData
{
    public String name;
    public int rollno;
    public int Aperc;

    public GridViewData(String name,int rollno,int Aperc)
    {
        name = name.replace('_',' ');
        this.name = name;
        this.rollno = rollno;
        this.Aperc = Aperc;
    }

}
