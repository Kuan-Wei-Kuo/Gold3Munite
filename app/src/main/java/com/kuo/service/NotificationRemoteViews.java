package com.kuo.service;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Build;
import android.widget.RemoteViews;

import com.kuo.gold3munite.G3MSQLite;
import com.kuo.gold3munite.MainActivity;
import com.kuo.gold3munite.R;
import com.kuo.task.MediaPlayerLoadingTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * Created by User on 2015/6/8.
 */
public class NotificationRemoteViews {

    private RemoteViews remoteViewsEnglish;
    private RemoteViews remoteViewsScience;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy:MM:dd");
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("kk:mm:ss");

    private MediaPlayer mediaPlayer;

    private Context context;
    private IntentFilter intentFilter;

    private Notification notification;
    private NotificationManager notificationManager;

    private SharedPreferences settings;

    private Intent intent;

    private int next;
    private int scienceType;
    private int rowId;

    private String tag;

    private G3MSQLite g3MSQLite;

    public NotificationRemoteViews (Context context){

        this.context = context;

        g3MSQLite = new G3MSQLite(context);
        g3MSQLite.OpenDB();

        initializeView();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void initializeView(){

        remoteViewsEnglish = new RemoteViews(context.getPackageName(), R.layout.status_notification_item);
        remoteViewsScience = new RemoteViews(context.getPackageName(), R.layout.science_notification_item);

        intentFilter = new IntentFilter();
        intentFilter.addAction("NEXT_BUTTON");
        intentFilter.addAction("SOUND_BUTTON");
        intentFilter.addAction("MORE_BUTTON");

        settings = context.getSharedPreferences(MainActivity.SETTING_NAME, 0);

        intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        setButtonClickListener();

        notification = new Notification.Builder(context)
                .setContentTitle("黃金分鐘")
                .setContentText("下拉學習更多...")
                .setSmallIcon(R.mipmap.g3m_icon).build();

        //notification.contentIntent = pendingIntent;

        notificationManager = (NotificationManager)context.getSystemService(context.NOTIFICATION_SERVICE);
    }


    public IntentFilter getIntentFilter() {
        return intentFilter;
    }

    public void setButtonClickListener(){

        Intent nextButtonIntent = new Intent("NEXT_BUTTON");
        PendingIntent nextButtonPendingIntent = PendingIntent.getBroadcast(context, 0, nextButtonIntent, 0);
        remoteViewsEnglish.setOnClickPendingIntent(R.id.nextButton, nextButtonPendingIntent);
        remoteViewsScience.setOnClickPendingIntent(R.id.nextButton, nextButtonPendingIntent);

        Intent soundButtonIntent = new Intent("SOUND_BUTTON");
        PendingIntent soundButtonPendingIntent = PendingIntent.getBroadcast(context, 0, soundButtonIntent, 0);
        remoteViewsEnglish.setOnClickPendingIntent(R.id.soundButton, soundButtonPendingIntent);

        Intent moreButtonIntent = new Intent("MORE_BUTTON");
        PendingIntent moreButtonPendingIntent = PendingIntent.getBroadcast(context, 0, moreButtonIntent, 0);
        remoteViewsEnglish.setOnClickPendingIntent(R.id.moreButton, moreButtonPendingIntent);
        remoteViewsScience.setOnClickPendingIntent(R.id.moreButton, moreButtonPendingIntent);

    }

    public void startNotification(){
        notificationManager.notify(0, notification);
    }

    public void startMediaPlayer(){
        mediaPlayer.start();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void onEnglishRemoteViews(){

        tag = "ENGLISH";

        g3MSQLite.insterStatisics(dateFormat.format(new Date()), "english", "notify");

        Cursor cursor = g3MSQLite.getEnglish();

        Random random = new Random();
        rowId = random.nextInt(cursor.getCount())+1;

        Cursor cursorQuestion = g3MSQLite.getEnglish(rowId);

        remoteViewsEnglish.setImageViewResource(R.id.icon, R.mipmap.g3m_icon);
        remoteViewsEnglish.setTextViewText(R.id.title, "黃金三分鐘");
        remoteViewsEnglish.setTextViewText(R.id.englishText, cursorQuestion.getString(1));
        remoteViewsEnglish.setTextViewText(R.id.chineseText, cursorQuestion.getString(3));
        remoteViewsEnglish.setTextViewText(R.id.exampleEnglishText, cursorQuestion.getString(5));
        remoteViewsEnglish.setTextViewText(R.id.exampleChineseText, cursorQuestion.getString(4));

        new Thread(mediaPlayerRunnable).start();

        notification.bigContentView = remoteViewsEnglish;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void onScienceRemoteViews(int type){

        if((type - 1) == 0){
            tag = "MATH";
            scienceType = 0;
            g3MSQLite.insterStatisics(dateFormat.format(new Date()), "math", "notify");
        }else{
            tag = "PHYSICS";
            scienceType = 1;
            g3MSQLite.insterStatisics(dateFormat.format(new Date()), "physics", "notify");
        }

        Cursor cursor = g3MSQLite.getScience(type - 1);
        Random random = new Random();
        rowId = random.nextInt(cursor.getCount())+1;
        Cursor cursorQuestion = g3MSQLite.getScience(rowId, type - 1);

        remoteViewsScience.setImageViewResource(R.id.icon, R.mipmap.g3m_icon);
        remoteViewsScience.setTextViewText(R.id.title, "黃金三分鐘");
        remoteViewsScience.setTextViewText(R.id.scienceText, cursorQuestion.getString(1));

        notification.bigContentView = remoteViewsScience;
    }

    private Runnable mediaPlayerRunnable = new Runnable() {
        @Override
        public void run() {

            Cursor cursorQuestion = g3MSQLite.getEnglish(rowId);
            String[] englishWord = cursorQuestion.getString(1).split(" ");
            String english = "";

            for(int i = 0 ; i < englishWord.length ; i++){
                english += englishWord[i] + "%20";
            }

            //mediaPlayer.release();
            mediaPlayer = new MediaPlayerLoadingTask(context).doInBackground(
                    "https://translate.google.com.tw/translate_tts?ie=UTF-8&q="
                            + english
                            +"&tl=en&total=1&idx=0&textlen=5&client=t&prev=input&sa=N");
        }
    };

    public int getRowId(){
        return rowId;
    }

    public void setNotificationDefaults(int defaults){
        notification.defaults = defaults;
    }

    public void removeNotification(){
        notificationManager.cancelAll();
    }

    public String getRemoteViewsTag(){
        return tag;
    }

    public int getScienceType() {
        return scienceType;
    }
}
