package com.kuo.gold3munite;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.TimePicker;

/**
 * Created by User on 2015/4/6.
 */
public class SettintTimeFragment extends Fragment {

    private TimePicker timePicker;
    private Button cancel, enter;
    private TextView typeText, weekText;
    private CheckBox soundCheckBox, shockCheckBox;
    private G3MSQLite g3MSQLite;

    static SettintTimeFragment newIntance(long rowId, int position){
        SettintTimeFragment settintTimeFragment = new SettintTimeFragment();

        Bundle bundle = new Bundle();
        bundle.putLong("rowId", rowId);
        bundle.putInt("position", position);
        settintTimeFragment.setArguments(bundle);

        return settintTimeFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        g3MSQLite = new G3MSQLite(getActivity());
        g3MSQLite.OpenDB();

        View view = inflater.inflate(R.layout.fragment_setting_item, container, false);

        timePicker = (TimePicker) view.findViewById(R.id.timePicker);
        cancel = (Button) view.findViewById(R.id.cancel);
        enter = (Button) view.findViewById(R.id.enter);
        soundCheckBox = (CheckBox) view.findViewById(R.id.soundCheckBox);
        shockCheckBox = (CheckBox) view.findViewById(R.id.shockCheckBox);
        typeText = (TextView) view.findViewById(R.id.typeText);
        weekText = (TextView) view.findViewById(R.id.weekText);


        enter.setOnClickListener(buttonClick);
        cancel.setOnClickListener(buttonClick);

        return view;
    }

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
