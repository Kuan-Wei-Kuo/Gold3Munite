package com.kuo.gold3munite;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;

/**
 * Created by User on 2015/4/2.
 */
public class EnglishTestFragment extends Fragment {

    private ImageView soundButton;
    private TextView timerText;
    private MediaPlayer mediaPlayer = new MediaPlayer();
    private Handler handler = new Handler();
    private int scoend = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final String sound = "https://translate.google.com.tw/translate_tts?ie=UTF-8&q=peace&tl=en&total=1&idx=0&textlen=5&client=t&prev=input&sa=N";

        mediaPlayer.setAudioStreamType (AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(getActivity(), Uri.parse(sound));
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_english_test, container, false);

        soundButton = (ImageView) view.findViewById(R.id.soundButton);
        timerText = (TextView) view.findViewById(R.id.timerText);

        soundButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //MediaPlayer soundTest = MediaPlayer.create(view.getContext(), Uri.parse(sound));
                mediaPlayer.start();
            }
        });

        handler.postDelayed(runTimerStop, 1000);

        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.setDrawerListChanged(1);
        mainActivity.toolbar.setTitle("黃金三分鐘 - 測驗");
        mainActivity.toolbar.setBackgroundColor(getResources().getColor(R.color.red_2));
        mainActivity.setSupportActionBar(mainActivity.toolbar);
        mainActivity.actionBarDrawerToggle.syncState();
        mainActivity.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        mainActivity.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

        return view;
    }
    private Runnable runTimerStop = new Runnable()
    {
        @Override
        public void run()
        {
            if(scoend <= 15){
                timerText.setText("請於" + (15 - scoend) + "秒內回答題目");
                scoend++;
                handler.postDelayed(runTimerStop, 1000);
            }else{
                timerText.setText("時間結束，銘謝惠顧!");
            }
        }
    };
}
