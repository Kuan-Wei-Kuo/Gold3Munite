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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

/**
 * Created by User on 2015/4/3.
 */
public class G3MService extends Service {

    private Handler handler = new Handler();
    private Calendar calendar;
    private Notification notification;
    private NotificationManager notificationManager;
    private RemoteViews contentViewsEnglish;
    private RemoteViews contentViewsScience;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("kk:mm:ss");
    private SharedPreferences settings;
    private Long stratTime;
    private Long currentTime;
    private Long tempTime;
    private Long minute;
    private Long hour;
    private int[] typeArray = {0, 1, 2};
    private Object[] weekArrays;
    private Object[] typeArrays;
    private static boolean pushState = false;

    @Override
    public void onCreate() {
        super.onCreate();
        contentViewsEnglish = new RemoteViews(getApplicationContext().getPackageName(), R.layout.status_notification_item);

        contentViewsScience = new RemoteViews(getApplicationContext().getPackageName(), R.layout.science_notification_item);
        contentViewsScience.setImageViewResource(R.id.icon, R.mipmap.ic_launcher);
        contentViewsScience.setTextViewText(R.id.title, "黃金三分鐘");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        settings = getSharedPreferences(MainActivity.SETTING_NAME, 0);

        intent = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        notification = new Notification();
        notification.bigContentView = contentViewsEnglish;
        notification.icon=R.mipmap.ic_launcher;
        notification.defaults=Notification.DEFAULT_SOUND;
        notification.setLatestEventInfo(getApplicationContext(),"黃金三分鐘","趕快記錄吧！", pendingIntent );
        notificationManager=(NotificationManager)getApplicationContext().getSystemService(getApplicationContext().NOTIFICATION_SERVICE);

        //notificationManager.notify(0, notification);

        /*try {
            stratTime = simpleDateFormat.parse(settings.getString(MainActivity.START_TIME, "")).getTime();
            currentTime = simpleDateFormat.parse(simpleDateFormat.format(new Date())).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Long tempTime = currentTime - stratTime;
        Long minute = tempTime % (1000*60*60) / (1000*60);
        Long hour = tempTime / (1000*60*60);
        Log.d("stratDate", settings.getString(MainActivity.START_TIME, ""));
        Log.d("currentDate", simpleDateFormat.format(new Date()));
        Log.d("stratTime", ""+(stratTime/(1000*60*60)));
        Log.d("currentTime", ""+(currentTime/(1000*60*60)));
        Log.d("小時", ""+ hour);
        Log.d("分鐘", ""+ minute);*/
       // Log.d("禮拜", calendar.get(Calendar.DAY_OF_WEEK)-1+"");
        Log.d("推播時間", settings.getInt(MainActivity.AREA_TIME, 0)+"");

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

            calendar = Calendar.getInstance();
            weekArrays = settings.getStringSet(MainActivity.WEEK_REPEAT, null).toArray();

            for(int i = 0 ; i < 7 ; i++){
                if((calendar.get(Calendar.DAY_OF_WEEK)-1+"").equals(weekArrays[i].toString())){
                    pushNotification();
                }
            }

            handler.postDelayed(this, 1000);
        }
    };

    private void pushNotification(){
        typeArrays = settings.getStringSet(MainActivity.TYPE, null).toArray();
        try {
            if(simpleDateFormat.parse(settings.getString(MainActivity.START_TIME, "")).compareTo(simpleDateFormat.parse(simpleDateFormat.format(new Date()))) == -1 &&
                    simpleDateFormat.parse(settings.getString(MainActivity.END_TIME, "")).compareTo(simpleDateFormat.parse(simpleDateFormat.format(new Date()))) == 1){
                stratTime = simpleDateFormat.parse(settings.getString(MainActivity.START_TIME, "")).getTime();

                currentTime = new Date().getTime();
                tempTime = currentTime - stratTime;
                minute = tempTime % (1000*60*60) / (1000*60);
                hour = tempTime / (1000*60*60);

                if((((hour*3600)+(minute*60))%settings.getInt(MainActivity.AREA_TIME, 0)) == 0){
                    if(!pushState){
                        pushState = true;
                        notificationManager.notify(0, notification);
                    }
                }else{
                    pushState = false;
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private int getRandomNumber(int maxNumber){
        Random random = new Random();

        return random.nextInt(maxNumber);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void onEnglishPushNotification(){

        contentViewsEnglish.setImageViewResource(R.id.icon, R.mipmap.ic_launcher);
        contentViewsEnglish.setTextViewText(R.id.title, "黃金三分鐘");
        contentViewsEnglish.setTextViewText(R.id.englishText, "Poor");
        contentViewsEnglish.setTextViewText(R.id.chineseText, "貧窮");

        notification.bigContentView = contentViewsEnglish;
        notificationManager.notify(0, notification);
    }
}
