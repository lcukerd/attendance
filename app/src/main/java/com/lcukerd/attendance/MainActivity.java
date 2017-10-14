package com.lcukerd.attendance;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.StackView;
import android.widget.TextView;
import android.widget.Toast;

import com.lcukerd.attendance.Adapters.StackViewAdapter;
import com.lcukerd.attendance.Models.StackViewData;

import java.util.ArrayList;
import java.util.Objects;

import link.fls.swipestack.SwipeStack;

public class MainActivity extends AppCompatActivity
{

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

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

        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static class PlaceholderFragment extends Fragment implements SwipeStack.SwipeStackListener
    {
        private static final String ARG_SECTION_NUMBER = "section_number";

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
                SwipeStack swipeStack = getView().findViewById(R.id.stackT);

                ArrayList<StackViewData> stackViewDatas = new ArrayList<>();
                setuprollno(stackViewDatas);
                StackViewAdapter stackViewAdapter = new StackViewAdapter(getContext(),stackViewDatas);
                swipeStack.setAdapter(stackViewAdapter);
                swipeStack.setListener(this);
            }
            else
            {

            }
        }

        private void setuprollno(ArrayList<StackViewData> stackViewDatas)
        {
            for (int i=0;i<5;i++)
            {
                StackViewData temp = new StackViewData("abc",i);
                stackViewDatas.add(temp);
            }
        }

        @Override
        public void onViewSwipedToRight(int position) {
            Toast.makeText(getContext(),"Right " + String.valueOf(position) ,Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onViewSwipedToLeft(int position) {
            Toast.makeText(getContext(),"Left",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onStackEmpty() {
            Toast.makeText(getContext(),"Empty",Toast.LENGTH_SHORT).show();
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
