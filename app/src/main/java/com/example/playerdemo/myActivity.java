package com.example.playerdemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.util.List;

public class MyActivity extends AppCompatActivity{

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
    private boolean playing = false;
    //private String filePlay = null;
    private boolean onCreate = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playing);
        verifyStoragePermissions(this);

        /*
        Intent intentTmp = this.getIntent();
        if(intentTmp != null){
            Log.d("StevenLog","Get click intent to play music.");
        }
        */

        final Button button_playing = this.findViewById(R.id.button_playing);
        button_playing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(this,"button 101", Toast.LENGTH_LONG).show();
                if (!playing) {//resume
                    Parcel data = Parcel.obtain();
                    Parcel reply = Parcel.obtain();
                    data.writeString("Resume");
                    try {
                        mbinder.transact(2, data, reply, 0);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    playing = true;
                    button_playing.setText("Pause");
                    //(Button)(v.findViewById(R.id.button_playing)).setText;
                } else { //pause
                    Parcel data = Parcel.obtain();
                    Parcel reply = Parcel.obtain();
                    data.writeString("Pause");
                    try {
                        mbinder.transact(0, data, reply, 0);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    playing = false;
                    button_playing.setText("Resume");
                }
            }
        });

        button_playing.setEnabled(false);

        Spinner mySpinner = (Spinner) findViewById(R.id.spinner);
        GetFileUtils gfu = GetFileUtils.getInstance();
        File file = new File(GetFileUtils.FOLDER_PATH);
        List<String> list = gfu.getMusicFiles(file);
        Log.d("activity", list.get(0));

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, list);
        dataAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        mySpinner.setAdapter(dataAdapter);
        mySpinner.setSelection(0);
        mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!onCreate) {
                    Parcel data = Parcel.obtain();
                    Parcel reply = Parcel.obtain();
                    data.writeString(parent.getItemAtPosition(position).toString());
                    try {
                        mbinder.transact(1, data, reply, 0);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    button_playing.setEnabled(true);
                    playing = true;
                } else {
                    button_playing.setEnabled(false);
                    onCreate = false;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                /* do nothing */
            }
        });

        final Button button_switch = this.findViewById(R.id.button_switch);
        button_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("StevenLog", "click switch button.");
                Intent intent = new Intent(MyActivity.this, ExampleActivity.class);
                startActivityForResult(intent, 1234);
            }
        });

        //Button to jump to Provider.
        final Button button_provider = this.findViewById(R.id.button_provider);
        button_provider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("StevenLog", "click switch button to jump to provider.");
                Intent intent = new Intent(MyActivity.this, MediaProviderActivity.class);
                startActivityForResult(intent, 1122);
            }
        });


        startService(new Intent(this, MediaService.class));
        Intent intent = new Intent(MyActivity.this, MediaService.class);
        bindService(intent, conn, Context.BIND_AUTO_CREATE);
    }


    /*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (resultCode == 4321) {
            Toast.makeText(this, resultCode, Toast.LENGTH_LONG).show();
        }
        //Intent mIntent = new Intent(intent, MyActivity.this);
        //super.onActivityResult(requestCode, resultCode, mIntent);
    }
     */


}



