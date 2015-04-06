package com.kuo.gold3munite;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by User on 2015/4/5.
 */
public class StatisicsFragment extends Fragment {

    private Button notificationButton;
    private Calendar calendar = Calendar.getInstance();

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

                /*G3MSQLite g3MSQLite = new G3MSQLite(getActivity());
                g3MSQLite.OpenDB();

                Cursor cursor = g3MSQLite.getNotificationTimer();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM.dd");
                SimpleDateFormat simpleDateFormatA = new SimpleDateFormat("A");
                if(cursor.getCount() != 0){
                    cursor.moveToFirst();
                    for(int i = 0 ; i < cursor.getCount() ; i++){
                        for(int j = 0 ; j < cursor.getString(4).length() ; j++){
                            if(cursor.getString(4).charAt(i) == onDatWeekChinese(calendar).charAt(0) && cursor.getString(2).equals(simpleDateFormatA.format(new Date()))
                                    && cursor.getString(1).equals(simpleDateFormat.format(new Date()))){

                                notificationManager.notify(0, notification);

                            }
                        }
                        cursor.moveToNext();
                    }
                }*/
                notificationManager.notify(0, notification);
            }
        });

        return view;
    }

    private String onDatWeekChinese(Calendar calendar){

        String[] chineseDay = {"一","二","三","四","五","六","日"};

        return chineseDay[calendar.get(Calendar.DAY_OF_WEEK)-1];
    }
}
