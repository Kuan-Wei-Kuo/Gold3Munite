package com.kuo.gold3munite;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 2015/4/3.
 */
public class MathFragment extends Fragment {

    private RecyclerView recyclerView;
    private G3MRecyclerAdapter g3MRecyclerAdapter;
    private LinearLayoutManager linearLayoutManager;
    private G3MSQLite g3MSQLite;
    private List<ListItem> listItems = new ArrayList<ListItem>();

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_recycler, container, false);

        g3MSQLite = new G3MSQLite(view.getContext());
        g3MSQLite.OpenDB();

        Cursor cursor = g3MSQLite.getScience(G3MSQLite.MATH);

        if(cursor.getCount() != 0){
            cursor.moveToFirst();
            for (int i = 0; i < 5 ; i++){
                if(cursor.getInt(6) == 1){
                    ListItem listItem = new ListItem();
                    listItem.rowId = cursor.getLong(0);
                    listItem.scienceText = "";
                    listItem.url = "file:///android_asset/MathFormula/"+ cursor.getString(2) +".html";
                    listItems.add(listItem);
                }
                cursor.moveToNext();
            }
        }

        g3MSQLite.CloseDB();

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(view.getContext());
        g3MRecyclerAdapter = new G3MRecyclerAdapter(R.layout.list_item_science, listItems, G3MRecyclerAdapter.SCIENCE, null);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(g3MRecyclerAdapter);

        return view;
    }
}
