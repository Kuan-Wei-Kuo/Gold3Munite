package com.kuo.gold3munite;

import android.app.Service;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

/**
 * Created by User on 2015/4/13.
 */
public class MathTestFragment extends Fragment {

    private WebView questionWebView;
    private LinearLayout scrollViewLinearLayout;
    private RadioGroup radioGroup;
    private Button nextButton, answerButton, enterButton;
    private G3MSQLite g3MSQLite;
    private Cursor cursorQuestion;
    private int questionCount = 0;
    private int[] scienceRowId;
    private String answer = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        g3MSQLite = new G3MSQLite(getActivity());
        g3MSQLite.OpenDB();

        Cursor cursor = g3MSQLite.getScience(G3MSQLite.MATH);

        scienceRowId = new int[cursor.getCount()];

        for(int i = 0 ; i < cursor.getCount() ; i++){
            scienceRowId[i] = i+1;
        }

        scienceRowId = setPorkerRandom(scienceRowId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_science_test, container, false);

        cursorQuestion = g3MSQLite.getScience(scienceRowId[questionCount], G3MSQLite.MATH);

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
            switch (view.getId()){
                case R.id.nextButton:
                    questionCount++;
                    cursorQuestion = g3MSQLite.getScience(scienceRowId[questionCount], G3MSQLite.MATH);
                    questionWebView.loadUrl("file:///android_asset/MathQuestion/"+ G3MSQLite.MATH_QUESTION_URL+cursorQuestion.getLong(0) +".html");
                    break;
                case R.id.answerButton:

                    TextView answerText = new TextView(view.getContext());
                    answerText.setText("解答：");
                    answerText.setTextColor(getResources().getColor(R.color.black_2));

                    WebView answerWebView = new WebView(view.getContext());
                    answerWebView.getSettings().setJavaScriptEnabled(true);
                    answerWebView.setWebViewClient(webViewClient);
                    answerWebView.getSettings().setSupportZoom(true);
                    answerWebView.getSettings().setBuiltInZoomControls(true);
                    answerWebView.loadUrl("file:///android_asset/MathAnswer/" + G3MSQLite.MATH_ANSWER_URL + cursorQuestion.getLong(0) + ".html");

                    scrollViewLinearLayout.addView(answerText);
                    scrollViewLinearLayout.addView(answerWebView);

                    break;
                case R.id.enterButton:
                    if(cursorQuestion.getString(2).equals(answer)){
                        Toast.makeText(view.getContext(), "答案正確！", Toast.LENGTH_SHORT).show();
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
}
