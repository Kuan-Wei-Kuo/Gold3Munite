package com.kuo.task;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;

import java.io.IOException;

public class MediaPlayerLoadingTask extends AsyncTask<String, Integer, MediaPlayer> {

    private Context context;

    public MediaPlayerLoadingTask(Context context){
        this.context = context;
    }

    @Override
    public MediaPlayer doInBackground(String... strings) {

        String songUrl = strings[0];
        MediaPlayer mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType (AudioManager.STREAM_MUSIC);

        try {
            mediaPlayer.setDataSource(context, Uri.parse(songUrl));
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return mediaPlayer;
    }
}
