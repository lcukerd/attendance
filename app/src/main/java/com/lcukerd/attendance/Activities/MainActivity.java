package com.lcukerd.attendance.Activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.lcukerd.attendance.Adapters.GridViewAdapter;
import com.lcukerd.attendance.Adapters.StackViewAdapter;
import com.lcukerd.attendance.Database.DbInteract;
import com.lcukerd.attendance.Models.CustomViewPager;
import com.lcukerd.attendance.Models.GridViewData;
import com.lcukerd.attendance.Models.StackViewData;
import com.lcukerd.attendance.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import link.fls.swipestack.SwipeStack;

public class MainActivity extends AppCompatActivity
{

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private static CustomViewPager mViewPager;
    private static DbInteract interact;
    private static MenuItem retake_rollno;
    private static final String tag = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        interact = new DbInteract(this);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (CustomViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }
    protected void onStart()
    {
        super.onStart();
        SharedPreferences prefs = getSharedPreferences("com.lcukerd.attendance", MODE_PRIVATE);
        if (prefs.getBoolean("intialLaunch", true))
        {
            showregister();
            prefs.edit().putBoolean("intialLaunch", false).commit();
        } else
        {
            if (prefs.getBoolean("intialCardLaunch", true))
            {
                showdialog("Random attendance history for all students added to show functionality.");
                showdialog("Swipe Card right to mark present else left.");
                prefs.edit().putBoolean("intialCardLaunch", false).commit();
            }
        }
    }


    private void showdialog(String msg)
    {
        final AlertDialog.Builder instruct = new AlertDialog.Builder(this);
        instruct.setMessage(msg)
                .setPositiveButton("OK", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        dialog.dismiss();
                    }
                });
        AlertDialog instructd = instruct.create();
        instructd.show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main, menu);
        retake_rollno = menu.findItem(R.id.retake_rollno);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if (id == R.id.create_list)
        {
            showregister();
            return true;
        } else if (id == R.id.retake)
        {
            interact.deleteAttendance();
            recreate();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showregister()
    {
        startActivity(new Intent(this, StudentRegister.class));
    }

    public static class PlaceholderFragment extends Fragment implements SwipeStack.SwipeStackListener
    {
        private static final String ARG_SECTION_NUMBER = "section_number";
        private ArrayList<Integer> attendance;
        private CountDownTimer timer = null;
        private SwipeStack swipeStack;
        private StackViewAdapter stackViewAdapter;

        public PlaceholderFragment()
        {
        }

        public static PlaceholderFragment newInstance(int sectionNumber)
        {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState)
        {
            setHasOptionsMenu(true);
            if (getArguments().getInt(ARG_SECTION_NUMBER) == 1)
            {
                View view = inflater.inflate(R.layout.taker, container, false);
                swipeStack = view.findViewById(R.id.stackT);
                swipeStack.resetStack();
                return view;
            } else
                return inflater.inflate(R.layout.report, container, false);
        }

        public void onStart()
        {
            super.onStart();
            if (getArguments().getInt(ARG_SECTION_NUMBER) == 1)
            {
                ArrayList<StackViewData> stackViewDatas = new ArrayList<>();
                attendance = new ArrayList<>();

                try
                {
                    if (!interact.attendanceTaken())
                    {
                        stackViewDatas = (ArrayList<StackViewData>) interact.readReport(0);
                    }
                } catch (SQLiteException e)
                {
                    e.printStackTrace();
                    ((TextView) getView().findViewById(R.id.comment))
                            .setText("Add students in register (menu icon)");
                }
                stackViewAdapter = new StackViewAdapter(getContext(), stackViewDatas);
                swipeStack.setAdapter(stackViewAdapter);
                swipeStack.setListener(this);
                swipeStack.setSwipeProgressListener(new SwipeStack.SwipeProgressListener()
                {
                    @Override
                    public void onSwipeStart(int position)
                    {
                        retake_rollno.setVisible(true);
                        try
                        {
                            timer.cancel();
                        } catch (NullPointerException e)
                        {
                            Log.e(tag, "First Swipe");
                        }
                        mViewPager.setPagingEnabled(false);
                        timer = new CountDownTimer(1000, 1000)
                        {
                            public void onTick(long millisUntilFinished)
                            {
                            }

                            public void onFinish()
                            {
                                mViewPager.setPagingEnabled(true);
                            }
                        }.start();
                    }

                    @Override
                    public void onSwipeProgress(int position, float progress)
                    {
                        View v = swipeStack.getTopView();
                        TextView cmt = v.findViewById(R.id.cmtT);
                        if (progress > 0)
                        {
                            cmt.setText("P");
                            cmt.setBackgroundColor(Color.rgb(76, 175, 80));
                        } else
                        {
                            cmt.setText("AB");
                            cmt.setBackgroundColor(Color.rgb(244, 67, 54));
                        }
                        cmt.setAlpha(Math.abs(progress * 3));
                    }

                    @Override
                    public void onSwipeEnd(int position)
                    {
                        View v = swipeStack.getTopView();
                        v.findViewById(R.id.cmtT).setAlpha(0);
                    }
                });
            } else
            {
                GridView gridView = getView().findViewById(R.id.gridView);
                try
                {
                    GridViewAdapter adapter = new GridViewAdapter(getContext(),
                            (ArrayList<GridViewData>) interact.readReport(1));
                    gridView.setAdapter(adapter);
                } catch (SQLiteException e)
                {
                    startActivity(new Intent(getContext(), StudentRegister.class));
                    Log.e(tag, "Table not found", e);
                }

            }
        }


        @Override
        public boolean onOptionsItemSelected(MenuItem item)
        {
            switch (item.getItemId())
            {
                case R.id.retake_rollno:
                    if (!((TextView) swipeStack.getTopView().findViewById(R.id.rollnoT))
                            .getText().equals("1"))
                    {
                        attendance.remove(attendance.size() - 1);
                        stackViewAdapter.rollback(attendance.size());
                        swipeStack.resetStack();
                    }
                    return true;
                default:
                    break;
            }

            return false;
        }

        @Override
        public void onViewSwipedToRight(int position)
        {
            attendance.add(1);
        }

        @Override
        public void onViewSwipedToLeft(int position)
        {
            attendance.add(0);
        }

        @Override
        public void onStackEmpty()
        {
            retake_rollno.setVisible(false);
            interact.markAttendance(attendance, 0);
            getActivity().recreate();
        }


    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter
    {

        public SectionsPagerAdapter(FragmentManager fm)
        {
            super(fm);
        }

        @Override
        public Fragment getItem(int position)
        {
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount()
        {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position)
        {
            switch (position)
            {
                case 0:
                    return "Attendance";
                case 1:
                    return "Report";
            }
            return null;
        }

    }
}
