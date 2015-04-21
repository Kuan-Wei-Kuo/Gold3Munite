package com.kuo.gold3munite;

import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * Created by User on 2015/4/2.
 */
public class TestFragment extends Fragment implements MaterialLinearLayout.OnAnimationListener{

    private LineChartView lineChartView;
    private MaterialLinearLayout englishLayout, mathLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_test, container, false);

        lineChartView = (LineChartView) view.findViewById(R.id.lineChartView);
        englishLayout = (MaterialLinearLayout) view.findViewById(R.id.englishLayout);
        mathLayout = (MaterialLinearLayout) view.findViewById(R.id.mathLayout);
        englishLayout.setOnClickListener(linearLayoutClickListener);
        mathLayout.setOnClickListener(linearLayoutClickListener);
        setCharts();

        englishLayout.setOnAnimationListener(this, 0);
        mathLayout.setOnAnimationListener(this, 1);

        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.setDrawerListChanged(1);
        mainActivity.setPopBack(false);
        mainActivity.setMenuEnable(false);
        mainActivity.toolbar.setTitle("黃金三分鐘 - 測驗");
        mainActivity.toolbar.setBackgroundColor(getResources().getColor(R.color.blue_1));
        mainActivity.setSupportActionBar(mainActivity.toolbar);
        mainActivity.actionBarDrawerToggle.syncState();
        mainActivity.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        mainActivity.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

        return view;
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
            }
        }
    };

    private void setCharts(){
        List<PointValue> values = new ArrayList<PointValue>();
        values.add(new PointValue(0, 2));
        values.add(new PointValue(1, 4));
        values.add(new PointValue(2, 3));
        values.add(new PointValue(3, 4));

        List<PointValue> values2 = new ArrayList<PointValue>();
        values2.add(new PointValue(0, 3));
        values2.add(new PointValue(1, 5));
        values2.add(new PointValue(2, 4));
        values2.add(new PointValue(3, 5));

        List<PointValue> values3 = new ArrayList<PointValue>();
        values3.add(new PointValue(0, 4));
        values3.add(new PointValue(1, 6));
        values3.add(new PointValue(2, 5));
        values3.add(new PointValue(3, 6));

        List<AxisValue> axisValuesX = new ArrayList<AxisValue>();
        for (int i = 0; i < 3; ++i) {
            axisValuesX.add(new AxisValue(i, null));
        }

        List<AxisValue> axisValuesY = new ArrayList<AxisValue>();
        for (int i = 0; i < 8; ++i) {
            axisValuesY.add(new AxisValue(i, null));
        }

        Axis axixX = new Axis(axisValuesX);
        Axis axixY= new Axis(axisValuesY);
        Line line_1 = new Line(values).setColor(ChartUtils.COLOR_BLUE).setCubic(true);
        Line line_2 = new Line(values2).setColor(ChartUtils.COLOR_RED).setCubic(true);
        Line line_3 = new Line(values3).setColor(ChartUtils.COLOR_GREEN).setCubic(true);

        List<Line> lines = new ArrayList<Line>();
        lines.add(line_1);
        lines.add(line_2);
        lines.add(line_3);

        LineChartData data = new LineChartData();
        data.setLines(lines);
        data.setValueLabelsTextColor(getResources().getColor(R.color.black_2));
        data.setValueLabelTextSize(17);
        data.setAxisYLeft(axixY.setHasLines(true).setTextSize(17).setLineColor(getResources().getColor(R.color.black_2)));
        data.setAxisXBottom(axixX.setTextSize(17));
        lineChartView.setTop(20);
        lineChartView.setLineChartData(data);
    }

    @Override
    public void onAnimationStart(int position) {
        //Log.d("position", ""+position);
        englishLayout.setClickable(false);
        mathLayout.setClickable(false);
    }

    @Override
    public void onAnimationEnd(int position) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        switch (position){
            case 0:
                EnglishTestFragment englishTestFragment = new EnglishTestFragment();
                fragmentTransaction.replace(R.id.contentFrame, englishTestFragment, "englishTestFragment");
                fragmentTransaction.addToBackStack("englishTestFragment");
                fragmentTransaction.commit();
                break;
            case 1:
                ScienceTestFragment scienceTestFragment = ScienceTestFragment.newIntance(ScienceTestFragment.MATH);
                fragmentTransaction.replace(R.id.contentFrame, scienceTestFragment, "scienceTestFragment");
                fragmentTransaction.addToBackStack("scienceTestFragment");
                fragmentTransaction.commit();
                break;
        }
        englishLayout.setClickable(true);
        mathLayout.setClickable(true);
    }
}
