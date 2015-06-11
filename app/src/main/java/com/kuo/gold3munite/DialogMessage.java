package com.kuo.gold3munite;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by User on 2015/6/6.
 */

public class DialogMessage extends DialogFragment {

    private TextView title, content;
    private Button cancel, enter;
    private OnButtonClickListener onButtonClickListener;

    public interface OnButtonClickListener{
        void onCancelClick(Dialog dialog);
        void onEnterClick(Dialog dialog);
    }

    public void setOnButtonClickListener(OnButtonClickListener onButtonClickListener){
        this.onButtonClickListener = onButtonClickListener;
    }

    public static DialogMessage newIntance(String title, String content){

        DialogMessage dialogMessage = new DialogMessage();

        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("content", content);
        dialogMessage.setArguments(bundle);

        return dialogMessage;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_message, container, false);

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        initializeView(view);

        return view;
    }

    private void initializeView(View view){

        title = (TextView) view.findViewById(R.id.title);
        content = (TextView) view.findViewById(R.id.content);

        cancel = (Button) view.findViewById(R.id.cancel);
        enter = (Button) view.findViewById(R.id.enter);

        title.setText(getArguments().getString("title"));
        content.setText(getArguments().getString("content"));

        cancel.setOnClickListener(buttonClickListener);
        enter.setOnClickListener(buttonClickListener);
    }

    private Button.OnClickListener buttonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(onButtonClickListener != null){
                switch (view.getId()){
                    case R.id.cancel:
                        onButtonClickListener.onCancelClick(getDialog());
                        break;
                    case R.id.enter:
                        onButtonClickListener.onEnterClick(getDialog());
                        break;
                }
            }
        }
    };
}
