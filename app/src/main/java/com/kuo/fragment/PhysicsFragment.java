package com.kuo.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kuo.gold3munite.DetailScienceActivity;
import com.kuo.adapter.G3MRecyclerAdapter;
import com.kuo.gold3munite.G3MSQLite;
import com.kuo.adapter.ListItem;
import com.kuo.gold3munite.R;
import com.kuo.task.ScienceLoadingTask;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 2015/4/4.
 */
public class PhysicsFragment extends Fragment{

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
        g3MRecyclerAdapter = new G3MRecyclerAdapter(R.layout.list_item_science, new ScienceLoadingTask().doInBackground(g3MSQLite.getScience(G3MSQLite.PHYSICS)), G3MRecyclerAdapter.SCIENCE, onItemClickListener);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(g3MRecyclerAdapter);

        g3MSQLite.CloseDB();

    }

    private G3MRecyclerAdapter.OnItemClickListener onItemClickListener = new G3MRecyclerAdapter.OnItemClickListener() {
        @Override
        public void onClick(long rowId, int posiwtion) {

            Intent intent = new Intent();
            Bundle bundle = new Bundle();

            bundle.putInt("type", DetailScienceActivity.PHYSICS);
            bundle.putInt("rowId", (int) rowId);

            intent.setClass(getActivity(), DetailScienceActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
            getActivity().finish();

        }
    };
}
