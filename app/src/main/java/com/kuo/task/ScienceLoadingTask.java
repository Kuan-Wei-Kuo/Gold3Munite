package com.kuo.task;

import android.database.Cursor;
import android.os.AsyncTask;

import com.kuo.adapter.ListItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 2015/6/3.
 */
public class ScienceLoadingTask extends AsyncTask<Cursor, Integer, List<ListItem>> {

    @Override
    public List<ListItem> doInBackground(Cursor... cursors) {

        Cursor cursor = cursors[0];
        List<ListItem> listItems = new ArrayList<ListItem>();

        if(cursor.getCount() != 0){
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount() ; i++){
                if(cursor.getInt(3) == 1){
                    ListItem listItem = new ListItem();
                    listItem.rowId = cursor.getLong(0);
                    listItem.scienceText = cursor.getString(1);
                    listItems.add(listItem);
                }
                cursor.moveToNext();
            }
        }

        return listItems;
    }
}
