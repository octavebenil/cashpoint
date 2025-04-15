package com.sbosoft.cashpoint; // Replace with your actual package

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import androidx.activity.ComponentActivity;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;

import com.sbosoft.cashpoint.services.RecordingService;

public class MainActivity extends ComponentActivity {

    private static final String TAG = "MainActivity";
    private ActivityResultLauncher<String> requestPermissionLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // ... other onCreate logic ...

        requestPermissionLauncher =
                registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                    if (isGranted) {
                        Log.i(TAG, "Microphone permission granted");
                        startRecordingService();
                    } else {
                        Log.w(TAG, "Microphone permission denied");
                        // Explain the feature unavailability to the user.
                    }
                });

        checkMicrophonePermission();
    }

    private void checkMicrophonePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "Microphone permission already granted");
            startRecordingService();
        } else if (shouldShowRequestPermissionRationale(Manifest.permission.RECORD_AUDIO)) {
            // Provide explanation to the user asynchronously.  Replace with your UI message.
            Log.i(TAG, "Showing microphone permission rationale");
            // Show a dialog/message, and then call:
            // requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO);
        } else {
            Log.i(TAG, "Requesting microphone permission");
            requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO);
        }
    }

    private void startRecordingService() {
        Intent serviceIntent = new Intent(this, RecordingService.class);
        startService(serviceIntent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent serviceIntent = new Intent(this, RecordingService.class);
        stopService(serviceIntent);
    }
}