package com.sbosoft.cashpoint;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.telephony.SmsMessage;

public class SMSReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)) {
            SmsMessage[] messages = Telephony.Sms.Intents.getMessagesFromIntent(intent);
            for (SmsMessage message : messages) {
                String sender = message.getDisplayOriginatingAddress();
                String messageBody = message.getMessageBody();
                
                // Start service to read message
                Intent serviceIntent = new Intent(context, MessageListenerService.class);
                serviceIntent.putExtra("message", 
                    "Nouveau message de " + sender + ". " + messageBody);
                context.startService(serviceIntent);
            }
        }
    }
}