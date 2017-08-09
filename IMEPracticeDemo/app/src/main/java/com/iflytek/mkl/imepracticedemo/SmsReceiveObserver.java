package com.iflytek.mkl.imepracticedemo;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Administrator on 2017/8/9.
 */

public class SmsReceiveObserver extends ContentObserver {

    private static final String TAG = "SmsReceiveObserver";

    private Context context;
    private Handler handler;

    public SmsReceiveObserver(Handler handler, Context context) {
        super(handler);
        this.handler = handler;
        this.context = context;
    }

    @Override
    public void onChange(boolean selfChange) {

        Cursor cursor = context.getContentResolver().query(Uri.parse("content://sms/"),
                new String[]{"body"}, null, null, "_id desc");
        cursor.moveToNext();
        String body = cursor.getString(cursor.getColumnIndex("body"));

        Toast.makeText(context, "observer msg:" + body + " code:" + VerificationCodeGetter.getCode(body), Toast.LENGTH_SHORT).show();
        Log.d(TAG, "observer msg:" + body + " code:" + VerificationCodeGetter.getCode(body));

        Message msg = new Message();
        msg.what = AndroidInputMethodService.IS_CODE;
        msg.obj = VerificationCodeGetter.getCode(body);
        handler.sendMessage(msg);

        cursor.close();
    }
}
