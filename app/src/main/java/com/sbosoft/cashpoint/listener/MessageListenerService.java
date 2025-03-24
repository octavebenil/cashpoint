package com.sbosoft.cashpoint.listener;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;
import java.util.Locale;

public class MessageListenerService extends Service {
    private TextToSpeech tts;

    @Override
    public void onCreate() {
        super.onCreate();
        initializeTTS();
    }

    private void initializeTTS() {
        tts = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                tts.setLanguage(Locale.FRENCH);
            }
        });
    }

    public void readMessage(String message) {
        if (tts != null) {
            tts.speak(message, TextToSpeech.QUEUE_FLUSH, null, null);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }
}