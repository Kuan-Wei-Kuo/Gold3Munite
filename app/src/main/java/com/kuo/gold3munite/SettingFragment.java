package com.kuo.gold3munite;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 2015/4/4.
 */
public class SettingFragment extends Fragment implements MaterialLinearLayout.OnAnimationListener {

    private MaterialLinearLayout weekLayout, startTimeLayout, endTimeLayout, typeLayout, areaTimeLayout;
    private RelativeLayout soundLayout, shockLayout;
    private TextView typeText, weekText, areaTimeText, startTimeText, endTimeText;
    private CheckBox soundCheckBox, shockCheckBox;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.setMenuEnable(false);
        mainActivity.toolbar.setTitle("黃金三分鐘 - 設定");
        mainActivity.toolbar.setBackgroundColor(getResources().getColor(R.color.blue_1));
        mainActivity.setSupportActionBar(mainActivity.toolbar);
        mainActivity.actionBarDrawerToggle.syncState();
        mainActivity.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        mainActivity.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        initializeUI(view);

        return view;
    }
    private void initializeUI(View view){

        soundCheckBox = (CheckBox) view.findViewById(R.id.soundCheckBox);
        shockCheckBox = (CheckBox) view.findViewById(R.id.shockCheckBox);
        typeText = (TextView) view.findViewById(R.id.typeText);
        weekText = (TextView) view.findViewById(R.id.weekText);
        startTimeText = (TextView) view.findViewById(R.id.startTimeText);
        endTimeText = (TextView) view.findViewById(R.id.endTimeText);
        areaTimeText = (TextView) view.findViewById(R.id.areaTimeText);
        weekLayout = (MaterialLinearLayout) view.findViewById(R.id.weekLayout);
        startTimeLayout = (MaterialLinearLayout) view.findViewById(R.id.startTimeLayout);
        endTimeLayout = (MaterialLinearLayout) view.findViewById(R.id.endTimeLayout);
        typeLayout = (MaterialLinearLayout) view.findViewById(R.id.typeLayout);
        areaTimeLayout = (MaterialLinearLayout) view.findViewById(R.id.areaTimeLayout);
        soundLayout = (RelativeLayout) view.findViewById(R.id.soundLayout);
        shockLayout = (RelativeLayout) view.findViewById(R.id.shockLayout);

        startTimeLayout.setOnAnimationListener(this, 0);
        endTimeLayout.setOnAnimationListener(this, 1);
        areaTimeLayout.setOnAnimationListener(this, 2);
        typeLayout.setOnAnimationListener(this, 3);
        weekLayout.setOnAnimationListener(this, 4);

        startTimeLayout.setOnClickListener(materialLinearClickListener);
        endTimeLayout.setOnClickListener(materialLinearClickListener);
        typeLayout.setOnClickListener(materialLinearClickListener);
        areaTimeLayout.setOnClickListener(materialLinearClickListener);
        weekLayout.setOnClickListener(materialLinearClickListener);
    }

    private MaterialLinearLayout.OnClickListener materialLinearClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.startTimeLayout:
                    startTimeLayout.startAnimator();
                    break;
                case R.id.endTimeLayout:
                    endTimeLayout.startAnimator();
                    break;
                case R.id.typeLayout:
                    typeLayout.startAnimator();
                    break;
                case R.id.areaTimeLayout:
                    areaTimeLayout.startAnimator();
                    break;
                case R.id.weekLayout:
                    weekLayout.startAnimator();
                    break;
            }
        }
    };

    @Override
    public void onAnimationStart(int position) {
        startTimeLayout.setClickable(false);
        endTimeLayout.setClickable(false);
        areaTimeLayout.setClickable(false);
        typeLayout.setClickable(false);
        weekLayout.setClickable(false);
    }

    @Override
    public void onAnimationEnd(int position) {
        switch (position){
            case 0:
                DialogTimePickerFragment dialogTimePickerFragment = DialogTimePickerFragment.newIntance("開始時間");
                dialogTimePickerFragment.setTargetFragment(SettingFragment.this, 0);
                dialogTimePickerFragment.show(getFragmentManager(), "dialogTimePickerFragment");
                break;
            case 1:
                dialogTimePickerFragment = DialogTimePickerFragment.newIntance("結束時間");
                dialogTimePickerFragment.setTargetFragment(SettingFragment.this, 0);
                dialogTimePickerFragment.show(getFragmentManager(), "dialogTimePickerFragment");
                break;
            case 2:
                String[] areaArray = {"半小時", "一小時", "二小時"};
                DialogRecyclerFragment dialogRecyclerFragment = DialogRecyclerFragment.newIntance(R.layout.list_item_radiobutton, DialogRecyclerFragment.RADIO_BUTTON, areaArray, "間隔時間");
                dialogRecyclerFragment.setTargetFragment(SettingFragment.this, 0);
                dialogRecyclerFragment.show(getFragmentManager(), "dialogRecyclerFragment");
                break;
            case 3:
                String[] typeArray = {"英文", "數學", "物理"};
                dialogRecyclerFragment = DialogRecyclerFragment.newIntance(R.layout.list_item_radiobutton, DialogRecyclerFragment.RADIO_BUTTON, typeArray, "類型");
                dialogRecyclerFragment.setTargetFragment(SettingFragment.this, 0);
                dialogRecyclerFragment.show(getFragmentManager(), "dialogRecyclerFragment");
                break;
            case 4:
                String[] weekArray = {"一", "二", "三", "四", "五", "六", "日"};
                dialogRecyclerFragment = DialogRecyclerFragment.newIntance(R.layout.list_item_checkbox, DialogRecyclerFragment.CHECK_BOX, weekArray, "重複");
                dialogRecyclerFragment.setTargetFragment(SettingFragment.this, 0);
                dialogRecyclerFragment.show(getFragmentManager(), "dialogRecyclerFragment");
                break;
        }
        startTimeLayout.setClickable(true);
        endTimeLayout.setClickable(true);
        areaTimeLayout.setClickable(true);
        typeLayout.setClickable(true);
        weekLayout.setClickable(true);
    }
}
