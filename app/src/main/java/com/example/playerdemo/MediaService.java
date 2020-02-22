package com.example.playerdemo;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

public class MediaService extends Service {
    public MediaService() {
    }
    private IBinder mb;

    @Override public void onCreate(){
        mb = new myBinder(this);
        Toast.makeText(this,"MediaService Created, Binder Created", Toast.LENGTH_LONG).show();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");
        Toast.makeText(this, "onBind， Server Binder Returned", Toast.LENGTH_LONG).show();
        return mb;
    }

   // @Override
//    public void onStart() {
//        mb = new myBinder(this);
//    }
    @Override
    public int onStartCommand(Intent intent , int flags, int startId) {
        //Toast.makeText(this, "onStartCommand， Server Binder Created", Toast.LENGTH_LONG).show();
        //mb = new myBinder(this);
        return super.onStartCommand(intent, flags, startId);
    }
}
