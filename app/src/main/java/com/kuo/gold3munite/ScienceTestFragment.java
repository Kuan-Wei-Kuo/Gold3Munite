package com.kuo.gold3munite;

import android.annotation.TargetApi;
import android.app.Service;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

/**
 * Created by User on 2015/4/13.
 */
public class ScienceTestFragment extends Fragment {

    public static final int MATH = 0;
    public static final int PHYSICS = 1;

    private WebView questionWebView;
    private LinearLayout scrollViewLinearLayout;
    private RadioGroup radioGroup;
    private Button nextButton, answerButton, enterButton;
    private G3MSQLite g3MSQLite;
    private Cursor cursorQuestion, cursor;
    private int questionCount = 0;
    private int[] scienceRowId;
    private String answer = "";
    private boolean openAnswer = false;

    static ScienceTestFragment newIntance(int TYPE){
        ScienceTestFragment scienceTestFragment = new ScienceTestFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("TYPE", TYPE);
        scienceTestFragment.setArguments(bundle);
        return  scienceTestFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        g3MSQLite = new G3MSQLite(getActivity());
        g3MSQLite.OpenDB();
        setToolbar();
        scienceRowId = new int[cursor.getCount()];

        for(int i = 0 ; i < cursor.getCount() ; i++){
            scienceRowId[i] = i+1;
        }
        scienceRowId = setPorkerRandom(scienceRowId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_science_test, container, false);

        if(getArguments().getInt("TYPE") == MATH){
            cursorQuestion = g3MSQLite.getScience(scienceRowId[questionCount], G3MSQLite.MATH);
        }else if(getArguments().getInt("TYPE") == PHYSICS){
            cursorQuestion = g3MSQLite.getScience(scienceRowId[questionCount], G3MSQLite.PHYSICS);
        }

        initializeUI(view);

        return view;
    }

    private void initializeUI(View view){
        questionWebView = (WebView) view.findViewById(R.id.questionWebView);
        radioGroup = (RadioGroup) view.findViewById(R.id.radioGroup);
        nextButton = (Button) view.findViewById(R.id.nextButton);
        answerButton = (Button) view.findViewById(R.id.answerButton);
        enterButton = (Button) view.findViewById(R.id.enterButton);
        scrollViewLinearLayout = (LinearLayout) view.findViewById(R.id.scrollViewLinearLayout);

        questionWebView.getSettings().setJavaScriptEnabled(true);
        questionWebView.setWebViewClient(webViewClient);
        questionWebView.getSettings().setSupportZoom(true);
        questionWebView.getSettings().setBuiltInZoomControls(true);
        questionWebView.loadUrl("file:///android_asset/MathQuestion/"+ G3MSQLite.MATH_QUESTION_URL+cursorQuestion.getLong(0) +".html");

        radioGroup.setOnCheckedChangeListener(radioGroupChangeListener);
        nextButton.setOnClickListener(buttonClickListener);
        answerButton.setOnClickListener(buttonClickListener);
        enterButton.setOnClickListener(buttonClickListener);
    }

    private Button.OnClickListener buttonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            TextView answerText = new TextView(view.getContext());
            WebView answerWebView = new WebView(view.getContext());

