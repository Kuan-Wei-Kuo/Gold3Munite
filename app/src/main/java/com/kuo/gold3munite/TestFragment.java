package com.kuo.gold3munite;

import android.support.v4.app.FragmentTransaction;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

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
public class TestFragment extends Fragment {

    private Handler handler = new Handler();
    private int scoend = 0;
    private LineChartView lineChartView;
    private LinearLayout englishLayout, mathLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_test, container, false);

        lineChartView = (LineChartView) view.findViewById(R.id.lineChartView);
        englishLayout = (LinearLayout) view.findViewById(R.id.englishLayout);
        mathLayout = (LinearLayout) view.findViewById(R.id.mathLayout);
        englishLayout.setOnClickListener(linearLayoutClickListener);
        mathLayout.setOnClickListener(linearLayoutClickListener);
        setCharts();


        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.setDrawerListChanged(1);
        mainActivity.toolbar.setTitle("黃金三分鐘 - 測驗");
        mainActivity.toolbar.setBackgroundColor(getResources().getColor(R.color.blue_1));
        mainActivity.setSupportActionBar(mainActivity.toolbar);
        mainActivity.actionBarDrawerToggle.syncState();
        mainActivity.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        mainActivity.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

        return view;
    }

    private LinearLayout.OnClickListener linearLayoutClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            switch (view.getId()){
                case R.id.englishLayout:
                    EnglishTestFragment englishTestFragment = new EnglishTestFragment();
                    fragmentTransaction.replace(R.id.contentFrame, englishTestFragment, "englishTestFragment");
                    fragmentTransaction.addToBackStack("englishTestFragment");
                    fragmentTransaction.commit();
                    break;
                case R.id.mathLayout:
                    MathTestFragment mathTestFragment = new MathTestFragment();
                    fragmentTransaction.replace(R.id.contentFrame, mathTestFragment, "mathTestFragment");
                    fragmentTransaction.addToBackStack("mathTestFragment");
                    fragmentTransaction.commit();
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
}
