package com.kuo.gold3munite;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {

    public Toolbar toolbar;
    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    private ListView listView;
    private boolean setMenuEnable = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        listView = (ListView) findViewById(R.id.leftDrawer);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        actionBarDrawerToggle.syncState();
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        String[] drawerMenu = {"測驗", "統計", "設定"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.drawer_list_item, drawerMenu);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(drawerItemClickListener);


        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

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
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                if(fragmentManager.getBackStackEntryCount() > 0){
                    setMenuEnable = false;
                    fragmentManager.popBackStack();
                }else{
                    if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
                        return true;
                    }
                }
                break;
            case R.id.action_append:
                item.setEnabled(false);
                Toast.makeText(this, "討厭~~死相~~", Toast.LENGTH_SHORT).show();
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

    private ListView.OnItemClickListener drawerItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            switch (i){
                case 0:
                    EnglishTestFragment englishTestFragment = new EnglishTestFragment();
                    fragmentTransaction.replace(R.id.contentFrame, englishTestFragment, "englishTestFragment");
                    fragmentTransaction.addToBackStack("mainFragment");
                    fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    fragmentTransaction.commit();
                    break;
                case 1:
                    break;
                case 2:
                    setMenuEnable = true;
                    SettingFragment settingFragment = new SettingFragment();
                    fragmentTransaction.replace(R.id.contentFrame, settingFragment, "settingFragment");
                    fragmentTransaction.addToBackStack("mainFragment");
                    fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    fragmentTransaction.commit();
                    break;
            }
        }
    };


}
