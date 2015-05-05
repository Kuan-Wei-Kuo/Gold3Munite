package com.kuo.gold3munite;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by User on 2015/4/4.
 */
public class SettingFragment extends Fragment implements MaterialLinearLayout.OnAnimationListener, DialogRecyclerFragment.OnCheckBoxData, DialogRecyclerFragment.OnRadioButtonData, DialogTimePickerFragment.OnTimePicker {

    private MaterialLinearLayout weekLayout, startTimeLayout, endTimeLayout, typeLayout, areaTimeLayout;
    private RelativeLayout soundLayout, shockLayout;
    private TextView typeText, weekText, areaTimeText, startTimeText, endTimeText;
    private CheckBox soundCheckBox, shockCheckBox;
    private SharedPreferences sharedPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getActivity().getSharedPreferences("Settings", 0);

        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.setDrawerListChanged(3);
        mainActivity.setToolbarTitle("設定");
        mainActivity.setToolbarBackgroundColor(getResources().getColor(R.color.BLUE_A400));
        mainActivity.setToolbarActionBar();
        mainActivity.syncStateActionBarDrawerToggle();
        mainActivity.setDrawerLayoutLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        mainActivity.setDrawerLayoutLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

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

        int[] areaArrays = {1800, 3600, 7200};
        Set<String> typeArrays = sharedPreferences.getStringSet(MainActivity.TYPE, null);
        Set<String> weekArrays = sharedPreferences.getStringSet(MainActivity.WEEK_REPEAT, null);
        Object[] typeObjects = typeArrays.toArray();
        Object[] weekObjects = weekArrays.toArray();
        String[] chineseMinArrays = {"30分鐘", "60分鐘", "120分鐘"};
        String[] chineseTypeArrays = {"英文", "數學", "物理"};
        String[] chineseDay = {"周一","周二","周三","周四","周五","周六","周日"};
        String chineseMin = "";
        String chineseType = "";
        String chineseWeek = "";

        Arrays.sort(weekObjects);

        for(int i = 0 ; i < areaArrays.length ; i++){
            if(sharedPreferences.getInt(MainActivity.AREA_TIME, 0) == areaArrays[i]){
                chineseMin = chineseMinArrays[i];
            }
        }

        for(int i = 0 ; i < typeObjects.length ; i++){
            chineseType += chineseTypeArrays[Integer.valueOf(typeObjects[i].toString())] + " ";
        }

        for(int i = 0 ; i < weekObjects.length ; i++){
            chineseWeek += chineseDay[Integer.valueOf(weekObjects[i].toString())] + " ";
        }

        startTimeText.setText(sharedPreferences.getString(MainActivity.START_TIME, ""));
        endTimeText.setText(sharedPreferences.getString(MainActivity.END_TIME, ""));
        areaTimeText.setText(chineseMin);
        typeText.setText(chineseType);
        weekText.setText(chineseWeek);
        soundCheckBox.setChecked(sharedPreferences.getBoolean(MainActivity.SOUND, false));
        shockCheckBox.setChecked(sharedPreferences.getBoolean(MainActivity.SHOCK, false));

        startTimeLayout.setOnAnimationListener(this, 0);
        endTimeLayout.setOnAnimationListener(this, 1);
        areaTimeLayout.setOnAnimationListener(this, 2);
        typeLayout.setOnAnimationListener(this, 3);
        weekLayout.setOnAnimationListener(this, 4);
        shockLayout.setOnClickListener(relativeLayoutClickListener);
        soundLayout.setOnClickListener(relativeLayoutClickListener);

