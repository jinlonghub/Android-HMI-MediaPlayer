package com.example.playerdemo;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

/**
 * Created by SJin2 on 2020/3/3.
 */
public class ExampleActivity extends AppCompatActivity {

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final int REQUEST_AUDIO_SETTINGS = 2;
    private static String[] PERMISSIONS_STORAGE = {
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.MODIFY_AUDIO_SETTINGS,
            Manifest.permission.WAKE_LOCK
    };

    public static void verifyStoragePermissions(Activity activity) {
        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(activity, "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
            }
            permission = ActivityCompat.checkSelfPermission(activity, "android.permission.MODIFY_AUDIO_SETTINGS");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_AUDIO_SETTINGS);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /*
    private IBinder mbinder = null;
    private ServiceConnection conn = new ServiceConnection() {
        //@Override
        public void onServiceConnected(ComponentName name, IBinder ibinder) {
            Log.d("binder","client onServiceConnected called");
            mbinder = ibinder;
        }
        public void onServiceDisconnected(ComponentName name) {
            //
        }
    };
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example);
        verifyStoragePermissions(this);

        final Intent receiveIntent = this.getIntent();
        if(receiveIntent != null){
            Log.d ("StevenLog","Get click intent to switch to ExampleActivity.");
        }

        final Button button2Home = this.findViewById(R.id.button_2home);
        button2Home.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //setResult(4321, receiveIntent);
                finish();
            }
        });
    }

}
