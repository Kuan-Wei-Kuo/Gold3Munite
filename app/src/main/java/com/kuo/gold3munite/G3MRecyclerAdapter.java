package com.kuo.gold3munite;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CheckBox;
import android.widget.TextView;

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

    private ViewHolder viewHolder;
    private List<ListItem> listItems = new ArrayList<ListItem>();
    private int layoutId;
    private int TYPE;
    private OnItemClickListener onItemClickListener;
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
            //viewHolder.scienceText = (TextView) view.findViewById(R.id.scienceText);
            viewHolder.webView = (WebView) view.findViewById(R.id.webView);
            viewHolder.webView.setWebViewClient(webViewClient);
            viewHolder.webView.getSettings().setJavaScriptEnabled(true);
            viewHolder.webView.setScrollContainer(false);
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
            //holder.scienceText.setText(listItems.get(position).scienceText);
            holder.webView.loadUrl(listItems.get(position).url);
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
                        holder.checkBox.setChecked(false);
                    }else{
                        holder.checkBox.setChecked(true);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    private WebViewClient webViewClient = new WebViewClient() {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    };

    public interface OnItemClickListener{
        void onClick(long rowId, int position);
    }
}
