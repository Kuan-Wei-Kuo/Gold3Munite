package com.kuo.gold3munite;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by User on 2015/4/6.
 */
public class SettintTimeFragment extends Fragment implements DialogRecyclerFragment.OnWeekText, DialogRecyclerFragment.OnTypeText, DialogTimePickerFragment.OnTimePicker{

    //private TimePicker timePicker;
    private Button cancel, enter;
    private TextView typeText, weekText, areaTimeText, startTimeText, endTimeText;
    private CheckBox soundCheckBox, shockCheckBox;
    private G3MSQLite g3MSQLite;
    private LinearLayout weekLayout, startTimeLayout, endTimeLayout, typeLayout, areaTimeLayout;
    private RelativeLayout soundLayout, shockLayout;
    private SlidingTabLayout slidingTabLayout;
    private ViewPager viewPager;
    private List<Fragment> fragmentList = new ArrayList<Fragment>();
    private List<String>   titleList = new ArrayList<String>();
    private int statusType = 0;
    private int statusTime = 0;
    private String _timerText, _timerTypeText, _typeText, _weekText;
    private int _state;
    private int _soundState, _shockState, intervalTime;

    static SettintTimeFragment newIntance(long rowId, int position){
        SettintTimeFragment settintTimeFragment = new SettintTimeFragment();

        Bundle bundle = new Bundle();
        bundle.putLong("rowId", rowId);
        bundle.putInt("position", position);
        settintTimeFragment.setArguments(bundle);

        return settintTimeFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        g3MSQLite = new G3MSQLite(getActivity());
        g3MSQLite.OpenDB();

        View view = inflater.inflate(R.layout.fragment_setting_item, container, false);

        //timePicker = (TimePicker) view.findViewById(R.id.timePicker);
        cancel = (Button) view.findViewById(R.id.cancel);
        enter = (Button) view.findViewById(R.id.enter);
        soundCheckBox = (CheckBox) view.findViewById(R.id.soundCheckBox);
        shockCheckBox = (CheckBox) view.findViewById(R.id.shockCheckBox);
        typeText = (TextView) view.findViewById(R.id.typeText);
        weekText = (TextView) view.findViewById(R.id.weekText);
        startTimeText = (TextView) view.findViewById(R.id.startTimeText);
        endTimeText = (TextView) view.findViewById(R.id.endTimeText);
        areaTimeText = (TextView) view.findViewById(R.id.areaTimeText);
        weekLayout = (LinearLayout) view.findViewById(R.id.weekLayout);
        startTimeLayout = (LinearLayout) view.findViewById(R.id.startTimeLayout);
        endTimeLayout = (LinearLayout) view.findViewById(R.id.endTimeLayout);
        typeLayout = (LinearLayout) view.findViewById(R.id.typeLayout);
        areaTimeLayout = (LinearLayout) view.findViewById(R.id.areaTimeLayout);
        soundLayout = (RelativeLayout) view.findViewById(R.id.soundLayout);
        shockLayout = (RelativeLayout) view.findViewById(R.id.shockLayout);

        if(getArguments().getLong("rowId") != 0){

            Cursor cursor = g3MSQLite.getNotificationTimer(getArguments().getLong("rowId"));

            _timerText = cursor.getString(1);
            _timerTypeText = cursor.getString(2);
            _typeText = cursor.getString(3);
            _weekText = cursor.getString(4);

            String[] timerArray = onStringSplit(_timerText, ";");
            String[] timerTypeArray = onStringSplit(_timerTypeText, ";");

            startTimeText.setText(timerTypeArray[0] + " - " +timerArray[0]);
            endTimeText.setText(timerTypeArray[1] + " - " +timerArray[1]);
            typeText.setText(_typeText);
            weekText.setText(_weekText);

            if(cursor.getInt(6) == 1){
                shockCheckBox.setChecked(true);
                _shockState = 1;
            }else{
                shockCheckBox.setChecked(false);
                _shockState = 0;
            }

            if(cursor.getInt(7) == 1){
                soundCheckBox.setChecked(true);
                _soundState = 1;
            }else {
                soundCheckBox.setChecked(false);
                _soundState = 0;
            }
        }else{

            _timerText = "";
            _timerTypeText = "";
            _typeText = "英文";
            _weekText = "不重複";
            _soundState = 1;
            _shockState = 1;
            intervalTime = 60;

            areaTimeText.setText("一小時");
            startTimeText.setText(_timerText);
            endTimeText.setText(_timerText);
            typeText.setText(_typeText);
            weekText.setText(_weekText);
            soundCheckBox.setChecked(true);
            shockCheckBox.setChecked(true);
        }

        soundLayout.setOnClickListener(relativeLayoutClick);
        shockLayout.setOnClickListener(relativeLayoutClick);
        startTimeLayout.setOnClickListener(linearLayoutClick);
        areaTimeLayout.setOnClickListener(linearLayoutClick);
        weekLayout.setOnClickListener(linearLayoutClick);
        startTimeLayout.setOnClickListener(linearLayoutClick);
        endTimeLayout.setOnClickListener(linearLayoutClick);
        typeLayout.setOnClickListener(linearLayoutClick);
        enter.setOnClickListener(buttonClick);
        cancel.setOnClickListener(buttonClick);

        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.setMenuEnable(false);
        mainActivity.toolbar.setTitle("黃金三分鐘 - 設定通知內容");
        mainActivity.setSupportActionBar(mainActivity.toolbar);
        mainActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mainActivity.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        return view;
    }

