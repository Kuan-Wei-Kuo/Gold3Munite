package com.kuo.gold3munite;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by User on 2015/4/2.
 */
public class MainFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);


        int backStackCount = getFragmentManager().getBackStackEntryCount();
        for (int i = 0; i < backStackCount; i++) {
            // Get the back stack fragment id.
            int backStackId = getFragmentManager().getBackStackEntryAt(i).getId();
            getFragmentManager().popBackStack(backStackId, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }

        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.toolbar.setTitle("黃金三分鐘");
        mainActivity.setSupportActionBar(mainActivity.toolbar);
        mainActivity.actionBarDrawerToggle.syncState();
        mainActivity.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

        return view;
    }
}
