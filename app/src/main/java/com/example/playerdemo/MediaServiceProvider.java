package com.example.playerdemo;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.provider.MediaStore;
import android.widget.Toast;

import java.io.IOException;

/**
 * Created by SJin2 on 2020/3/9.
 */
public class MediaServiceProvider extends Service {
    String[] mCursorCols = new String[] {
            "audio._id AS _id", // index must match IDCOLIDX below
            MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.MIME_TYPE, MediaStore.Audio.Media.ALBUM_ID,
            MediaStore.Audio.Media.ARTIST_ID, MediaStore.Audio.Media.DURATION};
    private MediaPlayer mMediaPlayer;
    private Cursor mCursor;
    private int mPlayPosition = 0;
    public static final String PLAY_ACTION = "com.tutor.music.PLAY_ACTION";
    public static final String PAUSE_ACTION = "com.tutor.music.PAUSE_ACTION";
    public static final String NEXT_ACTION = "com.tutor.music.NEXT_ACTION";
    public static final String PREVIOUS_ACTION = "com.tutor.music.PREVIOUS_ACTION";

    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mMediaPlayer = new MediaPlayer();
        //通过一个URI可以获取所有音频文件
        Uri MUSIC_URL = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        //默认大于10秒的可以看作是音乐播放
        mCursor = getContentResolver().query(MUSIC_URL, mCursorCols, "duration > 10000", null, null);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        String action = intent.getAction();
        if(action.equals(PLAY_ACTION)){
            play();
        }else
        if(action.equals(PAUSE_ACTION)){
            pause();
        }else
        if(action.equals(NEXT_ACTION)){
            next();
        }else
        if(action.equals(PREVIOUS_ACTION)){
            previous();
        }
        return 1;
    }


    //play the music
    public void play() {
        inite();
    }

    //暂停时，结束服务
    public void pause() {
        stopSelf();
    }

    //上一首
    public void previous() {
        if (mPlayPosition == 0) {
            mPlayPosition = mCursor.getCount() - 1;
        } else {
            mPlayPosition--;
        }
        inite();
    }

    //下一首
    public void next() {
        if (mPlayPosition == mCursor.getCount() - 1) {
            mPlayPosition = 0;
        } else {
            mPlayPosition++;
        }
        inite();
    }

    public void inite() {
        mMediaPlayer.reset();
        String dataSource = getDateByPosition(mCursor, mPlayPosition);
        String info = getInfoByPosition(mCursor, mPlayPosition);
        //用Toast显示歌曲信息
        Toast.makeText(getApplicationContext(), info, Toast.LENGTH_SHORT).show();
        try {
            mMediaPlayer.setDataSource(dataSource);
            mMediaPlayer.prepare();
            mMediaPlayer.start();
        } catch (IllegalArgumentException e1) {
            e1.printStackTrace();
        } catch (IllegalStateException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    //根据位置来获取歌曲位置
    public String getDateByPosition(Cursor c, int position){
        c.moveToPosition(position);
        int dataColumn = c.getColumnIndex(MediaStore.Audio.Media.DATA);
        String data = c.getString(dataColumn);
        return data;
    }

    //获取当前播放歌曲演唱者及歌名
    public
    String getInfoByPosition(Cursor c,int position){
        c.moveToPosition(position);
        int titleColumn = c.getColumnIndex(MediaStore.Audio.Media.TITLE);
        int artistColumn = c.getColumnIndex(MediaStore.Audio.Media.ARTIST);
        String info = c.getString(artistColumn)+" "  + c.getString(titleColumn);
        return info;
    }

    //服务结束时要释放MediaPlayer
    public void onDestroy() {
        super.onDestroy();
        mMediaPlayer.release();
    }

}
