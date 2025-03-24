package com.sbosoft.cashpoint.services;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Locale;

public class VoiceAssistantService extends AccessibilityService {
    private TextToSpeech tts;
    private SpeechRecognizer speechRecognizer;
    private boolean isListening = false;

    @Override
    public void onCreate() {
        super.onCreate();
        initializeTTS();
        initializeSpeechRecognizer();
    }

    private void initializeTTS() {
        tts = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                tts.setLanguage(Locale.FRENCH);
            }
        });
    }

    private void initializeSpeechRecognizer() {
        if (SpeechRecognizer.isRecognitionAvailable(this)) {
            speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
            speechRecognizer.setRecognitionListener(new RecognitionListener() {
                @Override
                public void onReadyForSpeech(Bundle bundle) {
                    isListening = true;
                }

                @Override
                public void onResults(Bundle results) {
                    ArrayList<String> matches = results.getStringArrayList(
                        SpeechRecognizer.RESULTS_RECOGNITION);
                    if (matches != null && !matches.isEmpty()) {
                        processVoiceCommand(matches.get(0));
                    }
                    startListening(); // Continue listening
                }

                // Implement other RecognitionListener methods...
            });
            startListening();
        }
    }

    private void startListening() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                       RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "fr-FR");
        speechRecognizer.startListening(intent);
    }

    private void processVoiceCommand(String command) {
        command = command.toLowerCase();
        if (command.contains("appeler")) {
            // Extract name and make phone call
            String name = command.replace("appeler", "").trim();
            makePhoneCall(name);
        } else if (command.contains("message")) {
            // Handle message command
            sendMessage(command);
        }
    }

    private void makePhoneCall(String name) {
        // Implement phone call logic
    }

    private void sendMessage(String command) {
        // Implement message sending logic
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (event.getEventType() == AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED) {
            // Handle notifications (e.g., new messages)
            String text = event.getText().toString();
            speak(text);
        }
    }

    private void speak(String text) {
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
    }

    @Override
    public void onInterrupt() {
        if (tts != null) {
            tts.stop();
        }
    }

    @Override
    public void onDestroy() {
        if (tts != null) {
            tts.shutdown();
        }
        if (speechRecognizer != null) {
            speechRecognizer.destroy();
        }
        super.onDestroy();
    }
}