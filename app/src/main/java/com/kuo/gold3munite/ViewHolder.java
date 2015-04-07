package com.kuo.gold3munite;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.webkit.WebView;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;

/**
 * Created by User on 2015/4/3.
 */
public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView englishText, chineseText, exampleEnglishText, exampleChineseText, scienceText, timerText, timerTypeText, typeText, weekText;
    public WebView webView;
    public CheckBox checkBox;
    public RadioButton radioButton;

    public ViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void onClick(View view) {

    }
}