            switch (view.getId()){
                case R.id.nextButton:
                    questionCount++;
                    if(getArguments().getInt("TYPE") == MATH){
                        cursorQuestion = g3MSQLite.getScience(scienceRowId[questionCount], G3MSQLite.MATH);
                        questionWebView.loadUrl("file:///android_asset/MathQuestion/"+ G3MSQLite.MATH_QUESTION_URL+cursorQuestion.getLong(0) +".html");
                    }else if(getArguments().getInt("TYPE") == PHYSICS){
                        cursorQuestion = g3MSQLite.getScience(scienceRowId[questionCount], G3MSQLite.PHYSICS);
                        questionWebView.loadUrl("file:///android_asset/PhysicsQuestion/"+ G3MSQLite.MATH_QUESTION_URL+cursorQuestion.getLong(0) +".JPG");
                    }
                    break;
                case R.id.answerButton:

                    if(!openAnswer){
                        openAnswer = true;
                        answerText.setText("解答：");
                        answerText.setTextColor(getResources().getColor(R.color.black_2));
                        answerWebView.getSettings().setJavaScriptEnabled(true);
                        answerWebView.setWebViewClient(webViewClient);
                        answerWebView.getSettings().setSupportZoom(true);
                        answerWebView.getSettings().setBuiltInZoomControls(true);
                        if(getArguments().getInt("TYPE") == MATH){
                            answerWebView.loadUrl("file:///android_asset/MathAnswer/" + G3MSQLite.MATH_ANSWER_URL + cursorQuestion.getLong(0) + ".html");
                        }else if(getArguments().getInt("TYPE") == PHYSICS){
                            answerWebView.loadUrl("file:///android_asset/PhysicsAnswer/" + G3MSQLite.MATH_ANSWER_URL + cursorQuestion.getLong(0) + ".JPG");
                        }
                        scrollViewLinearLayout.addView(answerText);
                        scrollViewLinearLayout.addView(answerWebView);
                    }

                    answerButton.setEnabled(false);
                    enterButton.setEnabled(false);

                    answerButton.setBackgroundResource(R.color.black_2);
                    enterButton.setBackgroundResource(R.color.black_2);

                    break;
                case R.id.enterButton:
                    if(cursorQuestion.getString(2).equals(answer)){
                        Toast.makeText(view.getContext(), "答案正確！", Toast.LENGTH_SHORT).show();
                        questionCount++;
                        if(getArguments().getInt("TYPE") == MATH){
                            cursorQuestion = g3MSQLite.getScience(scienceRowId[questionCount], G3MSQLite.MATH);
                            questionWebView.loadUrl("file:///android_asset/MathQuestion/"+ G3MSQLite.MATH_QUESTION_URL+cursorQuestion.getLong(0) +".html");
                        }else if(getArguments().getInt("TYPE") == PHYSICS){
                            cursorQuestion = g3MSQLite.getScience(scienceRowId[questionCount], G3MSQLite.PHYSICS);
                            questionWebView.loadUrl("file:///android_asset/PhysicsQuestion/"+ G3MSQLite.MATH_QUESTION_URL+cursorQuestion.getLong(0) +".JPG");
                        }
                    }else{
                        Toast.makeText(view.getContext(), "答案錯誤！", Toast.LENGTH_SHORT).show();
                        Vibrator vibrator =  (Vibrator) getActivity().getSystemService(Service.VIBRATOR_SERVICE);
                        vibrator.vibrate(500);
                    }
                    break;
            }
        }
    };

    private RadioGroup.OnCheckedChangeListener radioGroupChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int i) {
            switch (i){
                case R.id.radioA:
                    answer = "A";
                    break;
                case R.id.radioB:
                    answer = "B";
                    break;
                case R.id.radioC:
                    answer = "C";
                    break;
                case R.id.radioD:
                    answer = "D";
                    break;
            }
        }
    };

    private WebViewClient webViewClient = new WebViewClient() {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
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

    @Override
    public void onDestroy() {
        super.onDestroy();

        g3MSQLite.CloseDB();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setToolbar(){

        Window window = getActivity().getWindow();
        MainActivity mainActivity = (MainActivity) getActivity();

        if(getArguments().getInt("TYPE") == MATH){
            cursor = g3MSQLite.getScience(G3MSQLite.MATH);
            mainActivity.setToolbarTitle("數學測驗");
            window.setStatusBarColor(getResources().getColor(R.color.BLUE_A400));
            mainActivity.setToolbarBackgroundColor(getResources().getColor(R.color.BLUE_A400));
        }else if(getArguments().getInt("TYPE") == PHYSICS){
            cursor = g3MSQLite.getScience(G3MSQLite.PHYSICS);
            mainActivity.setToolbarTitle("物理測驗");
            window.setStatusBarColor(getResources().getColor(R.color.GREEN_500));
            mainActivity.setToolbarBackgroundColor(getResources().getColor(R.color.GREEN_500));
        }
        mainActivity.setToolbarActionBar();
        mainActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mainActivity.setDrawerLayoutLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }
}
