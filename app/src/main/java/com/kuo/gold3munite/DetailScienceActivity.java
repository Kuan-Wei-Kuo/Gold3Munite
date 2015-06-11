package com.kuo.gold3munite;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by User on 2015/6/9.
 */
public class DetailScienceActivity extends ActionBarActivity {

    public static final int MATH = 0;
    public static final int PHYSICS = 1;

    private ProgressBar progressBar;
    private LinearLayout mainLayout;
    private Toolbar toolbar;
    private TextView scienceText;
    private WebView webView;
    private G3MSQLite g3MSQLite;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_science);

        g3MSQLite = new G3MSQLite(this);
        g3MSQLite.OpenDB();

        initializeView();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void  initializeView(){

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        mainLayout = (LinearLayout) findViewById(R.id.mainLayout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        scienceText = (TextView) findViewById(R.id.scienceText);
        webView = (WebView) findViewById(R.id.webView);

        webView.setWebViewClient(webViewClient);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);

        Bundle bundle = getIntent().getExtras();

        if(bundle.getInt("type", -1) != -1){
            if(bundle.getInt("type", -1) == MATH){
                toolbar.setTitle("數學");
                cursor = g3MSQLite.getScience(bundle.getInt("rowId"), G3MSQLite.MATH);
                webView.loadUrl("file:///android_asset/MathFormula/"+ G3MSQLite.MATH_FORMULA_URL+cursor.getLong(0) +".html");
            }else if(bundle.getInt("type", -1) == PHYSICS){
                toolbar.setTitle("物理");
                cursor = g3MSQLite.getScience(bundle.getInt("rowId"), G3MSQLite.PHYSICS);
                webView.loadUrl("file:///android_asset/PhysicsFormula/"+ G3MSQLite.PHYSICS_FORMULA_URL+cursor.getLong(0) +".JPG");
            }
        }

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        scienceText.setText(cursor.getString(1));
    }

    private WebViewClient webViewClient = new WebViewClient() {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            progressBar.setVisibility(View.GONE);
            mainLayout.setVisibility(View.VISIBLE);
        }
    };
}
