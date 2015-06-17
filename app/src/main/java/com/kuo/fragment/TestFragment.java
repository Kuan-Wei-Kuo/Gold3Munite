package com.kuo.fragment;

import android.annotation.TargetApi;
import android.database.Cursor;
import android.os.Build;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.kuo.gold3munite.G3MSQLite;
import com.kuo.gold3munite.MainActivity;
import com.kuo.common.MaterialLinearLayout;
import com.kuo.gold3munite.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * Created by User on 2015/4/2.
 */
public class TestFragment extends Fragment implements MaterialLinearLayout.OnAnimationListener {

    private LineChartView lineChartView;
    private MaterialLinearLayout englishLayout, mathLayout, physicsLayout;
    private G3MSQLite g3MSQLite;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy:MM:dd");
    private SimpleDateFormat simpleDayFormat = new SimpleDateFormat("MM.dd");

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        g3MSQLite = new G3MSQLite(getActivity());
        g3MSQLite.OpenDB();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        setToolbar();

        View view = inflater.inflate(R.layout.fragment_test, container, false);

        initializeView(view);

        setCharts();

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        g3MSQLite.CloseDB();
    }

    private void initializeView(View view){
        lineChartView = (LineChartView) view.findViewById(R.id.lineChartView);
        englishLayout = (MaterialLinearLayout) view.findViewById(R.id.englishLayout);
        mathLayout = (MaterialLinearLayout) view.findViewById(R.id.mathLayout);
        physicsLayout = (MaterialLinearLayout) view.findViewById(R.id.physicsLayout);

        englishLayout.setOnClickListener(linearLayoutClickListener);
        mathLayout.setOnClickListener(linearLayoutClickListener);
        physicsLayout.setOnClickListener(linearLayoutClickListener);

        englishLayout.setOnAnimationListener(this, 0);
        mathLayout.setOnAnimationListener(this, 1);
        physicsLayout.setOnAnimationListener(this, 2);
    }

    private MaterialLinearLayout.OnClickListener linearLayoutClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.englishLayout:
                    englishLayout.startAnimator();
                    break;
                case R.id.mathLayout:
                    mathLayout.startAnimator();
                    break;
                case R.id.physicsLayout:
                    physicsLayout.startAnimator();
                    break;
            }
        }
    };

    private void setCharts(){
        Calendar calendar = Calendar.getInstance();
        List<PointValue> englishPointValue = new ArrayList<PointValue>();
        List<PointValue> mathPointValue = new ArrayList<PointValue>();
        List<PointValue> physicsPointValue = new ArrayList<PointValue>();
        List<AxisValue> axisValuesX = new ArrayList<AxisValue>();
        Cursor cursorEnglishOfDay = null;
        Cursor cursorMathOfDay = null;
        Cursor cursorPhysicsOfDay = null;

        int[] englishRecord = new int[5];
        int[] mathRecord = new int[5];
        int[] physicsRecord = new int[5];
        int[] maxNum = new int[3];

        String[] chineseDay = new String[5];

        calendar.add(Calendar.DATE, +1);

        for(int i = 0 ; i < 5 ; i++){

            calendar.add(Calendar.DATE, -1);

            cursorEnglishOfDay = g3MSQLite.getStatisics(dateFormat.format(calendar.getTime()), "english", "test");
            cursorMathOfDay = g3MSQLite.getStatisics(dateFormat.format(calendar.getTime()), "math", "test");
            cursorPhysicsOfDay = g3MSQLite.getStatisics(dateFormat.format(calendar.getTime()), "physics", "test");

            chineseDay[i] = simpleDayFormat.format(calendar.getTime());
            englishRecord[i] = cursorEnglishOfDay.getCount();
            mathRecord[i] = cursorMathOfDay.getCount();
            physicsRecord[i] = cursorPhysicsOfDay.getCount();
        }

        for(int i = 4 ; i >= 0 ; i--){
            englishPointValue.add(new PointValue(4-i, englishRecord[i]));
            mathPointValue.add(new PointValue(4-i, mathRecord[i]));
            physicsPointValue.add(new PointValue(4-i, physicsRecord[i]));

            axisValuesX.add(new AxisValue(4-i, chineseDay[i].toCharArray()));
        }

        Arrays.sort(englishRecord);
        Arrays.sort(mathRecord);
        Arrays.sort(physicsRecord);

        maxNum[0] = englishRecord[englishRecord.length-1];
        maxNum[1] = mathRecord[mathRecord.length-1];
        maxNum[2] = physicsRecord[physicsRecord.length-1];

        Arrays.sort(maxNum);

        List<AxisValue> axisValuesY = new ArrayList<AxisValue>();
        for (int i = 0; i <= maxNum[maxNum.length-1] ; i++) {
            axisValuesY.add(new AxisValue(i));
        }

        Axis axixX = new Axis(axisValuesX).setTextSize(17).setTextColor(getResources().getColor(R.color.BLUE_A700));
        Axis axixY = new Axis(axisValuesY).setHasLines(true).setTextSize(17).setLineColor(getResources().getColor(R.color.BLUE_A700)).setTextColor(getResources().getColor(R.color.BLUE_A700));

        Line line_1 = new Line(englishPointValue).setColor(getResources().getColor(R.color.PINKY_500)).setCubic(true);
        Line line_2 = new Line(mathPointValue).setColor(getResources().getColor(R.color.BLUE_A400)).setCubic(true);
        Line line_3 = new Line(physicsPointValue).setColor(getResources().getColor(R.color.GREEN_500)).setCubic(true);

        List<Line> lines = new ArrayList<Line>();
        lines.add(line_1);
        lines.add(line_2);
        lines.add(line_3);

        LineChartData data = new LineChartData();
        data.setLines(lines);
        //data.setValueLabelsTextColor(getResources().getColor(R.color.PINKY_500));
        //data.setValueLabelTextSize(17);
        data.setAxisYLeft(axixY);
        data.setAxisXBottom(axixX);
        lineChartView.setLineChartData(data);
    }

    @Override
    public void onAnimationStart(int position) {
        //Log.d("position", ""+position);
        englishLayout.setClickable(false);
        mathLayout.setClickable(false);
        physicsLayout.setClickable(false);
    }

    @Override
    public void onAnimationEnd(int position) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        switch (position){
            case 0:
                g3MSQLite.insterStatisics(dateFormat.format(new Date()), "english", "test");
                EnglishTestFragment englishTestFragment = new EnglishTestFragment();
                fragmentTransaction.replace(R.id.contentFrame, englishTestFragment, "englishTestFragment");
                fragmentTransaction.addToBackStack("englishTestFragment");
                fragmentTransaction.commit();
                break;
            case 1:
                g3MSQLite.insterStatisics(dateFormat.format(new Date()), "math", "test");
                ScienceTestFragment mathTestFragment = ScienceTestFragment.newIntance(ScienceTestFragment.MATH);
                fragmentTransaction.replace(R.id.contentFrame, mathTestFragment, "mathTestFragment");
                fragmentTransaction.addToBackStack("mathTestFragment");
                fragmentTransaction.commit();
                break;
            case 2:
                g3MSQLite.insterStatisics(dateFormat.format(new Date()), "physics", "test");
                ScienceTestFragment physicsTestFragment = ScienceTestFragment.newIntance(ScienceTestFragment.PHYSICS);
                fragmentTransaction.replace(R.id.contentFrame, physicsTestFragment, "physicsTestFragment");
                fragmentTransaction.addToBackStack("physicsTestFragment");
                fragmentTransaction.commit();
                break;
        }
        englishLayout.setClickable(true);
        mathLayout.setClickable(true);
        physicsLayout.setClickable(true);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setToolbar(){

        Window window = getActivity().getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.BLUE_A400));

        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.setDrawerListChanged(1);
        mainActivity.setToolbarTitle("測驗");
        mainActivity.setToolbarBackgroundColor(getResources().getColor(R.color.BLUE_A400));
        mainActivity.setToolbarActionBar();
        mainActivity.syncStateActionBarDrawerToggle();
        mainActivity.setDrawerLayoutLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        mainActivity.setDrawerLayoutLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }
}
