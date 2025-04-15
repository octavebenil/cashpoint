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
            // sdk<19
            Object[] pdus = (Object[]) intent.getExtras().get("pdus");
            SmsMessage[] messages = new SmsMessage[pdus.length];

            for (int i = 0; i < pdus.length; i++) {
                messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
            }

            // Loop through each message and process it
            for (SmsMessage message : messages) {
                String sender = message.getOriginatingAddress();
                String content = message.getMessageBody();

                // You can now process the message or display it
                // For example, log the message content
                System.out.println("Sender: " + sender);
                System.out.println("Message: " + content);
            }
            //sdk >19
//            SmsMessage[] messages = Telephony.Sms.Intents.getMessagesFromIntent(intent);
//            for (SmsMessage message : messages) {
//                String sender = message.getDisplayOriginatingAddress();
//                String messageBody = message.getMessageBody();
//
//                System.out.println("Nouveau message de " + sender + ". " + messageBody);
//
//                // Start service to read message
////                Intent serviceIntent = new Intent(context, MessageListenerService.class);
////                serviceIntent.putExtra("message",
////                    "Nouveau message de " + sender + ". " + messageBody);
////                context.startService(serviceIntent);
//            }
        }
    }
}