package com.kuo.gold3munite;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by User on 2015/4/3.
 */
public class G3MService extends Service {

    private Handler handler = new Handler();
    private SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("HH:mm:ss");
    private SharedPreferences mSharedPreferences;
    private Calendar calendar = Calendar.getInstance();
    private Notification notification;
    private NotificationManager notificationManager;
    private RemoteViews contentViews;
    private RemoteViews contentViewsScience;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM.dd");
    private SimpleDateFormat simpleDateFormatA = new SimpleDateFormat("A");

    @Override
    public void onCreate() {
        super.onCreate();
        mSharedPreferences = getSharedPreferences("isInfo", MODE_PRIVATE);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        contentViews = new RemoteViews(getApplicationContext().getPackageName(), R.layout.status_notification_item);
        contentViews.setImageViewResource(R.id.icon, R.mipmap.ic_launcher);
        contentViews.setTextViewText(R.id.title, "黃金三分鐘");
        contentViews.setTextViewText(R.id.englishText, "Poor");
        contentViews.setTextViewText(R.id.chineseText, "貧窮");

        contentViewsScience = new RemoteViews(getApplicationContext().getPackageName(), R.layout.science_notification_item);
        contentViews.setImageViewResource(R.id.icon, R.mipmap.ic_launcher);
        contentViews.setTextViewText(R.id.title, "黃金三分鐘");


        intent = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        notification = new Notification();
        notification.icon=R.mipmap.ic_launcher;
        notification.defaults=Notification.DEFAULT_SOUND;
        notification.setLatestEventInfo(getApplicationContext(),"黃金三分鐘","趕快記錄吧！", pendingIntent );
        notificationManager=(NotificationManager)getApplicationContext().getSystemService(getApplicationContext().NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);

        handler.postDelayed(showTime, 1000);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private Runnable showTime = new Runnable() {
        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        public void run() {

            G3MSQLite g3MSQLite = new G3MSQLite(getApplicationContext());
            g3MSQLite.OpenDB();

            Cursor cursor = g3MSQLite.getNotificationTimer();

            if(cursor.getInt(5) == 1) {
                if (cursor.getString(3).equals("ENGLISH")) {
                    notification.bigContentView = contentViews;
                } else {
                    notification.bigContentView = contentViewsScience;
                }

                if (cursor.getCount() != 0) {
                    cursor.moveToFirst();
                    for (int i = 0; i < cursor.getCount(); i++) {
                        for (int j = 0; j < cursor.getString(4).length(); j++) {
                            if (cursor.getString(4).charAt(i) == onDatWeekChinese(calendar).charAt(0) && cursor.getString(2).equals(simpleDateFormatA.format(new Date()))
                                    && cursor.getString(1).equals(simpleDateFormat.format(new Date()))) {

                                notificationManager.notify(0, notification);

                            }
                        }
                        cursor.moveToNext();
                    }
                }
            }

            g3MSQLite.CloseDB();
            handler.postDelayed(this, 1000);
        }
    };

    private String onDatWeekChinese(Calendar calendar){

        String[] chineseDay = {"一","二","三","四","五","六","日"};

        return chineseDay[calendar.get(Calendar.DAY_OF_WEEK)-1];
    }
}
