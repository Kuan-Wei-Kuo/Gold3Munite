package com.kuo.gold3munite;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
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
import java.util.List;


public class MainActivity extends ActionBarActivity implements G3MRecyclerAdapter.OnItemClickListener{

    public Toolbar toolbar;
    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    private ListView listView;
    private boolean setMenuEnable = false;
    private OnMenuItemClick onMenuItemClick;
    private boolean setListDawerClick = true;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private List<ListItem> listItems = new ArrayList<ListItem>();
    private G3MRecyclerAdapter g3MRecyclerAdapter;
    private FragmentManager fragmentManager = getSupportFragmentManager();
    private FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    private boolean setPopBack = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        recyclerView = (RecyclerView) findViewById(R.id.leftDrawer);
        linearLayoutManager =  new LinearLayoutManager(this);

        toolbar.setTitleTextColor(getResources().getColor(R.color.white_1));

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

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        actionBarDrawerToggle.syncState();
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        recyclerView.setHasFixedSize(true);
        g3MRecyclerAdapter = new G3MRecyclerAdapter(R.layout.drawer_list_item, listItems, G3MRecyclerAdapter.DAWER_LIST, this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(g3MRecyclerAdapter);

        fragmentManager.addOnBackStackChangedListener(fragmentStackChangeListener);

        MainFragment mainFragment = new MainFragment();
        fragmentTransaction.replace(R.id.contentFrame, mainFragment, "mainFragment");
        fragmentTransaction.commit();

        /*Intent i = new Intent();
        i.setClass(this, G3MService.class);
        this.startService(i);*/
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch(item.getItemId()){
            case android.R.id.home:
                if(setPopBack){
                    if(fragmentManager.getBackStackEntryCount() > 0){
                        fragmentManager.popBackStack();
                    }
                }else{
                    if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
                        return true;
                    }
                }
                break;
            case R.id.action_append:
                onMenuItemClick = (OnMenuItemClick) getSupportFragmentManager().findFragmentByTag("settingFragment");
                onMenuItemClick.onMenuItemClick();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(setMenuEnable){
            getMenuInflater().inflate(R.menu.menu_main, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        return super.dispatchKeyEvent(event);
    }

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
                fragmentTransaction.replace(R.id.contentFrame, settingFragment, "settingFragment");
                fragmentTransaction.addToBackStack("mainFragment");
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragmentTransaction.commit();
                break;
        }
    }

    private FragmentManager.OnBackStackChangedListener fragmentStackChangeListener = new FragmentManager.OnBackStackChangedListener() {
        @Override
        public void onBackStackChanged() {
            Toast.makeText(MainActivity.this, "狀態改變", Toast.LENGTH_SHORT).show();
        }
    };

    public interface OnMenuItemClick{
        void onMenuItemClick();
    }

    public void setMenuEnable(boolean i){
        this.setMenuEnable = i;
    }

    public void setPopBack(boolean i){
        this.setPopBack = i;
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
}
