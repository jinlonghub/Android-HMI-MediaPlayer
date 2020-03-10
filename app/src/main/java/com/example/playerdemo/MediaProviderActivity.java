package com.example.playerdemo;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.LayoutRes;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MediaProviderActivity extends AppCompatActivity implements View.OnClickListener {

    ComponentName component;
    Button mPrevious;
    Button mPlay;
    Button mNext;
    Button mPause;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_provider);

        final Intent receiveIntent = this.getIntent();
        if(receiveIntent != null){
            Log.d ("StevenLog","Get click intent to switch to ProviderActivity.");
        }

        final Button button2Home = this.findViewById(R.id.button_P2home);
        button2Home.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //setResult(2211, receiveIntent);
                finish();
            }
        });

        component = new ComponentName(this, MediaServiceProvider.class);
        //component = new ComponentName("com.example.playerdemo", "com.example.playerdemo.MediaServiceProvider");
        //View view = this.getLayoutInflater().inflate(R.layout.activity_media_provider, null);
        mPrevious = this.findViewById(R.id.previous);
        mPlay = this.findViewById(R.id.play);
        mNext = this.findViewById(R.id.next);
        mPause = this.findViewById(R.id.pause);

        //Provider_Button绑定
        setupProviderViews();
    }

    public void setupProviderViews(){
        mPrevious.setOnClickListener(this);
        mPlay.setOnClickListener(this);
        mNext.setOnClickListener(this);
        mPause.setOnClickListener(this);
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
