package com.lcukerd.attendance.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.lcukerd.attendance.Adapters.StackViewAdapter;
import com.lcukerd.attendance.Database.DbInteract;
import com.lcukerd.attendance.Models.StackViewData;
import com.lcukerd.attendance.R;

import java.util.ArrayList;

import link.fls.swipestack.SwipeStack;

public class MainActivity extends AppCompatActivity
{

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private static DbInteract interact;
    private static final String tag = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        interact = new DbInteract(this);
        SharedPreferences prefs = getSharedPreferences("com.lcukerd.attendance", MODE_PRIVATE);
        if (prefs.getBoolean("intialLaunch", true))
        {
            showregister();
            prefs.edit().putBoolean("intialLaunch", false).commit();
        }
        else
        {
            if (prefs.getBoolean("intialCardLaunch", true))
            {
                Toast.makeText(this, "Swipe Card right to mark present else left.", Toast.LENGTH_SHORT).show();
                prefs.edit().putBoolean("intialLaunch", false).commit();
            }
        }
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
        }
        else if (id == R.id.retake)
        {
            interact.deleteAttendance();
            recreate();
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
            if (getArguments().getInt(ARG_SECTION_NUMBER) == 1)
                return inflater.inflate(R.layout.taker, container, false);
            else
                return inflater.inflate(R.layout.report, container, false);
        }

        public void onStart()
        {
            super.onStart();
            if (getArguments().getInt(ARG_SECTION_NUMBER) == 1)
            {
                final SwipeStack swipeStack = getView().findViewById(R.id.stackT);

                ArrayList<StackViewData> stackViewDatas = new ArrayList<>();
                attendance = new ArrayList<>();
                if (!interact.attendanceTaken())
                {
                    try
                    {
                        stackViewDatas = interact.readReport();
                    } catch (SQLiteException e)
                    {
                        e.printStackTrace();
                        ((TextView) getView().findViewById(R.id.comment))
                                .setText("Add students in register (menu icon)");
                    }
                }
                StackViewAdapter stackViewAdapter = new StackViewAdapter(getContext(), stackViewDatas);
                swipeStack.setAdapter(stackViewAdapter);
                swipeStack.setListener(this);
                swipeStack.setSwipeProgressListener(new SwipeStack.SwipeProgressListener()
                {
                    @Override
                    public void onSwipeStart(int position)
                    {

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

            }
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
            interact.markAttendance(attendance);
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
