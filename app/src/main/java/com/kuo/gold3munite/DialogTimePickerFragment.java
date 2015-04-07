package com.kuo.gold3munite;

import android.content.res.Resources;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.lang.reflect.Field;
import java.util.Calendar;

/**
 * Created by User on 2015/4/6.
 */
public class DialogTimePickerFragment extends DialogFragment {

    private String time = "";
    private String timeA = "";
    private TimePicker timePicker;
    private Button cancel, enter;
    private OnTimePicker onTimePicker;
    private TextView title;

    public interface OnTimePicker{
        void getTime(String time, String timeA);
    }

    static DialogTimePickerFragment newIntance(String title){

        DialogTimePickerFragment dialogTimePickerFragment = new DialogTimePickerFragment();

        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        dialogTimePickerFragment.setArguments(bundle);

        return dialogTimePickerFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        View view = inflater.inflate(R.layout.fragment_timepicker, container, false);

        timePicker = (TimePicker) view.findViewById(R.id.timePicker);
        cancel = (Button) view.findViewById(R.id.cancel);
        enter = (Button) view.findViewById(R.id.enter);
        title = (TextView) view.findViewById(R.id.title);

        set_timepicker_text_colour();

        title.setText(getArguments().getString("title"));

        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int mintue = calendar.get(Calendar.MINUTE);

        if(mintue < 10){
            time = hour+":0"+mintue;
            if(hour < 10){
                time = "0"+hour+":0"+mintue;
            }
        }else if(hour < 10){
            time = "0"+hour+":0"+mintue;
            if(mintue < 10){
                time = hour+":0"+mintue;
            }
        }else{
            time = hour+":"+mintue;
            if(hour > 12){
                time = (hour-12)+":"+mintue;
            }
        }

        if(hour > 12){
            timeA = "下午";
        }else{
            timeA = "上午";
        }

        timePicker.setOnTimeChangedListener(timePickerChangeListener);
        cancel.setOnClickListener(buttonClick);
        enter.setOnClickListener(buttonClick);

        return view;
    }

    private TimePicker.OnTimeChangedListener timePickerChangeListener = new TimePicker.OnTimeChangedListener() {
        @Override
        public void onTimeChanged(TimePicker timePicker, int i, int i2) {
            int time_i = 0;
            if(i > 12){
                timeA = "下午";
                time_i = i - 12;
            }else{
                timeA = "上午";
                time_i = i;
            }

            if(i2 < 10){
                time = time_i+":0"+i2;
                if(time_i < 10){
                    time = "0"+time_i+":0"+i2;
                }
            }else if(time_i < 10){
                time = "0"+time_i+":"+i2;
                if(i2 < 10){
                    time = "0"+time_i+":0"+i2;
                }
            }else{
                time = time_i+":"+i2;
            }
        }
    };

    private Button.OnClickListener buttonClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.cancel:
                    getDialog().dismiss();
                    break;
                case R.id.enter:
                    onTimePicker = (OnTimePicker) getTargetFragment();
                    onTimePicker.getTime(time, timeA);
                    getDialog().dismiss();
                    break;
            }
        }
    };

    Resources system;

    private void set_timepicker_text_colour(){
        system = Resources.getSystem();
        int hour_numberpicker_id = system.getIdentifier("hour", "id", "android");
        int minute_numberpicker_id = system.getIdentifier("minute", "id", "android");
        int ampm_numberpicker_id = system.getIdentifier("amPm", "id", "android");

        NumberPicker hour_numberpicker = (NumberPicker) timePicker.findViewById(hour_numberpicker_id);
        NumberPicker minute_numberpicker = (NumberPicker) timePicker.findViewById(minute_numberpicker_id);
        NumberPicker ampm_numberpicker = (NumberPicker) timePicker.findViewById(ampm_numberpicker_id);


        set_numberpicker_text_colour(hour_numberpicker);
        set_numberpicker_text_colour(minute_numberpicker);
        set_numberpicker_text_colour(ampm_numberpicker);

        setDividerColor(hour_numberpicker);
        setDividerColor(minute_numberpicker);
        setDividerColor(ampm_numberpicker);
    }

    private void set_numberpicker_text_colour(NumberPicker number_picker){
        final int count = number_picker.getChildCount();
        final int color_blue = getResources().getColor(R.color.blue_1);

        for(int i = 0; i < count; i++){
            View child = number_picker.getChildAt(i);

            try{
                Field wheelpaint_field = number_picker.getClass().getDeclaredField("mSelectorWheelPaint");
                wheelpaint_field.setAccessible(true);

                ((Paint)wheelpaint_field.get(number_picker)).setColor(color_blue);
                ((EditText)child).setTextColor(color_blue);
                number_picker.invalidate();
            }
            catch(NoSuchFieldException e){
                Log.w("setNumberPickerTextColor", e);
            }
            catch(IllegalAccessException e){
                Log.w("setNumberPickerTextColor", e);
            }
            catch(IllegalArgumentException e){
                Log.w("setNumberPickerTextColor", e);
            }
        }
    }

    private void setDividerColor (NumberPicker picker) {

        java.lang.reflect.Field[] pickerFields = NumberPicker.class.getDeclaredFields();
        for (java.lang.reflect.Field pf : pickerFields) {
            if (pf.getName().equals("mSelectionDivider")) {
                pf.setAccessible(true);
                try {
                    //pf.set(picker, getResources().getColor(R.color.my_orange));
                    //Log.v(TAG,"here");
                    pf.set(picker, getResources().getDrawable(R.drawable.numpicker_divider_black));
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (Resources.NotFoundException e) {
                    e.printStackTrace();
                }
                catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }
}
