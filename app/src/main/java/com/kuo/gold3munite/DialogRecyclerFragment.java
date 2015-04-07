package com.kuo.gold3munite;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 2015/4/6.
 */
public class DialogRecyclerFragment extends DialogFragment{

    public static final int CHECK_BOX = 0;
    public static final int RADIO_BUTTON = 1;

    private TextView title;
    private RecyclerView recyclerView;
    private G3MRecyclerAdapter g3MRecyclerAdapter;
    private LinearLayoutManager linearLayoutManager;
    private List<ListItem> listItems = new ArrayList<ListItem>();
    private Button cancel, enter;
    private OnWeekText onWeekText;
    private OnTypeText onTypeText;

    static DialogRecyclerFragment newIntance(int layoutId, int TYPE, String[] contentArray, String title){

        DialogRecyclerFragment dialogRecyclerFragment = new DialogRecyclerFragment();

        Bundle bundle = new Bundle();
        bundle.putInt("layoutId", layoutId);
        bundle.putInt("TYPE", TYPE);
        bundle.putStringArray("contentArray", contentArray);
        bundle.putString("title", title);
        dialogRecyclerFragment.setArguments(bundle);

        return dialogRecyclerFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        View view = inflater.inflate(R.layout.dialog_recycler, container, false);

        title = (TextView) view.findViewById(R.id.title);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        cancel = (Button) view.findViewById(R.id.cancel);
        enter = (Button) view.findViewById(R.id.enter);

        title.setText(getArguments().getString("title"));

        if(getArguments().getInt("TYPE") == CHECK_BOX){

            String[] week = getArguments().getStringArray("contentArray");

            for(int i = 0 ; i < 7 ; i++){
                ListItem listItem = new ListItem();
                listItem.chineseText = "星期" + week[i];
                listItems.add(listItem);
            }

            g3MRecyclerAdapter = new G3MRecyclerAdapter(getArguments().getInt("layoutId"), listItems, G3MRecyclerAdapter.DIALOG_WEEK, null);

        }else if(getArguments().getInt("TYPE") == RADIO_BUTTON) {

            String[] week = getArguments().getStringArray("contentArray");
            for (int i = 0; i < 3; i++) {
                ListItem listItem = new ListItem();
                listItem.chineseText = week[i];
                listItems.add(listItem);
            }

            g3MRecyclerAdapter = new G3MRecyclerAdapter(getArguments().getInt("layoutId"), listItems, G3MRecyclerAdapter.DIALOG_TYPE, null);
        }

        linearLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(g3MRecyclerAdapter);

        cancel.setOnClickListener(buttonClick);
        enter.setOnClickListener(buttonClick);

        return view;
    }

    private Button.OnClickListener buttonClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.cancel:
                    getDialog().dismiss();
                    break;
                case R.id.enter:
                    if(getArguments().getInt("TYPE") == CHECK_BOX){
                        onWeekText = (OnWeekText) getTargetFragment();
                        onWeekText.getWeekText(getWeekText());
                    }else if(getArguments().getInt("TYPE") == RADIO_BUTTON) {
                        onTypeText = (OnTypeText) getTargetFragment();
                        onTypeText.getTypeText(getTypeText());
                    }
                    getDialog().dismiss();
                    break;
            }
        }
    };

    private String getWeekText(){
        List<ListItem> listItems = new ArrayList<ListItem>();
        listItems = g3MRecyclerAdapter.getListItems();
        String weekText = "";
        for(int i = 0 ; i < listItems.size() ; i++){
            if(listItems.get(i).check){
                weekText += listItems.get(i).chineseText + "、";
            }
        }
        return weekText;
    }

    private String getTypeText(){
        List<ListItem> listItems = new ArrayList<ListItem>();
        listItems = g3MRecyclerAdapter.getListItems();
        String typeText = "";
        for(int i = 0 ; i < listItems.size() ; i++){
            if(listItems.get(i).check){
                typeText = listItems.get(i).chineseText ;
            }
        }
        return typeText;
    }

    public interface OnWeekText{
        void getWeekText(String weekText);
    }

    public interface OnTypeText{
        void getTypeText(String typeText);
    }
}
