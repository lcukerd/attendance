package com.lcukerd.attendance.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
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
    private static final String tag = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        SharedPreferences prefs = getSharedPreferences("com.lcukerd.attendance", MODE_PRIVATE);
        if (prefs.getBoolean("intialLaunch", true))
        {
            showregister();
            prefs.edit().putBoolean("intialLaunch", false).commit();
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

        return super.onOptionsItemSelected(item);
    }

    private void showregister()
    {
        startActivity(new Intent(this,StudentRegister.class));
    }

    public static class PlaceholderFragment extends Fragment implements SwipeStack.SwipeStackListener
    {
        private static final String ARG_SECTION_NUMBER = "section_number";
        private DbInteract interact;
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
            interact = new DbInteract(getContext());
            if (getArguments().getInt(ARG_SECTION_NUMBER) == 1)
            {
                final SwipeStack swipeStack = getView().findViewById(R.id.stackT);

                ArrayList<StackViewData> stackViewDatas = new ArrayList<>();
                attendance = new ArrayList<>();
                setuprollno(stackViewDatas);
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
                        Log.i(tag, String.valueOf(progress));
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
                        Log.i(tag, "Swipe End");
                        v.findViewById(R.id.cmtT).setAlpha(0);
                    }
                });
            } else
            {

            }
        }

        private void setuprollno(ArrayList<StackViewData> stackViewDatas)
        {
            for (int i = 0; i < 5; i++)
            {
                StackViewData temp = new StackViewData("abc", i);
                stackViewDatas.add(temp);
            }
        }

        @Override
        public void onViewSwipedToRight(int position)
        {
            attendance.add(1);
            Toast.makeText(getContext(), "Right " + String.valueOf(position), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onViewSwipedToLeft(int position)
        {
            attendance.add(0);
            Toast.makeText(getContext(), "Left", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onStackEmpty()
        {
            interact.markAttendance(attendance);
            Toast.makeText(getContext(), "Empty", Toast.LENGTH_SHORT).show();
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
