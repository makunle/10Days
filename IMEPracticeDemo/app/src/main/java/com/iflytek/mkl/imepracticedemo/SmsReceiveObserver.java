package com.iflytek.mkl.imepracticedemo;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;

/**
 * Created by Administrator on 2017/8/9.
 */

public class SmsReceiveObserver extends ContentObserver {

    private Context context;
    private Handler handler;

    public SmsReceiveObserver(Handler handler, Context context) {
        super(handler);
        this.handler = handler;
        this.context = context;
    }

    @Override
    public void onChange(boolean selfChange) {
        Cursor cursor = context.getContentResolver().query(
                Uri.parse("content://sms/inbox"), null, null, null, null
        );
        cursor.moveToNext();
        String body = cursor.getString(cursor.getColumnIndex("body"));

        Message msg = new Message();
        msg.what = AndroidInputMethodService.IS_CODE;
        msg.obj = VerificationCodeGetter.getCode(body);
        handler.sendMessage(msg);

        cursor.close();
    }
}
