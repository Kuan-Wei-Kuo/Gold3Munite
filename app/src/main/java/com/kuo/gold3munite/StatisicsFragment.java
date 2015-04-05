package com.kuo.gold3munite;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RemoteViews;

/**
 * Created by User on 2015/4/5.
 */
public class StatisicsFragment extends Fragment {

    private Button notificationButton;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statisics, container, false);

        notificationButton = (Button) view.findViewById(R.id.notificationButton);

        notificationButton.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View view) {
                RemoteViews contentViews = new RemoteViews(getActivity().getPackageName(), R.layout.status_notification_item);
                contentViews.setImageViewResource(R.id.icon, R.mipmap.ic_launcher);
                contentViews.setTextViewText(R.id.title, "黃金三分鐘");
                contentViews.setTextViewText(R.id.englishText, "Poor");
                contentViews.setTextViewText(R.id.chineseText, "貧窮");

                Intent intent = new Intent(getActivity(), MainActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(), 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

                Notification notification = new Notification();
                notification.bigContentView = contentViews;
                notification.icon=R.mipmap.ic_launcher;
                notification.defaults=Notification.DEFAULT_SOUND;
                notification.setLatestEventInfo(getActivity(),"黃金三分鐘","趕快記錄吧！", pendingIntent );
                NotificationManager notificationManager=(NotificationManager)getActivity().getSystemService(getActivity().NOTIFICATION_SERVICE);
                notificationManager.notify(0, notification);
            }
        });

        return view;
    }
}
