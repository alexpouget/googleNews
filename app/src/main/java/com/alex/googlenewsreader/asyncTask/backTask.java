package com.alex.googlenewsreader.asyncTask;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Icon;
import android.os.AsyncTask;
import android.support.v7.app.NotificationCompat;
import android.widget.TextView;

import com.alex.googlenewsreader.MainActivity;
import com.alex.googlenewsreader.R;
import com.alex.googlenewsreader.bdd.Database;

import java.util.Calendar;
import java.util.Locale;

/**
 * Created by alex on 09/02/2016.
 */
public class BackTask extends AsyncTask<Void,Long,String> {
    private Context context;
    private Database db;

    protected void onPreExecute() {
        super.onPreExecute();
    }

    public BackTask(Context c,Database dbi) {
        context = c;
        db = dbi;
    }
    @Override
    protected String doInBackground(Void... params) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                   if(db.getNewNews()){
                       updatedNews();
                   }
                    try {
                        Thread.sleep(60000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).run();
        return "FINISH";
    }


    public void updatedNews() {

        context.sendBroadcast(new Intent(("N3W5")));
    }
}
