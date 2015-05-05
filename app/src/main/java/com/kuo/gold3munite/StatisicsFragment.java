package com.kuo.gold3munite;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by User on 2015/4/5.
 */
public class StatisicsFragment extends Fragment {

    private G3MSQLite g3MSQLite;
    private SlidingTabLayout slidingTabLayout;
    private ViewPager viewPager;
    private List<Fragment> fragmentList = new ArrayList<Fragment>();
    private List<String>   titleList = new ArrayList<String>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        g3MSQLite = new G3MSQLite(getActivity());
        g3MSQLite.OpenDB();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statisics, container, false);

        initializeView(view);

        return view;
    }

    private void initializeView(View view){

        fragmentList.clear();
        titleList.clear();

        fragmentList.add(new StatisicsNotifyFragment());
        fragmentList.add(new StatisicsTestFragment());

        titleList.add("推播次數");
        titleList.add("測驗次數");

        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        slidingTabLayout = (SlidingTabLayout) view.findViewById(R.id.slidingTabLayout);

        viewPager.setAdapter(new G3MPagerAdapter(getChildFragmentManager(), fragmentList, titleList));
        slidingTabLayout.setDistributeEvenly(true);
        slidingTabLayout.setViewPager(viewPager);

        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.setDrawerListChanged(2);
        mainActivity.setToolbarTitle("學習記錄");
        mainActivity.setToolbarBackgroundColor(getResources().getColor(R.color.BLUE_A400));
        mainActivity.setToolbarActionBar();
        mainActivity.syncStateActionBarDrawerToggle();
        mainActivity.setDrawerLayoutLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        mainActivity.setDrawerLayoutLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }
}
