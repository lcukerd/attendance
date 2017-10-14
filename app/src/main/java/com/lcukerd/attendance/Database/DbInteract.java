package com.lcukerd.attendance.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.util.Log;

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
    private static String TAG = DbInteract.class.getSimpleName();

    public DbInteract(Context context)
    {
        dBcontract = new eventDBcontract(context);
    }

    /*public ArrayList<Integer> readReport()
    {
        SQLiteDatabase db = dBcontract.getReadableDatabase();
        Cursor cursor = db.query(eventDBcontract.ListofItem.tableName1, projection1, null, null, null, null, null);
        ArrayList<User> usernames = new ArrayList<>();
        while (cursor.moveToNext())
            usernames.add(new User(getImage(cursor.getBlob(cursor.getColumnIndex(eventDBcontract.ListofItem.columnimage))),
                    cursor.getString(cursor.getColumnIndex(eventDBcontract.ListofItem.columnuser)),
                    cursor.getString(cursor.getColumnIndex(eventDBcontract.ListofItem.columnurl)),
                    cursor.getString(cursor.getColumnIndex(eventDBcontract.ListofItem.columnquery))));

        Log.d(TAG, "Returned " + String.valueOf(cursor.getCount()) + " usernames");
        return (usernames);
    }*/

    public void createtable(ArrayList<String> names)
    {
        SQLiteDatabase db = dBcontract.getWritableDatabase();

        for (int i = 0; i < names.size(); i++)
        {
            ContentValues values = new ContentValues();
            values.put(eventDBcontract.ListofItem.columnName, names.get(i));
            db.insert(eventDBcontract.ListofItem.tableName2, null, values);
        }

        Cursor cursor = db.query(eventDBcontract.ListofItem.tableName2,
                new String[]{eventDBcontract.ListofItem.columnName}, null, null, null, null,
                eventDBcontract.ListofItem.columnName + "ASC");

        names.clear();
        while (cursor.moveToNext())
            names.add(cursor.getString(cursor.getColumnIndex(eventDBcontract.ListofItem.columnName)));

        String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + eventDBcontract.ListofItem.tableName1 + " (" +
                        eventDBcontract.ListofItem.columndate + " INTEGER, ";
        for (int i = 0; i < names.size() - 1; i++)
            SQL_CREATE_ENTRIES += (names.get(i) + " INTEGER, ");

        SQL_CREATE_ENTRIES += (names.get(names.size() - 1) + " INTEGER );");

        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void markAttendance(ArrayList<Integer> attendance)
    {
        SQLiteDatabase db = dBcontract.getWritableDatabase();
        ArrayList<String> names = new ArrayList<>();

        Cursor cursor = db.query(eventDBcontract.ListofItem.tableName2,
                projection, null, null, null, null,
                eventDBcontract.ListofItem.columnName + "ASC");

        while (cursor.moveToNext())
            names.add(cursor.getString(cursor.getColumnIndex(eventDBcontract.ListofItem.columnName)));

        ContentValues values = new ContentValues();
        values.put(eventDBcontract.ListofItem.columndate,
                new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime()));

        for (int i = 0; i < names.size() - 1; i++)
            values.put(names.get(i), attendance.get(i));

        db.insert(eventDBcontract.ListofItem.tableName1, null, values);
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