    private RelativeLayout.OnClickListener relativeLayoutClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.soundLayout:
                    if(soundCheckBox.isChecked()){
                        soundCheckBox.setChecked(false);
                        _soundState = 0;
                    }else {
                        soundCheckBox.setChecked(true);
                        _soundState = 1;
                    }
                    break;
                case R.id.shockLayout:
                    if(shockCheckBox.isChecked()){
                        shockCheckBox.setChecked(false);
                        _shockState = 0;
                    }else {
                        shockCheckBox.setChecked(true);
                        _shockState = 1;
                    }
                    break;
            }
        }
    };

    private LinearLayout.OnClickListener linearLayoutClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            switch (view.getId()){
                case R.id.weekLayout:
                    String[] weekArray = {"一", "二", "三", "四", "五", "六", "日"};
                    DialogRecyclerFragment dialogRecyclerFragment = DialogRecyclerFragment.newIntance(R.layout.list_item_checkbox, DialogRecyclerFragment.CHECK_BOX, weekArray, "重複");
                    dialogRecyclerFragment.setTargetFragment(SettintTimeFragment.this, 0);
                    dialogRecyclerFragment.show(getFragmentManager(), "dialogRecyclerFragment");
                    break;
                case R.id.areaTimeLayout:
                    statusType = 0;
                    String[] areaArray = {"半小時", "一小時", "二小時"};
                    dialogRecyclerFragment = DialogRecyclerFragment.newIntance(R.layout.list_item_radiobutton, DialogRecyclerFragment.RADIO_BUTTON, areaArray, "間隔時間");
                    dialogRecyclerFragment.setTargetFragment(SettintTimeFragment.this, 0);
                    dialogRecyclerFragment.show(getFragmentManager(), "dialogRecyclerFragment");
                    break;
                case R.id.typeLayout:
                    statusType = 1;
                    String[] typeArray = {"英文", "數學", "物理"};
                    dialogRecyclerFragment = DialogRecyclerFragment.newIntance(R.layout.list_item_radiobutton, DialogRecyclerFragment.RADIO_BUTTON, typeArray, "類型");
                    dialogRecyclerFragment.setTargetFragment(SettintTimeFragment.this, 0);
                    dialogRecyclerFragment.show(getFragmentManager(), "dialogRecyclerFragment");
                    break;
                case R.id.startTimeLayout:
                    statusTime = 0;
                    DialogTimePickerFragment dialogTimePickerFragment = DialogTimePickerFragment.newIntance("開始時間");
                    dialogTimePickerFragment.setTargetFragment(SettintTimeFragment.this, 0);
                    dialogTimePickerFragment.show(getFragmentManager(), "dialogTimePickerFragment");
                    break;
                case R.id.endTimeLayout:
                    statusTime = 1;
                    dialogTimePickerFragment = DialogTimePickerFragment.newIntance("結束時間");
                    dialogTimePickerFragment.setTargetFragment(SettintTimeFragment.this, 0);
                    dialogTimePickerFragment.show(getFragmentManager(), "dialogTimePickerFragment");
                    break;
            }
        }
    };

    private Button.OnClickListener buttonClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            FragmentManager fragmentManager = getFragmentManager();
            switch (view.getId()){
                case R.id.cancel:
                    fragmentManager.popBackStack();
                    break;
                case R.id.enter:
                    if(!_timerText.equals("") && !_timerTypeText.equals("")){
                        _state = 1;
                        g3MSQLite.insertNotificationTimer(_timerText, _timerTypeText, _typeText, _weekText, _state, _shockState, _soundState, intervalTime);
                        fragmentManager.popBackStack();
                    }else{
                        Toast.makeText(view.getContext(), "請正確填寫內容!", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };

    private String[] onStringSplit(String cotent, String split){
        String[] cotentArray = cotent.split(split);
        return cotentArray;
    }

    @Override
    public void getWeekText(String weekText) {
        if(!weekText.equals("")){
            _weekText = weekText;
            this.weekText.setText(weekText);
        }else {
            _weekText = weekText;
            this.weekText.setText("不重複");
        }
    }

    @Override
    public void getTypeText(String typeText) {
        if(statusType == 0){
            if(typeText.equals("半小時")){
                intervalTime = 30;
            }else if(typeText.equals("一小時")){
                intervalTime = 60;
            }else{
                intervalTime = 120;
            }
            this.areaTimeText.setText(typeText);
        }else{
            _typeText = typeText;
            this.typeText.setText(typeText);
            statusType = 0;
        }
    }

    @Override
    public void getTime(String time, String timeA) {
        if(statusTime == 0){
            if(timeA.equals("上午")){
                this.startTimeText.setBackgroundColor(getResources().getColor(R.color.white_2));
                this.startTimeText.setTextColor(getResources().getColor(R.color.black_2));
            }else{
                this.startTimeText.setBackgroundColor(getResources().getColor(R.color.black_2));
                this.startTimeText.setTextColor(getResources().getColor(R.color.white_2));
            }
            _timerText += time+";";
            _timerTypeText += timeA+";";
            this.startTimeText.setText(timeA+" - "+time);
        }else{
            if(timeA.equals("上午")){
                this.endTimeText.setBackgroundColor(getResources().getColor(R.color.white_2));
                this.endTimeText.setTextColor(getResources().getColor(R.color.black_2));
            }else{
                this.endTimeText.setBackgroundColor(getResources().getColor(R.color.black_2));
                this.endTimeText.setTextColor(getResources().getColor(R.color.white_2));
            }
            _timerText += time;
            _timerTypeText += timeA;
            this.endTimeText.setText(timeA+" - "+time);
            statusTime = 0;
        }
    }
}
