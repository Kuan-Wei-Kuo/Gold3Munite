package com.kuo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kuo.gold3munite.G3MPagerAdapter;
import com.kuo.gold3munite.G3MSQLite;
import com.kuo.gold3munite.MainActivity;
import com.kuo.gold3munite.R;
import com.kuo.gold3munite.SlidingTabLayout;
import com.kuo.gold3munite.StatisicsNotifyFragment;

import java.util.ArrayList;
import java.util.List;

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
