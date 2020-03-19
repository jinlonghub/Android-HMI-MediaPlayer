package com.example.playerdemo;

import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ListView;
import java.util.List;
import java.util.ArrayList;

/**
 * Created by SJin2 on 2020/3/17.
 */
public class ListitemAsyncTask {
    public ListitemAsyncTask () {}

    public static List<String> itemsAsyncTask(Cursor c, List<String> itemsList) {
        Log.d("Steven Log", "itemsAsyncTask had been called.");
        while (c.moveToNext()) {
            Log.d("Steven Log", "itemsAsyncTask's while looper had been called.");
            String path = c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
            //if (GetFileUtils.fileIsExists(path)) {
            //    continue;
            //}

            int titleColumn = c.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int artistColumn = c.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            String item = c.getString(artistColumn) + "_" + c.getString(titleColumn);
            Log.d("Steven Log", "item is:" + item);
            Log.d("Steven Log", item);
            itemsList.add(item);
            Log.d("Steven Log", "itemList is:" + itemsList);
        }
        return itemsList;
    }
}
