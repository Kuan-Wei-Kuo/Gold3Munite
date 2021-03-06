package com.kuo.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.kuo.gold3munite.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 2015/4/3.
 */
public class G3MRecyclerAdapter extends RecyclerView.Adapter<ViewHolder> {

    public static final int ENGLISH = 0;
    public static final int SCIENCE = 1;
    public static final int SETTING = 2;
    public static final int ENGLISH_CONTNET = 3;
    public static final int SCIENCE_CONTNET = 4;
    public static final int DIALOG_WEEK = 5;
    public static final int DIALOG_TYPE = 6;
    public static final int DAWER_LIST = 7;

    private ViewHolder viewHolder;
    private List<ListItem> listItems = new ArrayList<ListItem>();
    private OnItemClickListener onItemClickListener;

    private int layoutId;
    private int TYPE;
    private int onCheck = 0;

    public G3MRecyclerAdapter(int layoutId, List<ListItem> listItems, int TYPE, OnItemClickListener onItemClickListener){
        this.layoutId = layoutId;
        this.listItems = listItems;
        this.TYPE = TYPE;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        viewHolder = new ViewHolder(view);

        if(TYPE == ENGLISH){
            viewHolder.englishText = (TextView) view.findViewById(R.id.englishText);
            viewHolder.chineseText = (TextView) view.findViewById(R.id.chineseText);
            viewHolder.exampleEnglishText = (TextView) view.findViewById(R.id.exampleEnglishText);
            viewHolder.exampleChineseText = (TextView) view.findViewById(R.id.exampleChineseText);
        }else if(TYPE == SCIENCE){
            viewHolder.scienceText = (TextView) view.findViewById(R.id.scienceText);
            //viewHolder.webView = (WebView) view.findViewById(R.id.webView);
            //viewHolder.webView.setWebViewClient(webViewClient);
            //viewHolder.webView.getSettings().setJavaScriptEnabled(true);
            //viewHolder.webView.setScrollContainer(false);
        }else if(TYPE == SETTING) {
            viewHolder.timerText = (TextView) view.findViewById(R.id.timerText);
            viewHolder.timerTypeText = (TextView) view.findViewById(R.id.timerTypeText);
            viewHolder.typeText = (TextView) view.findViewById(R.id.typeText);
            viewHolder.weekText = (TextView) view.findViewById(R.id.weekText);
        }else if(TYPE == ENGLISH_CONTNET){
            viewHolder.exampleEnglishText = (TextView) view.findViewById(R.id.exampleEnglishText);
            viewHolder.exampleChineseText = (TextView) view.findViewById(R.id.exampleChineseText);
        }else if(TYPE == DIALOG_WEEK){
            viewHolder.checkBox = (CheckBox) view.findViewById(R.id.checkBox);
            viewHolder.chineseText = (TextView) view.findViewById(R.id.chineseText);
        }else if(TYPE == DIALOG_TYPE){
            viewHolder.radioButton = (RadioButton) view.findViewById(R.id.radioButton);
            viewHolder.chineseText = (TextView) view.findViewById(R.id.chineseText);
        }else if(TYPE == DAWER_LIST){
            viewHolder.icon = (ImageView) view.findViewById(R.id.icon);
            viewHolder.chineseText = (TextView) view.findViewById(R.id.title);
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        if(TYPE == ENGLISH){
            holder.englishText.setText(listItems.get(position).englishText);
            holder.chineseText.setText(listItems.get(position).chineseText);
            holder.exampleEnglishText.setText(listItems.get(position).exampleEnglishText);
            holder.exampleChineseText.setText(listItems.get(position).exampleChineseText);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onClick(listItems.get(position).rowId, position);
                }
            });
        }else if(TYPE == SCIENCE){
            holder.scienceText.setText(listItems.get(position).scienceText);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onClick(listItems.get(position).rowId, position);
                }
            });
            //holder.webView.loadUrl(listItems.get(position).url);
        }else if(TYPE == SETTING) {
            holder.timerText.setText(listItems.get(position).timerText);
            holder.timerTypeText.setText(listItems.get(position).timerTypeText);
            holder.typeText.setText(listItems.get(position).typeText);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onClick(listItems.get(position).rowId, position);
                }
            });
        }else if(TYPE == ENGLISH_CONTNET){
            holder.exampleEnglishText.setText(listItems.get(position).exampleEnglishText);
            holder.exampleChineseText.setText(listItems.get(position).exampleChineseText);
        }else if(TYPE == DIALOG_WEEK){
            holder.chineseText.setText(listItems.get(position).chineseText);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(holder.checkBox.isChecked()){
                        listItems.get(position).check = false;
                        holder.checkBox.setChecked(false);
                    }else{
                        listItems.get(position).check = true;
                        holder.checkBox.setChecked(true);
                    }
                }
            });
        }else if(TYPE == DIALOG_TYPE){
            holder.chineseText.setText(listItems.get(position).chineseText);
            holder.radioButton.setChecked(listItems.get(position).check);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!holder.radioButton.isChecked()){
                        listItems.get(position).check = true;
                        holder.radioButton.setChecked(true);

                        for(int i = 0 ; i < listItems.size(); i++){
                            if(i != position){
                                listItems.get(i).check = false;
                            }
                        }
                        notifyDataSetChanged();
                    }
                }
            });
        }else if(TYPE == DAWER_LIST){

            holder.icon.setBackgroundResource(listItems.get(position).icon);
            holder.chineseText.setText(listItems.get(position).chineseText);
            holder.chineseText.setTextColor(holder.itemView.getResources().getColor(R.color.black_6));

            int[] icon = {R.mipmap.g3m_unfocus_icon, R.mipmap.test_unfocus_icon, R.mipmap.statisics_unfocus_icon, R.mipmap.setting_unfocus_icon};

            if(!listItems.get(position).check){
                holder.itemView.setClickable(true);
                holder.itemView.setBackgroundResource(R.drawable.background_selector);
                holder.chineseText.setTextColor(holder.itemView.getResources().getColor(R.color.black_6));
                holder.icon.setBackgroundResource(icon[position]);
            }else{
                holder.itemView.setClickable(false);
                holder.itemView.setBackgroundResource(R.color.black_4);
                holder.chineseText.setTextColor(holder.itemView.getResources().getColor(R.color.black_5));
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(!listItems.get(position).check){
                        listItems.get(position).check = true;
                        holder.itemView.setClickable(false);
                        holder.itemView.setBackgroundResource(R.color.black_2);
                        onItemClickListener.onClick(listItems.get(position).rowId, position);

                        for(int i = 0 ; i < listItems.size(); i++){
                            if(i != position){
                                listItems.get(i).check = false;
                            }
                        }
                        notifyDataSetChanged();
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public List<ListItem> getListItems(){
        return this.listItems;
    }

    public void setListItems(List<ListItem> listItems){
        this.listItems = listItems;
    }

    public interface OnItemClickListener{
        void onClick(long rowId, int posiwtion);
    }


}
