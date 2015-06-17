package com.kuo.service;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;

import com.kuo.gold3munite.DetailEnglishActivity;
import com.kuo.gold3munite.DetailScienceActivity;
import com.kuo.gold3munite.G3MSQLite;
import com.kuo.gold3munite.MainActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

/**
 * Created by User on 2015/4/3.
 */
public class G3MService extends Service {

    private int next;
    private int rowId;
    private int[] typeArray = {0};

    private boolean isSpeedGet;

    private double speed;

    private Long stratTime;
    private Long currentTime;
    private Long tempTime;
    private Long minute;
    private Long hour;

    private static boolean pushState = false;
    private boolean isReady = false;
    private boolean startGPS=false;

    private Handler handler = new Handler();
    private Calendar calendar;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("kk:mm:ss");
    private SharedPreferences settings;
    private Object[] weekArrays;
    private Object[] typeArrays;
    private G3MSQLite g3MSQLite;
    private NotificationRemoteViews notificationRemoteViews;
    private MapLocation mapLocation;

    @Override
    public void onCreate() {
        super.onCreate();

        mapLocation = new MapLocation(this);
        mapLocation.setOnLocationListener(onLocationListener);

        g3MSQLite = new G3MSQLite(this);
        g3MSQLite.OpenDB();

        notificationRemoteViews = new NotificationRemoteViews(getApplicationContext());
        getBaseContext().registerReceiver(receiver, notificationRemoteViews.getIntentFilter());
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        settings = getApplicationContext().getSharedPreferences(MainActivity.SETTING_NAME, 0);

        handler.postDelayed(showTime, 1000);

        notificationRemoteViews.onScienceRemoteViews(1);
        notificationRemoteViews.startNotification();

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

            for(int i = 0 ; i < weekArrays.length ; i++){
                if((calendar.get(Calendar.DAY_OF_WEEK)-1+"").equals(weekArrays[i].toString())){
                    pushNotification();
                }
            }

            handler.postDelayed(this, 1000);
        }
    };

    private Runnable Speed= new Runnable() {

        @Override
        public void run() {
            if (isSpeedGet) {
                if (speed >= 45) {
                    notificationRemoteViews.onEnglishRemoteViews();
                    notificationRemoteViews.startNotification();
                }
                isSpeedGet = false;
                startGPS = false;
                handler.postDelayed(this, 30000);
            } else {
                if (startGPS) {
                    handler.postDelayed(this, 1000);
                } else {
                    startGPS = true;
                    handler.postDelayed(this, 1000);
                }
            }
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
                        if(settings.getBoolean(MainActivity.SHOCK, false) && settings.getBoolean(MainActivity.SOUND, false)){
                            notificationRemoteViews.setNotificationDefaults(Notification.DEFAULT_ALL);
                        }else if(settings.getBoolean(MainActivity.SHOCK, false)){
                            notificationRemoteViews.setNotificationDefaults(Notification.DEFAULT_VIBRATE);
                        }else{
                            notificationRemoteViews.setNotificationDefaults(Notification.DEFAULT_SOUND);
                        }
                        pushState = true;
                        if(typeArrays.length >= 2){
                            if(typeArrays[getRandomNumber(typeArrays.length)].toString().equals("0")){
                                notificationRemoteViews.onEnglishRemoteViews();
                                notificationRemoteViews.startNotification();
                            }else {
                                notificationRemoteViews.onScienceRemoteViews(Integer.valueOf(typeArrays[getRandomNumber(typeArrays.length)].toString()));
                                notificationRemoteViews.startNotification();
                            }
                        }else if(typeArrays.length == 1){
                            if(typeArrays[0].toString().equals("0")){
                                notificationRemoteViews.onEnglishRemoteViews();
                                notificationRemoteViews.startNotification();
                            }else{
                                notificationRemoteViews.onScienceRemoteViews(Integer.valueOf(Integer.valueOf(typeArrays[0].toString())));
                                notificationRemoteViews.startNotification();
                            }
                        }else{
                            notificationRemoteViews.onEnglishRemoteViews();
                            notificationRemoteViews.startNotification();
                        }

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

    private MapLocation.OnLocationListener onLocationListener = new MapLocation.OnLocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            speed = location.getSpeed() * 3.6;   //*3.6  m/s 轉 km/h
            isSpeedGet = location.hasSpeed();
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals("NEXT_BUTTON")){
                if(notificationRemoteViews.getRemoteViewsTag().equals("ENGLISH")){
                    notificationRemoteViews.onEnglishRemoteViews();
                    notificationRemoteViews.startNotification();
                }else if(notificationRemoteViews.getRemoteViewsTag().equals("MATH")){
                    notificationRemoteViews.onScienceRemoteViews(1);
                    notificationRemoteViews.startNotification();
                }else if(notificationRemoteViews.getRemoteViewsTag().equals("PHYSICS")){
                    notificationRemoteViews.onScienceRemoteViews(2);
                    notificationRemoteViews.startNotification();
                }
            }else if(intent.getAction().equals("SOUND_BUTTON")) {
                isReady = true;
                notificationRemoteViews.startMediaPlayer();
            }else if(intent.getAction().equals("MORE_BUTTON")){

                notificationRemoteViews.removeNotification();

                Bundle bundle = new Bundle();
                bundle.putInt("rowId", notificationRemoteViews.getRowId());
                bundle.putInt("type", notificationRemoteViews.getScienceType());
                intent.putExtras(bundle);

                if(notificationRemoteViews.getRemoteViewsTag().equals("ENGLISH")){
                    intent.setClass(getApplicationContext(), DetailEnglishActivity.class);
                }else{
                    intent.setClass(getApplicationContext(), DetailScienceActivity.class);
                }

                startActivity(intent);
            }
        }
    };
}
