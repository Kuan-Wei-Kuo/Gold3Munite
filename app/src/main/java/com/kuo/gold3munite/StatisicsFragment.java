package com.kuo.gold3munite;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

/**
 * Created by User on 2015/4/5.
 */
public class StatisicsFragment extends Fragment {

    private Button notificationButton;
    private Calendar calendar = Calendar.getInstance();
    private G3MSQLite g3MSQLite;
    private Cursor cursor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        g3MSQLite = new G3MSQLite(getActivity());
        g3MSQLite.OpenDB();
        cursor = g3MSQLite.getEnglish();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("NEXT_BUTTON");
        getActivity().registerReceiver(receiver, intentFilter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statisics, container, false);

        notificationButton = (Button) view.findViewById(R.id.notificationButton);

        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.setDrawerListChanged(2);
        mainActivity.setMenuEnable(false);
        mainActivity.toolbar.setTitle("黃金三分鐘 - 統計");
        mainActivity.toolbar.setBackgroundColor(getResources().getColor(R.color.blue_1));
        mainActivity.setSupportActionBar(mainActivity.toolbar);
        mainActivity.actionBarDrawerToggle.syncState();
        mainActivity.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        mainActivity.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

        notificationButton.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View view) {

                Random random = new Random();
                Cursor cursorQuestion = g3MSQLite.getEnglish(random.nextInt(cursor.getCount())+1);

                RemoteViews contentViews = new RemoteViews(getActivity().getPackageName(), R.layout.status_notification_item);
                contentViews.setImageViewResource(R.id.icon, R.mipmap.ic_launcher);
                contentViews.setTextViewText(R.id.title, "黃金三分鐘");
                contentViews.setTextViewText(R.id.englishText, cursorQuestion.getString(1));
                contentViews.setTextViewText(R.id.chineseText, cursorQuestion.getString(3));
                contentViews.setTextViewText(R.id.exampleEnglishText, cursorQuestion.getString(5));
                contentViews.setTextViewText(R.id.exampleChineseText, cursorQuestion.getString(4));

                Intent intent = new Intent(getActivity(), MainActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(), 0, intent, 0);

                Intent nextButtonIntent = new Intent("NEXT_BUTTON");
                PendingIntent nextButtonPendingIntent = PendingIntent.getBroadcast(view.getContext(), 0, nextButtonIntent, 0);
                contentViews.setOnClickPendingIntent(R.id.nextButton, nextButtonPendingIntent);


                Notification notification = new Notification.Builder(getActivity())
                        .setContentTitle("黃金分鐘...")
                        .setSmallIcon(R.mipmap.learn_icon).build();
                notification.bigContentView = contentViews;
                notification.defaults=Notification.DEFAULT_SOUND;
                NotificationManager notificationManager=(NotificationManager)getActivity().getSystemService(getActivity().NOTIFICATION_SERVICE);
                notificationManager.notify(0, notification);
            }
        });

        return view;
    }

    private String onDatWeekChinese(Calendar calendar){

        String[] chineseDay = {"一","二","三","四","五","六","日"};

        return chineseDay[calendar.get(Calendar.DAY_OF_WEEK)-1];
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("NEXT_BUTTON")){
                Toast.makeText(context, "歐耶，成功", Toast.LENGTH_SHORT).show();
            }
        }
    };
}
