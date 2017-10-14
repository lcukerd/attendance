package com.lcukerd.attendance.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.lcukerd.attendance.R;

import java.util.ArrayList;

public class StudentRegister extends AppCompatActivity
{
    private ArrayAdapter<String> mForecastAdapter;
    private static final String tag = StudentRegister.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        EditText addName = (EditText) findViewById(R.id.addName);
        addName.setOnEditorActionListener(new EditText.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView exampleView, int actionId, KeyEvent event)
            {
                if ((actionId == EditorInfo.IME_ACTION_NEXT
                        || actionId == EditorInfo.IME_ACTION_GO
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || actionId == EditorInfo.IME_ACTION_SEND)
                        && exampleView.getText().toString().equals("") == false)
                {
                    mForecastAdapter.add(exampleView.getText().toString());
                    mForecastAdapter.notifyDataSetChanged();
                    exampleView.setText("");
                }
                return true;
            }
        });
        mForecastAdapter = new ArrayAdapter<>(
                this, R.layout.list_item, R.id.list_item_textView, new ArrayList<String>());

        ListView listView = (ListView) findViewById(R.id.register);
        listView.setAdapter(mForecastAdapter);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                Log.d(tag, mForecastAdapter.getItem(i));
                mForecastAdapter.remove(mForecastAdapter.getItem(i));
                return true;
            }
        });
    }

}
