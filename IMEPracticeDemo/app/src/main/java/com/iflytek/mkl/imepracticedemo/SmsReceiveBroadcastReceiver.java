package com.iflytek.mkl.imepracticedemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

public class SmsReceiveBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = "smscode";
    private MessageListener messageListener;

    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle bundle = intent.getExtras();
        SmsMessage msg = null;
        if (null != bundle) {
            Object[] smsObj = (Object[]) bundle.get("pdus");
            for (Object object : smsObj) {
                msg = SmsMessage.createFromPdu((byte[]) object);

                Log.d(TAG, "number:" + msg.getOriginatingAddress()
                        + "   body:" + msg.getDisplayMessageBody() + "  time:"
                        + msg.getTimestampMillis());

                String code = VerificationCodeGetter.getCode(msg.getMessageBody());
                if (code != null) {
                    Log.d(TAG, "receive SMS code is : " + code);
                }
                if(!TextUtils.isEmpty(code) &&messageListener != null){
                    messageListener.onReceiveVerificationCode(code);
//                    abortBroadcast();
                }

            }
        }
    }

    public interface MessageListener{
        public void onReceiveVerificationCode(String code);
    }

    public void setMessageListener(MessageListener listener){
        this.messageListener = listener;
    }
}
