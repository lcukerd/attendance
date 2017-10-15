package com.lcukerd.attendance.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.Toast;

import com.lcukerd.attendance.Models.StackViewData;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Programmer on 14-10-2017.
 */

public class DbInteract
{
    private eventDBcontract dBcontract;
    private String[] projection = {
            eventDBcontract.ListofItem.columnName
    };
    private static String tag = DbInteract.class.getSimpleName();
    private Context context;

    public DbInteract(Context context)
    {
        this.context = context;
        dBcontract = new eventDBcontract(context);
    }

    public ArrayList<StackViewData> readReport()
    {
        SQLiteDatabase db = dBcontract.getReadableDatabase();
        Cursor cursor = db.query(eventDBcontract.ListofItem.tableName2,
                projection, null, null, null, null, eventDBcontract.ListofItem.columnName + " ASC");
        ArrayList<StackViewData> students = new ArrayList<>();
        while (cursor.moveToNext())
            students.add(new StackViewData
                    (cursor.getString(cursor.getColumnIndex(eventDBcontract.ListofItem.columnName)),
                            cursor.getPosition()+1));
        Log.d(tag, "Returned " + String.valueOf(cursor.getCount()) + " students");
        return students;
    }

    public void createtable(ArrayList<String> names)
    {
        SQLiteDatabase db = dBcontract.getWritableDatabase();
        db.delete(eventDBcontract.ListofItem.tableName2,null,null);
        db.execSQL("DROP TABLE IF EXISTS " + eventDBcontract.ListofItem.tableName1);

        for (int i = 0; i < names.size(); i++)
        {
            ContentValues values = new ContentValues();
            values.put(eventDBcontract.ListofItem.columnName, names.get(i));
            db.insert(eventDBcontract.ListofItem.tableName2, null, values);
        }

        Cursor cursor = db.query(eventDBcontract.ListofItem.tableName2,
                new String[]{eventDBcontract.ListofItem.columnName}, null, null, null, null,
                eventDBcontract.ListofItem.columnName + " ASC");

        names.clear();
        while (cursor.moveToNext())
            names.add(cursor.getString(cursor.getColumnIndex(eventDBcontract.ListofItem.columnName)));

        String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + eventDBcontract.ListofItem.tableName1 + " (" +
                        eventDBcontract.ListofItem.columndate + " INTEGER, ";
        String temp=null;
        for (int i = 0; i < names.size() - 1; i++)
        {
            if (names.get(i).equals(temp))
            {
                temp = names.get(i).replace(' ','_');
                temp = "m_" + temp;
            }
            else
                temp = names.get(i).replace(' ','_');
            SQL_CREATE_ENTRIES += (temp + " INTEGER, ");
        }

        SQL_CREATE_ENTRIES += (names.get(names.size() - 1) + " INTEGER );");

        db.execSQL(SQL_CREATE_ENTRIES);
        Toast.makeText(context, "Students Added", Toast.LENGTH_SHORT).show();
        Log.d(tag, "Table Created with " + String.valueOf(names.size()) + " students");
    }

    public void markAttendance(ArrayList<Integer> attendance)
    {
        SQLiteDatabase db = dBcontract.getWritableDatabase();
        ArrayList<String> names = new ArrayList<>();

        Cursor cursor = db.query(eventDBcontract.ListofItem.tableName2,
                projection, null, null, null, null,
                eventDBcontract.ListofItem.columnName + " ASC");

        while (cursor.moveToNext())
            names.add(cursor.getString(cursor.getColumnIndex(eventDBcontract.ListofItem.columnName)));

        ContentValues values = new ContentValues();
        values.put(eventDBcontract.ListofItem.columndate,
                new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime()));

        for (int i = 0; i < names.size() - 1; i++)
            values.put(names.get(i).replace(' ','_'), attendance.get(i));

        db.insert(eventDBcontract.ListofItem.tableName1, null, values);

        Toast.makeText(context, "Attendance Complete", Toast.LENGTH_SHORT).show();
        Log.d(tag,"Attendance Complete");
    }

    public void deleteAttendance()
    {
        SQLiteDatabase db = dBcontract.getWritableDatabase();
        Log.d(tag, "Delete Today's Attendance" + String.valueOf(db.delete(eventDBcontract.ListofItem.tableName1,
                eventDBcontract.ListofItem.columndate + " = '" +
                        new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime()) +
                        "'", null)));
    }

    public boolean attendanceTaken()
    {
        SQLiteDatabase db = dBcontract.getReadableDatabase();
        boolean taken = false;
        Cursor cursor = db.query(eventDBcontract.ListofItem.tableName1,
                null, eventDBcontract.ListofItem.columndate + " = ?",
                new String[]{new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime())},
                null, null, null);
        if (cursor.getCount() != 0)
            taken = true;
        Log.d(tag, "Attandance Taken " + String.valueOf(taken));
        return taken;
    }


    /*public void adduser(Bitmap profilePic, String username, String url, String query)
    {
        SQLiteDatabase db = dBcontract.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(eventDBcontract.ListofItem.columnimage, getBitmapAsByteArray(profilePic));
        values.put(eventDBcontract.ListofItem.columnuser, username);
        values.put(eventDBcontract.ListofItem.columnurl, url);
        values.put(eventDBcontract.ListofItem.columnquery, query);
        db.insert(eventDBcontract.ListofItem.tableName1, null, values);
        Log.d(TAG, "Add User " + username);
    }

    public void deleteuser(String username)
    {
        SQLiteDatabase db = dBcontract.getWritableDatabase();
        Log.d(TAG, String.valueOf(db.delete(eventDBcontract.ListofItem.tableName1,
                eventDBcontract.ListofItem.columnuser + " = '" + username + "'", null)));
    }*/

}

