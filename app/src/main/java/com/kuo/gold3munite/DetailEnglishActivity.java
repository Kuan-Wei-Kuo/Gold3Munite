package com.kuo.gold3munite;

import android.content.Intent;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 2015/6/9.
 */
public class DetailEnglishActivity extends ActionBarActivity {

    private ProgressBar progressBar;
    private LinearLayout mainLayout;
    private Toolbar toolbar;
    private TextView englishText, kkText, chineseText;
    private Button buttonSound;
    private MediaPlayer mediaPlayer;
    private G3MSQLite g3MSQLite;
    private Cursor cursor;
    private RecyclerView recyclerView;
    private G3MRecyclerAdapter g3MRecyclerAdapter;
    private LinearLayoutManager linearLayoutManager;
    private List<ListItem> listItems = new ArrayList<ListItem>();
    private String sound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_english);


        g3MSQLite = new G3MSQLite(this);
        g3MSQLite.OpenDB();

        Bundle bundle = getIntent().getExtras();

        if(bundle.getInt("rowId") != 0){
            Log.d("rowId", "" + bundle.getInt("rowId"));
        }

        cursor = g3MSQLite.getEnglish(bundle.getInt("rowId"));
        sound = "https://translate.google.com.tw/translate_tts?ie=UTF-8&q="+ cursor.getString(1) +"&tl=en&total=1&idx=0&textlen=5&client=t&prev=input&sa=N";

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType (AudioManager.STREAM_MUSIC);

        new Thread(mediaPlayerRunnable).start();

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
        englishText = (TextView) findViewById(R.id.englishText);
        kkText = (TextView) findViewById(R.id.kkText);
        chineseText = (TextView) findViewById(R.id.chineseText);
        buttonSound = (Button) findViewById(R.id.buttonSound);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        linearLayoutManager = new LinearLayoutManager(this);

        toolbar.setTitle("英文");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        englishText.setText(cursor.getString(1));
        chineseText.setText(cursor.getString(3));
        kkText.setText(cursor.getString(2));

        String[] exampleEnglish = {cursor.getString(5), cursor.getString(7)};
        String[] exampleChinese = {cursor.getString(4), cursor.getString(6)};

        for (int i = 0; i < exampleChinese.length ; i++){
            ListItem listItem = new ListItem();
            listItem.exampleEnglishText = exampleEnglish[i];
            listItem.exampleChineseText = exampleChinese[i];
            listItems.add(listItem);
        }

        g3MRecyclerAdapter = new G3MRecyclerAdapter(R.layout.list_item_content_english, listItems, G3MRecyclerAdapter.ENGLISH_CONTNET, null);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(g3MRecyclerAdapter);

    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if(msg.what == 1) {
                buttonSound.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mediaPlayer.start();
                    }
                });
                progressBar.setVisibility(View.GONE);
                mainLayout.setVisibility(View.VISIBLE);
            }
        }
    };

    private Runnable mediaPlayerRunnable = new Runnable() {
        @Override
        public void run() {

            Message message = handler.obtainMessage(1);

            try {
                mediaPlayer.setDataSource(getApplicationContext(), Uri.parse(sound));
                mediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }

            handler.sendMessage(message);
        }
    };
}
