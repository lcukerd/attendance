package com.lcukerd.attendance.Models;

/**
 * Created by Programmer on 14-10-2017.
 */

public class StackViewData
{
    public String name;
    public int rollno;

    public StackViewData(String name,int rollno)
    {
        name = name.replace('_',' ');
        this.name= name;
        this.rollno = rollno;
    }

}
