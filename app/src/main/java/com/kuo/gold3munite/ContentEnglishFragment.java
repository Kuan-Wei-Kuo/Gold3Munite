package com.kuo.gold3munite;

import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    private MediaPlayer mediaPlayer = new MediaPlayer();
    private G3MSQLite g3MSQLite;
    private Cursor cursor;
    private RecyclerView recyclerView;
    private G3MRecyclerAdapter g3MRecyclerAdapter;
    private LinearLayoutManager linearLayoutManager;
    private List<ListItem> listItems = new ArrayList<ListItem>();

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

        final String sound = "https://translate.google.com.tw/translate_tts?ie=UTF-8&q="+ cursor.getString(1) +"&tl=en&total=1&idx=0&textlen=5&client=t&prev=input&sa=N";

        mediaPlayer.setAudioStreamType (AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(getActivity(), Uri.parse(sound));
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.setPopBack(true);
        mainActivity.setMenuEnable(false);
        mainActivity.toolbar.setTitle("英文");
        mainActivity.toolbar.setBackgroundColor(getResources().getColor(R.color.red_2));
        mainActivity.setSupportActionBar(mainActivity.toolbar);
        mainActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mainActivity.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_content_english, container, false);

        englishText = (TextView) view.findViewById(R.id.englishText);
        kkText = (TextView) view.findViewById(R.id.kkText);
        chineseText = (TextView) view.findViewById(R.id.chineseText);
        buttonSound = (Button) view.findViewById(R.id.buttonSound);

        buttonSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.start();
            }
        });

        englishText.setText(cursor.getString(1));
        kkText.setText(cursor.getString(2));
        chineseText.setText(cursor.getString(3));

        String[] exampleEnglish = {cursor.getString(5), cursor.getString(7)};
        String[] exampleChinese = {cursor.getString(4), cursor.getString(6)};

        for (int i = 0; i < 2 ; i++){
            ListItem listItem = new ListItem();
            listItem.exampleEnglishText = exampleEnglish[i];
            listItem.exampleChineseText = exampleChinese[i];
            listItems.add(listItem);
            cursor.moveToNext();
        }

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(view.getContext());
        g3MRecyclerAdapter = new G3MRecyclerAdapter(R.layout.list_item_content_english, listItems, G3MRecyclerAdapter.ENGLISH_CONTNET, null);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(g3MRecyclerAdapter);

        g3MSQLite.CloseDB();

        return view;
    }
}
