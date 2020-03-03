package com.example.playerdemo;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Environment;
import android.os.Parcel;
import android.renderscript.ScriptGroup;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

/**
 * Created by SJin2 on 2020/2/22.
 */
public class MyBinder extends Binder {
    private Context ctx;
    public MyBinder(Context cx) {
        ctx = cx;
    }

    private MediaPlayer mMediaPlayer = null;

    @Override
    public boolean onTransact(int code, Parcel data, Parcel replay, int flags) {
        //Toast.makeText(ctx, "onTransact triggered...", Toast.LENGTH_LONG).show();
        //File file = new File(Environment.getExternalStorageDirectory() + "/music/123.mp3");
//        if (file == null) {

//            //file = new File(Environment.getExternalStorageDirectory() + "/music/123.mp3");
//            file = new File(data.readString());
//        }
        //file = new File(data.readString());

        Log.d("binder", "onTransact tirggered ...");


        if(1 == code ) { // play
            Log.d("binder", "play");
            File file = new File(data.readString());
            if (file.exists()) {
                Log.d("mediaPlayer", "file is " + file.getAbsolutePath());
                if (mMediaPlayer != null) {
                    mMediaPlayer.stop();
                }
                mMediaPlayer = MediaPlayer.create(ctx, Uri.parse(file.getAbsolutePath()));
                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mMediaPlayer.start();
            } else {
                Log.d("binder", file.getAbsolutePath() + " not exists");
            }
        } else if (0 == code) { // pause
            Log.d("binder", "pause");
            if (mMediaPlayer != null) {
                if (mMediaPlayer.isPlaying()) {
                    mMediaPlayer.pause();
                }
            }
        } else {// resume
            //file = new File(data.readString());
            Log.d("binder", "resume");
            if (mMediaPlayer != null && !mMediaPlayer.isPlaying()) {
                mMediaPlayer.start();
            }
        }
        return true;
    }

}
