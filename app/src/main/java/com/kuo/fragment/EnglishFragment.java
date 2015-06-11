package com.kuo.fragment;

import android.annotation.TargetApi;
import android.content.Intent;
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

import com.kuo.gold3munite.DetailEnglishActivity;
import com.kuo.gold3munite.EnglishLoadingTask;
import com.kuo.gold3munite.G3MRecyclerAdapter;
import com.kuo.gold3munite.G3MSQLite;
import com.kuo.gold3munite.ListItem;
import com.kuo.gold3munite.R;

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
    private Boolean firstRun = false;
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

        initializeView(view);

        return view;
    }

    private void initializeView(View view){

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        linearLayoutManager = new LinearLayoutManager(view.getContext());
        g3MRecyclerAdapter = new G3MRecyclerAdapter(R.layout.list_item_english, new EnglishLoadingTask().doInBackground(g3MSQLite.getEnglish()), G3MRecyclerAdapter.ENGLISH, listItemClickListener);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(g3MRecyclerAdapter);

    }

    private G3MRecyclerAdapter.OnItemClickListener listItemClickListener = new G3MRecyclerAdapter.OnItemClickListener() {
        @Override
        public void onClick(long rowId, int position) {

            Intent intent = new Intent();
            Bundle bundle = new Bundle();

            bundle.putInt("rowId", (int) rowId);

            intent.setClass(getActivity(), DetailEnglishActivity.class);
            intent.putExtras(bundle);

            startActivity(intent);
            getActivity().finish();
        }
    };
}
