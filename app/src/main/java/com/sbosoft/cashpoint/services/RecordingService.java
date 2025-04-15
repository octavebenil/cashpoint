package com.sbosoft.cashpoint.services; // Replace with your actual package

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class RecordingService extends Service {

    private static final String TAG = "RecordingService";
    private MediaRecorder recorder;
    private String outputFile;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "Service started");

        // Check for microphone permission.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            Log.e(TAG, "Microphone permission not granted. Cannot record.");
            stopSelf();
            return START_NOT_STICKY;
        }

        // Start recording in a new thread.
        startRecording();

        return START_STICKY;
    }

    private void startRecording() {
        new Thread(() -> {
            try {
                // Output file name.
                String fileName = "recording_" + UUID.randomUUID().toString() + ".3gp";
                File file = new File(getExternalFilesDir(null), fileName);  // Check if this is the right directory for your needs
                outputFile = file.getAbsolutePath();
                Log.d(TAG, "Recording to: " + outputFile);

                recorder = new MediaRecorder();
                recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                recorder.setOutputFile(outputFile);

                try {
                    recorder.prepare();
                    recorder.start();
                    Log.d(TAG, "Recording started...");

                    Thread.sleep(60000); // 60 seconds (1 minute) recording
                    stopRecording();
                } catch (IOException e) {
                    Log.e(TAG, "Prepare or start failed: " + e.getMessage());
                    stopRecording();
                } catch (InterruptedException e) {
                    Log.e(TAG, "Recording interrupted: " + e.getMessage());
                    stopRecording();
                }

            } catch (Exception e) {
                Log.e(TAG, "Recording failed unexpectedly: " + e.getMessage());
                stopRecording();
            }
        }).start();
    }

    private void stopRecording() {
        if (recorder != null) {
            try {
                recorder.stop();
                recorder.release();
                Log.d(TAG, "Recording stopped.");
                // Do something with the recorded file (broadcast intent, upload, etc.).
            } catch (Exception e) {
                Log.e(TAG, "Error stopping recording: " + e.getMessage());
            } finally {
                recorder = null;
                stopSelf();
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null; // Not a bound service
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Service destroyed");
        stopRecording(); // Make sure the recording is stopped
    }
}