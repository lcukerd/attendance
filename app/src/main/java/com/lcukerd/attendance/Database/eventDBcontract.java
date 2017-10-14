package com.lcukerd.attendance.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Programmer on 14-10-2017.
 */

public class eventDBcontract extends SQLiteOpenHelper
{

    public static int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "AttendanceTAKER.db";
    private static final String SQL_CREATE_Students =
            "CREATE TABLE " + ListofItem.tableName2 + " (" +
                    ListofItem.columnName + " TEXT );";

    public eventDBcontract(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(SQL_CREATE_Students);
        Log.d("Database", "created");
    }



    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        switch (newVersion)
        {

        }
    }

    public static class ListofItem
    {
        public static final String tableName1 = "Attendance",
                tableName2 = "Students",
                columnName = "Name",
                columndate = "Date";
    }
}
