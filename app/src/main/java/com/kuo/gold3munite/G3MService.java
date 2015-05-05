package com.kuo.gold3munite;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import java.util.List;
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
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy:MM:dd");
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("kk:mm:ss");
    private SharedPreferences settings;
    private Long stratTime;
    private Long currentTime;
    private Long tempTime;
    private Long minute;
    private Long hour;
    private int[] typeArray = {0};
    private Object[] weekArrays;
    private Object[] typeArrays;
    private static boolean pushState = false;
    private G3MSQLite g3MSQLite;
    private int AppearedAppID;  //紀錄鬧鐘是否出現過

    @Override
    public void onCreate() {
        super.onCreate();

        g3MSQLite = new G3MSQLite(this);
        g3MSQLite.OpenDB();

        contentViewsEnglish = new RemoteViews(getApplicationContext().getPackageName(), R.layout.status_notification_item);
        contentViewsScience = new RemoteViews(getApplicationContext().getPackageName(), R.layout.science_notification_item);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("NEXT_BUTTON");
        getApplicationContext().registerReceiver(receiver, intentFilter);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        settings = getSharedPreferences(MainActivity.SETTING_NAME, 0);

        intent = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        notification = new Notification.Builder(getApplicationContext())
                .setContentTitle("黃金分鐘")
                .setContentText("下拉學習更多...")
                .setColor(getResources().getColor(R.color.BLUE_A400))
                .setSmallIcon(R.mipmap.ic_launcher).build();
        notificationManager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);

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
            /*ActivityManager activityManager = (ActivityManager)  getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> forGroundActivity = activityManager.getRunningTasks(1);
            ComponentName componentInfo = forGroundActivity.get(0).topActivity;
            String activityName = componentInfo.getClassName();
            Log.d("class", activityName);*/

            for(int i = 0 ; i < weekArrays.length ; i++){
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
                        if(settings.getBoolean(MainActivity.SHOCK, false) && settings.getBoolean(MainActivity.SOUND, false)){
                            notification.defaults = Notification.DEFAULT_ALL;
                        }else if(settings.getBoolean(MainActivity.SHOCK, false)){
                            notification.defaults = Notification.DEFAULT_VIBRATE;
                        }else{
                            notification.defaults = Notification.DEFAULT_SOUND;
                        }
                        pushState = true;
                        if(typeArrays.length >= 2){
                            if(typeArrays[getRandomNumber(typeArrays.length)].toString().equals("0")){
                                onEnglishPushNotification();
                            }else {
                                onSciencePushNotification(Integer.valueOf(typeArrays[getRandomNumber(typeArrays.length)].toString()));
                            }
                        }else if(typeArrays.length == 1){
                            if(typeArrays[0].toString().equals("0")){
                                onEnglishPushNotification();
                            }else{
                                onSciencePushNotification(Integer.valueOf(typeArrays[0].toString()));
                            }
                        }else{
                            onEnglishPushNotification();
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

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void onEnglishPushNotification(){

        g3MSQLite.insterStatisics(dateFormat.format(new Date()), "english", "notify");

        Cursor cursor = g3MSQLite.getEnglish();
        Random random = new Random();
        Cursor cursorQuestion = g3MSQLite.getEnglish(random.nextInt(cursor.getCount())+1);

        contentViewsEnglish.setImageViewResource(R.id.icon, R.mipmap.ic_launcher);
        contentViewsEnglish.setTextViewText(R.id.title, "黃金三分鐘");
        contentViewsEnglish.setTextViewText(R.id.englishText, cursorQuestion.getString(1));
        contentViewsEnglish.setTextViewText(R.id.chineseText, cursorQuestion.getString(3));
        contentViewsEnglish.setTextViewText(R.id.exampleEnglishText, cursorQuestion.getString(5));
        contentViewsEnglish.setTextViewText(R.id.exampleChineseText, cursorQuestion.getString(4));

        Intent nextButtonIntent = new Intent("NEXT_BUTTON");
        PendingIntent nextButtonPendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, nextButtonIntent, 0);
        contentViewsEnglish.setOnClickPendingIntent(R.id.nextButton, nextButtonPendingIntent);

        notification.bigContentView = contentViewsEnglish;
        notificationManager.notify(0, notification);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void onSciencePushNotification(int type){

        if((type - 1) == 0){
            g3MSQLite.insterStatisics(dateFormat.format(new Date()), "math", "notify");
        }else{
            g3MSQLite.insterStatisics(dateFormat.format(new Date()), "physics", "notify");
        }

        Cursor cursor = g3MSQLite.getScience(type - 1);
        Random random = new Random();
        Cursor cursorQuestion = g3MSQLite.getScience(random.nextInt(cursor.getCount()) + 1, type - 1);

        contentViewsScience.setImageViewResource(R.id.icon, R.mipmap.ic_launcher);
        contentViewsScience.setTextViewText(R.id.title, "黃金三分鐘");
        contentViewsScience.setTextViewText(R.id.chineseText, cursorQuestion.getString(1));

        notification.bigContentView = contentViewsScience;
        notificationManager.notify(0, notification);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("NEXT_BUTTON")){
                Log.d("Click", "true");
            }
        }
    };
}
