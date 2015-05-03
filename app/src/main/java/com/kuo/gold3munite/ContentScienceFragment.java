package com.kuo.gold3munite;

import android.annotation.TargetApi;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

/**
 * Created by User on 2015/4/16.
 */
public class ContentScienceFragment extends Fragment {

    public static final int MATH = 0;
    public static final int PHYSICS = 1;

    private TextView scienceText;
    private WebView webView;
    private G3MSQLite g3MSQLite;
    private Cursor cursor;
    private Handler handler = new Handler();

    static ContentScienceFragment newIntance(long rowId, int TYPE){

        ContentScienceFragment contentScienceFragment = new ContentScienceFragment();

        Bundle bundle = new Bundle();
        bundle.putLong("rowId", rowId);
        bundle.putInt("TYPE", TYPE);
        contentScienceFragment.setArguments(bundle);

        return contentScienceFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        g3MSQLite = new G3MSQLite(getActivity());
        g3MSQLite.OpenDB();
        setToolbar();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_sicence, container, false);

        scienceText = (TextView) view.findViewById(R.id.scienceText);
        webView = (WebView) view.findViewById(R.id.webView);

        webView.setWebViewClient(webViewClient);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);

        handler.post(uiRunnable);

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if(handler != null){
            handler.removeCallbacks(uiRunnable);
        }

        g3MSQLite.CloseDB();
    }

    private WebViewClient webViewClient = new WebViewClient() {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    };

    private Runnable uiRunnable = new Runnable() {
        @Override
        public void run() {
            if(getArguments().getInt("TYPE") == MATH){
                cursor = g3MSQLite.getScience(getArguments().getLong("rowId"), G3MSQLite.MATH);
                webView.loadUrl("file:///android_asset/MathFormula/"+ G3MSQLite.MATH_FORMULA_URL+cursor.getLong(0) +".html");
            }else if(getArguments().getInt("TYPE") == PHYSICS){
                cursor = g3MSQLite.getScience(getArguments().getLong("rowId"), G3MSQLite.PHYSICS);
                webView.loadUrl("file:///android_asset/PhysicsFormula/"+ G3MSQLite.PHYSICS_FORMULA_URL+cursor.getLong(0) +".JPG");
            }
            scienceText.setText(cursor.getString(1));
        }
    };

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setToolbar(){

        Window window = getActivity().getWindow();
        MainActivity mainActivity = (MainActivity) getActivity();

        mainActivity.setPopBack(true);
        mainActivity.setMenuEnable(false);
        if(getArguments().getInt("TYPE") == MATH){
            mainActivity.toolbar.setTitle("數學");
            window.setStatusBarColor(getResources().getColor(R.color.BLUE_A400));
            mainActivity.toolbar.setBackgroundColor(getResources().getColor(R.color.BLUE_A400));
        }else if(getArguments().getInt("TYPE") == PHYSICS){
            mainActivity.toolbar.setTitle("物理");
            window.setStatusBarColor(getResources().getColor(R.color.GREEN_500));
            mainActivity.toolbar.setBackgroundColor(getResources().getColor(R.color.GREEN_500));
        }
        mainActivity.setSupportActionBar(mainActivity.toolbar);
        mainActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mainActivity.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

}
