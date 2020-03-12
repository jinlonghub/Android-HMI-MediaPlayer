package com.example.playerdemo;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.LayoutRes;
import androidx.appcompat.app.AppCompatActivity;

import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.security.Provider;
import java.util.List;

public class MediaProviderActivity extends AppCompatActivity implements View.OnClickListener {

    ComponentName component;
    Button mPrevious;
    Button mPlay;
    Button mNext;
    Button mPause;
    //static Cursor mCursor;

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
        final ListView mListView = this.findViewById(R.id.listView_playlist);
        //构建播放列表
        //mCursor = getContentResolver().query(MediaServiceProvider.MUSIC_URL, MediaServiceProvider.mCursorCols, "duration > 10000", null, null);
        //String[] list = new String[] {
        //       mCursor
        //};
        //使用Adapter绑定数据源：MediaServiceProvider_mCursorCols.
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                MediaServiceProvider.mCursorCols);
        mListView.setAdapter(adapter);
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
