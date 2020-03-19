package com.example.playerdemo;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by SJin2 on 2020/2/22.
 */
public class GetFileUtils {
    public static final String FOLDER_PATH = Environment.getExternalStorageDirectory() + "/music";

    private static GetFileUtils gfu;

    private void GetFilesUtils() {
    }

    public static synchronized GetFileUtils getInstance() {
        if (gfu == null) {
            gfu = new GetFileUtils();
        }
        return gfu;
    }

    public List<String> getMusicFiles(File path) {
        List<String> list = new ArrayList<String>();
        if (path.isDirectory()) {
            //List<String> list = new ArrayList<String>;
            File[] files = path.listFiles();
            if (files != null) {
                for (int i = 0; i < files.length; i++) {
                    if (files[i].getAbsolutePath().substring(files[i].getAbsolutePath().indexOf('.') + 1).contentEquals("mp3")) {
                        list.add(files[i].getAbsolutePath());
                    }
                }
            }
        }
        return list;
    }

    public static boolean fileIsExists(String strFile) {
        try {
            File f = new File(strFile);
            if (!f.exists()) {
                return false;
            }
        }
        catch (Exception e ) {
            return  false;
        }
        return true;
    }
}
