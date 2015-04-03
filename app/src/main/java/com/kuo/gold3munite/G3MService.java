package com.kuo.gold3munite;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by User on 2015/4/3.
 */
public class G3MService extends Service {
    private Handler handler = new Handler();
    private SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("HH:mm:ss");
    private SharedPreferences mSharedPreferences;
    @Override
    public void onCreate() {
        super.onCreate();
        mSharedPreferences = getSharedPreferences("isInfo", MODE_PRIVATE);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        handler.postDelayed(showTime, 1000);




        super.onStart(intent, startId);

        /*Toast.makeText(this, "Service Opened", Toast.LENGTH_SHORT).show();
        NotificationManager notificationManager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        Intent notifyIntent = new Intent(this, MyActivity.class);
        notifyIntent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent appIntent = PendingIntent.getActivity(this, 0, notifyIntent,0);
        Notification notification = new Notification();
        notification.icon=R.drawable.ic_cmeal;
        notification.tickerText="notification on status bar.";
        notification.defaults=Notification.DEFAULT_SOUND;
        notification.setLatestEventInfo(this,"Title","content",appIntent);
        notificationManager.notify(0,notification);*/
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private Runnable showTime = new Runnable() {
        public void run() {
            //log目前時間
            //Log.i("time1:", mSharedPreferences.getString("notificationMorning", "")+":00");
            //Log.i("time2:", mSharedPreferences.getString("notificationLunch", "")+":00");
            //Log.i("time3:", mSharedPreferences.getString("notificationDinner", "")+":00");
            NotificationManager notificationManager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
            Intent notifyIntent = new Intent(G3MService.this, MainActivity.class);
            notifyIntent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent appIntent = PendingIntent.getActivity(G3MService.this, 0, notifyIntent,0);
            Notification notification = new Notification();
            notification.icon=R.mipmap.ic_launcher;
            notification.tickerText="別忘了記錄您的用餐熱量唷！";
            notification.defaults=Notification.DEFAULT_SOUND;
            notification.setLatestEventInfo(G3MService.this,"黃金三分鐘","趕快記錄吧！",appIntent);
            if(mSimpleDateFormat.format(new Date()).equals("04:21:00")){
                notificationManager.notify(0,notification);
            }
            handler.postDelayed(this, 1000);
        }
    };
}
