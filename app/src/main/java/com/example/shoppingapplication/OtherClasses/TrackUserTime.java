package com.example.shoppingapplication.OtherClasses;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.example.shoppingapplication.Database.GroceryItem;
import com.example.shoppingapplication.Database.Utils;

public class TrackUserTime extends Service {

    private final IBinder binder = new LocalBinder();
    private int seconds = 0;
    private boolean shouldFinish;
    private GroceryItem item;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        trackTime();
        return binder;
    }

    private void trackTime() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!shouldFinish) {
                    try {
                        Thread.sleep(1000);
                        seconds++;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
    }

    public void setItem(GroceryItem item) {
        this.item = item;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        shouldFinish = true;
        int minutes = seconds / 60;
        if (minutes > 0) {
            if (item != null) {
                Utils.changeUserPoint(this, item, minutes);
            }

        }
    }

    public class LocalBinder extends Binder {
        public TrackUserTime getService() {
            return TrackUserTime.this;
        }
    }
}
