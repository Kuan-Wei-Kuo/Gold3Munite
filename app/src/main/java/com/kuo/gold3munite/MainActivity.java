package com.kuo.gold3munite;

import android.annotation.TargetApi;
import android.app.Notification;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.transition.Explode;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class MainActivity extends ActionBarActivity{

    public static final String SETTING_NAME = "Settings";
    public static final String FIRST_RUN = "firstRun";
    public static final String START_TIME = "startTime";
    public static final String END_TIME = "endTime";
    public static final String AREA_TIME = "areaTime";
    public static final String TYPE = "type";
    public static final String WEEK_REPEAT = "weekRepeat";
    public static final String SHOCK = "shock";
    public static final String SOUND = "sound";

    private boolean firstRun;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private OnMenuItemClick onMenuItemClick;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private List<ListItem> listItems = new ArrayList<ListItem>();
    private G3MRecyclerAdapter g3MRecyclerAdapter;
    private FragmentManager fragmentManager = getSupportFragmentManager();
    private FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    private Fragment content;
    private MainFragment mainFragment;

    public interface OnMenuItemClick{
        void onMenuItemClick();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeSettings();
        initializeView();
    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences settings = getSharedPreferences(SETTING_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        if (firstRun) {
            editor.putBoolean(FIRST_RUN, false);
        }
        editor.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if(item.getItemId() == android.R.id.home){
            if(fragmentManager.getBackStackEntryCount() > 0){
                getSupportFragmentManager().popBackStack();
            }else{
                if (actionBarDrawerToggle.onOptionsItemSelected(item)){
                    return true;
                }
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void initializeView(){

        //Find View Id.
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        recyclerView = (RecyclerView) findViewById(R.id.leftDrawer);

        //initialize object.
        initializeMenuTitle();
        linearLayoutManager =  new LinearLayoutManager(this);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        g3MRecyclerAdapter = new G3MRecyclerAdapter(R.layout.drawer_list_item, listItems, G3MRecyclerAdapter.DAWER_LIST, onMenuItemClickListener);
        mainFragment = new MainFragment();

        //set object.
        actionBarDrawerToggle.syncState();
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(g3MRecyclerAdapter);

        //replace mainfragment.
        fragmentTransaction.replace(R.id.contentFrame, mainFragment, "mainFragment");
        fragmentTransaction.commit();
    }

    private void initializeMenuTitle(){

        String[] title = {"首頁", "測驗", "統計", "設定"};

        int[] icon = {R.mipmap.g3m_icon, R.mipmap.learn_icon, R.mipmap.physics_icon, R.mipmap.setting_icon};

        for(int i = 0 ; i < 4 ; i++){
            ListItem listItem = new ListItem();
            listItem.chineseText = title[i];
            listItem.icon = icon[i];

            if(i == 0){
                listItem.check = true;
            }
            listItems.add(listItem);
        }

    }

    private void initializeSettings(){

        //initialize object.
        SharedPreferences settings = getSharedPreferences(SETTING_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        firstRun = settings.getBoolean(FIRST_RUN, true);

        if(firstRun){
            Intent i = new Intent();
            i.setClass(this, G3MService.class);
            this.startService(i);

            String[] week = {"0", "1", "2", "3", "4", "5", "6"};
            String[] type = {"0", "1", "2"};
            Set<String> weekArrays = new HashSet();
            Set<String> typeArrays = new HashSet();

            for(int j = 0 ; j < 3 ; j++){
                typeArrays.add(type[j]);
            }

            for(int j = 0 ; j < 7 ; j++){
                weekArrays.add(week[j]);
            }

            editor.putString(START_TIME, "09:00:00");
            editor.putString(END_TIME, "20:00:00");
            editor.putInt(AREA_TIME, 3600);
            editor.putStringSet(TYPE, typeArrays);
            editor.putStringSet(WEEK_REPEAT, weekArrays);
            editor.putBoolean(SHOCK, true);
            editor.putBoolean(SOUND, true);
            editor.commit();
        }
    }

    private G3MRecyclerAdapter.OnItemClickListener onMenuItemClickListener = new G3MRecyclerAdapter.OnItemClickListener() {
        @Override
        public void onClick(long rowId, int posiwtion) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            switch (posiwtion){
                case 0:
                    MainFragment mainFragment = new MainFragment();
                    fragmentTransaction.replace(R.id.contentFrame, mainFragment, "mainFragment");
                    fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    fragmentTransaction.addToBackStack("mainFragment");
                    fragmentTransaction.commit();
                    break;
                case 1:
                    TestFragment testFragment = new TestFragment();
                    fragmentTransaction.replace(R.id.contentFrame, testFragment, "testFragment");
                    fragmentTransaction.addToBackStack("mainFragment");
                    fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    fragmentTransaction.commit();
                    break;
                case 2:
                    StatisicsFragment statisicsFragment = new StatisicsFragment();
                    fragmentTransaction.replace(R.id.contentFrame, statisicsFragment, "statisicsFragment");
                    fragmentTransaction.addToBackStack("mainFragment");
                    fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    fragmentTransaction.commit();
                    break;
                case 3:
                    SettingFragment settingFragment = new SettingFragment();
                    fragmentTransaction.replace(R.id.contentFrame, settingFragment, "settingsFragment");
                    fragmentTransaction.addToBackStack("mainFragment");
                    fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    fragmentTransaction.commit();
                    break;
            }
        }
    };

    public void setToolbarBackgroundColor(int color){
        toolbar.setBackgroundColor(color);
    }

    public void setToolbarTitle(String title){
        toolbar.setTitle(title);
    }

    public void setToolbarActionBar(){
        setSupportActionBar(toolbar);
    }

    public void setActionBarDisplayHomeAsUpEnabled(boolean i){
        getSupportActionBar().setDisplayHomeAsUpEnabled(i);
    }

    public void setDrawerLayoutLockMode(int lockMode){
        drawerLayout.setDrawerLockMode(lockMode);
    }

    public void setDrawerListChanged(int position){

        List<ListItem> listItems = g3MRecyclerAdapter.getListItems();

        for(int i = 0 ; i < listItems.size() ; i++){
            if(i != position){
                listItems.get(i).check = false;
            }else{
                listItems.get(i).check = true;
            }
        }
        g3MRecyclerAdapter.notifyDataSetChanged();
    }

    public void syncStateActionBarDrawerToggle(){
        actionBarDrawerToggle.syncState();
    }

    public void switchContent(Fragment from, Fragment to) {
        if (content != to) {
            content = to;
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction().setCustomAnimations(
                    android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            if (!to.isAdded()) {
                transaction.hide(from).add(R.id.contentFrame, to).commit();
            } else {
                transaction.hide(from).show(to).commit();
            }
        }
    }

}
