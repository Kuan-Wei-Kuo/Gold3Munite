package com.kuo.gold3munite;

import android.annotation.TargetApi;
import android.app.Service;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Random;

/**
 * Created by User on 2015/4/2.
 */
public class EnglishTestFragment extends Fragment {

    private ImageView soundButton;
    private TextView timerText, chineseText;
    private EditText englishEdit;
    private MediaPlayer mediaPlayer = new MediaPlayer();
    private Handler handler = new Handler();

    private int scoend = 0;
    private int[] englishRowId;
    private G3MSQLite g3MSQLite;
    private Cursor cursor, cursorQuestion;
    private Button clearButton, enterButton;
    private int questionCount = 0;
    private Handler medialPlayerHandler;
    private HandlerThread medialPlayerThread;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setToolbar();

        g3MSQLite = new G3MSQLite(getActivity());
        g3MSQLite.OpenDB();

        cursor = g3MSQLite.getEnglish();

        englishRowId = new int[cursor.getCount()];

        for(int i = 0 ; i < cursor.getCount() ; i++){
            englishRowId[i] = i+1;
        }

        englishRowId = setPorkerRandom(englishRowId);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_english_test, container, false);

        soundButton = (ImageView) view.findViewById(R.id.soundButton);
        timerText = (TextView) view.findViewById(R.id.timerText);
        chineseText = (TextView) view.findViewById(R.id.chineseText);
        englishEdit = (EditText) view.findViewById(R.id.englishEdit);
        clearButton = (Button) view.findViewById(R.id.clearButton);
        enterButton = (Button) view.findViewById(R.id.enterButton);

        mediaPlayer.setAudioStreamType (AudioManager.STREAM_MUSIC);

        cursorQuestion = g3MSQLite.getEnglish(englishRowId[questionCount]);

        medialPlayerThread = new HandlerThread("medialPlayerThread");
        medialPlayerThread.start();

        medialPlayerHandler = new Handler(medialPlayerThread.getLooper());
        medialPlayerHandler.post(medialPlayerRunnable);

        soundButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.start();
            }
        });
        englishEdit.setHint(setStringPorkerRandom(cursorQuestion.getString(1)));
        chineseText.setText(cursorQuestion.getString(3));

        enterButton.setOnClickListener(buttonClcikListener);
        clearButton.setOnClickListener(buttonClcikListener);

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        handler.removeCallbacks(runTimerStop);

        if(medialPlayerHandler != null){
            medialPlayerHandler.removeCallbacks(medialPlayerRunnable);
        }
        if(medialPlayerThread != null){
            medialPlayerThread.quit();
        }

        g3MSQLite.CloseDB();
        mediaPlayer.release();
    }

    @Override
    public void onStop() {
        super.onStop();
        handler.removeCallbacks(runTimerStop);
    }

    @Override
    public void onResume() {
        super.onResume();
        handler.postDelayed(runTimerStop, 1000);
    }

    private Runnable runTimerStop = new Runnable()
    {
        @Override
        public void run()
        {
            if(scoend <= 20){
                timerText.setText("請於" + (20 - scoend) + "秒內回答題目");
                scoend++;
                handler.postDelayed(runTimerStop, 1000);
            }else{

                questionCount++;
                scoend = 0;
                cursorQuestion = g3MSQLite.getEnglish(englishRowId[questionCount]);

                handler.postDelayed(runTimerStop, 1000);
                medialPlayerHandler.post(medialPlayerRunnable);

                Vibrator vibrator =  (Vibrator) getActivity().getSystemService(Service.VIBRATOR_SERVICE);
                vibrator.vibrate(500);

                timerText.setText("答題時間結束!");

                englishEdit.setHint(setStringPorkerRandom(cursorQuestion.getString(1)));
                chineseText.setText(cursorQuestion.getString(3));
            }
        }
    };


    private Runnable medialPlayerRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                mediaPlayer.reset();
                mediaPlayer.setDataSource(getActivity(), Uri.parse("https://translate.google.com.tw/translate_tts?ie=UTF-8&q="+ cursorQuestion.getString(1) +"&tl=en&total=1&idx=0&textlen=5&client=t&prev=input&sa=N"));
                mediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    private Button.OnClickListener buttonClcikListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.clearButton:
                    englishEdit.setText("");
                    break;
                case R.id.enterButton:
                    if(englishEdit.getText().toString().equals(cursorQuestion.getString(1))){
                        Toast.makeText(view.getContext(), "答案正確！", Toast.LENGTH_SHORT).show();
                        questionCount++;
                        cursorQuestion = g3MSQLite.getEnglish(englishRowId[questionCount]);
                        englishEdit.setText("");
                        englishEdit.setHint(setStringPorkerRandom(cursorQuestion.getString(1)));
                        chineseText.setText(cursorQuestion.getString(3));
                        medialPlayerHandler.post(medialPlayerRunnable);
                    }else{
                        Toast.makeText(view.getContext(), "答案錯誤！", Toast.LENGTH_SHORT).show();
                        Vibrator vibrator =  (Vibrator) getActivity().getSystemService(Service.VIBRATOR_SERVICE);
                        vibrator.vibrate(500);
                    }
                    break;
            }
        }
    };

    private int[] setPorkerRandom(int[] result){
        Random random = new Random();
        for(int i = 0; i < result.length ; i++){
            int index = random.nextInt(result.length);
            int temp = result[index];
            result[index] = result[i];
            result[i] = temp;
        }
        return result;
    }

    private String setStringPorkerRandom(String stringResult){
        int[] result = new int[stringResult.length()];
        int stringLenght = stringResult.length()/2;
        char[] porkerChar = new char[stringResult.length()];
        String porkerString = "";

        for(int i = 0 ; i < result.length ; i++){
            result[i] = i;
        }

        result = setPorkerRandom(result);

        for(int i = 0 ; i < result.length ; i++){
            if(i == result[i]){
                porkerChar[i] = '_';
            }else{
                porkerChar[i] = stringResult.charAt(i);
            }
            porkerString += porkerChar[i];
        }
        return porkerString;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setToolbar(){

        Window window = getActivity().getWindow();
        MainActivity mainActivity = (MainActivity) getActivity();

        window.setStatusBarColor(getResources().getColor(R.color.PINKY_500));
        mainActivity.setPopBack(true);
        mainActivity.setMenuEnable(false);
        mainActivity.toolbar.setTitle("英文測驗");
        mainActivity.toolbar.setBackgroundColor(getResources().getColor(R.color.PINKY_500));
        mainActivity.setSupportActionBar(mainActivity.toolbar);
        mainActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mainActivity.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }
}
