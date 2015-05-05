package com.kuo.gold3munite;

import android.annotation.TargetApi;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Explode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 2015/4/5.
 */
public class ContentEnglishFragment extends Fragment {

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
    private View view;
    private boolean isRunnbale = false;

    static ContentEnglishFragment newIntance(long rowId, int position){

        ContentEnglishFragment contentEnglishFragment = new ContentEnglishFragment();

        Bundle bundle = new Bundle();
        bundle.putLong("rowId", rowId);
        bundle.putInt("position", position);
        contentEnglishFragment.setArguments(bundle);

        return contentEnglishFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        g3MSQLite = new G3MSQLite(getActivity());
        g3MSQLite.OpenDB();
        cursor = g3MSQLite.getEnglish(getArguments().getLong("rowId"));
        sound = "https://translate.google.com.tw/translate_tts?ie=UTF-8&q="+ cursor.getString(1) +"&tl=en&total=1&idx=0&textlen=5&client=t&prev=input&sa=N";

        Thread mediaPlayerThread = new Thread(mediaPlayerRunnable);
        mediaPlayerThread.start();

        Thread uiThread = new Thread(uiRunnable);
        uiThread.start();

        setToolbar();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_content_english, container, false);

        initializeView(view);

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if(handler != null){
            handler.removeMessages(1);
            handler.removeCallbacks(uiRunnable);
        }

        if(mediaPlayer != null){
            mediaPlayer.release();
        }
        g3MSQLite.CloseDB();
    }

    private void  initializeView(View view){

        cursor = g3MSQLite.getEnglish(getArguments().getLong("rowId"));
        englishText = (TextView) view.findViewById(R.id.englishText);
        kkText = (TextView) view.findViewById(R.id.kkText);
        chineseText = (TextView) view.findViewById(R.id.chineseText);
        buttonSound = (Button) view.findViewById(R.id.buttonSound);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        linearLayoutManager = new LinearLayoutManager(view.getContext());

        englishText.setText(cursor.getString(1));
        chineseText.setText(cursor.getString(3));
        kkText.setText(cursor.getString(2));


    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setToolbar(){

        Window window = getActivity().getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.PINKY_500));

        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.setToolbarTitle("英文");
        mainActivity.setToolbarBackgroundColor(getResources().getColor(R.color.PINKY_500));
        mainActivity.setToolbarActionBar();
        mainActivity.setActionBarDisplayHomeAsUpEnabled(true);
        mainActivity.setDrawerLayoutLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if(msg.what == 1){
                buttonSound.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mediaPlayer.start();
                    }
                });
            }else if(msg.what == 2){
                g3MRecyclerAdapter = new G3MRecyclerAdapter(R.layout.list_item_content_english, listItems, G3MRecyclerAdapter.ENGLISH_CONTNET, null);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(g3MRecyclerAdapter);
            }
        }
    };

    private Runnable mediaPlayerRunnable = new Runnable() {
        @Override
        public void run() {
            Message message = handler.obtainMessage(1);
            mediaPlayer = MediaPlayer.create(getActivity(), Uri.parse(sound));
            mediaPlayer.setAudioStreamType (AudioManager.STREAM_MUSIC);
            handler.sendMessage(message);
        }
    };

    private Runnable uiRunnable = new Runnable() {
        @Override
        public void run() {

            Message message = handler.obtainMessage(2);

            String[] exampleEnglish = {cursor.getString(5), cursor.getString(7)};
            String[] exampleChinese = {cursor.getString(4), cursor.getString(6)};

            for (int i = 0; i < exampleChinese.length ; i++){
                ListItem listItem = new ListItem();
                listItem.exampleEnglishText = exampleEnglish[i];
                listItem.exampleChineseText = exampleChinese[i];
                listItems.add(listItem);
                cursor.moveToNext();
            }
            handler.sendMessage(message);
        }
    };
}