        startTimeLayout.setOnClickListener(materialLinearClickListener);
        endTimeLayout.setOnClickListener(materialLinearClickListener);
        typeLayout.setOnClickListener(materialLinearClickListener);
        areaTimeLayout.setOnClickListener(materialLinearClickListener);
        weekLayout.setOnClickListener(materialLinearClickListener);
    }

    private RelativeLayout.OnClickListener relativeLayoutClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            SharedPreferences settings = getActivity().getSharedPreferences(MainActivity.SETTING_NAME, 0);
            SharedPreferences.Editor editor = settings.edit();
            switch (view.getId()){
                case R.id.soundLayout:
                    if(soundCheckBox.isChecked()){
                        soundCheckBox.setChecked(false);
                    }else{
                        soundCheckBox.setChecked(true);
                    }
                    editor.putBoolean(MainActivity.SOUND, soundCheckBox.isChecked());
                    break;
                case R.id.shockLayout:
                    if(shockCheckBox.isChecked()){
                        shockCheckBox.setChecked(false);
                    }else{
                        shockCheckBox.setChecked(true);
                    }
                    editor.putBoolean(MainActivity.SHOCK, shockCheckBox.isChecked());
                    break;
            }
            editor.commit();
        }
    };

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
                DialogTimePickerFragment dialogTimePickerFragment = DialogTimePickerFragment.newIntance("開始時間", 0);
                dialogTimePickerFragment.setTargetFragment(SettingFragment.this, 0);
                dialogTimePickerFragment.show(getFragmentManager(), "dialogTimePickerFragment");
                break;
            case 1:
                dialogTimePickerFragment = DialogTimePickerFragment.newIntance("結束時間", 1);
                dialogTimePickerFragment.setTargetFragment(SettingFragment.this, 0);
                dialogTimePickerFragment.show(getFragmentManager(), "dialogTimePickerFragment");
                break;
            case 2:
                String[] areaArray = {"30分鐘", "60分鐘", "120分鐘"};
                DialogRecyclerFragment dialogRecyclerFragment = DialogRecyclerFragment.newIntance(R.layout.list_item_radiobutton, DialogRecyclerFragment.RADIO_BUTTON, areaArray, "間隔時間", 0);
                dialogRecyclerFragment.setTargetFragment(SettingFragment.this, 0);
                dialogRecyclerFragment.show(getFragmentManager(), "dialogRecyclerFragment");
                break;
            case 3:
                String[] typeArray = {"英文", "數學", "物理"};
                dialogRecyclerFragment = DialogRecyclerFragment.newIntance(R.layout.list_item_checkbox, DialogRecyclerFragment.CHECK_BOX, typeArray, "類型", 1);
                dialogRecyclerFragment.setTargetFragment(SettingFragment.this, 0);
                dialogRecyclerFragment.show(getFragmentManager(), "dialogRecyclerFragment");
                break;
            case 4:
                String[] weekArray = {"日", "一", "二", "三", "四", "五", "六"};
                dialogRecyclerFragment = DialogRecyclerFragment.newIntance(R.layout.list_item_checkbox, DialogRecyclerFragment.CHECK_BOX, weekArray, "重複", 2);
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

    @Override
    public void getTime(int hour, int minute, int position) {

        SharedPreferences settings = getActivity().getSharedPreferences(MainActivity.SETTING_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();

        switch (position){
            case 0:
                //Log.d("開始時間", String.format("%02d", hour) + ":" + String.format("%02d", minute)+":00");
                startTimeText.setText(time(hour, minute));
                editor.putString(MainActivity.START_TIME, String.format("%02d", hour)+":" + String.format("%02d", minute)+":00");
                break;
            case 1:
                //Log.d("結束時間", String.format("%02d", hour) + ":" + String.format("%02d", minute)+":00");
                endTimeText.setText(time(hour, minute));
                editor.putString(MainActivity.END_TIME, String.format("%02d", hour) + ":" + String.format("%02d", minute) + ":00");
                break;
        }

        editor.commit();
    }

    @Override
    public void getCheckData(boolean[] count, int position) {

        SharedPreferences settings = getActivity().getSharedPreferences(MainActivity.SETTING_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();

        switch (position){
            case 1:
                editor.putStringSet(MainActivity.TYPE, getDateArrays(count));
                this.typeText.setText(getChineseText(count, 0));
                break;
            case 2:
                editor.putStringSet(MainActivity.WEEK_REPEAT, getDateArrays(count));
                this.weekText.setText(getChineseText(count, 1));
                break;
        }

        editor.commit();
    }

    @Override
    public void getRadioData(String typeText, int position) {
        SharedPreferences settings = getActivity().getSharedPreferences(MainActivity.SETTING_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();

        switch (position){
            case 0:
                if(typeText.equals("30分鐘")){
                    editor.putInt(MainActivity.AREA_TIME, 1800);
                }else if(typeText.equals("60分鐘")){
                    editor.putInt(MainActivity.AREA_TIME, 3600);
                }else{
                    editor.putInt(MainActivity.AREA_TIME, 7200);
                }
                this.areaTimeText.setText(typeText);
                break;
        }
        editor.commit();
    }

    private String time(int hour, int minute){

        String time = "";
        String timeA = "";

        if(hour > 12){
            hour -= 12;
            timeA = "下午";
        }else{
            timeA = "上午";
        }

        time = timeA + " - " + String.format("%02d", hour)+":" + String.format("%02d", minute);
        return time;
    }

    private String getChineseText(boolean[] count, int type){

        String chineseText = "";
        String[] chineseData = new String[count.length];

        if(type == 0){
            String[] chineseType = {"英文", "數學", "物理"};
            chineseData = chineseType;
        }else{
            String[] chineseDay = {"一","二","三","四","五","六","日"};
            chineseData = chineseDay;
        }

        for(int i = 0 ; i < count.length ; i++){
            if(count[i]){
                chineseText += chineseData[i]+" ";
            }
        }

        return chineseText;
    }

    private Set<String> getDateArrays(boolean[] count){

        Set<String> setArrays = new HashSet<String>();

        for(int i = 0 ; i < count.length ; i++){
            if(count[i]){
                setArrays.add(""+i);
            }
        }

        return setArrays;
    }
}
