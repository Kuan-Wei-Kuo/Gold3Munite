package com.kuo.fragment;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.Service;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.kuo.dialog.DialogMessage;
import com.kuo.gold3munite.G3MSQLite;
import com.kuo.gold3munite.MainActivity;
import com.kuo.task.MediaPlayerLoadingTask;
import com.kuo.gold3munite.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by User on 2015/4/2.
 */
public class EnglishTestFragment extends Fragment {

    private int scoend = 0;
    private int questionCount = 0;
    private int rightAnswerCount = 0;
    private int[] englishRowId;

    private TextView timerText, chineseText, questionCountText, greatText;
    private Button clearButton, enterButton, nextButton;
    private ImageView soundButton;
    private EditText englishEdit;
    private ProgressBar progressBar;
    private LinearLayout mainLayout;

    private G3MSQLite g3MSQLite;
    private Cursor cursor, cursorQuestion;

    private List<MediaPlayer> mediaPlayerList = new ArrayList<MediaPlayer>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setToolbar();

        g3MSQLite = new G3MSQLite(getActivity());
        g3MSQLite.OpenDB();

        cursor = g3MSQLite.getEnglish();

        englishRowId = new int[cursor.getCount()];

        if(cursor.getCount() != 0){
            cursor.moveToFirst();
            for(int i = 0 ; i < cursor.getCount() ; i++){
                englishRowId[i] = cursor.getInt(0);
                cursor.moveToNext();
            }
        }

        englishRowId = setPorkerRandom(englishRowId);
        new Thread(mediaPlayerRunnable).start();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_english_test, container, false);

        initializeView(view);

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mediaPlayerList.clear();
        handler.removeCallbacks(runTimerStop);
        g3MSQLite.CloseDB();
    }

    @Override
    public void onStop() {
        super.onStop();
        handler.removeCallbacks(runTimerStop);
    }

    private void initializeView(View view){

        soundButton = (ImageView) view.findViewById(R.id.soundButton);
        timerText = (TextView) view.findViewById(R.id.timerText);
        chineseText = (TextView) view.findViewById(R.id.chineseText);
        questionCountText = (TextView) view.findViewById(R.id.questionCountText);
        greatText = (TextView) view.findViewById(R.id.greatText);
        englishEdit = (EditText) view.findViewById(R.id.englishEdit);
        clearButton = (Button) view.findViewById(R.id.clearButton);
        enterButton = (Button) view.findViewById(R.id.enterButton);
        nextButton = (Button) view.findViewById(R.id.nextButton);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        mainLayout = (LinearLayout) view.findViewById(R.id.mainLayout);

        soundButton.setOnClickListener(soundButtonClickListener);
        enterButton.setOnClickListener(buttonClcikListener);
        nextButton.setOnClickListener(buttonClcikListener);
        clearButton.setOnClickListener(buttonClcikListener);

    }

    private ImageView.OnClickListener soundButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mediaPlayerList.get(questionCount).start();
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

                    if(cursorQuestion.getString(1).equals(englishEdit.getText().toString())){
                        rightAnswerCount++;
                        if(questionCount == 4){
                            DialogMessage dialogMessage = DialogMessage.newIntance("英文測驗結果", "共達對" + rightAnswerCount + "/5");
                            dialogMessage.show(getFragmentManager(), "dialog");
                            dialogMessage.setOnButtonClickListener(onButtonClickListener);
                        }else{
                            Toast.makeText(view.getContext(), "答對了！", Toast.LENGTH_SHORT).show();
                        }
                        updateText();
                    }

                    break;
                case R.id.nextButton:
                    updateText();
                    break;
            }

        }
    };

    private void updateText(){

        if(questionCount < 4){

            mediaPlayerList.get(questionCount).release(); // Clear mediaplayer memory.
            questionCount++;
            scoend = 0;

            cursorQuestion = g3MSQLite.getEnglish(englishRowId[questionCount]);
            englishEdit.setText("");
            englishEdit.setHint(setStringPorkerRandom(cursorQuestion.getString(1)));
            chineseText.setText(cursorQuestion.getString(3));
            questionCountText.setText("第 " + (questionCount+1) + " 題");
            greatText.setText("答對 " + rightAnswerCount + "題");

            if(questionCount == 4){
                nextButton.setVisibility(View.INVISIBLE);
            }
        }
    }

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
        mainActivity.setToolbarTitle("英文測驗");
        mainActivity.setToolbarBackgroundColor(getResources().getColor(R.color.PINKY_500));
        mainActivity.setToolbarActionBar();
        mainActivity.setActionBarDisplayHomeAsUpEnabled(true);
        mainActivity.setDrawerLayoutLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what){
                case 0:

                    mainLayout.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    handler.postDelayed(runTimerStop, 1000);

                    cursorQuestion = g3MSQLite.getEnglish(englishRowId[questionCount]);
                    englishEdit.setText("");
                    englishEdit.setHint(setStringPorkerRandom(cursorQuestion.getString(1)));
                    chineseText.setText(cursorQuestion.getString(3));
                    questionCountText.setText("第 " + (questionCount+1) + " 題");
                    greatText.setText("答對 " + rightAnswerCount + "題");

                    break;
                case 1:

                    updateText();

                    if(questionCount == 4){
                        nextButton.setVisibility(View.INVISIBLE);
                    }

                    break;
                case 2:
                    DialogMessage dialogMessage = DialogMessage.newIntance("英文測驗結果", "共達對" + rightAnswerCount + "/5");
                    dialogMessage.setOnButtonClickListener(onButtonClickListener);
                    dialogMessage.show(getFragmentManager(), "dialog");
                    break;
            }
        }
    };

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
                if(questionCount < 4){
                    Message message = handler.obtainMessage(1);
                    vibratorStart(500);
                    handler.postDelayed(runTimerStop, 1000);
                    handler.sendMessage(message);
                }else if(questionCount == 4){
                    Message message = handler.obtainMessage(2);
                    handler.sendMessage(message);
                }
            }
        }
    };

    private Runnable mediaPlayerRunnable = new Runnable() {
        @Override
        public void run() {
            Message message = handler.obtainMessage(0);
            for(int i = 0 ; i < 5 ; i++){
                Log.d("english", g3MSQLite.getEnglish(englishRowId[i]).getString(1));
                mediaPlayerList.add(new MediaPlayerLoadingTask(getActivity()).doInBackground(
                        "https://translate.google.com.tw/translate_tts?ie=UTF-8&q="
                        + g3MSQLite.getEnglish(englishRowId[i]).getString(1)
                        + "&tl=en&total=1&idx=0&textlen=5&client=t&prev=input&sa=N"));
            }
            handler.sendMessage(message);
        }
    };

    private DialogMessage.OnButtonClickListener onButtonClickListener = new DialogMessage.OnButtonClickListener() {

        @Override
        public void onCancelClick(Dialog dialog) {
            dialog.dismiss();
        }

        @Override
        public void onEnterClick(Dialog dialog) {
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            TestFragment testFragment = new TestFragment();
            fragmentTransaction.replace(R.id.contentFrame, testFragment, "testFragment");
            fragmentTransaction.commit();
            dialog.dismiss();
        }
    };

    private void vibratorStart(int millescoend){
        Vibrator vibrator =  (Vibrator) getActivity().getSystemService(Service.VIBRATOR_SERVICE);
        vibrator.vibrate(millescoend);
    }

}
