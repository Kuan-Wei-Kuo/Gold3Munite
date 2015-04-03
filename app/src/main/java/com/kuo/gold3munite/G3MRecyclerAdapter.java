package com.kuo.gold3munite;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 2015/4/3.
 */
public class G3MRecyclerAdapter extends RecyclerView.Adapter<ViewHolder> {

    public static final int ENGLISH = 0;
    public static final int SCIENCE = 1;
    public static final int PHYSICAL = 2;

    private ViewHolder viewHolder;
    private List<ListItem> listItems = new ArrayList<ListItem>();
    private int layoutId;
    private int TYPE;

    public G3MRecyclerAdapter(int layoutId, List<ListItem> listItems, int TYPE){
        this.layoutId = layoutId;
        this.listItems = listItems;
        this.TYPE = TYPE;
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
            viewHolder.webView = (WebView) view.findViewById(R.id.webView);
            viewHolder.webView.setWebViewClient(webViewClient);
            viewHolder.webView.getSettings().setJavaScriptEnabled(true);
            viewHolder.webView.setScrollContainer(false);
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        if(TYPE == ENGLISH){
            holder.englishText.setText(listItems.get(position).englishText);
            holder.chineseText.setText(listItems.get(position).chineseText);
            holder.exampleEnglishText.setText(listItems.get(position).exampleEnglishText);
            holder.exampleChineseText.setText(listItems.get(position).exampleChineseText);
        }else if(TYPE == SCIENCE){
            holder.scienceText.setText(listItems.get(position).scienceText);
            holder.webView.loadUrl(listItems.get(position).url);
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
}
