package com.kuo.gold3munite;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

/**
 * Created by User on 2015/4/3.
 */
public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView englishText, chineseText, exampleEnglishText, exampleChineseText, scienceText;
    public WebView webView;

    public ViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void onClick(View view) {

    }
}
