package com.kuo.gold3munite;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 2015/4/6.
 */
public class SettintTimeFragment extends Fragment {

    //private TimePicker timePicker;
    private Button cancel, enter;
    private TextView typeText, weekText;
    private CheckBox soundCheckBox, shockCheckBox;
    private G3MSQLite g3MSQLite;
    private LinearLayout weekLayout, startTimeLayout, endTimeLayout;
    private SlidingTabLayout slidingTabLayout;
    private ViewPager viewPager;
    private List<Fragment> fragmentList = new ArrayList<Fragment>();
    private List<String>   titleList = new ArrayList<String>();


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
        weekLayout = (LinearLayout) view.findViewById(R.id.weekLayout);
        startTimeLayout = (LinearLayout) view.findViewById(R.id.startTimeLayout);
        endTimeLayout = (LinearLayout) view.findViewById(R.id.endTimeLayout);

        weekLayout.setOnClickListener(linearLayoutClick);
        startTimeLayout.setOnClickListener(linearLayoutClick);
        endTimeLayout.setOnClickListener(linearLayoutClick);
        enter.setOnClickListener(buttonClick);
        cancel.setOnClickListener(buttonClick);

        return view;
    }

    private LinearLayout.OnClickListener linearLayoutClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            DialogTimePickerFragment dialogTimePickerFragment = new DialogTimePickerFragment();
            switch (view.getId()){
                case R.id.weekLayout:
                    DialogRecyclerFragment dialogRecyclerFragment = new DialogRecyclerFragment();
                    dialogRecyclerFragment.show(getFragmentManager(), "dialogRecyclerFragment");
                    break;
                case R.id.startTimeLayout:
                    dialogTimePickerFragment.show(getFragmentManager(), "dialogTimePickerFragment");
                    break;
                case R.id.endTimeLayout:
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
                    break;
            }
        }
    };
}
