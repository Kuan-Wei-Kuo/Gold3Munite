package com.kuo.fragment;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.kuo.adapter.G3MPagerAdapter;
import com.kuo.gold3munite.MainActivity;
import com.kuo.gold3munite.R;
import com.kuo.common.SlidingTabLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 2015/4/2.
 */
public class MainFragment extends Fragment {

    private SlidingTabLayout slidingTabLayout;
    private ViewPager viewPager;
    private List<Fragment> fragmentList = new ArrayList<Fragment>();
    private List<String>   titleList = new ArrayList<String>();

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);

        initializeView(view);

        setToolbar();

        return view;
    }

    private void initializeView(View view){

        int backStackCount = getFragmentManager().getBackStackEntryCount();
        for (int i = 0; i < backStackCount; i++) {
            // Get the back stack fragment id.
            int backStackId = getFragmentManager().getBackStackEntryAt(i).getId();
            getFragmentManager().popBackStack(backStackId, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }

        fragmentList.clear();
        titleList.clear();

        fragmentList.add(new EnglishFragment());
        fragmentList.add(new MathFragment());
        fragmentList.add(new PhysicsFragment());

        titleList.add("英文");
        titleList.add("數學");
        titleList.add("物理");

        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        slidingTabLayout = (SlidingTabLayout) view.findViewById(R.id.slidingTabLayout);

        viewPager.setAdapter(new G3MPagerAdapter(getChildFragmentManager(), fragmentList, titleList));
        slidingTabLayout.setDistributeEvenly(true);
        slidingTabLayout.setBackgroundColor(getResources().getColor(R.color.GREY_300));
        slidingTabLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.BLUE_A400);
            }
        });

        slidingTabLayout.setViewPager(viewPager);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setToolbar(){

        Window window = getActivity().getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.BLUE_A400));

        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.setDrawerListChanged(0);
        mainActivity.setToolbarTitle("黃金三分鐘");
        mainActivity.setToolbarBackgroundColor(getResources().getColor(R.color.BLUE_A400));
        mainActivity.setToolbarActionBar();
        mainActivity.syncStateActionBarDrawerToggle();
        mainActivity.setDrawerLayoutLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        mainActivity.setDrawerLayoutLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

    }
}
