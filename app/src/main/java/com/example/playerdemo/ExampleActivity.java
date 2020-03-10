package com.example.playerdemo;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.MediaStore;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

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

        /* 功能：TextView显示超链接：textView to display autoLink. */
        String html = "百度一下： \n";
        html += "http://www.baidu.com";
        //TextView mTextView = new TextView(this);
        //mTextView.setText(html);
        //mTextView.setAutoLinkMask(Linkify.ALL);
        //mTextView.setMovementMethod(LinkMovementMethod.getInstance());
        final TextView mTextView = this.findViewById(R.id.textView_link);
        mTextView.setText(html);

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

        /* 功能：Button点击时，改变editText的文字为"Type in here:" */
        final EditText editText = this.findViewById(R.id.editText_change);
        final String tmp = "Type in here:";
        final Button button_playlist = this.findViewById(R.id.button_playlist);
        button_playlist.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                editText.setText(tmp);
                button_playlist.setText("Playing");
            }
        });

         /* 功能：CheckBox */
        final CheckBox checkBox_1 = this.findViewById(R.id.checkBox_1);
        final CheckBox checkBox_2 = this.findViewById(R.id.checkBox_2);
        checkBox_1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked == true){
                    checkBox_1.setText("CheckBox select 周杰伦！");
                }
            }
        });
        checkBox_2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked == true){
                    checkBox_2.setText("CheckBox select 毛不易！");
                }
            }
        });

        /* 功能：RadioGroup & RadioButton */
        final RadioGroup radioGroup = this.findViewById(R.id.radioGroup_1);
        final RadioButton radioButton_1 = this.findViewById(R.id.radioButton_1);
        final RadioButton radioButton_2 = this.findViewById(R.id.radioButton_2);
        final RadioButton radioButton_3 = this.findViewById(R.id.radioButton_3);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == radioButton_1.getId()){
                    radioButton_1.setText("RadioButton DJ had been select.");
                }else if(checkedId == radioButton_2.getId()){
                    radioButton_2.setText("RadioButton 电音 had been select.");
                }else if(checkedId == radioButton_3.getId()){
                    radioButton_3.setText("RadioButton 爵士 had been select.");
                }
            }
        });

        Button buttonProvider = this.findViewById(R.id.button_provider);
        buttonProvider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentResolver contentResolver = getContentResolver();
                Cursor tmpCursor = contentResolver.query(Uri.parse("content://com.example.playerdemo.myprovider"),null,null,null,null);


            }
        });
    }

}
