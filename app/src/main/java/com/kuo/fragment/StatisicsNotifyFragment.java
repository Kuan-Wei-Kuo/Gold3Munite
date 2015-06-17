package com.kuo.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kuo.gold3munite.G3MSQLite;
import com.kuo.gold3munite.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * Created by User on 2015/5/3.
 */
public class StatisicsNotifyFragment extends Fragment {

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy:MM:dd");
    private SimpleDateFormat simpleDayFormat = new SimpleDateFormat("MM.dd");
    private LineChartView lineChartView;
    private G3MSQLite g3MSQLite;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        g3MSQLite = new G3MSQLite(getActivity());
        g3MSQLite.OpenDB();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_notify_statisics, container, false);

        lineChartView = (LineChartView) view.findViewById(R.id.lineChartView);
        setCharts();
        g3MSQLite.CloseDB();

        return view;
    }

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

            cursorEnglishOfDay = g3MSQLite.getStatisics(dateFormat.format(calendar.getTime()), "english", "notify");
            cursorMathOfDay = g3MSQLite.getStatisics(dateFormat.format(calendar.getTime()), "math", "notify");
            cursorPhysicsOfDay = g3MSQLite.getStatisics(dateFormat.format(calendar.getTime()), "physics", "notify");

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
        data.setValueLabelsTextColor(getResources().getColor(R.color.BLUE_A700));
        data.setValueLabelTextSize(17);
        data.setAxisYLeft(axixY);
        data.setAxisXBottom(axixX);
        lineChartView.setLineChartData(data);
    }
}
