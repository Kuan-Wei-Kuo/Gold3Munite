package com.kuo.gold3munite;

import android.support.v4.app.FragmentTransaction;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 2015/4/3.
 */
public class EnglishFragment extends Fragment implements G3MRecyclerAdapter.OnItemClickListener{

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

        listItems.clear();
        Cursor cursor = g3MSQLite.getEnglish();

        if(cursor.getCount() != 0){
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount() ; i++){
                if(cursor.getInt(8) == 1){
                    ListItem listItem = new ListItem();
                    listItem.rowId = cursor.getLong(0);
                    listItem.englishText = cursor.getString(1);
                    Log.d("englishText", cursor.getString(1));
                    listItem.chineseText = cursor.getString(3);
                    listItem.exampleEnglishText = cursor.getString(5);
                    listItem.exampleChineseText = cursor.getString(4);
                    listItems.add(listItem);
                }
                cursor.moveToNext();
            }
        }

        g3MSQLite.CloseDB();

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(view.getContext());
        g3MRecyclerAdapter = new G3MRecyclerAdapter(R.layout.list_item_english, listItems, G3MRecyclerAdapter.ENGLISH, this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(g3MRecyclerAdapter);

        return view;
    }

    @Override
    public void onClick(long rowId, int position) {

        FragmentManager fragmentManager = getParentFragment().getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        ContentEnglishFragment contentEnglishFragment = ContentEnglishFragment.newIntance(rowId, position);
        fragmentTransaction.replace(R.id.contentFrame, contentEnglishFragment, "contentEnglishFragment");
        fragmentTransaction.addToBackStack("englishFragmeny");
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.commit();
    }
}
