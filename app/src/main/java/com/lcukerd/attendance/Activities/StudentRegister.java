package com.lcukerd.attendance.Activities;

import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lcukerd.attendance.Database.DbInteract;
import com.lcukerd.attendance.Models.StackViewData;
import com.lcukerd.attendance.R;

import java.util.ArrayList;

public class StudentRegister extends AppCompatActivity
{
    private ArrayAdapter<String> mRegisterAdapter;
    private static final String tag = StudentRegister.class.getSimpleName();
    private DbInteract interact;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        interact = new DbInteract(this);
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
                    mRegisterAdapter.add(exampleView.getText().toString());
                    mRegisterAdapter.notifyDataSetChanged();
                    exampleView.setText("");
                }
                return true;
            }
        });
        mRegisterAdapter = new ArrayAdapter<>(
                this, R.layout.list_item, R.id.list_item_textView, new ArrayList<String>());

        ArrayList<StackViewData> stackViewDatas = (ArrayList<StackViewData>) interact.readReport(0);
        for (StackViewData temp:stackViewDatas)
            mRegisterAdapter.add(temp.name);

        ListView listView = (ListView) findViewById(R.id.register);
        listView.setAdapter(mRegisterAdapter);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                Log.d(tag, mRegisterAdapter.getItem(i));
                mRegisterAdapter.remove(mRegisterAdapter.getItem(i));
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_register, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if (id == R.id.done)
        {
            ArrayList<String> arrayList = new ArrayList<>();
            for (int i=0;i<mRegisterAdapter.getCount();i++)
                arrayList.add(toTitleCase(mRegisterAdapter.getItem(i)));
            try
            {
                interact.createtable(arrayList);
            }
            catch (SQLiteException e)
            {
                Toast.makeText(this, "Special character not accepted", Toast.LENGTH_SHORT).show();
                Log.e(tag,"Duplicate Column",e);
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private String toTitleCase(String str) {

        if (str == null) {
            return null;
        }

        boolean space = true;
        StringBuilder builder = new StringBuilder(str);
        final int len = builder.length();

        for (int i = 0; i < len; ++i) {
            char c = builder.charAt(i);
            if (space) {
                if (!Character.isWhitespace(c)) {
                    // Convert to title case and switch out of whitespace mode.
                    builder.setCharAt(i, Character.toTitleCase(c));
                    space = false;
                }
            } else if (Character.isWhitespace(c)) {
                space = true;
            } else {
                builder.setCharAt(i, Character.toLowerCase(c));
            }
        }

        return builder.toString();
    }


}
