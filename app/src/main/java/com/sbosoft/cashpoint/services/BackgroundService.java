package com.sbosoft.cashpoint.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class BackgroundService extends Service {

    private static final String TAG = "BackgroundService";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "Service started");

        // Run your background task in a separate thread to avoid blocking the main thread.
        new Thread(new Runnable() {
            @Override
            public void run() {
                // Replace this with your actual background work.
                while (true) {
                    try {
                        // Simulate some work every 5 seconds.
                        Thread.sleep(5000);
                        Log.d(TAG, "Doing some work in the background...");
                        // Call your background processing function here:
                        // yourBackgroundTask()
                    } catch (InterruptedException e) {
                        // Handle the exception if your service is interrupted.
                        Log.e(TAG, "Service interrupted: " + e.getMessage());
                        break; // Exit the loop if interrupted
                    }
                }
            }
        }).start();

        // Indicate that the service should be restarted if the system kills it.
        return START_STICKY;
    }

    // You won't be binding to this service, so return null for the binder.
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Service destroyed");
    }
}
