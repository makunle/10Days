package com.iflytek.mkl.imepracticedemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

public class SmsReceiveBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = "smscode";
    private MessageListener messageListener;

    @Override
    public void onReceive(Context context, Intent intent) {

        SmsMessage[] messages = null;
        if (Build.VERSION.SDK_INT >= 19) {
            messages = android.provider.Telephony.Sms.Intents.getMessagesFromIntent(intent);
            if (messages == null) return;
        } else {
            messages = getSmsUnder19(intent);
            if (messages == null) return;
        }

        for (SmsMessage msg : messages) {

            Log.d(TAG, "number:" + msg.getOriginatingAddress()
                    + "   body:" + msg.getDisplayMessageBody() + "  time:"
                    + msg.getTimestampMillis());
            String code = VerificationCodeGetter.getCode(msg.getMessageBody());
            if (code != null) {
                Log.d(TAG, "receive SMS code is : " + code);
            }
            if (!TextUtils.isEmpty(code) && messageListener != null) {
                messageListener.onReceiveVerificationCode(code);
                abortBroadcast();
            }
        }
    }

    public interface MessageListener {
        public void onReceiveVerificationCode(String code);
    }

    public void setMessageListener(MessageListener listener) {
        this.messageListener = listener;
    }

    private SmsMessage[] getSmsUnder19(Intent intent) {
        SmsMessage[] messages;
        Bundle bundle = intent.getExtras();
        // 相关链接:https://developer.android.com/reference/android/provider/Telephony.Sms.Intents.html#SMS_DELIVER_ACTION
        Object[] pdus = (Object[]) bundle.get("pdus");

        if ((pdus == null) || (pdus.length == 0)) {
            return null;
        }

        messages = new SmsMessage[pdus.length];
        for (int i = 0; i < pdus.length; i++) {
            messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
        }
        return messages;
    }
}
