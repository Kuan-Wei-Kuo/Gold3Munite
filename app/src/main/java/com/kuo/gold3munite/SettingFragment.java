package com.kuo.gold3munite;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 2015/4/4.
 */
public class SettingFragment extends Fragment {

    private RecyclerView recyclerView;
    private G3MRecyclerAdapter g3MRecyclerAdapter;
    private LinearLayoutManager linearLayoutManager;
    private G3MSQLite g3MSQLite;
    private List<ListItem> listItems = new ArrayList<ListItem>();

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_recycler, container, false);

        for (int i = 0; i < 3 ; i++){
            ListItem listItem = new ListItem();
            listItem.timerText = "12:0"+i;
            listItem.timerTypeText = "上午";
            listItem.typeText = "英文";
            listItems.add(listItem);
        }

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(view.getContext());
        g3MRecyclerAdapter = new G3MRecyclerAdapter(R.layout.list_item_setting, listItems, G3MRecyclerAdapter.SETTING);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(g3MRecyclerAdapter);

        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.toolbar.setTitle("黃金三分鐘 - 設定");
        mainActivity.setSupportActionBar(mainActivity.toolbar);
        mainActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mainActivity.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        return view;
    }
}