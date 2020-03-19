package com.example.playerdemo;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.LayoutRes;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.MalformedParameterizedTypeException;
import java.security.Provider;
import java.util.ArrayList;
import java.util.List;

public class MediaProviderActivity extends AppCompatActivity implements View.OnClickListener {

    ComponentName component;
    Button mPrevious;
    Button mPlay;
    Button mNext;
    Button mPause;
    //static Cursor mCursor;
    static ListView mListView;
    List<String> itemsList = new ArrayList<String>();
    private Handler asyncHandler = null;

    private IBinder mbinder = null;
    private ServiceConnection conn = new ServiceConnection() {
        //@Override
        public void onServiceConnected(ComponentName name, IBinder ibinder) {
            Log.d("binder","Provider_client onServiceConnected called");
            mbinder = ibinder;
        }
        public void onServiceDisconnected(ComponentName name) {
            /*Do nothing*/
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_provider);

        final Intent receiveIntent = this.getIntent();
        if(receiveIntent != null){
            Log.d ("StevenLog","Get click intent to switch to ProviderActivity.");
        }

        /*定义ReturnBack按键*/
        final Button button2Home = this.findViewById(R.id.button_P2home);
        button2Home.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //setResult(2211, receiveIntent);
                finish();
            }
        });

        /*调用ListView_显示播放列表*/
        displayListView();

        /*定义播放按键_使用provider提供播放源*/
        component = new ComponentName(this, MediaServiceProvider.class);
        //component = new ComponentName("com.example.playerdemo", "com.example.playerdemo.MediaServiceProvider");
        //View view = this.getLayoutInflater().inflate(R.layout.activity_media_provider, null);
        mPrevious = this.findViewById(R.id.previous);
        mPlay = this.findViewById(R.id.play);
        mNext = this.findViewById(R.id.next);
        mPause = this.findViewById(R.id.pause);
            //Provider_Button绑定
        setupProviderViews();

        Log.d("Steven Log", "Start to connect with MediaServiceProvider.");
        //startService(new Intent(this, MediaServiceProvider.class));
        //Intent intent = new Intent(MediaProviderActivity.this, MediaServiceProvider.class);
        //bindService(intent, conn, Context.BIND_AUTO_CREATE);
    }



    /*定义ListView_显示播放列表*/
    private void displayListView(){
        mListView = this.findViewById(R.id.listView_playlist);

        /*方法一：使用音频路径作，作为数据源：MediaServiceProvider.mCursorCols
        //构建播放列表
        mCursor = getContentResolver().query(MediaServiceProvider.MUSIC_URL, MediaServiceProvider.mCursorCols, "duration > 10000", null, null);
        String[] list = new String[] {
               mCursor
        };
        //使用Adapter绑定数据源：MediaServiceProvider_mCursorCols.
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                MediaServiceProvider.mCursorCols);
        */

        /*方法二：使用子线程+Handler，构建播放列表数据源，并在UI上显示
        new Thread() {
            @Override
            public void run() {
                super.run();
                Cursor lisCursor = getContentResolver().query(MediaServiceProvider.MUSIC_URL, MediaServiceProvider.mCursorCols, "duration > 10000", null, null);
                //ListitemAsyncTask.itemsAsyncTask(MediaServiceProvider.mCursor, itemsList);
                ListitemAsyncTask.itemsAsyncTask(lisCursor, itemsList);
                Log.d("Steven Log", "Async Task start to run.");
                asyncHandler.sendMessage(asyncHandler.obtainMessage(0, itemsList));
            }
        }.start();
        asyncHandler = new Handler() {
            public void handleMessage(Message msg) {
                if (msg.what == 0) {
                    Log.d("Steven Log", "handler had been called.");
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(MediaProviderActivity.this, android.R.layout.simple_list_item_1, itemsList);
                    mListView.setAdapter(adapter);
                }
            }
        };
         */

        /*方法三：使用AsyncTask,构建播放列表数据源*/
        Cursor lisCursor = getContentResolver().query(MediaServiceProvider.MUSIC_URL, MediaServiceProvider.mCursorCols,
                                                      "duration > 10000", null, null);
        //ListitemAsyncTask.itemsAsyncTask(lisCursor, itemsList);
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(MediaProviderActivity.this, android.R.layout.simple_list_item_1, itemsList);
        //mListView.setAdapter(adapter);
        new PlaylistAsyncTask().execute(itemsList);
    }

    public class PlaylistAsyncTask extends AsyncTask<List<String>, Void, List<String>> {
        @Override
        protected List<String> doInBackground(List<String>... params){
            Cursor lisCursor = getContentResolver().query(MediaServiceProvider.MUSIC_URL, MediaServiceProvider.mCursorCols,
                    "duration > 10000", null, null);
            try {
                Log.d("Steven Log", "itemsAsyncTask had been called.");
                while (lisCursor.moveToNext()) {
                    Log.d("Steven Log", "doInBackground had been called.");
                    int titleColumn = lisCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
                    int artistColumn = lisCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
                    String item = lisCursor.getString(artistColumn) + " _ " + lisCursor.getString(titleColumn);
                    Log.d("Steven Log", "item is:" + item);
                    itemsList.add(item);
                    Log.d("Steven Log", "itemList is:" + itemsList);
                }
            } catch (MalformedParameterizedTypeException e) {
                e.printStackTrace();
            }
            return itemsList;
        }
        @Override
        protected void onPostExecute(List<String> itemsList) {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(MediaProviderActivity.this, android.R.layout.simple_list_item_1, itemsList);
            MediaProviderActivity.mListView.setAdapter(adapter);
        }
    }



    public void setupProviderViews(){
        mPrevious.setOnClickListener(this);
        mPlay.setOnClickListener(this);
        mNext.setOnClickListener(this);
        mPause.setOnClickListener(this);
        Log.d("Steven Log", "ProviderViews had setup success.");
    }

    @Override    //按钮点击事件响应
    public void onClick(View v) {
        if(v == mPrevious){
            Intent mIntent = new Intent(MediaServiceProvider.PREVIOUS_ACTION);
            mIntent.setComponent(component);
            startService(mIntent);
        }else if(v == mPlay){
            Intent mIntent = new Intent(MediaServiceProvider.PLAY_ACTION);
            mIntent.setComponent(component);
            startService(mIntent);
        }else if(v == mNext){
            Intent mIntent = new Intent(MediaServiceProvider.NEXT_ACTION);
            mIntent.setComponent(component);
            startService(mIntent);
        }else{
            Intent mIntent = new Intent(MediaServiceProvider.PAUSE_ACTION);
            mIntent.setComponent(component);
            startService(mIntent);
        }
    }

}
