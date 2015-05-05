package com.kuo.gold3munite;

import android.annotation.TargetApi;
import android.media.Image;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 2015/4/3.
 */
public class EnglishFragment extends Fragment{

    private RecyclerView recyclerView;
    private G3MRecyclerAdapter g3MRecyclerAdapter;
    private LinearLayoutManager linearLayoutManager;
    private G3MSQLite g3MSQLite;
    private List<ListItem> listItems = new ArrayList<ListItem>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        g3MSQLite = new G3MSQLite(getActivity());
        g3MSQLite.OpenDB();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_recycler, container, false);

        linearLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        Thread uiThread = new Thread(uiRunnable);
        uiThread.start();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if(handler != null){
            handler.removeMessages(1);
            handler.removeCallbacks(uiRunnable);
        }

        g3MSQLite.CloseDB();
    }

    private G3MRecyclerAdapter.OnItemClickListener listItemClickListener = new G3MRecyclerAdapter.OnItemClickListener() {
        @Override
        public void onClick(long rowId, int position) {

            FragmentManager fragmentManager = getParentFragment().getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            ContentEnglishFragment contentEnglishFragment = ContentEnglishFragment.newIntance(rowId, position);
            fragmentTransaction.replace(R.id.contentFrame, contentEnglishFragment, "contentEnglishFragment");
            fragmentTransaction.addToBackStack("contentEnglishFragment");
            fragmentTransaction.commit();
        }
    };

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 1){
                g3MRecyclerAdapter = new G3MRecyclerAdapter(R.layout.list_item_english, listItems, G3MRecyclerAdapter.ENGLISH, listItemClickListener);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(g3MRecyclerAdapter);
            }
        }
    };

    private Runnable uiRunnable = new Runnable() {
        @Override
        public void run() {

            Message message = handler.obtainMessage(1);
            Cursor cursor = g3MSQLite.getEnglish();
            listItems.clear();

            if(cursor.getCount() != 0){
                cursor.moveToFirst();
                for (int i = 0; i < cursor.getCount() ; i++){
                    if(cursor.getInt(8) == 1){
                        ListItem listItem = new ListItem();
                        listItem.rowId = cursor.getLong(0);
                        listItem.englishText = cursor.getString(1);
                        listItem.chineseText = cursor.getString(3);
                        listItem.exampleEnglishText = cursor.getString(5);
                        listItem.exampleChineseText = cursor.getString(4);
                        listItems.add(listItem);
                    }
                    cursor.moveToNext();
                }
            }
            handler.sendMessage(message);
        }
    };
}
